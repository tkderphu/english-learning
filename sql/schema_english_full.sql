-- =============================================================================
-- English Learning Backend — full MySQL schema (aligned with JPA entities)
-- Database name matches application.properties: english
-- Charset: utf8mb4 (recommended for Spring Boot + JPA)
--
-- Usage:
--   mysql -u root -p < schema_english_full.sql
-- Then optionally load seed data:
--   mysql -u root -p english < ../src/main/resources/data.sql
--
-- Note: spring.jpa.hibernate.ddl-auto=update can also create/update tables;
-- this script is for clean installs, CI, or documentation.
-- =============================================================================

CREATE DATABASE IF NOT EXISTS `english`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `english`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ---------------------------------------------------------------------------
-- Core / catalog (extends BaseEntity: audit + status)
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `user` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `created_user`  VARCHAR(255) NULL,
  `created_at`    DATETIME(6) NULL,
  `modified_by`   VARCHAR(255) NULL,
  `modified_at`   DATETIME(6) NULL,
  `status`        INT NOT NULL DEFAULT 0,
  `email`         VARCHAR(255) NULL,
  `password`      VARCHAR(255) NOT NULL,
  `full_name`     VARCHAR(255) NULL,
  `avatar`        VARCHAR(255) NULL,
  `role`          INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `book` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `created_user`  VARCHAR(255) NULL,
  `created_at`    DATETIME(6) NULL,
  `modified_by`   VARCHAR(255) NULL,
  `modified_at`   DATETIME(6) NULL,
  `status`        INT NOT NULL DEFAULT 0,
  `title`         VARCHAR(255) NULL,
  `language`      VARCHAR(255) NULL,
  `cover_url`     VARCHAR(500) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `author` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `created_user`  VARCHAR(255) NULL,
  `created_at`    DATETIME(6) NULL,
  `modified_by`   VARCHAR(255) NULL,
  `modified_at`   DATETIME(6) NULL,
  `status`        INT NOT NULL DEFAULT 0,
  `name`          VARCHAR(255) NULL,
  `avatar`        VARCHAR(255) NULL,
  `nationality`   VARCHAR(255) NULL,
  `biography`     VARCHAR(1000) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `genre` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `created_user`  VARCHAR(255) NULL,
  `created_at`    DATETIME(6) NULL,
  `modified_by`   VARCHAR(255) NULL,
  `modified_at`   DATETIME(6) NULL,
  `status`        INT NOT NULL DEFAULT 0,
  `name`          VARCHAR(255) NULL,
  `thumbnail`     VARCHAR(500) NULL,
  `description`   VARCHAR(1000) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `level` (
  `id`              INT NOT NULL AUTO_INCREMENT,
  `created_user`    VARCHAR(255) NULL,
  `created_at`      DATETIME(6) NULL,
  `modified_by`     VARCHAR(255) NULL,
  `modified_at`     DATETIME(6) NULL,
  `status`          INT NOT NULL DEFAULT 0,
  `name`            VARCHAR(255) NULL,
  `description`     VARCHAR(1000) NULL,
  `number_course`   INT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `topic` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `created_user`  VARCHAR(255) NULL,
  `created_at`    DATETIME(6) NULL,
  `modified_by`   VARCHAR(255) NULL,
  `modified_at`   DATETIME(6) NULL,
  `status`        INT NOT NULL DEFAULT 0,
  `name`          VARCHAR(255) NULL,
  `description`   VARCHAR(1000) NULL,
  `thumbnail`     VARCHAR(500) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `audio` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `created_user`  VARCHAR(255) NULL,
  `created_at`    DATETIME(6) NULL,
  `modified_by`   VARCHAR(255) NULL,
  `modified_at`   DATETIME(6) NULL,
  `status`        INT NOT NULL DEFAULT 0,
  `duration`      BIGINT NOT NULL,
  `format`        VARCHAR(255) NULL,
  `sample_rate`   DOUBLE NULL,
  `file_size`     DOUBLE NULL,
  `file_url`      VARCHAR(500) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `files` (
  `id`             INT NOT NULL AUTO_INCREMENT,
  `created_user`   VARCHAR(255) NULL,
  `created_at`     DATETIME(6) NULL,
  `modified_by`    VARCHAR(255) NULL,
  `modified_at`    DATETIME(6) NULL,
  `status`         INT NOT NULL DEFAULT 0,
  `file_name`      VARCHAR(255) NOT NULL,
  `original_name`  VARCHAR(255) NOT NULL,
  `content_type`   VARCHAR(255) NULL,
  `size`           BIGINT NOT NULL,
  `path`           VARCHAR(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------------
-- Book structure
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `chapter` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `created_user`  VARCHAR(255) NULL,
  `created_at`    DATETIME(6) NULL,
  `modified_by`   VARCHAR(255) NULL,
  `modified_at`   DATETIME(6) NULL,
  `status`        INT NOT NULL DEFAULT 0,
  `book_id`       INT NULL,
  `title`         VARCHAR(255) NULL,
  `description`   VARCHAR(2000) NULL,
  `number`        INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_chapter_book` (`book_id`),
  CONSTRAINT `fk_chapter_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `page` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `created_user`  VARCHAR(255) NULL,
  `created_at`    DATETIME(6) NULL,
  `modified_by`   VARCHAR(255) NULL,
  `modified_at`   DATETIME(6) NULL,
  `status`        INT NOT NULL DEFAULT 0,
  `chapter_id`    INT NULL,
  `number`        INT NOT NULL,
  `audio_id`      INT NULL,
  `content`       TEXT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_page_chapter` (`chapter_id`),
  KEY `idx_page_audio` (`audio_id`),
  CONSTRAINT `fk_page_chapter` FOREIGN KEY (`chapter_id`) REFERENCES `chapter` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_page_audio` FOREIGN KEY (`audio_id`) REFERENCES `audio` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `sentence` (
  `id`              INT NOT NULL AUTO_INCREMENT,
  `created_user`    VARCHAR(255) NULL,
  `created_at`      DATETIME(6) NULL,
  `modified_by`     VARCHAR(255) NULL,
  `modified_at`     DATETIME(6) NULL,
  `status`          INT NOT NULL DEFAULT 0,
  `page_id`         INT NULL,
  `content`         VARCHAR(2000) NULL,
  `transcription1`  VARCHAR(2000) NULL,
  `start_time`      BIGINT NOT NULL,
  `end_time`        BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sentence_page` (`page_id`),
  CONSTRAINT `fk_sentence_page` FOREIGN KEY (`page_id`) REFERENCES `page` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `author_book` (
  `id`        INT NOT NULL AUTO_INCREMENT,
  `author_id` INT NULL,
  `book_id`   INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_author_book_author` (`author_id`),
  KEY `idx_author_book_book` (`book_id`),
  CONSTRAINT `fk_author_book_author` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_author_book_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `book_genre` (
  `id`        INT NOT NULL AUTO_INCREMENT,
  `genre_id`  INT NULL,
  `book_id`   INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_book_genre_genre` (`genre_id`),
  KEY `idx_book_genre_book` (`book_id`),
  CONSTRAINT `fk_book_genre_genre` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_book_genre_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `book_progress` (
  `id`                     INT NOT NULL AUTO_INCREMENT,
  `created_user`           VARCHAR(255) NULL,
  `created_at`             DATETIME(6) NULL,
  `modified_by`            VARCHAR(255) NULL,
  `modified_at`            DATETIME(6) NULL,
  `status`                 INT NOT NULL DEFAULT 0,
  `user_id`                INT NULL,
  `book_id`                INT NULL,
  `progress_percent`       DOUBLE NOT NULL,
  `last_read_time`         DATETIME(6) NULL,
  `is_favorite`            INT NULL,
  `last_read_page_number`  INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_book_progress_user` (`user_id`),
  KEY `idx_book_progress_book` (`book_id`),
  CONSTRAINT `fk_book_progress_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_book_progress_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------------
-- Flashcards / decks
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `decks` (
  `id`               INT NOT NULL AUTO_INCREMENT,
  `created_user`     VARCHAR(255) NULL,
  `created_at`       DATETIME(6) NULL,
  `modified_by`      VARCHAR(255) NULL,
  `modified_at`      DATETIME(6) NULL,
  `status`           INT NOT NULL DEFAULT 0,
  `user_id`          INT NOT NULL,
  `title`            VARCHAR(255) NOT NULL,
  `cover_image_url`  VARCHAR(500) NULL,
  `total_words`      INT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_decks_user` (`user_id`),
  CONSTRAINT `fk_decks_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `flashcards` (
  `id`                 INT NOT NULL AUTO_INCREMENT,
  `created_user`       VARCHAR(255) NULL,
  `created_at`         DATETIME(6) NULL,
  `modified_by`        VARCHAR(255) NULL,
  `modified_at`        DATETIME(6) NULL,
  `status`             INT NOT NULL DEFAULT 0,
  `deck_id`            INT NOT NULL,
  `word`               VARCHAR(255) NOT NULL,
  `phonetic`           VARCHAR(255) NULL,
  `part_of_speech`     VARCHAR(255) NULL,
  `meaning`            TEXT NULL,
  `example_sentence`   TEXT NULL,
  `visual_cue_url`     VARCHAR(500) NULL,
  `note`               TEXT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_flashcards_deck` (`deck_id`),
  CONSTRAINT `fk_flashcards_deck` FOREIGN KEY (`deck_id`) REFERENCES `decks` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------------
-- Auth / user prefs
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `user_session` (
  `id`             INT NOT NULL AUTO_INCREMENT,
  `created_user`   VARCHAR(255) NULL,
  `created_at`     DATETIME(6) NULL,
  `modified_by`    VARCHAR(255) NULL,
  `modified_at`    DATETIME(6) NULL,
  `status`         INT NOT NULL DEFAULT 0,
  `user_id`        INT NOT NULL,
  `refresh_token`  VARCHAR(500) NULL,
  `remote_ip`      VARCHAR(255) NULL,
  `user_agent`     VARCHAR(500) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_user_session_refresh` (`refresh_token`),
  KEY `idx_user_session_user` (`user_id`),
  CONSTRAINT `fk_user_session_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `user_profile` (
  `id`                  INT NOT NULL AUTO_INCREMENT,
  `job_title`           VARCHAR(255) NULL,
  `learning_goal`       VARCHAR(500) NULL,
  `daily_goal_minutes`  INT NOT NULL,
  `level_id`            INT NULL,
  `user_id`             INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_profile_user` (`user_id`),
  KEY `idx_user_profile_level` (`level_id`),
  CONSTRAINT `fk_user_profile_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_profile_level` FOREIGN KEY (`level_id`) REFERENCES `level` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `user_topic` (
  `id`        INT NOT NULL AUTO_INCREMENT,
  `topic_id`  INT NULL,
  `user_id`   INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_topic_user` (`user_id`),
  KEY `idx_user_topic_topic` (`topic_id`),
  CONSTRAINT `fk_user_topic_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_topic_topic` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `user_genre` (
  `id`        INT NOT NULL AUTO_INCREMENT,
  `user_id`   INT NULL,
  `genre_id`  INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_genre_user` (`user_id`),
  KEY `idx_user_genre_genre` (`genre_id`),
  CONSTRAINT `fk_user_genre_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_genre_genre` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `password_reset_otps` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `created_user` VARCHAR(255) NULL,
  `created_at`  DATETIME(6) NULL,
  `modified_by` VARCHAR(255) NULL,
  `modified_at` DATETIME(6) NULL,
  `status`      INT NOT NULL DEFAULT 0,
  `email`       VARCHAR(255) NOT NULL,
  `otp`         VARCHAR(255) NOT NULL,
  `expires_at`  DATETIME(6) NOT NULL,
  `used`        BIT(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `idx_password_reset_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------------
-- AI chat / scenarios (no BaseEntity)
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `ai_scenarios` (
  `id`                 INT NOT NULL AUTO_INCREMENT,
  `title`              VARCHAR(500) NULL,
  `description`        TEXT NULL,
  `topic_id`           INT NULL,
  `level_id`           INT NULL,
  `type`               VARCHAR(100) NULL,
  `ai_role`            VARCHAR(500) NULL,
  `instruction`        TEXT NULL,
  `icon_url`           VARCHAR(500) NULL,
  `generated_by_ai`    TINYINT(1) NULL,
  `generation_prompt`  TEXT NULL,
  `is_active`          TINYINT(1) NULL,
  `system_prompt`      TEXT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ai_scenarios_topic` (`topic_id`),
  KEY `idx_ai_scenarios_level` (`level_id`),
  CONSTRAINT `fk_ai_scenarios_topic` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ai_scenarios_level` FOREIGN KEY (`level_id`) REFERENCES `level` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ai_chat_sessions` (
  `id`                       INT NOT NULL AUTO_INCREMENT,
  `user_id`                  INT NULL,
  `scenario_id`              INT NULL,
  `topic_id`                 INT NULL,
  `level_id`                 INT NULL,
  `title`                    VARCHAR(500) NULL,
  `session_type`             VARCHAR(50) NULL,
  `status`                   VARCHAR(50) NULL,
  `ai_role_snapshot`         VARCHAR(500) NULL,
  `instruction_snapshot`     TEXT NULL,
  `max_turns`                INT NULL,
  `current_turn`             INT NULL,
  `started_at`               DATETIME(6) NULL,
  `ended_at`                 DATETIME(6) NULL,
  `last_message_at`          DATETIME(6) NULL,
  `created_at`               DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6),
  `system_prompt_snapshot`   TEXT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ai_chat_sessions_user` (`user_id`),
  KEY `idx_ai_chat_sessions_scenario` (`scenario_id`),
  CONSTRAINT `fk_ai_chat_sessions_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ai_chat_sessions_scenario` FOREIGN KEY (`scenario_id`) REFERENCES `ai_scenarios` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ai_chat_messages` (
  `id`                  INT NOT NULL AUTO_INCREMENT,
  `session_id`          INT NULL,
  `sender_type`         VARCHAR(20) NULL,
  `input_type`          VARCHAR(20) NULL,
  `turn_number`         INT NULL,
  `content`             TEXT NULL,
  `normalized_content`  TEXT NULL,
  `audio_url`           VARCHAR(500) NULL,
  `audio_duration`      INT NULL,
  `stt_transcript`      TEXT NULL,
  `created_at`          DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  KEY `idx_ai_chat_messages_session` (`session_id`),
  CONSTRAINT `fk_ai_chat_messages_session` FOREIGN KEY (`session_id`) REFERENCES `ai_chat_sessions` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ai_message_feedback` (
  `id`                    INT NOT NULL AUTO_INCREMENT,
  `message_id`            INT NULL,
  `pronunciation_score`   DECIMAL(19,4) NULL,
  `grammar_score`         DECIMAL(19,4) NULL,
  `vocabulary_score`      DECIMAL(19,4) NULL,
  `fluency_score`         DECIMAL(19,4) NULL,
  `overall_comment`       TEXT NULL,
  `improved_version`      TEXT NULL,
  `natural_suggestion`    TEXT NULL,
  `error_count`           INT NULL,
  `created_at`            DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  KEY `idx_ai_message_feedback_message` (`message_id`),
  CONSTRAINT `fk_ai_message_feedback_message` FOREIGN KEY (`message_id`) REFERENCES `ai_chat_messages` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ai_message_errors` (
  `id`              INT NOT NULL AUTO_INCREMENT,
  `feedback_id`     INT NULL,
  `error_type`      VARCHAR(100) NULL,
  `original_text`   TEXT NULL,
  `suggested_text`  TEXT NULL,
  `explanation`     TEXT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ai_message_errors_feedback` (`feedback_id`),
  CONSTRAINT `fk_ai_message_errors_feedback` FOREIGN KEY (`feedback_id`) REFERENCES `ai_message_feedback` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ai_session_summaries` (
  `id`                 INT NOT NULL AUTO_INCREMENT,
  `session_id`         INT NULL,
  `fluency_level`      VARCHAR(100) NULL,
  `grammar_level`      VARCHAR(100) NULL,
  `vocabulary_level`   VARCHAR(100) NULL,
  `sentence_count`     INT NULL,
  `error_count`        INT NULL,
  `duration_seconds`   INT NULL,
  `next_suggestion`    TEXT NULL,
  `related_topics`     TEXT NULL,
  `created_at`         DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  KEY `idx_ai_session_summaries_session` (`session_id`),
  CONSTRAINT `fk_ai_session_summaries_session` FOREIGN KEY (`session_id`) REFERENCES `ai_chat_sessions` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;

-- End of schema
