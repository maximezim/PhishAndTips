import uuid
from sqlalchemy import Column, Text, JSON, TIMESTAMP, text
from sqlalchemy.dialects.postgresql import UUID as pgUUID, JSONB
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

class SpiderfootScan(Base):
    __tablename__ = "spiderfoot_scans"
    target = Column(Text, primary_key=True)
    id = Column(pgUUID(as_uuid=True), nullable=False, default=uuid.uuid4)
    modules = Column(Text, nullable=True)
    status = Column(Text, nullable=False, default='queued')
    result = Column(JSONB, nullable=True)
    spiderfoot_scan_id = Column(Text, nullable=True)
    created_at = Column(TIMESTAMP(timezone=True), server_default=text("NOW()"))
    updated_at = Column(TIMESTAMP(timezone=True), server_default=text("NOW()"))

