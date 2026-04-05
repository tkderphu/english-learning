-- =============================================================================
-- Seed tối thiểu để test chức năng AI (kịch bản, filter level, lịch sử phiên)
--
-- Điều kiện:
--   - Database `english` đã tồn tại (xem schema_english_full.sql).
--   - Phiên mẫu gắn user id = 1. Nếu chưa có user id 1, script sẽ tạo tài khoản seed
--     (email ai_seed_holder@english.local — cùng bcrypt với admin trong data.sql).
--
-- Chạy:
--   mysql -u root -p english < sql/seed_ai_test_data.sql
--
-- Ghi chú:
--   - Scenario id 100–105: nếu đã tồn tại, ON DUPLICATE KEY UPDATE sẽ ghi đè
--     cùng id (chỉnh id trong file nếu trùng dữ liệu thật của bạn).
--   - Level/topic id 1–3: cùng cơ chế; phù hợp DB mới chưa có topic/level.
-- =============================================================================

USE `english`;

SET NAMES utf8mb4;

-- ---------------------------------------------------------------------------
-- Level (mobile filter: Beginner / Intermediate / Advanced theo levelName)
-- ---------------------------------------------------------------------------
INSERT INTO `level` (
  `id`, `created_user`, `created_at`, `modified_by`, `modified_at`, `status`,
  `name`, `description`, `number_course`
) VALUES
(1, 'ai_seed', NOW(6), NULL, NULL, 1, 'Beginner', 'Cơ bản / A1–A2', 0),
(2, 'ai_seed', NOW(6), NULL, NULL, 1, 'Intermediate', 'Trung cấp / B1', 0),
(3, 'ai_seed', NOW(6), NULL, NULL, 1, 'Advanced', 'Nâng cao / B2–C1', 0)
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `description` = VALUES(`description`),
  `status` = VALUES(`status`),
  `number_course` = VALUES(`number_course`);

-- ---------------------------------------------------------------------------
-- Topic (FK cho ai_scenarios.topic_id)
-- ---------------------------------------------------------------------------
INSERT INTO `topic` (
  `id`, `created_user`, `created_at`, `modified_by`, `modified_at`, `status`,
  `name`, `description`, `thumbnail`
) VALUES
(1, 'ai_seed', NOW(6), NULL, NULL, 1, 'Daily life', 'Hội thoại hằng ngày', NULL),
(2, 'ai_seed', NOW(6), NULL, NULL, 1, 'Travel', 'Du lịch & sân bay', NULL),
(3, 'ai_seed', NOW(6), NULL, NULL, 1, 'Business', 'Công sở & họp hành', NULL)
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `description` = VALUES(`description`),
  `status` = VALUES(`status`);

