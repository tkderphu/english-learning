-- =============================================================================
-- Full demo seed for AI + Profile features
-- Idempotent-ish: uses fixed IDs / seed prefixes, can re-run safely.
-- Requires:
--   - schema_english_full.sql
--   - scripts/add_ai_chat_session_coaching_columns.sql
-- =============================================================================

USE `english`;
SET NAMES utf8mb4;

-- Demo users
INSERT INTO `user` (
  `id`, `created_user`, `created_at`, `modified_by`, `modified_at`, `status`,
  `email`, `password`, `full_name`, `avatar`, `role`
)
VALUES
  (1, 'seed_demo', NOW(6), NULL, NULL, 1, 'demo1@english.local', '$2a$10$5H8hgCQY6hcjsllVPAnire6OD9r5Zve4Ze56/jI7DJj.v0TBgP6Ue', 'Demo Learner 1', NULL, 2),
  (2, 'seed_demo', NOW(6), NULL, NULL, 1, 'demo2@english.local', '$2a$10$5H8hgCQY6hcjsllVPAnire6OD9r5Zve4Ze56/jI7DJj.v0TBgP6Ue', 'Demo Learner 2', NULL, 2)
ON DUPLICATE KEY UPDATE
  `full_name` = VALUES(`full_name`),
  `status` = VALUES(`status`);

-- Levels / Topics
INSERT INTO `level` (`id`, `created_user`, `created_at`, `status`, `name`, `description`, `number_course`)
VALUES
  (1, 'seed_demo', NOW(6), 1, 'Beginner', 'A1-A2', 0),
  (2, 'seed_demo', NOW(6), 1, 'Intermediate', 'B1', 0),
  (3, 'seed_demo', NOW(6), 1, 'Advanced', 'B2-C1', 0)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `description` = VALUES(`description`), `status` = VALUES(`status`);

INSERT INTO `topic` (`id`, `created_user`, `created_at`, `status`, `name`, `description`, `thumbnail`)
VALUES
  (1, 'seed_demo', NOW(6), 1, 'Daily life', 'Daily small talk', NULL),
  (2, 'seed_demo', NOW(6), 1, 'Travel', 'Airport, hotel, city travel', NULL),
  (3, 'seed_demo', NOW(6), 1, 'Business', 'Meetings and professional communication', NULL),
  (4, 'seed_demo', NOW(6), 1, 'Food', 'Ordering food and drinks', NULL)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `description` = VALUES(`description`), `status` = VALUES(`status`);

-- Scenarios for Next Topic + AI chat list
INSERT INTO `ai_scenarios` (
  `id`, `title`, `description`, `topic_id`, `level_id`, `type`,
  `ai_role`, `instruction`, `icon_url`, `generated_by_ai`, `generation_prompt`,
  `is_active`, `system_prompt`
)
VALUES
  (300, 'Coffee shop order', 'Order drinks politely', 4, 1, 'SCENARIO', 'Barista', 'Take drink order and ask follow-up questions.', NULL, 0, NULL, 1, NULL),
  (301, 'Restaurant reservation', 'Book a table by phone', 4, 2, 'SCENARIO', 'Receptionist', 'Ask for date, time, guests, and contact info.', NULL, 0, NULL, 1, NULL),
  (302, 'Hotel check-in', 'Check in at hotel desk', 2, 2, 'SCENARIO', 'Hotel receptionist', 'Collect booking details and give room info.', NULL, 0, NULL, 1, NULL),
  (303, 'Airport immigration', 'Answer immigration questions', 2, 2, 'SCENARIO', 'Immigration officer', 'Ask short factual questions.', NULL, 0, NULL, 1, NULL),
  (304, 'Team stand-up', 'Daily project update', 3, 3, 'SCENARIO', 'Team lead', 'Ask yesterday/today/blockers questions.', NULL, 0, NULL, 1, NULL),
  (305, 'Job interview short', 'Basic interview round', 3, 3, 'SCENARIO', 'Interviewer', 'Ask motivation and experience questions.', NULL, 0, NULL, 1, NULL),
  (306, 'Ask for directions', 'Find bus/train route', 1, 1, 'SCENARIO', 'Local resident', 'Guide learner with simple directions.', NULL, 0, NULL, 1, NULL),
  (307, 'Doctor appointment', 'Describe symptoms clearly', 1, 2, 'SCENARIO', 'Clinic doctor', 'Ask follow-up symptom and timeline questions.', NULL, 0, NULL, 1, NULL)
