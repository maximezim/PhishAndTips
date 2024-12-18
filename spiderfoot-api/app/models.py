import uuid
from sqlalchemy import Column, Text, JSON, TIMESTAMP, func
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

class SpiderfootScan(Base):
    __tablename__ = 'spiderfoot_scans'

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    target = Column(Text, nullable=False)
    modules = Column(Text, nullable=True)
    status = Column(Text, nullable=False, default='queued')
    result = Column(JSON, nullable=True)
    created_at = Column(TIMESTAMP(timezone=True), server_default=func.now())
    updated_at = Column(TIMESTAMP(timezone=True), onupdate=func.now(), server_default=func.now())
