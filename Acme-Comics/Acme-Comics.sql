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
INSERT INTO `Author` VALUES (106,0,'1973-01-25','Detroit, Michigan, EEUU','One of the best comic book authors of his generation.    Known for his absolut knowledge of the superheroe world.','https://i.imgur.com/ZK7gYgU.jpg','Geoff Johns'),(107,0,'1973-01-25','Glasgow, Scotland','One of the best comic book authors of his generation.    He is known for his anti-american stories.','https://i.imgur.com/SompCDe.jpg','Mark Millar'),(108,0,'1953-11-18','Northampton, England','Considered as the greatest comic book   writer in history, he is known for being the author of some of the best   masterpieces of this art like Watchmen, V for Vendetta, From Hell,etc.','https://i.imgur.com/b0oONUB.jpg','Alan Moore'),(109,0,'1957-01-27','Olney, Maryland, EEUU','One of the best comic book authors of    history, he is one of the authors who reinvented the superheroe genre on the   80\'s. Known for being the author of masterpieces like Dark knight returns,    Daredevil:Born again, Sin City, etc.','https://i.imgur.com/2jwQhPF.jpg','Frank Miller'),(110,0,'1978-11-30','Richmon, Kentucky, EEUU','Robert kirkman is an american comic book author who is most known   for being the author of the famous comic book serie named The walkind dead.','https://i.imgur.com/QBIjdsS.jpg','Robert kirkman');
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
INSERT INTO `Comic` VALUES (123,0,'Due to Flash\'s actions, the world turned into    an alternate reality set in a civil war.','https://i.imgur.com/CBHopfy.jpg','Flashpoint',6,106,103),(124,0,'Due to government issues, the superheroes of the world split in two groups,    one led by Captain America and the other one led by Ironman. Both groups fight in a war to achieve their own goals.','https://i.imgur.com/awsZKCj.png','Civil War',3,107,104),(125,0,'1984. Superheroes have been banned and   now they only can work for the government. One of them is murdered and Rorschach,    a old hero who works against the government rules, starts investigating it.','https://i.imgur.com/ZGY4lBj.png','Watchmen',0,108,103),(126,0,'Daredevil\'s secret identity is blown by his    worst enemy, Kingpin. Due to this, his life becomes an absolut hell.','https://i.imgur.com/jIjyRIb.jpg','Daredevil: Born again',0,109,104),(127,0,'This comic shows a distopic reality where the zombies have taken over the world and    a group of peope has to survive in it.','https://i.imgur.com/O3sYUfT.jpg','The walking dead',0,110,105),(128,0,'Superman was born on the planet Krypton, and as a baby was sent to Earth in a small spaceship by his scientist father Jor-El, moments before Krypton was destroyed in a natural cataclysm. His ship landed in the American countryside, where he was discovered and adopted by a farming couple. They named him Clark Kent and imbued him with a strong moral compass. He developed various superhuman abilities, which he resolved to use for the benefit of humanity. Clark Kent resides in the fictional American city of Metropolis, where he works as a journalist for the Daily Planet, a newspaper. To protect his privacy, he changes into a colorful costume and uses the alias \"Superman\" when fighting crime. Superman\'s love interest is his fellow journalist Lois Lane, and his classic archenemy is the genius inventor Lex Luthor. He is a friend of many other superheroes in the DC Universe, such as Batman and Wonder Woman.','https://i.imgur.com/ECacSct.png','Superman',0,106,103);
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
INSERT INTO `ComicCharacter` VALUES (191,0,'Batman','Gotham','One of the most famous comic characters in    all comic books. Created by Bob Kane and Bill Finger for DC Comics.    He is one of the three members of DC Trinity.Due to his parents\' murdered, he    fights crime using amazing items built by him.','Detective Comics 27, 1939','https://i.imgur.com/P5cAGr3.jpg','Bruce Wayne',103),(192,0,'Ironman','New york','Tony stark is a billionaire playboy who    lives the life the way he wants. But he also is an engineering genius    capable of build amazing war armors. He fights the crime using this armors.','Tales of Suspense 39, 1963','https://i.imgur.com/MPjwaEP.jpg','Tony Stark',104),(193,0,'Captain America','Brooklyn, New york','Captain America is an american symbol who    represents the american way. He is a very patriotic hero who fights crime    for his country.','Captain America Comics 1, 1941 ','https://i.imgur.com/ewUYnCq.jpg','Steve Rogers',104),(194,0,'Rorschach','New york','An old violent hero who works against the    rules set by the government.','Watchmen, 1984 ','https://i.imgur.com/Uk0om3X.jpg','Walter Kovacs',103),(195,0,'Daredevil','Hell\'s kitchen, New york','Matt Murdock is a blind person who jobs as    a lawyer by day but, when the night comes, he becomes a crime fighter known as   Daredevil.','Daredevil 1, 1964','https://i.imgur.com/qQxuHsd.jpg','Matt Murdock',104),(196,0,'Spiderman','Queens, New york','Peter Parker is a student who was bitten    by a radioactive spider.He got powers related to spiders due to this bite.    Now, as Spiderman, he uses these powers to fight crime.','Amazing fantasy 15, 1962','https://i.imgur.com/gZZsssV.jpg','Peter Parker',104);
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
INSERT INTO `ComicCharacter_otherAliases` VALUES (191,'The dark knight'),(191,'Gotham\'s bat');
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
INSERT INTO `ComicComicCharacter` VALUES (199,0,'Main character',123,191),(200,0,'Main character',124,192),(201,0,'Main character',124,193),(202,0,'Main character',125,194),(203,0,'Main character',126,195),(204,0,'Supporting character',124,196);
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
INSERT INTO `Comic_tags` VALUES (128,'strong'),(128,'fast'),(128,'american');
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
INSERT INTO `Comment` VALUES (161,0,'2017-07-16 00:00:00','Great comic.',NULL,123,NULL,NULL,NULL,115,NULL),(162,1,'2017-07-18 00:00:00','Awesome character.',NULL,NULL,191,NULL,NULL,115,NULL),(163,0,'2017-07-20 00:00:00','A great publisher, but their movies are so bad.',NULL,NULL,NULL,103,NULL,117,NULL),(164,0,'2017-07-16 00:00:00','The first comic i read. I still love it.',NULL,124,NULL,NULL,NULL,115,NULL),(165,0,'2017-07-16 00:00:00','They have the best movies. Keep it on!.',NULL,NULL,NULL,104,NULL,117,NULL),(166,1,'2017-07-16 00:00:00','Ready to read it.',NULL,NULL,NULL,NULL,NULL,117,177),(167,0,'2017-07-23 00:00:00','Great comic, awful tv show.',NULL,127,NULL,NULL,NULL,115,NULL),(168,0,'2017-07-16 00:00:00','The greatest ever.',108,NULL,NULL,NULL,NULL,117,NULL);
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
INSERT INTO `DirectMessage` VALUES (148,0,'','I have just read Watchmen. What a gem.','1990-01-01 00:00:00','\0','Watchmen read.',138,117,NULL,115),(149,0,'','I have just read Watchmen. What a gem.','1990-01-01 00:00:00','\0','Watchmen read.',136,117,NULL,115),(150,0,'','I have just read Watchmen. What a gem.','1990-01-01 00:00:00','\0','Watchmen read.',139,117,NULL,115),(151,0,'','I have just started reading Flashpoint.    So much fun so far!.','1990-01-01 00:00:00','\0','Flashpoint started.',140,115,NULL,117),(152,0,'','I have just started reading Flashpoint.    So much fun so far!.','1990-01-01 00:00:00','\0','Flashpoint started.',135,115,NULL,117),(153,0,'\0','Hi there! I don\'t have any comics to    read now. Any recommendations?.','1990-01-01 00:00:00','\0','Any recommendations?.',136,117,NULL,115),(154,0,'\0','Hi there! I don\'t have any comics to    read now. Any recommendations?.','1990-01-01 00:00:00','\0','Any recommendations?.',139,117,NULL,115),(155,0,'','Hey you, i am thinking about buying V for Vendetta or From Hell,    but i can\'t make a decision. Which one do you think i should take?.','1990-01-01 00:00:00','\0','Help here.',140,115,NULL,117),(156,0,'','Hey you, i am thinking about buying V for Vendetta or From Hell,    but i can\'t make a decision. Which one do you think i should take?.','1990-01-01 00:00:00','\0','Help here.',135,115,NULL,117),(157,0,'\0','I\'ve just read From Hell. So so good,    far better than the movie, obviously.','1990-01-01 00:00:00','\0','From hell read.',146,115,NULL,121),(158,0,'\0','I\'ve just read From Hell. So so good,    far better than the movie, obviously.','1990-01-01 00:00:00','\0','From hell read.',135,115,NULL,121),(159,0,'','Hi there!. If you are new at reading comic books   here you have somebody to tell you some good stuff!.','1990-01-01 00:00:00','\0','Here you have a friend!.',136,121,NULL,115),(160,0,'','Hi there!. If you are new at reading comic books   here you have somebody to tell you some good stuff!.','1990-01-01 00:00:00','\0','Here you have a friend!.',145,121,NULL,115),(187,1,'\0','Nice keychain','2018-09-10 18:14:01','','About Watchmen keychain',140,115,198,117),(188,1,'\0','Nice keychain','2018-09-10 18:14:01','','About Watchmen keychain',135,115,198,117),(189,1,'\0','Let\'s meet at the coffee shop and I\'ll buy it','2018-09-10 18:14:32','','About Watchmen keychain',140,115,198,117),(190,1,'\0','Let\'s meet at the coffee shop and I\'ll buy it','2018-09-10 18:14:32','','About Watchmen keychain',135,115,198,117);
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
INSERT INTO `MessageFolder` VALUES (129,0,NULL,'SYSTEM_INBOX',111),(130,0,NULL,'SYSTEM_SENT',111),(131,0,NULL,'SYSTEM_TRASH',111),(132,0,NULL,'SYSTEM_INBOX',113),(133,0,NULL,'SYSTEM_SENT',113),(134,0,NULL,'SYSTEM_TRASH',113),(135,0,NULL,'SYSTEM_INBOX',115),(136,0,NULL,'SYSTEM_SENT',115),(137,0,NULL,'SYSTEM_TRASH',115),(138,0,'My custom folder','USER',115),(139,0,NULL,'SYSTEM_INBOX',117),(140,0,NULL,'SYSTEM_SENT',117),(141,0,NULL,'SYSTEM_TRASH',117),(142,0,NULL,'SYSTEM_INBOX',119),(143,0,NULL,'SYSTEM_SENT',119),(144,0,NULL,'SYSTEM_TRASH',119),(145,0,NULL,'SYSTEM_INBOX',121),(146,0,NULL,'SYSTEM_SENT',121),(147,0,NULL,'SYSTEM_TRASH',121);
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
INSERT INTO `Publisher` VALUES (103,0,'Publisher founded in 1934 by Malcolm Wheeler-Nicholson.','1939-01-01','https://i.imgur.com/vbUEyd1.png','DC Comics'),(104,0,'Publisher founded in 1939 as Timely    Publications by Martin Goodman. Refounded in 1960 as Marvel Comics by    Stan Lee and Jack Kirby.','1934-01-01','https://i.imgur.com/LB4uf8o.jpg','Marvel Comics'),(105,0,'Publisher founded in 1992 by famous   authors who left Marvel Comics like Todd McFarlane, Jim Lee and Rob Liefeld.','1992-01-01','https://i.imgur.com/97GOBxa.png','Image Comics');
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
INSERT INTO `Sale` VALUES (197,0,'2018-09-10 18:10:12','The Flashpoint comic volume 1. Good condition.','Flashpoint comic volume 1',100,'SELLING',123,115,NULL),(198,0,'2018-09-10 18:13:14','Limited edition','Watchmen keychain',20,'COMPLETED',125,115,117);
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
INSERT INTO `Sale_User` VALUES (197,117),(198,117);
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
INSERT INTO `User` VALUES (115,0,'2018-07-15 00:00:00','This is user1\'s description.','User 1',116,NULL,'\0','2018-09-10 18:10:08','B','\0',''),(117,0,'2018-07-15 00:00:00','This is user2\'s description.','User 2',118,NULL,'\0','2018-09-10 18:13:38','C','\0','\0'),(119,0,'2018-07-20 00:00:00','This is user3\'s description.','User 3',120,'Bad behavior.','','2018-07-20 00:00:00','C','\0','\0'),(121,0,'2018-07-22 00:00:00','This is user4\'s description.','User 4',122,NULL,'\0','2018-07-22 00:00:00','B','','\0');
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
INSERT INTO `UserAccount` VALUES (112,0,'21232f297a57a5a743894a0e4a801fc3','admin'),(114,0,'c84258e9c39059a89ab77d846ddab909','admin2'),(116,0,'24c9e15e52afc47c225b757e7bee1f9d','user1'),(118,0,'7e58d63b60197ceb55a1c487989a3720','user2'),(120,0,'92877af70a45fd6a2ed7fe81e1236b78','user3'),(122,0,'3f02ebe3d7929b091e3d8ccfde2f3bc6','user4');
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
INSERT INTO `UserAccount_authorities` VALUES (112,'ADMINISTRATOR'),(114,'ADMINISTRATOR'),(116,'USER'),(118,'USER'),(120,'USER'),(122,'USER');
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
INSERT INTO `UserComic` VALUES (169,0,3,9,'','COMPLETED',123,115),(170,0,0,9,'','COMPLETED',124,117),(171,0,0,10,'','COMPLETED',125,115),(172,0,0,9,'','COMPLETED',125,117),(173,0,0,NULL,'\0','PLANNING_TO_READ',126,115),(174,0,0,NULL,'\0','PLANNING_TO_READ',126,121),(175,0,0,NULL,'\0','PLANNING_TO_READ',123,121),(176,0,0,NULL,'\0','PLANNING_TO_READ',127,115),(183,0,0,NULL,'','NONE',128,115);
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
INSERT INTO `UserComic_Volume` VALUES (169,177),(169,178),(169,179);
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
INSERT INTO `User_User` VALUES (115,115),(115,117),(115,121),(117,115),(121,115);
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
INSERT INTO `Volume` VALUES (177,0,3,'Flash has travelled to the past    to save his mother, but his actions have turned the reality into a new one.','https://i.imgur.com/qHJkDHm.jpg','Beginning',1,'2011-01-25',106,123),(178,0,3,'Wonder Woman and Aquaman are enemies in this reality.    This war is destroying the whole world and Flash,    helped by Thomas Wayne, has to fix the reality before it\'s too late.','https://i.imgur.com/gQ3OPWn.jpg','The civil war',2,'2011-04-25',106,123),(179,0,3,'The reality is about to collapse. Zoom, Flash\'s main enemy, is here to kill    Flash once and for all. The final battle is set to happen.','https://i.imgur.com/1UJjGvd.jpg','Back to our world.',3,'2011-07-25',106,123),(180,0,2,'Two groups of superheroes are formed    due to government issues. Captain America and Ironman are the leaders of   each one.','https://i.imgur.com/RmMBKuo.jpg','The split',1,'2007-04-01',107,124),(181,0,4,'Captain America\'s group is against the    government, so they need to hide. Ironman\'s group is looking after them   to catch them.','https://i.imgur.com/iKMf8Mj.jpg','The chasing',2,'2007-08-01',107,124),(182,0,2,'The two groups collide in a epic battle   in the middle of New York.','https://i.imgur.com/BsYJ3IL.jpg','The final battle',1,'2007-12-01',107,124),(184,0,NULL,NULL,NULL,'The World of Flashpoint Featuring The Flash',5,'2012-03-01',106,123),(185,0,NULL,NULL,NULL,'The World of Flashpoint Featuring Wonder Woman',6,'2012-03-01',106,123),(186,0,NULL,NULL,NULL,'The World of Flashpoint Featuring Superman',7,'2012-03-01',106,123);
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
INSERT INTO `hibernate_sequences` VALUES ('DomainEntity',2);
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

-- Dump completed on 2018-09-13  1:45:43