ON DUPLICATE KEY UPDATE
  `title` = VALUES(`title`),
  `description` = VALUES(`description`),
  `topic_id` = VALUES(`topic_id`),
  `level_id` = VALUES(`level_id`),
  `type` = VALUES(`type`),
  `ai_role` = VALUES(`ai_role`),
  `instruction` = VALUES(`instruction`),
  `is_active` = VALUES(`is_active`);

-- User profile + preferences
DELETE FROM `user_profile` WHERE `user_id` IN (1, 2);
INSERT INTO `user_profile` (
  `job_title`, `learning_goal`, `daily_goal_minutes`, `level_id`, `user_id`,
  `after_login_onboarding_completed`
)
VALUES
  ('Student', 'Speak naturally in daily situations', 20, 1, 1, 1),
  ('Engineer', 'Improve professional speaking confidence', 30, 2, 2, 1);

DELETE FROM `user_topic` WHERE `user_id` IN (1, 2);
INSERT INTO `user_topic` (`user_id`, `topic_id`)
VALUES (1,1),(1,4),(2,2),(2,3);

-- Decks + flashcards (drives Profile words learned + AI personalization)
SET @old_sql_safe_updates := @@SQL_SAFE_UPDATES;
SET SQL_SAFE_UPDATES = 0;

DELETE FROM `flashcards` WHERE `deck_id` IN (
  SELECT id FROM (SELECT id FROM `decks` WHERE `title` LIKE '[SEED DEMO] %') t
);
DELETE FROM `decks` WHERE `id` IN (
  SELECT id FROM (SELECT id FROM `decks` WHERE `title` LIKE '[SEED DEMO] %') t
);

SET SQL_SAFE_UPDATES = @old_sql_safe_updates;

INSERT INTO `decks` (`created_user`, `created_at`, `status`, `user_id`, `title`) VALUES
  ('seed_demo', NOW(6), 1, 1, '[SEED DEMO] Coffee essentials'),
  ('seed_demo', NOW(6), 1, 1, '[SEED DEMO] Travel basics'),
  ('seed_demo', NOW(6), 1, 1, '[SEED DEMO] Work phrases'),
  ('seed_demo', NOW(6), 1, 2, '[SEED DEMO] Daily conversation'),
  ('seed_demo', NOW(6), 1, 2, '[SEED DEMO] Interview expressions');

SET @d1 := (SELECT id FROM `decks` WHERE `user_id` = 1 AND `title` = '[SEED DEMO] Coffee essentials' LIMIT 1);
SET @d2 := (SELECT id FROM `decks` WHERE `user_id` = 1 AND `title` = '[SEED DEMO] Travel basics' LIMIT 1);
SET @d3 := (SELECT id FROM `decks` WHERE `user_id` = 1 AND `title` = '[SEED DEMO] Work phrases' LIMIT 1);
SET @d4 := (SELECT id FROM `decks` WHERE `user_id` = 2 AND `title` = '[SEED DEMO] Daily conversation' LIMIT 1);
SET @d5 := (SELECT id FROM `decks` WHERE `user_id` = 2 AND `title` = '[SEED DEMO] Interview expressions' LIMIT 1);

