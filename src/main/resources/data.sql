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
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `author` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_user` varchar(255) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `biography` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES (4,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/ava_thompson.jpg','Contemporary writer of short stories and essays','Ava Thompson','US'),(5,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/liam_johnson.jpg','Academic and language educator','Liam Johnson','US'),(6,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/olivia_martinez.jpg','Translator and novelist','Olivia Martinez','ES'),(7,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/noah_brown.jpg','Children books author and storyteller','Noah Brown','AU'),(8,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/emma_wilson.jpg','Writer focusing on travel and culture','Emma Wilson','CA'),(9,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/oliver_davis.jpg','Poet and essayist','Oliver Davis','UK'),(10,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/sophia_garcia.jpg','Young adult fiction author','Sophia Garcia','MX'),(11,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/william_miller.jpg','Historian and textbook author','William Miller','US'),(12,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/isabella_rodriguez.jpg','Language learning content creator','Isabella Rodriguez','ES'),(13,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/james_martinez.jpg','Short fiction and dialogue-focused writer','James Martinez','US'),(14,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/mia_hernandez.jpg','Educational book author','Mia Hernandez','CO'),(15,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/benjamin_lopez.jpg','Novelist and playwright','Benjamin Lopez','AR'),(16,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/charlotte_gonzalez.jpg','Cultural commentator and blogger','Charlotte Gonzalez','US'),(17,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/elijah_perez.jpg','Sci-fi short story author','Elijah Perez','US'),(18,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/amelia_sanchez.jpg','Children educational content','Amelia Sanchez','ES'),(19,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/lucas_ramirez.jpg','Travelogue and essays','Lucas Ramirez','CL'),(20,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/harper_torres.jpg','Language pedagogy researcher','Harper Torres','US'),(21,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/henry_nguyen.jpg','Bilingual educator and author','Henry Nguyen','VN'),(22,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/evelyn_kim.jpg','Modern fiction and translations','Evelyn Kim','KR'),(23,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/alexander_lee.jpg','Non-fiction on communication','Alexander Lee','US'),(24,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/sofia_patel.jpg','Young readers author','Sofia Patel','IN'),(25,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/daniel_wright.jpg','Mystery novelist','Daniel Wright','UK'),(26,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/grace_scott.jpg','Creative writing instructor','Grace Scott','US'),(27,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/michael_green.jpg','Essayist and critic','Michael Green','US'),(28,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/chloe_adams.jpg','Romance and contemporary fiction','Chloe Adams','AU'),(29,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/jack_baker.jpg','Adventure stories for young adults','Jack Baker','UK'),(30,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/zoe_carter.jpg','Educational technologist and writer','Zoe Carter','US'),(31,'2026-03-26 19:52:23.000000','system',NULL,NULL,1,'https://example.com/avatars/ryan_mitchell.jpg','Language learning researcher','Ryan Mitchell','US');
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `author_book`
--

DROP TABLE IF EXISTS `author_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `author_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author_id` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author_book`
--

LOCK TABLES `author_book` WRITE;
/*!40000 ALTER TABLE `author_book` DISABLE KEYS */;
INSERT INTO `author_book` VALUES (5,5,5),(6,4,5),(7,6,6),(8,11,6),(9,7,7),(10,18,7),(11,8,8),(12,25,8),(13,9,9),(14,1,9),(15,5,11),(16,9,12),(17,10,13),(18,11,14),(19,5,125),(20,7,125),(21,5,16),(22,8,16);
/*!40000 ALTER TABLE `author_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_user` varchar(255) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `cover_url` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (5,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Everyday English: Conversations 1'),(6,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Everyday English: Conversations 2'),(7,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Stories for Beginners A'),(8,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Stories for Beginners B'),(9,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Travel English Guide'),(10,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Business English Basics'),(11,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Food & Dining Phrases'),(12,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','English for Work'),(13,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Kids: Animal Stories'),(14,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Advanced Readings'),(15,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Pronunciation Practice'),(16,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Grammar in Use: Exercises'),(17,'2026-03-26 19:53:00.000000','system',NULL,NULL,1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','Idioms and Expressions'),(125,'2026-03-27 01:02:01.530680','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-27 01:02:01.530680','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,'https://tse3.mm.bing.net/th/id/OIP.Z9tH0d8B_BUyYWObWVeNKgHaL0?rs=1&pid=ImgDetMain&o=7&rm=3','en','7 viên ngọc rồng');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_genre`
--

DROP TABLE IF EXISTS `book_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `book_genre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) DEFAULT NULL,
  `genre_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_genre`
--

