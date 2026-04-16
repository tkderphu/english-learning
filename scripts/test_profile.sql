-- =========================================
-- PROFILE TEST SEED (user_id = 3) - V3
-- Safe-update friendly + duplicate-proof
-- =========================================

USE english;

START TRANSACTION;

SET @USER_ID := 3;

-- ---------- 0) PRECHECK ----------
SELECT id, email FROM `user` WHERE id = @USER_ID;

SET @BOOK1 := (SELECT id FROM `book` ORDER BY id LIMIT 1);
SET @BOOK2 := (SELECT id FROM `book` ORDER BY id LIMIT 1 OFFSET 1);
SET @LEVEL1 := (SELECT id FROM `level` ORDER BY id LIMIT 1);
SET @TOPIC1 := (SELECT id FROM `topic` ORDER BY id LIMIT 1);
SET @SCENARIO1 := (SELECT id FROM `ai_scenarios` ORDER BY id LIMIT 1);

SELECT
    @BOOK1 AS book1_id,
    @BOOK2 AS book2_id,
    @LEVEL1 AS level1_id,
    @TOPIC1 AS topic1_id,
    @SCENARIO1 AS scenario1_id;

-- ---------- 1) CLEAN OLD TEST DATA ----------
DELETE e
FROM ai_message_errors e
JOIN ai_message_feedback f ON e.feedback_id = f.id
JOIN ai_chat_messages m ON f.message_id = m.id
JOIN ai_chat_sessions s ON m.session_id = s.id
WHERE e.id > 0
  AND s.user_id = @USER_ID
  AND s.title LIKE '[PROFILE_TEST]%';

DELETE f
FROM ai_message_feedback f
JOIN ai_chat_messages m ON f.message_id = m.id
JOIN ai_chat_sessions s ON m.session_id = s.id
WHERE f.id > 0
  AND s.user_id = @USER_ID
  AND s.title LIKE '[PROFILE_TEST]%';

DELETE m
FROM ai_chat_messages m
JOIN ai_chat_sessions s ON m.session_id = s.id
WHERE m.id > 0
  AND s.user_id = @USER_ID
  AND s.title LIKE '[PROFILE_TEST]%';

DELETE FROM ai_chat_sessions
WHERE id > 0
  AND user_id = @USER_ID
  AND title LIKE '[PROFILE_TEST]%';

DELETE FROM learning_activities
WHERE id > 0
  AND user_id = @USER_ID
  AND (
    title LIKE '[PROFILE_TEST]%'
        OR detail_json LIKE '%"profile_test":true%'
    );

-- xóa theo source_module test
DELETE FROM user_learned_words
WHERE id > 0
  AND user_id = @USER_ID
  AND source_module = 'PROFILE_TEST';

-- xóa thêm theo term để tránh trùng unique (user_id,term)
DELETE FROM user_learned_words
WHERE id > 0
  AND user_id = @USER_ID
  AND term IN (
               'check-in','reservation','itinerary','amenities',
               'complimentary','luggage','vacancy','upgrade'
    );