INSERT INTO `flashcards` (`created_user`, `created_at`, `status`, `deck_id`, `term`, `definition`, `image_url`) VALUES
  ('seed_demo', NOW(6), 1, @d1, 'black coffee', 'Coffee without milk', NULL),
  ('seed_demo', NOW(6), 1, @d1, 'coconut milk', 'Milk extracted from coconut', NULL),
  ('seed_demo', NOW(6), 1, @d1, 'takeaway', 'Food/drink to go', NULL),
  ('seed_demo', NOW(6), 1, @d1, 'extra shot', 'Additional espresso shot', NULL),
  ('seed_demo', NOW(6), 1, @d1, 'iced latte', 'Cold milk coffee', NULL),
  ('seed_demo', NOW(6), 1, @d2, 'boarding pass', 'Travel document for boarding', NULL),
  ('seed_demo', NOW(6), 1, @d2, 'departure gate', 'Gate to board flight', NULL),
  ('seed_demo', NOW(6), 1, @d2, 'carry-on baggage', 'Small bag in cabin', NULL),
  ('seed_demo', NOW(6), 1, @d2, 'hotel reservation', 'Booked room confirmation', NULL),
  ('seed_demo', NOW(6), 1, @d2, 'check-in counter', 'Airport check-in desk', NULL),
  ('seed_demo', NOW(6), 1, @d3, 'stand-up meeting', 'Short daily team update', NULL),
  ('seed_demo', NOW(6), 1, @d3, 'blocker', 'Issue that stops progress', NULL),
  ('seed_demo', NOW(6), 1, @d3, 'follow up', 'Continue discussion later', NULL),
  ('seed_demo', NOW(6), 1, @d3, 'deadline', 'Final due date', NULL),
  ('seed_demo', NOW(6), 1, @d3, 'action item', 'Task assigned after meeting', NULL),
  ('seed_demo', NOW(6), 1, @d4, 'small talk', 'Light casual conversation', NULL),
  ('seed_demo', NOW(6), 1, @d4, 'by the way', 'Phrase to add a side point', NULL),
  ('seed_demo', NOW(6), 1, @d4, 'sounds good', 'Expression of agreement', NULL),
  ('seed_demo', NOW(6), 1, @d4, 'no worries', 'It is okay / no problem', NULL),
  ('seed_demo', NOW(6), 1, @d4, 'catch up', 'Talk and update each other', NULL),
  ('seed_demo', NOW(6), 1, @d5, 'strength', 'Strong point or advantage', NULL),
  ('seed_demo', NOW(6), 1, @d5, 'weakness', 'Area to improve', NULL),
  ('seed_demo', NOW(6), 1, @d5, 'career goal', 'Long-term professional objective', NULL),
  ('seed_demo', NOW(6), 1, @d5, 'problem-solving', 'Ability to solve problems', NULL),
  ('seed_demo', NOW(6), 1, @d5, 'team player', 'Works well with others', NULL);

-- Clean previous seeded AI sessions (deep delete by title prefix)
SET SESSION sql_safe_updates = 0;

DELETE FROM `ai_session_summaries`
WHERE `session_id` IN (
  SELECT id FROM (SELECT id FROM `ai_chat_sessions` WHERE `title` LIKE '[SEED DEMO] %') x
);
DELETE FROM `ai_message_errors`
WHERE `feedback_id` IN (
  SELECT id FROM `ai_message_feedback`
  WHERE `message_id` IN (
    SELECT id FROM `ai_chat_messages`
    WHERE `session_id` IN (
      SELECT id FROM (SELECT id FROM `ai_chat_sessions` WHERE `title` LIKE '[SEED DEMO] %') y
    )
  )
);
DELETE FROM `ai_message_feedback`
WHERE `message_id` IN (
  SELECT id FROM `ai_chat_messages`
  WHERE `session_id` IN (
    SELECT id FROM (SELECT id FROM `ai_chat_sessions` WHERE `title` LIKE '[SEED DEMO] %') y
  )
);
DELETE FROM `ai_chat_messages`
WHERE `session_id` IN (
  SELECT id FROM (SELECT id FROM `ai_chat_sessions` WHERE `title` LIKE '[SEED DEMO] %') y
);
DELETE FROM `ai_chat_sessions` WHERE `title` LIKE '[SEED DEMO] %';

SET SESSION sql_safe_updates = 1;