-- ---------------------------------------------------------------------------
-- Kịch bản AI (is_active = 1 mới được API list)
-- type = SCENARIO; app lọc bỏ FREE_CHAT (id 0 là ảo, không insert)
-- ---------------------------------------------------------------------------
INSERT INTO `ai_scenarios` (
  `id`, `title`, `description`, `topic_id`, `level_id`, `type`,
  `ai_role`, `instruction`, `icon_url`, `generated_by_ai`, `generation_prompt`,
  `is_active`, `system_prompt`
) VALUES
(
  100,
  'Cà phê & chào hỏi',
  'Luyện đặt đồ uống và small talk tại quán.',
  1, 1, 'SCENARIO',
  'Friendly barista',
  'You work at a coffee shop. Greet customers, take their order, make small talk. Use short, clear English.',
  NULL, 0, NULL, 1,
  'Stay in character as a café barista. English only. Be warm and patient with beginners.'
),
(
  101,
  'Siêu thị — hỏi kệ hàng',
  'Hỏi nhân viên vị trí sản phẩm và giá.',
  1, 1, 'SCENARIO',
  'Store clerk',
  'You work in a supermarket. Help customers find items and read simple price tags.',
  NULL, 0, NULL, 1,
  NULL
),
(
  102,
  'Hỏi đường & xe buýt',
  'Chỉ đường phố và phương tiện công cộng.',
  1, 2, 'SCENARIO',
  'Local resident',
  'A tourist asks for directions and bus numbers. Give clear steps.',
  NULL, 0, NULL, 1,
  NULL
),
(
  103,
  'Check-in khách sạn',
  'Thủ tục nhận phòng.',
  2, 2, 'SCENARIO',
  'Hotel receptionist',
  'Handle check-in: room type, ID, breakfast hours. Stay professional.',
  NULL, 0, NULL, 1,
  NULL
),
(
  104,
  'Phỏng vấn xin việc (ngắn)',
  'Buổi phỏng vấn 5–10 phút.',
  3, 3, 'SCENARIO',
  'HR interviewer',
  'Ask typical interview questions; keep answers short; be polite.',
  NULL, 0, NULL, 1,
  NULL
),
(
  105,
  'Họp team online',
  'Daily stand-up tiếng Anh.',
  3, 3, 'SCENARIO',
  'Team lead',
  'Run a short stand-up: yesterday, today, blockers. One question at a time.',
  NULL, 0, NULL, 1,
  NULL
)
ON DUPLICATE KEY UPDATE
  `title` = VALUES(`title`),
  `description` = VALUES(`description`),
  `topic_id` = VALUES(`topic_id`),
  `level_id` = VALUES(`level_id`),
  `type` = VALUES(`type`),
  `ai_role` = VALUES(`ai_role`),
  `instruction` = VALUES(`instruction`),
  `icon_url` = VALUES(`icon_url`),
  `generated_by_ai` = VALUES(`generated_by_ai`),
  `generation_prompt` = VALUES(`generation_prompt`),
  `is_active` = VALUES(`is_active`),
  `system_prompt` = VALUES(`system_prompt`);

-- ---------------------------------------------------------------------------
-- Phiên chat mẫu (ENDED) + tin nhắn + feedback + summary — user_id = 1
-- Để test GET /api/ai-chat/v1/sessions, .../detail, .../transcript
-- Xóa bản cũ cùng tiêu đề seed (nếu chạy lại script)
--
-- FK: cần `user`.id = 1. Nếu bảng user không có id 1 (chưa chạy data.sql), tạo bản ghi seed.
--     Mật khẩu bcrypt giống user admin trong data.sql (cùng hash).
-- MySQL Workbench "Safe Updates": DELETE theo title không dùng PK → tắt tạm session.
-- ---------------------------------------------------------------------------
INSERT INTO `user` (
  `id`, `created_user`, `created_at`, `modified_by`, `modified_at`, `status`,
  `email`, `password`, `full_name`, `avatar`, `role`
)
SELECT
  1,
  'ai_seed',
  NOW(6),
  NULL,
  NULL,
  1,
  'ai_seed_holder@english.local',
  '$2a$10$5H8hgCQY6hcjsllVPAnire6OD9r5Zve4Ze56/jI7DJj.v0TBgP6Ue',
  'AI seed holder',
  NULL,
  2
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `user` u WHERE u.`id` = 1);

SET SESSION sql_safe_updates = 0;

DELETE FROM `ai_session_summaries`
WHERE `session_id` IN (
  SELECT `id` FROM (SELECT `id` FROM `ai_chat_sessions` WHERE `title` = '[SEED] Sample ended chat') t
);
DELETE FROM `ai_message_errors`
WHERE `feedback_id` IN (
  SELECT `id` FROM `ai_message_feedback`
  WHERE `message_id` IN (
    SELECT `id` FROM `ai_chat_messages`
    WHERE `session_id` IN (
      SELECT `id` FROM (SELECT `id` FROM `ai_chat_sessions` WHERE `title` = '[SEED] Sample ended chat') x
    )
  )
);
DELETE FROM `ai_message_feedback`
WHERE `message_id` IN (
  SELECT `id` FROM `ai_chat_messages`
  WHERE `session_id` IN (
    SELECT `id` FROM (SELECT `id` FROM `ai_chat_sessions` WHERE `title` = '[SEED] Sample ended chat') x
  )
);
DELETE FROM `ai_chat_messages`
WHERE `session_id` IN (
  SELECT `id` FROM (SELECT `id` FROM `ai_chat_sessions` WHERE `title` = '[SEED] Sample ended chat') x
);
DELETE FROM `ai_chat_sessions` WHERE `title` = '[SEED] Sample ended chat';

