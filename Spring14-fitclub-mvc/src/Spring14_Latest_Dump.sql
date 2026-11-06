CREATE DATABASE  IF NOT EXISTS `spring14` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `spring14`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: spring14
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_date` datetime(6) NOT NULL,
  `status` enum('CANCELLED','CONFIRMED') NOT NULL,
  `class_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpsqm40sln6jj8u11moxortsrj` (`class_id`),
  KEY `FK2hu43b2w26um1gql30wbf0173` (`member_id`),
  CONSTRAINT `FK2hu43b2w26um1gql30wbf0173` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`),
  CONSTRAINT `FKpsqm40sln6jj8u11moxortsrj` FOREIGN KEY (`class_id`) REFERENCES `fitness_classes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (1,'2026-09-22 21:58:29.487747','CONFIRMED',1,1),(2,'2026-09-30 00:24:29.569879','CONFIRMED',2,1),(3,'2026-10-13 19:22:14.401106','CONFIRMED',2,2),(4,'2026-10-14 10:55:12.457204','CONFIRMED',1,2),(5,'2026-10-14 11:01:49.099950','CONFIRMED',3,2);
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class_schedule`
--

DROP TABLE IF EXISTS `class_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `day_of_week` varchar(15) NOT NULL,
  `end_time` time NOT NULL,
  `start_time` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class_schedule`
--

LOCK TABLES `class_schedule` WRITE;
/*!40000 ALTER TABLE `class_schedule` DISABLE KEYS */;
INSERT INTO `class_schedule` VALUES (1,'MONDAY','08:00:00','07:00:00'),(2,'WEDNESDAY','19:00:00','18:00:00'),(3,'FRIDAY','18:00:00','17:00:00');
/*!40000 ALTER TABLE `class_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fitness_class_trainers`
--

DROP TABLE IF EXISTS `fitness_class_trainers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fitness_class_trainers` (
  `class_id` bigint NOT NULL,
  `trainer_id` bigint NOT NULL,
  PRIMARY KEY (`class_id`,`trainer_id`),
  KEY `FKn18igmg0lhdfbh3yqtmwjttoq` (`trainer_id`),
  CONSTRAINT `FK7lofvvy2bn2k2gn33jxqva49v` FOREIGN KEY (`class_id`) REFERENCES `fitness_classes` (`id`),
  CONSTRAINT `FKn18igmg0lhdfbh3yqtmwjttoq` FOREIGN KEY (`trainer_id`) REFERENCES `trainers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fitness_class_trainers`
--

LOCK TABLES `fitness_class_trainers` WRITE;
/*!40000 ALTER TABLE `fitness_class_trainers` DISABLE KEYS */;
INSERT INTO `fitness_class_trainers` VALUES (1,1),(2,2),(2,3),(3,3);
/*!40000 ALTER TABLE `fitness_class_trainers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fitness_classes`
--

DROP TABLE IF EXISTS `fitness_classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fitness_classes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `capacity` int NOT NULL,
  `class_name` varchar(60) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `schedule_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKe0tawfk9yb0gqyr74fqolrk9h` (`schedule_id`),
  CONSTRAINT `FKlj7yp73ylqeixl858o2vaqcso` FOREIGN KEY (`schedule_id`) REFERENCES `class_schedule` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fitness_classes`
--

LOCK TABLES `fitness_classes` WRITE;
/*!40000 ALTER TABLE `fitness_classes` DISABLE KEYS */;
INSERT INTO `fitness_classes` VALUES (1,15,'Morning Yoga','Gentle flow to start your day',1),(2,12,'HIIT Blast','High intensity interval training',2),(3,8,'Strength Basics','Introductory weight training',3);
/*!40000 ALTER TABLE `fitness_classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_favorite_classes`
--

DROP TABLE IF EXISTS `member_favorite_classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_favorite_classes` (
  `member_id` bigint NOT NULL,
  `class_id` bigint NOT NULL,
  PRIMARY KEY (`member_id`,`class_id`),
  KEY `FKtls5qrj2q44p5kvk2ilwfkwxt` (`class_id`),
  CONSTRAINT `FK7yhxi3gu38yh2423r7d9f4623` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`),
  CONSTRAINT `FKtls5qrj2q44p5kvk2ilwfkwxt` FOREIGN KEY (`class_id`) REFERENCES `fitness_classes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_favorite_classes`
--

LOCK TABLES `member_favorite_classes` WRITE;
/*!40000 ALTER TABLE `member_favorite_classes` DISABLE KEYS */;
INSERT INTO `member_favorite_classes` VALUES (1,1),(2,1),(2,2),(2,3);
/*!40000 ALTER TABLE `member_favorite_classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `registered_on` datetime(6) NOT NULL,
  `username` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9d30a9u1qpg8eou0otgkwrp5d` (`email`),
  UNIQUE KEY `UKlj4daw762ura5d2y6iu7g5n1i` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members`
--

LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
INSERT INTO `members` VALUES (1,'ravi@example.com','secret123','2026-09-20 22:21:29.260332','ravi_k'),(2,'roshansmariage@indigo','roshan','2026-10-12 05:32:45.160395','roshan'),(3,'aryan@example.com','aryan1','2026-10-17 18:42:46.139344','aryan');
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members_profile`
--

DROP TABLE IF EXISTS `members_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members_profile` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(200) DEFAULT NULL,
  `bio` varchar(500) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKji9pop55k81d9wv28j1kyg5ov` (`member_id`),
  CONSTRAINT `FKkvkoddljh26lufjiyom32wm6h` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members_profile`
--

LOCK TABLES `members_profile` WRITE;
/*!40000 ALTER TABLE `members_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `members_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainers`
--

DROP TABLE IF EXISTS `trainers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(60) NOT NULL,
  `specialization` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainers`
--

LOCK TABLES `trainers` WRITE;
/*!40000 ALTER TABLE `trainers` DISABLE KEYS */;
INSERT INTO `trainers` VALUES (1,'Asha Patel','Yoga & Flexibility'),(2,'Ravi Kumar','HIIT & Conditioning'),(3,'Meera Iyer','Strength Training');
/*!40000 ALTER TABLE `trainers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-06 17:48:32
