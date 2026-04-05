-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: english
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `ai_chat_messages`
--

LOCK TABLES `ai_chat_messages` WRITE;
/*!40000 ALTER TABLE `ai_chat_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_chat_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ai_chat_sessions`
--

LOCK TABLES `ai_chat_sessions` WRITE;
/*!40000 ALTER TABLE `ai_chat_sessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_chat_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ai_message_errors`
--

LOCK TABLES `ai_message_errors` WRITE;
/*!40000 ALTER TABLE `ai_message_errors` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_message_errors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ai_message_feedback`
--

LOCK TABLES `ai_message_feedback` WRITE;
/*!40000 ALTER TABLE `ai_message_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_message_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ai_scenarios`
--

LOCK TABLES `ai_scenarios` WRITE;
/*!40000 ALTER TABLE `ai_scenarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_scenarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ai_session_summaries`
--

LOCK TABLES `ai_session_summaries` WRITE;
/*!40000 ALTER TABLE `ai_session_summaries` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_session_summaries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `audio`
--

LOCK TABLES `audio` WRITE;
/*!40000 ALTER TABLE `audio` DISABLE KEYS */;
INSERT INTO `audio` VALUES (1,'2026-04-02 13:49:42.239704','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:49:42.239704','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,133248,799488,'http://10.0.2.2:7000/api/assets/f04c950e-d864-42ef-8780-c8b01fd658a4_test.mp3','mp3',24000);
/*!40000 ALTER TABLE `audio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES (4,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/ava_thompson.jpg','Contemporary writer of short stories and essays','Ava Thompson','US'),(5,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/liam_johnson.jpg','Academic and language educator','Liam Johnson','US'),(6,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/olivia_martinez.jpg','Translator and novelist','Olivia Martinez','ES'),(7,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/noah_brown.jpg','Children books author and storyteller','Noah Brown','AU'),(8,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/emma_wilson.jpg','Writer focusing on travel and culture','Emma Wilson','CA'),(9,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/oliver_davis.jpg','Poet and essayist','Oliver Davis','UK'),(10,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/sophia_garcia.jpg','Young adult fiction author','Sophia Garcia','MX'),(11,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/william_miller.jpg','Historian and textbook author','William Miller','US'),(12,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/isabella_rodriguez.jpg','Language learning content creator','Isabella Rodriguez','ES'),(13,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/james_martinez.jpg','Short fiction and dialogue-focused writer','James Martinez','US'),(14,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/mia_hernandez.jpg','Educational book author','Mia Hernandez','CO'),(15,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/benjamin_lopez.jpg','Novelist and playwright','Benjamin Lopez','AR'),(16,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/charlotte_gonzalez.jpg','Cultural commentator and blogger','Charlotte Gonzalez','US'),(17,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/elijah_perez.jpg','Sci-fi short story author','Elijah Perez','US'),(18,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/amelia_sanchez.jpg','Children educational content','Amelia Sanchez','ES'),(19,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/lucas_ramirez.jpg','Travelogue and essays','Lucas Ramirez','CL'),(20,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/harper_torres.jpg','Language pedagogy researcher','Harper Torres','US'),(21,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/henry_nguyen.jpg','Bilingual educator and author','Henry Nguyen','VN'),(22,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/evelyn_kim.jpg','Modern fiction and translations','Evelyn Kim','KR'),(23,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/alexander_lee.jpg','Non-fiction on communication','Alexander Lee','US'),(24,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/sofia_patel.jpg','Young readers author','Sofia Patel','IN'),(25,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/daniel_wright.jpg','Mystery novelist','Daniel Wright','UK'),(26,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/grace_scott.jpg','Creative writing instructor','Grace Scott','US'),(27,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/michael_green.jpg','Essayist and critic','Michael Green','US'),(28,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/chloe_adams.jpg','Romance and contemporary fiction','Chloe Adams','AU'),(29,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/jack_baker.jpg','Adventure stories for young adults','Jack Baker','UK'),(30,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/zoe_carter.jpg','Educational technologist and writer','Zoe Carter','US'),(31,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/ryan_mitchell.jpg','Language learning researcher','Ryan Mitchell','US');
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `author_book`
--

LOCK TABLES `author_book` WRITE;
/*!40000 ALTER TABLE `author_book` DISABLE KEYS */;
INSERT INTO `author_book` VALUES (5,5,5),(6,4,5),(7,6,6),(8,11,6),(9,7,7),(10,18,7),(11,8,8),(12,25,8),(13,9,9),(14,1,9),(15,5,11),(16,9,12),(17,10,13),(18,11,14),(19,5,125),(20,7,125),(21,5,16),(22,8,16);
/*!40000 ALTER TABLE `author_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (5,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Everyday English: Conversations 1'),(6,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Everyday English: Conversations 2'),(7,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Stories for Beginners A'),(8,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Stories for Beginners B'),(9,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Travel English Guide'),(10,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Business English Basics'),(11,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Food & Dining Phrases'),(12,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','English for Work'),(13,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Kids: Animal Stories'),(14,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Advanced Readings'),(15,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Pronunciation Practice'),(16,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Grammar in Use: Exercises'),(17,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Idioms and Expressions'),(125,'2026-03-27 01:02:01.530680','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-27 01:02:01.530680','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','7 viên ngọc rồng');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `book_genre`
--

LOCK TABLES `book_genre` WRITE;
/*!40000 ALTER TABLE `book_genre` DISABLE KEYS */;
INSERT INTO `book_genre` VALUES (6,5,5),(7,6,6),(8,7,7),(9,8,8),(10,9,9),(11,10,10),(12,11,11),(13,12,12),(14,13,13),(15,14,14),(16,125,9),(17,125,4),(18,16,6),(19,16,9);
/*!40000 ALTER TABLE `book_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `book_progress`
--

LOCK TABLES `book_progress` WRITE;
/*!40000 ALTER TABLE `book_progress` DISABLE KEYS */;
INSERT INTO `book_progress` VALUES (1,'2026-03-26 12:57:19.258453','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:57:19.258453','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,16,1,NULL,10,1,NULL,NULL),(2,NULL,NULL,NULL,NULL,1,125,0,NULL,20,1,NULL,NULL);
/*!40000 ALTER TABLE `book_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `chapter`
--

LOCK TABLES `chapter` WRITE;
/*!40000 ALTER TABLE `chapter` DISABLE KEYS */;
INSERT INTO `chapter` VALUES (1,NULL,NULL,NULL,NULL,1,16,'this is test',1,'How can learn english by yourself'),(3,'2026-04-02 13:09:25.119459','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:09:25.119459','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,125,'heheheheheh',0,'A beating between LiuChang and berrus');
/*!40000 ALTER TABLE `chapter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `decks`
--

LOCK TABLES `decks` WRITE;
/*!40000 ALTER TABLE `decks` DISABLE KEYS */;
/*!40000 ALTER TABLE `decks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
INSERT INTO `files` VALUES (1,'2026-04-02 13:37:30.239463','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:37:30.239463','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,'audio/mpeg','ac4c060e-a4f8-4ef9-9a25-cc867fff673b_test.mp3','test.mp3','D:\\WorkSpaceD\\project\\ez-english\\english\\main_eng\\uploads\\ac4c060e-a4f8-4ef9-9a25-cc867fff673b_test.mp3',799488),(2,'2026-04-02 13:42:19.998519','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:42:19.998519','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,'audio/mpeg','0abc0664-01ee-4a40-b5e7-528b0ad9c5dd_test.mp3','test.mp3','D:\\WorkSpaceD\\project\\ez-english\\english\\main_eng\\uploads\\0abc0664-01ee-4a40-b5e7-528b0ad9c5dd_test.mp3',799488),(3,'2026-04-02 13:42:41.471876','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:42:41.471876','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,'audio/mpeg','d5d690d8-17b1-4a81-9631-9b599d5dec34_test.mp3','test.mp3','D:\\WorkSpaceD\\project\\ez-english\\english\\main_eng\\uploads\\d5d690d8-17b1-4a81-9631-9b599d5dec34_test.mp3',799488),(4,'2026-04-02 13:45:39.127902','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:45:39.127902','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,'audio/mpeg','5e653a18-d741-4370-9191-10b16ec5e1b6_test.mp3','test.mp3','D:\\WorkSpaceD\\project\\ez-english\\english\\main_eng\\uploads\\5e653a18-d741-4370-9191-10b16ec5e1b6_test.mp3',799488),(5,'2026-04-02 13:49:08.120886','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:49:08.120886','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,'audio/mpeg','f04c950e-d864-42ef-8780-c8b01fd658a4_test.mp3','test.mp3','D:\\WorkSpaceD\\project\\ez-english\\english\\main_eng\\uploads\\f04c950e-d864-42ef-8780-c8b01fd658a4_test.mp3',799488);
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `flashcards`
--

LOCK TABLES `flashcards` WRITE;
/*!40000 ALTER TABLE `flashcards` DISABLE KEYS */;
/*!40000 ALTER TABLE `flashcards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (4,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Suspense and mystery fiction','Mystery','https://www.shutterstock.com/image-vector/hand-drawn-criminal-investigation-icons-600nw-2700024747.jpg'),(5,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Romantic fiction and relationships','Romance','https://example.com/genres/romance.jpg'),(6,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Science fiction and futuristic stories','Sci-Fi','https://example.com/genres/scifi.jpg'),(7,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Informative and factual works','Non-Fiction','https://example.com/genres/nonfiction.jpg'),(8,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Books for children and early readers','Children','https://example.com/genres/children.jpg'),(9,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Young adult fiction','Young Adult','https://example.com/genres/ya.jpg'),(10,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Historical fiction and narratives','Historical','https://example.com/genres/historical.jpg'),(11,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Personal development and guides','Self-Help','https://example.com/genres/selfhelp.jpg'),(12,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Textbooks and language learning materials','Education','https://example.com/genres/education.jpg'),(13,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Collections of poems','Poetry','https://example.com/genres/poetry.jpg'),(14,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Travel writing and guides','Travel','https://example.com/genres/travel.jpg'),(15,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Life stories and memoirs','Biography','https://example.com/genres/biography.jpg');
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` VALUES (1,'2026-03-26 19:52:48.000000','system',NULL,NULL,1,'For learners starting out','Beginner',1),(2,'2026-03-26 19:52:48.000000','system',NULL,NULL,1,'Basic vocabulary and grammar','Elementary',2),(3,'2026-03-26 19:52:48.000000','system',NULL,NULL,1,'Everyday English and simple conversations','Pre-Intermediate',3),(4,'2026-03-26 19:52:48.000000','system',NULL,NULL,1,'Improving fluency and comprehension','Intermediate',4),(5,'2026-03-26 19:52:48.000000','system',NULL,NULL,1,'Complex grammar and reading','Upper-Intermediate',5),(6,'2026-03-26 19:52:48.000000','system',NULL,NULL,1,'Near-native proficiency and nuance','Advanced',6);
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `page`
--

LOCK TABLES `page` WRITE;
/*!40000 ALTER TABLE `page` DISABLE KEYS */;
INSERT INTO `page` VALUES (2,'2026-04-02 13:56:45.884400','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:56:45.884400','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,1,3,'Weiss\' face showed an extremely surprised expression.\n\n\"It\'s so fragrant. This is the first time I\'ve smelled such an attractive aroma.\"\n\n\"Lord Wang Ting, what is the name of this delicacy?\"\n\nFor Weiss, who loves food and is also a foodie, there is absolutely no resistance in the face of such food.\n\n\"Weiss, what did I say?\"\n\nWeiss, immersed in the food, immediately nodded.\n\n\"Hmm, no problem.\"\n\nWeiss agreeing to his request without any hesitation was obviously expected by him.\n\n\"Is he the same as Pike?\"\n\nSeeing Weiss\' small steps, Wang Ting felt speechless, and at this moment, Beerus was also attracted by the fragrance.\n\nThen he saw the food in front of Weiss and immediately yelled, \"Weiss, you actually eat alone.\"\n\nThe two started arguing. Of course, this had nothing to do with Wang Ting, because his goal had already been achieved.\n\nRoar.\n\nWeiss\'s voice echoed throughout the God of Destruction\'s realm.\n\n\"Weiss.\"\n\nDuring the conversation between Wang Ting and Weiss, Beerus asked softly, \"Weiss, what are you talking about with the lord?\"\n\n\"Why do I feel like there is some secret being hidden from me?\"\n\n\"Lord Wang Ting lacks an opponent. Aren\'t you the best candidate?\"\n\n\"Nanny?\"\n\n\"Why do I feel like I\'ve been tricked again?\"\n\nIt was Wang Ting and Weiss, the Angel of Universe Seven.\n\nIn the next second, Wang Ting suddenly erupted and instantly transformed into a Super Saiyan 4, and his golden aura continued to rise.\n\n\"Not bad.\"\n\nWeiss looked at Wang Ting, who had transformed in an instant, and at this moment, Weiss already knew his combat strength.\n\nAlthough Wang Ting\'s strength has reached the level of a weaker God of Destruction, he is still a little weak in his opinion, but considering Wang Ting\'s age, he can indeed be regarded as a genius.\n\nGoku and Vegeta, who were watching the battle below, were also staring at the two people in the air with their eyes fixed.\n\nSon Gohan clenched his fists tightly, and his heart was extremely eager for strength at this moment.\n\nAlthough Lord Wang Ting\'s strength is...',0);
/*!40000 ALTER TABLE `page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `password_reset_otps`
--

LOCK TABLES `password_reset_otps` WRITE;
/*!40000 ALTER TABLE `password_reset_otps` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_otps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sentence`
--

LOCK TABLES `sentence` WRITE;
/*!40000 ALTER TABLE `sentence` DISABLE KEYS */;
INSERT INTO `sentence` VALUES (1,'2026-04-02 13:57:02.758012','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.758012','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Weiss\' face showed an extremely surprised expression.',3600,2,0,'Khuôn mặt Weiss lộ rõ vẻ vô cùng ngạc nhiên.'),(2,'2026-04-02 13:57:02.796731','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.796731','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'It\'s so fragrant. This is the first time I\'ve smelled such an attractive aroma.',8660,2,3600,'Thật thơm. Đây là lần đầu tiên tôi ngửi thấy mùi hương hấp dẫn như vậy.'),(3,'2026-04-02 13:57:02.806683','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.806683','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Lord Wang Ting, what is the name of this delicacy?',12440,2,8660,'Ngài Wang Ting, món ăn này tên là gì?'),(4,'2026-04-02 13:57:02.817373','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.817373','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'For Weiss, who loves food and is also a foodie, there is absolutely no resistance in the face',17180,2,12440,'Đối với Weiss, người đam mê ẩm thực, thì hoàn toàn không thể cưỡng lại khi đối mặt'),(5,'2026-04-02 13:57:02.831501','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.831501','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'of such food.',18920,2,17180,'với những món ăn như vậy.'),(6,'2026-04-02 13:57:02.841484','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.841484','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Weiss, what did I say?',21320,2,18920,'Weiss, tôi đã nói gì?'),(7,'2026-04-02 13:57:02.853239','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.853239','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Weiss, immersed in the food, immediately nodded.',24720,2,21320,'Weiss, đắm chìm trong món ăn, lập tức gật đầu.'),(8,'2026-04-02 13:57:02.867799','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.867799','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Hmm, no problem.',27160,2,24720,'Ừm, không vấn đề gì.'),(9,'2026-04-02 13:57:02.878798','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.878798','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Weiss agreeing to his request without any hesitation was obviously expected by him.',33500,2,27160,'Việc Weiss đồng ý ngay lập tức rõ ràng đã nằm trong dự đoán của ông.'),(10,'2026-04-02 13:57:02.893800','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.893800','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Is he the same as Pike?',35840,2,33500,'Anh ta giống Pike sao?'),(11,'2026-04-02 13:57:02.903722','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.903722','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Seeing Weiss\' small steps, Wang Ting felt speechless, and at this moment, Beerus was',40920,2,35840,'Nhìn những bước nhỏ của Weiss, Wang Ting chỉ biết cạn lời, và lúc này Beerus đã'),(12,'2026-04-02 13:57:02.916729','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.916729','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'also attracted by the fragrance.',43800,2,40920,'cũng bị thu hút bởi mùi hương.'),(13,'2026-04-02 13:57:02.928087','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.928087','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Then he saw the food in front of Weiss and immediately yelled, \"Weiss, you actually eat',48480,2,43800,'Sau đó ông nhìn thấy món ăn trước mặt Weiss và lập tức hét lên: \"Weiss, ngươi lại dám ăn'),(14,'2026-04-02 13:57:02.939369','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.939369','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'alone.\"',49480,2,48480,'một mình sao.\"'),(15,'2026-04-02 13:57:02.953382','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.953382','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'The.',50480,2,49480,'…'),(16,'2026-04-02 13:57:02.966276','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.966276','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'The two started arguing. Of course, this has nothing to do with Wang Ting, because his goal',55760,2,50480,'Hai người bắt đầu cãi nhau. Tất nhiên, điều này không liên quan đến Wang Ting, bởi vì mục tiêu của anh'),(17,'2026-04-02 13:57:02.977391','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.977391','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'has been achieved.',57760,2,55760,'đã đạt được.'),(18,'2026-04-02 13:57:02.987850','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.987850','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Roar.',58760,2,57760,'Gầm lên.'),(19,'2026-04-02 13:57:02.998763','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:02.998763','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Weiss\'s voice echoed throughout the God of Destruction\'s realm.',62960,2,58760,'Giọng nói của Weiss vang vọng khắp Thần Giới Hủy Diệt.'),(20,'2026-04-02 13:57:03.008851','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.008851','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Weiss.',64720,2,62960,'Weiss.'),(21,'2026-04-02 13:57:03.020793','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.020793','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'During the conversation between Wang Ting and Weiss, Beerus asked softly, \"Weiss, what are you talking',69840,2,64720,'Trong cuộc trò chuyện giữa Wang Ting và Weiss, Beerus nhẹ giọng hỏi: \"Weiss, ngươi đang nói gì'),(22,'2026-04-02 13:57:03.032906','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.032906','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'about with the lord?\"',71800,2,69840,'với ngài vậy?\"'),(23,'2026-04-02 13:57:03.042812','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.042812','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Why do I feel like there is some secret being hidden from me?',75120,2,71800,'Sao ta có cảm giác có điều gì đó đang bị giấu khỏi ta?'),(24,'2026-04-02 13:57:03.055807','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.055807','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Lord Wang Ting lacks an opponent. Aren\'t you the best candidate?',78920,2,75120,'Ngài Wang Ting đang thiếu đối thủ. Ngài chẳng phải là lựa chọn tốt nhất sao?'),(25,'2026-04-02 13:57:03.067885','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.067885','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Nanny?',80420,2,78920,'Cái gì cơ?'),(26,'2026-04-02 13:57:03.079797','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.079797','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Why do I feel like I\'ve been tricked again?',82640,2,80420,'Sao ta lại có cảm giác mình bị lừa nữa rồi?'),(27,'2026-04-02 13:57:03.090798','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.090798','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'It was Wang Ting and Weiss, the Angel of Universe Seven.',86640,2,82640,'Đó là Wang Ting và Weiss, thiên thần của Vũ trụ thứ Bảy.'),(28,'2026-04-02 13:57:03.101891','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.101891','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'In the next second, Wang Ting suddenly erupted and instantly transformed into a',91340,2,86640,'Ngay giây tiếp theo, Wang Ting bùng nổ và lập tức biến thành một'),(29,'2026-04-02 13:57:03.113796','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.113796','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Super Saiyan 4, and his golden aura continued to rise.',96840,2,91340,'Super Saiyan 4, và hào quang vàng của anh không ngừng dâng cao.'),(30,'2026-04-02 13:57:03.124795','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.124795','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Not bad.',98400,2,96840,'Không tệ.'),(31,'2026-04-02 13:57:03.134825','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.134825','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Weiss looked at Wang Ting, who had transformed in an instant, and at this moment, Weiss already',103200,2,98400,'Weiss nhìn Wang Ting, người vừa biến đổi trong chớp mắt, và lúc này Weiss đã'),(32,'2026-04-02 13:57:03.146393','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.146393','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'knew his combat strength.',105920,2,103200,'biết được thực lực chiến đấu của anh.'),(33,'2026-04-02 13:57:03.157312','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.157312','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Although Wang Ting\'s strength has reached the level of a weaker God of Destruction, he is still',110120,2,105920,'Mặc dù sức mạnh của Wang Ting đã đạt đến cấp độ Thần Hủy Diệt yếu, nhưng anh vẫn'),(34,'2026-04-02 13:57:03.167086','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.167086','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'a little weak in his opinion, but considering Wang Ting\'s age, he can indeed be regarded as a genius.',116480,2,110120,'còn hơi yếu theo đánh giá của ông, nhưng xét về độ tuổi, anh thực sự có thể được coi là một thiên tài.'),(35,'2026-04-02 13:57:03.179085','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.179085','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'And Goku and Vegeta, who were watching the battle below, were also staring at the two',120720,2,116480,'Goku và Vegeta, những người đang theo dõi trận chiến bên dưới, cũng đang nhìn chằm chằm vào hai'),(36,'2026-04-02 13:57:03.192061','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.192061','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'people in the air with their eyes fixed.',124320,2,120720,'người trên không trung với ánh mắt tập trung.'),(37,'2026-04-02 13:57:03.203067','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.203067','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Sun Gohan clenched his fists tightly, and his heart was extremely eager for strength',128640,2,124320,'Son Gohan siết chặt nắm đấm, trong lòng vô cùng khao khát sức mạnh'),(38,'2026-04-02 13:57:03.214067','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.214067','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'at this moment.',130520,2,128640,'ngay lúc này.'),(39,'2026-04-02 13:57:03.225256','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-04-02 13:57:03.225256','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,'Although Lord Wang Ting\'s strength is al.',132280,2,130520,'Mặc dù sức mạnh của ngài Wang Ting là...');
/*!40000 ALTER TABLE `sentence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `topic`
--

LOCK TABLES `topic` WRITE;
/*!40000 ALTER TABLE `topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'2026-03-26 12:51:49.996860','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:51:49.996860','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,NULL,'admin@gmail.com','admin 20024','$2a$10$5H8hgCQY6hcjsllVPAnire6OD9r5Zve4Ze56/jI7DJj.v0TBgP6Ue',2);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_genre`
--

LOCK TABLES `user_genre` WRITE;
/*!40000 ALTER TABLE `user_genre` DISABLE KEYS */;
INSERT INTO `user_genre` VALUES (1,1,1),(2,2,1),(3,4,1),(4,3,1),(5,3,1),(6,3,1),(7,3,1),(8,13,1),(9,6,1),(10,8,1),(11,12,1),(12,15,1),(13,11,1),(14,9,1),(15,14,1);
/*!40000 ALTER TABLE `user_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_profile`
--

LOCK TABLES `user_profile` WRITE;
/*!40000 ALTER TABLE `user_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_session`
--

LOCK TABLES `user_session` WRITE;
/*!40000 ALTER TABLE `user_session` DISABLE KEYS */;
INSERT INTO `user_session` VALUES (1,'2026-03-26 12:54:08.036125','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:54:08.036125','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0NDk2NDgwMDQsImlkX3Rva2VuIjoiOTQ2NDQ3NGMtNjExNy00YTJmLTk4Y2YtMjgyMmE3N2RjMGVjIn0.9oI_ckfC99d37T6cgCnCLUS2U3cMX7j_VW-TCXf85jM38oBaF9sNyEhj1pbP2qF3',NULL,NULL,1),(2,'2026-03-27 00:11:29.568862',NULL,'2026-03-27 00:11:29.568862',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTAyODk0NjEsImlkX3Rva2VuIjoiNWZiYzUwYTMtMWQ5Ni00ODhjLTgzNDAtZjk0Y2U3NzM1NGUyIn0.PyXANbAEm9WnN53zfEjYGoF8dFTZDby94zVj6Kk6bYWW8-N_9W6TJZ6U2UoCHVsG',NULL,NULL,1),(3,'2026-03-27 00:11:58.545434',NULL,'2026-03-27 00:11:58.545434',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTAzMTg1MzksImlkX3Rva2VuIjoiYjI0ZDYzOWEtYTBmMi00ZmM3LWFjMjMtNjRhYTUwMjg5YjY4In0.kgeWWiXkUr-SIr5fvrTZ2pvS4Bpv0Z7hi_rrt3FjHhlc4-zyQyBaHF-YeQRI5DWo',NULL,NULL,1),(4,'2026-03-27 00:17:35.069325',NULL,'2026-03-27 00:17:35.069325',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTA2NTUwMTMsImlkX3Rva2VuIjoiNDY1NzQ5YzYtYzk4My00ZDlkLWFhMzUtZWI0YWUxZDg0NzgxIn0.lN_s4TQa25c9hL67jglFyhdhWHQMuCaAwcMzbT7X9_AcXHVT1RSfysBpJ3gLkKTV',NULL,NULL,1),(5,'2026-03-27 00:28:44.523190',NULL,'2026-03-27 00:28:44.523190',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTEzMjQ1MjMsImlkX3Rva2VuIjoiNTFmYTlkNjgtZjFlMC00NTVhLWJhMWUtMGM2Mjk5NDQwMDljIn0.jlO55QAUcftUDQz7pmUxP4Uw-iSwDDi47c9BmUVqHDLMPd_vWbqkb9kqQxdadus4',NULL,NULL,1),(6,'2026-03-27 00:28:46.639027',NULL,'2026-03-27 00:28:46.639027',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTEzMjY2MzksImlkX3Rva2VuIjoiMjlkZTEwYzctMjBlMC00ZDY0LWEzZWUtNmE3Mzk1OGZkMTIyIn0.h8McAkX0AwMkCioCYQ4GWJyhC2SKusbVC_v2mM_VwC8x8m6AQT-m-UqS0hQNzHgz',NULL,NULL,1),(7,'2026-03-27 00:29:36.626915',NULL,'2026-03-27 00:29:36.626915',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTEzNzY2MTYsImlkX3Rva2VuIjoiYmY4ZTBlYTYtZDM3ZS00MGJlLTljNTQtNjQzN2Q0ODIzMmExIn0.7p1REmEiltVC4ZepnjpjkkSMWaYY56RCfuePMIu1AJm8OujnyZQF7_3j0NGlF7sZ',NULL,NULL,1),(8,'2026-03-27 00:30:14.474556',NULL,'2026-03-27 00:30:14.474556',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTE0MTQ0NzQsImlkX3Rva2VuIjoiMjhmYTdmNDMtMTY0Zi00ZmIwLTllNjMtYmQ1MzhiZjI0YjlkIn0.XB5lOomztNais2n0leG5NbeijrJDhrFZg21NUFQ9fLGZFLnt544X4FyGAlrZI25e',NULL,NULL,1),(9,'2026-03-27 00:53:38.488150',NULL,'2026-03-27 00:53:38.488150',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTI4MTg0ODQsImlkX3Rva2VuIjoiMWFlNTE3N2EtMWMxNi00ZjFkLWI0NGMtZmFjYWQ4ZmE5NzA3In0.nHJYkuwHoB28IwPYsqI375HEda-0nLE7lPWI92d8J5R7CMyCcR2-aoHCDwL6DT10',NULL,NULL,1),(10,'2026-03-27 15:22:58.036058',NULL,'2026-03-27 15:22:58.036058',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA1NDQ5Nzc5NTYsImlkX3Rva2VuIjoiMjViMDMyYzEtNjNkMy00MTNjLTgyODMtYTM0YmIxZGI1ZTdjIn0.PBb60uun1h-0pc4MTRZQh57Vuib7jR1_tPV6tKRro-3yJXmbHDYa1waQ4FLJPalw',NULL,NULL,1),(11,'2026-03-27 15:28:53.205504',NULL,'2026-03-27 15:28:53.205504',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA1NDUzMzMwODgsImlkX3Rva2VuIjoiMjYwZjVkZjItYTFhOC00MjkxLTkxZTQtYjM3YjljMzljYjMyIn0.E8o7H6Ka7FuLfGoWOaM3pTo6sNs1e3V3Lb2b7EeewbtSzHqF34M6sDD6bBeN4exh',NULL,NULL,1),(12,'2026-03-27 15:58:06.131290',NULL,'2026-03-27 15:58:06.131290',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA1NDcwODYwODEsImlkX3Rva2VuIjoiNzUyYTc3YjktZjllYi00ZTkwLTgzMDgtOTAwYjBhODQyNmZiIn0.iWgWBEFo81D2hpdETfLv3ApoDbzLvdDu-w7YFLjzcJ6dyxHRoDqVeDJmMgidkrup',NULL,NULL,1),(13,'2026-03-28 13:50:33.400771',NULL,'2026-03-28 13:50:33.400771',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA2MjU4MzMyNzEsImlkX3Rva2VuIjoiNWQ0MmFmMGUtZGFmZC00MjcyLWI2MmUtYmQ1ZGFlMmRiODBhIn0.v6G_5gyZoOKIMECKwsACsoGqGJw2isrucAB4_SHFgSjmVcN7JpG0GwMEGwm6ruiE',NULL,NULL,1),(14,'2026-03-28 14:18:20.552210',NULL,'2026-03-28 14:18:20.552210',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA2Mjc1MDA0MzAsImlkX3Rva2VuIjoiNGMxMGIxMDItZDNhMi00YzM1LThmMGQtZDNkZjcyMGQwZjVhIn0.MuL22m1ec_N5LaPc2vNDDdYa_Y4anv0-ULj5CDUmy2zsjqQ6YrheLOBctNgYasgS',NULL,NULL,1),(15,'2026-03-28 14:22:30.481989',NULL,'2026-03-28 14:22:30.481989',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA2Mjc3NTAzOTcsImlkX3Rva2VuIjoiZjJjMTRkY2EtYzM1NS00NjAxLTg4ZGEtOGZkYzE3ZjNmZGExIn0.8faLrFZ9jWoX1ULCDOrk9wfFQTwJX-ZAflmDiKsXbwrKamq7PGeaqqSj1TAOZa4r',NULL,NULL,1),(16,'2026-03-28 14:30:14.378931',NULL,'2026-03-28 14:30:14.378931',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA2MjgyMTQyOTksImlkX3Rva2VuIjoiYTM2NDE5ZGQtNmZlZi00ZDA5LTg2MDAtNWE4YTcwNGM5OWE3In0.ns5_FKdJjD2NBoGg3wqjX_yoIuFgFqLD57iRrRrUIt9WXahB2ynTwKJ8Sx86uW4H',NULL,NULL,1),(17,'2026-03-31 09:46:47.853149',NULL,'2026-03-31 09:46:47.853149',NULL,1,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA4NzA0MDc3NTMsImlkX3Rva2VuIjoiNTgzODk1MmUtMTlkYi00ZGJmLTljNzMtYzZhMzFmNmFjNGQzIn0.DT-OTFR4_3Pgy_ic3G9pkZvLDBEt1_deJtywlS5_MQQ',NULL,NULL,1),(18,'2026-04-02 13:02:12.565626',NULL,'2026-04-02 13:02:12.565626',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDEwNTQ5MzI0NTgsImlkX3Rva2VuIjoiMWM1ZTJlNjYtZGJiOS00NmQ1LTg4YTctZjVjY2JlZmRjZmJiIn0.-yIDtF_pupwtfX7PPjcsyVdnW3ptTcaK1p-HUk1XQyGQTRLt_6NhGUcJkFxhdaKl',NULL,NULL,1),(19,'2026-04-02 13:35:54.859889',NULL,'2026-04-02 13:35:54.859889',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDEwNTY5NTQ3NTgsImlkX3Rva2VuIjoiOWNlYjIyNTctMWQ4Ny00OGMxLTkxMTQtY2M3YjFiMjIwMzdkIn0.tMyX0Xg2Ek5__R3g4UO1R3t-7VnBY8z2IWPd1Sn2tjytlcOXYBJb-G9HcQXGWl-7',NULL,NULL,1),(20,'2026-04-03 01:04:45.606876',NULL,'2026-04-03 01:04:45.606876',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDEwOTgyODU0NzMsImlkX3Rva2VuIjoiOTA2NTllZGUtYzI0My00MTZmLWFmZWUtOWNjMmVhMmVhNGQyIn0.FWHF9G_sDx_B1PnUD9vI0Qx88_gDuJpuiOUAKT6geRM1hI_I0ZH7hbLaIdirMnTE',NULL,NULL,1),(21,'2026-04-03 01:46:28.280606',NULL,'2026-04-03 01:46:28.280606',NULL,1,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDExMDA3ODgxNzYsImlkX3Rva2VuIjoiODU3NzE3YmEtZTg3NC00MTZhLWIyOTEtYTA5ZmUwOTk0NWEyIn0.RkeTouZpZz8xZrTx35dDC7-5A7tcoIQboiPfBansPZY',NULL,NULL,1),(22,'2026-04-05 02:15:46.967960',NULL,'2026-04-05 02:15:46.967960',NULL,1,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDEyNzUzNDY4MzEsImlkX3Rva2VuIjoiYTMzZmJmMjgtOGU4Ny00MGRiLWJlNjAtYWZhNTAwZGNlZTM2In0.FBwLLqigoVDLJwDvtpWX_kjvGOz1qQRMZxTLz7Q-20A',NULL,NULL,1),(23,'2026-04-05 02:34:51.298547',NULL,'2026-04-05 02:34:51.298547',NULL,1,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDEyNzY0OTExNDQsImlkX3Rva2VuIjoiZGE1NzI1ZGEtZmFjZi00NjgwLTg1MzctMTVmZWE4MTU1MzU3In0.V-VXNrz0Zlp3WoKMgwBX5yng60StoeaNSxd5l842Dts',NULL,NULL,1),(24,'2026-04-05 09:04:06.867029',NULL,'2026-04-05 09:04:06.867029',NULL,1,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDEyOTk4NDY3NDEsImlkX3Rva2VuIjoiMDIwN2YzY2MtMDdlOS00YjNlLThlODAtOTk5MTJjMzg1MzczIn0.hNPpbQF6xbGeKH0-BrXoks9l3f1ODA7SKNbhgCfZF-8',NULL,NULL,1),(25,'2026-04-05 09:15:59.272471',NULL,'2026-04-05 09:15:59.272471',NULL,1,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDEzMDA1NTkyMDksImlkX3Rva2VuIjoiMDU4NGQ3NWEtYzRiMi00NTEzLWJiYzgtZjg2YTAxZDA0ZTA3In0.-bEcyAE0GWq1GjmuSRmEa1KoUE7avfSBZMPDnvkHUMs',NULL,NULL,1);
/*!40000 ALTER TABLE `user_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_topic`
--

LOCK TABLES `user_topic` WRITE;
/*!40000 ALTER TABLE `user_topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'english'
--

--
-- Dumping routines for database 'english'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-05 16:23:43
