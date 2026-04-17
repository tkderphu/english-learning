-- Add coaching/session-plan columns to ai_chat_sessions.
-- Compatible with MySQL 5.7/8.0 (no ADD COLUMN IF NOT EXISTS required).

USE `english`;

SET @db := DATABASE();

-- goal_type
SET @exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db
      AND TABLE_NAME = 'ai_chat_sessions'
      AND COLUMN_NAME = 'goal_type'
);
SET @sql := IF(@exists = 0,
    'ALTER TABLE ai_chat_sessions ADD COLUMN goal_type VARCHAR(100) NULL',
    'SELECT ''goal_type already exists''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- focus_skill
SET @exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db
      AND TABLE_NAME = 'ai_chat_sessions'
      AND COLUMN_NAME = 'focus_skill'
);
SET @sql := IF(@exists = 0,
    'ALTER TABLE ai_chat_sessions ADD COLUMN focus_skill VARCHAR(100) NULL',
    'SELECT ''focus_skill already exists''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- coaching_mode
SET @exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db
      AND TABLE_NAME = 'ai_chat_sessions'
      AND COLUMN_NAME = 'coaching_mode'
);
SET @sql := IF(@exists = 0,
    'ALTER TABLE ai_chat_sessions ADD COLUMN coaching_mode VARCHAR(100) NULL',
    'SELECT ''coaching_mode already exists''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- fluency_mode
SET @exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db
      AND TABLE_NAME = 'ai_chat_sessions'
      AND COLUMN_NAME = 'fluency_mode'
);
SET @sql := IF(@exists = 0,
    'ALTER TABLE ai_chat_sessions ADD COLUMN fluency_mode BIT(1) NULL',
    'SELECT ''fluency_mode already exists''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- target_duration_minutes
SET @exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db
      AND TABLE_NAME = 'ai_chat_sessions'
      AND COLUMN_NAME = 'target_duration_minutes'
);
SET @sql := IF(@exists = 0,
    'ALTER TABLE ai_chat_sessions ADD COLUMN target_duration_minutes INT NULL',
    'SELECT ''target_duration_minutes already exists''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- mission_title
SET @exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db
      AND TABLE_NAME = 'ai_chat_sessions'
      AND COLUMN_NAME = 'mission_title'
);
SET @sql := IF(@exists = 0,
    'ALTER TABLE ai_chat_sessions ADD COLUMN mission_title VARCHAR(255) NULL',
    'SELECT ''mission_title already exists''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- mission_objective
SET @exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db
      AND TABLE_NAME = 'ai_chat_sessions'
      AND COLUMN_NAME = 'mission_objective'
);
SET @sql := IF(@exists = 0,
    'ALTER TABLE ai_chat_sessions ADD COLUMN mission_objective TEXT NULL',
    'SELECT ''mission_objective already exists''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- mission_success_criteria_json
SET @exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db
      AND TABLE_NAME = 'ai_chat_sessions'
      AND COLUMN_NAME = 'mission_success_criteria_json'
);
SET @sql := IF(@exists = 0,
    'ALTER TABLE ai_chat_sessions ADD COLUMN mission_success_criteria_json TEXT NULL',
    'SELECT ''mission_success_criteria_json already exists''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- mission_status
SET @exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @db
      AND TABLE_NAME = 'ai_chat_sessions'
      AND COLUMN_NAME = 'mission_status'
);
SET @sql := IF(@exists = 0,
    'ALTER TABLE ai_chat_sessions ADD COLUMN mission_status VARCHAR(50) NULL',
    'SELECT ''mission_status already exists''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