LOCK TABLES `book_genre` WRITE;
/*!40000 ALTER TABLE `book_genre` DISABLE KEYS */;
INSERT INTO `book_genre` VALUES (6,5,5),(7,6,6),(8,7,7),(9,8,8),(10,9,9),(11,10,10),(12,11,11),(13,12,12),(14,13,13),(15,14,14),(16,125,9),(17,125,4),(18,16,6),(19,16,9);
/*!40000 ALTER TABLE `book_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_progress`
--

DROP TABLE IF EXISTS `book_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `book_progress` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_user` varchar(255) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `book_id` int(11) DEFAULT NULL,
  `is_favorite` int(11) DEFAULT NULL,
  `last_read` datetime(6) DEFAULT NULL,
  `progress_percent` double DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `last_read_page_number` int(11) DEFAULT NULL,
  `last_read_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_progress`
--

LOCK TABLES `book_progress` WRITE;
/*!40000 ALTER TABLE `book_progress` DISABLE KEYS */;
INSERT INTO `book_progress` VALUES (1,'2026-03-26 12:57:19.258453','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:57:19.258453','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',0,16,1,NULL,10,1,NULL,NULL),(2,NULL,NULL,NULL,NULL,1,125,0,NULL,20,1,NULL,NULL);
/*!40000 ALTER TABLE `book_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `genre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_user` varchar(255) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (4,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Suspense and mystery fiction','Mystery','https://www.shutterstock.com/image-vector/hand-drawn-criminal-investigation-icons-600nw-2700024747.jpg'),(5,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Romantic fiction and relationships','Romance','https://example.com/genres/romance.jpg'),(6,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Science fiction and futuristic stories','Sci-Fi','https://example.com/genres/scifi.jpg'),(7,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Informative and factual works','Non-Fiction','https://example.com/genres/nonfiction.jpg'),(8,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Books for children and early readers','Children','https://example.com/genres/children.jpg'),(9,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Young adult fiction','Young Adult','https://example.com/genres/ya.jpg'),(10,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Historical fiction and narratives','Historical','https://example.com/genres/historical.jpg'),(11,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Personal development and guides','Self-Help','https://example.com/genres/selfhelp.jpg'),(12,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Textbooks and language learning materials','Education','https://example.com/genres/education.jpg'),(13,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Collections of poems','Poetry','https://example.com/genres/poetry.jpg'),(14,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Travel writing and guides','Travel','https://example.com/genres/travel.jpg'),(15,'2026-03-26 19:52:43.000000','system',NULL,NULL,1,'Life stories and memoirs','Biography','https://example.com/genres/biography.jpg');
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_user` varchar(255) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'2026-03-26 12:51:49.996860','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:51:49.996860','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,NULL,'admin@gmail.com','admin 20024','$2a$10$5H8hgCQY6hcjsllVPAnire6OD9r5Zve4Ze56/jI7DJj.v0TBgP6Ue',2);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_genre`
--

DROP TABLE IF EXISTS `user_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_genre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `genre_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_genre`
--

LOCK TABLES `user_genre` WRITE;
/*!40000 ALTER TABLE `user_genre` DISABLE KEYS */;
INSERT INTO `user_genre` VALUES (1,1,1),(2,2,1),(3,4,1),(4,3,1),(5,3,1),(6,3,1),(7,3,1),(8,13,1),(9,6,1),(10,8,1),(11,12,1),(12,15,1),(13,11,1),(14,9,1),(15,14,1);
/*!40000 ALTER TABLE `user_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_profile`
--

DROP TABLE IF EXISTS `user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `daily_goal_minutes` int(11) DEFAULT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  `learning_goal` varchar(255) DEFAULT NULL,
  `level_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_profile`
--

LOCK TABLES `user_profile` WRITE;
/*!40000 ALTER TABLE `user_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_session`
--

DROP TABLE IF EXISTS `user_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_user` varchar(255) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `remote_ip` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7fxbfjuyejj8uk6hld0odiub3` (`refresh_token`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_session`
--

LOCK TABLES `user_session` WRITE;
/*!40000 ALTER TABLE `user_session` DISABLE KEYS */;
INSERT INTO `user_session` VALUES (1,'2026-03-26 12:54:08.036125','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)','2026-03-26 12:54:08.036125','User(email=admin 20024, password=null, fullName=null, avatar=null, role=2)',1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0NDk2NDgwMDQsImlkX3Rva2VuIjoiOTQ2NDQ3NGMtNjExNy00YTJmLTk4Y2YtMjgyMmE3N2RjMGVjIn0.9oI_ckfC99d37T6cgCnCLUS2U3cMX7j_VW-TCXf85jM38oBaF9sNyEhj1pbP2qF3',NULL,NULL,1),(2,'2026-03-27 00:11:29.568862',NULL,'2026-03-27 00:11:29.568862',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTAyODk0NjEsImlkX3Rva2VuIjoiNWZiYzUwYTMtMWQ5Ni00ODhjLTgzNDAtZjk0Y2U3NzM1NGUyIn0.PyXANbAEm9WnN53zfEjYGoF8dFTZDby94zVj6Kk6bYWW8-N_9W6TJZ6U2UoCHVsG',NULL,NULL,1),(3,'2026-03-27 00:11:58.545434',NULL,'2026-03-27 00:11:58.545434',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTAzMTg1MzksImlkX3Rva2VuIjoiYjI0ZDYzOWEtYTBmMi00ZmM3LWFjMjMtNjRhYTUwMjg5YjY4In0.kgeWWiXkUr-SIr5fvrTZ2pvS4Bpv0Z7hi_rrt3FjHhlc4-zyQyBaHF-YeQRI5DWo',NULL,NULL,1),(4,'2026-03-27 00:17:35.069325',NULL,'2026-03-27 00:17:35.069325',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTA2NTUwMTMsImlkX3Rva2VuIjoiNDY1NzQ5YzYtYzk4My00ZDlkLWFhMzUtZWI0YWUxZDg0NzgxIn0.lN_s4TQa25c9hL67jglFyhdhWHQMuCaAwcMzbT7X9_AcXHVT1RSfysBpJ3gLkKTV',NULL,NULL,1),(5,'2026-03-27 00:28:44.523190',NULL,'2026-03-27 00:28:44.523190',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTEzMjQ1MjMsImlkX3Rva2VuIjoiNTFmYTlkNjgtZjFlMC00NTVhLWJhMWUtMGM2Mjk5NDQwMDljIn0.jlO55QAUcftUDQz7pmUxP4Uw-iSwDDi47c9BmUVqHDLMPd_vWbqkb9kqQxdadus4',NULL,NULL,1),(6,'2026-03-27 00:28:46.639027',NULL,'2026-03-27 00:28:46.639027',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTEzMjY2MzksImlkX3Rva2VuIjoiMjlkZTEwYzctMjBlMC00ZDY0LWEzZWUtNmE3Mzk1OGZkMTIyIn0.h8McAkX0AwMkCioCYQ4GWJyhC2SKusbVC_v2mM_VwC8x8m6AQT-m-UqS0hQNzHgz',NULL,NULL,1),(7,'2026-03-27 00:29:36.626915',NULL,'2026-03-27 00:29:36.626915',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTEzNzY2MTYsImlkX3Rva2VuIjoiYmY4ZTBlYTYtZDM3ZS00MGJlLTljNTQtNjQzN2Q0ODIzMmExIn0.7p1REmEiltVC4ZepnjpjkkSMWaYY56RCfuePMIu1AJm8OujnyZQF7_3j0NGlF7sZ',NULL,NULL,1),(8,'2026-03-27 00:30:14.474556',NULL,'2026-03-27 00:30:14.474556',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTE0MTQ0NzQsImlkX3Rva2VuIjoiMjhmYTdmNDMtMTY0Zi00ZmIwLTllNjMtYmQ1MzhiZjI0YjlkIn0.XB5lOomztNais2n0leG5NbeijrJDhrFZg21NUFQ9fLGZFLnt544X4FyGAlrZI25e',NULL,NULL,1),(9,'2026-03-27 00:53:38.488150',NULL,'2026-03-27 00:53:38.488150',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA0OTI4MTg0ODQsImlkX3Rva2VuIjoiMWFlNTE3N2EtMWMxNi00ZjFkLWI0NGMtZmFjYWQ4ZmE5NzA3In0.nHJYkuwHoB28IwPYsqI375HEda-0nLE7lPWI92d8J5R7CMyCcR2-aoHCDwL6DT10',NULL,NULL,1),(10,'2026-03-27 15:22:58.036058',NULL,'2026-03-27 15:22:58.036058',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA1NDQ5Nzc5NTYsImlkX3Rva2VuIjoiMjViMDMyYzEtNjNkMy00MTNjLTgyODMtYTM0YmIxZGI1ZTdjIn0.PBb60uun1h-0pc4MTRZQh57Vuib7jR1_tPV6tKRro-3yJXmbHDYa1waQ4FLJPalw',NULL,NULL,1),(11,'2026-03-27 15:28:53.205504',NULL,'2026-03-27 15:28:53.205504',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA1NDUzMzMwODgsImlkX3Rva2VuIjoiMjYwZjVkZjItYTFhOC00MjkxLTkxZTQtYjM3YjljMzljYjMyIn0.E8o7H6Ka7FuLfGoWOaM3pTo6sNs1e3V3Lb2b7EeewbtSzHqF34M6sDD6bBeN4exh',NULL,NULL,1),(12,'2026-03-27 15:58:06.131290',NULL,'2026-03-27 15:58:06.131290',NULL,1,'eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOjEsImV4cGlyZWQiOjE4MDA1NDcwODYwODEsImlkX3Rva2VuIjoiNzUyYTc3YjktZjllYi00ZTkwLTgzMDgtOTAwYjBhODQyNmZiIn0.iWgWBEFo81D2hpdETfLv3ApoDbzLvdDu-w7YFLjzcJ6dyxHRoDqVeDJmMgidkrup',NULL,NULL,1);
/*!40000 ALTER TABLE `user_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_topic`
--

DROP TABLE IF EXISTS `user_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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

-- Dump completed on 2026-03-28 20:35:27
