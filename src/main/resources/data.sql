
use english;
-- Authors
INSERT INTO author (id, name, avatar, nationality, biography, created_user, created_at, modified_by, modified_at, status) VALUES
(1, 'J.K. Rowling', 'https://example.com/avatars/jk_rowling.jpg', 'UK', 'Author of the Harry Potter series', 'system', NOW(), NULL, NULL, 1),
(2, 'J.R.R. Tolkien', 'https://example.com/avatars/tolkien.jpg', 'UK', 'Author of The Lord of the Rings', 'system', NOW(), NULL, NULL, 1),
(3, 'George R.R. Martin', 'https://example.com/avatars/grrm.jpg', 'US', 'Author of A Song of Ice and Fire', 'system', NOW(), NULL, NULL, 1);

-- Genres
INSERT INTO genre (id, name, thumbnail, description, created_user, created_at, modified_by, modified_at, status) VALUES
(1, 'Fantasy', 'https://example.com/genres/fantasy.jpg', 'Stories with magical or supernatural elements', 'system', NOW(), NULL, NULL, 1),
(2, 'Adventure', 'https://example.com/genres/adventure.jpg', 'Action-oriented stories and journeys', 'system', NOW(), NULL, NULL, 1),
(3, 'Classic', 'https://example.com/genres/classic.jpg', 'Enduring literature classics', 'system', NOW(), NULL, NULL, 1);

-- Books
INSERT INTO book (id, title, language, cover_url, created_user, created_at, modified_by, modified_at, status) VALUES
(1, 'Harry Potter and the Philosopher''s Stone', 'en', 'https://example.com/covers/hp1.jpg', 'system', NOW(), NULL, NULL, 1),
(2, 'The Lord of the Rings', 'en', 'https://example.com/covers/lotr.jpg', 'system', NOW(), NULL, NULL, 1),
(3, 'A Game of Thrones', 'en', 'https://example.com/covers/got1.jpg', 'system', NOW(), NULL, NULL, 1),
(4, 'Fantastic Beasts and Where to Find Them', 'en', 'https://example.com/covers/fantastic_beasts.jpg', 'system', NOW(), NULL, NULL, 1);

-- Author-Book (many-to-many)
INSERT INTO author_book (id, author_id, book_id) VALUES
(1, 1, 1),  -- JK Rowling -> HP1
(2, 2, 2),  -- Tolkien -> LOTR
(3, 3, 3),  -- GRRM -> A Game of Thrones
(4, 1, 4);  -- JK Rowling -> Fantastic Beasts

-- Book-Genre (many-to-many)
INSERT INTO book_genre (id, genre_id, book_id) VALUES
(1, 1, 1),  -- HP1 -> Fantasy
(2, 1, 2),  -- LOTR -> Fantasy
(3, 1, 3),  -- A Game of Thrones -> Fantasy
(4, 1, 4),  -- Fantastic Beasts -> Fantasy
(5, 2, 2);  -- LOTR -> Adventure (also)