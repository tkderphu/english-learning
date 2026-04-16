-- Cleanup + harden script for book_progress uniqueness.
-- Run this once on existing databases before adding the unique key.

USE `english`;

-- 1) Remove invalid rows that cannot satisfy NOT NULL + unique constraints.
DELETE FROM book_progress
WHERE user_id IS NULL OR book_id IS NULL;

-- 2) Deduplicate rows by keeping the newest record (max id) per (user_id, book_id).
DELETE bp
FROM book_progress bp
JOIN (
    SELECT user_id, book_id, MAX(id) AS keep_id
    FROM book_progress
    GROUP BY user_id, book_id
    HAVING COUNT(*) > 1
) dups
    ON dups.user_id = bp.user_id
   AND dups.book_id = bp.book_id
WHERE bp.id <> dups.keep_id;

-- 3) Enforce invariants to prevent duplicate rows in future writes.
ALTER TABLE book_progress
    MODIFY COLUMN user_id INT NOT NULL,
    MODIFY COLUMN book_id INT NOT NULL;

ALTER TABLE book_progress
    ADD CONSTRAINT uk_book_progress_user_book UNIQUE (user_id, book_id);

-- 4) Verification query (expect 0 rows).
SELECT user_id, book_id, COUNT(*) AS duplicate_count
FROM book_progress
GROUP BY user_id, book_id
HAVING COUNT(*) > 1;
