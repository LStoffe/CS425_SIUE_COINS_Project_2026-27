-- Widen password columns to support BCrypt hashes (60+ chars).
-- Run after schema exists (e.g. from backup.sql or 01/02). Safe to run multiple times.

DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.tables
    WHERE table_schema = 'public' AND table_name = 'admin'
  ) THEN
    ALTER TABLE public.admin
      ALTER COLUMN password TYPE character varying(255);
  END IF;

  IF EXISTS (
    SELECT 1 FROM information_schema.tables
    WHERE table_schema = 'public' AND table_name = 'company'
  ) THEN
    ALTER TABLE public.company
      ALTER COLUMN password TYPE character varying(255);
  END IF;
END $$;
