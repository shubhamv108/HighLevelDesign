CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE persons (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(255) UNIQUE NOT NULL,
    phone       VARCHAR(20),
    dob         DATE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC')
);

-- Indexes for faster lookups
CREATE INDEX idx_persons_last_name   ON persons (last_name);
CREATE INDEX idx_persons_email_lower ON persons (LOWER(email));
CREATE INDEX idx_persons_created_at  ON persons (created_at DESC);

-- Optional: trigger for auto-updating updated_at
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = (NOW() AT TIME ZONE 'UTC');
   RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON persons
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TABLE persons_audit (
    audit_id     UUID PRIMARY KEY DEFAULT uuid_generate_v7(),
    id           UUID NOT NULL,
    first_name   VARCHAR(100),
    last_name    VARCHAR(100),
    email        VARCHAR(255),
    phone        VARCHAR(20),
    dob          DATE,
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ NOT NULL,
    action       VARCHAR(10) NOT NULL,
    audit_created_at TIMESTAMPTZ NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
    changed_by   TEXT 
) PARTITION BY RANGE (audit_created_at);

CREATE OR REPLACE FUNCTION audit_persons()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO persons_audit (
        id, first_name, last_name, email, phone, dob,
        created_at, updated_at,
        action, audit_created_at, changed_by
    )
    VALUES (
        COALESCE(OLD.id, NEW.id),
        COALESCE(OLD.first_name, NEW.first_name),
        COALESCE(OLD.last_name, NEW.last_name),
        COALESCE(OLD.email, NEW.email),
        COALESCE(OLD.phone, NEW.phone),
        COALESCE(OLD.dob, NEW.dob),
        COALESCE(OLD.created_at, NEW.created_at),
        COALESCE(OLD.updated_at, NEW.updated_at),
        TG_OP,
        NOW() AT TIME ZONE 'UTC',
        current_user
    );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_persons_audit
AFTER INSERT OR UPDATE OR DELETE ON persons
FOR EACH ROW
EXECUTE FUNCTION audit_persons();