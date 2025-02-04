#!/usr/bin/env python3
import requests
from sqlalchemy import text
from app.database import SessionLocal

def run_weekly_scan():
    db = SessionLocal()
    result = db.execute(text("SELECT email FROM user_data"))
    users = [row[0] for row in result]
    db.close()

    for email in users:
        payload = {"target": email}
        response = requests.post("http://localhost:8000/internal/spiderfoot/scan", json=payload)
        print(f"Triggered scan for {email}: {response.status_code}")

if __name__ == "__main__":
    run_weekly_scan()
