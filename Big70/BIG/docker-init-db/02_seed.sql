-- Create the "postgres" role so backups taken from servers that use it as owner
-- can be restored without "role postgres does not exist" errors.
-- Safe to run multiple times (IF NOT EXISTS in PostgreSQL 15+).

DO $$
BEGIN
  -- Check if the 'postgres' role already exists in the database
  IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'postgres') THEN
    -- If it doesn't exist, create the 'postgres' role with login, superuser privileges, and password
    CREATE ROLE postgres WITH LOGIN SUPERUSER PASSWORD 'postgres';
  END IF; -- End of IF check
END
$$;