-- ---------- 2) VOCABULARY ----------
INSERT INTO user_learned_words
(user_id, term, phonetic, definition, source_module, is_favorite, needs_attention, audio_url, created_at)
VALUES
    (@USER_ID, 'check-in', '/ˈtʃek ɪn/', 'to register at a hotel', 'PROFILE_TEST', 1, 0, NULL, NOW() - INTERVAL 6 DAY),
    (@USER_ID, 'reservation', '/ˌrez.əˈveɪ.ʃən/', 'an arrangement to have something kept', 'PROFILE_TEST', 0, 0, NULL, NOW() - INTERVAL 5 DAY),
    (@USER_ID, 'itinerary', '/aɪˈtɪn.ər.əri/', 'a plan of a journey', 'PROFILE_TEST', 0, 1, NULL, NOW() - INTERVAL 4 DAY),
    (@USER_ID, 'amenities', '/əˈmiː.nə.tiz/', 'useful or pleasant facilities', 'PROFILE_TEST', 1, 0, NULL, NOW() - INTERVAL 3 DAY),
    (@USER_ID, 'complimentary', '/ˌkɒm.plɪˈmen.tər.i/', 'given free of charge', 'PROFILE_TEST', 0, 0, NULL, NOW() - INTERVAL 2 DAY),
    (@USER_ID, 'luggage', '/ˈlʌɡ.ɪdʒ/', 'bags and suitcases', 'PROFILE_TEST', 0, 0, NULL, NOW() - INTERVAL 1 DAY),
    (@USER_ID, 'vacancy', '/ˈveɪ.kən.si/', 'an empty room or position', 'PROFILE_TEST', 0, 0, NULL, NOW() - INTERVAL 12 HOUR),
    (@USER_ID, 'upgrade', '/ʌpˈɡreɪd/', 'to improve to a better level', 'PROFILE_TEST', 1, 0, NULL, NOW() - INTERVAL 1 HOUR);

-- ---------- 3) AI CHAT DATA (for Corrections + Session Review) ----------
INSERT INTO ai_chat_sessions
(user_id, scenario_id, topic_id, level_id, title, session_type, status,
 ai_role_snapshot, instruction_snapshot, max_turns, current_turn, started_at, ended_at, last_message_at, system_prompt_snapshot)
VALUES
    (@USER_ID, @SCENARIO1, @TOPIC1, @LEVEL1,
     '[PROFILE_TEST] Hotel receptionist',
     'SCENARIO', 'ENDED',
     'Hotel receptionist',
     'Handle check-in: room type, ID, breakfast hours. Stay professional.',
     20, 3,
     NOW() - INTERVAL 1 DAY - INTERVAL 40 MINUTE,
     NOW() - INTERVAL 1 DAY - INTERVAL 25 MINUTE,
     NOW() - INTERVAL 1 DAY - INTERVAL 25 MINUTE,
     'profile_test_prompt');

SET @SESSION_A := LAST_INSERT_ID();

INSERT INTO ai_chat_messages
(session_id, sender_type, input_type, turn_number, content, normalized_content, audio_url, audio_duration, stt_transcript, created_at)
VALUES
    (@SESSION_A, 'USER', 'TEXT', 1, 'I has a reservation for tonight.', 'I has a reservation for tonight.', NULL, NULL, NULL, NOW() - INTERVAL 1 DAY - INTERVAL 39 MINUTE),
    (@SESSION_A, 'ASSISTANT', 'TEXT', 1, 'Welcome! May I have your booking name and ID, please?', NULL, NULL, NULL, NULL, NOW() - INTERVAL 1 DAY - INTERVAL 38 MINUTE),
    (@SESSION_A, 'USER', 'TEXT', 2, 'Can I get a room with sea view and free breakfasts?', 'Can I get a room with sea view and free breakfasts?', NULL, NULL, NULL, NOW() - INTERVAL 1 DAY - INTERVAL 35 MINUTE),
    (@SESSION_A, 'ASSISTANT', 'TEXT', 2, 'Sure. We have sea-view rooms and complimentary breakfast from 6 to 10 AM.', NULL, NULL, NULL, NULL, NOW() - INTERVAL 1 DAY - INTERVAL 34 MINUTE);

SET @MSG_A_U1 := (
  SELECT id FROM ai_chat_messages
  WHERE session_id = @SESSION_A AND sender_type = 'USER' AND turn_number = 1
  ORDER BY id DESC LIMIT 1
);

INSERT INTO ai_message_feedback
(message_id, pronunciation_score, grammar_score, vocabulary_score, fluency_score,
 overall_comment, improved_version, natural_suggestion, error_count, created_at)
VALUES
    (@MSG_A_U1, 80.00, 62.00, 76.00, 70.00,
     'Good intent, but subject-verb agreement needs correction.',
     'I have a reservation for tonight.',
     'Use "I have" instead of "I has".',
     1,
     NOW() - INTERVAL 1 DAY - INTERVAL 39 MINUTE);

