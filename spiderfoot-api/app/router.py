from fastapi import APIRouter, HTTPException
from uuid import uuid4, UUID
from sqlalchemy.orm import Session
from app.database import SessionLocal
from app.models import SpiderfootScan
from app.schemas import ScanRequest, ScanResponse, ScanResult
from app.queue_worker import scan_queue

router = APIRouter()

@router.post("/scan", response_model=ScanResponse)
def start_scan(scan_request: ScanRequest):
    db = SessionLocal()
    scan_id = uuid4()  # This is the new UUID for this scan attempt

    # Check if there's already a row for this target
    existing_scan = db.query(SpiderfootScan).filter(
        SpiderfootScan.target == scan_request.target
    ).first()

    if existing_scan:
        # If it exists, treat this as an update.
        existing_scan.id = scan_id
        existing_scan.modules = scan_request.modules
        existing_scan.status = "queued"
        existing_scan.result = None
        existing_scan.spiderfoot_scan_id = None
        db.commit()
    else:
        # If no existing row, insert a new one
        new_scan = SpiderfootScan(
            target=scan_request.target,
            id=scan_id,
            modules=scan_request.modules,
            status="queued"
        )
        db.add(new_scan)
        db.commit()

    db.close()

    # Add to queue
    scan_queue.append({
        "target": scan_request.target,
        "id": scan_id,
        "modules": scan_request.modules
    })

    return ScanResponse(id=scan_id, status="queued")


@router.get("/scan/{target}", response_model=ScanResult)
def get_scan_result(target: str):
    db = SessionLocal()
    scan = db.query(SpiderfootScan).filter(SpiderfootScan.target == target).first()
    if not scan:
        raise HTTPException(status_code=404, detail="Scan not found")
    return ScanResult(
        id=scan.id,
        target=scan.target,
        modules=scan.modules,
        status=scan.status,
        result=scan.result,
        spiderfoot_scan_id=scan.spiderfoot_scan_id
    )
