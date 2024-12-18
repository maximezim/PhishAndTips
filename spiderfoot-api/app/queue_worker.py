import threading
import time
import uuid
import json
import subprocess
import os
import re

from app.database import SessionLocal
from app.models import SpiderfootScan

# A simple in-memory queue
scan_queue = []

def fetch_scan_data(spiderfoot_scan_id: str):
    # Prepare the data command
    data_commands = f"data {spiderfoot_scan_id}\nexit\n"
    data_cmd = [
        "python3",
        "/opt/spiderfoot/sfcli.py",
        "-s", "http://spiderfoot:5001"
    ]

    # Run the data command
    data_process = subprocess.Popen(
        data_cmd,
        stdin=subprocess.PIPE,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True
    )
    data_stdout, data_stderr = data_process.communicate(data_commands)

    if data_process.returncode != 0:
        return {"error": f"Error fetching data: {data_stderr.strip()}"}

    # The expected output format is something like:
    # sf> data 02B667A7
    # Data                                                                           Type
    # ------------------------------------------------------------------------------+------------------------
    # Ultimate Guitar (Category: hobby)                                              | ACCOUNT_EXTERNAL_OWNED
    # https://www.ultimate-guitar.com/u/bastub57                                     | ACCOUNT_EXTERNAL_OWNED
    # bastub57                                                                       | USERNAME
    # bastub57@gmail.com                                                             | ROOT
    # bastub57@gmail.com                                                             | EMAILADDR

    lines = data_stdout.splitlines()
    results = []

    # Find the index of the separator line which typically contains '-' and a '+'
    sep_index = None
    for i, line in enumerate(lines):
        # Remove spaces and check if line matches the expected pattern of a separator
        # Typically something like: "----------------------------------------------------------------------+------------------------"
        if re.match(r"^-+(\+)-+$", line.replace(' ', '')):
            sep_index = i
            break

    if sep_index is None:
        # No separator line found - unexpected format
        return {"error": "Failed to parse data output."}

    # Data entries should be after the separator line
    data_lines = lines[sep_index+1:]
    for l in data_lines:
        l = l.strip()
        if not l:
            continue
        # Split on the '|' which separates data and type
        parts = l.rsplit('|', 1)
        if len(parts) == 2:
            data_val = parts[0].strip()
            type_val = parts[1].strip()
            results.append({"Data": data_val, "Type": type_val})

    # Count the number of entries
    count = len(results)
    return {"count": count, "results": results}


