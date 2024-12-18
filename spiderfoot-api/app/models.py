import uuid
from sqlalchemy import Column, Text, JSON, TIMESTAMP, func, String, text
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.dialects.postgresql import UUID as pgUUID, JSONB
Base = declarative_base()


class SpiderfootScan(Base):
    __tablename__ = "spiderfoot_scans"

    id = Column(pgUUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    target = Column(Text, nullable=False)
    modules = Column(Text, nullable=True)
    status = Column(Text, nullable=False, default='queued')
    result = Column(JSONB, nullable=True)
    spiderfoot_scan_id = Column(Text, nullable=True)  # Added column for SpiderFoot scan ID
    created_at = Column(TIMESTAMP(timezone=True), server_default=text("NOW()"))
    updated_at = Column(TIMESTAMP(timezone=True), server_default=text("NOW()"))
