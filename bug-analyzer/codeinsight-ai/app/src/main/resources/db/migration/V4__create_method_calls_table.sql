CREATE TABLE IF NOT EXISTS method_calls (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id UUID NOT NULL,
    caller_class VARCHAR(255),
    caller_method VARCHAR(255),
    callee_method VARCHAR(255)
    );

CREATE INDEX idx_method_calls_project_id
    ON method_calls(project_id);

CREATE INDEX idx_method_calls_caller
    ON method_calls(caller_class, caller_method);