-- Seed 4 ended sessions + 1 active session for user 1
INSERT INTO `ai_chat_sessions` (
  `user_id`, `scenario_id`, `topic_id`, `level_id`, `title`, `session_type`, `status`,
  `ai_role_snapshot`, `instruction_snapshot`, `max_turns`, `current_turn`,
  `started_at`, `ended_at`, `last_message_at`, `system_prompt_snapshot`,
  `goal_type`, `focus_skill`, `coaching_mode`, `fluency_mode`, `target_duration_minutes`,
  `mission_title`, `mission_objective`, `mission_success_criteria_json`, `mission_status`
)
VALUES
  (1, 300, 4, 1, '[SEED DEMO] Coffee order ended', 'SCENARIO', 'ENDED', 'Barista',
   'Practice ordering drinks politely.', 20, 3,
   DATE_SUB(NOW(6), INTERVAL 6 DAY), DATE_ADD(DATE_SUB(NOW(6), INTERVAL 6 DAY), INTERVAL 12 MINUTE), DATE_ADD(DATE_SUB(NOW(6), INTERVAL 6 DAY), INTERVAL 12 MINUTE), NULL,
   'DAILY_CONVERSATION', 'VOCABULARY', 'GENTLE', b'1', 12,
   'Order a drink naturally', 'Use polite requests and confirm order details', 'Use please|Ask size|Confirm takeaway', 'COMPLETED'),
  (1, 302, 2, 2, '[SEED DEMO] Hotel check-in ended', 'SCENARIO', 'ENDED', 'Receptionist',
   'Complete check-in dialogue with proper phrases.', 20, 4,
   DATE_SUB(NOW(6), INTERVAL 4 DAY), DATE_ADD(DATE_SUB(NOW(6), INTERVAL 4 DAY), INTERVAL 18 MINUTE), DATE_ADD(DATE_SUB(NOW(6), INTERVAL 4 DAY), INTERVAL 18 MINUTE), NULL,
   'TRAVEL_ENGLISH', 'GRAMMAR', 'STRICT', b'0', 18,
   'Check in smoothly', 'Provide clear booking details and ask for breakfast hours', 'State booking name|Ask facilities|Confirm checkout time', 'COMPLETED'),
  (1, 304, 3, 3, '[SEED DEMO] Team stand-up ended', 'SCENARIO', 'ENDED', 'Team lead',
   'Give concise update: yesterday/today/blockers.', 20, 5,
   DATE_SUB(NOW(6), INTERVAL 2 DAY), DATE_ADD(DATE_SUB(NOW(6), INTERVAL 2 DAY), INTERVAL 16 MINUTE), DATE_ADD(DATE_SUB(NOW(6), INTERVAL 2 DAY), INTERVAL 16 MINUTE), NULL,
   'WORK_CONFIDENCE', 'FLUENCY', 'GENTLE', b'1', 15,
   'Speak clearly in stand-up', 'Answer with short structured progress update', 'Mention blocker|Use present perfect once|Close with next action', 'ACTIVE'),
  (1, 306, 1, 1, '[SEED DEMO] Ask directions ended', 'SCENARIO', 'ENDED', 'Local resident',
   'Ask and understand route instructions.', 20, 2,
   DATE_SUB(NOW(6), INTERVAL 8 HOUR), DATE_ADD(DATE_SUB(NOW(6), INTERVAL 8 HOUR), INTERVAL 9 MINUTE), DATE_ADD(DATE_SUB(NOW(6), INTERVAL 8 HOUR), INTERVAL 9 MINUTE), NULL,
   'DAILY_CONVERSATION', 'PRONUNCIATION', 'GENTLE', b'1', 10,
   'Get to destination correctly', 'Repeat route steps and transport number', 'Ask follow-up|Repeat directions|Confirm stop name', 'COMPLETED'),
  (1, 301, 4, 2, '[SEED DEMO] Reservation active', 'SCENARIO', 'ACTIVE', 'Receptionist',
   'Book a table by phone and clarify details.', 20, 1,
   DATE_SUB(NOW(6), INTERVAL 30 MINUTE), NULL, DATE_SUB(NOW(6), INTERVAL 5 MINUTE), NULL,
   'DAILY_CONVERSATION', 'VOCABULARY', 'GENTLE', b'1', 8,
   'Book a restaurant table', 'Collect date/time/guests and phone number', 'Ask date and time|Confirm guest count', 'ACTIVE');

-- Session 1 detail (coffee)
SET @s1 := (SELECT id FROM `ai_chat_sessions` WHERE `title` = '[SEED DEMO] Coffee order ended' LIMIT 1);
INSERT INTO `ai_chat_messages` (`session_id`, `sender_type`, `input_type`, `turn_number`, `content`)
VALUES
  (@s1, 'USER', 'TEXT', 1, 'Hi, I want one black coffee and one coconut coffee.'),
  (@s1, 'AI', 'TEXT', 1, 'Sure! Would you like your black coffee hot or iced?'),
  (@s1, 'USER', 'TEXT', 2, 'Hot black coffee please, and coconut milk coffee with less sugar.'),
  (@s1, 'AI', 'TEXT', 2, 'Great choice. Anything else for takeaway?');
