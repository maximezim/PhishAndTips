CREATE TABLE IF NOT EXISTS spiderfoot_scans (
    id UUID PRIMARY KEY,
    target TEXT NOT NULL,
    modules TEXT,
    status TEXT NOT NULL DEFAULT 'queued',
    result JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