SET @FB_A_U1 := LAST_INSERT_ID();

INSERT INTO ai_message_errors
(feedback_id, error_type, original_text, suggested_text, explanation)
VALUES
    (@FB_A_U1, 'GRAMMAR', 'I has a reservation for tonight.', 'I have a reservation for tonight.', 'Subject-verb agreement: use "have" with "I".');

SET @MSG_A_U2 := (
  SELECT id FROM ai_chat_messages
  WHERE session_id = @SESSION_A AND sender_type = 'USER' AND turn_number = 2
  ORDER BY id DESC LIMIT 1
);

INSERT INTO ai_message_feedback
(message_id, pronunciation_score, grammar_score, vocabulary_score, fluency_score,
 overall_comment, improved_version, natural_suggestion, error_count, created_at)
VALUES
    (@MSG_A_U2, 82.00, 74.00, 67.00, 72.00,
     'Meaning is clear, but wording can be more natural.',
     'Can I get a sea-view room with complimentary breakfast?',
     'Use "complimentary breakfast" in hospitality context.',
     2,
     NOW() - INTERVAL 1 DAY - INTERVAL 35 MINUTE);

SET @FB_A_U2 := LAST_INSERT_ID();

INSERT INTO ai_message_errors
(feedback_id, error_type, original_text, suggested_text, explanation)
VALUES
    (@FB_A_U2, 'VOCABULARY', 'free breakfasts', 'complimentary breakfast', 'More natural collocation in hospitality English.'),
    (@FB_A_U2, 'SPELLING', 'sea view', 'sea-view', 'Hyphenated adjective before noun is preferred.');

INSERT INTO ai_chat_sessions
(user_id, scenario_id, topic_id, level_id, title, session_type, status,
 ai_role_snapshot, instruction_snapshot, max_turns, current_turn, started_at, ended_at, last_message_at, system_prompt_snapshot)
VALUES
    (@USER_ID, NULL, @TOPIC1, @LEVEL1,
     '[PROFILE_TEST] Daily conversation - travel',
     'FREE_CHAT', 'ENDED',
     'Friendly English tutor',
     'Talk about travel plans and daily life.',
     20, 2,
     NOW() - INTERVAL 3 HOUR,
     NOW() - INTERVAL 2 HOUR,
     NOW() - INTERVAL 2 HOUR,
     'profile_test_prompt');

SET @SESSION_B := LAST_INSERT_ID();

INSERT INTO ai_chat_messages
(session_id, sender_type, input_type, turn_number, content, normalized_content, audio_url, audio_duration, stt_transcript, created_at)
VALUES
    (@SESSION_B, 'USER', 'TEXT', 1, 'Yesterday I goed to the airport.', 'Yesterday I goed to the airport.', NULL, NULL, NULL, NOW() - INTERVAL 3 HOUR),
    (@SESSION_B, 'ASSISTANT', 'TEXT', 1, 'Nice! You can say: "Yesterday I went to the airport."', NULL, NULL, NULL, NULL, NOW() - INTERVAL 2 HOUR - INTERVAL 50 MINUTE);

SET @MSG_B_U1 := (
  SELECT id FROM ai_chat_messages
  WHERE session_id = @SESSION_B AND sender_type = 'USER' AND turn_number = 1
  ORDER BY id DESC LIMIT 1
);

INSERT INTO ai_message_feedback
(message_id, pronunciation_score, grammar_score, vocabulary_score, fluency_score,
 overall_comment, improved_version, natural_suggestion, error_count, created_at)
VALUES
    (@MSG_B_U1, 78.00, 58.00, 70.00, 68.00,
     'Great attempt. Past tense form is incorrect.',
     'Yesterday I went to the airport.',
     'Irregular verb: go -> went.',
     1,
     NOW() - INTERVAL 3 HOUR);

SET @FB_B_U1 := LAST_INSERT_ID();

