# docker-init-db

These scripts are automatically run by PostgreSQL on first startup, in alphabetical order.
They are only executed once — when the database volume is first created.

## Files

- **01_schema.sql** — Full database schema for BIG70. Creates all tables, sequences, constraints,
  and seeds all default game parameters. Also includes the default head admin account (username: `admin`, password: `admin1357`).

- **02_seed.sql** — Creates the `postgres` superuser role if it does not already exist.
  Required because `01_schema.sql` was exported from a server where `postgres` is the default owner of all tables.

- **03_widen-password.sql** - This just widens passwords under user tables to make sure encryption is correct

## Notes

- If you need to reset the database to its initial state, run:
```bash
  docker compose down -v
  docker compose build --no-cache
  docker compose up
```
- Do **not** add additional schema or seed files unless you know what you are doing —
  the full schema and seed data is already included in `01_schema.sql`.
- Password columns are already set to `VARCHAR(255)` in the schema to support BCrypt hashing.