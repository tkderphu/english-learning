use english;

-- Sua cot file_url: DOUBLE -> VARCHAR(500) de luu URL
ALTER TABLE `audio` MODIFY COLUMN `file_url` VARCHAR(500);


INSERT INTO `author` VALUES (4,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Ava Thompson','https://example.com/avatars/ava_thompson.jpg','US','Contemporary writer of short stories and essays'),(5,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Liam Johnson','https://example.com/avatars/liam_johnson.jpg','US','Academic and language educator'),(6,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Olivia Martinez','https://example.com/avatars/olivia_martinez.jpg','ES','Translator and novelist'),(7,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Noah Brown','https://example.com/avatars/noah_brown.jpg','AU','Children books author and storyteller'),(8,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Emma Wilson','https://example.com/avatars/emma_wilson.jpg','CA','Writer focusing on travel and culture'),(9,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Oliver Davis','https://example.com/avatars/oliver_davis.jpg','UK','Poet and essayist'),(10,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Sophia Garcia','https://example.com/avatars/sophia_garcia.jpg','MX','Young adult fiction author'),(11,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'William Miller','https://example.com/avatars/william_miller.jpg','US','Historian and textbook author'),(12,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Isabella Rodriguez','https://example.com/avatars/isabella_rodriguez.jpg','ES','Language learning content creator'),(13,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'James Martinez','https://example.com/avatars/james_martinez.jpg','US','Short fiction and dialogue-focused writer'),(14,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Mia Hernandez','https://example.com/avatars/mia_hernandez.jpg','CO','Educational book author'),(15,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Benjamin Lopez','https://example.com/avatars/benjamin_lopez.jpg','AR','Novelist and playwright'),(16,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Charlotte Gonzalez','https://example.com/avatars/charlotte_gonzalez.jpg','US','Cultural commentator and blogger'),(17,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Elijah Perez','https://example.com/avatars/elijah_perez.jpg','US','Sci-fi short story author'),(18,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Amelia Sanchez','https://example.com/avatars/amelia_sanchez.jpg','ES','Children educational content'),(19,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Lucas Ramirez','https://example.com/avatars/lucas_ramirez.jpg','CL','Travelogue and essays'),(20,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Harper Torres','https://example.com/avatars/harper_torres.jpg','US','Language pedagogy researcher'),(21,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Henry Nguyen','https://example.com/avatars/henry_nguyen.jpg','VN','Bilingual educator and author'),(22,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Evelyn Kim','https://example.com/avatars/evelyn_kim.jpg','KR','Modern fiction and translations'),(23,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Alexander Lee','https://example.com/avatars/alexander_lee.jpg','US','Non-fiction on communication'),(24,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Sofia Patel','https://example.com/avatars/sofia_patel.jpg','IN','Young readers author'),(25,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Daniel Wright','https://example.com/avatars/daniel_wright.jpg','UK','Mystery novelist'),(26,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Grace Scott','https://example.com/avatars/grace_scott.jpg','US','Creative writing instructor'),(27,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Michael Green','https://example.com/avatars/michael_green.jpg','US','Essayist and critic'),(28,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Chloe Adams','https://example.com/avatars/chloe_adams.jpg','AU','Romance and contemporary fiction'),(29,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Jack Baker','https://example.com/avatars/jack_baker.jpg','UK','Adventure stories for young adults'),(30,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Zoe Carter','https://example.com/avatars/zoe_carter.jpg','US','Educational technologist and writer'),(31,'system','2026-03-26 19:52:23.000000',NULL,NULL,1,'Ryan Mitchell','https://example.com/avatars/ryan_mitchell.jpg','US','Language learning researcher');

INSERT INTO `author_book` VALUES (5,5,5),(6,4,5),(7,6,6),(8,11,6),(9,7,7),(10,18,7),(11,8,8),(12,25,8),(13,9,9),(14,1,9),(15,5,11),(16,9,12),(17,10,13),(18,11,14),(19,5,125),(20,7,125),(21,5,16),(22,8,16);

INSERT INTO `book` VALUES (5,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Everyday English: Conversations 1','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(6,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Everyday English: Conversations 2','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(7,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Stories for Beginners A','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(8,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Stories for Beginners B','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(9,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Travel English Guide','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(10,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Business English Basics','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(11,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Food & Dining Phrases','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(12,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'English for Work','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(13,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Kids: Animal Stories','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(14,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Advanced Readings','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(15,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Pronunciation Practice','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(16,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Grammar in Use: Exercises','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(17,'system','2026-03-26 19:53:00.000000',NULL,NULL,1,'Idioms and Expressions','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3'),(125,'User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-27 01:02:01.530680','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-27 01:02:01.530680',1,'7 vi├¬n ngß╗ìc rß╗ông','en','https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3');

INSERT INTO `book_genre` VALUES (6,5,5),(7,6,6),(8,7,7),(9,8,8),(10,9,9),(11,10,10),(12,11,11),(13,12,12),(14,13,13),(15,14,14),(16,125,9),(17,125,4),(18,16,6),(19,16,9);

INSERT INTO `book_progress` VALUES (1,'User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:57:19.258453','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:57:19.258453',0,1,16,10,NULL,1,NULL),(2,NULL,NULL,NULL,NULL,1,1,125,0,NULL,NULL,NULL);

INSERT INTO `genre` VALUES (4,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Mystery','https://www.shutterstock.com/image-vector/hand-drawn-criminal-investigation-icons-600nw-2700024747.jpg','Suspense and mystery fiction'),(5,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Romance','https://example.com/genres/romance.jpg','Romantic fiction and relationships'),(6,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Sci-Fi','https://example.com/genres/scifi.jpg','Science fiction and futuristic stories'),(7,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Non-Fiction','https://example.com/genres/nonfiction.jpg','Informative and factual works'),(8,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Children','https://example.com/genres/children.jpg','Books for children and early readers'),(9,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Young Adult','https://example.com/genres/ya.jpg','Young adult fiction'),(10,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Historical','https://example.com/genres/historical.jpg','Historical fiction and narratives'),(11,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Self-Help','https://example.com/genres/selfhelp.jpg','Personal development and guides'),(12,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Education','https://example.com/genres/education.jpg','Textbooks and language learning materials'),(13,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Poetry','https://example.com/genres/poetry.jpg','Collections of poems'),(14,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Travel','https://example.com/genres/travel.jpg','Travel writing and guides'),(15,'system','2026-03-26 19:52:43.000000',NULL,NULL,1,'Biography','https://example.com/genres/biography.jpg','Life stories and memoirs');

INSERT INTO `user` VALUES (1,'User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:51:49.996860','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:51:49.996860',1,'admin@gmail.com','$2a$10$5H8hgCQY6hcjsllVPAnire6OD9r5Zve4Ze56/jI7DJj.v0TBgP6Ue','admin 20024',NULL,2);

INSERT INTO `user_genre` VALUES (1,1,1),(2,2,1),(3,4,1),(4,3,1),(5,3,1),(6,3,1),(7,3,1),(8,13,1),(9,6,1),(10,8,1),(11,12,1),(12,15,1),(13,11,1),(14,9,1),(15,14,1);
 
INSERT INTO `audio` (id, created_at, created_user, modified_at, modified_by, status, duration, format, sample_rate, file_size, file_url) VALUES
(1, '2026-03-29 13:00:00.000000', 'system', NULL, NULL, 1, 120, 'mp3', 44100.0, 2.5, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(2, '2026-03-29 13:05:00.000000', 'system', NULL, NULL, 1, 150, 'mp3', 44100.0, 3.1, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(3, '2026-03-29 13:10:00.000000', 'system', NULL, NULL, 1, 180, 'mp3', 44100.0, 4.0, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg');
 
INSERT INTO `chapter` (id, created_at, created_user, modified_at, modified_by, status, book_id, title, description, number) VALUES
(1, '2026-03-29 10:00:00.000000', 'system', NULL, NULL, 1, 5, 'Chapter 1: Greetings', 'Basic greetings and introductions in daily life', 1),
(2, '2026-03-29 10:05:00.000000', 'system', NULL, NULL, 1, 5, 'Chapter 2: Ordering Food', 'Common phrases used at restaurants and cafes', 2),
(3, '2026-03-29 10:10:00.000000', 'system', NULL, NULL, 1, 6, 'Chapter 1: Meeting New People', 'Expanding your vocabulary for networking', 1),
(4, '2026-03-29 10:15:00.000000', 'system', NULL, NULL, 1, 7, 'The Brave Little Tailor', 'Story portion part 1 about the clever tailor', 1),
(5, '2026-03-29 10:20:00.000000', 'system', NULL, NULL, 1, 125, 'Chapter 1: B├⌐ con t├¬n l├á Songoku', 'S╞í l╞░ß╗úc vß╗ü nguß╗ôn gß╗æc cß╗ºa nh├ón vß║¡t ch├¡nh', 1),
(6, '2026-03-29 10:25:00.000000', 'system', NULL, NULL, 1, 125, 'Chapter 2: Bulma v├á Songoku', 'Cuß╗Öc gß║╖p gß╗í t├¼nh cß╗¥ ─æß║ºu ti├¬n', 2),
(7, '2026-03-29 10:30:00.000000', 'system', NULL, NULL, 1, 8, 'The Ugly Duckling', 'A classic fairy tale part 1', 1),
(8, '2026-03-29 10:35:00.000000', 'system', NULL, NULL, 1, 9, 'Arriving at the Airport', 'Practical English for travel', 1);
 
INSERT INTO `page` (id, created_at, created_user, modified_at, modified_by, status, chapter_id, number, audio_id) VALUES
(1, '2026-03-29 11:00:00.000000', 'system', NULL, NULL, 1, 5, 1, 3),
(2, '2026-03-29 11:05:00.000000', 'system', NULL, NULL, 1, 5, 2, 3),
(3, '2026-03-29 11:10:00.000000', 'system', NULL, NULL, 1, 1, 1, 1),
(4, '2026-03-29 11:15:00.000000', 'system', NULL, NULL, 1, 2, 1, 2),
(5, '2026-03-29 11:20:00.000000', 'system', NULL, NULL, 1, 8, 1, NULL);
 
INSERT INTO `sentence` (id, created_at, created_user, modified_at, modified_by, status, page_id, content, transcription1, start_time, end_time) VALUES
(1, '2026-03-29 12:00:00.000000', 'system', NULL, NULL, 1, 1, 'Ng├áy xß╗¡a ng├áy x╞░a, c├│ mß╗Öt l├úo gi├á t├¬n l├á Gohan.', 'Once upon a time, there was an old man named Gohan.', 0.0, 5.0),
(2, '2026-03-29 12:05:00.000000', 'system', NULL, NULL, 1, 1, '├öng ß║Ñy t├¼m thß║Ñy mß╗Öt ─æß╗⌐a b├⌐ c├│ ─æu├┤i trong rß╗½ng.', 'He found a baby with a tail in the forest.', 5.1, 10.0),
(3, '2026-03-29 12:10:00.000000', 'system', NULL, NULL, 1, 3, 'Hello, how are you today?', 'Xin ch├áo, h├┤m nay bß║ín thß║┐ n├áo?', 0.0, 3.0),
(4, '2026-03-29 12:15:00.000000', 'system', NULL, NULL, 1, 3, 'I am fine, thank you. And you?', 'T├┤i khß╗Åe, cß║úm ╞ín. C├▓n bß║ín?', 3.1, 6.0),
(5, '2026-03-29 12:20:00.000000', 'system', NULL, NULL, 1, 4, 'I would like to order a cup of coffee.', 'T├┤i muß╗æn gß╗ìi mß╗Öt t├ích c├á ph├¬.', 0.0, 4.0),
(6, '2026-03-29 12:25:00.000000', 'system', NULL, NULL, 1, 4, 'Sure, would you like milk or sugar?', 'Chß║»c chß║»n rß╗ôi, bß║ín c├│ muß╗æn d├╣ng sß╗»a hay ─æ╞░ß╗¥ng kh├┤ng?', 4.1, 8.0);
 
INSERT INTO `audio` (id, created_at, created_user, modified_at, modified_by, status, duration, format, sample_rate, file_size, file_url) VALUES
(4, '2026-03-29 13:15:00.000000', 'system', NULL, NULL, 1, 100, 'mp3', 44100.0, 2.0, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(5, '2026-03-29 13:20:00.000000', 'system', NULL, NULL, 1, 110, 'mp3', 44100.0, 2.2, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg');
 
INSERT INTO `chapter` (id, created_at, created_user, modified_at, modified_by, status, book_id, title, description, number) VALUES
(9, '2026-03-29 10:40:00.000000', 'system', NULL, NULL, 1, 10, 'Chapter 1: The First Meeting', 'Initial business meetings and formal greetings', 1),
(10, '2026-03-29 10:45:00.000000', 'system', NULL, NULL, 1, 11, 'Chapter 1: At a Fancy Restaurant', 'Etiquette and vocabulary for high-end dining', 1),
(11, '2026-03-29 10:50:00.000000', 'system', NULL, NULL, 1, 13, 'Chapter 1: The Lion and the Mouse', 'A famous animal fable for children', 1);
 
INSERT INTO `page` (id, created_at, created_user, modified_at, modified_by, status, chapter_id, number, audio_id) VALUES
(6, '2026-03-29 11:30:00.000000', 'system', NULL, NULL, 1, 9, 1, 4),
(7, '2026-03-29 11:35:00.000000', 'system', NULL, NULL, 1, 10, 1, 5),
(8, '2026-03-29 11:40:00.000000', 'system', NULL, NULL, 1, 11, 1, NULL);
 
INSERT INTO `sentence` (id, created_at, created_user, modified_at, modified_by, status, page_id, content, transcription1, start_time, end_time) VALUES
(7, '2026-03-29 12:30:00.000000', 'system', NULL, NULL, 1, 6, 'Pleasure to meet you, Mr. Smith.', 'Rß║Ñt vui ─æ╞░ß╗úc gß║╖p ng├ái, ├┤ng Smith.', 0.0, 4.0),
(8, '2026-03-29 12:35:00.000000', 'system', NULL, NULL, 1, 6, 'Let\'s discuss the contract details.', 'H├úy thß║úo luß║¡n vß╗ü c├íc chi tiß║┐t trong hß╗úp ─æß╗ông.', 4.1, 9.0),
(9, '2026-03-29 12:40:00.000000', 'system', NULL, NULL, 1, 7, 'Could you please bring us the wine list?', 'Bß║ín c├│ thß╗â vui l├▓ng mang danh s├ích r╞░ß╗úu vang cho ch├║ng t├┤i kh├┤ng?', 0.0, 5.0),
(10, '2026-03-29 12:45:00.000000', 'system', NULL, NULL, 1, 7, 'I would like to order the steak medium-rare.', 'T├┤i muß╗æn gß╗ìi m├│n b├¡t tß║┐t ch├¡n t├íi.', 5.1, 10.0),
(11, '2026-03-29 12:50:00.000000', 'system', NULL, NULL, 1, 8, 'A big lion was sleeping under a tree.', 'Mß╗Öt con s╞░ tß╗¡ lß╗¢n ─æang ngß╗º d╞░ß╗¢i gß╗æc c├óy.', 0.0, 5.0),
(12, '2026-03-29 12:55:00.000000', 'system', NULL, NULL, 1, 8, 'A small mouse began to run over him.', 'Mß╗Öt con chuß╗Öt nhß╗Å bß║»t ─æß║ºu chß║íy qua ng╞░ß╗¥i n├│.', 5.1, 10.0);

INSERT INTO `audio` (id, created_at, created_user, modified_at, modified_by, status, duration, format, sample_rate, file_size, file_url) VALUES
(6, '2026-03-29 13:25:00.000000', 'system', NULL, NULL, 1, 95, 'mp3', 44100.0, 1.8, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(7, '2026-03-29 13:30:00.000000', 'system', NULL, NULL, 1, 120, 'mp3', 44100.0, 2.4, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(8, '2026-03-29 13:35:00.000000', 'system', NULL, NULL, 1, 140, 'mp3', 44100.0, 2.9, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(9, '2026-03-29 13:40:00.000000', 'system', NULL, NULL, 1, 105, 'mp3', 44100.0, 2.1, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(10, '2026-03-29 13:45:00.000000', 'system', NULL, NULL, 1, 130, 'mp3', 44100.0, 2.7, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(11, '2026-03-29 13:50:00.000000', 'system', NULL, NULL, 1, 115, 'mp3', 44100.0, 2.3, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(12, '2026-03-29 13:55:00.000000', 'system', NULL, NULL, 1, 160, 'mp3', 44100.0, 3.5, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(13, '2026-03-29 14:00:00.000000', 'system', NULL, NULL, 1, 80, 'mp3', 44100.0, 1.5, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(14, '2026-03-29 14:05:00.000000', 'system', NULL, NULL, 1, 90, 'mp3', 44100.0, 1.7, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg'),
(15, '2026-03-29 14:10:00.000000', 'system', NULL, NULL, 1, 100, 'mp3', 44100.0, 2.0, 'https://www.learningcontainer.com/wp-content/uploads/2020/02/Sample-OGG-File.ogg');






