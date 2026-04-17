-- =============================================================================
-- Full demo database setup (schema + AI/Profile demo data)
--
-- Run from folder: english-learning/sql
-- Command:
--   mysql -u root -p < setup_full_demo_database.sql
--
-- This script will:
--   1) Create/refresh base schema
--   2) Add new coaching columns for ai_chat_sessions
--   3) Seed a large demo dataset for AI + Profile flows
-- =============================================================================

SOURCE schema_english_full.sql;
SOURCE ../scripts/add_ai_chat_session_coaching_columns.sql;
SOURCE seed_full_ai_profile_demo.sql;