def run_spiderfoot_scan(scan_id: uuid.UUID, target: str, modules: str):
    # Define paths for logs and output
    command_log_file = f"/tmp/{scan_id}_commands.log"
    output_file = f"/tmp/{scan_id}.json"

    # Prepare the commands to send to sfcli.py
    # Start the scan and then exit
    commands = f"start {target} -u all -n {scan_id}\nexit\n"

    # Command to start sfcli.py with server URL and log file
    cmd = [
        "python3",
        "/opt/spiderfoot/sfcli.py",
        "-s", "http://spiderfoot:5001",
        "-o", command_log_file  # Spool commands and output to log file
    ]

    spiderfoot_scan_id = None

    try:
        # Run sfcli.py and send commands via stdin
        process = subprocess.Popen(
            cmd,
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        stdout, stderr = process.communicate(commands)

        if process.returncode != 0:
            # Handle errors from the subprocess
            result_data = {
                "error": f"SpiderFoot CLI error: {stderr.strip()}"
            }
        else:
            # Parse the SpiderFoot scan ID from stdout
            # Example line: "[*] Scan ID: F25409E4"
            match = re.search(r"Scan ID:\s+([A-Za-z0-9]+)", stdout)
            if match:
                spiderfoot_scan_id = match.group(1)
            else:
                result_data = {"error": "Failed to parse SpiderFoot scan ID from output."}

            if spiderfoot_scan_id:
                # If we got the scan ID, we consider the start successful
                result_data = {"message": "Scan started successfully.", "spiderfoot_scan_id": spiderfoot_scan_id}

            # Check if the command log file exists and remove it
            if os.path.exists(command_log_file):
                with open(command_log_file, 'r') as f:
                    log_data = f.read()
                os.remove(command_log_file)

        # Update the database with the scan status as 'running' and store the SpiderFoot scan ID
        db = SessionLocal()
        scan = db.query(SpiderfootScan).filter(SpiderfootScan.id == scan_id).first()
        if scan:
            scan.status = "running"
            scan.result = result_data
            if spiderfoot_scan_id:
                scan.spiderfoot_scan_id = spiderfoot_scan_id
            db.commit()
        db.close()

        # If we don't have a valid spiderfoot_scan_id, stop here
        if not spiderfoot_scan_id:
            return

        # Wait for the scan to complete
        # This is a simplified approach; consider implementing polling for a robust solution
        time.sleep(120)  # Wait 2 minutes; adjust based on expected scan duration

        # Export the scan results as JSON using the SpiderFoot scan ID
        export_commands = f"export {spiderfoot_scan_id} -t json -f {output_file}\nexit\n"

        export_cmd = [
            "python3",
            "/opt/spiderfoot/sfcli.py",
            "-s", "http://spiderfoot:5001",
            "-o", f"/tmp/{scan_id}_export.log"  # Log export commands and output
        ]

        # Run sfcli.py to export the scan results
        export_process = subprocess.Popen(
            export_cmd,
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        export_stdout, export_stderr = export_process.communicate(export_commands)

        if export_process.returncode != 0:
            # Handle export errors
            export_result = {
                "error": f"SpiderFoot export error: {export_stderr.strip()}"
            }
        else:
            # Read the exported JSON file
            if os.path.exists(output_file):
                with open(output_file, 'r') as f:
                    try:
                        export_data = json.load(f)
                    except json.JSONDecodeError:
                        export_data = {"error": "Failed to parse JSON output from SpiderFoot export."}
                os.remove(output_file)
                # Remove the export log file if it exists
                export_log_file = f"/tmp/{scan_id}_export.log"
                if os.path.exists(export_log_file):
                    os.remove(export_log_file)
                export_result = export_data
            else:
                export_result = {"error": "No output file generated by SpiderFoot export."}

        # Update the database with the scan status and results
        db = SessionLocal()
        scan = db.query(SpiderfootScan).filter(SpiderfootScan.id == scan_id).first()
        if scan:
            scan.status = "completed"
            scan.result = export_result
            db.commit()
        db.close()

        # Once the scan is done and exported, fetch the parsed data results
        # This will return JSON with count and results
        data_result = fetch_scan_data(spiderfoot_scan_id)

        # Update the database again to include the fetched data results
        db = SessionLocal()
        scan = db.query(SpiderfootScan).filter(SpiderfootScan.id == scan_id).first()
        if scan:
            # Merge data_result into the existing results, or store separately
            # Here we merge under a new key "parsed_data"
            if isinstance(scan.result, dict):
                scan.result["parsed_data"] = data_result
            else:
                # If result isn't dict for some reason, just replace
                scan.result = {"export_result": export_result, "parsed_data": data_result}
            db.commit()
        db.close()

    except Exception as e:
        # Handle unexpected exceptions
        db = SessionLocal()
        scan = db.query(SpiderfootScan).filter(SpiderfootScan.id == scan_id).first()
        if scan:
            scan.status = "error"
            scan.result = {"error": str(e)}
            db.commit()
        db.close()


def queue_worker():
    while True:
        if scan_queue:
            db = SessionLocal()
            scan_info = scan_queue.pop(0)
            scan_id, target, modules = scan_info["id"], scan_info["target"], scan_info["modules"]

            # Update scan status to 'running'
            scan = db.query(SpiderfootScan).filter(SpiderfootScan.id == scan_id).first()
            if scan:
                scan.status = "running"
                db.commit()
            db.close()

            # Execute the scan
            run_spiderfoot_scan(scan_id, target, modules)
        else:
            time.sleep(2)

# Start the worker thread
worker_thread = threading.Thread(target=queue_worker, daemon=True)
worker_thread.start()
