from pydantic import BaseModel, Field
from typing import Optional, Any
from uuid import UUID

class ScanRequest(BaseModel):
    target: str
    modules: Optional[str] = None

class ScanResponse(BaseModel):
    id: UUID
    status: str
    spiderfoot_scan_id: Optional[str] = None  # Added this field to return the SpiderFoot assigned scan ID

class ScanResult(BaseModel):
    id: UUID
    target: str
    modules: Optional[str]
    status: str
    result: Optional[Any]
    spiderfoot_scan_id: Optional[str] = None  # Added this field to store SpiderFoot scan ID
