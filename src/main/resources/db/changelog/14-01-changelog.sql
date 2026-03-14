
ALTER TABLE person
    ADD is_deleted BOOLEAN DEFAULT FALSE;

-- changeset Wess:1773447579627-11
ALTER TABLE person
    ALTER COLUMN is_deleted SET NOT NULL;

