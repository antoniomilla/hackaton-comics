DROP DATABASE IF EXISTS `Acme-Comics`;
CREATE DATABASE `Acme-Comics`;

-- This creates the user, if it does not exist, which is required to
-- portably implement "DROP USER IF EXISTS".
SET @mode = @@SESSION.sql_mode;
SET SESSION sql_mode = REPLACE(REPLACE(@mode, 'NO_AUTO_CREATE_USER', ''), ',,', ',');
GRANT USAGE ON `Acme-Comics`.* TO 'acme-user'@'%' IDENTIFIED BY 'a';
GRANT USAGE ON `Acme-Comics`.* TO 'acme-manager'@'%' IDENTIFIED BY 'a';
SET SESSION sql_mode = @mode;

DROP USER 'acme-user'@'%';
DROP USER 'acme-manager'@'%';

CREATE USER 'acme-user'@'%' IDENTIFIED BY 'ACME-Us3r-P@ssw0rd';
CREATE USER 'acme-manager'@'%' IDENTIFIED BY 'ACME-M@n@ger-6874';

GRANT SELECT, INSERT, UPDATE, DELETE
	ON `Acme-Comics`.* TO 'acme-user'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER,
        CREATE TEMPORARY TABLES, LOCK TABLES, CREATE VIEW, CREATE ROUTINE,
        ALTER ROUTINE, EXECUTE, TRIGGER, SHOW VIEW
    ON `Acme-Comics`.* TO 'acme-manager'@'%';

USE `Acme-Comics`;

-- MySQL dump 10.15  Distrib 10.0.36-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: Acme-Comics
-- ------------------------------------------------------
-- Server version	10.0.36-MariaDB-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Administrator`
--

DROP TABLE IF EXISTS `Administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `description` longtext,
  `nickname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_idt4b4u259p6vs4pyr9lax4eg` (`userAccount_id`),
  CONSTRAINT `FK_idt4b4u259p6vs4pyr9lax4eg` FOREIGN KEY (`userAccount_id`) REFERENCES `UserAccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Administrator`
--

