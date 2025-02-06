CREATE TABLE IF NOT EXISTS spiderfoot_scans (
    target TEXT PRIMARY KEY,
    id UUID NOT NULL,
    modules TEXT,
    status TEXT NOT NULL DEFAULT 'queued',
    result JSONB,
    spiderfoot_scan_id TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
