CREATE TABLE IF NOT EXISTS projects (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    upload_path VARCHAR(500),
    created_at TIMESTAMP
);