LOCK TABLES `Administrator` WRITE;
/*!40000 ALTER TABLE `Administrator` DISABLE KEYS */;
INSERT INTO `Administrator` VALUES (111,0,'2018-07-15 00:00:00',NULL,'Administrator 1',112),(113,0,'2018-07-15 00:00:00',NULL,'Administrador 2',114);
/*!40000 ALTER TABLE `Administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Author`
--

DROP TABLE IF EXISTS `Author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Author` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `birthDate` date DEFAULT NULL,
  `birthPlace` varchar(255) DEFAULT NULL,
  `description` longtext,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Author`
--

LOCK TABLES `Author` WRITE;
/*!40000 ALTER TABLE `Author` DISABLE KEYS */;
/*!40000 ALTER TABLE `Author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Comic`
--

DROP TABLE IF EXISTS `Comic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comic` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` longtext,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `volumeCount` int(11) NOT NULL,
  `author_id` int(11) NOT NULL,
  `publisher_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_bgbmwxrjbl3yjk1xy1vnb9rrx` (`author_id`),
  KEY `FK_caqys1c7l7h8k7xege48sb4pb` (`publisher_id`),
  CONSTRAINT `FK_bgbmwxrjbl3yjk1xy1vnb9rrx` FOREIGN KEY (`author_id`) REFERENCES `Author` (`id`),
  CONSTRAINT `FK_caqys1c7l7h8k7xege48sb4pb` FOREIGN KEY (`publisher_id`) REFERENCES `Publisher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comic`
--

LOCK TABLES `Comic` WRITE;
/*!40000 ALTER TABLE `Comic` DISABLE KEYS */;
/*!40000 ALTER TABLE `Comic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ComicCharacter`
--

DROP TABLE IF EXISTS `ComicCharacter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ComicCharacter` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `description` longtext,
  `firstAppearance` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `publisher_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6bsulf0kinrx7wau2kb8wladb` (`publisher_id`),
  CONSTRAINT `FK_6bsulf0kinrx7wau2kb8wladb` FOREIGN KEY (`publisher_id`) REFERENCES `Publisher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ComicCharacter`
--

LOCK TABLES `ComicCharacter` WRITE;
/*!40000 ALTER TABLE `ComicCharacter` DISABLE KEYS */;
/*!40000 ALTER TABLE `ComicCharacter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ComicCharacter_otherAliases`
--

DROP TABLE IF EXISTS `ComicCharacter_otherAliases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ComicCharacter_otherAliases` (
  `ComicCharacter_id` int(11) NOT NULL,
  `otherAliases` varchar(255) DEFAULT NULL,
  KEY `FK_qr361wa4w3n5sunmg6lne1vy4` (`ComicCharacter_id`),
  CONSTRAINT `FK_qr361wa4w3n5sunmg6lne1vy4` FOREIGN KEY (`ComicCharacter_id`) REFERENCES `ComicCharacter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ComicCharacter_otherAliases`
--

LOCK TABLES `ComicCharacter_otherAliases` WRITE;
/*!40000 ALTER TABLE `ComicCharacter_otherAliases` DISABLE KEYS */;
/*!40000 ALTER TABLE `ComicCharacter_otherAliases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ComicComicCharacter`
--

DROP TABLE IF EXISTS `ComicComicCharacter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ComicComicCharacter` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `comic_id` int(11) NOT NULL,
  `comicCharacter_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sf05ih2n8e5xalt7upai7ml0u` (`comic_id`,`comicCharacter_id`),
  KEY `FK_3bnnlhq0wtktcnf3gcfn1shbe` (`comicCharacter_id`),
  CONSTRAINT `FK_3bnnlhq0wtktcnf3gcfn1shbe` FOREIGN KEY (`comicCharacter_id`) REFERENCES `ComicCharacter` (`id`),
  CONSTRAINT `FK_l4jweeg8wqs7tr1o4p47s8l12` FOREIGN KEY (`comic_id`) REFERENCES `Comic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ComicComicCharacter`
--

LOCK TABLES `ComicComicCharacter` WRITE;
/*!40000 ALTER TABLE `ComicComicCharacter` DISABLE KEYS */;
/*!40000 ALTER TABLE `ComicComicCharacter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Comic_tags`
--

DROP TABLE IF EXISTS `Comic_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comic_tags` (
  `Comic_id` int(11) NOT NULL,
  `tags` varchar(255) DEFAULT NULL,
  KEY `FK_1q35st7neld7qs0ocmnbauscg` (`Comic_id`),
  CONSTRAINT `FK_1q35st7neld7qs0ocmnbauscg` FOREIGN KEY (`Comic_id`) REFERENCES `Comic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comic_tags`
--

LOCK TABLES `Comic_tags` WRITE;
/*!40000 ALTER TABLE `Comic_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `Comic_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Comment`
--

DROP TABLE IF EXISTS `Comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Comment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `text` longtext,
  `author_id` int(11) DEFAULT NULL,
  `comic_id` int(11) DEFAULT NULL,
  `comicCharacter_id` int(11) DEFAULT NULL,
  `publisher_id` int(11) DEFAULT NULL,
  `sale_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `volume_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_owfig5srycdpbl69jh2p665tl` (`user_id`,`creationTime`),
  KEY `UK_2w2y9obgyk8suxjnaadj28cn3` (`publisher_id`,`creationTime`),
  KEY `UK_560pg3co1lrpsed4crfbhmgk` (`author_id`,`creationTime`),
  KEY `UK_1vreat9gy7ak04q439v2mx81b` (`comic_id`,`creationTime`),
  KEY `UK_6pvloj7a51p30y0ixy14kuq6i` (`volume_id`,`creationTime`),
  KEY `UK_gbi5e3gvbvdmkmgogop0ensf0` (`comicCharacter_id`,`creationTime`),
  KEY `UK_k2hdv9lethvd6hg3ol53j8gej` (`sale_id`,`creationTime`),
  CONSTRAINT `FK_656l7l2xefsprygvcgbicqcy6` FOREIGN KEY (`comic_id`) REFERENCES `Comic` (`id`),
  CONSTRAINT `FK_8ty13fu0k88mcyprlsjl3f6wi` FOREIGN KEY (`sale_id`) REFERENCES `Sale` (`id`),
  CONSTRAINT `FK_a2add0jblgj52e0rwaggn37th` FOREIGN KEY (`volume_id`) REFERENCES `Volume` (`id`),
  CONSTRAINT `FK_gn61mficmjyetdx9gbuaugasf` FOREIGN KEY (`comicCharacter_id`) REFERENCES `ComicCharacter` (`id`),
  CONSTRAINT `FK_j94pith5sd971k29j6ysxuk7` FOREIGN KEY (`author_id`) REFERENCES `Author` (`id`),
  CONSTRAINT `FK_jhvt6d9ap8gxv67ftrmshdfhj` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  CONSTRAINT `FK_msnl6q6rbi64125lx4amkcj2d` FOREIGN KEY (`publisher_id`) REFERENCES `Publisher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comment`
--

LOCK TABLES `Comment` WRITE;
/*!40000 ALTER TABLE `Comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `Comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DirectMessage`
--

DROP TABLE IF EXISTS `DirectMessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DirectMessage` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `administrationNotice` bit(1) NOT NULL,
  `body` longtext,
  `creationTime` datetime DEFAULT NULL,
  `readByUser` bit(1) NOT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `messageFolder_id` int(11) NOT NULL,
  `recipient_id` int(11) NOT NULL,
  `sale_id` int(11) DEFAULT NULL,
  `sender_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_h7vcpb8tv46odmqr8be6cqc3k` (`messageFolder_id`,`creationTime`),
  KEY `UK_n68935ecc41qnj5abb1hjaps4` (`sale_id`,`creationTime`),
  CONSTRAINT `FK_bjw26hkd0s8qnj7ipe7m2adm8` FOREIGN KEY (`messageFolder_id`) REFERENCES `MessageFolder` (`id`),
  CONSTRAINT `FK_bmevpw5jd68g1la6oqx9oqlpm` FOREIGN KEY (`sale_id`) REFERENCES `Sale` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DirectMessage`
--

LOCK TABLES `DirectMessage` WRITE;
/*!40000 ALTER TABLE `DirectMessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `DirectMessage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MessageFolder`
--

DROP TABLE IF EXISTS `MessageFolder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MessageFolder` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `actor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_ulrq9k7vg9o7m9ldlxks1owi` (`actor_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MessageFolder`
--

LOCK TABLES `MessageFolder` WRITE;
/*!40000 ALTER TABLE `MessageFolder` DISABLE KEYS */;
INSERT INTO `MessageFolder` VALUES (129,0,NULL,'SYSTEM_INBOX',111),(130,0,NULL,'SYSTEM_SENT',111),(131,0,NULL,'SYSTEM_TRASH',111);
/*!40000 ALTER TABLE `MessageFolder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Publisher`
--

DROP TABLE IF EXISTS `Publisher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Publisher` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` longtext,
  `foundationDate` date DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Publisher`
--

LOCK TABLES `Publisher` WRITE;
/*!40000 ALTER TABLE `Publisher` DISABLE KEYS */;
/*!40000 ALTER TABLE `Publisher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Sale`
--

DROP TABLE IF EXISTS `Sale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Sale` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `comic_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `userSoldTo_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_ktyoeof753w2pncr1jhqcxakh` (`status`,`creationTime`),
  KEY `UK_s6dyeyy4v8h02v0ld5bfyywlv` (`comic_id`,`creationTime`),
  KEY `FK_momf1sy7ohllk8sqj1w6hfwvd` (`user_id`),
  KEY `FK_p8rr8o681yhfdif95i9tlwto8` (`userSoldTo_id`),
  CONSTRAINT `FK_ey13332xw833ps6448pitxu19` FOREIGN KEY (`comic_id`) REFERENCES `Comic` (`id`),
  CONSTRAINT `FK_momf1sy7ohllk8sqj1w6hfwvd` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  CONSTRAINT `FK_p8rr8o681yhfdif95i9tlwto8` FOREIGN KEY (`userSoldTo_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Sale`
--

LOCK TABLES `Sale` WRITE;
/*!40000 ALTER TABLE `Sale` DISABLE KEYS */;
/*!40000 ALTER TABLE `Sale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Sale_User`
--

DROP TABLE IF EXISTS `Sale_User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Sale_User` (
  `Sale_id` int(11) NOT NULL,
  `interestedUsers_id` int(11) NOT NULL,
  KEY `FK_j1ylyhnt6fksi8xqpxfuqq29q` (`interestedUsers_id`),
  KEY `FK_6iqv4q9s1gi9folbfo2jx5u31` (`Sale_id`),
  CONSTRAINT `FK_6iqv4q9s1gi9folbfo2jx5u31` FOREIGN KEY (`Sale_id`) REFERENCES `Sale` (`id`),
  CONSTRAINT `FK_j1ylyhnt6fksi8xqpxfuqq29q` FOREIGN KEY (`interestedUsers_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Sale_User`
--

LOCK TABLES `Sale_User` WRITE;
/*!40000 ALTER TABLE `Sale_User` DISABLE KEYS */;
/*!40000 ALTER TABLE `Sale_User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Sale_images`
--

DROP TABLE IF EXISTS `Sale_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Sale_images` (
  `Sale_id` int(11) NOT NULL,
  `images` varchar(255) DEFAULT NULL,
  KEY `FK_id43d7ib7gnykjpo41b5521hi` (`Sale_id`),
  CONSTRAINT `FK_id43d7ib7gnykjpo41b5521hi` FOREIGN KEY (`Sale_id`) REFERENCES `Sale` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Sale_images`
--

LOCK TABLES `Sale_images` WRITE;
/*!40000 ALTER TABLE `Sale_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `Sale_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `description` longtext,
  `nickname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  `blockReason` varchar(255) DEFAULT NULL,
  `blocked` bit(1) NOT NULL,
  `lastLevelUpdateTime` datetime DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `onlyFriendsCanSendDms` bit(1) NOT NULL,
  `trusted` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o6s94d43co03sx067ili5760c` (`userAccount_id`),
  CONSTRAINT `FK_o6s94d43co03sx067ili5760c` FOREIGN KEY (`userAccount_id`) REFERENCES `UserAccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserAccount`
--

DROP TABLE IF EXISTS `UserAccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserAccount` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_csivo9yqa08nrbkog71ycilh5` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserAccount`
--

LOCK TABLES `UserAccount` WRITE;
/*!40000 ALTER TABLE `UserAccount` DISABLE KEYS */;
INSERT INTO `UserAccount` VALUES (112,0,'21232f297a57a5a743894a0e4a801fc3','admin');
/*!40000 ALTER TABLE `UserAccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserAccount_authorities`
--

DROP TABLE IF EXISTS `UserAccount_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserAccount_authorities` (
  `UserAccount_id` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_b63ua47r0u1m7ccc9lte2ui4r` (`UserAccount_id`),
  CONSTRAINT `FK_b63ua47r0u1m7ccc9lte2ui4r` FOREIGN KEY (`UserAccount_id`) REFERENCES `UserAccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserAccount_authorities`
--

LOCK TABLES `UserAccount_authorities` WRITE;
/*!40000 ALTER TABLE `UserAccount_authorities` DISABLE KEYS */;
INSERT INTO `UserAccount_authorities` VALUES (112,'ADMINISTRATOR');
/*!40000 ALTER TABLE `UserAccount_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserComic`
--

DROP TABLE IF EXISTS `UserComic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserComic` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `readVolumeCount` int(11) NOT NULL,
  `score` int(11) DEFAULT NULL,
  `starred` bit(1) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `comic_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dibc2xo7iuvh3fr8jcfx1ehk9` (`user_id`,`comic_id`),
  KEY `UK_tihd14cearv00y2y3qc9guap0` (`user_id`,`status`),
  KEY `FK_3ysj1vl48skslmd39lrpqsy9o` (`comic_id`),
  CONSTRAINT `FK_3ysj1vl48skslmd39lrpqsy9o` FOREIGN KEY (`comic_id`) REFERENCES `Comic` (`id`),
  CONSTRAINT `FK_k4k2csyb0yaj1yrl5oux6kpuw` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserComic`
--

LOCK TABLES `UserComic` WRITE;
/*!40000 ALTER TABLE `UserComic` DISABLE KEYS */;
/*!40000 ALTER TABLE `UserComic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserComic_Volume`
--

DROP TABLE IF EXISTS `UserComic_Volume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserComic_Volume` (
  `userComics_id` int(11) NOT NULL,
  `readVolumes_id` int(11) NOT NULL,
  KEY `FK_3sr71n4c1esaxoor3mtc5ggtt` (`readVolumes_id`),
  KEY `FK_p96kt8xb0eiy8xtriuuk1jiji` (`userComics_id`),
  CONSTRAINT `FK_3sr71n4c1esaxoor3mtc5ggtt` FOREIGN KEY (`readVolumes_id`) REFERENCES `Volume` (`id`),
  CONSTRAINT `FK_p96kt8xb0eiy8xtriuuk1jiji` FOREIGN KEY (`userComics_id`) REFERENCES `UserComic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserComic_Volume`
--

LOCK TABLES `UserComic_Volume` WRITE;
/*!40000 ALTER TABLE `UserComic_Volume` DISABLE KEYS */;
/*!40000 ALTER TABLE `UserComic_Volume` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_User`
--

DROP TABLE IF EXISTS `User_User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User_User` (
  `User_id` int(11) NOT NULL,
  `friends_id` int(11) NOT NULL,
  KEY `FK_kukojr3rgw9vjhxl8jtipwhp2` (`friends_id`),
  KEY `FK_nlnx78x3m38aq2r86t1d5eio1` (`User_id`),
  CONSTRAINT `FK_kukojr3rgw9vjhxl8jtipwhp2` FOREIGN KEY (`friends_id`) REFERENCES `User` (`id`),
  CONSTRAINT `FK_nlnx78x3m38aq2r86t1d5eio1` FOREIGN KEY (`User_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_User`
--

LOCK TABLES `User_User` WRITE;
/*!40000 ALTER TABLE `User_User` DISABLE KEYS */;
/*!40000 ALTER TABLE `User_User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Volume`
--

DROP TABLE IF EXISTS `Volume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Volume` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `chapterCount` int(11) DEFAULT NULL,
  `description` longtext,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `orderNumber` int(11) NOT NULL,
  `releaseDate` date DEFAULT NULL,
  `author_id` int(11) NOT NULL,
  `comic_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_2wokuq3k8mftwd8yhcyu4e9rq` (`comic_id`,`orderNumber`,`name`),
  KEY `UK_lshi8l09k1ov9wqnmnlvjo3uo` (`author_id`,`releaseDate`),
  CONSTRAINT `FK_ev6afd4qxitbsrswj6auh160h` FOREIGN KEY (`comic_id`) REFERENCES `Comic` (`id`),
  CONSTRAINT `FK_n141p0wjla40e1mpfi2rel91e` FOREIGN KEY (`author_id`) REFERENCES `Author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Volume`
--

LOCK TABLES `Volume` WRITE;
/*!40000 ALTER TABLE `Volume` DISABLE KEYS */;
/*!40000 ALTER TABLE `Volume` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('DomainEntity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-13 11:06:06
