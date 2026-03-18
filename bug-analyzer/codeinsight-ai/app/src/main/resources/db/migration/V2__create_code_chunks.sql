CREATE TABLE IF NOT EXISTS code_chunks (
                             id UUID PRIMARY KEY,
                             project_id UUID,
                             file_path VARCHAR(1000),
                             class_name VARCHAR(255),
                             method_name VARCHAR(255),
                             line_start INT,
                             line_end INT,
                             code_text TEXT
);