SET @m1 := (SELECT id FROM `ai_chat_messages` WHERE `session_id` = @s1 AND `sender_type` = 'USER' ORDER BY id ASC LIMIT 1);
INSERT INTO `ai_message_feedback` (`message_id`,`pronunciation_score`,`grammar_score`,`vocabulary_score`,`fluency_score`,`overall_comment`,`improved_version`,`natural_suggestion`,`error_count`)
VALUES (@m1, 8.1, 7.8, 8.3, 7.9, 'Good intent, but one phrase can be more natural.', 'Hi, I would like one black coffee and one coconut coffee.', 'I would like one black coffee and one coconut coffee.', 1);
SET @f1 := LAST_INSERT_ID();
INSERT INTO `ai_message_errors` (`feedback_id`,`error_type`,`original_text`,`suggested_text`,`explanation`)
VALUES (@f1, 'GRAMMAR', 'I want one black coffee', 'I would like one black coffee', 'More polite and natural in ordering context.');
INSERT INTO `ai_session_summaries` (`session_id`,`fluency_level`,`grammar_level`,`vocabulary_level`,`sentence_count`,`error_count`,`duration_seconds`,`next_suggestion`,`related_topics`)
VALUES (@s1, 'GOOD', 'FAIR', 'GOOD', 4, 1, 720, 'Try a restaurant ordering scenario next.', 'Food ordering; polite requests');

-- Session 2 detail (hotel)
SET @s2 := (SELECT id FROM `ai_chat_sessions` WHERE `title` = '[SEED DEMO] Hotel check-in ended' LIMIT 1);
INSERT INTO `ai_chat_messages` (`session_id`, `sender_type`, `input_type`, `turn_number`, `content`)
VALUES
  (@s2, 'USER', 'TEXT', 1, 'Hello, I have booking under Tran name.'),
  (@s2, 'AI', 'TEXT', 1, 'Welcome. Could you show your ID, please?'),
  (@s2, 'USER', 'TEXT', 2, 'Yes, and breakfast include?'),
  (@s2, 'AI', 'TEXT', 2, 'Yes, breakfast is included from 6:30 to 10:00.');
SET @m2 := (SELECT id FROM `ai_chat_messages` WHERE `session_id` = @s2 AND `sender_type` = 'USER' ORDER BY id ASC LIMIT 1);
INSERT INTO `ai_message_feedback` (`message_id`,`pronunciation_score`,`grammar_score`,`vocabulary_score`,`fluency_score`,`overall_comment`,`improved_version`,`natural_suggestion`,`error_count`)
VALUES (@m2, 7.5, 7.2, 7.7, 7.3, 'Understandable. Minor grammar fixes needed.', 'Hello, I have a booking under the name Tran.', 'I have a booking under the name Tran.', 1);
SET @f2 := LAST_INSERT_ID();
INSERT INTO `ai_message_errors` (`feedback_id`,`error_type`,`original_text`,`suggested_text`,`explanation`)
VALUES (@f2, 'GRAMMAR', 'I have booking under Tran name', 'I have a booking under the name Tran', 'Articles and word order are needed.');
INSERT INTO `ai_session_summaries` (`session_id`,`fluency_level`,`grammar_level`,`vocabulary_level`,`sentence_count`,`error_count`,`duration_seconds`,`next_suggestion`,`related_topics`)
VALUES (@s2, 'FAIR', 'NEED_IMPROVEMENT', 'GOOD', 4, 1, 1080, 'Practice airport immigration Q&A.', 'Hotel; travel check-in');

-- Session 3 detail (stand-up)
SET @s3 := (SELECT id FROM `ai_chat_sessions` WHERE `title` = '[SEED DEMO] Team stand-up ended' LIMIT 1);
INSERT INTO `ai_chat_messages` (`session_id`, `sender_type`, `input_type`, `turn_number`, `content`)
VALUES
  (@s3, 'USER', 'TEXT', 1, 'Yesterday I fix login bug and today I work dashboard.'),
  (@s3, 'AI', 'TEXT', 1, 'Nice. Any blocker at the moment?'),
  (@s3, 'USER', 'TEXT', 2, 'I am waiting API from backend team.'),
  (@s3, 'AI', 'TEXT', 2, 'Got it. Please share the dependency in the channel.');
