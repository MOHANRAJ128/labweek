CREATE TABLE IF NOT EXISTS error_logs (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        project_id UUID,
                        file_name VARCHAR(255),
                        file_path VARCHAR(500),
                        exception_type VARCHAR(255),
                        class_name VARCHAR(255),
                        method_name VARCHAR(255),
                        line_number INTEGER,
                        raw_log TEXT,
                        created_at TIMESTAMP,

                      CONSTRAINT fk_logs_project
                          FOREIGN KEY (project_id)
                              REFERENCES projects(id)
                              ON DELETE CASCADE
);

CREATE INDEX idx_logs_project_id
    ON error_logs(project_id);