INSERT INTO ai_message_errors
(feedback_id, error_type, original_text, suggested_text, explanation)
VALUES
    (@FB_B_U1, 'GRAMMAR', 'Yesterday I goed to the airport.', 'Yesterday I went to the airport.', 'Use irregular past tense "went".');

-- ---------- 4) LEARNING ACTIVITIES (for Overview/Heatmap/Completed/Daily Detail) ----------
INSERT INTO learning_activities
(user_id, activity_type, title, started_at, ended_at, duration_seconds, score_percent, words_new_count, detail_json, reference_type, reference_id)
VALUES
    (@USER_ID, 'BOOK', '[PROFILE_TEST] Book reading session A', NOW() - INTERVAL 50 MINUTE, NOW() - INTERVAL 30 MINUTE, 1200, 92.00, NULL, '{"profile_test":true,"bookSession":"A"}', 'BOOK', @BOOK1),
    (@USER_ID, 'EXERCISE', '[PROFILE_TEST] Grammar drill - Present Perfect', NOW() - INTERVAL 28 MINUTE, NOW() - INTERVAL 18 MINUTE, 600, 88.00, NULL, '{"profile_test":true,"exercise":"grammar"}', 'BOOK', @BOOK1),
    (@USER_ID, 'AI_CHAT', '[PROFILE_TEST] Hotel receptionist', NOW() - INTERVAL 17 MINUTE, NOW() - INTERVAL 7 MINUTE, 600, NULL, NULL, '{"profile_test":true,"source":"ai_chat"}', 'AI_CHAT_SESSION', @SESSION_A),

    (@USER_ID, 'BOOK', '[PROFILE_TEST] Book reading session B', NOW() - INTERVAL 1 DAY - INTERVAL 45 MINUTE, NOW() - INTERVAL 1 DAY - INTERVAL 25 MINUTE, 1200, 85.00, NULL, '{"profile_test":true,"bookSession":"B"}', 'BOOK', @BOOK2),
    (@USER_ID, 'EXERCISE', '[PROFILE_TEST] Vocabulary quiz', NOW() - INTERVAL 1 DAY - INTERVAL 22 MINUTE, NOW() - INTERVAL 1 DAY - INTERVAL 10 MINUTE, 720, 76.00, NULL, '{"profile_test":true,"exercise":"vocab"}', 'BOOK', @BOOK2),

    (@USER_ID, 'FLASHCARD', '[PROFILE_TEST] Travel flashcards', NOW() - INTERVAL 3 DAY - INTERVAL 35 MINUTE, NOW() - INTERVAL 3 DAY - INTERVAL 20 MINUTE, 900, NULL, 15, '{"profile_test":true,"deck":"travel"}', 'DECK', NULL),

    (@USER_ID, 'BOOK', '[PROFILE_TEST] Book reading session C', NOW() - INTERVAL 5 DAY - INTERVAL 40 MINUTE, NOW() - INTERVAL 5 DAY - INTERVAL 20 MINUTE, 1200, 90.00, NULL, '{"profile_test":true,"bookSession":"C"}', 'BOOK', @BOOK1);

COMMIT;

-- ---------- 5) VERIFY ----------
SELECT activity_type, COUNT(*) AS cnt
FROM learning_activities
WHERE user_id = @USER_ID
GROUP BY activity_type
ORDER BY activity_type;

SELECT s.id AS session_id, s.title, s.session_type, COUNT(e.id) AS error_count
FROM ai_chat_sessions s
         LEFT JOIN ai_chat_messages m ON m.session_id = s.id
         LEFT JOIN ai_message_feedback f ON f.message_id = m.id
         LEFT JOIN ai_message_errors e ON e.feedback_id = f.id
WHERE s.user_id = @USER_ID
  AND s.title LIKE '[PROFILE_TEST]%'
GROUP BY s.id, s.title, s.session_type
ORDER BY s.id DESC;

SELECT COUNT(*) AS vocab_test_count
FROM user_learned_words
WHERE user_id = @USER_ID
  AND source_module = 'PROFILE_TEST';