SET @m3 := (SELECT id FROM `ai_chat_messages` WHERE `session_id` = @s3 AND `sender_type` = 'USER' ORDER BY id ASC LIMIT 1);
INSERT INTO `ai_message_feedback` (`message_id`,`pronunciation_score`,`grammar_score`,`vocabulary_score`,`fluency_score`,`overall_comment`,`improved_version`,`natural_suggestion`,`error_count`)
VALUES (@m3, 7.8, 6.8, 7.9, 7.2, 'Clear update, but tense consistency can improve.', 'Yesterday I fixed a login bug, and today I am working on the dashboard.', 'I fixed a login bug yesterday, and today I am working on the dashboard.', 1);
SET @f3 := LAST_INSERT_ID();
INSERT INTO `ai_message_errors` (`feedback_id`,`error_type`,`original_text`,`suggested_text`,`explanation`)
VALUES (@f3, 'GRAMMAR', 'Yesterday I fix login bug', 'Yesterday I fixed a login bug', 'Use past tense for completed actions.');
INSERT INTO `ai_session_summaries` (`session_id`,`fluency_level`,`grammar_level`,`vocabulary_level`,`sentence_count`,`error_count`,`duration_seconds`,`next_suggestion`,`related_topics`)
VALUES (@s3, 'GOOD', 'NEED_IMPROVEMENT', 'GOOD', 4, 1, 960, 'Try a short interview scenario.', 'Work meeting; stand-up');

-- Session 4 detail (directions)
SET @s4 := (SELECT id FROM `ai_chat_sessions` WHERE `title` = '[SEED DEMO] Ask directions ended' LIMIT 1);
INSERT INTO `ai_chat_messages` (`session_id`, `sender_type`, `input_type`, `turn_number`, `content`)
VALUES
  (@s4, 'USER', 'TEXT', 1, 'Excuse me, where is bus station near here?'),
  (@s4, 'AI', 'TEXT', 1, 'Go straight two blocks and turn left at the bank.');
SET @m4 := (SELECT id FROM `ai_chat_messages` WHERE `session_id` = @s4 AND `sender_type` = 'USER' ORDER BY id ASC LIMIT 1);
INSERT INTO `ai_message_feedback` (`message_id`,`pronunciation_score`,`grammar_score`,`vocabulary_score`,`fluency_score`,`overall_comment`,`improved_version`,`natural_suggestion`,`error_count`)
VALUES (@m4, 8.0, 7.0, 7.4, 7.8, 'Good question structure with small article omission.', 'Excuse me, where is the bus station near here?', 'Where is the nearest bus station?', 1);
SET @f4 := LAST_INSERT_ID();
INSERT INTO `ai_message_errors` (`feedback_id`,`error_type`,`original_text`,`suggested_text`,`explanation`)
VALUES (@f4, 'GRAMMAR', 'where is bus station', 'where is the bus station', 'Use "the" before specific countable nouns.');
INSERT INTO `ai_session_summaries` (`session_id`,`fluency_level`,`grammar_level`,`vocabulary_level`,`sentence_count`,`error_count`,`duration_seconds`,`next_suggestion`,`related_topics`)
VALUES (@s4, 'GOOD', 'FAIR', 'FAIR', 2, 1, 540, 'Practice airport transportation dialogue.', 'Directions; transport');

-- Optional validation queries:
-- SELECT COUNT(*) AS scenarios FROM ai_scenarios WHERE id BETWEEN 300 AND 307;
-- SELECT COUNT(*) AS sessions_seed_demo FROM ai_chat_sessions WHERE title LIKE '[SEED DEMO] %';
-- SELECT COUNT(*) AS cards_user1 FROM flashcards f JOIN decks d ON d.id = f.deck_id WHERE d.user_id = 1;
-- SELECT id, title, goal_type, focus_skill, coaching_mode, mission_status FROM ai_chat_sessions WHERE user_id = 1 ORDER BY id DESC LIMIT 10;
