CREATE TABLE processing_log
(
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     uuid NOT NULL REFERENCES users (id),
    input_text  TEXT,
    output_text TEXT,
    created_at  TIMESTAMP
)