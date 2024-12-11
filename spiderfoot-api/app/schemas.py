from pydantic import BaseModel, Field
from typing import Optional, Any
from uuid import UUID

class ScanRequest(BaseModel):
    target: str
    modules: Optional[str] = None 

class ScanResponse(BaseModel):
    id: UUID
    status: str

class ScanResult(BaseModel):
    id: UUID
    target: str
    modules: Optional[str]
    status: str
    result: Optional[Any]
