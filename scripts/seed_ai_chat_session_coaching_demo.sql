-- =============================================================================
-- Seed demo data for new coaching/mission columns in ai_chat_sessions
--
-- Run after:
--   scripts/add_ai_chat_session_coaching_columns.sql
--
-- Usage:
--   mysql -u root -p english < scripts/seed_ai_chat_session_coaching_demo.sql
-- =============================================================================

USE `english`;
SET NAMES utf8mb4;

-- Change this user id to the account you use for demo.
SET @target_user_id := 1;

-- 1) Update existing seed session (if present) with coaching data.
UPDATE `ai_chat_sessions`
SET
    `goal_type` = 'DAILY_CONVERSATION',
    `focus_skill` = 'VOCABULARY',
    `coaching_mode` = 'GENTLE',
    `fluency_mode` = b'1',
    `target_duration_minutes` = 12,
    `mission_title` = 'Order confidently at a coffee shop',
    `mission_objective` = 'Use polite requests and confirm order details naturally.',
    `mission_success_criteria_json` = 'Use please|Ask for size|Confirm takeaway or dine-in',
    `mission_status` = 'COMPLETED'
WHERE `user_id` = @target_user_id
  AND `title` = '[SEED] Sample ended chat';

-- 2) Insert one extra demo session with full coaching/mission fields.
--    Remove old one (safe to re-run script).
DELETE FROM `ai_chat_sessions`
WHERE `user_id` = @target_user_id
  AND `title` = '[SEED] AI Coach mission demo';

INSERT INTO `ai_chat_sessions` (
    `user_id`, `scenario_id`, `topic_id`, `level_id`, `title`, `session_type`, `status`,
    `ai_role_snapshot`, `instruction_snapshot`, `max_turns`, `current_turn`,
    `started_at`, `ended_at`, `last_message_at`, `system_prompt_snapshot`,
    `goal_type`, `focus_skill`, `coaching_mode`, `fluency_mode`, `target_duration_minutes`,
    `mission_title`, `mission_objective`, `mission_success_criteria_json`, `mission_status`
)
SELECT
    @target_user_id,
    100, 1, 1,
    '[SEED] AI Coach mission demo',
    'SCENARIO',
    'ENDED',
    'Friendly barista',
    'You are a barista. Keep questions short and beginner-friendly.',
    20,
    3,
    DATE_SUB(NOW(6), INTERVAL 3 HOUR),
    DATE_SUB(NOW(6), INTERVAL 2 HOUR),
    DATE_SUB(NOW(6), INTERVAL 2 HOUR),
    NULL,
    'SPEAKING_CONFIDENCE',
    'FLUENCY',
    'STRICT',
    b'1',
    15,
    'Handle a coffee order from start to finish',
    'Ask and answer naturally about drink type, size, and extras.',
    'Use at least 3 polite phrases|Avoid Vietnamese in chat|Respond without long pauses',
    'ACTIVE'
FROM DUAL
WHERE EXISTS (SELECT 1 FROM `user` u WHERE u.`id` = @target_user_id);

-- Optional quick check:
-- SELECT id, title, goal_type, focus_skill, coaching_mode, fluency_mode, target_duration_minutes, mission_title, mission_status
-- FROM ai_chat_sessions
-- WHERE user_id = @target_user_id
-- ORDER BY id DESC
-- LIMIT 10;
