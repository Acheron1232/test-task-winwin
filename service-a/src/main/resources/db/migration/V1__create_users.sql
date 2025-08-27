CREATE TABLE users
(
    id       uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email    VARCHAR(254) NOT NULL UNIQUE,
    role     VARCHAR(50)  NOT NULL,
    password VARCHAR(255) NOT NULL
    -- password can be null for oauth users in the future, but it's better to use a not null constraint for now

)