SET SESSION sql_safe_updates = 1;

INSERT INTO `ai_chat_sessions` (
  `user_id`, `scenario_id`, `topic_id`, `level_id`, `title`, `session_type`, `status`,
  `ai_role_snapshot`, `instruction_snapshot`, `max_turns`, `current_turn`,
  `started_at`, `ended_at`, `last_message_at`, `system_prompt_snapshot`
) VALUES (
  1,
  100,
  1,
  1,
  '[SEED] Sample ended chat',
  'SCENARIO',
  'ENDED',
  'Friendly barista',
  'You work at a coffee shop. Greet customers and take orders.',
  20,
  2,
  DATE_SUB(NOW(6), INTERVAL 2 DAY),
  DATE_SUB(NOW(6), INTERVAL 1 DAY),
  DATE_SUB(NOW(6), INTERVAL 1 DAY),
  NULL
);

SET @seed_sess := LAST_INSERT_ID();

INSERT INTO `ai_chat_messages` (
  `session_id`, `sender_type`, `input_type`, `turn_number`, `content`
) VALUES (
  @seed_sess, 'USER', 'TEXT', 1, 'Hi, can I get a latte please?'
);

SET @seed_msg_user := LAST_INSERT_ID();

INSERT INTO `ai_chat_messages` (
  `session_id`, `sender_type`, `input_type`, `turn_number`, `content`
) VALUES (
  @seed_sess, 'AI', 'TEXT', 1, 'Sure! What size would you like — small, medium, or large?'
);

INSERT INTO `ai_message_feedback` (
  `message_id`,
  `pronunciation_score`, `grammar_score`, `vocabulary_score`, `fluency_score`,
  `overall_comment`, `improved_version`, `natural_suggestion`, `error_count`
) VALUES (
  @seed_msg_user,
  8.5000, 8.0000, 7.5000, 8.0000,
  'Clear and polite request. Good for ordering at a café.',
  'Hi, could I get a latte, please?',
  'Could I get a latte, please?',
  1
);

SET @seed_fb := LAST_INSERT_ID();

INSERT INTO `ai_message_errors` (
  `feedback_id`, `error_type`, `original_text`, `suggested_text`, `explanation`
) VALUES (
  @seed_fb,
  'EXPRESSION',
  'get a latte',
  'order a latte',
  'Both are OK; ''order'' is slightly more natural in many cafés.'
);

INSERT INTO `ai_session_summaries` (
  `session_id`, `fluency_level`, `grammar_level`, `vocabulary_level`,
  `sentence_count`, `error_count`, `duration_seconds`, `next_suggestion`, `related_topics`
) VALUES (
  @seed_sess,
  'GOOD',
  'GOOD',
  'FAIR',
  2,
  1,
  120,
  'Practice sizes, milk options, and takeaway cups.',
  'Ordering drinks; Polite requests'
);

-- ---------------------------------------------------------------------------
-- Kiểm tra nhanh (tùy chọn — bỏ comment để xem)
-- SELECT id, title, level_id, topic_id, is_active FROM ai_scenarios WHERE id BETWEEN 100 AND 105;
-- SELECT id, title, status FROM ai_chat_sessions WHERE user_id = 1 ORDER BY id DESC LIMIT 5;
-- =============================================================================
