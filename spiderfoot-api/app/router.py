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
    scan_id = uuid4()
    db = SessionLocal()

    new_scan = SpiderfootScan(
        id=scan_id,
        target=scan_request.target,
        modules=scan_request.modules,
        status="queued"
    )
    db.add(new_scan)
    db.commit()

    # Add to queue
    scan_queue.append({"id": scan_id, "target": scan_request.target, "modules": scan_request.modules})

    return ScanResponse(id=scan_id, status="queued")

@router.get("/scan/{scan_id}", response_model=ScanResult)
def get_scan_result(scan_id: UUID):
    db = SessionLocal()
    scan = db.query(SpiderfootScan).filter(SpiderfootScan.id == scan_id).first()
    if not scan:
        raise HTTPException(status_code=404, detail="Scan not found")
    return ScanResult(
        id=scan.id,
        target=scan.target,
        modules=scan.modules,
        status=scan.status,
        result=scan.result
    )
