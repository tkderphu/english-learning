use english;

-- Assign favorite/selected genres to user id = 1 (admin)
-- We deliberately omit the `id` column so the DB assigns auto-increment values.
INSERT INTO user_genre (user_id, genre_id) VALUES
(1, 1), -- Fantasy
(1, 2), -- Adventure
(1, 4), -- Mystery
(1, 6), -- Sci-Fi
(1, 8), -- Children
(1, 12), -- Education
(1, 15), -- Biography
(1, 11), -- Self-Help
(1, 9), -- Young Adult
(1, 14); -- Travel
