-- MySQL dump 10.13  Distrib 5.1.59, for Win32 (ia32)
--
-- Host: localhost    Database: kitanda_db
-- ------------------------------------------------------
-- Server version	5.1.59-community

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
-- Current Database: `kitanda_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `kitanda_db` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `kitanda_db`;

--
-- Table structure for table `accesso_armazem`
--

DROP TABLE IF EXISTS `accesso_armazem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accesso_armazem` (
  `pk_accesso_armazem` int(11) NOT NULL AUTO_INCREMENT,
  `fk_usuario` int(11) NOT NULL,
  `fk_armazem` int(10) unsigned NOT NULL,
  `data_time` datetime DEFAULT NULL,
  PRIMARY KEY (`pk_accesso_armazem`),
  KEY `fk_accesso_armazem_tb_usuario1_idx` (`fk_usuario`),
  KEY `fk_accesso_armazem_tb_armazem1_idx` (`fk_armazem`),
  CONSTRAINT `fk_accesso_armazem_tb_armazem1` FOREIGN KEY (`fk_armazem`) REFERENCES `tb_armazem` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_accesso_armazem_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accesso_armazem`
--

LOCK TABLES `accesso_armazem` WRITE;
/*!40000 ALTER TABLE `accesso_armazem` DISABLE KEYS */;
INSERT INTO `accesso_armazem` VALUES (23,15,2,'2025-07-22 10:11:47');
/*!40000 ALTER TABLE `accesso_armazem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acerto_stock`
--

DROP TABLE IF EXISTS `acerto_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acerto_stock` (
  `pk_acerto_stock` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_hora` datetime NOT NULL,
  `cod_produto` int(10) unsigned NOT NULL,
  `designacao_produto` varchar(150) NOT NULL,
  `cod_usuario` int(10) unsigned NOT NULL,
  `nome_usuario` varchar(100) NOT NULL,
  `qtd_anterior` double NOT NULL,
  `qtd_acerto` double NOT NULL,
  `qtd_depois` double NOT NULL,
  `cod_armazem` int(10) unsigned NOT NULL,
  `designcao_armazem` varchar(100) NOT NULL,
  `motivo_acerto` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`pk_acerto_stock`)
) ENGINE=InnoDB AUTO_INCREMENT=319 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acerto_stock`
--

LOCK TABLES `acerto_stock` WRITE;
/*!40000 ALTER TABLE `acerto_stock` DISABLE KEYS */;
INSERT INTO `acerto_stock` VALUES (1,'2025-09-07 02:25:05',1,'ADIANTAMENTO DE CLIENTE',15,'',1,50,51,2,'Loja',NULL),(2,'2025-09-07 02:25:05',2,'ALCINO 14T 105L',15,'',10,0,10,2,'Loja',NULL),(3,'2025-09-07 02:25:05',3,'APARAFUSADOR C/CARREGADOR',15,'',10,0,10,2,'Loja',NULL),(4,'2025-09-07 02:25:05',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',0,0,0,2,'Loja',NULL),(5,'2025-09-07 02:25:05',5,'ARAME GALVANIZADO 2.7MMX1OKG',15,'',0,0,0,2,'Loja',NULL),(6,'2025-09-07 02:25:05',6,'ARAME LAMINADO 22X7.8KG',15,'',0,0,0,2,'Loja',NULL),(7,'2025-09-07 02:25:05',7,'ARAME QEHMADO 1.1MM 15-KG-LOCAL',15,'',0,0,0,2,'Loja',NULL),(8,'2025-09-07 02:25:05',8,'ARAME QEHMADO 1.1MM-20 KG',15,'',0,0,0,2,'Loja',NULL),(9,'2025-09-07 02:25:05',9,'ARAME QEHMADO 1.5MM-18 KG',15,'',0,0,0,2,'Loja',NULL),(10,'2025-09-07 02:25:05',10,'ARAME QEMADO 1.1MM-18 KG',15,'',0,0,0,2,'Loja',NULL),(11,'2025-09-07 02:25:05',11,'ARMASAO 15 PCS',15,'',0,0,0,2,'Loja',NULL),(12,'2025-09-07 02:25:05',12,'ARMASAO NO-1',15,'',0,0,0,2,'Loja',NULL),(13,'2025-09-07 02:25:05',13,'AUTO CLISMO',15,'',0,0,0,2,'Loja',NULL),(14,'2025-09-07 02:25:05',14,'AZULEIJO 30X45-35000',15,'',0,0,0,2,'Loja',NULL),(15,'2025-09-07 02:25:05',15,'AZULEIJO 30X45-35001',15,'',0,0,0,2,'Loja',NULL),(16,'2025-09-07 02:25:05',16,'AZULEIJO 30X45-35003',15,'',0,0,0,2,'Loja',NULL),(17,'2025-09-07 02:25:05',17,'AZULEIJO 30X45-35004',15,'',0,0,0,2,'Loja',NULL),(18,'2025-09-07 02:25:05',18,'AZULEIJO 30X60-36000',15,'',0,0,0,2,'Loja',NULL),(19,'2025-09-07 02:25:05',19,'AZULEIJO 30X60-36002',15,'',0,0,0,2,'Loja',NULL),(20,'2025-09-07 02:25:05',20,'BALANCA 20KG',15,'',0,0,0,2,'Loja',NULL),(21,'2025-09-07 02:25:05',21,'BALANCA 30-KG',15,'',0,0,0,2,'Loja',NULL),(22,'2025-09-07 02:25:05',22,'BALAO DE E-BOMBA-AUTO',15,'',0,0,0,2,'Loja',NULL),(23,'2025-09-07 02:25:05',23,'BALAO DE E-BOMBA-NOR',15,'',0,0,0,2,'Loja',NULL),(24,'2025-09-07 02:25:05',24,'BALDE PRETO 10 LITRO',15,'',0,0,0,2,'Loja',NULL),(25,'2025-09-07 02:25:05',25,'BALDE PRETO 10-LITRO-2ND',15,'',0,0,0,2,'Loja',NULL),(26,'2025-09-07 02:25:05',26,'BERBEQUIN DE INPACTO 680W',15,'',0,0,0,2,'Loja',NULL),(27,'2025-09-07 02:32:19',1,'ADIANTAMENTO DE CLIENTE',15,'',51,1,52,2,'Loja',NULL),(28,'2025-09-07 02:32:49',2,'ALCINO 14T 105L',15,'',10,3,13,2,'Loja',NULL),(29,'2025-09-07 02:32:52',3,'APARAFUSADOR C/CARREGADOR',15,'',10,5,15,2,'Loja',NULL),(30,'2025-09-07 02:32:59',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',0,22,22,2,'Loja',NULL),(31,'2025-09-07 02:57:37',1,'ADIANTAMENTO DE CLIENTE',15,'',52,1,53,2,'Loja',NULL),(32,'2025-09-07 02:57:43',2,'ALCINO 14T 105L',15,'',13,2,15,2,'Loja',NULL),(33,'2025-09-07 02:58:06',1,'ADIANTAMENTO DE CLIENTE',15,'',53,1,54,2,'Loja',NULL),(34,'2025-09-07 03:04:02',1,'ADIANTAMENTO DE CLIENTE',15,'',54,1,55,2,'Loja',NULL),(35,'2025-09-07 03:04:08',2,'ALCINO 14T 105L',15,'',15,2,17,2,'Loja',NULL),(36,'2025-09-07 03:04:46',1,'ADIANTAMENTO DE CLIENTE',15,'',55,1,56,2,'Loja',NULL),(37,'2025-09-07 03:05:19',1,'ADIANTAMENTO DE CLIENTE',15,'',56,1,57,2,'Loja',NULL),(38,'2025-09-07 03:05:26',2,'ALCINO 14T 105L',15,'',17,11,28,2,'Loja',NULL),(39,'2025-09-07 03:05:53',1,'ADIANTAMENTO DE CLIENTE',15,'',57,1,58,2,'Loja',NULL),(40,'2025-09-07 03:05:53',2,'ALCINO 14T 105L',15,'',28,1,29,2,'Loja',NULL),(41,'2025-09-07 03:05:53',3,'APARAFUSADOR C/CARREGADOR',15,'',15,1,16,2,'Loja',NULL),(42,'2025-09-07 03:05:53',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',22,1,23,2,'Loja',NULL),(43,'2025-09-07 03:05:53',5,'ARAME GALVANIZADO 2.7MMX1OKG',15,'',0,1,1,2,'Loja',NULL),(44,'2025-09-07 03:05:53',6,'ARAME LAMINADO 22X7.8KG',15,'',0,1,1,2,'Loja',NULL),(45,'2025-09-07 03:05:53',7,'ARAME QEHMADO 1.1MM 15-KG-LOCAL',15,'',0,1,1,2,'Loja',NULL),(46,'2025-09-07 03:05:53',8,'ARAME QEHMADO 1.1MM-20 KG',15,'',0,1,1,2,'Loja',NULL),(47,'2025-09-07 03:05:53',9,'ARAME QEHMADO 1.5MM-18 KG',15,'',0,1,1,2,'Loja',NULL),(48,'2025-09-07 03:05:53',10,'ARAME QEMADO 1.1MM-18 KG',15,'',0,1,1,2,'Loja',NULL),(49,'2025-09-07 03:05:53',11,'ARMASAO 15 PCS',15,'',0,1,1,2,'Loja',NULL),(50,'2025-09-07 03:05:53',12,'ARMASAO NO-1',15,'',0,1,1,2,'Loja',NULL),(51,'2025-09-07 03:05:53',13,'AUTO CLISMO',15,'',0,1,1,2,'Loja',NULL),(52,'2025-09-07 03:05:53',14,'AZULEIJO 30X45-35000',15,'',0,1,1,2,'Loja',NULL),(53,'2025-09-07 03:05:53',15,'AZULEIJO 30X45-35001',15,'',0,1,1,2,'Loja',NULL),(54,'2025-09-07 03:05:53',16,'AZULEIJO 30X45-35003',15,'',0,1,1,2,'Loja',NULL),(55,'2025-09-07 03:05:53',17,'AZULEIJO 30X45-35004',15,'',0,1,1,2,'Loja',NULL),(56,'2025-09-07 03:05:53',18,'AZULEIJO 30X60-36000',15,'',0,1,1,2,'Loja',NULL),(57,'2025-09-07 03:05:53',19,'AZULEIJO 30X60-36002',15,'',0,1,1,2,'Loja',NULL),(58,'2025-09-07 03:05:53',20,'BALANCA 20KG',15,'',0,1,1,2,'Loja',NULL),(59,'2025-09-07 03:05:53',21,'BALANCA 30-KG',15,'',0,1,1,2,'Loja',NULL),(60,'2025-09-07 03:05:53',22,'BALAO DE E-BOMBA-AUTO',15,'',0,1,1,2,'Loja',NULL),(61,'2025-09-07 03:05:53',23,'BALAO DE E-BOMBA-NOR',15,'',0,1,1,2,'Loja',NULL),(62,'2025-09-07 03:05:53',24,'BALDE PRETO 10 LITRO',15,'',0,1,1,2,'Loja',NULL),(63,'2025-09-07 03:05:53',25,'BALDE PRETO 10-LITRO-2ND',15,'',0,1,1,2,'Loja',NULL),(64,'2025-09-07 03:05:53',26,'BERBEQUIN DE INPACTO 680W',15,'',0,1,1,2,'Loja',NULL),(65,'2025-09-07 03:05:53',27,'BERBEQUIN NORMAL 650W',15,'',0,1,1,2,'Loja',NULL),(66,'2025-09-07 03:05:53',28,'BOTA DE BORACHA 41',15,'',0,1,1,2,'Loja',NULL),(67,'2025-09-07 03:06:19',1,'ADIANTAMENTO DE CLIENTE',15,'',58,1,59,2,'Loja',NULL),(68,'2025-09-07 03:06:19',2,'ALCINO 14T 105L',15,'',29,1,30,2,'Loja',NULL),(69,'2025-09-07 03:06:19',3,'APARAFUSADOR C/CARREGADOR',15,'',16,1,17,2,'Loja',NULL),(70,'2025-09-07 03:06:19',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',23,1,24,2,'Loja',NULL),(71,'2025-09-07 03:06:19',5,'ARAME GALVANIZADO 2.7MMX1OKG',15,'',1,1,2,2,'Loja',NULL),(72,'2025-09-07 03:06:19',6,'ARAME LAMINADO 22X7.8KG',15,'',1,1,2,2,'Loja',NULL),(73,'2025-09-07 03:06:19',7,'ARAME QEHMADO 1.1MM 15-KG-LOCAL',15,'',1,1,2,2,'Loja',NULL),(74,'2025-09-07 03:06:20',8,'ARAME QEHMADO 1.1MM-20 KG',15,'',1,1,2,2,'Loja',NULL),(75,'2025-09-07 03:06:20',9,'ARAME QEHMADO 1.5MM-18 KG',15,'',1,1,2,2,'Loja',NULL),(76,'2025-09-07 03:06:20',10,'ARAME QEMADO 1.1MM-18 KG',15,'',1,1,2,2,'Loja',NULL),(77,'2025-09-07 03:06:20',11,'ARMASAO 15 PCS',15,'',1,1,2,2,'Loja',NULL),(78,'2025-09-07 03:06:20',12,'ARMASAO NO-1',15,'',1,1,2,2,'Loja',NULL),(79,'2025-09-07 03:06:20',13,'AUTO CLISMO',15,'',1,1,2,2,'Loja',NULL),(80,'2025-09-07 03:06:20',14,'AZULEIJO 30X45-35000',15,'',1,1,2,2,'Loja',NULL),(81,'2025-09-07 03:06:20',15,'AZULEIJO 30X45-35001',15,'',1,1,2,2,'Loja',NULL),(82,'2025-09-07 03:06:20',16,'AZULEIJO 30X45-35003',15,'',1,1,2,2,'Loja',NULL),(83,'2025-09-07 03:06:20',17,'AZULEIJO 30X45-35004',15,'',1,1,2,2,'Loja',NULL),(84,'2025-09-07 03:06:20',18,'AZULEIJO 30X60-36000',15,'',1,1,2,2,'Loja',NULL),(85,'2025-09-07 03:06:20',19,'AZULEIJO 30X60-36002',15,'',1,1,2,2,'Loja',NULL),(86,'2025-09-07 03:06:20',20,'BALANCA 20KG',15,'',1,1,2,2,'Loja',NULL),(87,'2025-09-07 03:06:20',21,'BALANCA 30-KG',15,'',1,1,2,2,'Loja',NULL),(88,'2025-09-07 03:06:20',22,'BALAO DE E-BOMBA-AUTO',15,'',1,1,2,2,'Loja',NULL),(89,'2025-09-07 03:06:20',23,'BALAO DE E-BOMBA-NOR',15,'',1,1,2,2,'Loja',NULL),(90,'2025-09-07 03:06:20',24,'BALDE PRETO 10 LITRO',15,'',1,1,2,2,'Loja',NULL),(91,'2025-09-07 03:06:20',25,'BALDE PRETO 10-LITRO-2ND',15,'',1,1,2,2,'Loja',NULL),(92,'2025-09-07 03:06:20',26,'BERBEQUIN DE INPACTO 680W',15,'',1,1,2,2,'Loja',NULL),(93,'2025-09-07 03:06:20',27,'BERBEQUIN NORMAL 650W',15,'',1,1,2,2,'Loja',NULL),(94,'2025-09-07 03:06:20',28,'BOTA DE BORACHA 41',15,'',1,1,2,2,'Loja',NULL),(95,'2025-09-07 03:06:39',1,'ADIANTAMENTO DE CLIENTE',15,'',59,1,60,2,'Loja',NULL),(96,'2025-09-07 03:07:05',1,'ADIANTAMENTO DE CLIENTE',15,'',60,1,61,2,'Loja',NULL),(97,'2025-09-07 03:07:30',1,'ADIANTAMENTO DE CLIENTE',15,'',61,1,62,2,'Loja',NULL),(98,'2025-09-07 03:10:33',1,'ADIANTAMENTO DE CLIENTE',15,'',62,1,63,2,'Loja',NULL),(99,'2025-09-07 03:19:08',1,'ADIANTAMENTO DE CLIENTE',15,'',63,1,64,2,'Loja',NULL),(100,'2025-09-07 03:19:08',2,'ALCINO 14T 105L',15,'',30,1,31,2,'Loja',NULL),(101,'2025-09-07 03:19:08',3,'APARAFUSADOR C/CARREGADOR',15,'',17,1,18,2,'Loja',NULL),(102,'2025-09-07 03:19:08',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',24,1,25,2,'Loja',NULL),(103,'2025-09-07 03:19:08',5,'ARAME GALVANIZADO 2.7MMX1OKG',15,'',2,1,3,2,'Loja',NULL),(104,'2025-09-07 03:19:08',6,'ARAME LAMINADO 22X7.8KG',15,'',2,1,3,2,'Loja',NULL),(105,'2025-09-07 03:19:08',7,'ARAME QEHMADO 1.1MM 15-KG-LOCAL',15,'',2,1,3,2,'Loja',NULL),(106,'2025-09-07 03:19:08',8,'ARAME QEHMADO 1.1MM-20 KG',15,'',2,1,3,2,'Loja',NULL),(107,'2025-09-07 03:19:08',9,'ARAME QEHMADO 1.5MM-18 KG',15,'',2,1,3,2,'Loja',NULL),(108,'2025-09-07 03:19:08',10,'ARAME QEMADO 1.1MM-18 KG',15,'',2,1,3,2,'Loja',NULL),(109,'2025-09-07 03:19:08',11,'ARMASAO 15 PCS',15,'',2,1,3,2,'Loja',NULL),(110,'2025-09-07 03:19:08',12,'ARMASAO NO-1',15,'',2,1,3,2,'Loja',NULL),(111,'2025-09-07 03:19:08',13,'AUTO CLISMO',15,'',2,1,3,2,'Loja',NULL),(112,'2025-09-07 03:19:08',14,'AZULEIJO 30X45-35000',15,'',2,1,3,2,'Loja',NULL),(113,'2025-09-07 03:19:08',15,'AZULEIJO 30X45-35001',15,'',2,1,3,2,'Loja',NULL),(114,'2025-09-07 03:19:08',16,'AZULEIJO 30X45-35003',15,'',2,1,3,2,'Loja',NULL),(115,'2025-09-07 03:19:08',17,'AZULEIJO 30X45-35004',15,'',2,1,3,2,'Loja',NULL),(116,'2025-09-07 03:19:08',18,'AZULEIJO 30X60-36000',15,'',2,1,3,2,'Loja',NULL),(117,'2025-09-07 03:19:08',19,'AZULEIJO 30X60-36002',15,'',2,1,3,2,'Loja',NULL),(118,'2025-09-07 03:19:08',20,'BALANCA 20KG',15,'',2,1,3,2,'Loja',NULL),(119,'2025-09-07 03:19:08',21,'BALANCA 30-KG',15,'',2,1,3,2,'Loja',NULL),(120,'2025-09-07 03:19:08',22,'BALAO DE E-BOMBA-AUTO',15,'',2,1,3,2,'Loja',NULL),(121,'2025-09-07 03:19:08',23,'BALAO DE E-BOMBA-NOR',15,'',2,1,3,2,'Loja',NULL),(122,'2025-09-07 03:19:08',24,'BALDE PRETO 10 LITRO',15,'',2,1,3,2,'Loja',NULL),(123,'2025-09-07 03:19:08',25,'BALDE PRETO 10-LITRO-2ND',15,'',2,1,3,2,'Loja',NULL),(124,'2025-09-07 03:19:08',26,'BERBEQUIN DE INPACTO 680W',15,'',2,1,3,2,'Loja',NULL),(125,'2025-09-07 03:19:08',27,'BERBEQUIN NORMAL 650W',15,'',2,1,3,2,'Loja',NULL),(126,'2025-09-07 03:19:08',28,'BOTA DE BORACHA 41',15,'',2,1,3,2,'Loja',NULL),(127,'2025-09-07 03:19:08',29,'BOTA DE BORACHA 42',15,'',0,1,1,2,'Loja',NULL),(128,'2025-09-07 03:19:08',30,'BOTA DE CONSTRUCAO-NO-41',15,'',0,1,1,2,'Loja',NULL),(129,'2025-09-07 03:19:08',31,'BOTA DE CONSTRUCAO-NO-42',15,'',0,1,1,2,'Loja',NULL),(130,'2025-09-07 03:19:08',32,'BOTA DE CONSTRUCAO-NO-43',15,'',0,1,1,2,'Loja',NULL),(131,'2025-09-07 03:19:08',33,'BRASADERA',15,'',0,1,1,2,'Loja',NULL),(132,'2025-09-07 03:19:08',34,'BROCA ELECTRICA 500W',15,'',0,1,1,2,'Loja',NULL),(133,'2025-09-07 03:19:08',35,'BROCA IMPACTO',15,'',0,1,1,2,'Loja',NULL),(134,'2025-09-07 03:19:08',36,'BROQUIM 1050W',15,'',0,1,1,2,'Loja',NULL),(135,'2025-09-07 03:19:08',37,'BROQUIM 900W',15,'',0,1,1,2,'Loja',NULL),(136,'2025-09-07 03:19:08',38,'BUCAPOL/LIGHT-TESTER',15,'',0,1,1,2,'Loja',NULL),(137,'2025-09-07 03:19:08',39,'BUCHA 10 X 50MM (40)-PCS',15,'',0,1,1,2,'Loja',NULL),(138,'2025-09-07 03:19:09',40,'BUCHA 8.0 X 40MM (80)-PCS',15,'',0,1,1,2,'Loja',NULL),(139,'2025-09-07 03:19:09',41,'CABO D ELECTRICO 2X16MM-PRETO',15,'',0,1,1,2,'Loja',NULL),(140,'2025-09-07 03:19:09',42,'CABO ELETRICO 2X2.5MM',15,'',0,1,1,2,'Loja',NULL),(141,'2025-09-07 03:19:09',43,'CABO ELETRICO 3X2.5MM',15,'',0,1,1,2,'Loja',NULL),(142,'2025-09-07 03:19:09',44,'CADEADO MEDIO-CHAVE ESTRELA',15,'',0,1,1,2,'Loja',NULL),(143,'2025-09-07 03:19:09',45,'CADEADO-GRANDE',15,'',0,1,1,2,'Loja',NULL),(144,'2025-09-07 03:19:09',46,'CADEADO-MEDIO',15,'',0,1,1,2,'Loja',NULL),(145,'2025-09-07 03:19:09',47,'CADEADO-NORMAL',15,'',0,1,1,2,'Loja',NULL),(146,'2025-09-07 03:19:09',48,'CADEADO-PEQHENO',15,'',0,1,1,2,'Loja',NULL),(147,'2025-09-07 03:19:09',49,'CAMARA SIMPLES',15,'',0,1,1,2,'Loja',NULL),(148,'2025-09-07 03:19:09',50,'CANISSO LISO 16',15,'',0,1,1,2,'Loja',NULL),(149,'2025-09-07 03:19:09',51,'CANISSO LISO 20',15,'',0,1,1,2,'Loja',NULL),(150,'2025-09-07 03:19:09',52,'CANISSO LISO- 16',15,'',0,1,1,2,'Loja',NULL),(151,'2025-09-07 03:19:09',53,'CANISSO-FLIXIBEL- 16',15,'',0,1,1,2,'Loja',NULL),(152,'2025-09-07 03:19:09',54,'CANISSO-FLIXIBEL- 20',15,'',0,1,1,2,'Loja',NULL),(153,'2025-09-07 03:19:09',55,'CANISSO-FLIXIBEL- 32',15,'',0,1,1,2,'Loja',NULL),(154,'2025-09-07 03:19:09',56,'CANISSO-FLIXIBEL-25',15,'',0,1,1,2,'Loja',NULL),(155,'2025-09-07 03:19:09',57,'CANTONERA 300*350MM',15,'',0,1,1,2,'Loja',NULL),(156,'2025-09-07 03:19:09',58,'CANTONERA 300*350MM-CIZA',15,'',0,1,1,2,'Loja',NULL),(157,'2025-09-07 03:19:09',59,'CAPA DE CHUVA',15,'',0,1,1,2,'Loja',NULL),(158,'2025-09-07 03:19:09',60,'CAPASET',15,'',0,1,1,2,'Loja',NULL),(159,'2025-09-07 03:19:09',61,'CARO DE MAO GRANDE',15,'',0,1,1,2,'Loja',NULL),(160,'2025-09-07 03:19:09',62,'CARO DE MAO MID 2ND',15,'',0,1,1,2,'Loja',NULL),(161,'2025-09-07 03:19:09',63,'CATANA 16',15,'',0,1,1,2,'Loja',NULL),(162,'2025-09-07 03:19:09',64,'CIMENTO COLA 20 KG--SUPER',15,'',0,1,1,2,'Loja',NULL),(163,'2025-09-07 03:19:09',65,'CIMENTO COLA 20KG-PER',15,'',0,1,1,2,'Loja',NULL),(164,'2025-09-07 03:19:09',66,'COLA DE MADERA 1.5L WOODFIX',15,'',0,1,1,2,'Loja',NULL),(165,'2025-09-07 03:19:09',67,'COLA DE MADERA BRETX BRANCA 1KG',15,'',0,1,1,2,'Loja',NULL),(166,'2025-09-07 03:19:09',68,'COLA DE MADERA TOP  BRANCA 1KG',15,'',0,1,1,2,'Loja',NULL),(167,'2025-09-07 03:19:09',69,'COLA DE MADERA WOOD FIX-1/2 LITRO',15,'',0,1,1,2,'Loja',NULL),(168,'2025-09-07 03:19:09',70,'COLA DE MADERA WOOD FIX-1LITRO',15,'',0,1,1,2,'Loja',NULL),(169,'2025-09-07 03:19:09',71,'CORANTE 12-PCS',15,'',0,1,1,2,'Loja',NULL),(170,'2025-09-07 03:19:09',72,'CORRANTE  GRANDE',15,'',0,1,1,2,'Loja',NULL),(171,'2025-09-07 03:19:09',73,'CORTADOR DE TUBO PVC',15,'',0,1,1,2,'Loja',NULL),(172,'2025-09-07 03:19:09',74,'CULEHR DE PEDRERO MID',15,'',0,1,1,2,'Loja',NULL),(173,'2025-09-07 03:19:09',75,'CULETE VERMELHO',15,'',0,1,1,2,'Loja',NULL),(174,'2025-09-07 03:19:09',76,'CULHER DE CONSTRUSAO',15,'',0,1,1,2,'Loja',NULL),(175,'2025-09-07 03:19:09',77,'DISCO DE CORT 118* 230MM FINO',15,'',0,1,1,2,'Loja',NULL),(176,'2025-09-07 03:19:09',78,'DISCO DE CORT 180MM JOGADOR',15,'',0,1,1,2,'Loja',NULL),(177,'2025-09-07 03:19:09',79,'DISCO DE CORT 230MM JOGADOR',15,'',0,1,1,2,'Loja',NULL),(178,'2025-09-07 03:19:09',80,'DISCO DE CORT FINO 180MM',15,'',0,1,1,2,'Loja',NULL),(179,'2025-09-07 03:19:09',81,'DISCO DE CORT MADERA115MM',15,'',0,1,1,2,'Loja',NULL),(180,'2025-09-07 03:19:09',82,'DISCO DE CORT MADERA180MM',15,'',0,1,1,2,'Loja',NULL),(181,'2025-09-07 03:19:09',83,'DISCO DE CORT MADERA230MM',15,'',0,1,1,2,'Loja',NULL),(182,'2025-09-07 03:19:09',84,'DISCO DE CORT MOZICO 115MM',15,'',0,1,1,2,'Loja',NULL),(183,'2025-09-07 03:19:09',85,'DISCO DE CORT MOZICO 115MM 2ND',15,'',0,1,1,2,'Loja',NULL),(184,'2025-09-07 03:19:09',86,'DISCO DE CORT MOZICO 180MM',15,'',0,1,1,2,'Loja',NULL),(185,'2025-09-07 03:19:30',1,'ADIANTAMENTO DE CLIENTE',15,'',64,1,65,2,'Loja',NULL),(186,'2025-09-07 03:19:30',2,'ALCINO 14T 105L',15,'',31,1,32,2,'Loja',NULL),(187,'2025-09-07 03:19:30',3,'APARAFUSADOR C/CARREGADOR',15,'',18,1,19,2,'Loja',NULL),(188,'2025-09-07 03:19:30',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',25,1,26,2,'Loja',NULL),(189,'2025-09-07 03:19:30',5,'ARAME GALVANIZADO 2.7MMX1OKG',15,'',3,1,4,2,'Loja',NULL),(190,'2025-09-07 03:19:30',6,'ARAME LAMINADO 22X7.8KG',15,'',3,1,4,2,'Loja',NULL),(191,'2025-09-07 03:19:30',7,'ARAME QEHMADO 1.1MM 15-KG-LOCAL',15,'',3,1,4,2,'Loja',NULL),(192,'2025-09-07 03:19:30',8,'ARAME QEHMADO 1.1MM-20 KG',15,'',3,1,4,2,'Loja',NULL),(193,'2025-09-07 03:19:30',9,'ARAME QEHMADO 1.5MM-18 KG',15,'',3,1,4,2,'Loja',NULL),(194,'2025-09-07 03:19:30',10,'ARAME QEMADO 1.1MM-18 KG',15,'',3,1,4,2,'Loja',NULL),(195,'2025-09-07 03:19:30',11,'ARMASAO 15 PCS',15,'',3,1,4,2,'Loja',NULL),(196,'2025-09-07 03:19:30',12,'ARMASAO NO-1',15,'',3,1,4,2,'Loja',NULL),(197,'2025-09-07 03:19:30',13,'AUTO CLISMO',15,'',3,1,4,2,'Loja',NULL),(198,'2025-09-07 03:19:30',14,'AZULEIJO 30X45-35000',15,'',3,1,4,2,'Loja',NULL),(199,'2025-09-07 03:19:30',15,'AZULEIJO 30X45-35001',15,'',3,1,4,2,'Loja',NULL),(200,'2025-09-07 03:19:30',16,'AZULEIJO 30X45-35003',15,'',3,1,4,2,'Loja',NULL),(201,'2025-09-07 03:19:30',17,'AZULEIJO 30X45-35004',15,'',3,1,4,2,'Loja',NULL),(202,'2025-09-07 03:19:30',18,'AZULEIJO 30X60-36000',15,'',3,1,4,2,'Loja',NULL),(203,'2025-09-07 03:19:30',19,'AZULEIJO 30X60-36002',15,'',3,1,4,2,'Loja',NULL),(204,'2025-09-07 03:19:30',20,'BALANCA 20KG',15,'',3,1,4,2,'Loja',NULL),(205,'2025-09-07 03:19:30',21,'BALANCA 30-KG',15,'',3,1,4,2,'Loja',NULL),(206,'2025-09-07 03:19:30',22,'BALAO DE E-BOMBA-AUTO',15,'',3,1,4,2,'Loja',NULL),(207,'2025-09-07 03:19:30',23,'BALAO DE E-BOMBA-NOR',15,'',3,1,4,2,'Loja',NULL),(208,'2025-09-07 03:19:30',24,'BALDE PRETO 10 LITRO',15,'',3,1,4,2,'Loja',NULL),(209,'2025-09-07 03:19:30',25,'BALDE PRETO 10-LITRO-2ND',15,'',3,1,4,2,'Loja',NULL),(210,'2025-09-07 03:19:30',26,'BERBEQUIN DE INPACTO 680W',15,'',3,1,4,2,'Loja',NULL),(211,'2025-09-07 03:19:30',27,'BERBEQUIN NORMAL 650W',15,'',3,1,4,2,'Loja',NULL),(212,'2025-09-07 03:19:30',28,'BOTA DE BORACHA 41',15,'',3,1,4,2,'Loja',NULL),(213,'2025-09-07 03:19:30',29,'BOTA DE BORACHA 42',15,'',1,1,2,2,'Loja',NULL),(214,'2025-09-07 03:19:30',30,'BOTA DE CONSTRUCAO-NO-41',15,'',1,1,2,2,'Loja',NULL),(215,'2025-09-07 03:19:30',31,'BOTA DE CONSTRUCAO-NO-42',15,'',1,1,2,2,'Loja',NULL),(216,'2025-09-07 03:19:30',32,'BOTA DE CONSTRUCAO-NO-43',15,'',1,1,2,2,'Loja',NULL),(217,'2025-09-07 03:19:30',33,'BRASADERA',15,'',1,1,2,2,'Loja',NULL),(218,'2025-09-07 03:19:30',34,'BROCA ELECTRICA 500W',15,'',1,1,2,2,'Loja',NULL),(219,'2025-09-07 03:19:30',35,'BROCA IMPACTO',15,'',1,1,2,2,'Loja',NULL),(220,'2025-09-07 03:19:30',36,'BROQUIM 1050W',15,'',1,1,2,2,'Loja',NULL),(221,'2025-09-07 03:19:30',37,'BROQUIM 900W',15,'',1,1,2,2,'Loja',NULL),(222,'2025-09-07 03:19:30',38,'BUCAPOL/LIGHT-TESTER',15,'',1,1,2,2,'Loja',NULL),(223,'2025-09-07 03:19:30',39,'BUCHA 10 X 50MM (40)-PCS',15,'',1,1,2,2,'Loja',NULL),(224,'2025-09-07 03:19:30',40,'BUCHA 8.0 X 40MM (80)-PCS',15,'',1,1,2,2,'Loja',NULL),(225,'2025-09-07 03:19:30',41,'CABO D ELECTRICO 2X16MM-PRETO',15,'',1,1,2,2,'Loja',NULL),(226,'2025-09-07 03:19:30',42,'CABO ELETRICO 2X2.5MM',15,'',1,1,2,2,'Loja',NULL),(227,'2025-09-07 03:19:30',43,'CABO ELETRICO 3X2.5MM',15,'',1,1,2,2,'Loja',NULL),(228,'2025-09-07 03:19:30',44,'CADEADO MEDIO-CHAVE ESTRELA',15,'',1,1,2,2,'Loja',NULL),(229,'2025-09-07 03:19:30',45,'CADEADO-GRANDE',15,'',1,1,2,2,'Loja',NULL),(230,'2025-09-07 03:19:30',46,'CADEADO-MEDIO',15,'',1,1,2,2,'Loja',NULL),(231,'2025-09-07 03:19:30',47,'CADEADO-NORMAL',15,'',1,1,2,2,'Loja',NULL),(232,'2025-09-07 03:19:30',48,'CADEADO-PEQHENO',15,'',1,1,2,2,'Loja',NULL),(233,'2025-09-07 03:19:30',49,'CAMARA SIMPLES',15,'',1,1,2,2,'Loja',NULL),(234,'2025-09-07 03:19:30',50,'CANISSO LISO 16',15,'',1,1,2,2,'Loja',NULL),(235,'2025-09-07 03:19:30',51,'CANISSO LISO 20',15,'',1,1,2,2,'Loja',NULL),(236,'2025-09-07 03:19:30',52,'CANISSO LISO- 16',15,'',1,1,2,2,'Loja',NULL),(237,'2025-09-07 03:19:30',53,'CANISSO-FLIXIBEL- 16',15,'',1,1,2,2,'Loja',NULL),(238,'2025-09-07 03:19:30',54,'CANISSO-FLIXIBEL- 20',15,'',1,1,2,2,'Loja',NULL),(239,'2025-09-07 03:19:30',55,'CANISSO-FLIXIBEL- 32',15,'',1,1,2,2,'Loja',NULL),(240,'2025-09-07 03:19:30',56,'CANISSO-FLIXIBEL-25',15,'',1,1,2,2,'Loja',NULL),(241,'2025-09-07 03:19:30',57,'CANTONERA 300*350MM',15,'',1,1,2,2,'Loja',NULL),(242,'2025-09-07 03:19:30',58,'CANTONERA 300*350MM-CIZA',15,'',1,1,2,2,'Loja',NULL),(243,'2025-09-07 03:19:30',59,'CAPA DE CHUVA',15,'',1,1,2,2,'Loja',NULL),(244,'2025-09-07 03:19:30',60,'CAPASET',15,'',1,1,2,2,'Loja',NULL),(245,'2025-09-07 03:19:30',61,'CARO DE MAO GRANDE',15,'',1,1,2,2,'Loja',NULL),(246,'2025-09-07 03:19:30',62,'CARO DE MAO MID 2ND',15,'',1,1,2,2,'Loja',NULL),(247,'2025-09-07 03:19:30',63,'CATANA 16',15,'',1,1,2,2,'Loja',NULL),(248,'2025-09-07 03:19:30',64,'CIMENTO COLA 20 KG--SUPER',15,'',1,1,2,2,'Loja',NULL),(249,'2025-09-07 03:19:30',65,'CIMENTO COLA 20KG-PER',15,'',1,1,2,2,'Loja',NULL),(250,'2025-09-07 03:19:30',66,'COLA DE MADERA 1.5L WOODFIX',15,'',1,1,2,2,'Loja',NULL),(251,'2025-09-07 03:19:30',67,'COLA DE MADERA BRETX BRANCA 1KG',15,'',1,1,2,2,'Loja',NULL),(252,'2025-09-07 03:19:30',68,'COLA DE MADERA TOP  BRANCA 1KG',15,'',1,1,2,2,'Loja',NULL),(253,'2025-09-07 03:19:30',69,'COLA DE MADERA WOOD FIX-1/2 LITRO',15,'',1,1,2,2,'Loja',NULL),(254,'2025-09-07 03:19:30',70,'COLA DE MADERA WOOD FIX-1LITRO',15,'',1,1,2,2,'Loja',NULL),(255,'2025-09-07 03:19:30',71,'CORANTE 12-PCS',15,'',1,1,2,2,'Loja',NULL),(256,'2025-09-07 03:19:30',72,'CORRANTE  GRANDE',15,'',1,1,2,2,'Loja',NULL),(257,'2025-09-07 03:19:30',73,'CORTADOR DE TUBO PVC',15,'',1,1,2,2,'Loja',NULL),(258,'2025-09-07 03:19:30',74,'CULEHR DE PEDRERO MID',15,'',1,1,2,2,'Loja',NULL),(259,'2025-09-07 03:19:30',75,'CULETE VERMELHO',15,'',1,1,2,2,'Loja',NULL),(260,'2025-09-07 03:19:30',76,'CULHER DE CONSTRUSAO',15,'',1,1,2,2,'Loja',NULL),(261,'2025-09-07 03:19:30',77,'DISCO DE CORT 118* 230MM FINO',15,'',1,1,2,2,'Loja',NULL),(262,'2025-09-07 03:19:30',78,'DISCO DE CORT 180MM JOGADOR',15,'',1,1,2,2,'Loja',NULL),(263,'2025-09-07 03:19:30',79,'DISCO DE CORT 230MM JOGADOR',15,'',1,1,2,2,'Loja',NULL),(264,'2025-09-07 03:19:30',80,'DISCO DE CORT FINO 180MM',15,'',1,1,2,2,'Loja',NULL),(265,'2025-09-07 03:19:30',81,'DISCO DE CORT MADERA115MM',15,'',1,1,2,2,'Loja',NULL),(266,'2025-09-07 03:19:30',82,'DISCO DE CORT MADERA180MM',15,'',1,1,2,2,'Loja',NULL),(267,'2025-09-07 03:19:30',83,'DISCO DE CORT MADERA230MM',15,'',1,1,2,2,'Loja',NULL),(268,'2025-09-07 03:19:30',84,'DISCO DE CORT MOZICO 115MM',15,'',1,1,2,2,'Loja',NULL),(269,'2025-09-07 03:19:30',85,'DISCO DE CORT MOZICO 115MM 2ND',15,'',1,1,2,2,'Loja',NULL),(270,'2025-09-07 03:19:30',86,'DISCO DE CORT MOZICO 180MM',15,'',1,1,2,2,'Loja',NULL),(271,'2025-09-07 03:20:17',1,'ADIANTAMENTO DE CLIENTE',15,'',65,1,66,2,'Loja',NULL),(272,'2025-09-07 03:21:22',1,'ADIANTAMENTO DE CLIENTE',15,'',66,1,67,2,'Loja',NULL),(273,'2025-09-07 03:21:29',2,'ALCINO 14T 105L',15,'',32,1,33,2,'Loja',NULL),(274,'2025-09-07 03:21:33',3,'APARAFUSADOR C/CARREGADOR',15,'',19,1,20,2,'Loja',NULL),(275,'2025-09-07 03:22:00',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',26,2,28,2,'Loja',NULL),(276,'2025-09-07 03:22:51',1,'ADIANTAMENTO DE CLIENTE',15,'',67,1,68,2,'Loja',NULL),(277,'2025-09-07 03:23:16',1,'ADIANTAMENTO DE CLIENTE',15,'',68,1,69,2,'Loja',NULL),(278,'2025-09-07 03:23:22',2,'ALCINO 14T 105L',15,'',33,1,34,2,'Loja',NULL),(279,'2025-09-07 03:23:47',1,'ADIANTAMENTO DE CLIENTE',15,'',69,1,70,2,'Loja',NULL),(280,'2025-09-07 03:26:20',1,'ADIANTAMENTO DE CLIENTE',15,'',70,1,71,2,'Loja',NULL),(281,'2025-09-07 03:26:26',2,'ALCINO 14T 105L',15,'',34,2,36,2,'Loja',NULL),(282,'2025-09-07 03:26:33',3,'APARAFUSADOR C/CARREGADOR',15,'',20,1,21,2,'Loja',NULL),(283,'2025-09-07 03:26:49',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',28,2,30,2,'Loja',NULL),(284,'2025-09-07 03:31:20',1,'ADIANTAMENTO DE CLIENTE',64,'yyyy_MM_dd_HH_mm_ss',71,2,73,2,'Loja',NULL),(285,'2025-09-07 12:35:37',1,'ADIANTAMENTO DE CLIENTE',64,'yyyy_MM_dd_HH_mm_ss',73,7,80,2,'Loja',NULL),(286,'2025-09-08 12:28:49',440,'Novo Produto 01',15,'',0,5,5,2,'Loja',NULL),(287,'2025-09-08 13:07:56',440,'Novo Produto 01',15,'',5,7,12,2,'Loja',NULL),(288,'2025-09-08 13:09:15',440,'Novo Produto 01',15,'',12,-3,9,2,'Loja',NULL),(289,'2025-09-10 04:57:36',2,'ALCINO 14T 105L',15,'',10,2,12,1,'Armazem 1',NULL),(290,'2025-09-10 05:00:40',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',30,20,50,1,'Armazem 1',NULL),(291,'2025-09-10 05:01:12',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',30,-20,10,1,'Armazem 1',NULL),(292,'2025-09-10 05:01:21',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',30,-20,10,1,'Armazem 1',NULL),(293,'2025-09-10 05:01:27',2,'ALCINO 14T 105L',15,'',12,-6,6,1,'Armazem 1',NULL),(294,'2025-09-10 05:01:47',440,'Novo Produto 01',15,'',0,5,5,1,'Armazem 1',NULL),(295,'2025-09-10 05:03:12',2,'ALCINO 14T 105L',15,'',6,4,10,1,'Armazem 1',NULL),(296,'2025-09-10 05:03:44',2,'ALCINO 14T 105L',15,'',6,2,8,1,'Armazem 1',NULL),(297,'2025-09-10 07:51:10',3,'APARAFUSADOR C/CARREGADOR',15,'',11,-1,10,2,'Loja',NULL),(298,'2025-09-10 07:52:05',3,'APARAFUSADOR C/CARREGADOR',15,'',10,-5,5,2,'Loja',NULL),(299,'2025-09-10 07:55:26',3,'APARAFUSADOR C/CARREGADOR',15,'',5,-2,3,2,'Loja',NULL),(300,'2025-09-10 07:55:54',3,'APARAFUSADOR C/CARREGADOR',15,'',5,10,15,2,'Loja',NULL),(301,'2025-09-10 07:56:24',3,'APARAFUSADOR C/CARREGADOR',15,'',15,-5,10,2,'Loja',NULL),(302,'2025-09-10 07:56:57',3,'APARAFUSADOR C/CARREGADOR',15,'',10,4,14,2,'Loja',NULL),(303,'2025-09-10 07:57:12',3,'APARAFUSADOR C/CARREGADOR',15,'',10,4,14,2,'Loja',NULL),(304,'2025-09-10 07:57:28',3,'APARAFUSADOR C/CARREGADOR',15,'',14,4,18,2,'Loja',NULL),(305,'2025-09-10 07:57:30',3,'APARAFUSADOR C/CARREGADOR',15,'',14,4,18,2,'Loja',NULL),(306,'2025-09-10 07:58:15',4,'APARAFUSADOR C/CARREGADOR/TID',15,'',29,-9,20,2,'Loja',NULL),(307,'2025-09-10 16:38:09',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',8,4,12,1,'Armazem 1',NULL),(308,'2025-09-10 16:38:15',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',8,4,12,1,'Armazem 1',NULL),(309,'2025-09-10 16:38:24',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',8,4,12,1,'Armazem 1',NULL),(310,'2025-09-10 16:38:29',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',8,4,12,1,'Armazem 1',NULL),(311,'2025-09-10 16:38:45',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',36,4,40,2,'Loja',NULL),(312,'2025-09-10 16:38:49',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',36,6,42,2,'Loja',NULL),(313,'2025-09-10 16:38:53',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',36,6,42,2,'Loja',NULL),(314,'2025-09-10 16:38:58',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',36,6,42,2,'Loja',NULL),(315,'2025-09-10 16:40:27',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',12,6,18,1,'Armazem 1',NULL),(316,'2025-09-10 16:41:52',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',18,2,20,1,'Armazem 1',NULL),(317,'2025-09-10 16:41:57',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',18,4,22,1,'Armazem 1',NULL),(318,'2025-09-10 16:42:33',2,'ALCINO 14T 105L',64,'yyyy_MM_dd_HH_mm_ss',22,2,24,1,'Armazem 1',NULL);
/*!40000 ALTER TABLE `acerto_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agregado_familiar`
--

DROP TABLE IF EXISTS `agregado_familiar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agregado_familiar` (
  `pk_agregado_familiar` int(11) NOT NULL AUTO_INCREMENT,
  `nome_filho` varchar(100) DEFAULT NULL,
  `data_nascimento` date DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `fk_funcionario` int(11) NOT NULL,
  PRIMARY KEY (`pk_agregado_familiar`),
  KEY `fk_agregado_familiar_tb_funcionario1_idx` (`fk_funcionario`),
  CONSTRAINT `fk_agregado_familiar_tb_funcionario1` FOREIGN KEY (`fk_funcionario`) REFERENCES `tb_funcionario` (`idFuncionario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agregado_familiar`
--

LOCK TABLES `agregado_familiar` WRITE;
/*!40000 ALTER TABLE `agregado_familiar` DISABLE KEYS */;
/*!40000 ALTER TABLE `agregado_familiar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `amortizacao`
--

DROP TABLE IF EXISTS `amortizacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `amortizacao` (
  `pk_amortizacao` int(11) NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `fk_venda` int(11) NOT NULL,
  `fk_usuario` int(11) NOT NULL,
  `cod_fact` varchar(100) DEFAULT NULL,
  `ref_cod_fact` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pk_amortizacao`),
  KEY `fk_vendas_idx` (`fk_venda`),
  KEY `fk_usuarios_idx` (`fk_usuario`),
  CONSTRAINT `fk_usuarios` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_vendas` FOREIGN KEY (`fk_venda`) REFERENCES `tb_venda` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amortizacao`
--

LOCK TABLES `amortizacao` WRITE;
/*!40000 ALTER TABLE `amortizacao` DISABLE KEYS */;
/*!40000 ALTER TABLE `amortizacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `amortizacao_divida`
--

DROP TABLE IF EXISTS `amortizacao_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `amortizacao_divida` (
  `pk_amortizacao_divida` int(11) NOT NULL AUTO_INCREMENT,
  `fk_usuario` int(11) DEFAULT NULL,
  `data` datetime DEFAULT NULL,
  `valor_pendente` double DEFAULT NULL,
  `valor_entregue` double DEFAULT NULL,
  `troco` double DEFAULT NULL,
  `obs` varchar(200) DEFAULT NULL,
  `desconto` double DEFAULT NULL,
  `fk_venda` int(11) DEFAULT NULL,
  `ref_cod_fact` varchar(100) DEFAULT NULL,
  `total_venda_fact` double DEFAULT NULL,
  `valor_pago` decimal(30,2) DEFAULT NULL,
  `net_total` decimal(30,2) DEFAULT '0.00',
  `tax` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`pk_amortizacao_divida`),
  KEY `fk_usuario_armotizacoes_idx` (`fk_usuario`),
  KEY `fk_venda_amortizacoes_idx` (`fk_venda`),
  CONSTRAINT `fk_usuario_armotizacoes` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`),
  CONSTRAINT `fk_venda_amortizacoes` FOREIGN KEY (`fk_venda`) REFERENCES `tb_venda` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amortizacao_divida`
--

LOCK TABLES `amortizacao_divida` WRITE;
/*!40000 ALTER TABLE `amortizacao_divida` DISABLE KEYS */;
/*!40000 ALTER TABLE `amortizacao_divida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anexos`
--

DROP TABLE IF EXISTS `anexos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anexos` (
  `pk_anexos` int(11) NOT NULL AUTO_INCREMENT,
  `data_hora` datetime DEFAULT NULL,
  `nome_ficheiro` varchar(100) DEFAULT NULL,
  `descricao` varchar(200) DEFAULT NULL,
  `fk_funcionario` int(11) NOT NULL,
  `fk_usuario` int(11) NOT NULL,
  `caminho` varchar(100) DEFAULT NULL,
  `caminho_ficheiro` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_anexos`),
  KEY `fk_anexos_tb_funcionario1_idx` (`fk_funcionario`),
  KEY `fk_anexos_tb_usuario1_idx` (`fk_usuario`),
  CONSTRAINT `fk_anexos_tb_funcionario1` FOREIGN KEY (`fk_funcionario`) REFERENCES `tb_funcionario` (`idFuncionario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_anexos_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anexos`
--

LOCK TABLES `anexos` WRITE;
/*!40000 ALTER TABLE `anexos` DISABLE KEYS */;
/*!40000 ALTER TABLE `anexos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ano_economico`
--

DROP TABLE IF EXISTS `ano_economico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ano_economico` (
  `pk_ano_economico` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) DEFAULT NULL,
  `serie` varchar(45) DEFAULT NULL,
  `data_inicio` date DEFAULT NULL,
  `data_fim` date DEFAULT NULL,
  PRIMARY KEY (`pk_ano_economico`),
  UNIQUE KEY `serie_UNIQUE` (`serie`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ano_economico`
--

LOCK TABLES `ano_economico` WRITE;
/*!40000 ALTER TABLE `ano_economico` DISABLE KEYS */;
INSERT INTO `ano_economico` VALUES (1,'2020','2020','2020-01-01','2020-12-31'),(2,'2021','2021','2021-01-05','2021-12-31'),(3,'2022','2022','2022-01-05','2022-12-31'),(4,'2023','2023','2023-01-05','2023-12-31'),(5,'2024','2024','2024-01-02','2024-12-31'),(6,'2025','2025','2025-01-01','2025-12-31');
/*!40000 ALTER TABLE `ano_economico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caixa`
--

DROP TABLE IF EXISTS `caixa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caixa` (
  `pk_caixa` int(11) NOT NULL AUTO_INCREMENT,
  `data_abertura` datetime DEFAULT NULL,
  `data_fecho` datetime DEFAULT NULL,
  `total_vendas` double DEFAULT NULL,
  `numero_vendas` int(11) DEFAULT NULL,
  `valor_inicial` double DEFAULT NULL,
  `usuario_fecho` varchar(100) DEFAULT NULL,
  `usuario_abertura` varchar(100) DEFAULT NULL,
  `cod_usuario_abertura` int(10) unsigned DEFAULT NULL,
  `cod_usuario_fecho` int(10) unsigned DEFAULT NULL,
  `total_desconto` decimal(30,2) DEFAULT NULL,
  `total_iva` decimal(30,2) DEFAULT NULL,
  `total_iliquido` decimal(30,2) DEFAULT NULL,
  PRIMARY KEY (`pk_caixa`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caixa`
--

LOCK TABLES `caixa` WRITE;
/*!40000 ALTER TABLE `caixa` DISABLE KEYS */;
INSERT INTO `caixa` VALUES (1,'2025-09-10 05:15:07',NULL,0,0,0,'','DVML COMERCIAL',15,0,'0.00','0.00','0.00');
/*!40000 ALTER TABLE `caixa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cambio`
--

DROP TABLE IF EXISTS `cambio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cambio` (
  `pk_cambio` int(11) NOT NULL AUTO_INCREMENT,
  `valor` double DEFAULT NULL,
  `data_hora` datetime DEFAULT NULL,
  `fk_moeda` int(11) NOT NULL,
  PRIMARY KEY (`pk_cambio`),
  KEY `fk_cambio_moeda1_idx` (`fk_moeda`),
  CONSTRAINT `fk_cambio_moeda1` FOREIGN KEY (`fk_moeda`) REFERENCES `moeda` (`pk_moeda`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cambio`
--

LOCK TABLES `cambio` WRITE;
/*!40000 ALTER TABLE `cambio` DISABLE KEYS */;
INSERT INTO `cambio` VALUES (1,1,'2019-10-06 19:42:20',1),(2,400,'2019-10-06 19:42:20',2),(3,450,'2019-10-06 19:42:20',3);
/*!40000 ALTER TABLE `cambio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compras`
--

DROP TABLE IF EXISTS `compras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `compras` (
  `pk_compra` int(11) NOT NULL AUTO_INCREMENT,
  `data_compra` datetime DEFAULT NULL,
  `total_compra` double DEFAULT NULL,
  `nome_fornecedor` varchar(100) DEFAULT NULL,
  `hash_cod` varchar(1000) DEFAULT NULL,
  `cod_fact` varchar(100) DEFAULT NULL,
  `ref_cod_fact` varchar(100) DEFAULT NULL,
  `total_iva` double DEFAULT NULL,
  `assinatura` varchar(50) DEFAULT NULL,
  `total_por_extenso` varchar(200) DEFAULT NULL,
  `codigo_usuario` int(11) DEFAULT NULL,
  `desconto_comercial` double DEFAULT NULL,
  `desconto_financeiro` double DEFAULT NULL,
  `desconto_total` double DEFAULT NULL,
  `total_incidencia` double DEFAULT NULL,
  `obs` varchar(200) DEFAULT NULL,
  `total_geral` double DEFAULT NULL,
  `valor_entregue` double DEFAULT NULL,
  `troco` double DEFAULT NULL,
  `total_incidencia_isento` double DEFAULT NULL,
  `data_limite_levantamento` date DEFAULT NULL,
  `fk_documento` int(11) NOT NULL,
  `fk_fornecedor` int(11) DEFAULT NULL,
  `idArmazemFK` int(10) unsigned DEFAULT '1',
  `fornecedor_nif` varchar(75) DEFAULT NULL,
  `fk_ano_economico` int(11) DEFAULT NULL,
  `autorizado` tinyint(1) DEFAULT '0',
  `despacho_encomenda` tinyint(1) DEFAULT '0',
  `encomendado` tinyint(1) DEFAULT '0',
  `status_eliminado` varchar(45) DEFAULT 'false',
  `status_recibo` tinyint(4) DEFAULT '0',
  `valor_por_pagar` decimal(30,2) DEFAULT NULL,
  `valor_pago` decimal(30,2) DEFAULT NULL,
  `doc_vosso` varchar(45) DEFAULT NULL,
  `doc_vosso_numero` varchar(45) DEFAULT NULL,
  `doc_vosso_data` date DEFAULT NULL,
  `doc_vosso_data_vencimento` date DEFAULT NULL,
  PRIMARY KEY (`pk_compra`) USING BTREE,
  KEY `fk_compras_documento1_idx` (`fk_documento`),
  KEY `fk_compras_tb_fornecedor1_idx` (`fk_fornecedor`),
  KEY `newfk_codigo_usuario` (`codigo_usuario`),
  KEY `newfk_fk_armazem` (`idArmazemFK`),
  KEY `newfk_ano_economicos` (`fk_ano_economico`),
  CONSTRAINT `fk_compras_documento1` FOREIGN KEY (`fk_documento`) REFERENCES `documento` (`pk_documento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_compras_tb_fornecedor1` FOREIGN KEY (`fk_fornecedor`) REFERENCES `tb_fornecedor` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `newfk_ano_economicos` FOREIGN KEY (`fk_ano_economico`) REFERENCES `ano_economico` (`pk_ano_economico`),
  CONSTRAINT `newfk_codigo_usuario` FOREIGN KEY (`codigo_usuario`) REFERENCES `tb_usuario` (`codigo`),
  CONSTRAINT `newfk_fk_armazem` FOREIGN KEY (`idArmazemFK`) REFERENCES `tb_armazem` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compras`
--

LOCK TABLES `compras` WRITE;
/*!40000 ALTER TABLE `compras` DISABLE KEYS */;
INSERT INTO `compras` VALUES (1,'2025-09-06 10:56:18',1000,'null','94055e7ae23e0a41d3afe1e887323929d6c6c13b5b427cb929f68f36b0ebfca3bb5197b19aaf4497167132713158a0937699ceb4cd8a49f31d9916564bce9e5','CO 2025/1','',0,'93e2','Compramos no valor de:  Um Mil Kwanza(s)',15,0,0,0,0,NULL,1000,NULL,NULL,1000,'2025-09-21',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'2025-09-06 10:56:21',1000,'null','4639e14ae117b81b07b24ddb896c406152bbaab30144b27c54702334e4c02ed798e423a8153dd66d74768c99f6fbf28c92a3078230a76dac2bb7e33771f06964','CO 2025/2','',0,'4146','Compramos no valor de:  Um Mil Kwanza(s)',15,0,0,0,0,NULL,1000,NULL,NULL,1000,'2025-09-21',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'2025-09-06 11:09:24',1000,'null','3d64ff9c217fc76d72b2b1de458571ce153bb5c4be48aaa305d9c0865e1c5e63c923c663de852cc2167573e306ef0338203239fc61a96569f644c2ea9cafd5c5','CO 2025/3','',0,'37bc','Compramos no valor de:  Um Mil Kwanza(s)',15,0,0,0,0,NULL,1000,NULL,NULL,1000,'2025-09-21',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'2025-09-06 11:22:32',1000,'null','91887acaf1106279ec3a39e34ed5f4fec86a0e1bd5f2b800a0e70ddf2281ee5926715368aa83f74c2649e616559c43150ac8428b6d0331bb533fa1ee1f5acc48','CO 2025/4','',0,'913f','Compramos no valor de:  Um Mil Kwanza(s)',15,0,0,0,0,NULL,1000,NULL,NULL,1000,'2025-09-21',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,'2025-09-06 11:23:14',1000,'null','ffea05113ee794260a05509706a50425f9c5d59b9826c3d61fb5ab29efba5a52d7436a249ef9a5430a6601fd7104b7447baeaeea842697800d41ea33fdb2dcea','CO 2025/5','',0,'fe52','Compramos no valor de:  Um Mil Kwanza(s)',15,0,0,0,0,NULL,1000,NULL,NULL,1000,'2025-09-21',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,'2025-09-06 11:40:15',1000,'null','4cf0f7359e63a945b202bc8ee77a76b9d5baf8aafbd72f4212b0aca86e345b184a330d0f1ce6a76ea54c4605437a3ffce9c983c43c6d0bf8a827a19fafbffb10','CO 2025/6','',0,'46bb','Compramos no valor de:  Um Mil Kwanza(s)',15,0,0,0,0,NULL,1000,NULL,NULL,1000,'2025-09-21',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,'2025-09-06 11:40:21',1000,'null','b4fbd4d3e5cfa616629b8e83b52d8147c9251b5108b9e5c4daffdc526a90788b2a41e754be6b9622686183d58101f08685618f7d14b4be66673219e40ed1e523','CO 2025/7','',0,'bc84','Compramos no valor de:  Um Mil Kwanza(s)',15,0,0,0,0,NULL,1000,NULL,NULL,1000,'2025-09-21',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,'2025-09-06 11:42:13',1000,'null','7a6a30c18be4158754890d1bf7dc648922dc80da3af3de68bd3b32c2778c7c184d91ec8820fb5aa11887767d43217b1ec680d9a4cbcd8b79e0a0ac8d2cc63291','CO 2025/8','',0,'7e08','Compramos no valor de:  Um Mil Kwanza(s)',15,0,0,0,0,NULL,1000,NULL,NULL,1000,'2025-09-21',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,'2025-09-08 09:09:03',243960,'null','55ba2b656ece77fe95104a663b6db944812340762665f2db554afc681788e0ce2d72d2232368783a375b0017b3ac4df480e8cd75404cc391479e5e21c6ba4c6','CO 2025/9','',15960,'5c44','Compramos no valor de: Duzentos  e Quarenta e Três Mil, Novecentos  e Sessenta Kwanza(s)',15,0,0,0,228000,NULL,228000,NULL,NULL,0,'2025-09-23',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(10,'2025-09-10 09:29:18',56000,'null','599380d08137b70c4a8c564083fcafa678f07635df2e87595dcb5c9ddaccd91c2eb599f1847366d4c4d4da3dbee62f8354e8ace5ae7d244045ec0a414f38f860','CO 2025/10','',0,'535a','Compramos no valor de: Cinquenta e Seis Mil Kwanza(s)',15,0,0,0,0,NULL,56000,NULL,NULL,56000,'2025-09-25',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(11,'2025-09-10 14:06:38',8400,'null','f6babd627568fb28187e363a5c5aade6039365823ca32af8c897d1605a547cd7f76d850cffae592f609ca321a9741fde358d16b772523b46441d343f5fad606','CO 2025/11','',0,'f63e','Compramos no valor de: Oito Mil, Quatrocentos  Kwanza(s)',15,0,0,0,0,NULL,8400,NULL,NULL,8400,'2025-09-25',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(12,'2025-09-10 14:12:03',96000,'null','b2e8903d4f0eea989020cdd00c0d081b6605e9f4fd5a912043e0616e47216ed8606ac8dd3ae7bb5d941f67a74bc8c2517372edad8539bc259301179c20e23eb2','CO 2025/12','',0,'b0c1','Compramos no valor de: Noventa e Seis Mil Kwanza(s)',15,0,0,0,0,NULL,96000,NULL,NULL,96000,'2025-09-25',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(13,'2025-09-10 14:12:30',64000,'null','df5236fa6941165eeb739a7005baff5339b1fa7a49568a11475393687e51f44e079604c69739059b69751cffa98264d4a0f760aef8b8e2a7858e192fe9a0b18b','CO 2025/13','',0,'d495','Compramos no valor de: Sessenta e Quatro Mil Kwanza(s)',15,0,0,0,0,NULL,64000,NULL,NULL,64000,'2025-09-25',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(14,'2025-09-10 14:13:32',60000,'null','1d6fbcd979aaf2bf7e156bf79963847e99acf08ae64e40ecbde8408ea864c0fff2c1cf007b94c770fe10a498909bd3fa56c3abff4aad31cc64b2d182402108d4','CO 2025/14','',0,'1a67','Compramos no valor de: Sessenta Mil Kwanza(s)',15,0,0,0,0,NULL,60000,NULL,NULL,60000,'2025-09-25',9,1,1,'999999999',6,NULL,NULL,NULL,'false',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `compras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta_movimentos`
--

DROP TABLE IF EXISTS `conta_movimentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conta_movimentos` (
  `pk_conta_movimento` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_hora` datetime DEFAULT NULL,
  `conta_id` int(10) unsigned DEFAULT NULL,
  `conta_designacao` varchar(100) DEFAULT NULL,
  `saldo_anterior` decimal(30,2) DEFAULT NULL,
  `valor_entrada` decimal(30,2) DEFAULT NULL,
  `valor_saida` decimal(30,2) DEFAULT NULL,
  `saldo_final` decimal(30,2) DEFAULT NULL,
  `tipo` enum('Entrada','Saida') DEFAULT NULL,
  `descricao` varchar(500) DEFAULT NULL,
  `documento` varchar(75) DEFAULT NULL,
  `cod_operacao` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`pk_conta_movimento`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta_movimentos`
--

LOCK TABLES `conta_movimentos` WRITE;
/*!40000 ALTER TABLE `conta_movimentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `conta_movimentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta_operacoes`
--

DROP TABLE IF EXISTS `conta_operacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conta_operacoes` (
  `pk_conta_operacao` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_hora` datetime DEFAULT NULL,
  `tipo` enum('Entrada','Saida','Transferencia','Anulacao') NOT NULL,
  `valor` decimal(30,2) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `user_name` varchar(75) DEFAULT NULL,
  `beneficiario` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`pk_conta_operacao`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta_operacoes`
--

LOCK TABLES `conta_operacoes` WRITE;
/*!40000 ALTER TABLE `conta_operacoes` DISABLE KEYS */;
/*!40000 ALTER TABLE `conta_operacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta_permissoes`
--

DROP TABLE IF EXISTS `conta_permissoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conta_permissoes` (
  `pk_conta_permissao` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `cod_usuario` int(10) unsigned DEFAULT NULL,
  `cod_conta` int(10) unsigned DEFAULT NULL,
  `entrada` tinyint(1) DEFAULT NULL,
  `saida` tinyint(1) DEFAULT NULL,
  `transferencia` tinyint(1) DEFAULT NULL,
  `vis_entrato` tinyint(1) DEFAULT NULL,
  `anulacao` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`pk_conta_permissao`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta_permissoes`
--

LOCK TABLES `conta_permissoes` WRITE;
/*!40000 ALTER TABLE `conta_permissoes` DISABLE KEYS */;
INSERT INTO `conta_permissoes` VALUES (1,15,1,1,1,1,1,1);
/*!40000 ALTER TABLE `conta_permissoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contas`
--

DROP TABLE IF EXISTS `contas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contas` (
  `pk_contas` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `designacao` varchar(75) DEFAULT NULL,
  `numero` varchar(75) DEFAULT NULL,
  `iban` varchar(100) DEFAULT NULL,
  `titular_1` varchar(45) DEFAULT NULL,
  `titular_2` varchar(45) DEFAULT NULL,
  `saldo` decimal(30,2) DEFAULT NULL,
  `objecto` enum('Pessoal','Profissional') DEFAULT NULL,
  `data_criacao` datetime DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `tipo_conta_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`pk_contas`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contas`
--

LOCK TABLES `contas` WRITE;
/*!40000 ALTER TABLE `contas` DISABLE KEYS */;
INSERT INTO `contas` VALUES (1,'Conta1','42433','s/n','s/n','s/n','3554.00','Profissional','2023-09-23 22:43:54',15,2),(2,'Transf','fwefwe','s/n','s/n','s/n','2010.00','Profissional','2023-09-29 08:30:38',15,2);
/*!40000 ALTER TABLE `contas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `db_backup_schedule`
--

DROP TABLE IF EXISTS `db_backup_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `db_backup_schedule` (
  `pk_db_backup_schedule` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ano` int(10) unsigned DEFAULT '0',
  `mes` int(10) unsigned DEFAULT '0',
  `dia` int(10) unsigned DEFAULT '0',
  `hora` int(10) unsigned DEFAULT '0',
  `minuto` int(10) unsigned DEFAULT '0',
  `segundo` int(10) unsigned DEFAULT '0',
  `dataUltimoBackup` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `dataProximoBackup` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`pk_db_backup_schedule`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `db_backup_schedule`
--

LOCK TABLES `db_backup_schedule` WRITE;
/*!40000 ALTER TABLE `db_backup_schedule` DISABLE KEYS */;
INSERT INTO `db_backup_schedule` VALUES (1,0,0,0,1,0,8,'2020-02-14 13:24:00','0000-00-00 00:00:00');
/*!40000 ALTER TABLE `db_backup_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deposito_bancario`
--

DROP TABLE IF EXISTS `deposito_bancario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deposito_bancario` (
  `pk_deposito` int(11) NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `nborderaux` varchar(45) DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `fk_banco` int(11) DEFAULT NULL,
  `fk_usuario` int(11) DEFAULT NULL,
  `descricao` varchar(900) DEFAULT NULL,
  PRIMARY KEY (`pk_deposito`),
  KEY `newfkBanc` (`fk_banco`),
  KEY `newfkUsuari` (`fk_usuario`),
  CONSTRAINT `newfkBanc` FOREIGN KEY (`fk_banco`) REFERENCES `tb_banco` (`idBanco`),
  CONSTRAINT `newfkUsuari` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deposito_bancario`
--

LOCK TABLES `deposito_bancario` WRITE;
/*!40000 ALTER TABLE `deposito_bancario` DISABLE KEYS */;
INSERT INTO `deposito_bancario` VALUES (1,'2017-11-17','19:55:15','12312214',1000,2,NULL,'sdgsddgsd'),(2,'2017-11-29','01:24:29','327582352',2000,3,15,'Foi depositado'),(3,'2020-06-04','10:11:22','435235',20000,2,15,'aeres');
/*!40000 ALTER TABLE `deposito_bancario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalhes_documento`
--

DROP TABLE IF EXISTS `detalhes_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detalhes_documento` (
  `pk_detalhes_documento` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ar_condicionado` varchar(45) DEFAULT NULL,
  `bateria` varchar(45) DEFAULT NULL,
  `camara` varchar(45) DEFAULT NULL,
  `canicos` varchar(45) DEFAULT NULL,
  `chave_roda` varchar(45) DEFAULT NULL,
  `cinto_seguranca` varchar(45) DEFAULT NULL,
  `colete` varchar(45) DEFAULT NULL,
  `elevador` varchar(45) DEFAULT NULL,
  `extintor` varchar(45) DEFAULT NULL,
  `farois` varchar(45) DEFAULT NULL,
  `guincho` varchar(45) DEFAULT NULL,
  `isqueiro` varchar(45) DEFAULT NULL,
  `limpa_parabrisa` varchar(45) DEFAULT NULL,
  `livrete` varchar(45) DEFAULT NULL,
  `titulo` varchar(45) DEFAULT NULL,
  `longos` varchar(45) DEFAULT NULL,
  `macaco` varchar(45) DEFAULT NULL,
  `manipulo_ext` varchar(45) DEFAULT NULL,
  `manipulo_int` varchar(45) DEFAULT NULL,
  `painel` varchar(45) DEFAULT NULL,
  `piscas` varchar(45) DEFAULT NULL,
  `porcas_jantes` varchar(45) DEFAULT NULL,
  `presenca` varchar(45) DEFAULT NULL,
  `radio` varchar(45) DEFAULT NULL,
  `reflectores` varchar(45) DEFAULT NULL,
  `retrovisores` varchar(45) DEFAULT NULL,
  `sensor` varchar(45) DEFAULT NULL,
  `sobresalente` varchar(45) DEFAULT NULL,
  `stop` varchar(45) DEFAULT NULL,
  `tampas_jantes` varchar(45) DEFAULT NULL,
  `tampoes` varchar(45) DEFAULT NULL,
  `tapetes` varchar(45) DEFAULT NULL,
  `tejadilho` varchar(45) DEFAULT NULL,
  `triangulo` varchar(45) DEFAULT NULL,
  `vidros` varchar(45) DEFAULT NULL,
  `cod_venda` int(10) unsigned DEFAULT NULL,
  `data_entrada` datetime DEFAULT NULL,
  `notas` varchar(2500) DEFAULT NULL,
  PRIMARY KEY (`pk_detalhes_documento`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalhes_documento`
--

LOCK TABLES `detalhes_documento` WRITE;
/*!40000 ALTER TABLE `detalhes_documento` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalhes_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalhes_documento_seccao`
--

DROP TABLE IF EXISTS `detalhes_documento_seccao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detalhes_documento_seccao` (
  `pk_detalhes_documento_seccao` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fk_detalhes_documento` int(10) unsigned DEFAULT NULL,
  `fk_seccao` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`pk_detalhes_documento_seccao`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalhes_documento_seccao`
--

LOCK TABLES `detalhes_documento_seccao` WRITE;
/*!40000 ALTER TABLE `detalhes_documento_seccao` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalhes_documento_seccao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documento`
--

DROP TABLE IF EXISTS `documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documento` (
  `pk_documento` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(50) DEFAULT NULL,
  `abreviacao` varchar(45) DEFAULT NULL,
  `cod_ultimo_doc` int(11) DEFAULT NULL,
  `descricao_ultimo_doc` varchar(45) DEFAULT NULL,
  `ultima_data` datetime DEFAULT NULL,
  PRIMARY KEY (`pk_documento`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documento`
--

LOCK TABLES `documento` WRITE;
/*!40000 ALTER TABLE `documento` DISABLE KEYS */;
INSERT INTO `documento` VALUES (1,'Factura/Recibo','FR',0,NULL,'2020-01-01 00:00:00'),(2,'Factura','FT',0,NULL,'2020-01-01 00:00:00'),(3,'Factura-Proforma','PP',0,NULL,'2020-01-01 00:00:00'),(4,'Nota de débito','ND',0,NULL,'2020-01-01 00:00:00'),(5,'Nota de crédito','NC',0,NULL,'2020-01-01 00:00:00'),(6,'Recibo','RE',0,NULL,'2020-01-01 00:00:00'),(7,'Guia de Transporte','GT',0,NULL,'2020-01-01 00:00:00'),(8,'Solicitacao Compra','SO',0,NULL,'2020-01-01 00:00:00'),(9,'Compras','CO',0,NULL,'2020-01-01 00:00:00'),(10,'Nota de Encomenda','NE',0,NULL,'2020-01-01 00:00:00'),(11,'Nota de Levantamento','NL',0,NULL,'2020-01-01 00:00:00'),(12,'Nota Credito Compra','NCO',0,NULL,'2020-01-01 00:00:00'),(13,'Consulta Mesa','CM',0,NULL,'2020-01-01 00:00:00'),(14,'Entrega Lavandaria','EL',0,NULL,'2020-01-01 00:00:00'),(15,'Vistoria','VI',0,NULL,'2020-01-01 00:00:00'),(16,'Despacho','DE',0,NULL,'2020-01-01 00:00:00');
/*!40000 ALTER TABLE `documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empresa`
--

DROP TABLE IF EXISTS `empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `empresa` (
  `pk_empresa` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) DEFAULT NULL,
  `telefone` varchar(100) DEFAULT NULL,
  `endereco` varchar(100) DEFAULT NULL,
  `NIF` varchar(75) DEFAULT NULL,
  `email` varchar(75) DEFAULT NULL,
  `logo_tipo` longblob,
  PRIMARY KEY (`pk_empresa`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresa`
--

LOCK TABLES `empresa` WRITE;
/*!40000 ALTER TABLE `empresa` DISABLE KEYS */;
INSERT INTO `empresa` VALUES (1,'Filial1','(+244) 999 999 999','N/A','N/A','N/A',NULL),(2,'Filiar2','(+244) 999 999 999','dd','22','rr',NULL),(3,'Sede','(+244) 999 999 999','dd','33','ss',NULL);
/*!40000 ALTER TABLE `empresa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrada_tesouraria`
--

DROP TABLE IF EXISTS `entrada_tesouraria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entrada_tesouraria` (
  `pk_entrada_tesouraria` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(900) DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `data_entrada` date DEFAULT NULL,
  `hora_entrada` time DEFAULT NULL,
  `fk_usuario` int(11) DEFAULT NULL,
  `fk_banco` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_entrada_tesouraria`),
  KEY `newfkUserss` (`fk_usuario`),
  KEY `newfk_bank` (`fk_banco`),
  CONSTRAINT `newfkUserss` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`),
  CONSTRAINT `newfk_bank` FOREIGN KEY (`fk_banco`) REFERENCES `tb_banco` (`idBanco`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrada_tesouraria`
--

LOCK TABLES `entrada_tesouraria` WRITE;
/*!40000 ALTER TABLE `entrada_tesouraria` DISABLE KEYS */;
INSERT INTO `entrada_tesouraria` VALUES (1,'dbdfbsdfb',500,'2017-11-17','21:01:23',15,1),(2,'asdawd',1000,'2020-06-09','19:03:31',15,1);
/*!40000 ALTER TABLE `entrada_tesouraria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extrato_conta_cliente`
--

DROP TABLE IF EXISTS `extrato_conta_cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `extrato_conta_cliente` (
  `pk_extrato_conta_cliente` bigint(20) NOT NULL AUTO_INCREMENT,
  `documento` varchar(100) DEFAULT NULL COMMENT 'Este campo serve para inserir os documentos:\nFactura\nRecibo\nProforma\nNota de Crédito\nNota de Débito\n…, etc',
  `referencia` varchar(100) DEFAULT NULL COMMENT 'Este campo serve para armazenar as referências tais como:\nFT 2021/1\nNC 2021/1\nRE 2021/1',
  `descricao` varchar(300) DEFAULT NULL COMMENT 'Serve para inserir a descrição do movimento. Exemplo:\nPagamento pelo multicaixa(TPA)',
  `data_hora` datetime DEFAULT NULL,
  `saldo_anterior` decimal(30,2) DEFAULT '0.00',
  `debito` decimal(30,2) DEFAULT '0.00',
  `credito` decimal(30,2) DEFAULT '0.00',
  `saldo_actual` decimal(30,2) DEFAULT NULL,
  `tipo_extrato` enum('DB','CR') DEFAULT NULL COMMENT 'Define o tipo de movimento:\nDebito = DB\nCredito = CR',
  `cliente_nome` varchar(300) DEFAULT NULL,
  `cliente_id` int(11) DEFAULT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  `usuario_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_extrato_conta_cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extrato_conta_cliente`
--

LOCK TABLES `extrato_conta_cliente` WRITE;
/*!40000 ALTER TABLE `extrato_conta_cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `extrato_conta_cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extrato_conta_fornecedor`
--

DROP TABLE IF EXISTS `extrato_conta_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `extrato_conta_fornecedor` (
  `pk_extrato_conta_fornecedor` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `documento` varchar(45) DEFAULT NULL,
  `referencia` varchar(45) DEFAULT NULL,
  `data_hora` datetime DEFAULT NULL,
  `saldo_anterior` decimal(30,2) DEFAULT NULL,
  `debito` decimal(30,2) DEFAULT NULL,
  `credito` decimal(30,2) DEFAULT NULL,
  `saldo_actual` decimal(30,2) DEFAULT NULL,
  `tipo_extrato` enum('DB','CR') DEFAULT NULL COMMENT 'Define o tipo de movimento:\nDebito = DB\n Credito = CR',
  `fornecedor_id` int(10) unsigned DEFAULT NULL,
  `fonecedor_nome` varchar(100) DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  `descricao` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pk_extrato_conta_fornecedor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extrato_conta_fornecedor`
--

LOCK TABLES `extrato_conta_fornecedor` WRITE;
/*!40000 ALTER TABLE `extrato_conta_fornecedor` DISABLE KEYS */;
/*!40000 ALTER TABLE `extrato_conta_fornecedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `familia`
--

DROP TABLE IF EXISTS `familia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `familia` (
  `pk_familia` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(100) NOT NULL,
  PRIMARY KEY (`pk_familia`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `familia`
--

LOCK TABLES `familia` WRITE;
/*!40000 ALTER TABLE `familia` DISABLE KEYS */;
INSERT INTO `familia` VALUES (1,'Serviços'),(2,'Produtos');
/*!40000 ALTER TABLE `familia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fecho_contrato`
--

DROP TABLE IF EXISTS `fecho_contrato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fecho_contrato` (
  `pk_fecho_contrato` int(11) NOT NULL AUTO_INCREMENT,
  `data_hora` datetime DEFAULT NULL,
  `salario_base` double DEFAULT NULL,
  `percentagem_feria` double DEFAULT NULL,
  `valor_feria` double DEFAULT NULL,
  `percentagem_natal` double DEFAULT NULL,
  `valor_natal` double DEFAULT NULL,
  `fk_funcionario` int(11) NOT NULL,
  `fk_usuario` int(11) NOT NULL,
  PRIMARY KEY (`pk_fecho_contrato`),
  KEY `fk_fecho_contrato_tb_funcionario1_idx` (`fk_funcionario`),
  KEY `fk_fecho_contrato_tb_usuario1_idx` (`fk_usuario`),
  CONSTRAINT `fk_fecho_contrato_tb_funcionario1` FOREIGN KEY (`fk_funcionario`) REFERENCES `tb_funcionario` (`idFuncionario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_fecho_contrato_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fecho_contrato`
--

LOCK TABLES `fecho_contrato` WRITE;
/*!40000 ALTER TABLE `fecho_contrato` DISABLE KEYS */;
/*!40000 ALTER TABLE `fecho_contrato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fecho_periodo`
--

DROP TABLE IF EXISTS `fecho_periodo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fecho_periodo` (
  `pk_fecho_periodo` int(11) NOT NULL AUTO_INCREMENT,
  `data_abertura` datetime DEFAULT NULL,
  `data_fecho` datetime DEFAULT NULL,
  `fk_usuario` int(11) NOT NULL,
  `fk_ano` int(11) NOT NULL,
  `fk_periodo` int(11) NOT NULL,
  PRIMARY KEY (`pk_fecho_periodo`),
  KEY `fk_fecho_periodo_tb_usuario1_idx` (`fk_usuario`),
  KEY `fk_fecho_periodo_tb_ano1_idx` (`fk_ano`),
  KEY `fk_fecho_periodo_tb_mes_rh1_idx` (`fk_periodo`),
  CONSTRAINT `fk_fecho_periodo_tb_ano1` FOREIGN KEY (`fk_ano`) REFERENCES `tb_ano` (`idAno`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_fecho_periodo_tb_mes_rh1` FOREIGN KEY (`fk_periodo`) REFERENCES `tb_mes_rh` (`pk_mes_rh`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_fecho_periodo_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fecho_periodo`
--

LOCK TABLES `fecho_periodo` WRITE;
/*!40000 ALTER TABLE `fecho_periodo` DISABLE KEYS */;
INSERT INTO `fecho_periodo` VALUES (1,'2020-08-11 00:00:00','2020-09-30 00:00:00',15,1,9),(2,'2022-01-01 00:00:00','2022-02-10 00:00:00',15,3,1),(3,'2023-01-02 00:00:00','2023-01-31 00:00:00',15,4,1),(4,'2023-02-01 00:00:00','2023-02-28 00:00:00',15,4,2),(5,'2023-03-01 00:00:00','2023-03-31 00:00:00',15,4,3),(6,'2023-04-01 00:00:00','2023-04-30 00:00:00',15,4,4),(7,'2023-05-01 00:00:00','2023-05-31 00:00:00',15,4,5),(8,'2023-06-01 00:00:00','2023-06-30 00:00:00',15,4,6),(9,'2023-07-01 00:00:00','2023-07-31 00:00:00',15,4,7),(10,'2023-08-01 00:00:00','2023-08-31 00:00:00',15,4,8),(11,'2023-09-01 00:00:00','2023-09-30 00:00:00',15,4,9);
/*!40000 ALTER TABLE `fecho_periodo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ficha_tecnica`
--

DROP TABLE IF EXISTS `ficha_tecnica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ficha_tecnica` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `produto_id` int(10) unsigned DEFAULT NULL,
  `data_criacao` datetime DEFAULT NULL,
  `usuario_id_criacao` int(10) unsigned DEFAULT NULL,
  `data_actualizacao` datetime DEFAULT NULL,
  `usuario_id_actualizacao` int(10) unsigned DEFAULT NULL,
  `custo_produto` decimal(30,2) DEFAULT NULL,
  `percentagem_ganho` decimal(30,2) DEFAULT NULL,
  `custo_venda` decimal(30,2) DEFAULT NULL,
  `photo` blob,
  `qtd_composto` double DEFAULT NULL,
  `prato` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ficha_tecnica`
--

LOCK TABLES `ficha_tecnica` WRITE;
/*!40000 ALTER TABLE `ficha_tecnica` DISABLE KEYS */;
/*!40000 ALTER TABLE `ficha_tecnica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forma_pagamento`
--

DROP TABLE IF EXISTS `forma_pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forma_pagamento` (
  `pk_forma_pagamento` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(75) DEFAULT NULL,
  `fk_conta_associada` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`pk_forma_pagamento`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forma_pagamento`
--

LOCK TABLES `forma_pagamento` WRITE;
/*!40000 ALTER TABLE `forma_pagamento` DISABLE KEYS */;
INSERT INTO `forma_pagamento` VALUES (1,'Numerário',0),(2,'Cartão',0),(3,'Gorjeta',0),(4,'Tranferência',0),(5,'Ordem Sac',0);
/*!40000 ALTER TABLE `forma_pagamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forma_pagamento_item`
--

DROP TABLE IF EXISTS `forma_pagamento_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forma_pagamento_item` (
  `pk_forma_pagamento_item` int(11) NOT NULL AUTO_INCREMENT,
  `valor` decimal(10,2) DEFAULT NULL,
  `referencia` varchar(100) DEFAULT NULL,
  `fk_forma_pagamento` int(11) NOT NULL,
  `fk_venda` int(11) DEFAULT NULL,
  `troco` decimal(30,2) DEFAULT NULL,
  `valor_real` decimal(30,2) DEFAULT NULL,
  PRIMARY KEY (`pk_forma_pagamento_item`),
  KEY `fk_vend_idx` (`fk_venda`),
  KEY `fk_forma_pagamento_idx` (`fk_forma_pagamento`),
  CONSTRAINT `fk_forma_pagamento` FOREIGN KEY (`fk_forma_pagamento`) REFERENCES `forma_pagamento` (`pk_forma_pagamento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_vend` FOREIGN KEY (`fk_venda`) REFERENCES `tb_venda` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forma_pagamento_item`
--

LOCK TABLES `forma_pagamento_item` WRITE;
/*!40000 ALTER TABLE `forma_pagamento_item` DISABLE KEYS */;
INSERT INTO `forma_pagamento_item` VALUES (1,'6840.00','',1,1,'0.00','6840.00'),(2,'6840.00','',1,2,'0.00','6840.00'),(3,'5130.00','',1,3,'0.00','5130.00'),(4,'2000.00','',1,4,'176.00','1824.00'),(5,'7500.00','',1,5,'1116.00','6384.00'),(6,'8000.00','',1,6,'590.00','7410.00'),(7,'741.00','',1,7,'0.00','741.00'),(8,'7000.00','',1,8,'616.00','6384.00'),(9,'32000.00','',1,9,'80.00','31920.00'),(10,'6000.00','',1,10,'870.00','5130.00');
/*!40000 ALTER TABLE `forma_pagamento_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `funcionario_view`
--

DROP TABLE IF EXISTS `funcionario_view`;
/*!50001 DROP VIEW IF EXISTS `funcionario_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `funcionario_view` (
  `idFuncionario` int(11),
  `ID` varchar(100),
  `nome` varchar(45),
  `telefone` varchar(45),
  `morada` varchar(45),
  `user_name` varchar(45),
  `password` varchar(45),
  `habilitacao_literaria` varchar(100),
  `dias_instituicao` varchar(100),
  `fk_funcao` int(11),
  `fk_departamento` int(11),
  `fk_grau_academico` int(11),
  `fk_especialidade` int(11),
  `nif` varchar(100),
  `data_nascimento` date,
  `docID` varchar(45),
  `ndocID` varchar(100),
  `data_emissao_docID` date,
  `data_validade_docID` date,
  `fk_estado_civil` int(11),
  `idStatusFK` int(11),
  `fkUsuario` int(11),
  `fk_modalidade` int(11),
  `fk_empresa` int(11),
  `sexo` enum('Masculino','Feminino'),
  `n_seguranca_social` varchar(100),
  `desconto_seguranca_social` enum('Sim','Não'),
  `data_inicio_contrato` date,
  `data_fim_contrato` date,
  `telefone_1` varchar(50),
  `tipo_contrato` enum('Determinado','Indeterminado'),
  `duracao_contrato` enum('1 Mes','3 Meses','6 Meses','9 Meses','12 Meses','Indeterminado'),
  `data_contrato` date,
  `conta_fechada` tinyint(4),
  `motivo_fecho` enum('Despediu-se','Foi Despedido','Contrato Terminado'),
  `photo` longblob,
  `telefone_2` varchar(50),
  `activo` int(11),
  `funcao` varchar(45)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `grupo`
--

DROP TABLE IF EXISTS `grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo` (
  `pk_grupo` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pk_grupo`),
  UNIQUE KEY `designacao_UNIQUE` (`designacao`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupo`
--

LOCK TABLES `grupo` WRITE;
/*!40000 ALTER TABLE `grupo` DISABLE KEYS */;
INSERT INTO `grupo` VALUES (1,'Diferenciado');
/*!40000 ALTER TABLE `grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imposto`
--

DROP TABLE IF EXISTS `imposto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `imposto` (
  `pk_imposto` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(45) DEFAULT NULL,
  `taxa` double DEFAULT NULL,
  `data_hora` datetime DEFAULT NULL,
  PRIMARY KEY (`pk_imposto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imposto`
--

LOCK TABLES `imposto` WRITE;
/*!40000 ALTER TABLE `imposto` DISABLE KEYS */;
INSERT INTO `imposto` VALUES (1,'IVA',14,'2019-10-06 03:35:05'),(2,'IVA',10,'2019-10-06 03:35:05'),(3,'IVA',7,'2019-10-06 03:35:05'),(4,'IVA',6,'2019-10-06 03:35:05'),(5,'IVA',5,'2019-10-06 03:35:05');
/*!40000 ALTER TABLE `imposto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_caixa`
--

DROP TABLE IF EXISTS `item_caixa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_caixa` (
  `pk_item_caixa` int(11) NOT NULL AUTO_INCREMENT,
  `valor_declarado` double DEFAULT NULL,
  `valor_real` double DEFAULT NULL,
  `fk_caixa` int(11) NOT NULL,
  `fk_forma_pagamento` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_item_caixa`),
  KEY `fk_item_caixa_caixa1_idx` (`fk_caixa`),
  KEY `fk_item_caixa_forma_pagamento1_idx` (`fk_forma_pagamento`),
  CONSTRAINT `fk_item_caixa_caixa1` FOREIGN KEY (`fk_caixa`) REFERENCES `caixa` (`pk_caixa`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_caixa_forma_pagamento1` FOREIGN KEY (`fk_forma_pagamento`) REFERENCES `forma_pagamento` (`pk_forma_pagamento`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_caixa`
--

LOCK TABLES `item_caixa` WRITE;
/*!40000 ALTER TABLE `item_caixa` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_caixa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_compras`
--

DROP TABLE IF EXISTS `item_compras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_compras` (
  `pk_itm_compras` int(11) NOT NULL AUTO_INCREMENT,
  `preco_compra` double DEFAULT NULL,
  `quantidade` double DEFAULT NULL,
  `valor_iva` double DEFAULT NULL,
  `motivo_isensao` varchar(150) DEFAULT NULL,
  `desconto` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  `codigo_isensao` varchar(45) DEFAULT NULL,
  `fk_produto` int(11) NOT NULL,
  `fk_compra` int(11) NOT NULL,
  PRIMARY KEY (`pk_itm_compras`),
  KEY `fk_item_compras_tb_produto1_idx` (`fk_produto`),
  KEY `fk_item_compras_compras1_idx` (`fk_compra`),
  CONSTRAINT `fk_item_compras_compras1` FOREIGN KEY (`fk_compra`) REFERENCES `compras` (`pk_compra`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_compras_tb_produto1` FOREIGN KEY (`fk_produto`) REFERENCES `tb_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_compras`
--

LOCK TABLES `item_compras` WRITE;
/*!40000 ALTER TABLE `item_compras` DISABLE KEYS */;
INSERT INTO `item_compras` VALUES (4,100,10,0,'',0,1000,'',2,4),(5,100,10,0,'',0,1000,'',2,5),(8,100,10,0,'',0,1000,'',3,8),(9,7600,30,7,'',0,243960,'',4,9),(10,5600,10,0,'',0,56000,'',4,10),(11,1200,7,0,'',0,8400,'',5,11),(12,3200,30,0,'',0,96000,'',10,12),(13,3200,20,0,'',0,64000,'',10,13),(14,1200,50,0,'',0,60000,'',13,14);
/*!40000 ALTER TABLE `item_compras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_recibo_rh`
--

DROP TABLE IF EXISTS `item_recibo_rh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_recibo_rh` (
  `pk_item_recibo_rh` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(75) DEFAULT NULL,
  `remuneracao` double DEFAULT NULL,
  `desconto` double DEFAULT NULL,
  `fk_recibo_rh` int(11) NOT NULL,
  PRIMARY KEY (`pk_item_recibo_rh`),
  KEY `fk_item_recibo_rh_recibo_rh1_idx` (`fk_recibo_rh`),
  CONSTRAINT `fk_item_recibo_rh_recibo_rh1` FOREIGN KEY (`fk_recibo_rh`) REFERENCES `recibo_rh` (`pk_recibo_rh`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_recibo_rh`
--

LOCK TABLES `item_recibo_rh` WRITE;
/*!40000 ALTER TABLE `item_recibo_rh` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_recibo_rh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_salario_extra`
--

DROP TABLE IF EXISTS `item_salario_extra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_salario_extra` (
  `pk_item_salario_extra` int(11) NOT NULL AUTO_INCREMENT,
  `valor` double DEFAULT NULL,
  `data_hora` datetime DEFAULT NULL,
  `fk_master_table` int(11) NOT NULL,
  `fk_tb_funcionario` int(11) NOT NULL,
  `fk_tb_usuario` int(11) NOT NULL,
  PRIMARY KEY (`pk_item_salario_extra`),
  KEY `fk_item_salario_extra_master_table1_idx` (`fk_master_table`),
  KEY `fk_item_salario_extra_tb_funcionario1_idx` (`fk_tb_funcionario`),
  KEY `fk_item_salario_extra_tb_usuario1_idx` (`fk_tb_usuario`),
  CONSTRAINT `fk_item_salario_extra_master_table1` FOREIGN KEY (`fk_master_table`) REFERENCES `master_table` (`pk_master_table`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_salario_extra_tb_funcionario1` FOREIGN KEY (`fk_tb_funcionario`) REFERENCES `tb_funcionario` (`idFuncionario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_item_salario_extra_tb_usuario1` FOREIGN KEY (`fk_tb_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_salario_extra`
--

LOCK TABLES `item_salario_extra` WRITE;
/*!40000 ALTER TABLE `item_salario_extra` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_salario_extra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itens_nota`
--

DROP TABLE IF EXISTS `itens_nota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `itens_nota` (
  `pk_itens_nota` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(200) DEFAULT NULL,
  `valor` varchar(45) DEFAULT NULL,
  `fk_nota` int(11) NOT NULL,
  PRIMARY KEY (`pk_itens_nota`),
  KEY `fk_itens_nota_notas1_idx` (`fk_nota`),
  CONSTRAINT `fk_itens_nota_notas1` FOREIGN KEY (`fk_nota`) REFERENCES `notas` (`pk_nota`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itens_nota`
--

LOCK TABLES `itens_nota` WRITE;
/*!40000 ALTER TABLE `itens_nota` DISABLE KEYS */;
/*!40000 ALTER TABLE `itens_nota` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `levantamento_bancario`
--

DROP TABLE IF EXISTS `levantamento_bancario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `levantamento_bancario` (
  `pk_levantamento` int(11) NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `nborderaux` varchar(45) DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `fk_banco` int(11) DEFAULT NULL,
  `fk_usuario` int(11) DEFAULT NULL,
  `obs` varchar(1500) DEFAULT NULL,
  PRIMARY KEY (`pk_levantamento`),
  KEY `newfkBancs` (`fk_banco`),
  KEY `newfkUsuar` (`fk_usuario`),
  CONSTRAINT `newfkBancs` FOREIGN KEY (`fk_banco`) REFERENCES `tb_banco` (`idBanco`),
  CONSTRAINT `newfkUsuar` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `levantamento_bancario`
--

LOCK TABLES `levantamento_bancario` WRITE;
/*!40000 ALTER TABLE `levantamento_bancario` DISABLE KEYS */;
INSERT INTO `levantamento_bancario` VALUES (1,'2017-11-17','19:55:33','13654',1000,2,NULL,'rhewrhwe'),(2,'2020-06-04','10:11:42','4523q52',10000,2,15,'rdhrdbx');
/*!40000 ALTER TABLE `levantamento_bancario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linha_ficha_tecnica`
--

DROP TABLE IF EXISTS `linha_ficha_tecnica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linha_ficha_tecnica` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `igrendiente_id` int(10) unsigned DEFAULT NULL,
  `igrendiente_designacao` varchar(115) DEFAULT NULL,
  `unidade` varchar(45) DEFAULT NULL,
  `preco_unitario` decimal(30,2) DEFAULT NULL,
  `qtd_bruto` double DEFAULT NULL,
  `qtd_liquido` double DEFAULT NULL,
  `factor_correcao` double DEFAULT NULL,
  `custo_total` decimal(30,2) DEFAULT NULL,
  `ficha_tecnica_id` int(10) unsigned DEFAULT NULL,
  `unidade_compra` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linha_ficha_tecnica`
--

LOCK TABLES `linha_ficha_tecnica` WRITE;
/*!40000 ALTER TABLE `linha_ficha_tecnica` DISABLE KEYS */;
/*!40000 ALTER TABLE `linha_ficha_tecnica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linha_transferencia`
--

DROP TABLE IF EXISTS `linha_transferencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linha_transferencia` (
  `pk_linha_transferencia` int(11) NOT NULL AUTO_INCREMENT,
  `qtd_before` double DEFAULT NULL,
  `qtd` double DEFAULT NULL,
  `qtd_after` double DEFAULT NULL,
  `fk_armazem_origem` int(11) DEFAULT NULL,
  `armazem_origem` varchar(250) DEFAULT NULL,
  `fk_armazem_destino` int(11) DEFAULT NULL,
  `armazem_destino` varchar(250) DEFAULT NULL,
  `fk_transferencia_armazem` int(11) NOT NULL,
  `fk_produto` int(10) unsigned DEFAULT NULL,
  `produto` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`pk_linha_transferencia`),
  KEY `fk_linha_transferencia_transferencia_armazem1_idx` (`fk_transferencia_armazem`),
  CONSTRAINT `fk_linha_transferencia_transferencia_armazem1` FOREIGN KEY (`fk_transferencia_armazem`) REFERENCES `transferencia_armazem` (`pk_transferencia_armazem`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linha_transferencia`
--

LOCK TABLES `linha_transferencia` WRITE;
/*!40000 ALTER TABLE `linha_transferencia` DISABLE KEYS */;
INSERT INTO `linha_transferencia` VALUES (1,0,10,0,1,'Armazem 1',2,'Loja',1,4,'APARAFUSADOR C/CARREGADOR/TID'),(2,0,5,0,1,'Armazem 1',2,'Loja',2,4,'APARAFUSADOR C/CARREGADOR/TID'),(3,0,4,0,1,'Armazem 1',2,'Loja',3,5,'ARAME GALVANIZADO 2.7MMX1OKG'),(4,0,20,0,1,'Armazem 1',2,'Loja',3,13,'AUTO CLISMO');
/*!40000 ALTER TABLE `linha_transferencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `pk_log` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ip_maquina` varchar(75) NOT NULL,
  `estado` enum('ON','OFF') NOT NULL,
  `nome_maquina` varchar(75) NOT NULL,
  PRIMARY KEY (`pk_log`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
INSERT INTO `log` VALUES (1,'127.0.0.1','OFF','DESKTOP-BK81URK'),(2,'192.168.1.1','ON','DESKTOP-PEMCFPL'),(3,'192.168.42.170','OFF','DESKTOP-QEL2NAS'),(4,'127.0.0.1','OFF','DESKTOP-BMC66KH'),(5,'192.168.1.188','ON','DESKTOP-NJ7UJ00'),(6,'192.168.1.190','OFF','POSV10');
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marca`
--

DROP TABLE IF EXISTS `marca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marca` (
  `pk_marca` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_marca`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marca`
--

LOCK TABLES `marca` WRITE;
/*!40000 ALTER TABLE `marca` DISABLE KEYS */;
INSERT INTO `marca` VALUES (1,'Diferenciado');
/*!40000 ALTER TABLE `marca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `master_table`
--

DROP TABLE IF EXISTS `master_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `master_table` (
  `pk_master_table` int(11) NOT NULL AUTO_INCREMENT,
  `designcao` varchar(100) DEFAULT NULL,
  `fk_master_table` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_master_table`),
  KEY `fk_master_table_master_table1_idx` (`fk_master_table`),
  CONSTRAINT `fk_master_table_master_table1` FOREIGN KEY (`fk_master_table`) REFERENCES `master_table` (`pk_master_table`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `master_table`
--

LOCK TABLES `master_table` WRITE;
/*!40000 ALTER TABLE `master_table` DISABLE KEYS */;
INSERT INTO `master_table` VALUES (1,'Remunerações',NULL),(2,'Descontos',NULL),(3,'Abonos',NULL),(4,'Remuneração 1',1),(5,'Remuneração 2',1),(6,'Remuneração 3',1),(7,'Desconto 1',2),(8,'Desconto 2',2),(9,'Desconto 3',2),(10,'Abono 1',3),(11,'Abono 2',3),(12,'Abono 3',3),(13,'Abono 4',3);
/*!40000 ALTER TABLE `master_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modalidade`
--

DROP TABLE IF EXISTS `modalidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modalidade` (
  `pk_modalidade` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) DEFAULT NULL,
  `dias_uteis_trabalho` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_modalidade`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modalidade`
--

LOCK TABLES `modalidade` WRITE;
/*!40000 ALTER TABLE `modalidade` DISABLE KEYS */;
INSERT INTO `modalidade` VALUES (1,'Regime Normal',22),(2,'Regime Intermédio',26),(3,'Regime de Turno',30);
/*!40000 ALTER TABLE `modalidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modelo`
--

DROP TABLE IF EXISTS `modelo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modelo` (
  `pk_modelo` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(100) DEFAULT NULL,
  `fk_marca` int(11) NOT NULL,
  PRIMARY KEY (`pk_modelo`),
  KEY `fk_modelo_marca1_idx` (`fk_marca`),
  CONSTRAINT `fk_modelo_marca1` FOREIGN KEY (`fk_marca`) REFERENCES `marca` (`pk_marca`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modelo`
--

LOCK TABLES `modelo` WRITE;
/*!40000 ALTER TABLE `modelo` DISABLE KEYS */;
INSERT INTO `modelo` VALUES (1,'Diferenciado',1);
/*!40000 ALTER TABLE `modelo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `moeda`
--

DROP TABLE IF EXISTS `moeda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moeda` (
  `pk_moeda` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) DEFAULT NULL,
  `abreviacao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_moeda`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moeda`
--

LOCK TABLES `moeda` WRITE;
/*!40000 ALTER TABLE `moeda` DISABLE KEYS */;
INSERT INTO `moeda` VALUES (1,'Kwanza','AOA'),(2,'Dólar Américano','USD'),(3,'Euro','EUR');
/*!40000 ALTER TABLE `moeda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimentacao`
--

DROP TABLE IF EXISTS `movimentacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movimentacao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `produto_id` int(11) NOT NULL,
  `data_mov` datetime NOT NULL,
  `tipo` enum('ENTRADA','SAIDA') NOT NULL,
  `documento` varchar(50) DEFAULT NULL,
  `quantidade_anterior` decimal(10,2) NOT NULL,
  `quantidade` decimal(10,2) NOT NULL,
  `quantidade_actual` decimal(10,2) NOT NULL,
  `valor_unitario` decimal(10,2) NOT NULL,
  `valor_total` decimal(15,2) DEFAULT '0.00',
  `custo_medio` decimal(15,2) DEFAULT '0.00',
  `usuario_id` int(11) NOT NULL,
  `armazem_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimentacao`
--

LOCK TABLES `movimentacao` WRITE;
/*!40000 ALTER TABLE `movimentacao` DISABLE KEYS */;
INSERT INTO `movimentacao` VALUES (1,3,'2025-09-10 07:48:53','SAIDA','FR 2025/6','21.00','10.00','11.00','0.00','0.00','0.00',15,2),(4,3,'2025-09-10 07:55:26','ENTRADA','ACERTO','5.00','-2.00','3.00','0.00','0.00','0.00',15,2),(5,3,'2025-09-10 07:55:54','ENTRADA','ACERTO','3.00','10.00','13.00','650.00','6500.00','500.00',15,2),(6,3,'2025-09-10 07:56:24','ENTRADA','ACERTO','15.00','-5.00','10.00','0.00','0.00','812.50',15,2),(7,3,'2025-09-10 07:56:57','ENTRADA','ACERTO','10.00','4.00','14.00','650.00','2600.00','766.07',15,2),(8,3,'2025-09-10 07:57:12','ENTRADA','ACERTO','14.00','4.00','18.00','650.00','2600.00','740.28',15,2),(9,3,'2025-09-10 07:57:28','ENTRADA','ACERTO','14.00','4.00','18.00','650.00','2600.00','723.87',15,2),(10,3,'2025-09-10 07:57:30','ENTRADA','ACERTO','18.00','4.00','22.00','650.00','2600.00','710.44',15,2),(11,4,'2025-09-10 07:58:15','ENTRADA','ACERTO','29.00','-9.00','20.00','0.00','0.00','0.00',15,2),(12,4,'2025-09-10 08:05:39','SAIDA','SAIDA 1','20.00','2.00','18.00','5600.00','11200.00','-622.22',15,2),(13,3,'2025-09-10 09:20:42','SAIDA','FR 2025/7','18.00','1.00','17.00','650.00','650.00','714.00',15,2),(14,4,'2025-09-10 09:21:12','SAIDA','FR 2025/8','18.00','1.00','17.00','5600.00','5600.00','-988.23',15,2),(15,4,'2025-09-10 09:29:18','ENTRADA','CO 2025/10','10.00','10.00','20.00','5600.00','56000.00','1451.86',15,1),(16,4,'2025-09-10 09:39:57','SAIDA','TRANSFERENCIA 1','20.00','10.00','10.00','5600.00','56000.00','-2696.28',15,1),(17,4,'2025-09-10 09:39:57','ENTRADA','TRANSFERENCIA 1','17.00','10.00','27.00','5600.00','56000.00','1451.86',15,2),(18,4,'2025-09-10 10:17:19','SAIDA','TRANSFERENCIA 2','10.00','5.00','5.00','5600.00','28000.00','-2696.28',15,1),(19,4,'2025-09-10 10:17:19','ENTRADA','TRANSFERENCIA 2','27.00','5.00','32.00','5600.00','28000.00','1451.86',15,2),(20,4,'2025-09-10 10:19:03','SAIDA','FR 2025/9','32.00','5.00','27.00','5600.00','28000.00','683.69',15,2),(21,10,'2025-09-10 14:12:03','ENTRADA','CO 2025/12','0.00','30.00','30.00','4500.00','135000.00','4500.00',15,1),(22,10,'2025-09-10 14:12:30','ENTRADA','CO 2025/13','30.00','20.00','50.00','4500.00','90000.00','4500.00',15,1),(23,13,'2025-09-10 14:13:32','ENTRADA','CO 2025/14','0.00','50.00','50.00','6000.00','300000.00','6000.00',15,1),(24,10,'2025-09-10 14:15:39','SAIDA','FR 2025/10','3.00','1.00','2.00','4500.00','4500.00','4500.00',15,2),(25,2,'2025-09-10 16:38:09','ENTRADA','ACERTO','8.00','4.00','12.00','0.00','0.00','0.00',64,1),(26,2,'2025-09-10 16:38:14','ENTRADA','ACERTO','12.00','4.00','16.00','0.00','0.00','0.00',64,1),(27,2,'2025-09-10 16:38:24','ENTRADA','ACERTO','12.00','4.00','16.00','0.00','0.00','0.00',64,1),(28,2,'2025-09-10 16:38:29','ENTRADA','ACERTO','12.00','4.00','16.00','0.00','0.00','0.00',64,1),(29,2,'2025-09-10 16:38:45','ENTRADA','ACERTO','36.00','4.00','40.00','0.00','0.00','0.00',64,2),(30,2,'2025-09-10 16:38:49','ENTRADA','ACERTO','40.00','6.00','46.00','0.00','0.00','0.00',64,2),(31,2,'2025-09-10 16:38:53','ENTRADA','ACERTO','42.00','6.00','48.00','0.00','0.00','0.00',64,2),(32,2,'2025-09-10 16:38:58','ENTRADA','ACERTO','42.00','6.00','48.00','0.00','0.00','0.00',64,2),(33,2,'2025-09-10 16:40:27','ENTRADA','ACERTO','12.00','6.00','18.00','0.00','0.00','0.00',64,1),(34,2,'2025-09-10 16:41:52','ENTRADA','ACERTO','18.00','2.00','20.00','0.00','0.00','0.00',64,1),(35,2,'2025-09-10 16:41:57','ENTRADA','ACERTO','20.00','4.00','24.00','0.00','0.00','0.00',64,1),(36,2,'2025-09-10 16:42:33','ENTRADA','ACERTO','22.00','2.00','24.00','0.00','0.00','0.00',64,1),(37,5,'2025-09-10 16:45:53','SAIDA','TRANSFERENCIA 3','7.00','4.00','3.00','1600.00','6400.00','-2133.33',15,1),(38,5,'2025-09-10 16:45:53','ENTRADA','TRANSFERENCIA 3','3.00','4.00','7.00','1600.00','6400.00','0.00',15,2),(39,13,'2025-09-10 16:45:53','SAIDA','TRANSFERENCIA 3','50.00','20.00','30.00','6000.00','120000.00','6000.00',15,1),(40,13,'2025-09-10 16:45:53','ENTRADA','TRANSFERENCIA 3','2.00','20.00','22.00','6000.00','120000.00','6000.00',15,2);
/*!40000 ALTER TABLE `movimentacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas`
--

DROP TABLE IF EXISTS `notas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas` (
  `pk_nota` int(11) NOT NULL AUTO_INCREMENT,
  `tipo_nota` enum('C','D') DEFAULT NULL,
  `cod_nota` varchar(45) DEFAULT NULL,
  `ref_cod_nota` varchar(45) DEFAULT NULL,
  `dataNota` datetime DEFAULT NULL,
  `total_venda` decimal(30,2) NOT NULL,
  `performance` varchar(45) NOT NULL DEFAULT 'false',
  `credito` varchar(45) NOT NULL DEFAULT 'false',
  `valor_entregue` double NOT NULL,
  `troco` double NOT NULL,
  `hora` time NOT NULL,
  `nome_cliente` varchar(45) NOT NULL,
  `status_eliminado` varchar(45) NOT NULL DEFAULT 'false',
  `desconto_total` double DEFAULT '0',
  `total_iva` double DEFAULT NULL,
  `total_geral` double NOT NULL,
  `cod_fact` varchar(100) DEFAULT NULL,
  `assinatura` varchar(100) DEFAULT NULL,
  `hash_cod` varchar(500) DEFAULT NULL,
  `obs` varchar(200) DEFAULT NULL,
  `ref_cod_fact` varchar(100) DEFAULT NULL,
  `total_por_extenso` varchar(200) DEFAULT NULL,
  `idBanco` int(11) DEFAULT '1',
  `codigo_usuario` int(11) NOT NULL,
  `codigo_cliente` int(11) NOT NULL,
  `idArmazemFK` int(10) unsigned NOT NULL DEFAULT '1',
  `fk_documento` int(11) NOT NULL,
  `fk_ano_economico` int(11) NOT NULL,
  `fk_cambio` int(11) NOT NULL,
  `ref_fact_data` datetime DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL,
  `desconto_comercial` double DEFAULT NULL,
  `desconto_finaceiro` double DEFAULT NULL,
  `local_carga` varchar(100) DEFAULT NULL,
  `local_descarga` varchar(100) DEFAULT NULL,
  `motivo` varchar(100) DEFAULT NULL,
  `total_incidencia` double DEFAULT NULL,
  `cliente_nif` varchar(75) DEFAULT NULL,
  `total_incidencia_isento` double DEFAULT '0',
  `ref_data_fact` varchar(45) DEFAULT NULL,
  `total_nota` decimal(30,2) DEFAULT NULL,
  PRIMARY KEY (`pk_nota`),
  UNIQUE KEY `assinatura_UNIQUE` (`assinatura`),
  KEY `fk_tb_venda_tb_usuario1` (`codigo_usuario`),
  KEY `fk_tb_venda_tb_cliente1` (`codigo_cliente`),
  KEY `FK_tb_venda_3` (`idArmazemFK`),
  KEY `fk_tb_usuario_banco` (`idBanco`),
  KEY `fk_tb_venda_documento1_idx` (`fk_documento`),
  KEY `fk_tb_venda_ano_economico1_idx` (`fk_ano_economico`),
  KEY `fk_tb_venda_cambio1_idx` (`fk_cambio`),
  CONSTRAINT `fk_tb_usuario_banco_1` FOREIGN KEY (`idBanco`) REFERENCES `tb_banco` (`idBanco`),
  CONSTRAINT `FK_tb_venda_31` FOREIGN KEY (`idArmazemFK`) REFERENCES `tb_armazem` (`codigo`),
  CONSTRAINT `fk_tb_venda_ano_economico11` FOREIGN KEY (`fk_ano_economico`) REFERENCES `ano_economico` (`pk_ano_economico`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_venda_cambio11` FOREIGN KEY (`fk_cambio`) REFERENCES `cambio` (`pk_cambio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_venda_documento11` FOREIGN KEY (`fk_documento`) REFERENCES `documento` (`pk_documento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_venda_tb_cliente11` FOREIGN KEY (`codigo_cliente`) REFERENCES `tb_cliente` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_venda_tb_usuario11` FOREIGN KEY (`codigo_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas`
--

LOCK TABLES `notas` WRITE;
/*!40000 ALTER TABLE `notas` DISABLE KEYS */;
/*!40000 ALTER TABLE `notas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_compras`
--

DROP TABLE IF EXISTS `notas_compras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas_compras` (
  `pk_nota_compras` int(11) NOT NULL AUTO_INCREMENT,
  `cod_nota` varchar(45) DEFAULT NULL,
  `ref_cod_nota` varchar(45) DEFAULT NULL,
  `dataNota` datetime DEFAULT NULL,
  `total_compra` float NOT NULL,
  `performance` varchar(45) NOT NULL DEFAULT 'false',
  `credito` varchar(45) NOT NULL DEFAULT 'false',
  `hora` time NOT NULL,
  `status_eliminado` varchar(45) NOT NULL DEFAULT 'false',
  `desconto_total` double DEFAULT '0',
  `total_iva` double DEFAULT NULL,
  `total_geral` double DEFAULT NULL,
  `cod_fact` varchar(100) DEFAULT NULL,
  `assinatura` varchar(100) DEFAULT NULL,
  `hash_cod` varchar(500) DEFAULT NULL,
  `obs` varchar(200) DEFAULT NULL,
  `ref_cod_fact` varchar(100) DEFAULT NULL,
  `total_por_extenso` varchar(200) DEFAULT NULL,
  `idBanco` int(10) DEFAULT '1',
  `codigo_usuario` int(10) NOT NULL,
  `fk_fornecedor` int(11) NOT NULL,
  `fk_documento` int(10) DEFAULT NULL,
  `fk_ano_economico` int(10) NOT NULL,
  `ref_fact_data` datetime DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  `desconto_comercial` double DEFAULT NULL,
  `desconto_financeiro` double DEFAULT NULL,
  `motivo` varchar(100) DEFAULT NULL,
  `total_incidencia` double DEFAULT NULL,
  `total_incidencia_isento` double DEFAULT '0',
  `ref_data_fact` varchar(45) DEFAULT NULL,
  `idArmazemFK` int(10) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`pk_nota_compras`),
  KEY `fk_bancos_idx` (`idBanco`),
  KEY `fkUsers_idx` (`codigo_usuario`),
  KEY `fk_forn_idx` (`fk_fornecedor`),
  KEY `codigo_armaze_idx` (`idArmazemFK`),
  KEY `fk_docs_idx` (`fk_documento`),
  KEY `ano_econ_idx` (`fk_ano_economico`),
  CONSTRAINT `ano_econ` FOREIGN KEY (`fk_ano_economico`) REFERENCES `ano_economico` (`pk_ano_economico`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `codigo_armaze` FOREIGN KEY (`idArmazemFK`) REFERENCES `tb_armazem` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `documentos_fk` FOREIGN KEY (`fk_documento`) REFERENCES `documento` (`pk_documento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkUsers` FOREIGN KEY (`codigo_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bancos` FOREIGN KEY (`idBanco`) REFERENCES `tb_banco` (`idBanco`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_forn` FOREIGN KEY (`fk_fornecedor`) REFERENCES `tb_fornecedor` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_compras`
--

LOCK TABLES `notas_compras` WRITE;
/*!40000 ALTER TABLE `notas_compras` DISABLE KEYS */;
/*!40000 ALTER TABLE `notas_compras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_item`
--

DROP TABLE IF EXISTS `notas_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas_item` (
  `pk_notas_item` int(11) NOT NULL AUTO_INCREMENT,
  `quantidade` double DEFAULT NULL,
  `valor_iva` double DEFAULT NULL,
  `motivo_isensao` varchar(150) DEFAULT NULL,
  `desconto` double DEFAULT NULL,
  `total` decimal(30,2) DEFAULT NULL,
  `fk_venda` int(11) DEFAULT NULL,
  `fk_produto` int(11) DEFAULT NULL,
  `fk_preco` int(10) unsigned DEFAULT NULL,
  `fk_nota` int(11) DEFAULT NULL,
  `codigo_isencao` varchar(50) DEFAULT NULL,
  `total_imposto` decimal(30,2) DEFAULT NULL,
  PRIMARY KEY (`pk_notas_item`),
  KEY `fk_venda_idx_1` (`fk_venda`),
  KEY `fk_produto_idx` (`fk_produto`),
  KEY `fk_preco_12_idx` (`fk_preco`),
  KEY `fk_nota_12_idx` (`fk_nota`),
  CONSTRAINT `fk_nota_12` FOREIGN KEY (`fk_nota`) REFERENCES `notas` (`pk_nota`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_preco` FOREIGN KEY (`fk_preco`) REFERENCES `tb_preco` (`pk_preco`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_produto_12` FOREIGN KEY (`fk_produto`) REFERENCES `tb_produto` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_venda_12` FOREIGN KEY (`fk_venda`) REFERENCES `tb_venda` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_item`
--

LOCK TABLES `notas_item` WRITE;
/*!40000 ALTER TABLE `notas_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `notas_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas_item_compras`
--

DROP TABLE IF EXISTS `notas_item_compras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas_item_compras` (
  `pk_notas_item` int(11) NOT NULL AUTO_INCREMENT,
  `quantidade` double DEFAULT NULL,
  `valor_iva` double DEFAULT NULL,
  `motivo_isensao` varchar(150) DEFAULT NULL,
  `desconto` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  `fk_compras` int(11) DEFAULT NULL,
  `fk_produto` int(11) DEFAULT NULL,
  `fk_preco` int(11) DEFAULT NULL,
  `fk_nota_compras` int(11) DEFAULT NULL,
  `codigo_isencao` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`pk_notas_item`),
  KEY `codig_compra_idx` (`fk_compras`),
  KEY `cod_prods_idx` (`fk_produto`),
  KEY `cod_not_idx` (`fk_nota_compras`),
  CONSTRAINT `codig_compra` FOREIGN KEY (`fk_compras`) REFERENCES `compras` (`pk_compra`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cod_not` FOREIGN KEY (`fk_nota_compras`) REFERENCES `notas_compras` (`pk_nota_compras`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cod_prods` FOREIGN KEY (`fk_produto`) REFERENCES `tb_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas_item_compras`
--

LOCK TABLES `notas_item_compras` WRITE;
/*!40000 ALTER TABLE `notas_item_compras` DISABLE KEYS */;
/*!40000 ALTER TABLE `notas_item_compras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagamento_subsidio_feria_natal`
--

DROP TABLE IF EXISTS `pagamento_subsidio_feria_natal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagamento_subsidio_feria_natal` (
  `pk_pagamento_subsidio_feria_natal` int(11) NOT NULL AUTO_INCREMENT,
  `data_hora` datetime DEFAULT NULL,
  `percentagem` double DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `salario_base` double DEFAULT NULL,
  `tipo_subsideo` enum('Feria','Natal') DEFAULT NULL,
  `fk_funcionario` int(11) NOT NULL,
  `fk_usuario` int(11) NOT NULL,
  `forma_pagamento` varchar(75) DEFAULT NULL,
  `desconto_irt` double DEFAULT NULL,
  PRIMARY KEY (`pk_pagamento_subsidio_feria_natal`),
  KEY `fk_pagamento_subsidio_feria_natal_tb_funcionario1_idx` (`fk_funcionario`),
  KEY `fk_pagamento_subsidio_feria_natal_tb_usuario1_idx` (`fk_usuario`),
  CONSTRAINT `fk_pagamento_subsidio_feria_natal_tb_funcionario1` FOREIGN KEY (`fk_funcionario`) REFERENCES `tb_funcionario` (`idFuncionario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pagamento_subsidio_feria_natal_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagamento_subsidio_feria_natal`
--

LOCK TABLES `pagamento_subsidio_feria_natal` WRITE;
/*!40000 ALTER TABLE `pagamento_subsidio_feria_natal` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagamento_subsidio_feria_natal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_feria`
--

DROP TABLE IF EXISTS `pedido_feria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedido_feria` (
  `pk_pedido_feria` int(11) NOT NULL AUTO_INCREMENT,
  `data_inicio` date DEFAULT NULL,
  `data_fim` date DEFAULT NULL,
  `dias_ferias` int(11) DEFAULT NULL,
  `data_registro` datetime DEFAULT NULL,
  `fk_funcionario` int(11) NOT NULL,
  `fk_usuario` int(11) NOT NULL,
  PRIMARY KEY (`pk_pedido_feria`),
  KEY `fk_pedido_feria_tb_funcionario1_idx` (`fk_funcionario`),
  KEY `fk_pedido_feria_tb_usuario1_idx` (`fk_usuario`),
  CONSTRAINT `fk_pedido_feria_tb_funcionario1` FOREIGN KEY (`fk_funcionario`) REFERENCES `tb_funcionario` (`idFuncionario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedido_feria_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_feria`
--

LOCK TABLES `pedido_feria` WRITE;
/*!40000 ALTER TABLE `pedido_feria` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedido_feria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `previo_aviso`
--

DROP TABLE IF EXISTS `previo_aviso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `previo_aviso` (
  `pk_previo_aviso` int(11) NOT NULL AUTO_INCREMENT,
  `data_previo` date DEFAULT NULL,
  `descricao` varchar(3000) DEFAULT NULL,
  `fk_usuario` int(11) NOT NULL,
  `fk_funcionario` int(11) NOT NULL,
  `numero_dias_aviso` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_previo_aviso`),
  KEY `fk_previo_aviso_tb_usuario1_idx` (`fk_usuario`),
  KEY `fk_previo_aviso_tb_funcionario1_idx` (`fk_funcionario`),
  CONSTRAINT `fk_previo_aviso_tb_funcionario1` FOREIGN KEY (`fk_funcionario`) REFERENCES `tb_funcionario` (`idFuncionario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_previo_aviso_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `previo_aviso`
--

LOCK TABLES `previo_aviso` WRITE;
/*!40000 ALTER TABLE `previo_aviso` DISABLE KEYS */;
/*!40000 ALTER TABLE `previo_aviso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productos` (
  `codigo_produto` int(11) NOT NULL AUTO_INCREMENT,
  `consumo` varchar(45) DEFAULT NULL,
  `preco` double DEFAULT NULL,
  `lugar` varchar(45) DEFAULT NULL,
  `qtd` double DEFAULT NULL,
  `mesa` varchar(45) DEFAULT NULL,
  `usuario` varchar(45) DEFAULT NULL,
  `data_hora` datetime DEFAULT NULL,
  PRIMARY KEY (`codigo_produto`)
) ENGINE=InnoDB AUTO_INCREMENT=2855 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'Caso Teste',2000,'Nokia',1,NULL,NULL,NULL),(2,'Teste Inactivo',1000,'Sony',0,NULL,NULL,NULL),(3,'Agua Pura',200,'LUGAR 7',1,'Mesa 4','DVML COMERCIAL','2022-11-15 16:08:51'),(4,'Café',600,'LUGAR 9',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:09:18'),(5,'Fino Caneca ',500,'LUGAR 8',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:09:23'),(6,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:09:38'),(7,'HAMBURGUER COMPOSTO',2500,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:09:44'),(8,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:09:49'),(9,'SOPA',1500,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:09:54'),(10,'SOPA',1500,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:09:58'),(11,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:10:03'),(12,'Café',600,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:10:11'),(13,'PEPSI COLA',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:10:15'),(14,'PEPSI COLA',300,'LUGAR 2',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:10:19'),(15,'Fino Normal',600,'LUGAR 2',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:10:28'),(16,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-25 13:10:34'),(17,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:11:30'),(18,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:11:35'),(19,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:15:10'),(20,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:15:20'),(21,'Café',600,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:15:24'),(22,'Café',600,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:15:28'),(23,'PEPSI COLA',300,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:15:32'),(24,'PEPSI COLA',300,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:15:37'),(25,'SOPA',1500,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:15:46'),(26,'Fino Normal',600,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:15:52'),(27,'HAMBURGUER COMPOSTO',2500,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:15:58'),(28,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:16:06'),(29,'Fino Caneca ',500,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-11-25 13:16:23'),(30,'NORMAL',250,'LUGAR 1',1,'Mesa 3','Lívia Kinessa','2022-11-25 17:28:24'),(31,'NORMAL',250,'LUGAR 1',1,'Mesa 3','Lívia Kinessa','2022-11-25 17:28:31'),(32,'NORMAL',250,'LUGAR 1',1,'Mesa 3','Lívia Kinessa','2022-11-25 17:28:37'),(33,'NORMAL',250,'LUGAR 1',1,'Mesa 3','Lívia Kinessa','2022-11-25 17:28:43'),(34,'Nocal Lata',500,'LUGAR 10',1,'Mesa 40','Lívia Kinessa','2022-11-25 19:12:11'),(35,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 13','Lívia Kinessa','2022-11-25 19:47:45'),(36,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 13','Lívia Kinessa','2022-11-25 19:47:49'),(37,'Duplo de Caxassa Rio de Janeiro',3000,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-11-25 19:51:51'),(38,'SUMO NATURAL MIX',2000,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-25 20:01:16'),(39,'SUMO NATURAL MIX',2000,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-25 20:01:29'),(40,'Choriço cazeiro',3500,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-25 20:56:17'),(41,'Água Pura 500ml',350,'LUGAR 1',1,'Mesa 4','Lívia Kinessa','2022-11-25 21:13:27'),(42,'Bitoque',5000,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-25 21:14:07'),(43,'Bitoque',5000,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-25 21:19:23'),(44,'Fino Normal',600,'LUGAR 1',1,'Mesa 14','Lívia Kinessa','2022-11-25 22:10:42'),(45,'Vinho do Barril Copo',1000,'LUGAR 7',1,'Mesa 5','Lívia Kinessa','2022-11-25 22:13:15'),(46,'Eka Garrafa',300,'LUGAR 6',1,'Mesa 5','Lívia Kinessa','2022-11-25 22:18:39'),(47,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-25 22:18:45'),(48,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-25 22:18:51'),(49,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-25 22:18:56'),(50,'Pica Pau',3500,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-25 22:19:02'),(51,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-25 22:19:09'),(52,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-25 22:19:24'),(53,'Vinho do Barril Copo',1000,'LUGAR 7',1,'Mesa 5','Lívia Kinessa','2022-11-26 12:55:41'),(54,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-26 12:55:47'),(55,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-26 12:56:03'),(56,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-26 12:56:24'),(57,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-26 12:56:35'),(58,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:06:57'),(59,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:07:04'),(60,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:07:12'),(61,'CIGARRO ROTHMANS',1500,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:07:17'),(62,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:07:21'),(63,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:07:26'),(64,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:07:36'),(65,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:07:42'),(66,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:08:02'),(67,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-26 13:08:23'),(68,'Fino Lambreta',400,'LUGAR 7',1,'Mesa 5','Lívia Kinessa','2022-11-26 17:14:38'),(69,'Dopel Lata',600,'LUGAR 1',1,'Mesa 13','Lívia Kinessa','2022-11-26 19:26:39'),(70,'Fino Normal',600,'LUGAR 1',1,'Mesa 13','Lívia Kinessa','2022-11-26 20:01:54'),(71,'Fino Normal',600,'LUGAR 1',1,'Mesa 13','Lívia Kinessa','2022-11-26 20:02:02'),(72,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 13','Lívia Kinessa','2022-11-26 20:02:07'),(73,'Dopel Garrafa',400,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-26 20:12:55'),(74,'Fino Normal',600,'LUGAR 9',1,'Mesa 5','Lívia Kinessa','2022-11-26 20:14:36'),(75,'Fino Normal',600,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-26 20:15:32'),(76,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-26 20:16:01'),(77,'Fino Normal',600,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-26 20:16:16'),(78,'Fino Normal',600,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-26 20:16:24'),(79,'Cuca garrafa',300,'LUGAR 10',1,'Mesa 3','Lívia Kinessa','2022-11-26 20:45:46'),(80,'Cuca garrafa',300,'LUGAR 9',1,'Mesa 7','Lívia Kinessa','2022-11-26 20:46:59'),(81,'Fino Normal',600,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-26 20:58:33'),(82,'Fino Normal',600,'LUGAR 9',1,'Mesa 5','Lívia Kinessa','2022-11-26 20:58:42'),(83,'Pica Pau',3500,'LUGAR 6',1,'Mesa 5','Lívia Kinessa','2022-11-26 20:59:11'),(84,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 15','Lívia Kinessa','2022-11-26 21:01:49'),(85,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-11-27 12:44:19'),(86,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 1','Lívia Kinessa','2022-11-27 13:11:18'),(87,'Fino Normal',600,'LUGAR 1',1,'Mesa 39','Nsiala Raquel','2022-11-28 16:20:08'),(88,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 39','Nsiala Raquel','2022-11-28 16:20:15'),(89,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 39','Nsiala Raquel','2022-11-28 16:21:18'),(90,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 39','Nsiala Raquel','2022-11-28 16:21:24'),(91,'Fino Normal',600,'LUGAR 1',1,'Mesa 39','Nsiala Raquel','2022-11-28 16:21:30'),(92,'Coca Cola Lata',500,'LUGAR 6',1,'Mesa 40','Nsiala Raquel','2022-11-28 16:22:18'),(93,'Bitoque',5000,'LUGAR 1',1,'Mesa 40','Nsiala Raquel','2022-11-28 16:22:27'),(94,'Coca Cola Lata',500,'LUGAR 1',1,'Mesa 40','Nsiala Raquel','2022-11-28 16:22:37'),(95,'Coca Cola Lata',500,'LUGAR 1',1,'Mesa 40','Nsiala Raquel','2022-11-28 16:22:54'),(96,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 40','Nsiala Raquel','2022-11-28 16:23:01'),(97,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 40','Nsiala Raquel','2022-11-28 16:23:13'),(98,'Bitoque',5000,'LUGAR 1',1,'Mesa 40','Nsiala Raquel','2022-11-28 16:23:18'),(99,'Fino Normal',600,'LUGAR 1',1,'Mesa 6','Lívia Kinessa','2022-11-28 17:56:45'),(100,'PEPSI COLA',300,'LUGAR 1',1,'Mesa 21','Lívia Kinessa','2022-11-28 18:50:16'),(101,'Bitoque',5000,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-11-28 19:03:58'),(102,'SUMOL GARRAFA',350,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-11-28 19:10:13'),(103,'Fino Normal',600,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-11-29 12:04:30'),(104,'Fino Normal',600,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-11-29 12:04:35'),(105,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 6','Lívia Kinessa','2022-11-29 12:14:58'),(106,'Nocal Lata',500,'LUGAR 1',1,'Mesa 6','Lívia Kinessa','2022-11-29 12:15:03'),(107,'Cuca Preta',500,'LUGAR 1',1,'Mesa 6','Lívia Kinessa','2022-11-29 12:15:07'),(108,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 6','Lívia Kinessa','2022-11-29 12:15:13'),(109,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 6','Lívia Kinessa','2022-11-29 12:15:17'),(110,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 6','Lívia Kinessa','2022-11-29 20:01:57'),(111,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 6','Lívia Kinessa','2022-11-29 20:02:12'),(112,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 2','Lívia Kinessa','2022-12-01 09:05:32'),(113,'Bitoque',5000,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-02 16:29:50'),(114,'NORMAL',250,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-02 18:19:38'),(115,'COMPAL GARRAFA',350,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-02 18:24:58'),(116,'ENTREGAS LOCAL',1000,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-02 18:25:48'),(117,'Água Pura 500ml',350,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-02 19:04:33'),(118,'Água Pura 500ml',350,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-02 19:04:43'),(119,'Água Pura 500ml',350,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-02 19:05:09'),(120,'Dopel Lata',600,'LUGAR 1',1,'Mesa 2','Lívia Kinessa','2022-12-02 19:56:29'),(121,'Fino Lambreta',400,'LUGAR 1',1,'Mesa 39','Nsiala Raquel','2022-12-03 11:34:34'),(122,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 40','Nsiala Raquel','2022-12-03 11:34:50'),(123,'Água Pura 500ml',350,'LUGAR 1',1,'Mesa 40','Nsiala Raquel','2022-12-03 11:34:55'),(124,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 40','Nsiala Raquel','2022-12-03 11:35:00'),(125,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 35','Lívia Kinessa','2022-12-05 10:34:11'),(126,'Tosta Simples',1600,'LUGAR 1',1,'Mesa 35','Lívia Kinessa','2022-12-05 10:34:17'),(127,'Água Pura 500ml',350,'LUGAR 1',1,'Mesa 35','Lívia Kinessa','2022-12-05 10:34:22'),(128,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 35','Lívia Kinessa','2022-12-05 10:34:26'),(129,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 35','Lívia Kinessa','2022-12-05 10:34:30'),(130,'Água Pura Grande 1.5L',600,'LUGAR 1',1,'Mesa 34','Lívia Kinessa','2022-12-05 10:34:41'),(131,'Água Pura Grande 1.5L',600,'LUGAR 1',1,'Mesa 34','Lívia Kinessa','2022-12-05 10:34:46'),(132,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 34','Lívia Kinessa','2022-12-05 10:34:54'),(133,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 34','Lívia Kinessa','2022-12-05 10:35:02'),(134,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 34','Lívia Kinessa','2022-12-05 10:35:06'),(135,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 34','Lívia Kinessa','2022-12-05 10:35:10'),(136,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-12-05 10:35:19'),(137,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-12-05 10:35:30'),(138,'Pica Pau',3500,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-12-05 10:35:35'),(139,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 33','Lívia Kinessa','2022-12-05 10:35:40'),(140,'SUMO NATURAL MIX',2000,'LUGAR 1',1,'Mesa 39','Lívia Kinessa','2022-12-05 10:36:32'),(141,'Vinho do Barril Copo',1000,'LUGAR 1',1,'Mesa 39','Lívia Kinessa','2022-12-05 10:36:36'),(142,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:36:48'),(143,'COMPAL GARRAFA',350,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:36:53'),(144,'COMPAL GARRAFA',350,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:36:58'),(145,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:37:02'),(146,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:37:12'),(147,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:37:21'),(148,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:37:27'),(149,'CALDO DE SIMPLES',2500,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:37:33'),(150,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:37:38'),(151,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:37:46'),(152,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-05 10:37:50'),(153,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:01:04'),(154,'Fino Normal',600,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:19:08'),(155,'Fino Normal',600,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:19:17'),(156,'Fino Normal',600,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:19:28'),(157,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:19:37'),(158,'Fino Normal',600,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:20:02'),(159,'Fino Normal',600,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:20:07'),(160,'Fino Normal',600,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:20:15'),(161,'COMPAL GARRAFA',350,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:20:25'),(162,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:20:43'),(163,'Fino Normal',600,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:20:57'),(164,'Fino Normal',600,'LUGAR 1',1,'Mesa 8','Lívia Kinessa','2022-12-05 17:21:12'),(165,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-12-05 18:51:24'),(166,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-12-05 18:51:29'),(167,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-12-05 18:51:33'),(168,'Choriço cazeiro',3500,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-05 19:38:04'),(169,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 5','Matilde Martins','2022-12-06 16:42:42'),(170,'Nocal Garrafa',300,'LUGAR 1',1,'Mesa 40','Lívia Kinessa','2022-12-06 18:51:07'),(171,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 39','Lívia Kinessa','2022-12-07 16:42:20'),(172,'COMPAL LATA',500,'LUGAR 1',1,'Mesa 39','Lívia Kinessa','2022-12-07 16:42:35'),(173,'SUMOL LATA',500,'LUGAR 1',1,'Mesa 4','Lívia Kinessa','2022-12-07 16:50:20'),(174,'Bitoque',5000,'LUGAR 1',1,'Mesa 4','Lívia Kinessa','2022-12-07 16:50:25'),(175,'SUMOL GARRAFA',350,'LUGAR 1',1,'Mesa 4','Lívia Kinessa','2022-12-07 16:50:29'),(176,'Fino Normal',600,'LUGAR 1',1,'Mesa 1','Lívia Kinessa','2022-12-07 17:14:36'),(177,'Coca Cola Lata',500,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-07 18:38:26'),(178,'SUMOL LATA',500,'LUGAR 1',1,'Mesa 4','Lívia Kinessa','2022-12-07 19:08:19'),(179,'Grellhada Mista',11000,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-07 19:13:40'),(180,'dose de arroz',700,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-07 19:13:49'),(181,'dose de arroz',700,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-07 19:13:55'),(182,'Cuca garrafa',300,'LUGAR 1',1,'Mesa 9','Lívia Kinessa','2022-12-07 19:28:19'),(183,'Fino Normal',600,'LUGAR 1',1,'Mesa 5','Lívia Kinessa','2022-12-07 19:50:33'),(184,'Eka Garrafa',300,'LUGAR 1',1,'Mesa 4','Lívia Kinessa','2022-12-08 13:47:40'),(185,'Fino Normal',600,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-08 16:55:13'),(186,'Fino Normal',600,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-08 16:55:19'),(187,'Fino Normal',600,'LUGAR 1',1,'Mesa 7','Lívia Kinessa','2022-12-08 16:55:35'),(188,'Coca Cola',300,'LUGAR 4',1,'Mesa 9','DVML COMERCIAL','2023-04-23 21:12:29'),(189,'Coca Colas',300,'LUGAR 1',1,'MESA 1','DVML COMERCIAL','2023-05-21 14:19:38'),(190,'Coca Colas',300,'LUGAR 2',1,'MESA 2','DVML COMERCIAL','2023-05-21 14:19:44'),(191,'torrada',3500,'LUGAR 2',1,'MESA 2','DVML COMERCIAL','2023-05-21 14:19:47'),(192,'torrada',3500,'LUGAR 2',1,'MESA 2','DVML COMERCIAL','2023-05-21 14:19:50'),(193,'torrada',3500,'LUGAR 6',1,'MESA 23','DVML COMERCIAL','2023-05-21 14:22:59'),(194,'Coca Colas',300,'LUGAR 2',1,'MESA 19','DVML COMERCIAL','2023-05-21 14:23:05'),(195,'torrada',3500,'LUGAR 2',1,'MESA 17','DVML COMERCIAL','2023-05-21 14:23:10'),(196,'torrada',3500,'LUGAR 1',1,'MESA 17','DVML COMERCIAL','2023-05-21 14:23:14'),(197,'torrada',3500,'LUGAR 8',1,'MESA 40','DVML COMERCIAL','2023-05-21 14:23:20'),(198,'torrada',3500,'LUGAR 5',1,'MESA 26','DVML COMERCIAL','2023-05-21 14:23:25'),(199,'torrada',3500,'LUGAR 2',1,'MESA 21','DVML COMERCIAL','2023-05-21 14:23:30'),(200,'torrada',3500,'LUGAR 1',1,'MESA 8','DVML COMERCIAL','2023-05-21 14:23:37'),(201,'torrada',3500,'LUGAR 2',1,'MESA 7','DVML COMERCIAL','2023-05-21 14:23:42'),(202,'sumol ananás',200,'LUGAR 1',1,'MESA 13','DVML COMERCIAL','2023-05-21 14:23:51'),(203,'Coca Colas',300,'LUGAR 1',1,'MESA 12','DVML COMERCIAL','2023-05-21 14:23:56'),(204,'torrada',3500,'LUGAR 1',1,'MESA 11','DVML COMERCIAL','2023-05-21 14:24:01'),(205,'torrada',3500,'LUGAR 1',1,'MESA 10','DVML COMERCIAL','2023-05-21 14:24:07'),(206,'torrada',3500,'LUGAR 1',1,'MESA 9','DVML COMERCIAL','2023-05-21 14:24:12'),(207,'torrada',3500,'LUGAR 1',1,'MESA 2','DVML COMERCIAL','2023-05-21 14:24:17'),(208,'Coca Colas',300,'LUGAR 6',1,'MESA 3','DVML COMERCIAL','2023-05-21 14:24:31'),(209,'torrada',3500,'LUGAR 4',1,'MESA 3','DVML COMERCIAL','2023-05-21 14:24:33'),(210,'torrada',3500,'LUGAR 2',1,'MESA 3','DVML COMERCIAL','2023-05-21 14:24:36'),(211,'torrada',3500,'LUGAR 2',1,'MESA 3','DVML COMERCIAL','2023-05-21 14:24:38'),(212,'torrada',3500,'LUGAR 2',1,'MESA 3','DVML COMERCIAL','2023-05-21 14:24:41'),(213,'Coarten',4500,'LUGAR 1',1,'MESA 1','Magda Pedro','2023-07-23 22:49:41'),(214,'Fato',1500,'LUGAR 1',1,'MESA 4','DVML COMERCIAL','2023-11-23 03:41:56'),(215,'Teste Produto com Iva',2000,'LUGAR 1',1,'MESA 4','DVML COMERCIAL','2023-11-29 09:24:04'),(216,'Fanta',300,'LUGAR 7',1,'MESA 1','DVML COMERCIAL','2023-12-19 00:18:43'),(217,'Pizza',5000,'LUGAR 2',1,'MESA 1','DVML COMERCIAL','2023-12-19 00:18:49'),(218,'Fanta',300,'LUGAR 1',1,'MESA 1','DVML COMERCIAL','2023-12-19 00:41:42'),(219,'Fanta',300,'LUGAR 8',1,'MESA 1','DVML COMERCIAL','2023-12-19 00:41:56'),(220,'Pizza',5000,'LUGAR 1',1,'MESA 1','DVML COMERCIAL','2023-12-19 00:42:03'),(221,'Pizza',5000,'LUGAR 2',1,'MESA 1','DVML COMERCIAL','2023-12-19 00:47:42'),(222,'Fanta',300,'LUGAR 2',1,'MESA 1','DVML COMERCIAL','2023-12-19 00:47:54'),(223,'Pizza',5000,'LUGAR 10',1,'MESA 1','DVML COMERCIAL','2023-12-19 00:48:17'),(224,'Pizza',5000,'LUGAR 1',1,'MESA 2','DVML COMERCIAL','2023-12-19 21:15:22'),(225,'Churrasco',1000,'LUGAR 3',1,'MESA 2','DVML COMERCIAL','2023-12-19 21:15:54'),(226,'Pizza',5000,'LUGAR 6',1,'MESA 2','DVML COMERCIAL','2023-12-20 13:09:37'),(227,'Churrasco',1000,'LUGAR 7',1,'MESA 2','DVML COMERCIAL','2023-12-20 13:10:15'),(228,'Churrasco',1000,'LUGAR 1',1,'MESA 2','DVML COMERCIAL','2023-12-20 13:10:29'),(229,'Pizza',5000,'LUGAR 6',1,'MESA 2','DVML COMERCIAL','2023-12-20 13:10:39'),(230,'Lavar Bata',3000,'2023-12-27',1,'MESA 16','Quissenda Jose','2023-12-28 12:39:57'),(231,'Lavar Vestido Simples',4500,'2023-12-27',1,'MESA 16','Quissenda Jose','2023-12-28 12:40:01'),(232,'Lavar Bata',3000,'2023-12-27',1,'MESA 16','Quissenda Jose','2023-12-28 12:40:04'),(233,'Lavar Bata',3000,'2023-12-26',1,'MESA 16','Quissenda Jose','2023-12-28 12:40:08'),(234,'Lavar Bata',3000,'2023-12-26',1,'MESA 16','Quissenda Jose','2023-12-28 12:40:11'),(235,'Lavar Bata',3000,'2023-12-26',1,'MESA 16','Quissenda Jose','2023-12-28 12:40:14'),(236,'Lavar Camisola Interior',900,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 12:55:10'),(237,'Lavar Macacão/jardineira',5500,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 12:59:03'),(238,'Lavar Fato de Linho H',4000,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 13:15:09'),(239,'Urgencia',6000,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 13:24:56'),(240,'Lavar Camisa Normal',1500,'2023-12-28',1,'MESA 16','Quissenda Jose','2023-12-28 15:37:56'),(241,'Lavar Tshirt normal',1500,'2023-12-28',1,'MESA 16','Quissenda Jose','2023-12-28 15:37:59'),(242,'Lavar Tshirt normal',1500,'2023-12-28',1,'MESA 16','Quissenda Jose','2023-12-28 15:38:03'),(243,'Lavar Camisola Interior H',900,'2023-12-28',1,'MESA 16','Quissenda Jose','2023-12-28 15:38:06'),(244,'Lavar Fato de Treino',2500,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 16:12:20'),(245,'Lavar Fato de Treino',2500,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 16:12:25'),(246,'Lavar Fato de Treino',2500,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 16:12:28'),(247,'Lavar Fato de Treino',2500,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 16:12:31'),(248,'Lavar Fato Social',3500,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 16:14:04'),(249,'Urgencia',1000,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 16:51:36'),(250,'Lavar Camisa Normal',1500,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 16:55:18'),(251,'Lavar Camisa Normal',1500,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 16:56:10'),(252,'Lavar Fato Social',3500,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 17:00:45'),(253,'Lavar Calça Social',1750,'2023-12-28',1,'MESA 16','Maria Cristina','2023-12-28 17:02:06'),(254,'Lavar Almofada Pequena',2000,'2023-12-29',1,'MESA 16','DVML COMERCIAL','2023-12-29 13:40:14'),(255,'Lavar Almofada Grande',3000,'2023-12-29',1,'MESA 16','DVML COMERCIAL','2023-12-29 13:40:33'),(256,'Casaco social de linho',2000,'2023-12-29',1,'MESA 16','DVML COMERCIAL','2023-12-29 13:40:43'),(257,'Lavar Bata',3000,'2023-12-29',1,'MESA 16','DVML COMERCIAL','2023-12-29 13:53:51'),(258,'Gravata',700,'2023-12-29',1,'MESA 16','DVML COMERCIAL','2023-12-29 14:00:47'),(259,'Urgencia',3800,'2023-12-29',1,'MESA 16','DVML COMERCIAL','2023-12-29 14:05:01'),(260,'Urgencia',3800,'2023-12-29',1,'MESA 16','DVML COMERCIAL','2023-12-29 14:08:11'),(261,'Lavar Toalhas de Banho Média',1800,'2023-12-29',1,'MESA 16','DVML COMERCIAL','2023-12-29 14:08:15'),(262,'Lavar Calça Normal',2000,'2023-12-29',1,'MESA 16','DVML COMERCIAL','2023-12-29 14:08:18'),(263,'Calça social femenina',1750,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 14:14:35'),(264,'Lavar Beca',3800,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 14:14:50'),(265,'Urgencia',750,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 14:28:00'),(266,'Lavar Camisa Normal',1500,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 14:28:04'),(267,'Lavar Vestido Simples',4500,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 14:29:08'),(268,'Lavar Camisa Normal',1500,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 14:29:16'),(269,'Gravata',700,'2023-12-29',1,'MESA 16','Maria Cristina','2023-12-29 15:05:10'),(270,'Lavar Colcha Casal',8000,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 16:31:06'),(271,'Lavar Fato Social',3500,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 18:43:34'),(272,'Lavar Camisa Normal',1500,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 18:51:37'),(273,'Urgencia',14000,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 19:00:40'),(274,'Lavar Fato Social',3500,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 19:00:46'),(275,'Urgencia',10500,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 19:00:51'),(276,'Lavar Beca',3800,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 19:07:29'),(277,'Urgencia',3800,'2023-12-29',1,'MESA 16','Flora Ngola','2023-12-29 19:07:56'),(278,'Lavar Fato Social',3500,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 09:04:15'),(279,'Lavar Blusão',1500,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 09:16:51'),(280,'Lavar Vestido Simples',4500,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 10:38:03'),(281,'Lavar Vestido Simples',4500,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 10:38:08'),(282,'Tshirt normal muito suja',1800,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 10:41:51'),(283,'Polo muito suja',1800,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 10:41:58'),(284,'Gravata',700,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 10:42:22'),(285,'Lavar Vestido Simples',4500,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 10:58:35'),(286,'Vestido de criança simples',1000,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 10:58:53'),(287,'Lavar Pantalona',2000,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 12:45:15'),(288,'Lavar Tshirt normal',1500,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 12:55:18'),(289,'Lavar Camisa de Ganga',1750,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 12:56:27'),(290,'Lavar Fato de Criança',2500,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 13:15:53'),(291,'Lavar Camisa de Linho',2000,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 13:16:51'),(292,'Lavar Calça Linho H',2200,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 13:17:31'),(293,'Lavar Macacão/jardineira',5500,'2023-12-30',1,'MESA 16','Quissenda Jose','2023-12-30 13:17:42'),(294,'Lavar Casaco Olimpico',2000,'2023-12-30',1,'MESA 16','Flora Ngola','2023-12-30 14:42:56'),(295,'Camisa social',1500,'2024-01-02',1,'MESA 16','Maria Cristina','2024-01-02 11:31:52'),(296,'Lavar Fato de Linho S',4000,'2024-01-02',1,'MESA 16','Maria Cristina','2024-01-02 11:32:56'),(297,'Lavar Casaco Social',1750,'2024-01-02',1,'MESA 16','Maria Cristina','2024-01-02 12:54:07'),(298,'Lavar Casaco Social',1750,'2024-01-02',1,'MESA 16','Maria Cristina','2024-01-02 12:54:32'),(299,'Lavar Casaco Social',1750,'2024-01-02',1,'MESA 16','Maria Cristina','2024-01-02 12:54:36'),(300,'Lavar Casaco Olimpico',2000,'2024-01-02',1,'MESA 16','Flora Ngola','2024-01-02 13:19:45'),(301,'Lavar Casaco Social',1750,'2024-01-02',1,'MESA 16','Flora Ngola','2024-01-02 14:05:42'),(302,'Lavar Casaco Social',1750,'2024-01-02',1,'MESA 16','Flora Ngola','2024-01-02 14:07:02'),(303,'Lavar Casaco Social',1750,'2024-01-02',1,'MESA 16','Flora Ngola','2024-01-02 14:07:36'),(304,'Lavar Polo',1500,'2024-01-02',1,'MESA 16','Flora Ngola','2024-01-02 14:55:00'),(305,'Lavar Camisa Normal',1500,'2024-01-02',1,'MESA 16','Flora Ngola','2024-01-02 14:55:37'),(306,'Lavar Vestido de Gala c/Lantenjolas',10500,'2024-01-03',1,'MESA 16','Quissenda Jose','2024-01-03 09:03:05'),(307,'Lavar Calça Ganga',1800,'2024-01-03',1,'MESA 16','Quissenda Jose','2024-01-03 09:05:08'),(308,'Lavar Casaco Social',1750,'2024-01-03',1,'MESA 16','Quissenda Jose','2024-01-03 13:32:03'),(309,'Lavar Lençois Casal 1',4000,'2024-01-03',1,'MESA 16','Flora Ngola','2024-01-03 15:24:57'),(310,'Camisa social',1500,'2024-01-03',1,'MESA 16','Flora Ngola','2024-01-03 19:30:14'),(311,'Lavar Bata',3000,'2024-01-04',1,'MESA 16','Quissenda Jose','2024-01-04 08:03:00'),(312,'Lavar Vestido de Baptizado(Simples)',7000,'2024-01-04',1,'MESA 16','Maria Cristina','2024-01-04 08:21:30'),(313,'Lavar Bata',3000,'2024-01-04',1,'MESA 16','Maria Cristina','2024-01-04 09:06:26'),(314,'Lavar Beca',3800,'2024-01-04',1,'MESA 16','Maria Cristina','2024-01-04 10:06:22'),(315,'Lavar Beca',3800,'2024-01-04',1,'MESA 16','Maria Cristina','2024-01-04 10:06:26'),(316,'Lavar Cortina Black Out p/metro',3000,'2024-01-04',1,'MESA 16','Maria Cristina','2024-01-04 14:08:53'),(317,'Lavar Cortina Linho p/metro',3200,'2024-01-04',1,'MESA 16','Maria Cristina','2024-01-04 14:20:05'),(318,'Lavar Fato Social',3500,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 08:03:18'),(319,'Lavar Blusa de Seda',2000,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 08:31:16'),(320,'Lavar Blusa de Seda',2000,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 08:31:25'),(321,'Lavar Blusa de Seda',2000,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 08:33:34'),(322,'Lavar Calça Normal',2000,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 08:45:25'),(323,'Tshirt normal muito suja',1800,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 09:08:45'),(324,'Lavar Tshirt normal',1500,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 09:08:48'),(325,'Lavar Tshirt normal',1500,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 09:12:01'),(326,'Lavar Tshirt normal',1500,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 09:12:07'),(327,'Urgencia',4750,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 09:12:17'),(328,'Urgencia',4750,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 09:12:32'),(329,'Lavar Bode',2000,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 09:30:27'),(330,'Lavar Bode',2000,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 09:30:30'),(331,'Lavar Bode',2000,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 09:30:33'),(332,'Lavar Bode',2000,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 09:30:36'),(333,'Lavar Bermuda de seda',2000,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 09:42:01'),(334,'Lavar Boxer',3500,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 09:42:26'),(335,'Blusa interior F',1000,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 09:42:47'),(336,'Lavar Vestido Simples',4500,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 10:28:58'),(337,'Lavar Fato Social',3500,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 10:29:38'),(338,'Lavar Tshirt normal',1500,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 10:37:41'),(339,'Lavar Calça Social',1750,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 10:38:38'),(340,'Lavar Calça Social',1750,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 10:38:41'),(341,'Lavar Beca',3800,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 11:12:20'),(342,'Engomar Bata',2000,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 11:12:27'),(343,'Engomar Bata',2000,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 11:12:31'),(344,'Lavar Casaco Social',1750,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 11:22:49'),(345,'Lavar Vestido de Noiva(Simples)',20000,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 11:22:53'),(346,'Lavar Calça Social',1750,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 11:22:56'),(347,'Lavar Blaser',2200,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 11:23:01'),(348,'Lavar Tshirt normal',1500,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 12:48:31'),(349,'Lavar Calça Social',1750,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 12:51:18'),(350,'Lavar Camisa Normal',1500,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 12:57:00'),(351,'Lavar Tshirt normal',1500,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 12:58:47'),(352,'Tshirt normal muito suja',1800,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 12:59:17'),(353,'Lavar Fato de Criança',2500,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 14:12:10'),(354,'Lavar Fato de Criança',2500,'2024-01-05',1,'MESA 16','Maria Cristina','2024-01-05 14:12:13'),(355,'Lavar Casaco Social',1750,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 14:40:43'),(356,'Lavar Cortina Black Out p/metro',3000,'2024-01-05',5,'MESA 16','Quissenda Jose','2024-01-05 14:48:29'),(357,'Lavar Cortina Black Out p/metro',3000,'2024-01-05',45,'MESA 16','Quissenda Jose','2024-01-05 14:55:14'),(358,'Lavar Tshirt normal',1500,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 15:17:42'),(359,'Calça social femenina',1750,'2024-01-05',1,'MESA 16','Quissenda Jose','2024-01-05 15:18:48'),(360,'Lavar Bata',3000,'2024-01-05',1,'MESA 16','Flora Ngola','2024-01-05 18:29:49'),(361,'Engomar Bata',2000,'2024-01-05',1,'MESA 16','Flora Ngola','2024-01-05 18:29:57'),(362,'Lavar Casaco Social',1750,'2024-01-06',1,'MESA 16','Maria Cristina','2024-01-06 10:03:53'),(363,'Lavar Camisa de Linho',2000,'2024-01-07',1,'MESA 16','Quissenda Jose','2024-01-07 11:27:32'),(364,'Lavar Calça Social',1750,'2024-01-07',1,'MESA 16','Quissenda Jose','2024-01-07 11:29:52'),(365,'Urgencia',6125,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 09:53:18'),(366,'Lavar Fato Social',3500,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 10:08:30'),(367,'Lavar Fato Social',3500,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 10:22:01'),(368,'Lavar Casaco Social',1750,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 10:23:07'),(369,'Lavar Calça Social',1750,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 10:46:02'),(370,'Lavar Calça Social',1750,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 11:48:25'),(371,'Lavar Calça Social',1750,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 11:48:33'),(372,'Urgencia',6125,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 12:33:47'),(373,'Lavar Camisa Normal',1500,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 13:26:59'),(374,'Urgencia',1500,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 13:35:29'),(375,'Camisa social',1500,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 14:30:55'),(376,'Lavar Vestido de Gala c/Lantenjolas',10500,'2024-01-08',1,'MESA 16','Quissenda Jose','2024-01-08 15:11:06'),(377,'Engomar vestido simples',3500,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 10:02:08'),(378,'Lavar Fato de Treino',2500,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 10:02:15'),(379,'Engomar vestido simples',3500,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 10:26:48'),(380,'Lavar Fato Social',3500,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 10:28:00'),(381,'Lavar Fato Social',3500,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 10:28:58'),(382,'LAVAR BERMUDA DE GANGA',1500,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 12:03:43'),(383,'Lavar Bermuda de seda',2000,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 12:03:49'),(384,'Lavar Veu de Noiva',4000,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 12:03:52'),(385,'Lavar Edredon Solteiro',8000,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 12:04:15'),(386,'Lavar Edredon Casal',9500,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 12:06:31'),(387,'Lavar Edredon Solteiro',8000,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 12:06:35'),(388,'Lavar Edredon Casal',9500,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 12:07:00'),(389,'Lavar Edredon Solteiro',8000,'2024-01-9',1,'MESA 16','Quissenda Jose','2024-01-09 12:07:07'),(390,'Lavar Gravata de Seda',1500,'2024-01-9',1,'MESA 16','Maria Cristina','2024-01-09 13:32:26'),(391,'POLOVER DE CRIANÇA',1500,'2024-01-9',1,'MESA 16','Maria Cristina','2024-01-09 13:32:31'),(392,'Lavar Chapeu',1500,'2024-01-9',1,'MESA 16','Maria Cristina','2024-01-09 13:32:34'),(393,'Lavar Blusa Simples',1500,'2024-01-9',1,'MESA 16','Maria Cristina','2024-01-09 15:04:51'),(394,'LAVAR BLUSA VESTIDO',2000,'2024-01-9',1,'MESA 16','Maria Cristina','2024-01-09 15:04:54'),(395,'LAVAR BERMUDA NORMAL',1500,'2024-01-9',1,'MESA 16','Maria Cristina','2024-01-09 15:04:58'),(396,'Lavar Calça Social',1750,'2024-01-9',1,'MESA 16','Flora Ngola','2024-01-09 18:27:56'),(397,'Lavar Camisa Normal',1500,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 07:35:39'),(398,'Lavar Toalhas de Banho Pequena',1300,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 09:18:04'),(399,'POLOVER DE CRIANÇA',1500,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 09:21:18'),(400,'Lavar Edredon Casal',9500,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 09:22:18'),(401,'Lavar Camisola Interior H',900,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 09:29:52'),(402,'Lavar Casaco Social',1750,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 10:35:47'),(403,'Lavar Calça Social',1750,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 11:04:10'),(404,'Lavar Fato Social',3500,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 11:14:08'),(405,'Lavar Fato Social',3500,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 11:14:15'),(406,'Lavar Fato Social',3500,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 11:14:18'),(407,'Lavar Calça Social',1750,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 12:00:24'),(408,'Lavar Calça Social',1750,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 12:00:58'),(409,'Lavar Fato Social',3500,'2024-01-10',1,'MESA 16','Quissenda Jose','2024-01-10 15:34:27'),(410,'Lavar Calça Social',1750,'2024-01-10',1,'MESA 16','Maria Cristina','2024-01-10 15:39:14'),(411,'Lavar Calça Social',1750,'2024-01-10',1,'MESA 16','Maria Cristina','2024-01-10 15:39:27'),(412,'Lavar Macacão/jardineira',5500,'2024-01-10',1,'MESA 16','Maria Cristina','2024-01-10 15:39:30'),(413,'Lavar Cortina Transp leves p/metro',2950,'2024-01-10',1,'MESA 16','Maria Cristina','2024-01-10 15:40:03'),(414,'Camisa social',1500,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 16:12:58'),(415,'Urgencia',2625,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 17:44:41'),(416,'Lavar Casaco Social',1750,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 17:45:07'),(417,'Engomar Bata',2000,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 18:20:03'),(418,'ENGOMAR BECA',2000,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 18:20:41'),(419,'ENGOMAR BECA',2000,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 18:20:45'),(420,'ENGOMAR BECA',2000,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 18:20:48'),(421,'ENGOMAR BECA',2000,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 18:20:53'),(422,'Lavar Beca',3800,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 18:20:58'),(423,'ENGOMAR BERMUDA DE ALGODÃ',1200,'2024-01-10',1,'MESA 16','Flora Ngola','2024-01-10 18:40:53'),(424,'Lavar Calça Social',1750,'2024-01-11',1,'MESA 16','Quissenda Jose','2024-01-11 08:44:45'),(425,'Lavar Calça Social',1750,'2024-01-11',1,'MESA 16','Quissenda Jose','2024-01-11 08:51:06'),(426,'Lavar Fato Social',3500,'2024-01-11',1,'MESA 16','Quissenda Jose','2024-01-11 08:53:25'),(427,'Casaco social de linho',2000,'2024-01-11',1,'MESA 16','Quissenda Jose','2024-01-11 10:51:23'),(428,'ENGOMAR CALÇA SOCIAL',1500,'2024-01-11',1,'MESA 16','Quissenda Jose','2024-01-11 11:18:59'),(429,'Lavar Casaco Social',1750,'2024-01-11',1,'MESA 16','Flora Ngola','2024-01-11 18:30:26'),(430,'Camisa social',1500,'2024-01-11',1,'MESA 16','Flora Ngola','2024-01-11 19:55:34'),(431,'Lavar Camisa Africana',1800,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 07:48:20'),(432,'Lavar Camisa Africana',1800,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 07:48:22'),(433,'Lavar Camisa Africana',1800,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 07:48:26'),(434,'Urgencia',5400,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 07:48:30'),(435,'Lavar Camisa Normal',1500,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 07:49:24'),(436,'Lavar Camisa de Linho',2000,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 07:49:27'),(437,'Lavar Calça Social',1750,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 11:15:19'),(438,'Lavar Camisa Normal',1500,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 11:17:14'),(439,'Camisa de seda',1700,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 11:18:14'),(440,'Lavar Camisa Normal',1500,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 11:19:20'),(441,'Lavar Vestido de Seda',8000,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 14:45:37'),(442,'Lavar Vestido de Seda',8000,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 14:45:41'),(443,'Lavar Vestido de Noiva(Simples)',20000,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 14:45:44'),(444,'Lavar Vestido Bubu',6500,'2024-01-12',1,'MESA 16','Maria Cristina','2024-01-12 14:45:54'),(445,'Lavar Casaco Social',1750,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 15:54:56'),(446,'Lavar Calça Social',1750,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:10:32'),(447,'Lavar Calça Social',1750,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:10:36'),(448,'Lavar Calça Social',1750,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:10:39'),(449,'Lavar Calça Social',1750,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:10:42'),(450,'Lavar Calça Social',1750,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:24'),(451,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:40'),(452,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:42'),(453,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:45'),(454,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:47'),(455,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:50'),(456,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:52'),(457,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:54'),(458,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:56'),(459,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:12:58'),(460,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:13:00'),(461,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:13:02'),(462,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:13:04'),(463,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:13:14'),(464,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:13:17'),(465,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:13:20'),(466,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:13:22'),(467,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:13:28'),(468,'Lavar Calça Ganga',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 16:13:31'),(469,'Polo muito suja',1800,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 18:32:27'),(470,'Lavar Blusa de ALGODÃO',1500,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 18:32:55'),(471,'LAVAR BLUSA VESTIDO',2000,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 18:33:14'),(472,'ENGOMAR BERMUDA LINHO',1200,'2024-01-12',1,'MESA 16','Flora Ngola','2024-01-12 18:33:32'),(473,'Urgencia',1750,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 07:36:50'),(474,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 07:37:32'),(475,'Lavar Camisa Normal',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:12:57'),(476,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:05'),(477,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:09'),(478,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:14'),(479,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:18'),(480,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:22'),(481,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:25'),(482,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:28'),(483,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:31'),(484,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:34'),(485,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:37'),(486,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:42'),(487,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:45'),(488,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:48'),(489,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:51'),(490,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:13:54'),(491,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:00'),(492,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:03'),(493,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:06'),(494,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:09'),(495,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:12'),(496,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:15'),(497,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:17'),(498,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:20'),(499,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:23'),(500,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:26'),(501,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:29'),(502,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:31'),(503,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:34'),(504,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:37'),(505,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:40'),(506,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:43'),(507,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:46'),(508,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:49'),(509,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:52'),(510,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:55'),(511,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:14:57'),(512,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:00'),(513,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:03'),(514,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:06'),(515,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:08'),(516,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:14'),(517,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:17'),(518,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:20'),(519,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:22'),(520,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:24'),(521,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:27'),(522,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:30'),(523,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:32'),(524,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:34'),(525,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:37'),(526,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:39'),(527,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:44'),(528,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:47'),(529,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:49'),(530,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:52'),(531,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:54'),(532,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:57'),(533,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:15:59'),(534,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:01'),(535,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:04'),(536,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:06'),(537,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:09'),(538,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:11'),(539,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:13'),(540,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:16'),(541,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:18'),(542,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:20'),(543,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:25'),(544,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:28'),(545,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:30'),(546,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:33'),(547,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:35'),(548,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:37'),(549,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:40'),(550,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:42'),(551,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:44'),(552,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:46'),(553,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:49'),(554,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:52'),(555,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:54'),(556,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:56'),(557,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:16:58'),(558,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:00'),(559,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:02'),(560,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:04'),(561,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:06'),(562,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:09'),(563,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:12'),(564,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:14'),(565,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:19'),(566,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:21'),(567,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:23'),(568,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:26'),(569,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:28'),(570,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:31'),(571,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:33'),(572,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:35'),(573,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:37'),(574,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:39'),(575,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:43'),(576,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:49'),(577,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:52'),(578,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:54'),(579,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:17:58'),(580,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:18:01'),(581,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:18:03'),(582,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:18:05'),(583,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:18:09'),(584,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:18:14'),(585,'Camisa social',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:18:17'),(586,'Lavar Casaco de Napa',4000,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:18:21'),(587,'Camisa de seda',1700,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:25:37'),(588,'Lavar Tshirt normal',1500,'2024-01-13',1,'MESA 16','Flora Ngola','2024-01-13 10:28:28'),(589,'Polo muito suja',1800,'2024-01-13',1,'MESA 16','Maria Cristina','2024-01-13 13:12:56'),(590,'Lavar Camisa Normal',1500,'2024-01-13',1,'MESA 16','Maria Cristina','2024-01-13 16:05:56'),(591,'Lavar Bata',3000,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:18:35'),(592,'Lavar Blusa de ALGODÃO',1500,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:18:38'),(593,'Lavar Camisa de Linho',2000,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:53:31'),(594,'LAVAR CAMISOLA VESTIDO',1700,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:01'),(595,'LAVAR CAMISOLA VESTIDO',1700,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:04'),(596,'LAVAR CAMISOLA VESTIDO',1700,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:08'),(597,'Lavar Polo',1500,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:23'),(598,'LAVAR CAMISOLA VESTIDO',1700,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:29'),(599,'LAVAR CAMISOLA VESTIDO',1700,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:33'),(600,'LAVAR CAMISOLA DE SEDA ',1500,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:39'),(601,'Lavar Tshirt normal',1500,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:42'),(602,'Polo muito suja',1800,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:45'),(603,'Lavar Polo',1500,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:51'),(604,'Tshirt normal muito suja',1800,'2024-01-14',1,'MESA 16','Flora Ngola','2024-01-14 12:54:54'),(605,'LAVAR CAMISOLA VESTIDO',1700,'2024-01-14',1,'MESA 16','Maria Cristina','2024-01-15 07:37:25'),(606,'LAVAR CAMISOLA NORMAL',1000,'2024-01-14',1,'MESA 16','Maria Cristina','2024-01-15 07:37:30'),(607,'Tshirt normal muito suja',1800,'2024-01-14',1,'MESA 16','Maria Cristina','2024-01-15 07:37:33'),(608,'Lavar Polo',1500,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 10:46:19'),(609,'Lavar Kimonu Judo',2000,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 10:48:41'),(610,'Lavar Tshirt normal',1500,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 10:56:22'),(611,'Lavar Calça Social',1750,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 10:56:27'),(612,'Lavar Lençois Casal 2',4500,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 13:15:00'),(613,'Lavar Lençois Casal 1',4000,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 13:15:26'),(614,'Lavar Lençois Casal 2',4500,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 13:15:34'),(615,'Lavar Lençois Casal 2',4500,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 13:16:23'),(616,'Lavar Tshirt normal',1500,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 13:17:39'),(617,'Lavar Toalhas de Banho Pequena',1300,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 13:19:58'),(618,'Lavar Fato Social',3500,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 13:41:38'),(619,'Lavar Macacão/jardineira',5500,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 14:00:46'),(620,'Lavar Macacão/jardineira',5500,'2024-01-15',1,'MESA 16','Maria Cristina','2024-01-15 14:00:50'),(621,'Lavar Tshirt normal',1500,'2024-01-15',1,'MESA 16','Flora Ngola','2024-01-15 17:04:49'),(622,'Camisa social',1500,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 08:58:50'),(623,'Camisa social',1500,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 09:22:39'),(624,'Camisa social',1500,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 09:22:44'),(625,'Camisa social',1500,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 09:22:49'),(626,'Camisa social',1500,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 09:22:52'),(627,'Lavar Camisa de Linho',2000,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 09:22:56'),(628,'Lavar Tshirt normal',1500,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 09:22:59'),(629,'Calça social femenina',1750,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 09:23:05'),(630,'Lavar Saia Simples',2500,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 09:23:08'),(631,'Urgencia',4000,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 18:07:10'),(632,'Lavar Vestido Simples',4500,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 18:08:59'),(633,'Lavar Vestido Fato',5250,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 18:09:03'),(634,'Engomar vestido simples',3500,'2024-01-16',1,'MESA 16','Flora Ngola','2024-01-16 18:09:07'),(635,'Lavar Tshirt normal',1500,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 09:30:19'),(636,'Camisa social',1500,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 10:11:01'),(637,'Camisa social',1500,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 10:11:22'),(638,'Lavar Fato Social',3500,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 14:10:34'),(639,'Lavar Fato Social',3500,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 14:10:40'),(640,'Camisa social',1500,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 14:13:37'),(641,'Lavar Casaco Social',1750,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 16:53:45'),(642,'Lavar Calça Social',1750,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 16:54:07'),(643,'Lavar Calça Social',1750,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 17:35:56'),(644,'Lavar Calça Social',1750,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 17:36:05'),(645,'Casaco social de linho',2000,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 17:36:16'),(646,'Lavar Colete',1500,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 17:36:21'),(647,'Camisa social',1500,'2024-01-17',1,'MESA 16','Flora Ngola','2024-01-17 17:37:28'),(648,'Camisa social',1500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 08:23:35'),(649,'Camisa social',1500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 11:01:30'),(650,'Lavar Cortina Black Out p/metro',3000,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 13:15:36'),(651,'Camisa social',1500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 14:14:56'),(652,'Lavar Camisa de Ganga',1750,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 14:15:00'),(653,'Lavar Vestido Simples',4500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 15:19:44'),(654,'Lavar Vestido Simples',4500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 15:19:54'),(655,'Lavar Vestido Simples',4500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 15:21:19'),(656,'Vestido simples de festa',7500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 16:16:21'),(657,'Urgencia',3750,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 16:16:24'),(658,'Lavar Fato Social',3500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 16:19:54'),(659,'Lavar Blaser',2200,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 16:25:45'),(660,'Camisa social',1500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 16:36:24'),(661,'Lavar Calça Social',1750,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:30:00'),(662,'Lavar Calça Social',1750,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:30:02'),(663,'Lavar Calça Social',1750,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:30:05'),(664,'Urgencia',1100,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:30:08'),(665,'Tshirt normal muito suja',1800,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:34:43'),(666,'Camisa social',1500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:34:46'),(667,'LAVAR SAIA CURTA ',2000,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:34:49'),(668,'Lavar Calça Linho S',2200,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:34:53'),(669,'Lavar Calça Linho H',2200,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:35:00'),(670,'Lavar Calça Linho H',2200,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 17:35:03'),(671,'Lavar Fato Social',3500,'2024-01-18',1,'MESA 16','Flora Ngola','2024-01-18 18:33:59'),(672,'LAVAR CAMISOLA VESTIDO',1700,'2024-01-19',1,'MESA 16','DVML COMERCIAL','2024-01-19 12:44:24'),(673,'Lavar Blusa Simples',1500,'2024-01-21',1,'MESA 16','DVML COMERCIAL','2024-01-19 12:45:58'),(674,'Urgencia',1500,'2024-01-19',1,'MESA 16','DVML COMERCIAL','2024-01-19 12:47:23'),(675,'Urgencia',1500,'2024-01-19',1,'MESA 16','DVML COMERCIAL','2024-01-19 12:47:29'),(676,'Urgencia',1500,'2024-01-19',1,'MESA 16','DVML COMERCIAL','2024-01-19 12:47:48'),(677,'Lavar Blusa de ALGODÃO',1500,'2024-01-19',1,'MESA 16','DVML COMERCIAL','2024-01-19 12:48:48'),(678,'Urgencia',1500,'2024-01-19',1,'MESA 16','Formanda','2024-01-19 14:15:14'),(679,'Camisa social',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 13:52:02'),(680,'Lavar Calção de Ganga',1700,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:40:57'),(681,'Lavar Polo',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:41:11'),(682,'Camisa social',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:02'),(683,'Camisa social',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:05'),(684,'Camisa social',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:07'),(685,'Lavar Camisa Normal',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:10'),(686,'Lavar Camisa Normal',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:17'),(687,'Lavar Camisa Normal',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:25'),(688,'Lavar Camisa Normal',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:28'),(689,'Lavar Calça Normal',2000,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:30'),(690,'Lavar Calça Normal',2000,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:33'),(691,'Lavar Camisa Normal',1500,'2024-01-20',1,'MESA 16','Mauricia João','2024-01-20 17:43:36'),(692,'Lavar Casaco Social',1750,'2024-01-22',1,'MESA 16','Maria Cristina','2024-01-22 08:46:03'),(693,'Camisa social',1500,'2024-01-22',1,'MESA 16','Maria Cristina','2024-01-22 14:48:46'),(694,'Polo muito suja',1800,'2024-01-22',1,'MESA 16','Maria Cristina','2024-01-22 14:53:19'),(695,'Lavar Calção Seda',1500,'2024-01-22',1,'MESA 16','Maria Cristina','2024-01-22 15:01:49'),(696,'Lavar Polo',1500,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 10:54:47'),(697,'Camisa social',1500,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 11:38:42'),(698,'Lavar Casaco Social',1750,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 11:42:25'),(699,'Camisa social',1500,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 11:53:29'),(700,'Camisa social',1500,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 11:55:32'),(701,'Camisa social',1500,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 11:56:16'),(702,'Lavar Camisa de Ganga',1750,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 11:57:38'),(703,'Tshirt normal muito suja',1800,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 12:31:27'),(704,'Polo muito suja',1800,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 12:32:54'),(705,'Lavar Tshirt normal',1500,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 12:34:20'),(706,'Lavar Tshirt normal',1500,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 14:23:13'),(707,'Lavar Calça Normal',2000,'2024-01-23',1,'MESA 16','Mauricia João','2024-01-23 14:29:10'),(708,'Urgencia',1750,'2024-01-24',1,'MESA 16','Flora Ngola','2024-01-24 15:55:09'),(709,'Lavar Casaco Social',1750,'2024-01-24',1,'MESA 16','Flora Ngola','2024-01-24 15:55:13'),(710,'Lavar Casaco Social',1750,'2024-01-24',1,'MESA 16','Flora Ngola','2024-01-24 15:55:17'),(711,'Lavar Casaco Social',1750,'2024-01-24',1,'MESA 16','Flora Ngola','2024-01-24 16:24:33'),(712,'Lavar Casaco Social',1750,'2024-01-24',1,'MESA 16','Flora Ngola','2024-01-24 16:24:36'),(713,'Lavar Casaco Social',1750,'2024-01-24',1,'MESA 16','Flora Ngola','2024-01-24 16:24:39'),(714,'Lavar Camisa Normal',1500,'2024-01-25',1,'MESA 16','Maria Cristina','2024-01-25 09:13:03'),(715,'Polo muito suja',1800,'2024-01-25',1,'MESA 16','Maria Cristina','2024-01-25 09:35:18'),(716,'Lavar Camisa Normal',1500,'2024-01-25',1,'MESA 16','Maria Cristina','2024-01-25 09:40:37'),(717,'Lavar Casaco Social',1750,'2024-01-25',1,'MESA 16','Maria Cristina','2024-01-25 13:32:32'),(718,'Lavar Casaco Social',1750,'2024-01-27',1,'MESA 16','Maria Cristina','2024-01-25 13:32:36'),(719,'Urgencia',3500,'2024-01-25',1,'MESA 16','Maria Cristina','2024-01-25 13:32:39'),(720,'Urgencia',3500,'2024-01-25',1,'MESA 16','Mauricia João','2024-01-25 13:43:22'),(721,'Lavar Blaser',2200,'2024-01-25',1,'MESA 16','Mauricia João','2024-01-25 16:25:04'),(722,'Lavar Blaser',2200,'2024-01-25',1,'MESA 16','Mauricia João','2024-01-25 16:25:08'),(723,'Lavar Blaser',2200,'2024-01-25',1,'MESA 16','Mauricia João','2024-01-25 16:29:01'),(724,'Lavar Edredon Solteiro',8000,'2024-01-26',1,'MESA 16','Maria Cristina','2024-01-26 09:26:25'),(725,'Lavar Tshirt normal',1500,'2024-01-26',1,'MESA 16','Maria Cristina','2024-01-26 09:32:40'),(726,'Lavar Tshirt normal',1500,'2024-01-26',1,'MESA 16','Maria Cristina','2024-01-26 09:33:10'),(727,'Tshirt normal muito suja',1800,'2024-01-26',1,'MESA 16','Maria Cristina','2024-01-26 09:33:16'),(728,'Lavar Fato Social',3500,'2024-01-26',1,'MESA 16','Maria Cristina','2024-01-26 12:02:10'),(729,'Lavar Camisa Normal',1500,'2024-01-26',1,'MESA 16','Maria Cristina','2024-01-26 12:02:12'),(730,'Urgencia',2500,'2024-01-26',1,'MESA 16','Maria Cristina','2024-01-26 12:02:14'),(731,'Lavar Bermuda DE LINHO',2000,'2024-01-26',1,'MESA 16','Maria Cristina','2024-01-26 15:00:16'),(732,'Lavar Bermuda DE LINHO',2000,'2024-01-26',1,'MESA 16','Maria Cristina','2024-01-26 15:00:19'),(733,'LAVAR TAPET P/METRO MEDIO',5000,'2024-01-26',1,'MESA 16','Flora Ngola','2024-01-26 15:28:34'),(734,'Lavar Camisa Branca (Muito Suja)',1700,'2024-01-26',1,'MESA 16','Flora Ngola','2024-01-26 16:39:33'),(735,'Lavar Fato Social',3500,'2024-01-26',1,'MESA 16','Flora Ngola','2024-01-26 16:45:34'),(736,'Camisa social',1500,'2024-01-26',1,'MESA 16','Flora Ngola','2024-01-26 16:45:40'),(737,'Camisa social',1500,'2024-01-26',1,'MESA 16','Flora Ngola','2024-01-26 16:45:44'),(738,'Camisa social',1500,'2024-01-26',1,'MESA 16','Flora Ngola','2024-01-26 16:46:03'),(739,'Camisa social',1500,'2024-01-26',1,'MESA 16','Flora Ngola','2024-01-26 16:48:27'),(740,'Lavar Fato Social',3500,'2024-01-26',1,'MESA 16','Flora Ngola','2024-01-26 17:50:21'),(741,'Lavar Calça Social',1750,'2024-01-27',1,'MESA 16','Maria Cristina','2024-01-27 11:33:33'),(742,'Lavar Blusa de Seda',2000,'2024-01-27',1,'MESA 16','Maria Cristina','2024-01-27 12:21:01'),(743,'LAVAR BLUSA VESTIDO',2000,'2024-01-27',1,'MESA 16','Maria Cristina','2024-01-27 12:22:05'),(744,'Camisa social',1500,'2024-01-27',1,'MESA 16','Maria Cristina','2024-01-27 12:42:28'),(745,'Casaco social de linho',2000,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:07:02'),(746,'Casaco social de linho',2000,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:07:04'),(747,'LAVAR SAIA CURTA ',2000,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:18:51'),(748,'LAVAR FATO SOCIAL',3500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:18:55'),(749,'LAVAR FATO SOCIAL',3500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:18:58'),(750,'Lavar Tshirt normal',1500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:19:01'),(751,'Lavar Tshirt normal',1500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:19:03'),(752,'Lavar Tshirt normal',1500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:19:06'),(753,'Lavar Tshirt normal',1500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:19:08'),(754,'LAVAR SAIA CURTA ',2000,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:19:10'),(755,'Lavar Blusa de Seda',2000,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:19:20'),(756,'Lavar Tshirt normal',1500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:19:23'),(757,'Lavar Polo',1500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:19:26'),(758,'Lavar Tshirt normal',1500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:19:34'),(759,'Lavar Calça Ganga',1800,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:21:39'),(760,'Lavar Vestido Simples',4500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:21:43'),(761,'Casaco social de linho',2000,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:21:47'),(762,'Lavar Macacão/jardineira',5500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 10:21:51'),(763,'Lavar Lençol Solteiro 2',3000,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 12:40:21'),(764,'Lavar Fato de Ganga',3500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 14:53:55'),(765,'Lavar Fato de Ganga',3500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 14:53:58'),(766,'Lavar Fato de Ganga',3500,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 14:54:01'),(767,'Urgencia',5700,'2024-01-30',1,'MESA 16','Maria Cristina','2024-01-29 14:54:20'),(768,'Lavar Vestido Africano',3800,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 14:54:22'),(769,'Lavar Vestido Africano',3800,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 14:54:25'),(770,'Lavar Vestido Africano',3800,'2024-01-29',1,'MESA 16','Maria Cristina','2024-01-29 14:54:29'),(771,'LAVAR FATO SOCIAL',3500,'2024-01-29',1,'MESA 16','Mauricia João','2024-01-29 17:11:21'),(772,'Lavar Fato de Linho S',4000,'2024-01-29',1,'MESA 16','Mauricia João','2024-01-29 17:11:46'),(773,'Camisa social',1500,'2024-01-30',1,'MESA 16','Maria Cristina','2024-01-30 14:04:37'),(774,'LAVAR FATO SOCIAL',3500,'2024-01-30',1,'MESA 16','Mauricia João','2024-01-30 20:21:39'),(775,'Camisa social',1500,'2024-01-30',1,'MESA 16','Mauricia João','2024-01-30 20:24:10'),(776,'Urgencia',3400,'2024-01-30',1,'MESA 16','Mauricia João','2024-01-30 20:29:38'),(777,'Lavar Camisa Branca (Muito Suja)',1700,'2024-01-30',1,'MESA 16','Mauricia João','2024-01-30 20:29:41'),(778,'Lavar Camisa Branca (Muito Suja)',1700,'2024-01-30',1,'MESA 16','Mauricia João','2024-01-30 20:29:45'),(779,'Camisa social',1500,'2024-01-30',1,'MESA 16','Maria Cristina','2024-01-31 09:57:19'),(780,'Lavar Bode',2000,'2024-01-31',1,'MESA 16','Maria Cristina','2024-01-31 10:26:52'),(781,'Lavar Blusa de Seda',2000,'2024-01-31',1,'MESA 16','Maria Cristina','2024-01-31 11:25:50'),(782,'Lavar Blusa de ALGODÃO',1500,'2024-01-31',1,'MESA 16','Maria Cristina','2024-01-31 11:26:00'),(783,'Lavar Casaco de Napa',4000,'2024-01-31',1,'MESA 16','Maria Cristina','2024-01-31 14:10:30'),(784,'Lavar Cortina Transp leves p/metro',2950,'2024-01-31',1,'MESA 16','Maria Cristina','2024-01-31 14:10:43'),(785,'Lavar Edredon Casal',9500,'2024-01-31',1,'MESA 16','Maria Cristina','2024-01-31 14:10:49'),(786,'Lavar Forro de Colchão',4500,'2024-01-31',1,'MESA 16','Maria Cristina','2024-01-31 14:10:54'),(787,'Lavar Colcha Solteiro',6500,'2024-01-31',1,'MESA 16','Maria Cristina','2024-01-31 14:10:59'),(788,'Lavar Gravata Normal',1500,'2024-02-01',1,'MESA 16','Maria Cristina','2024-02-01 11:48:21'),(789,'Lavar Gravata Normal',1500,'2024-02-01',1,'MESA 16','Maria Cristina','2024-02-01 11:48:25'),(790,'Lavar Tshirt normal',1500,'2024-02-01',1,'MESA 16','Maria Cristina','2024-02-01 14:00:58'),(791,'LAVAR CAMISOLA NORMAL',1000,'2024-02-01',1,'MESA 16','Eltone Ndongala','2024-02-01 14:24:31'),(792,'Lavar Tshirt normal',1500,'2024-02-01',1,'MESA 16','DVML COMERCIAL','2024-02-01 14:25:47'),(793,'Lavar Tshirt normal',1500,'2024-02-01',1,'MESA 16','Maria Cristina','2024-02-01 14:40:01'),(794,'Lavar Camisa de Linho',2000,'2024-02-01',1,'MESA 16','Maria Cristina','2024-02-01 14:40:04'),(795,'Lavar Casaco Social',1750,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:16:11'),(796,'Lavar Casaco Social',1750,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:19:51'),(797,'Lavar Casaco Social',1750,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:19:58'),(798,'Lavar Casaco Social',1750,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:20:26'),(799,'Lavar Casaco Social',1750,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:20:31'),(800,'  engomar Camisa Normal',1200,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:20:56'),(801,'  engomar Camisa Normal',1200,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:23:01'),(802,'Camisa social',1500,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:44:33'),(803,'Lavar Blaser',2200,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:45:53'),(804,'Camisa social',1500,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 16:45:58'),(805,'Lavar Casaco de Napa',4000,'2024-02-01',1,'MESA 16','Mauricia João','2024-02-01 18:34:56'),(806,'Lavar Fato de Ganga',3500,'2024-02-02',1,'MESA 16','Maria Cristina','2024-02-02 11:08:11'),(807,'Lavar Bata',3000,'2024-02-02',1,'MESA 16','Maria Cristina','2024-02-02 11:17:25'),(808,'Lavar Tapetes p/metros GRANDE',6000,'2024-02-02',1,'MESA 16','Maria Cristina','2024-02-02 11:17:29'),(809,'  engomar Camisa Normal',1200,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 14:04:13'),(810,'ENGOMAR CAMISA SOCIAL',1300,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 14:04:23'),(811,'  engomar Camisa Normal',1200,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 14:04:44'),(812,'Lav. COLETE',1500,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 16:38:22'),(813,'  engomar Camisa Normal',1200,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 16:38:25'),(814,'Lavar Tshirt normal',1500,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 16:41:37'),(815,'Lavar Tshirt normal',1500,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 16:41:40'),(816,'Camisa social',1500,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 16:48:40'),(817,'Lavar Tshirt normal',1500,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 16:56:08'),(818,'Lavar Casaco Social',1750,'2024-02-02',1,'MESA 16','Flora Ngola','2024-02-02 18:31:30'),(819,'ENGOMAR CAMISA SOCIAL',1300,'2024-02-03',1,'MESA 16','Maria Cristina','2024-02-03 12:54:01'),(820,'Camisa social',1500,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 13:22:58'),(821,'Lavar Calça Social',1750,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 13:24:01'),(822,'Lavar Toalhas de Banho Pequena',1300,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 13:25:18'),(823,'Lavar Tshirt normal',1500,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 13:35:09'),(824,'Lavar Tshirt normal',1500,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 13:37:14'),(825,'Camisa social',1500,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 14:05:32'),(826,'LAVAR FATO SOCIAL',3500,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 14:48:49'),(827,'Lavar Calça Social',1750,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 14:52:13'),(828,'Lavar Calça Social',1750,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 14:52:32'),(829,'Lavar Calça Normal',2000,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 14:53:36'),(830,'Lavar Calça Normal',2000,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 14:53:55'),(831,'Lavar Calça Normal',2000,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 14:54:15'),(832,'Camisa social',1500,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 14:55:00'),(833,'Camisa social',1500,'2024-02-03',1,'MESA 16','Flora Ngola','2024-02-03 14:58:37'),(834,'Lavar Tshirt normal',1500,'2024-02-04',1,'MESA 16','Maria Cristina','2024-02-04 10:50:43'),(835,'Lavar Calça Ganga',1800,'2024-02-04',1,'MESA 16','Maria Cristina','2024-02-04 10:50:45'),(836,'Camisa social',1500,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 13:35:59'),(837,'Lavar Vestido Simples',4500,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 13:45:12'),(838,'Lavar Macacão/jardineira',5500,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 13:45:45'),(839,'Lavar Casaco de Napa',4000,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:22:16'),(840,'POLOVER DE CRIANÇA',1500,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:22:34'),(841,'Lavar Vestido de Linho',6000,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:22:38'),(842,'Lavar Macacão/jardineira',5500,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:22:45'),(843,'Lavar Vestido Simples',4500,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:22:48'),(844,'Lavar Vestido Simples',4500,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:22:51'),(845,'Lavar Saia PLISSADA',3000,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:22:53'),(846,'Camisa social',1500,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:22:56'),(847,'Lavar Fato de Linho S',4000,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:22:59'),(848,'Lavar Blaser',2200,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:23:49'),(849,'Lavar Blaser',2200,'2024-02-05',1,'MESA 16','Maria Cristina','2024-02-05 14:24:11'),(850,'Lavar Vestido Simples',4500,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 15:18:40'),(851,'LAVAR FATO SOCIAL',3500,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 15:36:17'),(852,'Lavar Calça Social',1750,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 17:09:33'),(853,'Camisa social',1500,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 17:10:47'),(854,'ENGOMAR CALÇA SOCIAL',1500,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 17:11:45'),(855,'Lavar Fato de Noivo',5000,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 17:21:22'),(856,'Camisa social',1500,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 17:37:31'),(857,'Camisa social',1500,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 18:47:37'),(858,'Camisa social',1500,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 18:48:15'),(859,'Lavar Casaco Social',1750,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 18:55:48'),(860,'Lavar Casaco Social',1750,'2024-02-05',1,'MESA 16','Flora Ngola','2024-02-05 18:58:14'),(861,'Lav. COLETE',1500,'2024-02-06',1,'MESA 16','Eltone Ndongala','2024-02-06 09:25:33'),(862,'LAVAR FATO SOCIAL',3500,'2024-02-06',1,'MESA 16','Eltone Ndongala','2024-02-06 09:26:14'),(863,'LAVAR FATO SOCIAL',3500,'2024-02-06',1,'MESA 16','Eltone Ndongala','2024-02-06 09:26:18'),(864,'LAVAR FATO SOCIAL',3500,'2024-02-06',1,'MESA 16','Flora Ngola','2024-02-06 17:07:35'),(865,'ENGOMAR CALÇA SOCIAL',1500,'2024-02-07',1,'MESA 16','Eltone Ndongala','2024-02-07 08:47:14'),(866,'Lavar Polo',1500,'2024-02-07',1,'MESA 16','Flora Ngola','2024-02-07 16:51:29'),(867,'Lavar Calça Normal',2000,'2024-02-07',1,'MESA 16','Flora Ngola','2024-02-07 16:52:21'),(868,'Camisa social',1500,'2024-02-07',1,'MESA 16','Flora Ngola','2024-02-07 16:56:31'),(869,'Lavar Polo',1500,'2024-02-07',1,'MESA 16','Flora Ngola','2024-02-07 17:00:19'),(870,'POLOVER DE CRIANÇA',1500,'2024-02-07',1,'MESA 16','Flora Ngola','2024-02-07 19:53:06'),(871,'LAVAR FATO SOCIAL',3500,'2024-02-07',1,'MESA 16','Flora Ngola','2024-02-07 20:08:57'),(872,'Fato Africano',4000,'2024-02-07',1,'MESA 16','Flora Ngola','2024-02-07 20:22:15'),(873,'Lavar Tshirt normal',1500,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 10:07:38'),(874,'Lavar Tshirt normal',1500,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 10:16:08'),(875,'Urgencia',1750,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 10:48:09'),(876,'Lavar Camisa Branca (Muito Suja)',1700,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 11:03:39'),(877,'Lavar Camisa Branca (Muito Suja)',1700,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 11:05:03'),(878,'Lavar Tshirt normal',1500,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 11:08:22'),(879,'Urgencia',1700,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 13:21:46'),(880,'LAVAR CAMISOLA VESTIDO',1700,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 13:21:50'),(881,'Vestido simples de festa',7500,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 14:32:52'),(882,'Vestido simples de festa',7500,'2024-02-08',1,'MESA 16','Maria Cristina','2024-02-08 14:33:46'),(883,'Camisa social',1500,'2024-02-08',1,'MESA 16','Flora Ngola','2024-02-08 18:38:42'),(884,'Camisa social',1500,'2024-02-08',1,'MESA 16','Flora Ngola','2024-02-08 18:40:04'),(885,'Lavar Fronha',1300,'2024-02-08',1,'MESA 16','Flora Ngola','2024-02-08 20:38:02'),(886,'Calça social femenina',1750,'2024-02-9',1,'MESA 16','Maria Cristina','2024-02-09 12:10:38'),(887,'Lavar Calça Social',1750,'2024-02-9',1,'MESA 16','Maria Cristina','2024-02-09 13:25:46'),(888,'Lavar Calça Social',1750,'2024-02-9',1,'MESA 16','Maria Cristina','2024-02-09 13:26:42'),(889,'Urgencia',1700,'2024-02-9',1,'MESA 16','Flora Ngola','2024-02-09 16:28:37'),(890,'engomar Fato Social',1700,'2024-02-9',1,'MESA 16','Flora Ngola','2024-02-09 16:28:43'),(891,'  engomar Camisa Normal',1200,'2024-02-9',1,'MESA 16','Flora Ngola','2024-02-09 16:29:13'),(892,'Urgencia',1750,'2024-02-9',1,'MESA 16','Flora Ngola','2024-02-09 18:12:30'),(893,'Lavar Tshirt normal',1500,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 08:59:28'),(894,'Lavar Cortina Linho Fino p/metro',3500,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 09:12:07'),(895,'Lavar Cortina Linho Fino p/metro',3500,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 10:41:59'),(896,'Urgencia',3500,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 10:42:06'),(897,'Urgencia',7500,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 10:45:18'),(898,'Lavar Blaser',2200,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 11:04:40'),(899,'Lavar Calça Normal',2000,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 11:07:10'),(900,'LAVAR SAIA DE NAPA',5000,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 11:08:51'),(901,'POLOVER DE CRIANÇA',1500,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 11:08:54'),(902,'POLOVER DE CRIANÇA',1500,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 11:08:58'),(903,'Lavar Beca',3800,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 11:10:38'),(904,'Lavar Bode',2000,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 11:10:41'),(905,'LAVAR FATO SOCIAL',3500,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 11:26:18'),(906,'Urgencia',1750,'2024-02-10',1,'MESA 16','Eltone Ndongala','2024-02-10 12:28:28'),(907,'LAVAR FATO SOCIAL',3500,'2024-02-10',1,'MESA 16','Flora Ngola','2024-02-10 13:44:58'),(908,'LAVAR FATO SOCIAL',3500,'2024-02-10',1,'MESA 16','Flora Ngola','2024-02-10 13:48:37'),(909,'Lavar Calça Linho H',2200,'2024-02-10',1,'MESA 16','Flora Ngola','2024-02-10 13:50:52'),(910,'Lavar Calça Linho H',2200,'2024-02-10',1,'MESA 16','Flora Ngola','2024-02-10 13:50:59'),(911,'Lavar Colete',1500,'2024-02-10',1,'MESA 16','Flora Ngola','2024-02-10 15:30:43'),(912,'Lavar Colete',1500,'2024-02-10',1,'MESA 16','Flora Ngola','2024-02-10 15:30:48'),(913,'Lavar Colete',1500,'2024-02-10',1,'MESA 16','Flora Ngola','2024-02-10 15:30:53'),(914,'Calça social femenina',1750,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 12:10:41'),(915,'Lavar Calça Normal',2000,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 12:10:49'),(916,'Lavar Calça Social',1750,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 12:10:55'),(917,'Calça social femenina',1750,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 12:13:23'),(918,'Lavar Calça Social',1750,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 12:34:28'),(919,'Lavar Calça Social',1750,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 12:34:31'),(920,'Calça social femenina',1750,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 12:34:33'),(921,'Calça social femenina',1750,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 12:34:36'),(922,'Lavar Saia Simples',2500,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 13:43:42'),(923,'Lavar Saia Simples',2500,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 13:43:45'),(924,'Lavar Saia PLISSADA',3000,'2024-02-12',1,'MESA 16','Eltone Ndongala','2024-02-12 13:43:51'),(925,'Camisa social',1500,'2024-02-13',1,'MESA 16','Flora Ngola','2024-02-13 09:13:44'),(926,'Urgencia',1500,'2024-02-14',1,'MESA 16','Eltone Ndongala','2024-02-14 10:10:19'),(927,'Lavar Calça Normal',2000,'2024-02-14',1,'MESA 16','Eltone Ndongala','2024-02-14 11:23:26'),(928,'Lavar Camisa de Linho',2000,'2024-02-14',1,'MESA 16','Eltone Ndongala','2024-02-14 11:23:29'),(929,'Lavar Blaser',2200,'2024-02-14',1,'MESA 16','Flora Ngola','2024-02-14 14:10:23'),(930,'LAVAR FATO SOCIAL',3500,'2024-02-14',1,'MESA 16','Flora Ngola','2024-02-14 16:47:55'),(931,'Fato Africano',4000,'2024-02-14',1,'MESA 16','Flora Ngola','2024-02-14 16:49:50'),(932,'Lavar Tapetes p/metros GRANDE',6000,'2024-02-14',1,'MESA 16','Flora Ngola','2024-02-14 17:53:45'),(933,'LAVAR CASACO DE CRIANÇA',1250,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:56:19'),(934,'Lavar Cortina de Cozinha p/metro',2900,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:56:23'),(935,'Lavar Cortina Black Out p/metro',3000,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:56:26'),(936,'Lavar Cortina Solteira(impar)  p/metro',3300,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:56:30'),(937,'Lavar Cortina Solteira(impar)  p/metro',3300,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:56:32'),(938,'Lavar Cortina Transp leves p/metro',2950,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:56:36'),(939,'Lavar Cortina Transp leves p/metro',2950,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:56:39'),(940,'Lavar Casaco Criança',1250,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:56:42'),(941,'Lavar Casaco Criança',1250,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:56:45'),(942,'LAVAR FATO SOCIAL',3500,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:59:15'),(943,'Lavar Blaser',2200,'2024-02-15',1,'MESA 16','Eltone Ndongala','2024-02-15 15:59:32'),(944,'Lavar Lençois Casal 1',4000,'2024-02-15',1,'MESA 16','Flora Ngola','2024-02-15 16:19:32'),(945,'Lavar Lençois Casal 1',4000,'2024-02-15',1,'MESA 16','Flora Ngola','2024-02-15 16:19:34'),(946,'Lavar Toalhas de Banho Média',1800,'2024-02-15',1,'MESA 16','Flora Ngola','2024-02-15 16:19:37'),(947,'Camisa social',1500,'2024-02-15',1,'MESA 16','Flora Ngola','2024-02-15 16:25:05'),(948,'Lavar Calça Normal',2000,'2024-02-15',1,'MESA 16','Flora Ngola','2024-02-15 16:25:45'),(949,'Lavar Tshirt normal',1500,'2024-02-15',1,'MESA 16','Flora Ngola','2024-02-15 16:51:27'),(950,'Lavar Casaco Social',1750,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:36:29'),(951,'Lavar Blaser',2200,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:36:40'),(952,'Polo muito suja',1800,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:36:47'),(953,'Tshirt normal muito suja',1800,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:36:50'),(954,'Lavar Tshirt normal',1500,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:36:58'),(955,'Lavar Polo',1500,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:37:04'),(956,'Polo muito suja',1800,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:37:07'),(957,'Lavar Camisa Branca (Muito Suja)',1700,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:37:10'),(958,'Camisa social',1500,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:37:13'),(959,'LAVAR FATO SOCIAL',3500,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:37:16'),(960,'LAVAR FATO SOCIAL',3500,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 09:37:19'),(961,'Camisa de seda',1700,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 10:06:15'),(962,'Lavar Calça Social',1750,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 10:06:17'),(963,'Lavar Calça Social',1750,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 10:06:22'),(964,'Lavar Casaco Social',1750,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 10:06:24'),(965,'Urgencia',6387.5,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 12:40:49'),(966,'Urgencia',6387.5,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 12:41:23'),(967,'Urgencia',6387.5,'2024-02-16',1,'MESA 16','Eltone Ndongala','2024-02-16 12:41:25'),(968,'Urgencia',12775,'2024-02-16',1,'MESA 16','Flora Ngola','2024-02-16 12:43:46'),(969,'Urgencia',12775,'2024-02-16',1,'MESA 16','Flora Ngola','2024-02-16 13:01:18'),(970,'Camisa social',1500,'2024-02-16',1,'MESA 16','Flora Ngola','2024-02-16 13:07:22'),(971,'Lavar Polo',1500,'2024-02-16',1,'MESA 16','Flora Ngola','2024-02-16 13:08:19'),(972,'LAVAR CASACO DE CRIANÇA',1250,'2024-02-16',1,'MESA 16','DVML COMERCIAL','2024-02-16 13:48:32'),(973,'EDREDON KWIN',9500,'2024-02-16',1,'MESA 16','Flora Ngola','2024-02-16 17:00:44'),(974,'Lavar Blaser',2200,'2024-02-16',1,'MESA 16','Flora Ngola','2024-02-16 17:00:47'),(975,'Lavar Colcha Casal',8000,'2024-02-16',1,'MESA 16','Flora Ngola','2024-02-16 17:18:59'),(976,'Lavar Edredon Casal',9500,'2024-02-16',1,'MESA 16','Flora Ngola','2024-02-16 17:19:02'),(977,'Lavar Edredon Casal',9500,'2024-02-16',1,'MESA 16','Flora Ngola','2024-02-16 17:19:20'),(978,'Camisa social',1500,'2024-02-17',1,'MESA 16','Eltone Ndongala','2024-02-17 09:13:01'),(979,'Lavar Calça Normal',2000,'2024-02-17',1,'MESA 16','Eltone Ndongala','2024-02-17 10:41:56'),(980,'COLCHA CASAL',8000,'2024-02-17',1,'MESA 16','Eltone Ndongala','2024-02-17 10:44:22'),(981,'ENGOMAR CALÇA DE LNHO S',1500,'2024-02-17',1,'MESA 16','Eltone Ndongala','2024-02-17 12:59:54'),(982,'Camisa social',1500,'2024-02-17',1,'MESA 16','Flora Ngola','2024-02-17 15:13:13'),(983,'Camisa social',1500,'2024-02-17',1,'MESA 16','Flora Ngola','2024-02-17 15:13:30'),(984,'Camisa social',1500,'2024-02-17',1,'MESA 16','Flora Ngola','2024-02-17 17:00:46'),(985,'Lavar Casaco Social',1750,'2024-02-17',1,'MESA 16','Flora Ngola','2024-02-17 17:01:22'),(986,'Lavar Blaser',2200,'2024-02-17',1,'MESA 16','Flora Ngola','2024-02-17 17:32:28'),(987,'LAVAR FATO SOCIAL',3500,'2024-02-17',1,'MESA 16','Flora Ngola','2024-02-17 18:54:22'),(988,'LAVAR FATO SOCIAL',3500,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-17 18:54:29'),(989,'LAVAR FATO SOCIAL',3500,'2024-02-17',1,'MESA 16','Flora Ngola','2024-02-17 18:54:32'),(990,'LAVAR FATO SOCIAL',3500,'2024-02-17',1,'MESA 16','Flora Ngola','2024-02-18 08:17:00'),(991,'LAVAR FATO SOCIAL',3500,'2024-02-17',1,'MESA 16','Flora Ngola','2024-02-18 08:17:09'),(992,'LAVAR FATO SOCIAL',3500,'2024-02-20',1,'MESA 16','Flora Ngola','2024-02-18 08:17:16'),(993,'LAVAR FATO SOCIAL',3500,'2024-02-18',1,'MESA 16','Flora Ngola','2024-02-18 08:17:26'),(994,'LAVAR FATO SOCIAL',3500,'2024-02-18',1,'MESA 16','Flora Ngola','2024-02-18 08:26:21'),(995,'LAVAR FATO SOCIAL',3500,'2024-02-18',1,'MESA 16','Flora Ngola','2024-02-18 08:28:34'),(996,'LAVAR FATO SOCIAL',3500,'2024-02-18',1,'MESA 16','Flora Ngola','2024-02-18 08:28:38'),(997,'ENGOMAR CAMISA SOCIAL',1300,'2024-02-18',1,'MESA 16','Flora Ngola','2024-02-18 12:54:26'),(998,'Camisa social',1500,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 08:02:19'),(999,'LAVAR FATO SOCIAL',3500,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 10:11:12'),(1000,'LAVAR FATO SOCIAL',3500,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 10:11:15'),(1001,'Lavar Blusa de ALGODÃO',1500,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 10:21:24'),(1002,'Lavar Tshirt normal',1500,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 10:22:23'),(1003,'Lavar Tshirt normal',1500,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 10:22:25'),(1004,'Lavar Vestido Simples',4500,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 10:25:24'),(1005,'Vestido de criança simples',1000,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 10:25:46'),(1006,'Lavar Saia DE SEDA',5000,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 10:27:23'),(1007,'LAVAR SAIA DE NOIVA CURTA ',5000,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 10:28:33'),(1008,'LAVAR FATO SOCIAL',3500,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 11:02:44'),(1009,'Lavar Vestido Simples',4500,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 12:49:35'),(1010,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 12:49:39'),(1011,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 12:49:41'),(1012,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 12:50:35'),(1013,'Lavar Blusa de Seda',2000,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 12:51:53'),(1014,'Lavar Lençol Solteiro 1',3000,'2024-02-19',1,'MESA 16','Eltone Ndongala','2024-02-19 13:01:24'),(1015,'LAVAR FATO SOCIAL',3500,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-19 14:14:31'),(1016,'Lavar Cortina de Cozinha p/metro',2900,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-19 16:21:51'),(1017,'Lavar Cortina de Cozinha p/metro',2900,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-19 16:21:54'),(1018,'Lavar Cortina de Cozinha p/metro',2900,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-19 16:25:39'),(1019,'Lavar Cortina de Cozinha p/metro',2900,'2024-02-21',1,'MESA 16','Flora Ngola','2024-02-19 16:25:42'),(1020,'CORTINAS BLACK OUT',14500,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-19 16:26:08'),(1021,'CORTINAS BLACK OUT',14500,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-19 16:26:11'),(1022,'CORTINAS BLACK OUT',14500,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-19 16:26:15'),(1023,'CORTINAS BLACK OUT',14500,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-19 16:26:17'),(1024,'CORTINAS BLACK OUT',14500,'2024-02-19',1,'MESA 16','Flora Ngola','2024-02-19 16:26:20'),(1025,'Lavar Calça Ganga',1800,'2024-02-20',1,'MESA 16','Eltone Ndongala','2024-02-20 08:42:08'),(1026,'Lavar Toalhas de Banho Grande',2500,'2024-02-20',1,'MESA 16','Eltone Ndongala','2024-02-20 08:44:42'),(1027,'Lavar Vestido de Napa',6500,'2024-02-20',1,'MESA 16','Eltone Ndongala','2024-02-20 09:38:58'),(1028,'Lavar Vestido de Gala Simples',7500,'2024-02-20',1,'MESA 16','Eltone Ndongala','2024-02-20 09:40:44'),(1029,'Lavar Calça Linho H',2200,'2024-02-20',1,'MESA 16','Eltone Ndongala','2024-02-20 11:34:56'),(1030,'Lavar Calça Ganga',1800,'2024-02-20',1,'MESA 16','Eltone Ndongala','2024-02-20 11:35:01'),(1031,'Lavar Calça Ganga',1800,'2024-02-20',1,'MESA 16','Eltone Ndongala','2024-02-20 11:39:46'),(1032,'LAVAR FATO SOCIAL',3500,'2024-02-20',1,'MESA 16','Eltone Ndongala','2024-02-20 13:15:40'),(1033,'Camisa social',1500,'2024-02-20',1,'MESA 16','Flora Ngola','2024-02-20 13:45:02'),(1034,'Camisa social',1500,'2024-02-20',1,'MESA 16','Flora Ngola','2024-02-20 17:49:00'),(1035,'Lavar Calça Linho H',2200,'2024-02-20',1,'MESA 16','Flora Ngola','2024-02-20 17:51:51'),(1036,'Lavar Tshirt normal',1500,'2024-02-20',1,'MESA 16','Flora Ngola','2024-02-20 17:51:57'),(1037,'Tshirt normal muito suja',1800,'2024-02-20',1,'MESA 16','Flora Ngola','2024-02-20 17:52:15'),(1038,'LAVAR FATO SOCIAL',3500,'2024-02-21',1,'MESA 16','Eltone Ndongala','2024-02-21 10:02:25'),(1039,'LAVAR FATO SOCIAL',3500,'2024-02-21',1,'MESA 16','Eltone Ndongala','2024-02-21 10:04:54'),(1040,'LAVAR FATO SOCIAL',3500,'2024-02-21',1,'MESA 16','Eltone Ndongala','2024-02-21 10:22:14'),(1041,'LAVAR FATO SOCIAL',3500,'2024-02-21',1,'MESA 16','Eltone Ndongala','2024-02-21 11:19:53'),(1042,'Lavar Blaser',2200,'2024-02-21',1,'MESA 16','Eltone Ndongala','2024-02-21 11:30:19'),(1043,'Lavar Toalhas de Banho Grande',2500,'2024-02-21',1,'MESA 16','Eltone Ndongala','2024-02-21 12:12:17'),(1044,'Camisa social',1500,'2024-02-21',1,'MESA 16','Flora Ngola','2024-02-21 16:33:06'),(1045,'Lavar Casaco Social',1750,'2024-02-21',1,'MESA 16','Flora Ngola','2024-02-21 16:33:09'),(1046,'Lavar Casaco Social',1750,'2024-02-21',1,'MESA 16','Flora Ngola','2024-02-21 16:33:11'),(1047,'Lavar Casaco Social',1750,'2024-02-21',1,'MESA 16','Flora Ngola','2024-02-21 16:33:13'),(1048,'LAVAR FATO SOCIAL',3500,'2024-02-22',1,'MESA 16','Eltone Ndongala','2024-02-22 10:19:36'),(1049,'LAVAR FATO SOCIAL',3500,'2024-02-22',1,'MESA 16','Eltone Ndongala','2024-02-22 10:19:39'),(1050,'LAVAR FATO SOCIAL',3500,'2024-02-24',1,'MESA 16','Eltone Ndongala','2024-02-22 10:19:43'),(1051,'Lavar Vestido Simples',4500,'2024-02-22',1,'MESA 16','Flora Ngola','2024-02-22 18:34:38'),(1052,'Vestido simples de festa',7500,'2024-02-22',1,'MESA 16','Flora Ngola','2024-02-22 18:34:44'),(1053,'Vestido simples de festa',7500,'2024-02-22',1,'MESA 16','Flora Ngola','2024-02-22 18:34:47'),(1054,'LAVAR FATO SOCIAL',3500,'2024-02-23',1,'MESA 16','Eltone Ndongala','2024-02-23 07:44:16'),(1055,'Lavar Calção Normal',1500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 10:54:14'),(1056,'Lavar Toalhas de Banho Grande',2500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 11:21:33'),(1057,'Lavar Casaco Olimpico',2000,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 14:31:19'),(1058,'Lavar Casaco Olimpico',2000,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 14:31:22'),(1059,'Lavar Calção Normal',1500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 14:47:42'),(1060,'Lavar Toalhas de Banho Grande',2500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 14:48:29'),(1061,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 14:49:45'),(1062,'Lavar Veu de Noiva',4000,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 14:50:59'),(1063,'LAVAR FATO DE BANHO',3500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 14:51:17'),(1064,'engomar Fato Social',1700,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 15:27:50'),(1065,'Urgencia',1700,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 15:27:53'),(1066,'Urgencia',4100,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 16:06:56'),(1067,'Camisa social',1500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 16:15:14'),(1068,'Camisa social',1500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 16:16:29'),(1069,'Camisa social',1500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 16:26:42'),(1070,'Camisa social',1500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 16:36:21'),(1071,'Camisa social',1500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 16:52:19'),(1072,'Camisa social',1500,'2024-02-23',1,'MESA 16','Flora Ngola','2024-02-23 20:00:04'),(1073,'Lavar Calça Ganga',1800,'2024-02-24',1,'MESA 16','Flora Ngola','2024-02-24 12:35:42'),(1074,'Lavar Toalhas de Banho Média',1800,'2024-02-24',1,'MESA 16','Flora Ngola','2024-02-24 12:41:43'),(1075,'Vestido simples de festa',7500,'2024-02-24',1,'MESA 16','Flora Ngola','2024-02-24 15:18:53'),(1076,'LAVAR FATO SOCIAL',3500,'2024-02-24',1,'MESA 16','Flora Ngola','2024-02-24 15:42:16'),(1077,'Calça social femenina',1750,'2024-02-24',1,'MESA 16','Flora Ngola','2024-02-24 15:55:45'),(1078,'Gravata',700,'2024-02-24',1,'MESA 16','Flora Ngola','2024-02-24 17:02:39'),(1079,'LAVAR FATO SOCIAL',3500,'2024-02-25',1,'MESA 16','Flora Ngola','2024-02-25 10:45:12'),(1080,'Lavar Lençois Casal 1',4000,'2024-02-26',1,'MESA 16','Flora Ngola','2024-02-26 12:16:35'),(1081,'Lavar Casaco Social',1750,'2024-02-26',1,'MESA 16','Flora Ngola','2024-02-26 18:02:18'),(1082,'COLCHA CASAL',8000,'2024-02-26',1,'MESA 16','Flora Ngola','2024-02-26 20:11:01'),(1083,'Fato Africano',4000,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 08:10:36'),(1084,'Fato Africano',4000,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 08:10:39'),(1085,'Camisa social',1500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 09:26:14'),(1086,'Camisa social',1500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 09:40:19'),(1087,'Lavar Gabardina/Sobretudo',3500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 09:52:07'),(1088,'Lavar Forro de Cama/Colchão',5000,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 09:59:49'),(1089,'Lavar Edredon Casal',9500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 10:00:07'),(1090,'Lavar Chapeu',1500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 10:01:30'),(1091,'Lavar Toalhas de Banho Grande',2500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 10:02:12'),(1092,'Lavar Toalhas de Banho Grande',2500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 10:02:16'),(1093,'Lavar Forro de Colchão',4500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 10:02:27'),(1094,'Lavar Forro de Cama/Colchão',5000,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 10:02:31'),(1095,'Camisa social',1500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 14:21:03'),(1096,'Camisa social',1500,'2024-02-27',1,'MESA 16','Eltone Ndongala','2024-02-27 14:21:44'),(1097,'Lavar Calça Social',1750,'2024-02-27',1,'MESA 16','Flora Ngola','2024-02-27 20:11:05'),(1098,'Lavar Blaser',2200,'2024-02-28',1,'MESA 16','Quissenda Jose','2024-02-28 10:10:26'),(1099,'Lavar Vestido Bubu',6500,'2024-02-28',1,'MESA 16','Quissenda Jose','2024-02-28 12:58:17'),(1100,'Lavar Vestido Bubu',6500,'2024-02-28',1,'MESA 16','Quissenda Jose','2024-02-28 12:58:20'),(1101,'LAVAR FATO SOCIAL',3500,'2024-02-28',1,'MESA 16','Flora Ngola','2024-02-28 16:35:42'),(1102,'LAVAR FATO SOCIAL',3500,'2024-02-28',1,'MESA 16','Flora Ngola','2024-02-28 16:35:45'),(1103,'Camisa social',1500,'2024-02-28',1,'MESA 16','Flora Ngola','2024-02-28 16:50:08'),(1104,'Lavar Blaser',2200,'2024-02-28',1,'MESA 16','Flora Ngola','2024-02-28 19:15:42'),(1105,'Lavar Blaser',2200,'2024-02-28',1,'MESA 16','Flora Ngola','2024-02-28 19:15:45'),(1106,'Lavar Calça Social',1750,'2024-02-29',1,'MESA 16','Quissenda Jose','2024-02-29 09:46:23'),(1107,'Camisa social',1500,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 15:04:50'),(1108,'Urgencia',3500,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 16:09:43'),(1109,'Lavar Cortina Linho Fino p/metro',3500,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 18:10:07'),(1110,'LENCO DE BOLSO ',500,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 18:18:22'),(1111,'Lavar Casaco Cachecol',1300,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 18:23:59'),(1112,'Lavar Calça Normal',2000,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 18:50:26'),(1113,'Lavar Calça Normal',2000,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 18:50:29'),(1114,'Lavar Calça Normal',2000,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 18:51:40'),(1115,'LAVAR FATO SOCIAL',3500,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 19:03:57'),(1116,'Urgencia',7000,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 19:04:02'),(1117,'Lavar Vestido de Gala c/Lantenjolas',10500,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 19:05:50'),(1118,'Urgencia',7000,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 19:05:58'),(1119,'Urgencia',1750,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 19:12:59'),(1120,'LAVAR FATO SOCIAL',3500,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 19:13:04'),(1121,'Lavar Tshirt normal',1500,'2024-02-29',1,'MESA 16','Flora Ngola','2024-02-29 19:53:16'),(1122,'Lavar Blaser',2200,'2024-03-01',1,'MESA 16','Quissenda Jose','2024-03-01 09:12:31'),(1123,'Lavar Calça Ganga',1800,'2024-03-01',1,'MESA 16','Quissenda Jose','2024-03-01 10:39:17'),(1124,'Lavar Blaser',2200,'2024-03-01',1,'MESA 16','Quissenda Jose','2024-03-01 11:45:01'),(1125,'Lavar Calça Linho S',2200,'2024-03-01',1,'MESA 16','Flora Ngola','2024-03-01 17:21:07'),(1126,'Lavar Calça Social',1750,'2024-03-01',1,'MESA 16','Flora Ngola','2024-03-01 17:22:31'),(1127,'Lavar Camisa Branca (Muito Suja)',1700,'2024-03-01',1,'MESA 16','Flora Ngola','2024-03-01 17:23:17'),(1128,'Camisa social',1500,'2024-03-01',1,'MESA 16','Flora Ngola','2024-03-01 18:20:44'),(1129,'ENGOMAR CALÇA SOCIAL',1500,'2024-03-01',1,'MESA 16','Flora Ngola','2024-03-01 18:40:35'),(1130,'Polo muito suja',1800,'2024-03-02',1,'MESA 16','Quissenda Jose','2024-03-02 08:50:47'),(1131,'Polo muito suja',1800,'2024-03-02',1,'MESA 16','Quissenda Jose','2024-03-02 08:52:06'),(1132,'Lavar Polo',1500,'2024-03-02',1,'MESA 16','Quissenda Jose','2024-03-02 08:52:17'),(1133,'Lavar Tshirt normal',1500,'2024-03-02',1,'MESA 16','Quissenda Jose','2024-03-02 12:47:33'),(1134,'Lavar Vestido Bubu',6500,'2024-03-02',1,'MESA 16','Flora Ngola','2024-03-02 15:04:01'),(1135,'EDREDON KWIN',9500,'2024-03-02',1,'MESA 16','Flora Ngola','2024-03-02 15:05:25'),(1136,'LAVAR FATO SOCIAL',3500,'2024-03-02',1,'MESA 16','Flora Ngola','2024-03-02 16:55:03'),(1137,'LAVAR FATO SOCIAL',3500,'2024-03-02',1,'MESA 16','Flora Ngola','2024-03-02 16:55:10'),(1138,'LAVAR FATO SOCIAL',3500,'2024-03-02',1,'MESA 16','Flora Ngola','2024-03-02 18:15:42'),(1139,'Lavar Camisa Branca (Muito Suja)',1700,'2024-03-03',1,'MESA 16','Quissenda Jose','2024-03-03 10:52:21'),(1140,'Lavar Camisa Branca (Muito Suja)',1700,'2024-03-03',1,'MESA 16','Quissenda Jose','2024-03-03 10:54:08'),(1141,'Calça social femenina',1750,'2024-03-03',1,'MESA 16','Quissenda Jose','2024-03-03 12:44:08'),(1142,'Lavar Pantalona',1500,'2024-03-03',1,'MESA 16','Quissenda Jose','2024-03-03 12:44:25'),(1143,'Lavar Calça Linho H',2200,'2024-03-03',1,'MESA 16','Quissenda Jose','2024-03-03 12:44:30'),(1144,'Lavar Toalhas de Banho Grande',2500,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 08:10:13'),(1145,'COLCHA CASAL',8000,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 08:14:31'),(1146,'Lavar Tapetes p/metros',3500,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 08:14:35'),(1147,'CORTINAS BLACK OUT',14500,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 08:15:55'),(1148,'Lavar Cortina Black Out p/metro',3000,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 08:16:05'),(1149,'Lavar Cortina Black Out p/metro',3000,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 08:19:42'),(1150,'Lavar Tapetes p/metros',3500,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 08:20:20'),(1151,'Lavar Blusa de Seda',2000,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 10:11:54'),(1152,'Lavar Vestido Simples',4500,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 10:12:10'),(1153,'LAVAR FATO SOCIAL',3500,'2024-03-04',1,'MESA 16','Quissenda Jose','2024-03-04 13:17:26'),(1154,'Lavar Vestido Bubu',6500,'2024-03-04',1,'MESA 16','Flora Ngola','2024-03-04 16:53:26'),(1155,'Lavar Calça Social',1750,'2024-03-04',1,'MESA 16','Flora Ngola','2024-03-04 20:16:17'),(1156,'Lavar Calça Social',1750,'2024-03-04',1,'MESA 16','Flora Ngola','2024-03-04 20:22:32'),(1157,'Lavar Polo',1500,'2024-03-05',1,'MESA 16','Quissenda Jose','2024-03-05 09:53:57'),(1158,'Lavar Calça Social',1750,'2024-03-05',1,'MESA 16','Quissenda Jose','2024-03-05 10:00:41'),(1159,'Lavar Calça Social',1750,'2024-03-05',1,'MESA 16','Quissenda Jose','2024-03-05 10:00:47'),(1160,'ENGOMAR CALÇA SOCIAL',1500,'2024-03-05',1,'MESA 16','Flora Ngola','2024-03-05 18:24:17'),(1161,'Vestido simples de festa',7500,'2024-03-05',1,'MESA 16','Flora Ngola','2024-03-05 19:29:52'),(1162,'Lavar Blaser',2200,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 09:47:41'),(1163,'Lavar Vestido de Seda',8000,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 09:59:16'),(1164,'Camisa social',1500,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 11:56:36'),(1165,'Lavar Camisa Branca (Muito Suja)',1700,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 11:57:29'),(1166,'Lavar Camisa Branca (Muito Suja)',1700,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 11:57:34'),(1167,'Lavar Calção Normal',1500,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:48:58'),(1168,'Lavar Calça Social',1750,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:49:44'),(1169,'Camisa social',1500,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:50:21'),(1170,'Lavar Camisa de Linho',2000,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:51:45'),(1171,'Lavar Camisa de Linho',2000,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:51:51'),(1172,'Polo muito suja',1800,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:52:07'),(1173,'Polo muito suja',1800,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:52:13'),(1174,'Tshirt normal muito suja',1800,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:52:17'),(1175,'Tshirt normal muito suja',1800,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:52:22'),(1176,'Tshirt normal muito suja',1800,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:52:47'),(1177,'Lavar Calção de Ganga',1700,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:52:53'),(1178,'Lavar Calção Normal',1500,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 12:52:57'),(1179,'Casaco social de linho',2000,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 14:26:10'),(1180,'FATO DE BLUSA E SAIA ',6000,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 14:29:49'),(1181,'Lavar Vestido Fato',5250,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 14:29:52'),(1182,'Lavar Vestido Fato',5250,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 14:29:55'),(1183,'LAVAR FATO SOCIAL',3500,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 14:33:35'),(1184,'LAVAR FATO SOCIAL',3500,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 14:33:38'),(1185,'Vestido simples de festa',7500,'2024-03-06',1,'MESA 16','Quissenda Jose','2024-03-06 14:34:09'),(1186,'LAVAR FATO SOCIAL',3500,'2024-03-06',1,'MESA 16','Flora Ngola','2024-03-06 18:24:49'),(1187,'Camisa social',1500,'2024-03-06',1,'MESA 16','Flora Ngola','2024-03-06 19:33:32'),(1188,'Lavar Calça Social',1750,'2024-03-07',1,'MESA 16','Quissenda Jose','2024-03-07 08:08:11'),(1189,'Urgencia',9000,'2024-03-07',1,'MESA 16','Quissenda Jose','2024-03-07 08:09:30'),(1190,'Lavar Calça Social',1750,'2024-03-07',1,'MESA 16','Quissenda Jose','2024-03-07 08:56:47'),(1191,'Lavar Edredon King',10000,'2024-03-07',1,'MESA 16','Quissenda Jose','2024-03-07 10:30:09'),(1192,'LAVAR CAMISA NORMAL ',2000,'2024-03-07',1,'MESA 16','Quissenda Jose','2024-03-07 10:42:15'),(1193,'ENGOMAR CALÇA SOCIAL S',1500,'2024-03-07',1,'MESA 16','Quissenda Jose','2024-03-07 13:26:09'),(1194,'Urgencia',3000,'2024-03-07',1,'MESA 16','Quissenda Jose','2024-03-07 13:26:14'),(1195,'Lavar Blaser',2200,'2024-03-07',1,'MESA 16','Flora Ngola','2024-03-07 19:37:59'),(1196,'Lavar Calça Social',1750,'2024-03-10',1,'MESA 16','Quissenda Jose','2024-03-11 09:08:45'),(1197,'Lavar Blaser',2200,'2024-03-11',1,'MESA 16','Quissenda Jose','2024-03-11 09:23:28'),(1198,'Lavar Blaser',2200,'2024-03-11',1,'MESA 16','Quissenda Jose','2024-03-11 09:24:19'),(1199,'Lavar Ropão/Robe',4500,'2024-03-11',1,'MESA 16','Quissenda Jose','2024-03-11 12:08:13'),(1200,'COLCHA CASAL',8000,'2024-03-11',1,'MESA 16','Quissenda Jose','2024-03-11 12:08:17'),(1201,'Lavar Casaco Social',1750,'2024-03-11',1,'MESA 16','Quissenda Jose','2024-03-11 15:14:29'),(1202,'Camisa social',1500,'2024-03-11',1,'MESA 16','Quissenda Jose','2024-03-11 16:42:54'),(1203,'Lavar Blaser',2200,'2024-03-11',1,'MESA 16','Quissenda Jose','2024-03-11 16:42:56'),(1204,'LAVAR CAMISOLA DE LINHO',2000,'2024-03-12',1,'MESA 16','Quissenda Jose','2024-03-12 09:02:26'),(1205,'Lavar Polo',1500,'2024-03-12',1,'MESA 16','Quissenda Jose','2024-03-12 09:07:40'),(1206,'Lavar Tshirt normal',1500,'2024-03-12',1,'MESA 16','Quissenda Jose','2024-03-12 09:14:55'),(1207,'EDREDON KWIN',9500,'2024-03-12',1,'MESA 16','Quissenda Jose','2024-03-12 13:20:29'),(1208,'Lavar Lençol Solteiro 1',3000,'2024-03-12',1,'MESA 16','Quissenda Jose','2024-03-12 13:21:22'),(1209,'Lavar Lençois King',5000,'2024-03-12',1,'MESA 16','Quissenda Jose','2024-03-12 13:22:26'),(1210,'Lavar Calça Normal',2000,'2024-03-12',1,'MESA 16','Flora Ngola','2024-03-12 17:44:43'),(1211,'Urgencia',1900,'2024-03-12',1,'MESA 16','Quissenda Jose','2024-03-13 08:19:18'),(1212,'Lavar Vestido Africano',3800,'2024-03-12',1,'MESA 16','Quissenda Jose','2024-03-13 08:19:22'),(1213,'Lavar Camisa de Linho',2000,'2024-03-13',1,'MESA 16','Quissenda Jose','2024-03-13 10:06:51'),(1214,'Lavar Calça Ganga',1800,'2024-03-13',1,'MESA 16','Quissenda Jose','2024-03-13 10:51:27'),(1215,'Lavar Calça Social',1750,'2024-03-13',1,'MESA 16','Quissenda Jose','2024-03-13 10:51:38'),(1216,'Lavar Polo',1500,'2024-03-13',1,'MESA 16','Quissenda Jose','2024-03-13 16:20:35'),(1217,'LAVAR FATO SOCIAL',3500,'2024-03-13',1,'MESA 16','Flora Ngola','2024-03-13 18:47:14'),(1218,'Lavar Blaser',2200,'2024-03-13',1,'MESA 16','Flora Ngola','2024-03-13 19:23:37'),(1219,'Lavar Calça Social',1750,'2024-03-13',1,'MESA 16','Flora Ngola','2024-03-13 19:28:18'),(1220,'Vestido simples de festa',7500,'2024-03-13',1,'MESA 16','Flora Ngola','2024-03-13 20:42:03'),(1221,'LAVAR FATO SOCIAL',3500,'2024-03-14',1,'MESA 16','Quissenda Jose','2024-03-14 08:09:11'),(1222,'LAVAR BLUSA VESTIDO',2000,'2024-03-14',1,'MESA 16','Quissenda Jose','2024-03-14 08:46:09'),(1223,'Camisa social',1500,'2024-03-14',1,'MESA 16','Quissenda Jose','2024-03-14 11:59:28'),(1224,'Camisa social',1500,'2024-03-14',1,'MESA 16','Quissenda Jose','2024-03-14 12:01:58'),(1225,'Lavar Calça Social',1750,'2024-03-14',1,'MESA 16','Flora Ngola','2024-03-14 15:03:42'),(1226,'ENGOMAR CAMISA SOCIAL',1300,'2024-03-15',1,'MESA 16','Flora Ngola','2024-03-15 15:41:30'),(1227,'Lavar Vestido Bata',6000,'2024-03-15',1,'MESA 16','Flora Ngola','2024-03-15 16:04:28'),(1228,'Lavar Bata',3000,'2024-03-16',1,'MESA 16','Quissenda Jose','2024-03-16 09:10:14'),(1229,'Lavar Calça Ganga',1800,'2024-03-16',1,'MESA 16','Quissenda Jose','2024-03-16 09:28:09'),(1230,'Lavar Edredon Solteiro',8000,'2024-03-16',1,'MESA 16','Quissenda Jose','2024-03-16 10:57:32'),(1231,'Lavar Cortina Black Out p/metro',3000,'2024-03-16',5,'MESA 16','Quissenda Jose','2024-03-16 11:41:21'),(1232,'Lavar Cortina Black Out p/metro',3000,'2024-03-16',5,'MESA 16','Quissenda Jose','2024-03-16 11:41:24'),(1233,'Lavar Cortina Black Out p/metro',3000,'2024-03-16',5,'MESA 16','Quissenda Jose','2024-03-16 11:41:27'),(1234,'Lavar Cortina Black Out p/metro',3000,'2024-03-16',5,'MESA 16','Quissenda Jose','2024-03-16 11:41:32'),(1235,'Urgencia',60000,'2024-03-16',1,'MESA 16','Quissenda Jose','2024-03-16 11:55:37'),(1236,'Lavar Cortina Black Out p/metro',3000,'2024-03-16',20,'MESA 16','Quissenda Jose','2024-03-16 11:55:41'),(1237,'Lavar Tapetes p/metros',3500,'2024-03-16',1,'MESA 16','Quissenda Jose','2024-03-16 11:56:29'),(1238,'Lavar Calça Social',1750,'2024-03-16',1,'MESA 16','Flora Ngola','2024-03-16 16:24:39'),(1239,'LAVAR FATO SOCIAL',3500,'2024-03-17',1,'MESA 16','Quissenda Jose','2024-03-17 10:13:44'),(1240,'Camisa social',1500,'2024-03-18',1,'MESA 16','Quissenda Jose','2024-03-18 09:55:49'),(1241,'Lavar Polo',1500,'2024-03-18',1,'MESA 16','Quissenda Jose','2024-03-18 10:49:58'),(1242,'Lav. COLETE',1500,'2024-03-18',1,'MESA 16','Quissenda Jose','2024-03-18 12:25:37'),(1243,'Lav. COLETE',1500,'2024-03-18',1,'MESA 16','Quissenda Jose','2024-03-18 12:25:41'),(1244,'Lav. COLETE',1500,'2024-03-18',1,'MESA 16','Quissenda Jose','2024-03-18 12:25:43'),(1245,'Lavar Blaser',2200,'2024-03-18',1,'MESA 16','Flora Ngola','2024-03-18 19:20:58'),(1246,'LAVAR FATO SOCIAL',3500,'2024-03-19',1,'MESA 16','Quissenda Jose','2024-03-19 09:05:32'),(1247,'Engomar Blaser',1500,'2024-03-19',1,'MESA 16','Flora Ngola','2024-03-19 17:14:25'),(1248,'Camisa de seda',1700,'2024-03-19',1,'MESA 16','Flora Ngola','2024-03-19 20:27:37'),(1249,'Camisa social',1500,'2024-03-19',1,'MESA 16','Flora Ngola','2024-03-19 20:27:42'),(1250,'Lavar Calça Ganga',1800,'2024-03-20',1,'MESA 16','Quissenda Jose','2024-03-20 08:41:21'),(1251,'Lavar Blaser',2200,'2024-03-20',1,'MESA 16','Flora Ngola','2024-03-20 13:35:23'),(1252,'Camisa social',1500,'2024-03-20',1,'MESA 16','Flora Ngola','2024-03-20 14:39:20'),(1253,'Lavar Cortina Linho Fino p/metro',3500,'2024-03-20',88.984,'MESA 16','Flora Ngola','2024-03-20 15:27:12'),(1254,'Lavar Cortina Linho Fino p/metro',3500,'2024-03-20',21000,'MESA 16','Flora Ngola','2024-03-20 15:53:28'),(1255,'Camisa social',1500,'2024-03-20',1,'MESA 16','Flora Ngola','2024-03-20 15:54:11'),(1256,'LAVAR FATO SOCIAL',3500,'2024-03-20',1,'MESA 16','Flora Ngola','2024-03-20 16:35:41'),(1257,'Lavar Vestido de Seda',8000,'2024-03-20',1,'MESA 16','Flora Ngola','2024-03-20 16:38:00'),(1258,'Engomar fato de noivo',4500,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 08:14:18'),(1259,'Urgencia',1700,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 08:15:01'),(1260,'CORTINAS BLACK OUT',14500,'2024-03-21',1,'MESA 16','Eltone Ndongala','2024-03-21 09:18:26'),(1261,'CORTINAS BLACK OUT',14500,'2024-03-21',3.8,'MESA 16','Eltone Ndongala','2024-03-21 09:18:29'),(1262,'CORTINAS BLACK OUT',14500,'2024-03-21',1,'MESA 16','Eltone Ndongala','2024-03-21 09:18:31'),(1263,'CORTINAS BLACK OUT',14500,'2024-03-21',1,'MESA 16','Eltone Ndongala','2024-03-21 09:18:35'),(1264,'Lavar Cortina Transp leves p/metro',2950,'2024-03-21',1,'MESA 16','Eltone Ndongala','2024-03-21 09:27:56'),(1265,'Lavar Cortina Transp leves p/metro',2950,'2024-03-21',1,'MESA 16','Eltone Ndongala','2024-03-21 09:27:59'),(1266,'Lavar Cortina Transp leves p/metro',2950,'2024-03-21',12,'MESA 16','Eltone Ndongala','2024-03-21 09:49:27'),(1267,'Lavar Cortina Black Out p/metro',3000,'2024-03-21',12,'MESA 16','Eltone Ndongala','2024-03-21 09:49:30'),(1268,'Lavar Cortina Black Out p/metro',3000,'2024-03-21',16,'MESA 16','Eltone Ndongala','2024-03-21 09:50:35'),(1269,'Lavar Cortina Black Out p/metro',3000,'2024-03-21',5,'MESA 16','Eltone Ndongala','2024-03-21 10:10:06'),(1270,'Tshirt normal muito suja',1800,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:03:49'),(1271,'Lavar Cortina Black Out p/metro',3000,'2024-03-21',7,'MESA 16','Quissenda Jose','2024-03-21 12:16:07'),(1272,'Lavar Cortina Black Out p/metro',3000,'2024-03-21',10,'MESA 16','Quissenda Jose','2024-03-21 12:16:10'),(1273,'Taxa de Urgência 50',1900,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:29:16'),(1274,'Taxa Expresso 100',3800,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:29:24'),(1275,'Taxa Expresso 100',3800,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:29:35'),(1276,'Lavar Beca',3800,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:29:38'),(1277,'Taxa Expresso 100',1500,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:41:31'),(1278,'Lavar Calça Social',1750,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:47:41'),(1279,'Lavar Calça Social',1750,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:47:44'),(1280,'Lavar Calça Social',1750,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:47:49'),(1281,'Lavar Calça Social',1750,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:47:52'),(1282,'Lavar Tshirt normal',1500,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:47:56'),(1283,'Lavar Polo',1500,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:47:58'),(1284,'Camisa social',1500,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:48:01'),(1285,'Camisa social',1500,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:48:05'),(1286,'Lavar Vestido de Seda',8000,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 12:49:30'),(1287,'Lavar Calça Social',1750,'2024-03-21',1,'MESA 16','Quissenda Jose','2024-03-21 13:03:05'),(1288,'Taxa de Urgência 50',3500,'2024-03-21',1,'MESA 16','Flora Ngola','2024-03-21 19:45:25'),(1289,'Taxa de Urgência 50',750,'2024-03-22',1,'MESA 16','Quissenda Jose','2024-03-22 10:29:46'),(1290,'ENGOMAR CALÇA SOCIAL S',1500,'2024-03-22',1,'MESA 16','Quissenda Jose','2024-03-22 10:35:51'),(1291,'Taxa Expresso 100',1500,'2024-03-22',1,'MESA 16','Quissenda Jose','2024-03-22 10:35:55'),(1292,'Lavar Calça Social',1750,'2024-03-22',1,'MESA 16','Quissenda Jose','2024-03-22 10:36:14'),(1293,'Lavar Vestido de Seda',8000,'2024-03-22',1,'MESA 16','Quissenda Jose','2024-03-22 11:29:48'),(1294,'Lavar Blaser',2200,'2024-03-22',1,'MESA 16','Flora Ngola','2024-03-22 18:13:19'),(1295,'Lavar Calça Social',1750,'2024-03-22',1,'MESA 16','Flora Ngola','2024-03-22 19:28:46'),(1296,'EDREDON KWIN',9500,'2024-03-23',1,'MESA 16','Quissenda Jose','2024-03-23 10:55:46'),(1297,'LAVAR CASACO DE CRIANÇA',1250,'2024-03-23',1,'MESA 16','Quissenda Jose','2024-03-23 12:08:03'),(1298,'LAVAR FATO SOCIAL',3500,'2024-03-23',1,'MESA 16','Quissenda Jose','2024-03-23 12:36:16'),(1299,'Lavar Blusa de Seda',2000,'2024-03-23',1,'MESA 16','Quissenda Jose','2024-03-23 12:45:51'),(1300,'LAVAR FATO SOCIAL',3500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 13:06:23'),(1301,'Taxa de Urgência 50',1500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 13:24:29'),(1302,'Lavar Tshirt normal',1500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 13:24:31'),(1303,'Lavar Tshirt normal',1500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 13:24:43'),(1304,'LAVAR FATO SOCIAL',3500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 14:48:07'),(1305,'ENGOMAR CALÇA SOCIAL S',1500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 15:54:15'),(1306,'Lavar Calça Social',1750,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 15:54:19'),(1307,'Lavar Blaser',2200,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 15:54:22'),(1308,'Lavar Blaser',2200,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 15:54:25'),(1309,'LAVAR FATO SOCIAL',3500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 15:54:33'),(1310,'LAVAR FATO SOCIAL',3500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 15:54:36'),(1311,'LAVAR FATO SOCIAL',3500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 15:54:39'),(1312,'LAVAR FATO SOCIAL',3500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 15:54:42'),(1313,'Camisa social',1500,'2024-03-23',1,'MESA 16','Flora Ngola','2024-03-23 15:56:02'),(1314,'LAVAR FATO SOCIAL',3500,'2024-03-25',1,'MESA 16','Quissenda Jose','2024-03-25 10:27:03'),(1315,'CORTINA BLACK OUT ',18000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:06:17'),(1316,'CORTINA BLACK OUT ',18000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:06:21'),(1317,'CORTINA LINHO 1',21000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:09:18'),(1318,'CORTINA BLACK OUT ',18000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:20:18'),(1319,'CORTINA BLACK OUT ',18000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:20:34'),(1320,'CORTINA LINHO 2',24000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:21:09'),(1321,'CORTINA LINHO 1',21000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:21:13'),(1322,'CORTINA LINHO 2',24000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:21:41'),(1323,'CORTINA LINHO 1',21000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:21:46'),(1324,'CORTINA LINHO 2',24000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:21:57'),(1325,'CORTINA LINHO 2',24000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:22:13'),(1326,'CORTINA LINHO 2',24000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:22:22'),(1327,'CORTINA LINHO 1',21000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:22:25'),(1328,'CORTINA LINHO 1',21000,'2024-03-26',1,'MESA 16','Quissenda Jose','2024-03-26 13:22:32'),(1329,'CORTINA LINHO 1',21000,'2024-03-26',5,'MESA 16','Quissenda Jose','2024-03-26 14:27:10'),(1330,'CORTINA LINHO 2',24000,'2024-03-26',6,'MESA 16','Quissenda Jose','2024-03-26 14:27:13'),(1331,'CORTINA BLACK OUT ',18000,'2024-03-26',3,'MESA 16','Quissenda Jose','2024-03-26 14:27:16'),(1332,'Lavar Vestido Bata',6000,'2024-03-26',1,'MESA 16','Flora Ngola','2024-03-26 18:15:07'),(1333,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-03-26',1,'MESA 16','Flora Ngola','2024-03-26 18:32:42'),(1334,'Lavar macacão jardineira',5500,'2024-03-28',1,'MESA 16','Quissenda Jose','2024-03-28 07:52:33'),(1335,'Lavar Saia de Linho',3000,'2024-03-28',1,'MESA 16','Flora Ngola','2024-03-28 16:43:59'),(1336,'Camisa de seda',1700,'2024-03-29',1,'MESA 16','Quissenda Jose','2024-03-29 10:36:45'),(1337,'Lavar Camisa Branca (Muito Suja)',1700,'2024-03-29',1,'MESA 16','Quissenda Jose','2024-03-29 13:10:58'),(1338,'ENGOMAR TSHIRT',1000,'2024-03-30',1,'MESA 16','Flora Ngola','2024-03-30 10:41:28'),(1339,'LAVAR FATO SOCIAL',3500,'2024-03-30',1,'MESA 16','Flora Ngola','2024-03-30 12:18:59'),(1340,'Lavar Tshirt normal',1500,'2024-03-30',1,'MESA 16','Quissenda Jose','2024-03-30 15:02:18'),(1341,'Taxa Expresso 100',3000,'2024-03-30',1,'MESA 16','Quissenda Jose','2024-03-30 15:02:22'),(1342,'Taxa Expresso 100',4000,'2024-03-30',1,'MESA 16','Quissenda Jose','2024-03-30 15:03:04'),(1343,'Lavar Ropão/Robe',4500,'2024-03-30',1,'MESA 16','Quissenda Jose','2024-03-30 15:40:31'),(1344,'Lavar Ropão/Robe',4500,'2024-03-30',1,'MESA 16','Quissenda Jose','2024-03-30 15:40:34'),(1345,'Lavar Calça Social',1750,'2024-03-31',1,'MESA 16','Quissenda Jose','2024-03-31 10:51:10'),(1346,'engomar Fato Social',1700,'2024-03-31',1,'MESA 16','Quissenda Jose','2024-03-31 10:51:14'),(1347,'Camisa social',1500,'2024-03-31',1,'MESA 16','Quissenda Jose','2024-03-31 12:23:22'),(1348,'Lavar Calça Linho H',2200,'2024-03-31',1,'MESA 16','Quissenda Jose','2024-03-31 12:23:56'),(1349,'Taxa Expresso 100',13700,'2024-03-31',1,'MESA 16','Quissenda Jose','2024-03-31 12:27:21'),(1350,'LAVAR FATO SOCIAL',3500,'2024-04-01',1,'MESA 16','Eltone Ndongala','2024-04-01 14:11:01'),(1351,'Lavar Casaco Social',1750,'2024-04-01',1,'MESA 16','Eltone Ndongala','2024-04-01 14:11:17'),(1352,'Lavar Casaco Social',1750,'2024-04-01',1,'MESA 16','Eltone Ndongala','2024-04-01 14:11:20'),(1353,'Lavar Casaco Social',1750,'2024-04-01',1,'MESA 16','Eltone Ndongala','2024-04-01 14:11:24'),(1354,'Lavar Tshirt normal',1500,'2024-04-01',1,'MESA 16','Flora Ngola','2024-04-01 15:17:07'),(1355,'Lavar Camisa de Linho',2000,'2024-04-01',1,'MESA 16','Flora Ngola','2024-04-01 15:17:14'),(1356,'LAVAR FATO SOCIAL',3500,'2024-04-01',1,'MESA 16','Flora Ngola','2024-04-01 15:46:50'),(1357,'LAVAR FATO SOCIAL',3500,'2024-04-01',1,'MESA 16','Flora Ngola','2024-04-01 15:47:12'),(1358,'Gravata',700,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 10:23:50'),(1359,'Lavar Calça Ganga',1800,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 11:20:08'),(1360,'Lavar Colete',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:32:41'),(1361,'Lavar Polo',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:32:47'),(1362,'Lavar Tshirt normal',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:32:50'),(1363,'Lavar Tshirt normal',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:32:54'),(1364,'Lavar Tshirt normal',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:01'),(1365,'LAVAR FATO SOCIAL',3500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:09'),(1366,'Lavar Calça Linho H',2200,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:15'),(1367,'Lavar Calça Social',1750,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:18'),(1368,'Lavar Calça Normal',2000,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:23'),(1369,'Lavar Calça Social',1750,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:26'),(1370,'LAVAR FATO SOCIAL',3500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:28'),(1371,'Lavar Blaser',2200,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:35'),(1372,'Lavar Blaser',2200,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:38'),(1373,'LAVAR FATO SOCIAL',3500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:42'),(1374,'Lavar Tshirt normal',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:44'),(1375,'LAVAR FATO SOCIAL',3500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:33:47'),(1376,'Lavar Tshirt normal',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:34:57'),(1377,'Lavar Tshirt normal',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:35:06'),(1378,'Lavar Tshirt normal',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:35:09'),(1379,'Lavar Tshirt normal',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:35:12'),(1380,'Lavar Tshirt normal',1500,'2024-04-02',1,'MESA 16','Eltone Ndongala','2024-04-02 16:35:14'),(1381,'Lavar Calça Social',1750,'2024-04-03',1,'MESA 16','Eltone Ndongala','2024-04-03 10:18:09'),(1382,'Lavar Calça Normal',2000,'2024-04-03',1,'MESA 16','Eltone Ndongala','2024-04-03 12:41:18'),(1383,'Lavar Calça Social',1750,'2024-04-03',1,'MESA 16','Eltone Ndongala','2024-04-03 12:41:25'),(1384,'Lavar Cobertor Casal',5500,'2024-04-03',1,'MESA 16','Eltone Ndongala','2024-04-03 12:48:16'),(1385,'Lavar Lençois Casal 1',4000,'2024-04-03',1,'MESA 16','Eltone Ndongala','2024-04-03 12:48:36'),(1386,'Lavar Calça Ganga',1800,'2024-04-03',1,'MESA 16','Eltone Ndongala','2024-04-03 13:24:53'),(1387,'Camisa social',1500,'2024-04-03',1,'MESA 16','Flora Ngola','2024-04-03 14:49:05'),(1388,'Lavar Tshirt normal',1500,'2024-04-03',1,'MESA 16','Flora Ngola','2024-04-03 14:49:53'),(1389,'LAVAR FATO SOCIAL',3500,'2024-04-03',1,'MESA 16','Flora Ngola','2024-04-03 17:13:36'),(1390,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-04-03',1,'MESA 16','Flora Ngola','2024-04-03 17:19:25'),(1391,'Gravata',700,'2024-04-03',1,'MESA 16','Flora Ngola','2024-04-03 17:21:36'),(1392,'Calça social femenina',1750,'2024-04-05',1,'MESA 16','Quissenda Jose','2024-04-05 09:04:21'),(1393,'Lavar Blaser',2200,'2024-04-05',1,'MESA 16','Quissenda Jose','2024-04-05 13:36:56'),(1394,'Taxa de Urgência 50',2625,'2024-04-05',1,'MESA 16','Flora Ngola','2024-04-05 14:48:46'),(1395,'Taxa Expresso 100',3500,'2024-04-05',1,'MESA 16','Flora Ngola','2024-04-05 14:48:53'),(1396,'Camisa social',1500,'2024-04-07',1,'MESA 16','Flora Ngola','2024-04-07 10:07:22'),(1397,'Lavar Calça Social',1750,'2024-04-07',1,'MESA 16','Flora Ngola','2024-04-07 10:08:41'),(1398,'Lavar Edredon Casal',9500,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 11:22:33'),(1399,'Lavar Pijama',2500,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 12:18:20'),(1400,'LAVAR FATO SOCIAL',3500,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 12:18:28'),(1401,'Lavar Almofada Grande',3000,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 12:57:34'),(1402,'BODES DE BEBE ',1100,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 13:18:26'),(1403,'BODES DE BEBE ',1100,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 13:18:30'),(1404,'BODES DE BEBE ',1100,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 13:18:33'),(1405,'BODES DE BEBE ',1100,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 13:18:35'),(1406,'BODES DE BEBE ',1100,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 13:19:44'),(1407,'BODES DE BEBE ',1100,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 13:19:47'),(1408,'LAVAR FATO SOCIAL',3500,'2024-04-08',1,'MESA 16','Eltone Ndongala','2024-04-08 13:35:35'),(1409,'Lavar Blaser',2200,'2024-04-08',1,'MESA 16','Flora Ngola','2024-04-08 15:20:05'),(1410,'LAVAR CAMISA NORMAL ',2000,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 07:48:10'),(1411,'Camisa social',1500,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 07:48:59'),(1412,'Lavar Cobertor Solteiro',4500,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 11:24:14'),(1413,'Lavar Lençol Solteiro 1',3000,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 11:24:26'),(1414,'Lavar Lençol Solteiro 1',3000,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 11:27:40'),(1415,'Lavar Lençois King',5000,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 11:31:07'),(1416,'Lavar Toalhas de Mesa 24 Lugar',5500,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 11:34:26'),(1417,'Lavar Colcha Casal',8000,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 11:53:59'),(1418,'Lavar Edredon Solteiro',8000,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 11:54:26'),(1419,'Lavar Calça Normal',2000,'2024-04-9',1,'MESA 16','Evaristo Araujo','2024-04-09 12:37:34'),(1420,'Camisa social',1500,'2024-04-9',1,'MESA 16','Flora Ngola','2024-04-09 16:04:09'),(1421,'Lavar Casaco Social',1750,'2024-04-9',1,'MESA 16','Flora Ngola','2024-04-09 16:46:15'),(1422,'Lavar Tshirt normal',1500,'2024-04-9',1,'MESA 16','Flora Ngola','2024-04-09 17:09:21'),(1423,'Lavar Blusa Simples',1500,'2024-04-9',1,'MESA 16','Flora Ngola','2024-04-09 17:57:40'),(1424,'Taxa de Urgência 50',1750,'2024-04-10',1,'MESA 16','Evaristo Araujo','2024-04-10 08:55:14'),(1425,'Taxa de Urgência 50',1750,'2024-04-10',1,'MESA 16','Evaristo Araujo','2024-04-10 08:56:34'),(1426,'LAVAR FATO SOCIAL',3500,'2024-04-10',1,'MESA 16','Evaristo Araujo','2024-04-10 08:56:58'),(1427,'LAVAR FATO SOCIAL',3500,'2024-04-10',1,'MESA 16','Evaristo Araujo','2024-04-10 09:00:03'),(1428,'LAVAR FATO SOCIAL',3500,'2024-04-10',1,'MESA 16','Evaristo Araujo','2024-04-10 09:01:42'),(1429,'Lavar Calção Normal',1500,'2024-04-10',1,'MESA 16','Evaristo Araujo','2024-04-10 09:16:06'),(1430,'LENCOIS CASAL DE 4',1500,'2024-04-10',1,'MESA 16','Evaristo Araujo','2024-04-10 09:55:08'),(1431,'LENCOIS CASAL DE 4',1500,'2024-04-10',1,'MESA 16','Evaristo Araujo','2024-04-10 09:55:11'),(1432,'Lavar Edredon Casal',9500,'2024-04-10',1,'MESA 16','Evaristo Araujo','2024-04-10 11:00:22'),(1433,'Camisa social',1500,'2024-04-10',1,'MESA 16','Flora Ngola','2024-04-10 17:20:34'),(1434,'Lavar Tshirt normal',1500,'2024-04-11',1,'MESA 16','Evaristo Araujo','2024-04-11 09:43:54'),(1435,'Lavar Casaco Social',1750,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 15:13:54'),(1436,'Lavar Cortina Solteira(impar)  p/metro',3300,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 15:29:23'),(1437,'Lavar Calça Social',1750,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 15:31:46'),(1438,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 15:41:26'),(1439,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 15:42:31'),(1440,'Taxa de Urgência 50',3000,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 15:42:35'),(1441,'Taxa de Urgência 50',11718.75,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 15:52:48'),(1442,'Lavar Calção Linho',2000,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 15:53:55'),(1443,'Lavar Vestido Bata',6000,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 16:46:33'),(1444,'Taxa Expresso 100',6000,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 16:46:50'),(1445,'Lavar Vestido Simples',4500,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 16:46:53'),(1446,'LAVAR FATO SOCIAL',3500,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 17:50:05'),(1447,'Taxa Expresso 100',4500,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 17:50:08'),(1448,'Lavar Vestido Simples',4500,'2024-04-11',1,'MESA 16','Flora Ngola','2024-04-11 17:50:12'),(1449,'Calça social femenina',1750,'2024-04-12',1,'MESA 16','Eltone Ndongala','2024-04-12 10:36:05'),(1450,'Taxa de Urgência 50',4500,'2024-04-12',1,'MESA 16','Eltone Ndongala','2024-04-12 10:46:21'),(1451,'Taxa de Urgência 50',3000,'2024-04-12',1,'MESA 16','Eltone Ndongala','2024-04-12 10:57:07'),(1452,'Lavar Vestido Bata',6000,'2024-04-12',1,'MESA 16','Eltone Ndongala','2024-04-12 10:57:09'),(1453,'LAVAR FATO SOCIAL',3500,'2024-04-12',1,'MESA 16','Eltone Ndongala','2024-04-12 10:57:19'),(1454,'LAVAR FATO SOCIAL',3500,'2024-04-12',1,'MESA 16','Eltone Ndongala','2024-04-12 10:57:22'),(1455,'Camisa social',1500,'2024-04-12',1,'MESA 16','Eltone Ndongala','2024-04-12 13:14:15'),(1456,'Lavar Calça Ganga',1800,'2024-04-12',1,'MESA 16','Eltone Ndongala','2024-04-12 13:14:59'),(1457,'Lavar Calça Ganga',1800,'2024-04-13',1,'MESA 16','Eltone Ndongala','2024-04-13 10:33:59'),(1458,'LAVAR FATO SOCIAL',3500,'2024-04-13',1,'MESA 16','Eltone Ndongala','2024-04-13 12:52:49'),(1459,'Lavar Pijama',2500,'2024-04-13',1,'MESA 16','Flora Ngola','2024-04-13 15:19:28'),(1460,'LAVAR FATO SOCIAL',3500,'2024-04-13',1,'MESA 16','Flora Ngola','2024-04-13 15:19:34'),(1461,'Lavar Calça Social',1750,'2024-04-13',1,'MESA 16','Flora Ngola','2024-04-13 16:02:58'),(1462,'Camisa social',1500,'2024-04-13',1,'MESA 16','Flora Ngola','2024-04-13 16:26:39'),(1463,'Lavar Casaco Social',1750,'2024-04-13',1,'MESA 16','Flora Ngola','2024-04-13 18:11:56'),(1464,'LAVAR FATO SOCIAL',3500,'2024-04-15',1,'MESA 16','Eltone Ndongala','2024-04-15 11:23:23'),(1465,'LAVAR CAMISOLA DE LINHO',2000,'2024-04-15',1,'MESA 16','Eltone Ndongala','2024-04-15 11:44:27'),(1466,'LAVAR CASACO DE GANGA',1800,'2024-04-15',1,'MESA 16','Eltone Ndongala','2024-04-15 11:44:51'),(1467,'Lavar Vestido Bubu',6500,'2024-04-15',1,'MESA 16','Eltone Ndongala','2024-04-15 11:59:31'),(1468,'Camisa social',1500,'2024-04-15',1,'MESA 16','Eltone Ndongala','2024-04-15 12:55:46'),(1469,'Lavar Camisa Branca (Muito Suja)',1700,'2024-04-15',1,'MESA 16','Eltone Ndongala','2024-04-15 12:55:55'),(1470,'Lavar Tshirt normal',1500,'2024-04-15',1,'MESA 16','Eltone Ndongala','2024-04-15 12:56:05'),(1471,'Lavar Tshirt normal',1500,'2024-04-15',1,'MESA 16','Quissenda Jose','2024-04-15 16:54:02'),(1472,'Lavar Blaser',2200,'2024-04-15',1,'MESA 16','Quissenda Jose','2024-04-15 18:30:09'),(1473,'Lavar Camisa Branca (Muito Suja)',1700,'2024-04-16',13,'MESA 16','Eltone Ndongala','2024-04-16 10:34:54'),(1474,'Camisa de seda',1700,'2024-04-16',1,'MESA 16','Eltone Ndongala','2024-04-16 10:35:01'),(1475,'Lavar Polo',1500,'2024-04-16',1,'MESA 16','Eltone Ndongala','2024-04-16 10:41:56'),(1476,'Lavar Camisa de Ganga',1750,'2024-04-16',1,'MESA 16','Eltone Ndongala','2024-04-16 10:49:49'),(1477,'Lavar Blaser',2200,'2024-04-16',1,'MESA 16','Eltone Ndongala','2024-04-16 12:18:34'),(1478,'Lavar Blusa de ALGODÃO',1500,'2024-04-16',1,'MESA 16','Eltone Ndongala','2024-04-16 12:23:20'),(1479,'LAVAR FATO SOCIAL',3500,'2024-04-16',1,'MESA 16','Eltone Ndongala','2024-04-16 12:25:41'),(1480,'Lavar Fato de Linho S',4000,'2024-04-16',1,'MESA 16','Eltone Ndongala','2024-04-16 12:25:44'),(1481,'Lavar Calção Linho',2000,'2024-04-16',1,'MESA 16','Quissenda Jose','2024-04-16 13:37:41'),(1482,'LAVAR SAIA DE NOIVA CURTA ',5000,'2024-04-16',1,'MESA 16','Quissenda Jose','2024-04-16 15:12:31'),(1483,'Lavar Polo',1500,'2024-04-16',1,'MESA 16','Quissenda Jose','2024-04-16 15:49:26'),(1484,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-04-16',1,'MESA 16','Quissenda Jose','2024-04-16 15:51:40'),(1485,'Vestido simples de festa',7500,'2024-04-16',1,'MESA 16','Quissenda Jose','2024-04-16 15:57:12'),(1486,'Calça social femenina',1750,'2024-04-16',1,'MESA 16','Quissenda Jose','2024-04-16 18:05:34'),(1487,'Lavar Blaser',2200,'2024-04-16',1,'MESA 16','Quissenda Jose','2024-04-16 18:11:36'),(1488,'Calça social femenina',1750,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:45:23'),(1489,'Camisa social',1500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:45:26'),(1490,'Lavar Camisa de Linho',2000,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:45:30'),(1491,'LAVAR FATO SOCIAL',3500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:45:33'),(1492,'FATO DE CRIANCA 15 ANOS\n\n',3500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:46:13'),(1493,'Camisa social',1500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:46:41'),(1494,'Camisa social',1500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:46:46'),(1495,'Taxa de Urgência 50',5000,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:51:14'),(1496,'Taxa de Urgência 50',6500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:51:55'),(1497,'Camisa social',1500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:51:57'),(1498,'Camisa social',1500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:52:00'),(1499,'Lavar Colete',1500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:52:02'),(1500,'Lavar Colete',1500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:52:06'),(1501,'LAVAR FATO SOCIAL',3500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:52:09'),(1502,'LAVAR FATO SOCIAL',3500,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:52:12'),(1503,'Lavar Vestido Volumoso',8000,'2024-04-17',1,'MESA 16','Eltone Ndongala','2024-04-17 09:57:16'),(1504,'Lavar Blaser',2200,'2024-04-17',1,'MESA 16','Quissenda Jose','2024-04-17 13:56:52'),(1505,'Taxa Expresso 100',6000,'2024-04-18',1,'MESA 16','Eltone Ndongala','2024-04-18 07:24:48'),(1506,'Lavar Vestido de Noiva(Simples)',20000,'2024-04-18',1,'MESA 16','Eltone Ndongala','2024-04-18 08:48:27'),(1507,'Camisa social',1500,'2024-04-18',1,'MESA 16','Eltone Ndongala','2024-04-18 08:48:31'),(1508,'Camisa social',1500,'2024-04-18',1,'MESA 16','Eltone Ndongala','2024-04-18 08:48:35'),(1509,'Lavar Calça Normal',2000,'2024-04-18',1,'MESA 16','Quissenda Jose','2024-04-18 13:32:11'),(1510,'Lavar Tshirt normal',1500,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 08:03:36'),(1511,'Taxa Expresso 100',1500,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 08:03:41'),(1512,'ENGOMAR TSHIRT',1000,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 08:04:02'),(1513,'Lavar Calça Social',1750,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 08:06:54'),(1514,'ENGOMAR CALÇA DE LINHO ',1800,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 11:13:25'),(1515,'ENGOMAR CALÇA NORMAL',1500,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 11:13:38'),(1516,'Calça social femenina',1750,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 11:19:07'),(1517,'Taxa Expresso 100',2000,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 11:19:37'),(1518,'Lavar Calça Normal',2000,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 11:19:44'),(1519,'Taxa Expresso 100',9000,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 11:43:16'),(1520,'Taxa de Urgência 50',2250,'2024-04-19',1,'MESA 16','Eltone Ndongala','2024-04-19 11:46:01'),(1521,'LAVAR FATO SOCIAL',3500,'2024-04-19',1,'MESA 16','Quissenda Jose','2024-04-19 15:21:08'),(1522,'LAVAR FATO SOCIAL',3500,'2024-04-19',1,'MESA 16','Quissenda Jose','2024-04-19 15:21:11'),(1523,'Lavar Blaser',2200,'2024-04-19',1,'MESA 16','Quissenda Jose','2024-04-19 15:22:26'),(1524,'Camisa social',1500,'2024-04-19',1,'MESA 16','Quissenda Jose','2024-04-19 16:30:20'),(1525,'Lavar Bermuda DE LINHO',2000,'2024-04-19',1,'MESA 16','Quissenda Jose','2024-04-19 16:31:36'),(1526,'ENGOMAR TOALHA DE MESA DE 12 LUGARES',4000,'2024-04-20',1,'MESA 16','Eltone Ndongala','2024-04-20 07:54:58'),(1527,'Taxa Expresso 100',75000,'2024-04-20',1,'MESA 16','Eltone Ndongala','2024-04-20 07:55:07'),(1528,'ENGOMAR TOALHA DE MESA DE LINHO',5000,'2024-04-20',15,'MESA 16','Eltone Ndongala','2024-04-20 07:55:10'),(1529,'Taxa Expresso 100',52500,'2024-04-20',1,'MESA 16','Eltone Ndongala','2024-04-20 08:42:27'),(1530,'ENGOMAR TOALHA DE MESA DE 6 LUGARES',3500,'2024-04-20',15,'MESA 16','Eltone Ndongala','2024-04-20 08:42:29'),(1531,'Lavar Casaco Social',1750,'2024-04-20',1,'MESA 16','Eltone Ndongala','2024-04-20 12:06:58'),(1532,'Taxa de Urgência 50',1400,'2024-04-20',1,'MESA 16','Quissenda Jose','2024-04-20 14:36:27'),(1533,'ENGOMAR CAMISA SOCIAL',1300,'2024-04-20',1,'MESA 16','Quissenda Jose','2024-04-20 14:37:59'),(1534,'Taxa Expresso 100',2800,'2024-04-20',1,'MESA 16','Quissenda Jose','2024-04-20 14:38:07'),(1535,'Taxa Expresso 100',5000,'2024-04-20',1,'MESA 16','Quissenda Jose','2024-04-20 14:38:31'),(1536,'Camisa social',1500,'2024-04-22',1,'MESA 16','Evaristo Araujo','2024-04-22 09:24:36'),(1537,'Lavar Par de Meias',1500,'2024-04-22',1,'MESA 16','Evaristo Araujo','2024-04-22 09:30:46'),(1538,'Lavar Par de Meias',1500,'2024-04-22',1,'MESA 16','Evaristo Araujo','2024-04-22 09:30:52'),(1539,'LAVAR CASACO DE CRIANÇA',1250,'2024-04-22',1,'MESA 16','Evaristo Araujo','2024-04-22 09:30:58'),(1540,'LAVAR CASACO DE CRIANÇA',1250,'2024-04-22',1,'MESA 16','Evaristo Araujo','2024-04-22 09:31:01'),(1541,'Lavar Fato de Treino',2500,'2024-04-22',1,'MESA 16','Evaristo Araujo','2024-04-22 09:58:14'),(1542,'Vestido simples de festa',7500,'2024-04-22',1,'MESA 16','Evaristo Araujo','2024-04-22 11:36:49'),(1543,'Vestido simples de festa',7500,'2024-04-22',1,'MESA 16','Evaristo Araujo','2024-04-22 11:36:54'),(1544,'Lavar Calção de Ganga',1700,'2024-04-22',1,'MESA 16','Quissenda Jose','2024-04-22 14:50:37'),(1545,'Lavar Tshirt normal',1500,'2024-04-22',1,'MESA 16','Quissenda Jose','2024-04-22 15:08:30'),(1546,'Lavar Calça Social',1750,'2024-04-22',1,'MESA 16','Quissenda Jose','2024-04-22 16:19:08'),(1547,'Lavar Calça Social',1750,'2024-04-22',1,'MESA 16','Quissenda Jose','2024-04-22 17:18:16'),(1548,'Lavar Vestido de Gala c/Lantenjolas',10500,'2024-04-23',1,'MESA 16','Quissenda Jose','2024-04-23 20:19:42'),(1549,'Lavar Vestido Africano',3800,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 08:12:53'),(1550,'Lavar Vestido Fato',5250,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 08:17:23'),(1551,'LAVAR FATO SOCIAL',3500,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 09:14:30'),(1552,'Camisa social',1500,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 09:17:20'),(1553,'Camisa social',1500,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 09:17:24'),(1554,'Camisa social',1500,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 09:17:53'),(1555,'Camisa social',1500,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 09:17:56'),(1556,'Camisa social',1500,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 09:17:58'),(1557,'Camisa social',1500,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 09:18:01'),(1558,'Taxa Expresso 100',1750,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 09:21:46'),(1559,'Lavar Camisa de Ganga',1750,'2024-04-24',7,'MESA 16','Evaristo Araujo','2024-04-24 09:21:56'),(1560,'Taxa de Urgência 50',6125,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 10:00:12'),(1561,'Lavar Casaco Social',1750,'2024-04-24',1,'MESA 16','Evaristo Araujo','2024-04-24 12:22:31'),(1562,'ENGOMAR CALÇA SOCIAL S',1500,'2024-04-24',1,'MESA 16','Quissenda Jose','2024-04-24 17:18:16'),(1563,'Lavar Blaser',2200,'2024-04-24',1,'MESA 16','Quissenda Jose','2024-04-24 17:19:06'),(1564,'Calça social femenina',1750,'2024-04-24',1,'MESA 16','Quissenda Jose','2024-04-24 18:21:28'),(1565,'LAVAR FATO SOCIAL',3500,'2024-04-25',1,'MESA 16','Evaristo Araujo','2024-04-25 09:31:35'),(1566,'Vestido simples de festa',7500,'2024-04-25',1,'MESA 16','Evaristo Araujo','2024-04-25 09:31:38'),(1567,'Lavar Blaser',2200,'2024-04-25',1,'MESA 16','Evaristo Araujo','2024-04-25 09:31:47'),(1568,'Lavar Blaser',2200,'2024-04-25',1,'MESA 16','Evaristo Araujo','2024-04-25 09:31:51'),(1569,'Camisa social',1500,'2024-04-25',1,'MESA 16','Evaristo Araujo','2024-04-25 09:33:04'),(1570,'Camisa social',1500,'2024-04-25',1,'MESA 16','Evaristo Araujo','2024-04-25 09:33:07'),(1571,'Lavar Porta Bebé',1500,'2024-04-25',1,'MESA 16','Evaristo Araujo','2024-04-25 09:45:53'),(1572,'COLCHA CASAL',8000,'2024-04-25',1,'MESA 16','Evaristo Araujo','2024-04-25 09:45:56'),(1573,'LAVAR FATO SOCIAL',3500,'2024-04-25',1,'MESA 16','Quissenda Jose','2024-04-25 17:51:41'),(1574,'Lavar Blaser',2200,'2024-04-25',1,'MESA 16','Quissenda Jose','2024-04-25 18:09:36'),(1575,'Lavar Polo',1500,'2024-04-25',1,'MESA 16','Quissenda Jose','2024-04-25 18:34:47'),(1576,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-04-25',1,'MESA 16','Quissenda Jose','2024-04-25 19:05:33'),(1577,'Taxa Expresso 100',5500,'2024-04-26',1,'MESA 16','Evaristo Araujo','2024-04-26 08:44:12'),(1578,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-04-26',1,'MESA 16','Evaristo Araujo','2024-04-26 08:45:12'),(1579,'Vestido simples de festa',7500,'2024-04-26',1,'MESA 16','Evaristo Araujo','2024-04-26 11:20:52'),(1580,'Lavar Edredon Casal',9500,'2024-04-26',1,'MESA 16','Evaristo Araujo','2024-04-26 11:21:04'),(1581,'Lavar Edredon Casal',9500,'2024-04-26',1,'MESA 16','Evaristo Araujo','2024-04-26 11:23:01'),(1582,'Lavar Edredon Casal',9500,'2024-04-26',1,'MESA 16','Evaristo Araujo','2024-04-26 11:27:35'),(1583,'Lavar Edredon Casal',9500,'2024-04-26',1,'MESA 16','Evaristo Araujo','2024-04-26 11:27:39'),(1584,'Lavar Bata',3000,'2024-04-26',1,'MESA 16','Evaristo Araujo','2024-04-26 11:28:31'),(1585,'LAVAR FATO SOCIAL',3500,'2024-04-26',1,'MESA 16','Quissenda Jose','2024-04-26 14:29:16'),(1586,'Lavar Vestido Simples',4500,'2024-04-26',1,'MESA 16','Quissenda Jose','2024-04-26 14:55:08'),(1587,'Lavar Calça Social',1750,'2024-04-26',1,'MESA 16','Quissenda Jose','2024-04-26 15:15:53'),(1588,'ENGOMAR CALÇA SOCIAL',1500,'2024-04-26',1,'MESA 16','Quissenda Jose','2024-04-26 15:16:59'),(1589,'Lavar Tshirt normal',1500,'2024-04-26',1,'MESA 16','Quissenda Jose','2024-04-26 16:56:12'),(1590,'Lavar Polo',1500,'2024-04-26',1,'MESA 16','Quissenda Jose','2024-04-26 16:56:47'),(1591,'Lavar Calça Social',1750,'2024-04-26',1,'MESA 16','Quissenda Jose','2024-04-26 16:59:32'),(1592,'Lavar Calça Social',1750,'2024-04-26',1,'MESA 16','Quissenda Jose','2024-04-26 16:59:35'),(1593,'Lavar Colcha Casal',8000,'2024-04-26',1,'MESA 16','Quissenda Jose','2024-04-26 19:09:15'),(1594,'Vestido simples de festa',7500,'2024-04-27',1,'MESA 16','Evaristo Araujo','2024-04-27 07:42:36'),(1595,'Taxa Expresso 100',7500,'2024-04-27',1,'MESA 16','Evaristo Araujo','2024-04-27 07:42:41'),(1596,'Tshirt normal muito suja',1800,'2024-04-27',1,'MESA 16','Evaristo Araujo','2024-04-27 11:20:31'),(1597,'Tshirt normal muito suja',1800,'2024-04-27',1,'MESA 16','Evaristo Araujo','2024-04-27 11:47:10'),(1598,'Camisa social',1500,'2024-04-27',1,'MESA 16','Evaristo Araujo','2024-04-27 12:13:27'),(1599,'Lavar Blusa de Seda',2000,'2024-04-27',1,'MESA 16','Evaristo Araujo','2024-04-27 12:18:28'),(1600,'Lavar Calça Linho H',2200,'2024-04-27',1,'MESA 16','Quissenda Jose','2024-04-27 13:50:50'),(1601,'Lavar Vestido de Gala Simples',7500,'2024-04-27',1,'MESA 16','Quissenda Jose','2024-04-27 13:52:12'),(1602,'Camisa social',1500,'2024-04-27',1,'MESA 16','Quissenda Jose','2024-04-27 14:05:12'),(1603,'Lavar Polo',1500,'2024-04-27',1,'MESA 16','Quissenda Jose','2024-04-27 14:30:11'),(1604,'Taxa Expresso 100',6500,'2024-04-27',1,'MESA 16','Quissenda Jose','2024-04-27 17:10:14'),(1605,'LAVAR FATO SOCIAL',3500,'2024-04-29',1,'MESA 16','Quissenda Jose','2024-04-29 12:52:09'),(1606,'LAVAR FATO SOCIAL',3500,'2024-04-29',1,'MESA 16','Quissenda Jose','2024-04-29 12:52:15'),(1607,'Lavar Blusa de Seda',2000,'2024-04-29',1,'MESA 16','Quissenda Jose','2024-04-29 13:29:21'),(1608,'Lavar Cortina Black Out p/metro',3000,'2024-04-29',7,'MESA 16','Quissenda Jose','2024-04-29 13:55:04'),(1609,'Lavar Cortina Black Out p/metro',3000,'2024-04-29',7,'MESA 16','Quissenda Jose','2024-04-29 13:55:07'),(1610,'Camisa social',1500,'2024-04-30',1,'MESA 16','Evaristo Araujo','2024-04-30 08:30:34'),(1611,'Lavar Colete',1500,'2024-05-01',1,'MESA 16','Evaristo Araujo','2024-05-01 11:20:41'),(1612,'LAVAR FATO DE GANGA MECANICA MUITO SUJA',5500,'2024-05-02',1,'MESA 16','Evaristo Araujo','2024-05-02 10:14:47'),(1613,'Lavar Gabardina/Sobretudo',3500,'2024-05-02',1,'MESA 16','Evaristo Araujo','2024-05-02 11:58:37'),(1614,'Lavar Bata',3000,'2024-05-02',1,'MESA 16','Evaristo Araujo','2024-05-02 12:39:36'),(1615,'Tshirt normal muito suja',1800,'2024-05-02',1,'MESA 16','Evaristo Araujo','2024-05-02 12:45:27'),(1616,'Lavar Tshirt normal',1500,'2024-05-02',1,'MESA 16','Evaristo Araujo','2024-05-02 12:45:33'),(1617,'Lavar Calça Social',1750,'2024-05-02',1,'MESA 16','Quissenda Jose','2024-05-02 13:35:55'),(1618,'Taxa Expresso 100',9000,'2024-05-02',1,'MESA 16','Quissenda Jose','2024-05-02 13:35:58'),(1619,'Camisa social',1500,'2024-05-02',1,'MESA 16','Quissenda Jose','2024-05-02 13:36:01'),(1620,'ENGOMAR CALÇA SOCIAL',1500,'2024-05-02',1,'MESA 16','Quissenda Jose','2024-05-02 13:36:04'),(1621,'Camisa social',1500,'2024-05-02',1,'MESA 16','Quissenda Jose','2024-05-02 13:36:07'),(1622,'ENGOMAR TSHIRT',1000,'2024-05-03',1,'MESA 16','Evaristo Araujo','2024-05-03 08:51:35'),(1623,'ENGOMAR CALÇA DE GANGA',1300,'2024-05-03',1,'MESA 16','Evaristo Araujo','2024-05-03 08:51:39'),(1624,'LAVAR FATO SOCIAL',3500,'2024-05-03',1,'MESA 16','Quissenda Jose','2024-05-03 10:26:58'),(1625,'LAVAR CAMISA NORMAL ',2000,'2024-05-03',1,'MESA 16','Quissenda Jose','2024-05-03 19:09:08'),(1626,'LAVAR CAMISA NORMAL ',2000,'2024-05-03',1,'MESA 16','Quissenda Jose','2024-05-03 19:09:14'),(1627,'Tshirt normal muito suja',1800,'2024-05-04',1,'MESA 16','Quissenda Jose','2024-05-04 10:11:37'),(1628,'LAVAR POLOVER NORMAL',2000,'2024-05-04',1,'MESA 16','Quissenda Jose','2024-05-04 11:16:24'),(1629,'CASACO DE INVERNO\n',5000,'2024-05-04',1,'MESA 16','Quissenda Jose','2024-05-04 13:27:33'),(1630,'Camisa social',1500,'2024-05-04',1,'MESA 16','Quissenda Jose','2024-05-04 16:25:29'),(1631,'Taxa de Urgência 50',650,'2024-05-04',1,'MESA 16','Quissenda Jose','2024-05-04 16:25:58'),(1632,'ENGOMAR CAMISA DE LINHO',1500,'2024-05-04',1,'MESA 16','Quissenda Jose','2024-05-04 16:26:03'),(1633,'Taxa Expresso 100',5000,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 18:33:24'),(1634,'Taxa de Urgência 50',12325,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:51:59'),(1635,'LAVAR FATO SOCIAL',3500,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:16'),(1636,'Camisa social',1500,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:21'),(1637,'Camisa social',1500,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:24'),(1638,'Camisa social',1500,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:30'),(1639,'Camisa social',1500,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:34'),(1640,'Lavar Calça Social',1750,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:37'),(1641,'Lavar Blaser',2200,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:40'),(1642,'LAVAR FATO SOCIAL',3500,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:42'),(1643,'LAVAR FATO SOCIAL',3500,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:46'),(1644,'Camisa social',1500,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:48'),(1645,'Camisa social',1500,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:51'),(1646,'  engomar Camisa Normal',1200,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:52:54'),(1647,'Taxa de Urgência 50',600,'2024-05-06',1,'MESA 16','Quissenda Jose','2024-05-06 19:53:08'),(1648,'FATO DE CRIANCA 15 ANOS\n\n',3500,'2024-05-07',1,'MESA 16','Quissenda Jose','2024-05-07 10:07:11'),(1649,'Lavar Vestido Bubu',6500,'2024-05-07',1,'MESA 16','Quissenda Jose','2024-05-07 10:11:42'),(1650,'Lavar Vestido Simples',4500,'2024-05-07',1,'MESA 16','Quissenda Jose','2024-05-07 10:11:50'),(1651,'  engomar Camisa Normal',1200,'2024-05-07',1,'MESA 16','Eltone Ndongala','2024-05-07 10:19:10'),(1652,'Taxa Expresso 100',5700,'2024-05-07',1,'MESA 16','Eltone Ndongala','2024-05-07 10:19:52'),(1653,'Camisa social',1500,'2024-05-07',1,'MESA 16','Eltone Ndongala','2024-05-07 10:20:38'),(1654,'ENGOMAR CALÇA SOCIAL S',1500,'2024-05-07',1,'MESA 16','Eltone Ndongala','2024-05-07 10:20:41'),(1655,'Taxa Expresso 100',2700,'2024-05-07',1,'MESA 16','Eltone Ndongala','2024-05-07 10:21:15'),(1656,'ENGOMAR CALÇA SOCIAL S',1500,'2024-05-07',1,'MESA 16','Eltone Ndongala','2024-05-07 10:21:18'),(1657,'  engomar Camisa Normal',1200,'2024-05-07',1,'MESA 16','Eltone Ndongala','2024-05-07 10:21:24'),(1658,'Camisa social',1500,'2024-05-07',1,'MESA 16','Quissenda Jose','2024-05-07 10:48:13'),(1659,'Lavar Calça Social',1750,'2024-05-07',1,'MESA 16','Quissenda Jose','2024-05-07 10:48:19'),(1660,'Taxa Expresso 100',3250,'2024-05-07',1,'MESA 16','Quissenda Jose','2024-05-07 10:48:21'),(1661,'LAVAR FATO SOCIAL',3500,'2024-05-07',1,'MESA 16','Quissenda Jose','2024-05-07 13:33:25'),(1662,'Lavar Toalhas de Banho Média',1800,'2024-05-07',6,'MESA 16','Quissenda Jose','2024-05-07 16:22:38'),(1663,'Lavar Fronha',1300,'2024-05-07',22,'MESA 16','Quissenda Jose','2024-05-07 16:22:43'),(1664,'Taxa Expresso 100',39400,'2024-05-07',1,'MESA 16','Quissenda Jose','2024-05-07 16:22:46'),(1665,'Lavar Calça Social',1750,'2024-05-08',1,'MESA 16','Evaristo Araujo','2024-05-08 09:42:24'),(1666,'Camisa social',1500,'2024-05-08',1,'MESA 16','Evaristo Araujo','2024-05-08 09:42:44'),(1667,'Taxa Expresso 100',5200,'2024-05-08',1,'MESA 16','Evaristo Araujo','2024-05-08 11:59:19'),(1668,'Lavar Pijama',2500,'2024-05-08',1,'MESA 16','Evaristo Araujo','2024-05-08 12:00:25'),(1669,'LAVAR FATO SOCIAL',3500,'2024-05-08',1,'MESA 16','Quissenda Jose','2024-05-08 14:55:37'),(1670,'Taxa de Urgência 50',3950,'2024-05-08',1,'MESA 16','Quissenda Jose','2024-05-08 15:26:39'),(1671,'Lavar Vestido Bata',6000,'2024-05-9',1,'MESA 16','Eltone Ndongala','2024-05-09 10:59:28'),(1672,'Lavar Vestido Simples',4500,'2024-05-9',1,'MESA 16','Eltone Ndongala','2024-05-09 11:00:56'),(1673,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-05-9',1,'MESA 16','Maria Cristina','2024-05-09 12:49:07'),(1674,'LAVAR FATO SOCIAL',3500,'2024-05-9',1,'MESA 16','Maria Cristina','2024-05-09 13:51:04'),(1675,'Lavar Gravata Normal',1500,'2024-05-9',1,'MESA 16','Maria Cristina','2024-05-09 13:51:08'),(1676,'Camisa social',1500,'2024-05-9',1,'MESA 16','Maria Cristina','2024-05-09 13:51:11'),(1677,'Taxa de Urgência 50',3250,'2024-05-9',1,'MESA 16','Maria Cristina','2024-05-09 13:51:13'),(1678,'Lavar Edredon Casal',9500,'2024-05-9',1,'MESA 16','Quissenda Jose','2024-05-09 15:33:52'),(1679,'LAVAR FATO SOCIAL',3500,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 08:35:56'),(1680,'Engomar Pijama',1500,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 08:36:17'),(1681,'Lavar macacão jardineira',5500,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 09:12:18'),(1682,'Lavar Vestido Fato',5250,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 09:50:29'),(1683,'LAVAR FATO SOCIAL',3500,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 10:06:48'),(1684,'Lavar Saia de Linho',3000,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 12:09:00'),(1685,'LAVAR SAIA DE NOIVA CURTA ',5000,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 12:09:08'),(1686,'Lavar Saia de Linho',3000,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 12:10:01'),(1687,'LAVAR FATO SOCIAL',3500,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 12:42:01'),(1688,'LAVAR FATO SOCIAL',3500,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 12:42:06'),(1689,'Camisa social',1500,'2024-05-10',1,'MESA 16','Maria Cristina','2024-05-10 14:15:37'),(1690,'LAVAR FATO SOCIAL',3500,'2024-05-10',1,'MESA 16','Quissenda Jose','2024-05-10 15:18:12'),(1691,'LAVAR FATO SOCIAL',3500,'2024-05-10',1,'MESA 16','Quissenda Jose','2024-05-10 15:23:51'),(1692,'Lavar Blaser',2200,'2024-05-10',1,'MESA 16','Quissenda Jose','2024-05-10 15:25:12'),(1693,'LAVAR FATO SOCIAL',3500,'2024-05-10',1,'MESA 16','Quissenda Jose','2024-05-10 18:10:47'),(1694,'LAVAR FATO SOCIAL',3500,'2024-05-10',1,'MESA 16','Quissenda Jose','2024-05-10 19:27:33'),(1695,'LAVAR FATO SOCIAL',3500,'2024-05-10',1,'MESA 16','Quissenda Jose','2024-05-10 19:27:36'),(1696,'engomar Fato Social',1700,'2024-05-10',1,'MESA 16','Quissenda Jose','2024-05-10 19:31:13'),(1697,'Lavar Tshirt normal',1500,'2024-05-10',1,'MESA 16','Quissenda Jose','2024-05-10 19:35:27'),(1698,'Lavar Blaser',2200,'2024-05-10',1,'MESA 16','Quissenda Jose','2024-05-10 19:43:29'),(1699,'LAVAR FATO SOCIAL',3500,'2024-05-11',1,'MESA 16','Maria Cristina','2024-05-11 08:43:02'),(1700,'Camisa social',1500,'2024-05-11',1,'MESA 16','Maria Cristina','2024-05-11 09:32:10'),(1701,'Lavar Calça Social',1750,'2024-05-11',1,'MESA 16','Maria Cristina','2024-05-11 09:37:46'),(1702,'Lavar Calça Ganga',1800,'2024-05-11',1,'MESA 16','Maria Cristina','2024-05-11 11:32:11'),(1703,'Lavar Calça Ganga',1800,'2024-05-11',1,'MESA 16','Maria Cristina','2024-05-11 11:32:16'),(1704,'Lavar Tshirt normal',1500,'2024-05-11',1,'MESA 16','Maria Cristina','2024-05-11 11:36:40'),(1705,'Taxa Expresso 100',1500,'2024-05-11',1,'MESA 16','Maria Cristina','2024-05-11 11:41:36'),(1706,'Lavar Blusa de Seda',2000,'2024-05-11',1,'MESA 16','Maria Cristina','2024-05-11 12:13:43'),(1707,'Lavar Blusa de Seda',2000,'2024-05-11',1,'MESA 16','Maria Cristina','2024-05-11 12:13:48'),(1708,'LAVAR FATO SOCIAL',3500,'2024-05-11',1,'MESA 16','Quissenda Jose','2024-05-11 13:08:27'),(1709,'Camisa social',1500,'2024-05-11',1,'MESA 16','Quissenda Jose','2024-05-11 13:25:55'),(1710,'Taxa Expresso 100',4750,'2024-05-11',1,'MESA 16','Quissenda Jose','2024-05-11 13:56:11'),(1711,'ENGOMAR SAIA NORMAL',1500,'2024-05-11',1,'MESA 16','Quissenda Jose','2024-05-11 13:56:15'),(1712,'Engomar Blaser',1500,'2024-05-11',1,'MESA 16','Quissenda Jose','2024-05-11 13:56:17'),(1713,'ENGOMAR BLUSA DE SEDA',1750,'2024-05-11',1,'MESA 16','Quissenda Jose','2024-05-11 13:56:20'),(1714,'Taxa Expresso 100',1750,'2024-05-11',1,'MESA 16','Quissenda Jose','2024-05-11 14:58:48'),(1715,'Lavar Tshirt normal',1500,'2024-05-12',1,'MESA 16','Maria Cristina','2024-05-12 11:24:32'),(1716,'LAVAR FATO SOCIAL',3500,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 08:14:43'),(1717,'FATO DE BLUSA E SAIA ',6000,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 08:33:50'),(1718,'Camisa social',1500,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 11:33:04'),(1719,'Camisa social',1500,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 11:33:09'),(1720,'LAVAR CAMISA NORMAL ',2000,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 11:33:33'),(1721,'LAVAR CAMISA NORMAL ',2000,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 11:33:37'),(1722,'LAVAR CAMISA NORMAL ',2000,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 11:33:41'),(1723,'Lavar Camisa de Linho',2000,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 11:33:45'),(1724,'Lavar Camisa de Linho',2000,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 11:33:50'),(1725,'Lavar Vestido de Noiva(Simples)',20000,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 12:49:31'),(1726,'Lavar Vestido de Gala c/Lantenjolas',10500,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 12:49:33'),(1727,'Calça social femenina',1750,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 12:49:35'),(1728,'ENGOMAR CAMISA SOCIAL',1300,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 12:51:33'),(1729,'ENGOMAR CAMISA SOCIAL',1300,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 12:51:36'),(1730,'Taxa de Urgência 50',1300,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 12:51:39'),(1731,'Taxa de Urgência 50',3875,'2024-05-13',1,'MESA 16','Maria Cristina','2024-05-13 13:09:35'),(1732,'LAVAR FATO SOCIAL',3500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:01:44'),(1733,'Lavar Polo',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:01:46'),(1734,'Lavar Polo',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:01:48'),(1735,'Lavar Polo',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:01:51'),(1736,'Lavar Calça Ganga',1800,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:15'),(1737,'Lavar Polo',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:17'),(1738,'Lavar Polo',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:20'),(1739,'Lavar Tshirt normal',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:24'),(1740,'Lavar Tshirt normal',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:26'),(1741,'Lavar Tshirt normal',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:28'),(1742,'Lavar Tshirt normal',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:30'),(1743,'Lavar Tshirt normal',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:33'),(1744,'Lavar Tshirt normal',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:35'),(1745,'Lavar Tshirt normal',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:38'),(1746,'Camisa social',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:49'),(1747,'Camisa social',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:51'),(1748,'Camisa social',1500,'2024-05-13',1,'MESA 16','Eltone Ndongala','2024-05-13 16:02:56'),(1749,'Lavar Blusa de Seda',2000,'2024-05-13',1,'MESA 16','Ermelinda Samucongo','2024-05-13 16:30:11'),(1750,'Lavar Blusa Simples',1500,'2024-05-13',1,'MESA 16','Ermelinda Samucongo','2024-05-13 16:30:14'),(1751,'Lavar Blusa de Seda',2000,'2024-05-13',1,'MESA 16','Ermelinda Samucongo','2024-05-13 16:30:16'),(1752,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-05-13',1,'MESA 16','Ermelinda Samucongo','2024-05-13 16:30:19'),(1753,'Lavar Vestido Simples',4500,'2024-05-13',1,'MESA 16','Ermelinda Samucongo','2024-05-13 16:30:21'),(1754,'Lavar Tshirt normal',1500,'2024-05-13',1,'MESA 16','Ermelinda Samucongo','2024-05-13 17:21:17'),(1755,'Camisa social',1500,'2024-05-13',1,'MESA 16','Ermelinda Samucongo','2024-05-13 19:49:26'),(1756,'Lavar Polo',1500,'2024-05-13',1,'MESA 16','Ermelinda Samucongo','2024-05-13 20:37:15'),(1757,'BLUSAS DE BEBE ',700,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 08:30:37'),(1758,'Engomar vestido simples',3500,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 08:37:06'),(1759,'Taxa Expresso 100',3500,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 08:37:10'),(1760,'ENGOMAR VESTIDO DE CRIANÇA',1500,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 08:39:11'),(1761,'Lavar Calça Social',1750,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 10:01:44'),(1762,'LAVAR FATO SOCIAL',3500,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 10:29:38'),(1763,'Lavar Camisa de Linho',2000,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 10:29:54'),(1764,'Lavar Camisa de Linho',2000,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 10:29:58'),(1765,'Camisa social',1500,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 10:55:50'),(1766,'Camisa social',1500,'2024-05-14',1,'MESA 16','Maria Cristina','2024-05-14 10:55:52'),(1767,'LAVAR CAMISOLA DE SEDA ',1500,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 15:03:33'),(1768,'Lavar Tshirt normal',1500,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 15:03:38'),(1769,'LAVAR FATO SOCIAL',3500,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 16:22:56'),(1770,'LAVAR FATO SOCIAL',3500,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 17:15:39'),(1771,'Lavar Calça Linho S',2200,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 17:19:24'),(1772,'Lavar Saia Simples',2500,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 17:20:48'),(1773,'LAVAR CAMISOLA VESTIDO',1700,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 18:03:59'),(1774,'LAVAR CAMISOLA VESTIDO',1700,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 18:04:05'),(1775,'LAVAR CAMISOLA VESTIDO',1700,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 18:04:10'),(1776,'LAVAR CAMISOLA NORMAL',1000,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 18:04:19'),(1777,'LAVAR CAMISOLA VESTIDO',1700,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 18:04:26'),(1778,'Lavar Calça Normal',2000,'2024-05-14',1,'MESA 16','Quissenda Jose','2024-05-14 19:41:53'),(1779,'Lavar Vestido de Seda',8000,'2024-05-15',1,'MESA 16','Maria Cristina','2024-05-15 09:01:24'),(1780,'Taxa de Urgência 50',4000,'2024-05-15',1,'MESA 16','Maria Cristina','2024-05-15 09:01:27'),(1781,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-05-15',1,'MESA 16','Maria Cristina','2024-05-15 09:01:50'),(1782,'Taxa de Urgência 50',2750,'2024-05-15',1,'MESA 16','Maria Cristina','2024-05-15 09:01:55'),(1783,'Engomar vestido simples',3500,'2024-05-15',1,'MESA 16','Maria Cristina','2024-05-15 09:27:43'),(1784,'Taxa de Urgência 50',1750,'2024-05-15',1,'MESA 16','Maria Cristina','2024-05-15 09:27:46'),(1785,'Camisa social',1500,'2024-05-15',1,'MESA 16','Maria Cristina','2024-05-15 09:46:01'),(1786,'Camisa social',1500,'2024-05-15',1,'MESA 16','Maria Cristina','2024-05-15 14:10:01'),(1787,'TOALHA VILA GALE',700,'2024-05-15',24,'MESA 16','Quissenda Jose','2024-05-15 15:39:41'),(1788,'COLCHA VILA DA GALE1',1800,'2024-05-15',16,'MESA 16','Quissenda Jose','2024-05-15 15:39:43'),(1789,'Fato Africano',4000,'2024-05-15',1,'MESA 16','Quissenda Jose','2024-05-15 17:34:25'),(1790,'Lavar Casaco Social',1750,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 08:04:58'),(1791,'Taxa de Urgência 50',1625,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 08:05:21'),(1792,'Camisa social',1500,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 08:05:26'),(1793,'Lavar Casaco Social',1750,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 08:05:43'),(1794,'Taxa de Urgência 50',875,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 08:05:55'),(1795,'Casaco social de linho',2000,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 09:08:29'),(1796,'Casaco social de linho',2000,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 09:08:35'),(1797,'Camisa social',1500,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 09:08:37'),(1798,'Camisa social',1500,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 10:48:26'),(1799,'Lavar Casaco Social',1750,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 10:48:29'),(1800,'Camisa social',1500,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 10:51:29'),(1801,'Lavar Casaco Social',1750,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 10:51:31'),(1802,'Taxa de Urgência 50',875,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 10:51:40'),(1803,'engomar Fato Social',1700,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 12:30:17'),(1804,'Taxa de Urgência 50',850,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 12:30:29'),(1805,'engomar Fato Social',1700,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 12:30:47'),(1806,'LAVAR FATO SOCIAL',3500,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 13:52:04'),(1807,'Taxa de Urgência 50',15421.88,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 13:56:48'),(1808,'Lavar Vestido Simples',4500,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 14:27:39'),(1809,'Lavar Calção Normal',1500,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 14:29:14'),(1810,'Lavar Calção Linho',2000,'2024-05-16',1,'MESA 16','Maria Cristina','2024-05-16 14:29:18'),(1811,'FATO DE BLUSA E SAIA ',6000,'2024-05-16',1,'MESA 16','Quissenda Jose','2024-05-16 16:15:03'),(1812,'Camisa social',1500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 09:39:06'),(1813,'Camisa social',1500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 09:39:25'),(1814,'Camisa social',1500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 09:39:42'),(1815,'Camisa social',1500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 09:40:21'),(1816,'Camisa social',1500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 09:40:40'),(1817,'Lav. COLETE',1500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 10:24:02'),(1818,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:20'),(1819,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:22'),(1820,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:25'),(1821,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:27'),(1822,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:30'),(1823,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:32'),(1824,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:38'),(1825,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:41'),(1826,'Lavar Edredon Solteiro',8000,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:43'),(1827,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:45'),(1828,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:48'),(1829,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:50'),(1830,'Lavar Edredon Casal',9500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 12:42:53'),(1831,'Lavar Gravata Normal',1500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 13:13:06'),(1832,'Taxa de Urgência 50',7975,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 13:42:33'),(1833,'Lavar Calça Social',1750,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 13:42:38'),(1834,'Lavar Calça Linho S',2200,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 13:42:40'),(1835,'Camisa social',1500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 13:42:47'),(1836,'LAVAR FATO SOCIAL',3500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 13:42:50'),(1837,'LAVAR FATO SOCIAL',3500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 13:42:52'),(1838,'LAVAR FATO SOCIAL',3500,'2024-05-17',1,'MESA 16','Maria Cristina','2024-05-17 13:42:55'),(1839,'Taxa de Urgência 50',61000,'2024-05-17',1,'MESA 16','Quissenda Jose','2024-05-17 14:25:52'),(1840,'Camisa social',1500,'2024-05-17',1,'MESA 16','Quissenda Jose','2024-05-17 18:49:39'),(1841,'Lavar Calção Normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 09:43:01'),(1842,'Lavar Colcha Solteiro',6500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 09:49:57'),(1843,'LAVAR FATO SOCIAL',3500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 09:57:38'),(1844,'Lavar Fato de Linho H',4000,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 10:21:53'),(1845,'Lavar Tshirt normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 11:56:07'),(1846,'Lavar Camisa Branca (Muito Suja)',1700,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:03:49'),(1847,'Lavar Calça Social',1750,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:07'),(1848,'Lavar Tshirt normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:09'),(1849,'Lavar Blusa de ALGODÃO',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:12'),(1850,'LAVAR CAMISOLA DE SEDA ',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:16'),(1851,'Lavar Tshirt normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:18'),(1852,'Lavar Tshirt normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:21'),(1853,'Lavar Tshirt normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:23'),(1854,'Lavar Polo',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:25'),(1855,'Lavar Calça Ganga',1800,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:28'),(1856,'Lavar Calça Ganga',1800,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:30'),(1857,'Lavar Calção Seda',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:33'),(1858,'Lavar Blaser',2200,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:36'),(1859,'LAVAR TOP',1000,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:38'),(1860,'Camisa social',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:04:41'),(1861,'Taxa de Urgência 50',17812.5,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:06:41'),(1862,'Lavar Camisa Branca (Muito Suja)',1700,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:09:39'),(1863,'Lavar Calção Seda',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:09:42'),(1864,'Lavar Calça Social',1750,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:09:45'),(1865,'Lavar Calça Ganga',1800,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:09:47'),(1866,'Lavar Calça Ganga',1800,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:09:50'),(1867,'Lavar Polo',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:09:53'),(1868,'Lavar Tshirt normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:09:56'),(1869,'Lavar Tshirt normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:09:59'),(1870,'Lavar Tshirt normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:10:02'),(1871,'LAVAR CAMISOLA DE SEDA ',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:10:07'),(1872,'LAVAR TOP',1000,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:10:09'),(1873,'Lavar Blusa de ALGODÃO',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:10:12'),(1874,'Lavar Tshirt normal',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:10:15'),(1875,'Lavar Blaser',2200,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:10:17'),(1876,'Camisa social',1500,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:10:20'),(1877,'Lavar Casaco Social',1750,'2024-05-18',1,'MESA 16','Quissenda Jose','2024-05-18 12:49:20'),(1878,'Taxa Expresso 100',1500,'2024-05-18',1,'MESA 16','Maria Cristina','2024-05-18 14:24:15'),(1879,'Lavar Lençois King',5000,'2024-05-18',1,'MESA 16','Maria Cristina','2024-05-18 15:43:20'),(1880,'Camisa social',1500,'2024-05-18',1,'MESA 16','Maria Cristina','2024-05-18 17:21:56'),(1881,'Lavar Calça Normal',2000,'2024-05-20',1,'MESA 16','Maria Cristina','2024-05-20 11:09:01'),(1882,'FATO DE CRIANCA DE 1 A 12 ANOS\n\n',2500,'2024-05-20',1,'MESA 16','Maria Cristina','2024-05-20 12:26:16'),(1883,'Lavar Calção Normal',1500,'2024-05-20',1,'MESA 16','Maria Cristina','2024-05-20 13:02:37'),(1884,'Lavar Casaco Social',1750,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 16:12:05'),(1885,'Camisa de seda',1700,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 18:37:43'),(1886,'Lavar Cortina Black Out p/metro',3000,'2024-05-20',7,'MESA 16','Ermelinda Samucongo','2024-05-20 18:45:51'),(1887,'Lavar Cortina Black Out p/metro',3000,'2024-05-20',7,'MESA 16','Ermelinda Samucongo','2024-05-20 18:46:02'),(1888,'CORTINAS 1',10000,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 18:46:17'),(1889,'Lavar Fato de Ganga',3500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 20:49:11'),(1890,'Vestido simples de festa',7500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 20:52:53'),(1891,'Lavar Toalhas de Banho Grande',2500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 20:55:07'),(1892,'Taxa Expresso 100',46000,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 20:58:17'),(1893,'LAVAR FATO SOCIAL',3500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 20:58:44'),(1894,'LAVAR FATO SOCIAL',3500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:10'),(1895,'Lavar Calça Social',1750,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:19'),(1896,'Lavar Calça Social',1750,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:22'),(1897,'LAVAR POLOVER NORMAL',2000,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:24'),(1898,'Camisa social',1500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:32'),(1899,'Camisa social',1500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:35'),(1900,'Camisa social',1500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:38'),(1901,'Lavar Vestido de Gala c/Lantenjolas',10500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:44'),(1902,'Lavar Vestido de Seda',8000,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:47'),(1903,'Vestido simples de festa',7500,'2024-05-20',1,'MESA 16','Ermelinda Samucongo','2024-05-20 21:00:53'),(1904,'Lavar Vestido Bubu',6500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:03'),(1905,'Lavar Vestido Simples',4500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:06'),(1906,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:08'),(1907,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:10'),(1908,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:13'),(1909,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:16'),(1910,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:18'),(1911,'Polo muito suja',1800,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:20'),(1912,'Camisa social',1500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:22'),(1913,'Camisa social',1500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:50'),(1914,'BODES DE BEBE ',1100,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:54'),(1915,'Camisa social',1500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:57'),(1916,'Camisa social',1500,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:46:59'),(1917,'Casaco social de linho',2000,'2024-05-21',1,'MESA 16','Eltone Ndongala','2024-05-21 08:47:02'),(1918,'LAVAR FATO SOCIAL',3500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:07:58'),(1919,'Lavar Casaco Social',1750,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:16:34'),(1920,'Lavar Blaser',2200,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:16:39'),(1921,'Lavar Calção Normal',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:18:47'),(1922,'Lavar Calção Normal',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:19:43'),(1923,'CALCAO DE NAPA',3500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:19:45'),(1924,'Lavar Blaser',2200,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:19:48'),(1925,'ENGOMAR CALÇÃO NORMAL',1000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:20:02'),(1926,'ENGOMAR CALÇÃO NORMAL',1000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:20:21'),(1927,'Engomar Blaser',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 09:20:23'),(1928,'Lavar Polo',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:32:15'),(1929,'Polo muito suja',1800,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:22'),(1930,'BODES DE BEBE ',1100,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:28'),(1931,'Lavar Vestido Simples',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:32'),(1932,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:35'),(1933,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:38'),(1934,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:43'),(1935,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:47'),(1936,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:50'),(1937,'Lavar Tshirt normal',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:54'),(1938,'Camisa social',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:36:57'),(1939,'Camisa social',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:37:00'),(1940,'Camisa social',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:37:03'),(1941,'Camisa social',1500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:37:06'),(1942,'Casaco social de linho',2000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 10:37:10'),(1943,'Lavar Cortina de Cozinha p/metro',2900,'2024-05-21',8,'MESA 16','Maria Cristina','2024-05-21 11:50:48'),(1944,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:16'),(1945,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:19'),(1946,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:21'),(1947,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:24'),(1948,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:26'),(1949,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:28'),(1950,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:33'),(1951,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:36'),(1952,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:38'),(1953,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:41'),(1954,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:43'),(1955,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:47'),(1956,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:49'),(1957,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:56'),(1958,'Lavar Lençol Solteiro 1',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:55:58'),(1959,'Lavar Toalhas de Banho Média',1800,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:01'),(1960,'Lavar Toalhas de Banho Média',1800,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:04'),(1961,'Lavar Toalhas de Banho Média',1800,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:07'),(1962,'Lavar Toalhas de Banho Média',1800,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:12'),(1963,'Lavar Toalhas de Banho Média',1800,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:14'),(1964,'Lavar Lençol Solteiro 2',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:16'),(1965,'Lavar Lençol Solteiro 2',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:18'),(1966,'Lavar Lençol Solteiro 2',3000,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:20'),(1967,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:22'),(1968,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:24'),(1969,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:26'),(1970,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:28'),(1971,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:30'),(1972,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:33'),(1973,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:35'),(1974,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:37'),(1975,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:39'),(1976,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:40'),(1977,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:42'),(1978,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:44'),(1979,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:45'),(1980,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:47'),(1981,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:56:49'),(1982,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:57:03'),(1983,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:57:16'),(1984,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:57:18'),(1985,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:57:20'),(1986,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:57:21'),(1987,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:57:31'),(1988,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:57:34'),(1989,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:57:44'),(1990,'Lavar Cobertor Solteiro',4500,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 12:57:47'),(1991,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:17'),(1992,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:23'),(1993,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:25'),(1994,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:27'),(1995,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:30'),(1996,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:43'),(1997,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:46'),(1998,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:50'),(1999,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:53'),(2000,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:03:56'),(2001,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:04:00'),(2002,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:04:04'),(2003,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:04:07'),(2004,'Lavar Fronha',1300,'2024-05-21',1,'MESA 16','Maria Cristina','2024-05-21 13:04:11'),(2005,'Lavar Cortina Black Out p/metro',3000,'2024-05-21',1,'MESA 16','Ermelinda Samucongo','2024-05-21 13:24:18'),(2006,'Lavar Cortina Black Out p/metro',3000,'2024-05-21',1,'MESA 16','Ermelinda Samucongo','2024-05-21 13:24:22'),(2007,'Lavar Cortina Black Out p/metro',3000,'2024-05-21',1,'MESA 16','Ermelinda Samucongo','2024-05-21 13:26:11'),(2008,'Lavar Cortina Black Out p/metro',3000,'2024-05-21',1,'MESA 16','Ermelinda Samucongo','2024-05-21 13:26:19'),(2009,'Lavar Tapetes p/metros',3500,'2024-05-21',1,'MESA 16','Ermelinda Samucongo','2024-05-21 14:32:35'),(2010,'Lavar Polo',1500,'2024-05-21',1,'MESA 16','Ermelinda Samucongo','2024-05-21 17:43:59'),(2011,'Lavar Lençois Casal 2',4500,'2024-05-22',1,'MESA 16','Maria Cristina','2024-05-22 09:42:54'),(2012,'Lavar Lençol Solteiro 1',3000,'2024-05-22',1,'MESA 16','Maria Cristina','2024-05-22 09:42:58'),(2013,'Lavar Lençol Solteiro 1',3000,'2024-05-22',1,'MESA 16','Maria Cristina','2024-05-22 09:43:01'),(2014,'Lavar Fronha',1300,'2024-05-22',1,'MESA 16','Maria Cristina','2024-05-22 09:50:38'),(2015,'LAVAR FATO SOCIAL',3500,'2024-05-22',1,'MESA 16','Maria Cristina','2024-05-22 09:56:12'),(2016,'Lavar Almofada Pequena',2000,'2024-05-22',1,'MESA 16','Maria Cristina','2024-05-22 10:09:23'),(2017,'Lavar Casaco Social',1750,'2024-05-22',1,'MESA 16','Maria Cristina','2024-05-22 13:00:55'),(2018,'Lavar Casaco Social',1750,'2024-05-22',1,'MESA 16','Maria Cristina','2024-05-22 13:01:00'),(2019,'COLCHA VILA DA GALE1',1800,'2024-05-22',1,'MESA 16','Quissenda Jose','2024-05-22 14:31:08'),(2020,'Taxa Expresso 100',3400,'2024-05-22',1,'MESA 16','Quissenda Jose','2024-05-22 14:59:45'),(2021,'Lavar Camisa Branca (Muito Suja)',1700,'2024-05-22',1,'MESA 16','Quissenda Jose','2024-05-22 14:59:48'),(2022,'Lavar Camisa Branca (Muito Suja)',1700,'2024-05-22',1,'MESA 16','Quissenda Jose','2024-05-22 15:14:14'),(2023,'LAVAR CAMISA NORMAL ',2000,'2024-05-22',1,'MESA 16','Quissenda Jose','2024-05-22 17:06:09'),(2024,'Camisa social',1500,'2024-05-22',1,'MESA 16','Quissenda Jose','2024-05-22 17:45:42'),(2025,'Camisa social',1500,'2024-05-22',1,'MESA 16','Quissenda Jose','2024-05-22 17:46:26'),(2026,'Lavar Calça Social',1750,'2024-05-22',1,'MESA 16','Quissenda Jose','2024-05-22 17:55:19'),(2027,'LAVAR SAIA CURTA ',2000,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 08:20:18'),(2028,'LAVAR FATO SOCIAL',3500,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 08:56:55'),(2029,'Lavar Blusa de Seda',2000,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 08:57:41'),(2030,'Lavar Calção Seda',1500,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 08:57:43'),(2031,'LAVAR FATO SOCIAL',3500,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 08:57:47'),(2032,'LAVAR FATO SOCIAL',3500,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 08:57:49'),(2033,'Lavar Blaser',2200,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 08:57:52'),(2034,'VESTIDO DE NOITE SIMPLES ',6000,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 09:35:15'),(2035,'Taxa de Urgência 50',3000,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 09:39:11'),(2036,'Taxa Expresso 100',9000,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 10:51:36'),(2037,'Lavar Blusa de Seda',2000,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 10:51:39'),(2038,'LAVAR FATO SOCIAL',3500,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 10:51:41'),(2039,'LAVAR FATO SOCIAL',3500,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 10:51:44'),(2040,'Lavar Casaco Social',1750,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 11:03:29'),(2041,'Lavar Lençois King',5000,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 11:40:31'),(2042,'Lavar Colcha Solteiro',6500,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 11:41:18'),(2043,'Lavar Polo',1500,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 11:45:11'),(2044,'Lavar Calça Ganga',1800,'2024-05-23',1,'MESA 16','Maria Cristina','2024-05-23 11:45:13'),(2045,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 16:45:17'),(2046,'Vestido simples de festa',7500,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 17:02:46'),(2047,'Vestido simples de festa',7500,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 17:04:21'),(2048,'Lavar Calça Ganga',1800,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 17:26:02'),(2049,'Lavar Vestido de Seda',8000,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 17:44:03'),(2050,'Lavar Calça Normal',2000,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 18:38:18'),(2051,'Lavar Calça Normal',2000,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 18:38:38'),(2052,'Lavar Calça Normal',2000,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 18:38:40'),(2053,'Lavar Calça Normal',2000,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 18:38:43'),(2054,'Lavar Ropão/Robe',4500,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 18:53:33'),(2055,'Lavar Ropão/Robe',4500,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 18:53:35'),(2056,'Lavar Ropão/Robe',4500,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 18:53:38'),(2057,'Lavar Toalhas de Banho Média',1800,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 18:53:41'),(2058,'Lavar Cobertor Casal',5500,'2024-05-23',1,'MESA 16','Ermelinda Samucongo','2024-05-23 18:56:16'),(2059,'Lavar Forro de Cama/Colchão',5000,'2024-05-23',24,'MESA 16','Ermelinda Samucongo','2024-05-23 18:57:29'),(2060,'Lavar Toalhas de Banho Média',1800,'2024-05-23',5,'MESA 16','Ermelinda Samucongo','2024-05-23 19:11:32'),(2061,'Lavar Colcha Solteiro',6500,'2024-05-23',24,'MESA 16','Ermelinda Samucongo','2024-05-23 19:11:42'),(2062,'Lavar Lençois Casal 2',4500,'2024-05-23',9,'MESA 16','Ermelinda Samucongo','2024-05-23 19:11:46'),(2063,'Lavar Cortina Linho Fino p/metro',3500,'2024-05-26',1,'MESA 16','Maria Cristina','2024-05-24 10:50:57'),(2064,'FATO OLIMPICO DE CALCA E TSHIRT',3000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 10:51:42'),(2065,'FATO OLIMPICO DE CALCA E TSHIRT',3000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 10:51:44'),(2066,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 10:57:27'),(2067,'Lavar Polo',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 10:58:31'),(2068,'Lavar Casaco Olimpico',2000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:12'),(2069,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:14'),(2070,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:17'),(2071,'LAVAR POLOVER NORMAL',2000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:21'),(2072,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:23'),(2073,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:26'),(2074,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:28'),(2075,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:33'),(2076,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:35'),(2077,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:38'),(2078,'Lavar Tshirt normal',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:04:40'),(2079,'LAVAR CAMISOLA NORMAL',1000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:05:10'),(2080,'Lavar Camisola Interior H',900,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:05:25'),(2081,'LAVAR CAMISOLA NORMAL',1000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:05:27'),(2082,'FATO OLIMPICO DE CALCA E CASACO',4000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:05:29'),(2083,'FATO OLIMPICO DE CALCA E TSHIRT',3000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:05:47'),(2084,'FATO OLIMPICO DE CALCA E TSHIRT',3000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:05:49'),(2085,'FATO OLIMPICO DE CALCA E CASACO',4000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:05:53'),(2086,'Tshirt normal muito suja',1800,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:05:55'),(2087,'FATO OLIMPICO DE CALCA E TSHIRT',3000,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:05:58'),(2088,'Lavar Blaser',2200,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:13:44'),(2089,'Lavar Blusa Simples',1500,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:18:18'),(2090,'Calça social femenina',1750,'2024-05-24',1,'MESA 16','Maria Cristina','2024-05-24 11:23:44'),(2091,'Camisa social',1500,'2024-05-24',1,'MESA 16','Quissenda Jose','2024-05-24 15:07:14'),(2092,'LAVAR FATO SOCIAL',3500,'2024-05-25',1,'MESA 16','Maria Cristina','2024-05-25 11:26:34'),(2093,'Lavar Fato de Treino',2500,'2024-05-25',1,'MESA 16','Maria Cristina','2024-05-25 11:27:18'),(2094,'LAVAR FATO SOCIAL',3500,'2024-05-25',1,'MESA 16','Maria Cristina','2024-05-25 11:27:26'),(2095,'Taxa Expresso 100',1750,'2024-05-25',1,'MESA 16','Maria Cristina','2024-05-25 12:52:50'),(2096,'Lavar Casaco Social',1750,'2024-05-25',1,'MESA 16','Maria Cristina','2024-05-25 12:52:53'),(2097,'LAVAR FATO SOCIAL',3500,'2024-05-25',1,'MESA 16','Maria Cristina','2024-05-25 13:27:37'),(2098,'Camisa social',1500,'2024-05-25',1,'MESA 16','Maria Cristina','2024-05-25 13:27:39'),(2099,'LAVAR FATO SOCIAL',3500,'2024-05-25',1,'MESA 16','Maria Cristina','2024-05-25 13:27:42'),(2100,'Camisa social',1500,'2024-05-25',1,'MESA 16','Maria Cristina','2024-05-25 13:27:44'),(2101,'Camisa social',1500,'2024-05-25',1,'MESA 16','Quissenda Jose','2024-05-25 17:01:56'),(2102,'Lavar Casaco Social',1750,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 09:31:32'),(2103,'Lavar Casaco Social',1750,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 09:56:53'),(2104,'Lavar Casaco Social',1750,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 11:28:28'),(2105,'Casaco social de linho',2000,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 11:28:38'),(2106,'Casaco social de linho',2000,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 11:28:44'),(2107,'Casaco social de linho',2000,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 11:28:49'),(2108,'LAVAR FATO SOCIAL',3500,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:48:10'),(2109,'LAVAR FATO SOCIAL',3500,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:55:40'),(2110,'Lavar Casaco Social',1750,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:56:23'),(2111,'Lavar Calça Social',1750,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:56:27'),(2112,'Lavar Casaco Social',1750,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:56:31'),(2113,'Lavar Casaco Social',1750,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:56:35'),(2114,'Lavar Fato de Linho H',4000,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:56:41'),(2115,'Lavar Fato de Noivo',5000,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:57:04'),(2116,'Lavar Fato de Noivo',5000,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:57:11'),(2117,'Lavar Fato de Noivo',5000,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:57:30'),(2118,'Lavar Fato de Noivo',5000,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:57:36'),(2119,'LAVAR FATO SOCIAL',3500,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 13:57:51'),(2120,'LAVAR FATO SOCIAL',3500,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 14:00:52'),(2121,'LAVAR FATO SOCIAL',3500,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 14:13:26'),(2122,'Lavar Casaco Social',1750,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 14:16:59'),(2123,'Lavar Casaco Social',1750,'2024-05-27',1,'MESA 16','Maria Cristina','2024-05-27 14:21:02'),(2124,'BODES DE BEBE ',1100,'2024-05-27',1,'MESA 16','Eltone Ndongala','2024-05-27 16:25:50'),(2125,'BODES DE BEBE ',1100,'2024-05-27',1,'MESA 16','Eltone Ndongala','2024-05-27 16:25:53'),(2126,'BODES DE BEBE ',1100,'2024-05-27',1,'MESA 16','Eltone Ndongala','2024-05-27 16:25:56'),(2127,'Lavar Edredon Casal',9500,'2024-05-27',1,'MESA 16','Eltone Ndongala','2024-05-27 16:32:36'),(2128,'Lavar Edredon Solteiro',8000,'2024-05-27',1,'MESA 16','Eltone Ndongala','2024-05-27 16:32:51'),(2129,'Lavar Blusa Simples',1500,'2024-05-27',1,'MESA 16','Eltone Ndongala','2024-05-27 19:34:06'),(2130,'Lavar Calça Social',1750,'2024-05-27',1,'MESA 16','Eltone Ndongala','2024-05-27 19:38:34'),(2131,'Taxa de Urgência 50',7000,'2024-05-27',1,'MESA 16','Eltone Ndongala','2024-05-27 20:52:03'),(2132,'LAVAR FATO SOCIAL',3500,'2024-05-27',1,'MESA 16','Eltone Ndongala','2024-05-27 20:52:33'),(2133,'LAVAR FATO SOCIAL',3500,'2024-05-28',1,'MESA 16','Maria Cristina','2024-05-28 10:52:52'),(2134,'Lavar Calça Social',1750,'2024-05-28',1,'MESA 16','Maria Cristina','2024-05-28 11:13:03'),(2135,'Lavar Edredon King',10000,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 14:13:19'),(2136,'LAVAR FATO SOCIAL',3500,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 15:22:28'),(2137,'Lavar Camisa Branca (Muito Suja)',1700,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 16:23:39'),(2138,'Lavar Camisa Branca (Muito Suja)',1700,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 16:23:44'),(2139,'Camisa de seda',1700,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 16:24:13'),(2140,'Camisa social',1500,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 16:52:54'),(2141,'Camisa social',1500,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 16:53:01'),(2142,'Lavar Calça Social',1750,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 16:53:05'),(2143,'Lavar Camisa Branca (Muito Suja)',1700,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 16:55:06'),(2144,'Polo muito suja',1800,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 18:45:01'),(2145,'Lavar Casaco Social',1750,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 20:40:02'),(2146,'Camisa social',1500,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 20:40:07'),(2147,'Camisa social',1500,'2024-05-28',1,'MESA 16','FRANCISCA DINIS','2024-05-28 20:43:27'),(2148,'Lavar Tshirt normal',1500,'2024-05-29',1,'MESA 16','Maria Cristina','2024-05-29 08:23:12'),(2149,'Taxa Expresso 100',1500,'2024-05-29',1,'MESA 16','Maria Cristina','2024-05-29 08:23:26'),(2150,'Taxa Expresso 100',3500,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-30 09:41:38'),(2151,'Camisa social',1500,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-30 12:22:25'),(2152,'Lavar Calça Social',1750,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-30 12:25:32'),(2153,'Camisa social',1500,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-30 13:47:04'),(2154,'Lavar Camisa de Linho',2000,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-30 13:47:40'),(2155,'Camisa social',1500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 14:03:30'),(2156,'Lavar Edredon Casal',9500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 14:03:32'),(2157,'Camisa social',1500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 14:03:35'),(2158,'Lavar Camisa de Linho',2000,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 14:03:45'),(2159,'Camisa social',1500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 14:03:48'),(2160,'Lavar Calça Social',1750,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 14:03:51'),(2161,'Lavar Calça Linho H',2200,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 14:03:55'),(2162,'Lavar Calção de Ganga',1700,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 14:03:59'),(2163,'LAVAR CASACO DE GANGA',1800,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:20:50'),(2164,'Lavar Casaco Social',1750,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:20:52'),(2165,'LAVAR FATO SOCIAL',3500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:20:55'),(2166,'LAVAR FATO SOCIAL',3500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:20:58'),(2167,'Lavar Casaco Social',1750,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:21:00'),(2168,'LAVAR FATO SOCIAL',3500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:21:03'),(2169,'Lavar Blaser',2200,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:22:28'),(2170,'Lavar Blaser',2200,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:22:37'),(2171,'Lavar Blaser',2200,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:31:40'),(2172,'CASACO DE INVERNO\n',5000,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:31:46'),(2173,'Lavar Blaser',2200,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:31:48'),(2174,'Lavar Vestido Simples',4500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:31:50'),(2175,'Lavar Vestido Simples',4500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 15:32:10'),(2176,'Taxa de Urgência 50',11500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:29:58'),(2177,'Lav. COLETE',1500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:30:00'),(2178,'LAVAR FATO SOCIAL',3500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:30:02'),(2179,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:30:04'),(2180,'VESTIDO DE CRIANCA VOLUM\n\n',5000,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:30:06'),(2181,'Vestido simples de festa',7500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:30:15'),(2182,'Vestido simples de festa',7500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:30:50'),(2183,'Taxa de Urgência 50',3750,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:30:53'),(2184,'Taxa de Urgência 50',2750,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:32:03'),(2185,'Engomar vestido Bubu',5500,'2024-05-30',1,'MESA 16','Eltone Ndongala','2024-05-30 16:32:05'),(2186,'Vestido simples de festa',7500,'2024-05-30',1,'MESA 16','Quissenda Jose','2024-05-30 19:02:48'),(2187,'Taxa de Urgência 50',5625,'2024-05-30',1,'MESA 16','Quissenda Jose','2024-05-30 19:02:50'),(2188,'CASACO DE INVERNO\n',5000,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:02'),(2189,'CASACO DE INVERNO\n',5000,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:08'),(2190,'CASACO DE INVERNO\n',5000,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:12'),(2191,'CASACO DE INVERNO\n',5000,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:26'),(2192,'CASACO DE INVERNO\n',5000,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:29'),(2193,'CASACO DE INVERNO\n',5000,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:33'),(2194,'LAVAR CASACO DE GANGA',1800,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:38'),(2195,'CASACO DE INVERNO\n',5000,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:45'),(2196,'Lavar Casaco Social',1750,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:54'),(2197,'CASACO DE INVERNO\n',5000,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:54:58'),(2198,'Lavar Casaco Social',1750,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:55:02'),(2199,'LAVAR FATO SOCIAL',3500,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:55:13'),(2200,'LAVAR FATO SOCIAL',3500,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:55:16'),(2201,'LAVAR FATO SOCIAL',3500,'2024-05-30',1,'MESA 16','Maria Cristina','2024-05-31 06:55:20'),(2202,'Taxa Expresso 100',4300,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 08:57:01'),(2203,'Lavar Fato Olimpico',2800,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 08:57:07'),(2204,'ENGOMAR CAMISA SOCIAL',1300,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 09:43:20'),(2205,'ENGOMAR CAMISA SOCIAL',1300,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 09:44:00'),(2206,'Taxa Expresso 100',4100,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 09:44:10'),(2207,'ENGOMAR CALÇA SOCIAL',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 09:58:23'),(2208,'Taxa Expresso 100',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 09:58:25'),(2209,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 10:02:06'),(2210,'Lavar Calção de Ganga',1700,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 10:04:29'),(2211,'Lavar Calça Normal',2000,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 10:06:20'),(2212,'Taxa Expresso 100',27800,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 11:40:13'),(2213,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:28:31'),(2214,'Lavar Calça Ganga',1800,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:28:34'),(2215,'Lavar Calça Social',1750,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:00'),(2216,'Lavar Calça Ganga',1800,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:04'),(2217,'Lavar Calça Normal',2000,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:07'),(2218,'Lavar Calça Normal',2000,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:11'),(2219,'Lavar Calça Normal',2000,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:14'),(2220,'Camisa social',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:16'),(2221,'Lavar Calção de Ganga',1700,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:22'),(2222,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:25'),(2223,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:28'),(2224,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:33'),(2225,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:36'),(2226,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:39'),(2227,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:41'),(2228,'Lavar Polo',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:43'),(2229,'Lavar Polo',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:45'),(2230,'Lavar Polo',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:47'),(2231,'Lavar Polo',1500,'2024-05-31',1,'MESA 16','Maria Cristina','2024-05-31 12:29:49'),(2232,'Taxa de Urgência 50',2250,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:29:38'),(2233,'Camisa social',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:46:47'),(2234,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:46:55'),(2235,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:46:57'),(2236,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:47:00'),(2237,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:47:03'),(2238,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:47:06'),(2239,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:47:09'),(2240,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:47:13'),(2241,'Lavar Gravata de Seda',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:48:00'),(2242,'Lavar Gravata de Seda',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:48:08'),(2243,'Lavar Gravata de Seda',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:48:20'),(2244,'Lavar Gravata de Seda',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:48:31'),(2245,'Lavar Calça Social',1750,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:48:49'),(2246,'Lavar Calça Social',1750,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:48:58'),(2247,'Lavar Calça Ganga',1800,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:49:17'),(2248,'Camisa social',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:49:32'),(2249,'Camisa social',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:49:36'),(2250,'Camisa social',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:49:40'),(2251,'Camisa social',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:49:44'),(2252,'Camisa social',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:49:48'),(2253,'Camisa social',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:49:54'),(2254,'Lavar Calça Ganga',1800,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 13:50:50'),(2255,'VESTIDO DE CRIANCA ',3000,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:02:45'),(2256,'Lavar Blaser',2200,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:06:05'),(2257,'Lavar Calça Social',1750,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:06:38'),(2258,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:17:36'),(2259,'Lavar Calça Social',1750,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:23:59'),(2260,'Lavar Tshirt normal',1500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:24:29'),(2261,'Tshirt normal muito suja',1800,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:25:09'),(2262,'Lavar Tapetes p/metros',3500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:27:24'),(2263,'URGENCIA DE CAMISA ',750,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:33:11'),(2264,'LAVAR POLOVER NORMAL',2000,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:33:30'),(2265,'LAVAR FATO SOCIAL',3500,'2024-05-31',1,'MESA 16','Eltone Ndongala','2024-05-31 14:55:54'),(2266,'Taxa de Urgência 50',1750,'2024-05-31',1,'MESA 16','Quissenda Jose','2024-05-31 15:08:05'),(2267,'Lavar Edredon Solteiro',8000,'2024-05-31',1,'MESA 16','Quissenda Jose','2024-05-31 15:31:52'),(2268,'Lavar Edredon Casal',9500,'2024-05-31',1,'MESA 16','Quissenda Jose','2024-05-31 15:34:59'),(2269,'Lavar Blaser',2200,'2024-05-31',1,'MESA 16','Quissenda Jose','2024-05-31 19:39:08'),(2270,'Taxa de Urgência 50',2625,'2024-06-01',1,'MESA 16','Maria Cristina','2024-06-01 08:09:26'),(2271,'LAVAR FATO SOCIAL',3500,'2024-06-01',1,'MESA 16','Maria Cristina','2024-06-01 08:09:28'),(2272,'engomar Fato Social',1700,'2024-06-01',1,'MESA 16','Maria Cristina','2024-06-01 13:01:25'),(2273,'LAVAR FATO SOCIAL',3500,'2024-06-01',1,'MESA 16','Quissenda Jose','2024-06-01 13:54:15'),(2274,'Lavar Calção Normal',1500,'2024-06-01',1,'MESA 16','Quissenda Jose','2024-06-01 14:54:48'),(2275,'Lavar Calça Social',1750,'2024-06-01',1,'MESA 16','Quissenda Jose','2024-06-01 14:56:59'),(2276,'Lavar Calça Social',1750,'2024-06-01',1,'MESA 16','Quissenda Jose','2024-06-01 14:57:02'),(2277,'Lavar Fronha',1300,'2024-06-01',1,'MESA 16','Quissenda Jose','2024-06-01 18:29:12'),(2278,'Lavar Tshirt normal',1500,'2024-06-01',1,'MESA 16','Quissenda Jose','2024-06-01 18:29:56'),(2279,'Taxa Expresso 100',3000,'2024-06-03',1,'MESA 16','Maria Cristina','2024-06-03 08:18:35'),(2280,'Casaco social de linho',2000,'2024-06-03',1,'MESA 16','Maria Cristina','2024-06-03 11:27:03'),(2281,'COLCHA SOLTEIRO ',6500,'2024-06-03',9,'MESA 16','Eltone Ndongala','2024-06-03 12:04:26'),(2282,'COLCHA SOLTEIRO ',6500,'2024-06-03',7,'MESA 16','Eltone Ndongala','2024-06-03 12:04:30'),(2283,'COLCHA SOLTEIRO ',6500,'2024-06-03',5,'MESA 16','Eltone Ndongala','2024-06-03 12:04:33'),(2284,'Lavar Toalhas de Banho Pequena',1300,'2024-06-03',21,'MESA 16','Eltone Ndongala','2024-06-03 12:09:15'),(2285,'Lavar Lençois Casal 2',4500,'2024-06-03',1,'MESA 16','Eltone Ndongala','2024-06-03 12:14:26'),(2286,'Lavar Colcha Solteiro',6500,'2024-06-03',16,'MESA 16','Eltone Ndongala','2024-06-03 12:17:02'),(2287,'Lavar Toalhas de Banho Média',1800,'2024-06-03',24,'MESA 16','Eltone Ndongala','2024-06-03 12:17:04'),(2288,'Lavar Fato de Noivo',5000,'2024-06-03',1,'MESA 16','Maria Cristina','2024-06-03 12:18:27'),(2289,'Taxa Expresso 100',9500,'2024-06-03',1,'MESA 16','Maria Cristina','2024-06-03 12:26:20'),(2290,'Taxa de Urgência 50',4750,'2024-06-03',1,'MESA 16','Maria Cristina','2024-06-03 12:26:39'),(2291,'Lavar Vestido de Gala c/Lantenjolas',10500,'2024-06-03',1,'MESA 16','Quissenda Jose','2024-06-03 14:26:12'),(2292,'Lavar Saia Simples',2500,'2024-06-03',1,'MESA 16','Quissenda Jose','2024-06-03 14:26:16'),(2293,'Lavar Vestido de Gala c/Lantenjolas',10500,'2024-06-03',1,'MESA 16','Quissenda Jose','2024-06-03 15:47:16'),(2294,'Lavar Vestido de Seda',8000,'2024-06-03',1,'MESA 16','Quissenda Jose','2024-06-03 15:50:59'),(2295,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-06-03',1,'MESA 16','Quissenda Jose','2024-06-03 15:51:07'),(2296,'Camisa social',1500,'2024-06-03',1,'MESA 16','Margarida Casimiro','2024-06-03 18:17:49'),(2297,'Lavar Casaco Social',1750,'2024-06-03',1,'MESA 16','Margarida Casimiro','2024-06-03 20:13:06'),(2298,'Casaco social de linho',2000,'2024-06-04',1,'MESA 16','Maria Cristina','2024-06-04 07:48:06'),(2299,'Lavar Boxer',3500,'2024-06-04',1,'MESA 16','Maria Cristina','2024-06-04 08:13:15'),(2300,'Lavar Blaser',2200,'2024-06-04',1,'MESA 16','Maria Cristina','2024-06-04 11:32:36'),(2301,'Lavar Casaco de Napa',4000,'2024-06-04',1,'MESA 16','Maria Cristina','2024-06-04 11:32:41'),(2302,'Lavar Casaco de Napa',4000,'2024-06-04',1,'MESA 16','Maria Cristina','2024-06-04 11:32:45'),(2303,'Lavar Casaco Olimpico',2000,'2024-06-04',1,'MESA 16','Maria Cristina','2024-06-04 11:32:50'),(2304,'Taxa Expresso 100',3500,'2024-06-04',1,'MESA 16','Maria Cristina','2024-06-04 11:52:52'),(2305,'Fato Africano',4000,'2024-06-04',1,'MESA 16','Margarida Casimiro','2024-06-04 17:04:48'),(2306,'Lavar Calça Ganga',1800,'2024-06-04',1,'MESA 16','Margarida Casimiro','2024-06-04 17:25:48'),(2307,'Lavar Lençois Casal 1',4000,'2024-06-04',1,'MESA 16','Margarida Casimiro','2024-06-04 19:21:14'),(2308,'Lavar Calça Social',1750,'2024-06-05',1,'MESA 16','Maria Cristina','2024-06-05 11:04:29'),(2309,'Lavar Camisa Branca (Muito Suja)',1700,'2024-06-05',1,'MESA 16','Maria Cristina','2024-06-05 11:06:25'),(2310,'Camisa social',1500,'2024-06-05',1,'MESA 16','Maria Cristina','2024-06-05 11:06:43'),(2311,'Camisa social',1500,'2024-06-05',1,'MESA 16','Maria Cristina','2024-06-05 11:06:46'),(2312,'Camisa social',1500,'2024-06-05',1,'MESA 16','Maria Cristina','2024-06-05 11:06:50'),(2313,'Lavar Calça Social',1750,'2024-06-05',1,'MESA 16','Maria Cristina','2024-06-05 11:06:53'),(2314,'Lavar Calça Linho H',2200,'2024-06-05',1,'MESA 16','Maria Cristina','2024-06-05 11:06:55'),(2315,'Lavar Calça Linho H',2200,'2024-06-05',1,'MESA 16','Maria Cristina','2024-06-05 11:06:58'),(2316,'Lavar Calça Linho S',2200,'2024-06-05',1,'MESA 16','Margarida Casimiro','2024-06-05 14:11:38'),(2317,'Lavar Cortina Solteira(impar)  p/metro',3300,'2024-06-05',1,'MESA 16','Margarida Casimiro','2024-06-05 15:47:09'),(2318,'Lavar Cortina Solteira(impar)  p/metro',3300,'2024-06-05',1,'MESA 16','Margarida Casimiro','2024-06-05 15:49:22'),(2319,'LAVAR FATO SOCIAL',3500,'2024-06-05',1,'MESA 16','Margarida Casimiro','2024-06-05 17:07:41'),(2320,'LAVAR FATO SOCIAL',3500,'2024-06-05',1,'MESA 16','Margarida Casimiro','2024-06-05 17:39:41'),(2321,'Lavar Casaco Social',1750,'2024-06-05',1,'MESA 16','Margarida Casimiro','2024-06-05 17:41:23'),(2322,'Taxa Expresso 100',1750,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 08:09:18'),(2323,'Taxa de Urgência 50',875,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 08:09:34'),(2324,'ENGOMAR CALÇA SOCIAL',1500,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 09:45:58'),(2325,'Taxa Expresso 100',1500,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 09:46:01'),(2326,'Taxa de Urgência 50',1500,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 10:07:23'),(2327,'LAVAR FATO SOCIAL',3500,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 11:45:09'),(2328,'Lavar Polo',1500,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 11:54:07'),(2329,'Lavar Polo',1500,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 11:54:09'),(2330,'Camisa social',1500,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 11:54:11'),(2331,'Lavar Lençol Solteiro 1',3000,'2024-06-06',1,'MESA 16','Maria Cristina','2024-06-06 11:57:39'),(2332,'Lavar Vestido Simples',4500,'2024-06-06',1,'MESA 16','Margarida Casimiro','2024-06-06 13:14:13'),(2333,'Camisa de seda',1700,'2024-06-06',1,'MESA 16','Margarida Casimiro','2024-06-06 14:42:05'),(2334,'CORTINAS BLACK OUT',14500,'2024-06-06',1,'MESA 16','Margarida Casimiro','2024-06-06 15:35:31'),(2335,'Lavar Colcha Casal',8000,'2024-06-06',1,'MESA 16','Margarida Casimiro','2024-06-06 15:35:47'),(2336,'LAVAR FATO SOCIAL',3500,'2024-06-06',1,'MESA 16','Margarida Casimiro','2024-06-06 17:47:59'),(2337,'Lavar Tshirt normal',1500,'2024-06-06',1,'MESA 16','Margarida Casimiro','2024-06-06 19:03:30'),(2338,'Lav. COLETE',1500,'2024-06-07',1,'MESA 16','Eltone Ndongala','2024-06-07 09:12:41'),(2339,'Taxa Expresso 100',10000,'2024-06-07',1,'MESA 16','Eltone Ndongala','2024-06-07 09:16:40'),(2340,'LENCO DE BOLSO ',500,'2024-06-07',1,'MESA 16','Eltone Ndongala','2024-06-07 09:17:15'),(2341,'Lavar Vestido Fato',5250,'2024-06-07',1,'MESA 16','Margarida Casimiro','2024-06-07 13:47:03'),(2342,'Lavar Camisa de Linho',2000,'2024-06-07',1,'MESA 16','Margarida Casimiro','2024-06-07 14:00:13'),(2343,'Camisa social',1500,'2024-06-07',1,'MESA 16','Margarida Casimiro','2024-06-07 19:59:41'),(2344,'Taxa Expresso 100',5250,'2024-06-08',1,'MESA 16','Maria Cristina','2024-06-08 10:53:06'),(2345,'Taxa Expresso 100',2800,'2024-06-08',1,'MESA 16','Maria Cristina','2024-06-08 11:39:04'),(2346,'Lavar Camisa Branca (Muito Suja)',1700,'2024-06-08',1,'MESA 16','Maria Cristina','2024-06-08 12:19:25'),(2347,'Lavar Camisa Branca (Muito Suja)',1700,'2024-06-08',1,'MESA 16','Maria Cristina','2024-06-08 12:19:29'),(2348,'Lavar Camisa Branca (Muito Suja)',1700,'2024-06-08',1,'MESA 16','Maria Cristina','2024-06-08 12:19:32'),(2349,'Lavar Camisa Branca (Muito Suja)',1700,'2024-06-08',1,'MESA 16','Maria Cristina','2024-06-08 12:19:36'),(2350,'Lavar Camisa Branca (Muito Suja)',1700,'2024-06-08',1,'MESA 16','Maria Cristina','2024-06-08 12:19:42'),(2351,'Camisa social',1500,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 13:47:49'),(2352,'Taxa de Urgência 50',5875,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 14:06:35'),(2353,'Lavar Tshirt normal',1500,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 14:06:38'),(2354,'LAVAR FATO SOCIAL',3500,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 14:06:40'),(2355,'Lavar Calça Social',1750,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 14:06:44'),(2356,'Lavar Calção Seda',1500,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 14:06:47'),(2357,'Lavar Calça Social',1750,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 14:08:47'),(2358,'Lavar Calça Social',1750,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 14:08:50'),(2359,'Lavar Gravata Normal',1500,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 14:28:34'),(2360,'Lavar Gravata Normal',1500,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 14:28:37'),(2361,'Taxa Expresso 100',4500,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 18:13:32'),(2362,'Engomar vestido de linho',4500,'2024-06-08',1,'MESA 16','Margarida Casimiro','2024-06-08 18:13:51'),(2363,'Engomar vestido de linho',4500,'2024-06-08',1,'MESA 16','Maria Cristina','2024-06-09 09:35:08'),(2364,'Taxa Expresso 100',4500,'2024-06-08',1,'MESA 16','Maria Cristina','2024-06-09 09:35:11'),(2365,'Lavar Beca',3800,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 09:35:51'),(2366,'Lavar Beca',3800,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 09:36:01'),(2367,'Lavar Beca',3800,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 09:36:10'),(2368,'LAVAR TAPET P/METRO MEDIO',5000,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 10:13:36'),(2369,'Lavar Casaco Social',1750,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:33:24'),(2370,'LAVAR CAMISA NORMAL ',2000,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:47:32'),(2371,'Lavar Camisa de Ganga',1750,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:47:35'),(2372,'Lavar Camisa de Ganga',1750,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:47:38'),(2373,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:11'),(2374,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:14'),(2375,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:16'),(2376,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:19'),(2377,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:25'),(2378,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:27'),(2379,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:36'),(2380,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:38'),(2381,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:41'),(2382,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:43'),(2383,'Camisa social',1500,'2024-06-9',1,'MESA 16','Maria Cristina','2024-06-09 12:49:46'),(2384,'Lavar Blaser',2200,'2024-06-10',1,'MESA 16','Maria Cristina','2024-06-10 07:23:22'),(2385,'Lavar Blaser',2200,'2024-06-10',1,'MESA 16','Maria Cristina','2024-06-10 07:23:25'),(2386,'Camisa social',1500,'2024-06-10',1,'MESA 16','Maria Cristina','2024-06-10 08:09:57'),(2387,'Camisa social',1500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 17:40:24'),(2388,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:03'),(2389,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:05'),(2390,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:08'),(2391,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:10'),(2392,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:13'),(2393,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:15'),(2394,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:17'),(2395,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:20'),(2396,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:23'),(2397,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:25'),(2398,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:28'),(2399,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:30'),(2400,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:34'),(2401,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:37'),(2402,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:39'),(2403,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:42'),(2404,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:44'),(2405,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:52'),(2406,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:55'),(2407,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:25:58'),(2408,'Lavar Colcha Casal',8000,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:26:02'),(2409,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:28:19'),(2410,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:28:25'),(2411,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:28:35'),(2412,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:28:52'),(2413,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:29:03'),(2414,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:29:19'),(2415,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:29:23'),(2416,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:29:26'),(2417,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:29:30'),(2418,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:01'),(2419,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:05'),(2420,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:07'),(2421,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:10'),(2422,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:13'),(2423,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:16'),(2424,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:18'),(2425,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:21'),(2426,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:24'),(2427,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:27'),(2428,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:30'),(2429,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:32'),(2430,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:36'),(2431,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:39'),(2432,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:42'),(2433,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:45'),(2434,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:48'),(2435,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:30:59'),(2436,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:31:02'),(2437,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:31:04'),(2438,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:31:09'),(2439,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:31:13'),(2440,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:31:19'),(2441,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:31:22'),(2442,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:31:24'),(2443,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:31:35'),(2444,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:31:58'),(2445,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:03'),(2446,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:07'),(2447,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:09'),(2448,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:12'),(2449,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:16'),(2450,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:19'),(2451,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:22'),(2452,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:27'),(2453,'Lavar Toalhas de Banho Média',1800,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:31'),(2454,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:34'),(2455,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:37'),(2456,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:39'),(2457,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:41'),(2458,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:44'),(2459,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:46'),(2460,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:48'),(2461,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:51'),(2462,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:53'),(2463,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:55'),(2464,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:32:57'),(2465,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:33:03'),(2466,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:33:07'),(2467,'Lavar Lençois Casal 2',4500,'2024-06-10',1,'MESA 16','Margarida Casimiro','2024-06-10 20:33:10'),(2468,'Lavar Toalhas de Banho Média',1800,'2024-06-10',24,'MESA 16','Margarida Casimiro','2024-06-10 20:35:21'),(2469,'COLCHA CASAL',8000,'2024-06-10',6,'MESA 16','Margarida Casimiro','2024-06-10 20:35:39'),(2470,'Lavar Lençois Casal 2',4500,'2024-06-10',10,'MESA 16','Margarida Casimiro','2024-06-10 20:35:42'),(2471,'COLCHA CASAL',8000,'2024-06-10',16,'MESA 16','Maria Cristina','2024-06-11 08:13:31'),(2472,'LAVAR FATO SOCIAL',3500,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 08:13:35'),(2473,'Lavar Toalhas de Banho Média',1800,'2024-06-10',24,'MESA 16','Maria Cristina','2024-06-11 08:13:38'),(2474,'LAVAR FATO SOCIAL',3500,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 08:13:41'),(2475,'LAVAR FATO SOCIAL',3500,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 08:22:15'),(2476,'LAVAR FATO SOCIAL',3500,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 08:22:18'),(2477,'LAVAR FATO SOCIAL',3500,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 08:22:22'),(2478,'Lavar Calça Social',1750,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 09:15:36'),(2479,'Lavar Calça Social',1750,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 09:15:40'),(2480,'Lavar Casaco Social',1750,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 09:18:51'),(2481,'Lavar Calça Social',1750,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 09:18:58'),(2482,'Taxa Expresso 100',3500,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 09:19:58'),(2483,'LAVAR SAIA AFRICANA',4000,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 09:20:02'),(2484,'Lavar Saia PLISSADA',3000,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 09:20:06'),(2485,'Lavar Saia PLISSADA',3000,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 09:20:12'),(2486,'Casaco social de linho',2000,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 09:23:11'),(2487,'Lavar Beca',3800,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 12:32:48'),(2488,'Lavar Beca',3800,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 12:32:51'),(2489,'Taxa de Urgência 50',1750,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 12:34:42'),(2490,'Taxa Expresso 100',3500,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 12:34:55'),(2491,'Taxa de Urgência 50',1750,'2024-06-11',1,'MESA 16','Maria Cristina','2024-06-11 12:35:07'),(2492,'LAVAR CAMISA NORMAL ',2000,'2024-06-11',1,'MESA 16','Eltone Ndongala','2024-06-11 15:06:23'),(2493,'LAVAR FATO SOCIAL',3500,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 08:21:06'),(2494,'Taxa Expresso 100',7000,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 08:21:10'),(2495,'engomar Fato Social',1700,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 08:27:17'),(2496,'engomar Fato Social',1700,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 08:27:20'),(2497,'engomar Fato Social',1700,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:01:26'),(2498,'engomar Fato Social',1700,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:01:28'),(2499,'engomar Fato Social',1700,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:01:39'),(2500,'engomar Fato Social',1700,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:02:10'),(2501,'Taxa Expresso 100',1200,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:02:12'),(2502,'Engomar colete',1200,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:02:15'),(2503,'engomar Fato Social',1700,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:05:03'),(2504,'Taxa Expresso 100',1200,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:05:06'),(2505,'Engomar colete',1200,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:05:09'),(2506,'LAVAR FATO SOCIAL',3500,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:46:18'),(2507,'LAVAR FATO SOCIAL',3500,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 10:48:23'),(2508,'Camisa social',1500,'2024-06-12',1,'MESA 16','Maria Cristina','2024-06-12 12:48:09'),(2509,'LAVAR FATO SOCIAL',3500,'2024-06-12',1,'MESA 16','Margarida Casimiro','2024-06-12 14:42:04'),(2510,'Lavar Tapetes p/metros',3500,'2024-06-12',1,'MESA 16','Eltone Ndongala','2024-06-12 19:59:41'),(2511,'Taxa Expresso 100',1500,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 09:30:36'),(2512,'  engomar Camisa Normal',1200,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 09:30:40'),(2513,'ENGOMAR CAMISA SOCIAL',1300,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 09:31:14'),(2514,'ENGOMAR CAMISA SOCIAL',1300,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 09:31:26'),(2515,'ENGOMAR CAMISA DE LINHO',1500,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 09:31:42'),(2516,'ENGOMAR CAMISA SOCIAL',1300,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 09:31:48'),(2517,'ENGOMAR CAMISA SOCIAL',1300,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 09:31:55'),(2518,'Taxa Expresso 100',1500,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 09:32:34'),(2519,'Lavar Polo',1500,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 10:14:51'),(2520,'Lavar Polo',1500,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 10:14:59'),(2521,'Lavar Polo',1500,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 10:15:03'),(2522,'Lavar Vestido de Gala Simples',7500,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 11:33:36'),(2523,'Taxa de Urgência 50',5625,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 11:33:39'),(2524,'Camisa social',1500,'2024-06-13',1,'MESA 16','Maria Cristina','2024-06-13 11:33:42'),(2525,'Lavar Vestido de Seda',8000,'2024-06-13',1,'MESA 16','Margarida Casimiro','2024-06-13 15:34:32'),(2526,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-06-13',1,'MESA 16','Margarida Casimiro','2024-06-13 15:34:38'),(2527,'Lavar Vestido de Seda',8000,'2024-06-13',1,'MESA 16','Margarida Casimiro','2024-06-13 15:35:48'),(2528,'Lavar Vestido de Seda',8000,'2024-06-13',1,'MESA 16','Margarida Casimiro','2024-06-13 15:35:53'),(2529,'VESTIDO DE GALA CURTO SIMPLES ',5500,'2024-06-13',1,'MESA 16','Margarida Casimiro','2024-06-13 15:36:32'),(2530,'Lavar Calça Social',1750,'2024-06-14',1,'MESA 16','Maria Cristina','2024-06-14 10:16:43'),(2531,'VESTIDO DE NOITE SIMPLES ',6000,'2024-06-14',1,'MESA 16','Margarida Casimiro','2024-06-14 17:48:36'),(2532,'ENGOMAR PANTALONA',1500,'2024-06-14',1,'MESA 16','Margarida Casimiro','2024-06-14 17:54:28'),(2533,'Engomar vestido bata',4500,'2024-06-14',1,'MESA 16','Margarida Casimiro','2024-06-14 17:59:47'),(2534,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 08:35:00'),(2535,'LAVAR FATO SOCIAL',3500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 08:50:01'),(2536,'LAVAR FATO SOCIAL',3500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 08:53:28'),(2537,'LAVAR FATO SOCIAL',3500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 08:53:34'),(2538,'LAVAR FATO SOCIAL',3500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 08:53:40'),(2539,'LAVAR FATO SOCIAL',3500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 08:53:44'),(2540,'Lavar Casaco Social',1750,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 08:56:56'),(2541,'Lavar Fato de Linho H',4000,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:14:08'),(2542,'Lavar Casaco Social',1750,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:14:10'),(2543,'Lavar Vestido Bata',6000,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:14:13'),(2544,'Lavar Vestido Simples',4500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:14:15'),(2545,'Lavar Fato de Linho H',4000,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:14:18'),(2546,'Lavar Fato de Linho H',4000,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:14:20'),(2547,'Lavar macacão jardineira',5500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:14:23'),(2548,'Camisa social',1500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:17:17'),(2549,'Camisa social',1500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:17:20'),(2550,'Lavar macacão jardineira',5500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 09:46:26'),(2551,'LAVAR FATO SOCIAL',3500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 11:25:51'),(2552,'Lavar Casaco Social',1750,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 11:27:02'),(2553,'Lavar Calça Social',1750,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 13:28:57'),(2554,'Calça social femenina',1750,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 13:40:47'),(2555,'Taxa Expresso 100',1500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 13:48:36'),(2556,'Camisa social',1500,'2024-06-15',1,'MESA 16','Margarida Casimiro','2024-06-15 13:48:57'),(2557,'Lavar Tshirt normal',1500,'2024-06-17',1,'MESA 16','Maria Cristina','2024-06-17 12:25:53'),(2558,'engomar Fato Social',1700,'2024-06-17',1,'MESA 16','Margarida Casimiro','2024-06-17 13:40:59'),(2559,'Taxa Expresso 100',1700,'2024-06-17',1,'MESA 16','Margarida Casimiro','2024-06-17 13:41:02'),(2560,'Taxa Expresso 100',1700,'2024-06-17',1,'MESA 16','Margarida Casimiro','2024-06-17 14:05:03'),(2561,'Blusa interior F',1000,'2024-06-17',1,'MESA 16','Margarida Casimiro','2024-06-17 17:50:47'),(2562,'LAVAR FATO SOCIAL',3500,'2024-06-17',1,'MESA 16','Margarida Casimiro','2024-06-17 18:48:26'),(2563,'Lavar Calça Normal',2000,'2024-06-18',1,'MESA 16','Maria Cristina','2024-06-18 10:30:21'),(2564,'COLCHA VILA DA GALE1',1800,'2024-06-18',1,'MESA 16','Maria Cristina','2024-06-18 10:34:20'),(2565,'COLCHA VILA DA GALE1',1800,'2024-06-18',1,'MESA 16','Maria Cristina','2024-06-18 10:34:24'),(2566,'LAVAR FATO SOCIAL',3500,'2024-06-18',1,'MESA 16','Maria Cristina','2024-06-18 10:56:37'),(2567,'LAVAR FATO SOCIAL',3500,'2024-06-18',1,'MESA 16','Maria Cristina','2024-06-18 10:57:14'),(2568,'Vestido simples de festa',7500,'2024-06-18',1,'MESA 16','Margarida Casimiro','2024-06-18 13:45:00'),(2569,'Taxa de Urgência 50',7500,'2024-06-18',1,'MESA 16','Margarida Casimiro','2024-06-18 13:45:10'),(2570,'Taxa de Urgência 50',3625,'2024-06-19',1,'MESA 16','Maria Cristina','2024-06-19 09:19:10'),(2571,'Lavar Casaco Social',1750,'2024-06-19',1,'MESA 16','Maria Cristina','2024-06-19 09:19:13'),(2572,'Lavar macacão jardineira',5500,'2024-06-19',1,'MESA 16','Maria Cristina','2024-06-19 09:19:16'),(2573,'Taxa de Urgência 50',5750,'2024-06-19',1,'MESA 16','Maria Cristina','2024-06-19 10:47:46'),(2574,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-06-19',1,'MESA 16','Maria Cristina','2024-06-19 10:49:46'),(2575,'Lavar Edredon Casal',9500,'2024-06-19',1,'MESA 16','Maria Cristina','2024-06-19 11:09:51'),(2576,'Lavar Blusa de Seda',2000,'2024-06-19',1,'MESA 16','Maria Cristina','2024-06-19 11:11:33'),(2577,'Lavar Blusa de Seda',2000,'2024-06-19',1,'MESA 16','Maria Cristina','2024-06-19 11:15:26'),(2578,'Calça social femenina',1750,'2024-06-19',1,'MESA 16','Maria Cristina','2024-06-19 11:22:03'),(2579,'Taxa de Urgência 50',1750,'2024-06-19',1,'MESA 16','Eltone Ndongala','2024-06-19 11:37:57'),(2580,'FATO DE BLUSA E SAIA ',6000,'2024-06-19',1,'MESA 16','Eltone Ndongala','2024-06-19 12:22:38'),(2581,'Lavar Calça Social',1750,'2024-06-19',1,'MESA 16','Eltone Ndongala','2024-06-19 13:39:40'),(2582,'Polo muito suja',1800,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 15:36:51'),(2583,'Polo muito suja',1800,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 15:36:54'),(2584,'Polo muito suja',1800,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 15:36:57'),(2585,'Taxa de Urgência 50',4725,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 15:36:59'),(2586,'Lavar Blaser',2200,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 16:08:12'),(2587,'Lavar Blaser',2200,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 16:08:14'),(2588,'Lavar macacão jardineira',5500,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 16:08:17'),(2589,'Lavar macacão jardineira',5500,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 16:08:19'),(2590,'Lavar Blusa de Seda',2000,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 16:08:22'),(2591,'Taxa Expresso 100',17500,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 19:28:33'),(2592,'LAVAR FATO SOCIAL',3500,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 19:28:35'),(2593,'LAVAR FATO SOCIAL',3500,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 19:28:37'),(2594,'LAVAR FATO SOCIAL',3500,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 19:28:40'),(2595,'LAVAR FATO SOCIAL',3500,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 19:28:42'),(2596,'LAVAR FATO SOCIAL',3500,'2024-06-19',1,'MESA 16','Margarida Casimiro','2024-06-19 19:28:45'),(2597,'LAVAR FATO SOCIAL',3500,'2024-06-20',1,'MESA 16','Eltone Ndongala','2024-06-20 12:05:29'),(2598,'Taxa de Urgência 50',1750,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:55:54'),(2599,'LAVAR FATO SOCIAL',3500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:55:57'),(2600,'Taxa de Urgência 50',1500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:56:27'),(2601,'VESTIDO DE CRIANCA ',3000,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:57:00'),(2602,'Taxa de Urgência 50',1900,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:57:07'),(2603,'Lavar Beca',3800,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:57:33'),(2604,'Taxa de Urgência 50',2400,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:58:12'),(2605,'Lavar Tshirt normal',1500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:58:16'),(2606,'Taxa de Urgência 50',1650,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:58:24'),(2607,'Lavar Tshirt normal',1500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:58:29'),(2608,'Taxa de Urgência 50',1900,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:58:46'),(2609,'LAVAR CAMISOLA NORMAL',1000,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:58:57'),(2610,'Taxa de Urgência 50',1900,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:59:07'),(2611,'LAVAR CAMISOLA NORMAL',1000,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:59:14'),(2612,'Taxa de Urgência 50',1400,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:59:22'),(2613,'LAVAR CAMISOLA NORMAL',1000,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 13:59:26'),(2614,'Taxa de Urgência 50',1650,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 14:00:21'),(2615,'Lavar Tshirt normal',1500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 14:00:24'),(2616,'Taxa de Urgência 50',1800,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 14:00:37'),(2617,'Tshirt normal muito suja',1800,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 14:00:43'),(2618,'Polo muito suja',1800,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 14:00:55'),(2619,'LAVAR FATO SOCIAL',3500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 14:35:53'),(2620,'ENGOMAR CALÇA SOCIAL S',1500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 16:35:53'),(2621,'ENGOMAR CALÇA SOCIAL S',1500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 16:35:55'),(2622,'ENGOMAR CALÇA SOCIAL S',1500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 16:35:57'),(2623,'Camisa social',1500,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 19:13:52'),(2624,'LAVAR BLUSA VESTIDO',2000,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 19:17:54'),(2625,'Lavar Blaser',2200,'2024-06-20',1,'MESA 16','Margarida Casimiro','2024-06-20 19:22:10'),(2626,'LAVAR FATO SOCIAL',3500,'2024-06-21',1,'MESA 16','Maria Cristina','2024-06-21 08:46:11'),(2627,'Lavar Blaser',2200,'2024-06-21',1,'MESA 16','Maria Cristina','2024-06-21 10:13:40'),(2628,'ENGOMAR PANTALONA',1500,'2024-06-21',1,'MESA 16','Margarida Casimiro','2024-06-21 16:41:47'),(2629,'Lavar Camisa Branca (Muito Suja)',1700,'2024-06-22',1,'MESA 16','Maria Cristina','2024-06-22 10:01:38'),(2630,'LAVAR FATO SOCIAL',3500,'2024-06-22',1,'MESA 16','Maria Cristina','2024-06-22 18:46:49'),(2631,'Lavar Colcha Solteiro',6500,'2024-06-23',1,'MESA 16','Margarida Casimiro','2024-06-23 10:41:09'),(2632,'Lavar Colcha Casal',8000,'2024-06-23',9,'MESA 16','Margarida Casimiro','2024-06-23 10:43:43'),(2633,'Lavar Colcha Casal',8000,'2024-06-23',7,'MESA 16','Margarida Casimiro','2024-06-23 10:43:47'),(2634,'Lavar Colcha Casal',8000,'2024-06-23',5,'MESA 16','Margarida Casimiro','2024-06-23 10:44:37'),(2635,'Lavar Fronha',1300,'2024-06-23',42,'MESA 16','Margarida Casimiro','2024-06-23 10:49:01'),(2636,'Lavar Toalhas de Banho Média',1800,'2024-06-23',21,'MESA 16','Margarida Casimiro','2024-06-23 10:49:38'),(2637,'Lavar Lençois Casal 2',4500,'2024-06-23',10,'MESA 16','Margarida Casimiro','2024-06-23 10:51:28'),(2638,'Lavar Fronha',1300,'2024-06-23',20,'MESA 16','Margarida Casimiro','2024-06-23 10:51:33'),(2639,'Lavar Lençois Casal 2',4500,'2024-06-23',1,'MESA 16','Margarida Casimiro','2024-06-23 10:53:18'),(2640,'Lavar Fronha',1300,'2024-06-23',18,'MESA 16','Margarida Casimiro','2024-06-23 10:54:26'),(2641,'Lavar Lençois Casal 2',4500,'2024-06-23',9,'MESA 16','Margarida Casimiro','2024-06-23 10:54:29'),(2642,'Lavar Colcha Casal',8000,'2024-06-23',24,'MESA 16','Margarida Casimiro','2024-06-23 10:54:32'),(2643,'Lavar Toalhas de Banho Média',1800,'2024-06-23',5,'MESA 16','Margarida Casimiro','2024-06-23 10:54:35'),(2644,'Camisa social',1500,'2024-06-24',1,'MESA 16','Maria Cristina','2024-06-24 09:25:35'),(2645,'Lavar Boxer',3500,'2024-06-24',1,'MESA 16','Maria Cristina','2024-06-24 10:12:36'),(2646,'Lavar Smoking',3000,'2024-06-24',1,'MESA 16','Maria Cristina','2024-06-24 10:13:46'),(2647,'Lavar Casaco Social',1750,'2024-06-24',1,'MESA 16','Margarida Casimiro','2024-06-24 14:08:33'),(2648,'Lavar Casaco Social',1750,'2024-06-24',1,'MESA 16','Margarida Casimiro','2024-06-24 14:08:35'),(2649,'Lavar Casaco Social',1750,'2024-06-24',1,'MESA 16','Margarida Casimiro','2024-06-24 14:08:38'),(2650,'Lavar Vestido Bubu',6500,'2024-06-25',1,'MESA 16','Maria Cristina','2024-06-25 10:25:25'),(2651,'Lavar Vestido Bata',6000,'2024-06-25',1,'MESA 16','Maria Cristina','2024-06-25 10:25:28'),(2652,'Taxa de Urgência 50',875,'2024-06-25',1,'MESA 16','Maria Cristina','2024-06-25 10:41:28'),(2653,'Lavar Calça Social',1750,'2024-06-25',1,'MESA 16','Maria Cristina','2024-06-25 10:41:31'),(2654,'Lavar Lençois Casal 1',4000,'2024-06-25',1,'MESA 16','Margarida Casimiro','2024-06-25 16:30:57'),(2655,'LAVAR FATO SOCIAL',3500,'2024-06-26',1,'MESA 16','Maria Cristina','2024-06-26 10:56:53'),(2656,'Taxa de Urgência 50',1750,'2024-06-26',1,'MESA 16','Maria Cristina','2024-06-26 11:02:32'),(2657,'Lavar Calça Social',1750,'2024-06-26',1,'MESA 16','Maria Cristina','2024-06-26 11:23:53'),(2658,'Taxa Expresso 100',3000,'2024-06-26',1,'MESA 16','Maria Cristina','2024-06-26 13:23:22'),(2659,'ENGOMAR CALÇA SOCIAL S',1500,'2024-06-26',1,'MESA 16','Maria Cristina','2024-06-26 13:23:25'),(2660,'Engomar Blaser',1500,'2024-06-26',1,'MESA 16','Maria Cristina','2024-06-26 13:23:28'),(2661,'Taxa Expresso 100',1500,'2024-06-26',1,'MESA 16','Margarida Casimiro','2024-06-26 14:36:35'),(2662,'Lavar Calça Ganga',1800,'2024-06-26',1,'MESA 16','Margarida Casimiro','2024-06-26 14:43:38'),(2663,'Lavar Calça Normal',2000,'2024-06-26',1,'MESA 16','Margarida Casimiro','2024-06-26 14:43:40'),(2664,'Camisa social',1500,'2024-06-26',1,'MESA 16','Margarida Casimiro','2024-06-26 17:26:57'),(2665,'Taxa de Urgência 50',3750,'2024-06-27',1,'MESA 16','Maria Cristina','2024-06-27 12:56:23'),(2666,'Lavar Vestido de Gala Simples',7500,'2024-06-27',1,'MESA 16','Maria Cristina','2024-06-27 12:56:26'),(2667,'Taxa Expresso 100',3250,'2024-06-27',1,'MESA 16','Margarida Casimiro','2024-06-27 14:21:08'),(2668,'Camisa social',1500,'2024-06-27',1,'MESA 16','Margarida Casimiro','2024-06-27 14:21:16'),(2669,'Lavar Calça Social',1750,'2024-06-27',1,'MESA 16','Margarida Casimiro','2024-06-27 14:21:19'),(2670,'LAVAR FATO SOCIAL',3500,'2024-06-27',1,'MESA 16','Margarida Casimiro','2024-06-27 19:47:57'),(2671,'LAVAR FATO SOCIAL',3500,'2024-06-27',1,'MESA 16','Margarida Casimiro','2024-06-27 19:48:23'),(2672,'LAVAR FATO SOCIAL',3500,'2024-06-27',1,'MESA 16','Margarida Casimiro','2024-06-27 19:48:27'),(2673,'Camisa de seda',1700,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 08:18:57'),(2674,'Taxa Expresso 100',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 11:59:36'),(2675,'Taxa de Urgência 50',3250,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 12:35:25'),(2676,'Taxa Expresso 100',6500,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 12:35:34'),(2677,'Lavar Calça Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:42:16'),(2678,'Lavar Calça Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:42:19'),(2679,'Lavar Calça Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:42:24'),(2680,'Lavar Calça Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:42:27'),(2681,'Taxa Expresso 100',7000,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:42:29'),(2682,'Lavar Calça Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:44:09'),(2683,'Taxa Expresso 100',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:44:12'),(2684,'Lavar Calça Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:47:52'),(2685,'Lavar Calça Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:47:59'),(2686,'Lavar Calça Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:49:05'),(2687,'Lavar Calça Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:49:09'),(2688,'Lavar Casaco Social',1750,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:51:41'),(2689,'Taxa Expresso 100',3950,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:51:47'),(2690,'Lavar Fato Indiano',3500,'2024-06-28',1,'MESA 16','Maria Cristina','2024-06-28 13:59:35'),(2691,'Lavar Casaco Social',1750,'2024-06-28',1,'MESA 16','Margarida Casimiro','2024-06-28 14:58:13'),(2692,'Lavar Casaco Social',1750,'2024-06-28',1,'MESA 16','Margarida Casimiro','2024-06-28 14:59:18'),(2693,'Taxa Expresso 100',1500,'2024-06-28',1,'MESA 16','Margarida Casimiro','2024-06-28 15:09:50'),(2694,'ENGOMAR CASACO SOCIAL ',1500,'2024-06-28',1,'MESA 16','Margarida Casimiro','2024-06-28 15:09:53'),(2695,'engomar Fato Social',1700,'2024-06-29',1,'MESA 16','Margarida Casimiro','2024-06-29 13:59:56'),(2696,'Taxa Expresso 100',1700,'2024-06-29',1,'MESA 16','Margarida Casimiro','2024-06-29 14:00:23'),(2697,'ENGOMAR CAMISA DE LINHO',1500,'2024-06-29',1,'MESA 16','Margarida Casimiro','2024-06-29 14:16:35'),(2698,'ENGOMAR CASACO SOCIAL ',1500,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 08:47:22'),(2699,'ENGOMAR CASACO SOCIAL ',1500,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 08:47:24'),(2700,'ENGOMAR CASACO SOCIAL ',1500,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 08:47:27'),(2701,'Lavar Calça Social',1750,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 09:51:07'),(2702,'Taxa Expresso 100',1750,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 09:51:12'),(2703,'Lavar Tshirt normal',1500,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 09:56:36'),(2704,'Lavar Calça Normal',2000,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 09:56:39'),(2705,'Taxa de Urgência 50',1750,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 09:56:42'),(2706,'Lavar Casaco Social',1750,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 12:02:24'),(2707,'Taxa Expresso 100',1750,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 12:02:27'),(2708,'Lavar Calça Normal',2000,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 16:18:46'),(2709,'Lavar Blaser',2200,'2024-07-01',1,'MESA 16','Margarida Casimiro','2024-07-01 18:43:56'),(2710,'Vestido simples de festa',7500,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 10:49:59'),(2711,'Lavar Vestido de Baptizado(Simples)',7000,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 10:50:11'),(2712,'Lavar Fato de Linho H',4000,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 11:46:00'),(2713,'Lavar Camisa Branca (Muito Suja)',1700,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 11:46:39'),(2714,'Lavar Camisa Branca (Muito Suja)',1700,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 11:46:42'),(2715,'Taxa Expresso 100',6800,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 11:46:45'),(2716,'Lavar Fato de Linho H',4000,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 11:48:51'),(2717,'Lavar Camisa Branca (Muito Suja)',1700,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 12:43:03'),(2718,'Taxa Expresso 100',1700,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 12:43:06'),(2719,'Lavar Camisa Branca (Muito Suja)',1700,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 12:43:09'),(2720,'Lavar Camisa Branca (Muito Suja)',1700,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 12:59:06'),(2721,'Taxa Expresso 100',3400,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 12:59:35'),(2722,'Camisa social',1500,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 15:47:08'),(2723,'Lavar Casaco Social',1750,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 16:11:50'),(2724,'Camisa social',1500,'2024-07-02',1,'MESA 16','Margarida Casimiro','2024-07-02 19:44:28'),(2725,'Lavar Calça Ganga',1800,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 09:30:57'),(2726,'Lavar Polo',1500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 09:34:03'),(2727,'Lavar Polo',1500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 09:34:06'),(2728,'Camisa social',1500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 12:01:52'),(2729,'Camisa social',1500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 12:01:59'),(2730,'Camisa social',1500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 12:02:01'),(2731,'Taxa de Urgência 50',2250,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 12:02:04'),(2732,'Camisa social',1500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 13:45:09'),(2733,'Lavar Blaser',2200,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:25:45'),(2734,'Lavar Saia Simples',2500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:25:47'),(2735,'Lavar Blusa Simples',1500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:25:50'),(2736,'Lavar Blusa Simples',1500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:25:52'),(2737,'Lavar Blusa de Seda',2000,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:25:55'),(2738,'Lavar Blusa Simples',1500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:25:57'),(2739,'Lavar Saia Simples',2500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:26:00'),(2740,'Lavar Vestido Bata',6000,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:26:03'),(2741,'Lavar Vestido Bata',6000,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:26:07'),(2742,'Lavar Vestido Bata',6000,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:26:15'),(2743,'Lavar Vestido Bata',6000,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 14:26:18'),(2744,'Taxa Expresso 100',6000,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 15:25:20'),(2745,'Lavar Fato Indiano',3500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 15:27:25'),(2746,'Taxa de Urgência 50',1750,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 17:37:03'),(2747,'LAVAR FATO SOCIAL',3500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 18:19:53'),(2748,'LAVAR FATO SOCIAL',3500,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 18:23:37'),(2749,'Lavar Blaser',2200,'2024-07-03',1,'MESA 16','Margarida Casimiro','2024-07-03 18:44:08'),(2750,'LAVAR FATO SOCIAL',3500,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 08:22:00'),(2751,'LAVAR FATO SOCIAL',3500,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 09:00:32'),(2752,'LAVAR FATO SOCIAL',3500,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 09:00:34'),(2753,'LAVAR FATO SOCIAL',3500,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 09:00:37'),(2754,'LAVAR SAIA CURTA ',2000,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 16:29:15'),(2755,'Lavar Saia PLISSADA',3000,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 16:29:18'),(2756,'Taxa de Urgência 50',2500,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 17:30:29'),(2757,'Lavar Colete',1500,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 17:30:32'),(2758,'LAVAR FATO SOCIAL',3500,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 17:30:35'),(2759,'Camisa social',1500,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 17:33:06'),(2760,'Taxa de Urgência 50',2200,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 18:05:00'),(2761,'Lavar Blaser',2200,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 18:05:06'),(2762,'Taxa de Urgência 50',1100,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 18:05:27'),(2763,'Lavar Blaser',2200,'2024-07-04',1,'MESA 16','Margarida Casimiro','2024-07-04 18:06:12'),(2764,'Lavar Casaco Social',1750,'2024-07-05',1,'MESA 16','Margarida Casimiro','2024-07-05 10:15:40'),(2765,'Taxa de Urgência 50',875,'2024-07-05',1,'MESA 16','Margarida Casimiro','2024-07-05 10:15:44'),(2766,'Lavar Blaser',2200,'2024-07-05',1,'MESA 16','Margarida Casimiro','2024-07-05 13:18:08'),(2767,'Lavar Blaser',2200,'2024-07-05',1,'MESA 16','Margarida Casimiro','2024-07-05 13:18:12'),(2768,'Lavar Casaco Olimpico',2000,'2024-07-05',1,'MESA 16','Margarida Casimiro','2024-07-05 13:19:39'),(2769,'Camisa social',1500,'2024-07-05',1,'MESA 16','Margarida Casimiro','2024-07-05 13:50:16'),(2770,'Camisa social',1500,'2024-07-05',1,'MESA 16','Margarida Casimiro','2024-07-05 14:39:34'),(2771,'LAVAR FATO SOCIAL',3500,'2024-07-06',1,'MESA 16','Margarida Casimiro','2024-07-06 10:16:11'),(2772,'LAVAR FATO SOCIAL',3500,'2024-07-06',1,'MESA 16','Margarida Casimiro','2024-07-06 10:16:15'),(2773,'Lavar Calça Social',1750,'2024-07-06',1,'MESA 16','Margarida Casimiro','2024-07-06 10:16:17'),(2774,'Camisa social',1500,'2024-07-06',1,'MESA 16','Margarida Casimiro','2024-07-06 10:16:19'),(2775,'LAVAR FATO SOCIAL',3500,'2024-07-06',1,'MESA 16','Margarida Casimiro','2024-07-06 10:17:05'),(2776,'Taxa Expresso 100',3500,'2024-07-06',1,'MESA 16','Margarida Casimiro','2024-07-06 10:17:08'),(2777,'Lavar Blaser',2200,'2024-07-06',1,'MESA 16','Margarida Casimiro','2024-07-06 11:19:51'),(2778,'Taxa de Urgência 50',1100,'2024-07-06',1,'MESA 16','Margarida Casimiro','2024-07-06 11:19:54'),(2779,'Lavar Almofada Grande',3000,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 10:14:40'),(2780,'Lavar Almofada Grande',3000,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 10:14:56'),(2781,'Taxa de Urgência 50',3000,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 10:14:59'),(2782,'ENGOMAR PANTALONA',1500,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 10:39:44'),(2783,'Lavar Blaser',2200,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 10:52:31'),(2784,'Taxa de Urgência 50',2250,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 10:52:33'),(2785,'ENGOMAR CASACO SOCIAL  H',1500,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 10:52:37'),(2786,'ENGOMAR CASACO SOCIAL  H',1500,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 10:52:39'),(2787,'ENGOMAR CASACO SOCIAL  H',1500,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 10:52:43'),(2788,'Lavar Toalhas de Banho Grande',2500,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 12:31:03'),(2789,'Camisa social',1500,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 14:04:59'),(2790,'Vestido simples de festa',7500,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 17:35:51'),(2791,'Lavar Vestido Simples',4500,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 17:46:07'),(2792,'Lavar Calça Social',1750,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 18:44:30'),(2793,'LAVAR FATO SOCIAL',3500,'2024-07-08',1,'MESA 16','Margarida Casimiro','2024-07-08 19:24:55'),(2794,'LAVAR FATO SOCIAL',3500,'2024-07-9',1,'MESA 16','Margarida Casimiro','2024-07-09 08:57:26'),(2795,'Taxa de Urgência 50',1625,'2024-07-9',1,'MESA 16','Margarida Casimiro','2024-07-09 09:39:55'),(2796,'Lavar Casaco Social',1750,'2024-07-9',1,'MESA 16','Margarida Casimiro','2024-07-09 09:40:17'),(2797,'Camisa social',1500,'2024-07-9',1,'MESA 16','Margarida Casimiro','2024-07-09 09:40:20'),(2798,'Lavar Camisa de Linho',2000,'2024-07-9',1,'MESA 16','Margarida Casimiro','2024-07-09 12:52:13'),(2799,'Taxa de Urgência 50',1750,'2024-07-10',1,'MESA 16','Margarida Casimiro','2024-07-10 11:12:16'),(2800,'LAVAR FATO SOCIAL',3500,'2024-07-10',1,'MESA 16','Margarida Casimiro','2024-07-10 11:12:21'),(2801,'Casaco social de linho',2000,'2024-07-10',1,'MESA 16','Margarida Casimiro','2024-07-10 14:40:19'),(2802,'Lavar Calça Social',1750,'2024-07-10',1,'MESA 16','Margarida Casimiro','2024-07-10 14:40:21'),(2803,'Lavar Casaco Social',1750,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:06'),(2804,'Lavar Casaco Social',1750,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:09'),(2805,'Camisa social',1500,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:11'),(2806,'Camisa social',1500,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:14'),(2807,'Camisa social',1500,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:20'),(2808,'Camisa social',1500,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:25'),(2809,'Camisa social',1500,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:28'),(2810,'Camisa social',1500,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:30'),(2811,'Lavar Calça Social',1750,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:33'),(2812,'Camisa social',1500,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:37'),(2813,'Lavar Blaser',2200,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 09:42:41'),(2814,'Lavar Casaco Social',1750,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 10:40:06'),(2815,'Taxa de Urgência 50',875,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 10:40:08'),(2816,'engomar Fato Social',1700,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 15:37:29'),(2817,'Taxa de Urgência 50',850,'2024-07-11',1,'MESA 16','Margarida Casimiro','2024-07-11 15:37:33'),(2818,'Lavar Vestido de Seda',8000,'2024-07-12',1,'MESA 16','Margarida Casimiro','2024-07-12 14:04:26'),(2819,'Taxa de Urgência 50',4000,'2024-07-12',1,'MESA 16','Margarida Casimiro','2024-07-12 14:04:29'),(2820,'Taxa de Urgência 50',2500,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 09:21:32'),(2821,'Taxa Expresso 100',5000,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:02:38'),(2822,'Taxa de Urgência 50',2500,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:03:50'),(2823,'Taxa Expresso 100',2200,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:33:45'),(2824,'Taxa Expresso 100',6600,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:34:29'),(2825,'Taxa de Urgência 50',3300,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:34:49'),(2826,'Taxa de Urgência 50',3300,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:35:09'),(2827,'Lavar Blaser',2200,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:37:48'),(2828,'Lavar Blaser',2200,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:37:50'),(2829,'Lavar Blaser',2200,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:37:55'),(2830,'Lavar Blaser',2200,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:38:38'),(2831,'Lavar Blaser',2200,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 10:38:41'),(2832,'Camisa social',1500,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 14:15:27'),(2833,'ENGOMAR CAMISA DE LINHO',1500,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 14:15:52'),(2834,'Lavar Blaser',2200,'2024-07-13',1,'MESA 16','Margarida Casimiro','2024-07-13 16:06:04'),(2835,'Lavar Calça Social',1750,'2024-07-14',1,'MESA 16','Margarida Casimiro','2024-07-14 11:00:52'),(2836,'Lavar Calça Social',1750,'2024-07-14',1,'MESA 16','Margarida Casimiro','2024-07-14 11:01:06'),(2837,'Lavar Blaser',2200,'2024-07-15',1,'MESA 16','Gesca Fernando','2024-07-15 09:21:17'),(2838,'Lavar Blaser',2200,'2024-07-15',1,'MESA 16','Gesca Fernando','2024-07-15 09:21:34'),(2839,'Camisa social',1500,'2024-07-15',1,'MESA 16','Gesca Fernando','2024-07-15 16:12:13'),(2840,'Lavar Calça Normal',2000,'2024-07-15',1,'MESA 16','Gesca Fernando','2024-07-15 17:00:41'),(2841,'Camisa social',1500,'2024-07-15',1,'MESA 16','Gesca Fernando','2024-07-15 17:06:28'),(2842,'LAVAR FATO SOCIAL',3500,'2024-07-16',1,'MESA 16','Margarida Casimiro','2024-07-16 15:40:28'),(2843,'engomar Fato Social',1700,'2024-07-16',1,'MESA 16','Margarida Casimiro','2024-07-16 16:22:20'),(2844,'Lavar Vestido Simples',4500,'2024-07-16',1,'MESA 16','Margarida Casimiro','2024-07-16 17:19:34'),(2845,'LAV. VESTDO COMPRIDO SIMPLES',6000,'2024-07-16',1,'MESA 16','Margarida Casimiro','2024-07-16 17:42:09'),(2846,'LAVAR FATO SOCIAL',3500,'2024-07-17',1,'MESA 16','Gesca Fernando','2024-07-17 07:53:38'),(2847,'ENGOMAR CALÇA SOCIAL S',1500,'2024-07-17',1,'MESA 16','Gesca Fernando','2024-07-17 09:29:27'),(2848,'ENGOMAR BLUSA SIMPLES',1500,'2024-07-17',1,'MESA 16','Gesca Fernando','2024-07-17 09:29:33'),(2849,'Lavar Camisa de Linho',2000,'2024-07-17',1,'MESA 16','Gesca Fernando','2024-07-17 09:29:36'),(2850,'LAVAR FATO SOCIAL',3500,'2024-07-17',1,'MESA 16','Gesca Fernando','2024-07-17 13:22:44'),(2851,'Lavar Blaser',2200,'2024-07-17',1,'MESA 16','Margarida Casimiro','2024-07-17 16:13:07'),(2852,'Lavar Blusa de ALGODÃO',1500,'2024-07-18',1,'MESA 16','DVML COMERCIAL','2024-07-18 11:26:22'),(2853,'Teste 1',2000,'LUGAR 1',1,'MESA 1','DVML COMERCIAL','2025-09-01 20:58:47'),(2854,'APARAFUSADOR C/CARREGADOR',650,'LUGAR 1',1,'MESA 1','DVML COMERCIAL','2025-09-11 05:28:10');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produto_imposto`
--

DROP TABLE IF EXISTS `produto_imposto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produto_imposto` (
  `pk_produto_imposto` int(11) NOT NULL AUTO_INCREMENT,
  `fk_produto` int(11) NOT NULL,
  `fk_imposto` int(11) NOT NULL,
  PRIMARY KEY (`pk_produto_imposto`),
  KEY `fk_produto_imposto_tb_produto1_idx` (`fk_produto`),
  KEY `fk_produto_imposto_imposto1_idx` (`fk_imposto`),
  CONSTRAINT `fk_produto_imposto_imposto1` FOREIGN KEY (`fk_imposto`) REFERENCES `imposto` (`pk_imposto`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_produto_imposto_tb_produto1` FOREIGN KEY (`fk_produto`) REFERENCES `tb_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1392 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produto_imposto`
--

LOCK TABLES `produto_imposto` WRITE;
/*!40000 ALTER TABLE `produto_imposto` DISABLE KEYS */;
INSERT INTO `produto_imposto` VALUES (903,1,1),(904,2,1),(913,11,1),(914,12,1),(927,26,1),(928,27,1),(929,28,1),(930,29,1),(931,30,1),(932,31,1),(933,32,1),(934,33,1),(935,37,1),(936,38,1),(937,39,1),(938,40,1),(939,41,1),(940,42,1),(941,43,1),(942,44,1),(943,45,1),(944,46,1),(945,47,1),(946,48,1),(947,49,1),(948,50,1),(949,51,1),(950,52,1),(951,53,1),(952,54,1),(953,55,1),(954,56,1),(955,57,1),(956,58,1),(957,59,1),(958,60,1),(959,61,1),(960,62,1),(961,63,1),(962,64,1),(963,65,1),(964,66,1),(965,67,1),(966,68,1),(967,69,1),(968,70,1),(969,71,1),(970,72,1),(971,73,1),(972,74,1),(973,75,1),(974,76,1),(975,77,1),(976,78,1),(977,79,1),(978,80,1),(979,81,1),(980,82,1),(981,83,1),(982,84,1),(983,85,1),(984,86,1),(985,87,1),(986,88,1),(987,89,1),(988,90,1),(989,91,1),(990,92,1),(991,93,1),(992,94,1),(993,95,1),(994,96,1),(995,97,1),(996,98,1),(997,99,1),(998,100,1),(999,101,1),(1000,102,1),(1001,103,1),(1002,104,1),(1003,105,1),(1004,106,1),(1005,107,1),(1006,108,1),(1007,109,1),(1008,110,1),(1009,111,1),(1010,112,1),(1011,113,1),(1012,114,1),(1013,115,1),(1014,116,1),(1015,117,1),(1016,118,1),(1017,119,1),(1018,120,1),(1019,121,1),(1020,122,1),(1021,123,1),(1022,124,1),(1023,125,1),(1024,126,1),(1025,127,1),(1026,128,1),(1027,129,1),(1028,130,1),(1029,131,1),(1030,132,1),(1031,133,1),(1032,134,1),(1033,135,1),(1034,136,1),(1035,137,1),(1036,138,1),(1037,139,1),(1038,140,1),(1039,141,1),(1040,142,1),(1041,143,1),(1042,144,1),(1043,145,1),(1044,146,1),(1045,147,1),(1046,148,1),(1047,149,1),(1048,150,1),(1049,151,1),(1050,152,1),(1051,153,1),(1052,154,1),(1053,155,1),(1054,156,1),(1055,157,1),(1056,158,1),(1057,159,1),(1058,160,1),(1059,161,1),(1060,162,1),(1061,163,1),(1062,164,1),(1063,165,1),(1064,166,1),(1065,167,1),(1066,168,1),(1067,169,1),(1068,170,1),(1069,171,1),(1070,172,1),(1071,173,1),(1072,174,1),(1073,175,1),(1074,176,1),(1075,177,1),(1076,178,1),(1077,179,1),(1078,180,1),(1079,181,1),(1080,182,1),(1081,183,1),(1082,184,1),(1083,185,1),(1084,186,1),(1085,187,1),(1086,188,1),(1087,189,1),(1088,190,1),(1089,191,1),(1090,192,1),(1091,193,1),(1092,194,1),(1093,195,1),(1094,196,1),(1095,197,1),(1096,198,1),(1097,199,1),(1098,200,1),(1099,201,1),(1100,202,1),(1101,203,1),(1102,204,1),(1103,205,1),(1104,206,1),(1105,207,1),(1106,208,1),(1107,209,1),(1108,210,1),(1109,211,1),(1110,212,1),(1111,213,1),(1112,214,1),(1113,215,1),(1114,216,1),(1115,217,1),(1116,218,1),(1117,219,1),(1118,220,1),(1119,221,1),(1120,222,1),(1121,223,1),(1122,224,1),(1123,225,1),(1124,226,1),(1125,227,1),(1126,228,1),(1127,229,1),(1128,230,1),(1129,231,1),(1130,232,1),(1131,233,1),(1132,234,1),(1133,235,1),(1134,236,1),(1135,237,1),(1136,238,1),(1137,239,1),(1138,240,1),(1139,241,1),(1140,242,1),(1141,243,1),(1142,244,1),(1143,245,1),(1144,246,1),(1145,247,1),(1146,248,1),(1147,249,1),(1148,250,1),(1149,251,1),(1150,252,1),(1151,253,1),(1152,254,1),(1153,255,1),(1154,256,1),(1155,257,1),(1156,258,1),(1157,259,1),(1158,260,1),(1159,261,1),(1160,262,1),(1161,263,1),(1162,264,1),(1163,265,1),(1164,266,1),(1165,267,1),(1166,268,1),(1167,269,1),(1168,270,1),(1169,271,1),(1170,272,1),(1171,273,1),(1172,274,1),(1173,275,1),(1174,276,1),(1175,277,1),(1176,278,1),(1177,279,1),(1178,280,1),(1179,281,1),(1180,282,1),(1181,283,1),(1182,284,1),(1183,285,1),(1184,286,1),(1185,287,1),(1186,288,1),(1187,289,1),(1188,290,1),(1189,291,1),(1190,292,1),(1191,293,1),(1192,294,1),(1193,295,1),(1194,296,1),(1195,297,1),(1196,298,1),(1197,299,1),(1198,300,1),(1199,301,1),(1200,302,1),(1201,303,1),(1202,304,1),(1203,305,1),(1204,306,1),(1205,307,1),(1206,308,1),(1207,309,1),(1208,310,1),(1209,311,1),(1210,312,1),(1211,313,1),(1212,314,1),(1213,315,1),(1214,316,1),(1215,317,1),(1216,318,1),(1217,319,1),(1218,320,1),(1219,321,1),(1220,322,1),(1221,323,1),(1222,324,1),(1223,325,1),(1224,326,1),(1225,327,1),(1226,328,1),(1227,329,1),(1228,330,1),(1229,331,1),(1230,332,1),(1231,333,1),(1232,334,1),(1233,335,1),(1234,336,1),(1235,337,1),(1236,338,1),(1237,339,1),(1238,340,1),(1239,341,1),(1240,342,1),(1241,343,1),(1242,344,1),(1243,345,1),(1244,346,1),(1245,347,1),(1246,348,1),(1247,349,1),(1248,350,1),(1249,351,1),(1250,352,1),(1251,353,1),(1252,354,1),(1253,355,1),(1254,356,1),(1255,357,1),(1256,358,1),(1257,359,1),(1258,360,1),(1259,361,1),(1260,362,1),(1261,363,1),(1262,364,1),(1263,365,1),(1264,366,1),(1265,367,1),(1266,368,1),(1267,369,1),(1268,370,1),(1269,371,1),(1270,372,1),(1271,373,1),(1272,374,1),(1273,375,1),(1274,376,1),(1275,377,1),(1276,378,1),(1277,379,1),(1278,380,1),(1279,381,1),(1280,382,1),(1281,383,1),(1282,384,1),(1283,385,1),(1284,386,1),(1285,387,1),(1286,388,1),(1287,389,1),(1288,390,1),(1289,391,1),(1290,392,1),(1291,393,1),(1292,394,1),(1293,395,1),(1294,396,1),(1295,397,1),(1296,398,1),(1297,399,1),(1298,400,1),(1299,401,1),(1300,402,1),(1301,403,1),(1302,404,1),(1303,405,1),(1304,406,1),(1305,407,1),(1306,408,1),(1307,409,1),(1308,410,1),(1309,411,1),(1310,412,1),(1311,413,1),(1312,414,1),(1313,415,1),(1314,416,1),(1315,417,1),(1316,418,1),(1317,419,1),(1318,420,1),(1319,421,1),(1320,422,1),(1321,423,1),(1322,424,1),(1323,425,1),(1324,426,1),(1325,427,1),(1326,428,1),(1327,429,1),(1328,430,1),(1329,431,1),(1330,432,1),(1331,433,1),(1332,434,1),(1333,435,1),(1334,436,1),(1335,437,1),(1336,438,1),(1349,14,1),(1350,15,1),(1351,16,1),(1352,17,1),(1353,18,1),(1354,19,1),(1370,20,5),(1371,21,5),(1373,23,5),(1374,24,5),(1375,3,1),(1376,4,1),(1377,439,1),(1384,5,1),(1385,6,1),(1386,7,1),(1387,8,1),(1388,9,1),(1389,10,1),(1390,13,1),(1391,22,5);
/*!40000 ALTER TABLE `produto_imposto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produto_isento`
--

DROP TABLE IF EXISTS `produto_isento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produto_isento` (
  `pk_produto_isento` int(11) NOT NULL AUTO_INCREMENT,
  `fk_produto` int(11) NOT NULL,
  `fk_produtos_motivos_isensao` int(11) NOT NULL,
  PRIMARY KEY (`pk_produto_isento`),
  KEY `fk_produto_isento_produtos_motivos_isensao1_idx` (`fk_produtos_motivos_isensao`),
  KEY `fk_produto_isento_tb_produto1` (`fk_produto`),
  CONSTRAINT `fk_produto_isento_produtos_motivos_isensao1` FOREIGN KEY (`fk_produtos_motivos_isensao`) REFERENCES `produtos_motivos_isensao` (`pk_produtos_motivos_isensao`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_produto_isento_tb_produto1` FOREIGN KEY (`fk_produto`) REFERENCES `tb_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produto_isento`
--

LOCK TABLES `produto_isento` WRITE;
/*!40000 ALTER TABLE `produto_isento` DISABLE KEYS */;
INSERT INTO `produto_isento` VALUES (1,439,4),(2,440,4);
/*!40000 ALTER TABLE `produto_isento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produtos_motivos_isensao`
--

DROP TABLE IF EXISTS `produtos_motivos_isensao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produtos_motivos_isensao` (
  `pk_produtos_motivos_isensao` int(11) NOT NULL AUTO_INCREMENT,
  `regime` varchar(100) DEFAULT NULL,
  `descricao` varchar(250) DEFAULT NULL,
  `codigo_regime` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_produtos_motivos_isensao`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos_motivos_isensao`
--

LOCK TABLES `produtos_motivos_isensao` WRITE;
/*!40000 ALTER TABLE `produtos_motivos_isensao` DISABLE KEYS */;
INSERT INTO `produtos_motivos_isensao` VALUES (1,'Regime Simplificado','','M00'),(2,'Transmissão de bens e serviço não sujeita','','M02'),(3,'Regime de Exclusão',NULL,'M04'),(4,'Isento com base no artigo nº 12º. a) do CIVA',NULL,'M10'),(5,'Isento com base no artigo nº 12º. b) do CIVA',NULL,'M11'),(6,'Isento com base no artigo nº 12º. c) do CIVA',NULL,'M12'),(7,'Isento com base no artigo nº 12º. d) do CIVA',NULL,'M13'),(8,'Isento com base no artigo nº 12º. e) do CIVA',NULL,'M14'),(9,'Isento com base no artigo nº 12º. f) do CIVA',NULL,'M15'),(10,'Isento com base no artigo nº 12º. g) do CIVA',NULL,'M16'),(11,'Isento com base no artigo nº 12º. h) do CIVA',NULL,'M17'),(12,'Isento com base no artigo nº 12º. i) do CIVA',NULL,'M18'),(13,'Isento com base no artigo nº 12º. j) do CIVA',NULL,'M19'),(14,'Isento com base no artigo nº 12º. k) do CIVA',NULL,'M20'),(15,'Isento com base no artigo nº 15º. 1 a) do CIVA',NULL,'M30'),(16,'Isento com base no artigo nº 1 b) do CIVA',NULL,'M31'),(17,'Isento com base no artigo nº 15º. 1 c) do CIVA',NULL,'M32'),(18,'Isento com base no artigo nº 15º. 1 d) do CIVA',NULL,'M33'),(19,'Isento com base no artigo nº 15º. 1 e) do CIVA',NULL,'M34'),(20,'Isento com base no artigo nº 15º. 1 f) do CIVA',NULL,'M35'),(21,'Isento com base no artigo nº 15º. 1 g) do CIVA',NULL,'M36'),(22,'Isento com base no artigo nº 15º. 1 h) do CIVA',NULL,'M37'),(23,'Isento com base no artigo nº15º. 1 i) do CIVA',NULL,'M38');
/*!40000 ALTER TABLE `produtos_motivos_isensao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promocao`
--

DROP TABLE IF EXISTS `promocao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promocao` (
  `pk_promocao` int(11) NOT NULL AUTO_INCREMENT,
  `percentagem` double DEFAULT NULL,
  `data` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `retalho_groso` tinyint(1) DEFAULT '1',
  `fk_usuario` int(11) NOT NULL,
  PRIMARY KEY (`pk_promocao`),
  KEY `fk_promocao_tb_usuario1_idx` (`fk_usuario`),
  CONSTRAINT `fk_promocao_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promocao`
--

LOCK TABLES `promocao` WRITE;
/*!40000 ALTER TABLE `promocao` DISABLE KEYS */;
INSERT INTO `promocao` VALUES (1,0,'2018-11-07','09:26:58',1,15);
/*!40000 ALTER TABLE `promocao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recibo_rh`
--

DROP TABLE IF EXISTS `recibo_rh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recibo_rh` (
  `pk_recibo_rh` int(11) NOT NULL AUTO_INCREMENT,
  `data_hora` datetime DEFAULT NULL,
  `total_remuneracao` double DEFAULT NULL,
  `total_desconto` double DEFAULT NULL,
  `total_pago` double DEFAULT NULL,
  `fk_forma_pagamento` int(11) NOT NULL,
  `fk_funcionario` int(11) NOT NULL,
  `vencimento` double DEFAULT NULL,
  `dias_trabalhado` double DEFAULT NULL,
  `fk_fecho_periodo` int(11) DEFAULT NULL,
  `periodo` enum('Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro') DEFAULT NULL,
  PRIMARY KEY (`pk_recibo_rh`),
  KEY `fk_recibo_rh_forma_pagamento1_idx` (`fk_forma_pagamento`),
  KEY `fk_recibo_rh_tb_funcionario1_idx` (`fk_funcionario`),
  KEY `fk_fecho_periodo_idx` (`fk_fecho_periodo`),
  CONSTRAINT `fk_fecho_periodo` FOREIGN KEY (`fk_fecho_periodo`) REFERENCES `fecho_periodo` (`pk_fecho_periodo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_recibo_rh_forma_pagamento1` FOREIGN KEY (`fk_forma_pagamento`) REFERENCES `forma_pagamento` (`pk_forma_pagamento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_recibo_rh_tb_funcionario1` FOREIGN KEY (`fk_funcionario`) REFERENCES `tb_funcionario` (`idFuncionario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recibo_rh`
--

LOCK TABLES `recibo_rh` WRITE;
/*!40000 ALTER TABLE `recibo_rh` DISABLE KEYS */;
/*!40000 ALTER TABLE `recibo_rh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regime`
--

DROP TABLE IF EXISTS `regime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `regime` (
  `pk_regime` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(100) DEFAULT NULL,
  `valor` double DEFAULT NULL,
  PRIMARY KEY (`pk_regime`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regime`
--

LOCK TABLES `regime` WRITE;
/*!40000 ALTER TABLE `regime` DISABLE KEYS */;
INSERT INTO `regime` VALUES (1,'Regime Geral',NULL),(2,'Regime Simplificado',NULL),(3,'Regime de Exclusão',NULL);
/*!40000 ALTER TABLE `regime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `retencao`
--

DROP TABLE IF EXISTS `retencao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `retencao` (
  `pk_retencao` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(45) DEFAULT NULL,
  `taxa` double DEFAULT NULL,
  `data_hora` datetime DEFAULT NULL,
  PRIMARY KEY (`pk_retencao`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `retencao`
--

LOCK TABLES `retencao` WRITE;
/*!40000 ALTER TABLE `retencao` DISABLE KEYS */;
INSERT INTO `retencao` VALUES (1,'retencao',6.5,'0000-00-00 00:00:00'),(2,'retencao',0,'0000-00-00 00:00:00');
/*!40000 ALTER TABLE `retencao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `saidas_tesouraria`
--

DROP TABLE IF EXISTS `saidas_tesouraria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `saidas_tesouraria` (
  `pk_saidas_tesouraria` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(900) DEFAULT NULL,
  `valor` double DEFAULT NULL,
  `data_saida` date DEFAULT NULL,
  `hora_saida` time DEFAULT NULL,
  `fk_usuario` int(11) DEFAULT NULL,
  `fk_banco` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_saidas_tesouraria`),
  KEY `newfkUsers` (`fk_usuario`),
  KEY `newfkBanks` (`fk_banco`),
  CONSTRAINT `newfkBanks` FOREIGN KEY (`fk_banco`) REFERENCES `tb_banco` (`idBanco`),
  CONSTRAINT `newfkUsers` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `saidas_tesouraria`
--

LOCK TABLES `saidas_tesouraria` WRITE;
/*!40000 ALTER TABLE `saidas_tesouraria` DISABLE KEYS */;
INSERT INTO `saidas_tesouraria` VALUES (1,'dfggadsgsad',500,'2017-11-17','20:32:23',15,1),(2,'teggegewr',600,'2017-11-17','20:32:50',15,1),(3,'fnsdfnfns',500,'2017-11-17','21:02:06',15,1),(4,'Para taxi do Francisco',450,'2017-11-21','20:52:21',15,1),(5,'Para compra de Combustivel p/ Gerador',1000,'2017-11-29','01:20:52',15,1),(6,'Para deposito no BIC',2000,'2017-11-29','01:22:50',15,1);
/*!40000 ALTER TABLE `saidas_tesouraria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seccao`
--

DROP TABLE IF EXISTS `seccao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seccao` (
  `pk_seccao` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `descricao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_seccao`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seccao`
--

LOCK TABLES `seccao` WRITE;
/*!40000 ALTER TABLE `seccao` DISABLE KEYS */;
/*!40000 ALTER TABLE `seccao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servico_retencao`
--

DROP TABLE IF EXISTS `servico_retencao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servico_retencao` (
  `pk_servico_retencao` int(11) NOT NULL AUTO_INCREMENT,
  `fk_produto` int(11) DEFAULT NULL,
  `fk_retencao` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_servico_retencao`),
  KEY `fk_prods_idx` (`fk_produto`),
  KEY `fk_retencao_idx` (`fk_retencao`),
  CONSTRAINT `fk_prods` FOREIGN KEY (`fk_produto`) REFERENCES `tb_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_retencao` FOREIGN KEY (`fk_retencao`) REFERENCES `retencao` (`pk_retencao`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servico_retencao`
--

LOCK TABLES `servico_retencao` WRITE;
/*!40000 ALTER TABLE `servico_retencao` DISABLE KEYS */;
/*!40000 ALTER TABLE `servico_retencao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_acerto`
--

DROP TABLE IF EXISTS `tb_acerto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_acerto` (
  `idAcerto` int(11) NOT NULL AUTO_INCREMENT,
  `data_acerto` date DEFAULT NULL,
  `idProduto` int(11) DEFAULT NULL,
  `idUsuario` int(11) DEFAULT NULL,
  `idArmazemFK` int(10) unsigned DEFAULT NULL,
  `quantidade` int(11) DEFAULT NULL,
  PRIMARY KEY (`idAcerto`),
  KEY `Fk_produto` (`idProduto`),
  KEY `Fk_user` (`idUsuario`),
  KEY `Fk_armazem` (`idArmazemFK`),
  CONSTRAINT `Fk_armazem` FOREIGN KEY (`idArmazemFK`) REFERENCES `tb_armazem` (`codigo`),
  CONSTRAINT `Fk_produto` FOREIGN KEY (`idProduto`) REFERENCES `tb_produto` (`codigo`),
  CONSTRAINT `Fk_user` FOREIGN KEY (`idUsuario`) REFERENCES `tb_usuario` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_acerto`
--

LOCK TABLES `tb_acerto` WRITE;
/*!40000 ALTER TABLE `tb_acerto` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_acerto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_adiantamento`
--

DROP TABLE IF EXISTS `tb_adiantamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_adiantamento` (
  `idAdiantamentoFK` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `valor_adiantado` double NOT NULL,
  `descricao` varchar(45) NOT NULL,
  `data` date NOT NULL,
  `hora` time NOT NULL,
  `idFuncionarioFK` int(11) DEFAULT NULL,
  PRIMARY KEY (`idAdiantamentoFK`),
  KEY `Fkfuncionar` (`idFuncionarioFK`),
  CONSTRAINT `Fkfuncionar` FOREIGN KEY (`idFuncionarioFK`) REFERENCES `tb_funcionario` (`idFuncionario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_adiantamento`
--

LOCK TABLES `tb_adiantamento` WRITE;
/*!40000 ALTER TABLE `tb_adiantamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_adiantamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_ano`
--

DROP TABLE IF EXISTS `tb_ano`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_ano` (
  `idAno` int(11) NOT NULL AUTO_INCREMENT,
  `descrisao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idAno`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_ano`
--

LOCK TABLES `tb_ano` WRITE;
/*!40000 ALTER TABLE `tb_ano` DISABLE KEYS */;
INSERT INTO `tb_ano` VALUES (1,'2020'),(2,'2021'),(3,'2022'),(4,'2023'),(5,'2024');
/*!40000 ALTER TABLE `tb_ano` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_armazem`
--

DROP TABLE IF EXISTS `tb_armazem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_armazem` (
  `codigo` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) NOT NULL,
  PRIMARY KEY (`codigo`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_armazem`
--

LOCK TABLES `tb_armazem` WRITE;
/*!40000 ALTER TABLE `tb_armazem` DISABLE KEYS */;
INSERT INTO `tb_armazem` VALUES (1,'Armazem 1'),(2,'Loja');
/*!40000 ALTER TABLE `tb_armazem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_balanco`
--

DROP TABLE IF EXISTS `tb_balanco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_balanco` (
  `pk_balanco` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_balanco` date DEFAULT NULL,
  `valor_banco_outros` double DEFAULT NULL,
  `valor_banco_caixa` double DEFAULT NULL,
  `valor_banco_stock` double DEFAULT NULL,
  PRIMARY KEY (`pk_balanco`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_balanco`
--

LOCK TABLES `tb_balanco` WRITE;
/*!40000 ALTER TABLE `tb_balanco` DISABLE KEYS */;
INSERT INTO `tb_balanco` VALUES (1,'2017-11-29',7400,600,7363755.2067485);
/*!40000 ALTER TABLE `tb_balanco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_banco`
--

DROP TABLE IF EXISTS `tb_banco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_banco` (
  `idBanco` int(11) NOT NULL AUTO_INCREMENT,
  `descrisao` varchar(45) DEFAULT NULL,
  `numero` varchar(45) DEFAULT NULL,
  `saldo_banco` double DEFAULT NULL,
  PRIMARY KEY (`idBanco`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_banco`
--

LOCK TABLES `tb_banco` WRITE;
/*!40000 ALTER TABLE `tb_banco` DISABLE KEYS */;
INSERT INTO `tb_banco` VALUES (1,'Numerário','86968861',4667862202.38459),(2,'Multicaixa','2748823',3768710.08),(3,'Deposito','3331313',18329158.1656973);
/*!40000 ALTER TABLE `tb_banco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_cliente`
--

DROP TABLE IF EXISTS `tb_cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_cliente` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `morada` varchar(300) DEFAULT NULL,
  `telefone` varchar(150) DEFAULT NULL,
  `nif` varchar(100) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_cliente`
--

LOCK TABLES `tb_cliente` WRITE;
/*!40000 ALTER TABLE `tb_cliente` DISABLE KEYS */;
INSERT INTO `tb_cliente` VALUES (1,'Consumidor Final','N/A','999999999','999999999','N/A');
/*!40000 ALTER TABLE `tb_cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_clientes_encomenda`
--

DROP TABLE IF EXISTS `tb_clientes_encomenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_clientes_encomenda` (
  `idCliente` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nome_cliente` varchar(45) NOT NULL,
  `telefone` varchar(45) NOT NULL,
  `endereco` varchar(45) NOT NULL,
  `saldo` double NOT NULL,
  `email` varchar(45) NOT NULL,
  `credito` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`idCliente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_clientes_encomenda`
--

LOCK TABLES `tb_clientes_encomenda` WRITE;
/*!40000 ALTER TABLE `tb_clientes_encomenda` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_clientes_encomenda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_conta`
--

DROP TABLE IF EXISTS `tb_conta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_conta` (
  `idContaPK` int(11) NOT NULL AUTO_INCREMENT,
  `numero_conta` varchar(45) DEFAULT NULL,
  `saldo_conta` double DEFAULT NULL,
  `idBancoFK` int(11) DEFAULT '1',
  `iban` varchar(100) DEFAULT NULL,
  `idFuncionarioFK` int(11) DEFAULT NULL,
  PRIMARY KEY (`idContaPK`),
  KEY `FkBanco` (`idBancoFK`),
  KEY `FkFunciona` (`idFuncionarioFK`),
  CONSTRAINT `FkBanco` FOREIGN KEY (`idBancoFK`) REFERENCES `tb_banco` (`idBanco`),
  CONSTRAINT `FkFunciona` FOREIGN KEY (`idFuncionarioFK`) REFERENCES `tb_funcionario` (`idFuncionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_conta`
--

LOCK TABLES `tb_conta` WRITE;
/*!40000 ALTER TABLE `tb_conta` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_conta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_dados_instituicao`
--

DROP TABLE IF EXISTS `tb_dados_instituicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_dados_instituicao` (
  `idDadosInsitiuicao` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(500) NOT NULL,
  `telefone` varchar(45) NOT NULL,
  `enderecos` varchar(100) NOT NULL,
  `email` varchar(500) NOT NULL,
  `nif` varchar(45) NOT NULL,
  `cont` int(11) DEFAULT '0',
  `conta_bancaria1` varchar(700) DEFAULT NULL,
  `conta_bancaria2` varchar(700) DEFAULT NULL,
  `conta_bancaria3` varchar(700) DEFAULT NULL,
  `conta_bancaria4` varchar(700) DEFAULT NULL,
  `conta_bancaria5` varchar(700) DEFAULT NULL,
  `conta_bancaria6` varchar(700) DEFAULT NULL,
  `director_geral` varchar(150) DEFAULT NULL,
  `numero_vias` int(11) DEFAULT '2',
  `impressora` enum('A6','A6_O','S_A6_O','A4','A7','A5','S_A6','A6V') DEFAULT 'A6',
  `foco` enum('Codigo Interno','Codigo de Barra','Codigo Manual') DEFAULT 'Codigo Interno',
  `docpadrao` enum('Factura/Recibo','Factura','Factura-Proforma','Guia de Transporte') DEFAULT 'Factura/Recibo',
  `desactivarvias` enum('Sim','Nao') DEFAULT 'Sim',
  `desconto_financeiro` enum('Activar','Desactivar') DEFAULT 'Activar',
  `ano_economico` enum('Mostrar','Ocultar') DEFAULT 'Mostrar',
  `vizualisar_stock` enum('Vizualisar Stock','Nao Vizualisar Stock') DEFAULT 'Vizualisar Stock',
  `transtorno` enum('Activo','Desactivo') DEFAULT 'Activo',
  `correio_caixa` varchar(45) DEFAULT NULL,
  `negocio` enum('Oficina','Comercial','Transportes','Restaurante','Farmacia','Lavandaria','Prestação de Serviço','Layout') DEFAULT 'Comercial',
  `obs_ft` varchar(700) DEFAULT NULL,
  `prazo_ft` varchar(150) DEFAULT NULL,
  `local_carregamento` varchar(200) DEFAULT NULL,
  `conta_bancaria7` varchar(700) DEFAULT NULL,
  `conta_bancaria8` varchar(700) DEFAULT NULL,
  `slogan` varchar(45) DEFAULT NULL,
  `obs_devolucao` varchar(300) DEFAULT NULL,
  `teclado` enum('Normal','Toutch') DEFAULT 'Normal',
  `data_licenca` date DEFAULT NULL,
  `regime` enum('Regime de Exclusão','Regime Simplificado','Regime Geral') DEFAULT 'Regime de Exclusão',
  `regime_contrato` enum('anual','mensal') DEFAULT 'anual',
  `config_armazens` enum('Multi_armazem','Uni_armazem') DEFAULT 'Multi_armazem',
  `usar_dois_precos` enum('sim','nao') DEFAULT 'nao',
  `impressora_cozinha` varchar(45) DEFAULT NULL,
  `chave_mestre` varchar(45) DEFAULT NULL,
  `data_fecho` date DEFAULT NULL,
  `tesouraria` enum('Mostrar','Ocultar') DEFAULT 'Mostrar',
  `rh` enum('Mostrar','Ocultar') DEFAULT 'Mostrar',
  `comercial` enum('Mostrar','Ocultar') DEFAULT 'Mostrar',
  `janela_servico` enum('Manter fixa','Não Manter fixa') DEFAULT 'Não Manter fixa',
  `impressora_sala` varchar(45) DEFAULT NULL,
  `prazo_proforma` int(10) unsigned DEFAULT NULL,
  `desactivar_lugares` enum('Sim','Nao') DEFAULT 'Sim',
  `hora_comeco_venda` time DEFAULT NULL,
  `hora_termino_venda` time DEFAULT NULL,
  `tipo_fecho_caixa` enum('Normal','Simplificado') DEFAULT 'Normal',
  `enviar_email` enum('Nao','Sim') DEFAULT 'Sim',
  `stock_consulta` enum('Activo','Desactivo') NOT NULL DEFAULT 'Desactivo',
  `tipo_ficha_tecnica` enum('A','B','Nenhum') NOT NULL,
  PRIMARY KEY (`idDadosInsitiuicao`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_dados_instituicao`
--

LOCK TABLES `tb_dados_instituicao` WRITE;
/*!40000 ALTER TABLE `tb_dados_instituicao` DISABLE KEYS */;
INSERT INTO `tb_dados_instituicao` VALUES (1,'DVML COMERCIAL, LDA','921734126','Bº.Talatona, Rua da Logistica, Luanda','dvml.comercial@gmail.com','5484027543',1,'147515114.10.001','','','AO06.0040.0000.4751.5114.1019.4','','','Martinho Luis',1,'A6','Codigo de Barra','Factura/Recibo','Sim','Activar','Ocultar','Vizualisar Stock','Desactivo',NULL,'Comercial','','','','','','','Não aceitamos devolução/Troca','Normal','2024-12-17','Regime Geral','anual','Multi_armazem','nao','EPSON TM-U220 Receipt','123','2025-12-17','Ocultar','Ocultar','Mostrar','Não Manter fixa','',1,'Sim','06:40:00','21:30:00','Simplificado','Nao','Desactivo','Nenhum');
/*!40000 ALTER TABLE `tb_dados_instituicao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_departamento`
--

DROP TABLE IF EXISTS `tb_departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_departamento` (
  `pk_departamento` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_departamento`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_departamento`
--

LOCK TABLES `tb_departamento` WRITE;
/*!40000 ALTER TABLE `tb_departamento` DISABLE KEYS */;
INSERT INTO `tb_departamento` VALUES (1,'FINANÇAS'),(2,'COMERCIAL');
/*!40000 ALTER TABLE `tb_departamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_desconto`
--

DROP TABLE IF EXISTS `tb_desconto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_desconto` (
  `idDesconto` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `quantidade` double unsigned NOT NULL,
  `fk_produto` int(11) NOT NULL,
  `fk_cliente` int(11) NOT NULL,
  `fk_usuario` int(11) NOT NULL,
  `valor` double DEFAULT '0',
  PRIMARY KEY (`idDesconto`),
  KEY `fk_tb_desconto_tb_produto1_idx` (`fk_produto`),
  KEY `fk_tb_desconto_tb_cliente1_idx` (`fk_cliente`),
  KEY `fk_tb_desconto_tb_usuario1_idx` (`fk_usuario`),
  CONSTRAINT `fk_tb_desconto_tb_cliente1` FOREIGN KEY (`fk_cliente`) REFERENCES `tb_cliente` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_desconto_tb_produto1` FOREIGN KEY (`fk_produto`) REFERENCES `tb_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_desconto_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_desconto`
--

LOCK TABLES `tb_desconto` WRITE;
/*!40000 ALTER TABLE `tb_desconto` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_desconto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_encomenda`
--

DROP TABLE IF EXISTS `tb_encomenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_encomenda` (
  `idEncomenda` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_encomenda` datetime NOT NULL,
  `data_entrega_prevista` date NOT NULL,
  `idCliente` bigint(20) unsigned NOT NULL,
  `total_encomenda` varchar(45) NOT NULL,
  `status_entrega` tinyint(1) DEFAULT NULL,
  `obs` varchar(45) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  PRIMARY KEY (`idEncomenda`),
  KEY `FK_tb_encomenda_1` (`idCliente`),
  KEY `FK_tb_encomenda_2` (`idUsuario`),
  CONSTRAINT `FK_tb_encomenda_1` FOREIGN KEY (`idCliente`) REFERENCES `tb_clientes_encomenda` (`idCliente`),
  CONSTRAINT `FK_tb_encomenda_2` FOREIGN KEY (`idUsuario`) REFERENCES `tb_usuario` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_encomenda`
--

LOCK TABLES `tb_encomenda` WRITE;
/*!40000 ALTER TABLE `tb_encomenda` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_encomenda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_entrada`
--

DROP TABLE IF EXISTS `tb_entrada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_entrada` (
  `idEntrada` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `data_entrada` date NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `idArmazemFK` int(10) unsigned NOT NULL DEFAULT '1',
  `status_eliminado` varchar(45) DEFAULT NULL,
  `quantidade` double DEFAULT NULL,
  `idProduto` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`idEntrada`),
  KEY `FK_tb_entrada_1` (`idUsuario`),
  KEY `FK_tb_entrada_3` (`idArmazemFK`) USING BTREE,
  CONSTRAINT `FK_tb_entrada_1` FOREIGN KEY (`idUsuario`) REFERENCES `tb_usuario` (`codigo`),
  CONSTRAINT `FK_tb_entrada_3` FOREIGN KEY (`idArmazemFK`) REFERENCES `tb_armazem` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_entrada`
--

LOCK TABLES `tb_entrada` WRITE;
/*!40000 ALTER TABLE `tb_entrada` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_entrada` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_entrada_vasilhame`
--

DROP TABLE IF EXISTS `tb_entrada_vasilhame`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_entrada_vasilhame` (
  `pk_entrada_vasilhame` int(10) NOT NULL AUTO_INCREMENT,
  `data_entrada` date DEFAULT NULL,
  `hora_entrada` time DEFAULT NULL,
  `fk_usuario` int(10) DEFAULT NULL,
  `fk_amazem` int(10) unsigned DEFAULT '1',
  PRIMARY KEY (`pk_entrada_vasilhame`),
  KEY `usuario` (`fk_usuario`),
  KEY `armazem` (`fk_amazem`) USING BTREE,
  CONSTRAINT `armazem` FOREIGN KEY (`fk_amazem`) REFERENCES `tb_armazem` (`codigo`),
  CONSTRAINT `usuario` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_entrada_vasilhame`
--

LOCK TABLES `tb_entrada_vasilhame` WRITE;
/*!40000 ALTER TABLE `tb_entrada_vasilhame` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_entrada_vasilhame` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_especialidade`
--

DROP TABLE IF EXISTS `tb_especialidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_especialidade` (
  `pk_especialidade` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) NOT NULL,
  PRIMARY KEY (`pk_especialidade`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_especialidade`
--

LOCK TABLES `tb_especialidade` WRITE;
/*!40000 ALTER TABLE `tb_especialidade` DISABLE KEYS */;
INSERT INTO `tb_especialidade` VALUES (1,'TEC.FINANÇAS');
/*!40000 ALTER TABLE `tb_especialidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_estado_civil`
--

DROP TABLE IF EXISTS `tb_estado_civil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_estado_civil` (
  `pk_estado_civil` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_estado_civil`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_estado_civil`
--

LOCK TABLES `tb_estado_civil` WRITE;
/*!40000 ALTER TABLE `tb_estado_civil` DISABLE KEYS */;
INSERT INTO `tb_estado_civil` VALUES (1,'Viuvo(a)'),(2,'Divorciado(a)'),(3,'Solteiro(a)'),(4,'Casado(a)');
/*!40000 ALTER TABLE `tb_estado_civil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_estorno`
--

DROP TABLE IF EXISTS `tb_estorno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_estorno` (
  `pk_estorno` int(11) NOT NULL AUTO_INCREMENT,
  `data_estorno` date NOT NULL,
  `fk_usuario` int(11) NOT NULL,
  `idArmazemFK` int(10) unsigned NOT NULL DEFAULT '1',
  `hora_estorno` time NOT NULL,
  `status_eliminado` varchar(45) NOT NULL,
  `obs` varchar(1000) NOT NULL,
  PRIMARY KEY (`pk_estorno`),
  KEY `usuarioFk_idx` (`fk_usuario`),
  KEY `armazensFk_idx` (`idArmazemFK`),
  CONSTRAINT `armazensFk` FOREIGN KEY (`idArmazemFK`) REFERENCES `tb_armazem` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `usuarioFk` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_estorno`
--

LOCK TABLES `tb_estorno` WRITE;
/*!40000 ALTER TABLE `tb_estorno` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_estorno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_falta`
--

DROP TABLE IF EXISTS `tb_falta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_falta` (
  `idFaltaPK` int(11) NOT NULL AUTO_INCREMENT,
  `n_falta` double DEFAULT NULL,
  `data` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `descricao_falta` varchar(200) DEFAULT NULL,
  `idFuncionarioFK` int(11) DEFAULT NULL,
  `justificada_injustificada` tinyint(4) DEFAULT NULL,
  `descricao_justificativa` varchar(200) DEFAULT NULL,
  `data_justificativa` date DEFAULT NULL,
  PRIMARY KEY (`idFaltaPK`),
  KEY `FkFuncionai` (`idFuncionarioFK`),
  CONSTRAINT `FkFuncionai` FOREIGN KEY (`idFuncionarioFK`) REFERENCES `tb_funcionario` (`idFuncionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_falta`
--

LOCK TABLES `tb_falta` WRITE;
/*!40000 ALTER TABLE `tb_falta` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_falta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_forma_pagamento`
--

DROP TABLE IF EXISTS `tb_forma_pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_forma_pagamento` (
  `pk_forma_pagamento` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `descricao` varchar(50) NOT NULL,
  PRIMARY KEY (`pk_forma_pagamento`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_forma_pagamento`
--

LOCK TABLES `tb_forma_pagamento` WRITE;
/*!40000 ALTER TABLE `tb_forma_pagamento` DISABLE KEYS */;
INSERT INTO `tb_forma_pagamento` VALUES (1,'Dinheiro'),(2,'Crédito'),(3,'Multicaixa');
/*!40000 ALTER TABLE `tb_forma_pagamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_fornecedor`
--

DROP TABLE IF EXISTS `tb_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_fornecedor` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `telefone` varchar(200) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `site` varchar(45) DEFAULT NULL,
  `endereco` varchar(200) DEFAULT NULL,
  `nif` varchar(45) DEFAULT NULL,
  `fk_regime` int(11) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `newfk_regime` (`fk_regime`),
  CONSTRAINT `newfk_regime` FOREIGN KEY (`fk_regime`) REFERENCES `regime` (`pk_regime`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_fornecedor`
--

LOCK TABLES `tb_fornecedor` WRITE;
/*!40000 ALTER TABLE `tb_fornecedor` DISABLE KEYS */;
INSERT INTO `tb_fornecedor` VALUES (1,'Diferenciado','999-999-999','N/A','N/A','N/A','999999999',3);
/*!40000 ALTER TABLE `tb_fornecedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_funcao`
--

DROP TABLE IF EXISTS `tb_funcao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_funcao` (
  `pk_funcao` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_funcao`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_funcao`
--

LOCK TABLES `tb_funcao` WRITE;
/*!40000 ALTER TABLE `tb_funcao` DISABLE KEYS */;
INSERT INTO `tb_funcao` VALUES (1,'GESTOR');
/*!40000 ALTER TABLE `tb_funcao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_funcionario`
--

DROP TABLE IF EXISTS `tb_funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_funcionario` (
  `idFuncionario` int(11) NOT NULL AUTO_INCREMENT,
  `numero_funcionario` varchar(100) DEFAULT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `telefone` varchar(45) DEFAULT NULL,
  `morada` varchar(45) DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `habilitacao_literaria` varchar(100) DEFAULT NULL,
  `dias_instituicao` varchar(100) DEFAULT NULL,
  `fk_funcao` int(11) DEFAULT NULL,
  `fk_departamento` int(11) DEFAULT NULL,
  `fk_grau_academico` int(11) DEFAULT NULL,
  `fk_especialidade` int(11) DEFAULT NULL,
  `nif` varchar(100) DEFAULT NULL,
  `data_nascimento` date DEFAULT NULL,
  `docID` varchar(45) DEFAULT NULL,
  `ndocID` varchar(100) DEFAULT NULL,
  `data_emissao_docID` date DEFAULT NULL,
  `data_validade_docID` date DEFAULT NULL,
  `fk_estado_civil` int(11) DEFAULT NULL,
  `idStatusFK` int(11) DEFAULT '1',
  `fkUsuario` int(11) DEFAULT NULL,
  `fk_modalidade` int(11) NOT NULL,
  `fk_empresa` int(11) NOT NULL,
  `sexo` enum('Masculino','Feminino') DEFAULT NULL,
  `n_seguranca_social` varchar(100) DEFAULT NULL,
  `desconto_seguranca_social` enum('Sim','Não') DEFAULT NULL,
  `data_inicio_contrato` date DEFAULT NULL,
  `data_fim_contrato` date DEFAULT NULL,
  `telefone_1` varchar(50) DEFAULT NULL,
  `tipo_contrato` enum('Determinado','Indeterminado') DEFAULT NULL,
  `duracao_contrato` enum('1 Mes','3 Meses','6 Meses','9 Meses','12 Meses','Indeterminado') DEFAULT NULL,
  `data_contrato` date DEFAULT NULL,
  `conta_fechada` tinyint(4) DEFAULT '0',
  `motivo_fecho` enum('Despediu-se','Foi Despedido','Contrato Terminado') DEFAULT NULL,
  `photo` longblob,
  `telefone_2` varchar(50) DEFAULT NULL,
  `forma_pagamento` enum('Transferência','Depósito','Cash') DEFAULT NULL,
  `activo` int(11) DEFAULT NULL,
  `data_validade_bi` date DEFAULT NULL,
  `nbi` varchar(45) DEFAULT NULL,
  `fk_forma_pagamento` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`idFuncionario`),
  KEY `FkEstadoCivile` (`fk_estado_civil`),
  KEY `FkFuncao` (`fk_funcao`),
  KEY `FkDepartamento` (`fk_departamento`),
  KEY `FkGrauAca` (`fk_grau_academico`),
  KEY `FkEspecial` (`fk_especialidade`),
  KEY `FkStatu` (`idStatusFK`),
  KEY `fkUsuario` (`fkUsuario`),
  KEY `fk_tb_funcionario_modalidade1_idx` (`fk_modalidade`),
  KEY `fk_tb_funcionario_empresa1_idx` (`fk_empresa`),
  CONSTRAINT `FkDepartamento` FOREIGN KEY (`fk_departamento`) REFERENCES `tb_departamento` (`pk_departamento`),
  CONSTRAINT `FkEspecial` FOREIGN KEY (`fk_especialidade`) REFERENCES `tb_especialidade` (`pk_especialidade`),
  CONSTRAINT `FkEstadoCivile` FOREIGN KEY (`fk_estado_civil`) REFERENCES `tb_estado_civil` (`pk_estado_civil`),
  CONSTRAINT `FkFuncao` FOREIGN KEY (`fk_funcao`) REFERENCES `tb_funcao` (`pk_funcao`),
  CONSTRAINT `FkGrauAca` FOREIGN KEY (`fk_grau_academico`) REFERENCES `tb_grau_academico` (`pk_grau_academico`),
  CONSTRAINT `FkStatu` FOREIGN KEY (`idStatusFK`) REFERENCES `tb_status` (`idStatus`),
  CONSTRAINT `fkUsuario` FOREIGN KEY (`fkUsuario`) REFERENCES `tb_usuario` (`codigo`),
  CONSTRAINT `fk_tb_funcionario_empresa1` FOREIGN KEY (`fk_empresa`) REFERENCES `empresa` (`pk_empresa`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_funcionario_modalidade1` FOREIGN KEY (`fk_modalidade`) REFERENCES `modalidade` (`pk_modalidade`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_funcionario`
--

LOCK TABLES `tb_funcionario` WRITE;
/*!40000 ALTER TABLE `tb_funcionario` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_grau_academico`
--

DROP TABLE IF EXISTS `tb_grau_academico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_grau_academico` (
  `pk_grau_academico` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_grau_academico`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_grau_academico`
--

LOCK TABLES `tb_grau_academico` WRITE;
/*!40000 ALTER TABLE `tb_grau_academico` DISABLE KEYS */;
INSERT INTO `tb_grau_academico` VALUES (1,'MESTRE'),(2,'LICENCIADO'),(3,'TÉC.MÉDIO');
/*!40000 ALTER TABLE `tb_grau_academico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_desconto_funcionario`
--

DROP TABLE IF EXISTS `tb_item_desconto_funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_desconto_funcionario` (
  `idItemDescontoFuncionarioPK` int(11) NOT NULL AUTO_INCREMENT,
  `valor_desconto` double DEFAULT NULL,
  `idTipoDescontoFK` int(11) DEFAULT NULL,
  `idFuncionarioFK` int(11) DEFAULT NULL,
  PRIMARY KEY (`idItemDescontoFuncionarioPK`),
  KEY `FkTipoDesconto` (`idTipoDescontoFK`),
  KEY `FkFuncionario` (`idFuncionarioFK`),
  CONSTRAINT `FkFuncionario` FOREIGN KEY (`idFuncionarioFK`) REFERENCES `tb_funcionario` (`idFuncionario`),
  CONSTRAINT `FkTipoDesconto` FOREIGN KEY (`idTipoDescontoFK`) REFERENCES `tb_tipo_desconto` (`idTipoDescontoPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_desconto_funcionario`
--

LOCK TABLES `tb_item_desconto_funcionario` WRITE;
/*!40000 ALTER TABLE `tb_item_desconto_funcionario` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_item_desconto_funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_encomenda`
--

DROP TABLE IF EXISTS `tb_item_encomenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_encomenda` (
  `idEncomenda` bigint(20) unsigned NOT NULL DEFAULT '1',
  `idProduto` int(11) NOT NULL,
  `total` double NOT NULL,
  `quantidade` int(10) unsigned NOT NULL,
  `codigo` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`codigo`),
  KEY `FK_tb_item_encomenda_1` (`idEncomenda`),
  KEY `FK_tb_item_encomenda_2` (`idProduto`),
  CONSTRAINT `FK_tb_item_encomenda_1` FOREIGN KEY (`idEncomenda`) REFERENCES `tb_encomenda` (`idEncomenda`),
  CONSTRAINT `FK_tb_item_encomenda_2` FOREIGN KEY (`idProduto`) REFERENCES `tb_produto` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_encomenda`
--

LOCK TABLES `tb_item_encomenda` WRITE;
/*!40000 ALTER TABLE `tb_item_encomenda` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_item_encomenda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_entrada_vasilhame`
--

DROP TABLE IF EXISTS `tb_item_entrada_vasilhame`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_entrada_vasilhame` (
  `pk_item_entrada_vasilhame` int(10) NOT NULL AUTO_INCREMENT,
  `qtd` int(10) NOT NULL,
  `fk_vasilhame` int(10) NOT NULL,
  `fk_entrada_vasilhame` int(10) NOT NULL,
  PRIMARY KEY (`pk_item_entrada_vasilhame`),
  KEY `pk_vasilhame` (`fk_vasilhame`),
  KEY `pk_entrada_vasilhame` (`fk_entrada_vasilhame`),
  CONSTRAINT `pk_entrada_vasilhame` FOREIGN KEY (`fk_entrada_vasilhame`) REFERENCES `tb_entrada_vasilhame` (`pk_entrada_vasilhame`),
  CONSTRAINT `pk_vasilhame` FOREIGN KEY (`fk_vasilhame`) REFERENCES `tb_vasilhame` (`pk_vasilhame`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_entrada_vasilhame`
--

LOCK TABLES `tb_item_entrada_vasilhame` WRITE;
/*!40000 ALTER TABLE `tb_item_entrada_vasilhame` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_item_entrada_vasilhame` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_entradas`
--

DROP TABLE IF EXISTS `tb_item_entradas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_entradas` (
  `id_item_entradas` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_produto` int(10) NOT NULL,
  `quantidade` double NOT NULL,
  `fk_entradas` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_item_entradas`),
  KEY `FK_tb_produtos` (`id_produto`),
  KEY `FK_tb_entradas` (`fk_entradas`),
  CONSTRAINT `FK_tb_entradas` FOREIGN KEY (`fk_entradas`) REFERENCES `tb_entrada` (`idEntrada`),
  CONSTRAINT `FK_tb_produtos` FOREIGN KEY (`id_produto`) REFERENCES `tb_produto` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_entradas`
--

LOCK TABLES `tb_item_entradas` WRITE;
/*!40000 ALTER TABLE `tb_item_entradas` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_item_entradas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_estorno`
--

DROP TABLE IF EXISTS `tb_item_estorno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_estorno` (
  `codigo` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fk_estorno` int(11) NOT NULL,
  `fk_produtos` int(11) DEFAULT NULL,
  `quantidade` int(11) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `estornoFk_idx` (`fk_estorno`),
  KEY `produtosFk_idx` (`fk_produtos`),
  CONSTRAINT `estornoFk` FOREIGN KEY (`fk_estorno`) REFERENCES `tb_estorno` (`pk_estorno`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `produtosFk` FOREIGN KEY (`fk_produtos`) REFERENCES `tb_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_estorno`
--

LOCK TABLES `tb_item_estorno` WRITE;
/*!40000 ALTER TABLE `tb_item_estorno` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_item_estorno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_pedidos`
--

DROP TABLE IF EXISTS `tb_item_pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_pedidos` (
  `pk_item_pedidos` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fk_lugares` int(11) unsigned NOT NULL,
  `fk_produtos` int(11) NOT NULL,
  `qtd` double unsigned DEFAULT NULL,
  `fk_pedidos` int(11) unsigned NOT NULL,
  `status_convertido` tinyint(1) unsigned DEFAULT '0' COMMENT 'verifica se a factura e convertida',
  `total_item` double NOT NULL,
  `status_enviado` tinyint(1) DEFAULT '0',
  `status_efectuado` tinyint(1) DEFAULT '0',
  `data_entrega` datetime DEFAULT NULL,
  `obs` varchar(500) DEFAULT NULL,
  `preco` double DEFAULT NULL,
  PRIMARY KEY (`pk_item_pedidos`) USING BTREE,
  KEY `Index_2_pk_lugares` (`fk_lugares`),
  KEY `Index_3_fk_produtos` (`fk_produtos`),
  KEY `Index_5_fk_pedidos` (`fk_pedidos`),
  CONSTRAINT `FK_tb_item_pedidos_2_fk_lugares` FOREIGN KEY (`fk_lugares`) REFERENCES `tb_lugares` (`pk_lugares`),
  CONSTRAINT `FK_tb_item_pedidos_3_fk_produtos` FOREIGN KEY (`fk_produtos`) REFERENCES `tb_produto` (`codigo`),
  CONSTRAINT `FK_tb_item_pedidos_4_fk_pedidos` FOREIGN KEY (`fk_pedidos`) REFERENCES `tb_pedido` (`pk_pedido`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_pedidos`
--

LOCK TABLES `tb_item_pedidos` WRITE;
/*!40000 ALTER TABLE `tb_item_pedidos` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_item_pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_permissao`
--

DROP TABLE IF EXISTS `tb_item_permissao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_permissao` (
  `idItemPermissao` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) NOT NULL,
  `idPermissao` bigint(20) unsigned NOT NULL,
  `status` varchar(45) NOT NULL DEFAULT 'false',
  `modulo` varchar(45) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idItemPermissao`),
  KEY `FK_tb_item_permissao_1` (`idUsuario`),
  KEY `FK_tb_item_permissao_2` (`idPermissao`),
  CONSTRAINT `FK_tb_item_permissao_1` FOREIGN KEY (`idUsuario`) REFERENCES `tb_usuario` (`codigo`),
  CONSTRAINT `FK_tb_item_permissao_2` FOREIGN KEY (`idPermissao`) REFERENCES `tb_permissao` (`idPermissao`)
) ENGINE=InnoDB AUTO_INCREMENT=8780 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_permissao`
--

LOCK TABLES `tb_item_permissao` WRITE;
/*!40000 ALTER TABLE `tb_item_permissao` DISABLE KEYS */;
INSERT INTO `tb_item_permissao` VALUES (3716,15,49,'true','2'),(3717,15,61,'true','2'),(3718,15,60,'true','2'),(3719,15,59,'true','2'),(3720,15,58,'true','2'),(3721,15,57,'true','2'),(3722,15,54,'true','2'),(3723,15,56,'true','2'),(3724,15,55,'true','2'),(3725,15,62,'true','2'),(3726,15,63,'true','2'),(3727,15,77,'true','2'),(3728,15,76,'true','2'),(3729,15,75,'true','2'),(3730,15,74,'true','2'),(3731,15,73,'true','2'),(3732,15,72,'true','2'),(3733,15,71,'true','2'),(3734,15,70,'true','2'),(3735,15,69,'true','2'),(3736,15,68,'true','2'),(3737,15,67,'true','2'),(3738,15,66,'true','2'),(3739,15,65,'true','2'),(3740,15,64,'true','2'),(3741,15,73,'true','2'),(3742,15,62,'true','2'),(8701,15,1,'true','1'),(8702,15,81,'true','1'),(8703,15,82,'true','1'),(8704,15,83,'true','1'),(8705,15,84,'true','1'),(8706,15,135,'true','1'),(8707,15,17,'true','1'),(8708,15,2,'true','1'),(8709,15,85,'true','1'),(8710,15,3,'true','1'),(8711,15,17,'true','1'),(8712,15,4,'true','1'),(8713,15,80,'true','1'),(8714,15,37,'true','1'),(8715,15,142,'true','1'),(8716,15,44,'true','1'),(8717,15,107,'true','1'),(8718,15,29,'true','1'),(8719,15,42,'true','1'),(8720,15,108,'true','1'),(8721,15,109,'true','1'),(8722,15,112,'true','1'),(8723,15,113,'true','1'),(8724,15,114,'true','1'),(8725,15,115,'true','1'),(8726,15,116,'true','1'),(8727,15,117,'true','1'),(8728,15,111,'true','1'),(8729,15,118,'true','1'),(8730,15,120,'true','1'),(8731,15,121,'true','1'),(8732,15,128,'true','1'),(8733,15,136,'true','1'),(8734,15,110,'true','1'),(8735,15,9,'true','1'),(8736,15,79,'true','1'),(8737,15,45,'true','1'),(8738,15,12,'true','1'),(8739,15,13,'true','1'),(8740,15,14,'true','1'),(8741,15,15,'true','1'),(8742,15,19,'true','1'),(8743,15,21,'true','1'),(8744,15,28,'true','1'),(8745,15,31,'true','1'),(8746,15,32,'true','1'),(8747,15,46,'true','1'),(8748,15,47,'true','1'),(8749,15,97,'true','1'),(8750,15,122,'true','1'),(8751,15,139,'true','1'),(8752,15,89,'true','1'),(8753,15,88,'true','1'),(8754,15,94,'true','1'),(8755,15,93,'true','1'),(8756,15,92,'true','1'),(8757,15,96,'true','1'),(8758,15,98,'true','1'),(8759,15,127,'true','1'),(8760,15,100,'true','1'),(8761,15,101,'true','1'),(8762,15,102,'true','1'),(8763,15,103,'true','1'),(8764,15,143,'true','1'),(8765,15,104,'true','1'),(8766,15,105,'true','1'),(8767,15,106,'true','1'),(8768,15,122,'true','1'),(8769,15,123,'true','1'),(8770,15,124,'true','1'),(8771,15,126,'true','1'),(8772,15,125,'true','1'),(8773,15,129,'true','1'),(8774,15,130,'true','1'),(8775,15,134,'true','1'),(8776,15,131,'true','1'),(8777,15,137,'true','1'),(8778,15,138,'true','1'),(8779,15,140,'true','1');
/*!40000 ALTER TABLE `tb_item_permissao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_pro_forma`
--

DROP TABLE IF EXISTS `tb_item_pro_forma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_pro_forma` (
  `pk_item_pro_forma` int(11) NOT NULL AUTO_INCREMENT,
  `qtd` int(11) DEFAULT NULL,
  `fk_produto` int(11) NOT NULL,
  `fk_pro_forma` int(11) NOT NULL,
  `fk_preco` int(10) unsigned NOT NULL,
  PRIMARY KEY (`pk_item_pro_forma`),
  KEY `fk_tb_item_pro_forma_tb_produto1_idx` (`fk_produto`),
  KEY `fk_tb_item_pro_forma_tb_pro_forma1_idx` (`fk_pro_forma`),
  KEY `fk_tb_item_pro_forma_tb_preco1_idx` (`fk_preco`),
  CONSTRAINT `fk_tb_item_pro_forma_tb_preco1` FOREIGN KEY (`fk_preco`) REFERENCES `tb_preco` (`pk_preco`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_item_pro_forma_tb_produto1` FOREIGN KEY (`fk_produto`) REFERENCES `tb_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_item_pro_forma_tb_pro_forma1` FOREIGN KEY (`fk_pro_forma`) REFERENCES `tb_pro_forma` (`pk_pro_forma`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_pro_forma`
--

LOCK TABLES `tb_item_pro_forma` WRITE;
/*!40000 ALTER TABLE `tb_item_pro_forma` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_item_pro_forma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_saida_vasilhame`
--

DROP TABLE IF EXISTS `tb_item_saida_vasilhame`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_saida_vasilhame` (
  `pk_item_saida_vasilhame` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fk_vasihame` int(10) unsigned DEFAULT NULL,
  `fk_saida_vazilhame` int(10) unsigned DEFAULT NULL,
  `qtd` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`pk_item_saida_vasilhame`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_saida_vasilhame`
--

LOCK TABLES `tb_item_saida_vasilhame` WRITE;
/*!40000 ALTER TABLE `tb_item_saida_vasilhame` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_item_saida_vasilhame` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_saidas`
--

DROP TABLE IF EXISTS `tb_item_saidas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_saidas` (
  `codigo` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fk_saidas_produtos` int(10) unsigned NOT NULL,
  `fk_produtos` int(11) NOT NULL,
  `quantidade` int(10) unsigned NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `fk_saidas_prod_idx` (`fk_saidas_produtos`),
  KEY `fk_produtos_idx` (`fk_produtos`),
  CONSTRAINT `fk_produtos` FOREIGN KEY (`fk_produtos`) REFERENCES `tb_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_saidas_prod` FOREIGN KEY (`fk_saidas_produtos`) REFERENCES `tb_saidas_produtos` (`pk_saidas_produtos`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_saidas`
--

LOCK TABLES `tb_item_saidas` WRITE;
/*!40000 ALTER TABLE `tb_item_saidas` DISABLE KEYS */;
INSERT INTO `tb_item_saidas` VALUES (1,1,4,2);
/*!40000 ALTER TABLE `tb_item_saidas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_subsidios_funcionario`
--

DROP TABLE IF EXISTS `tb_item_subsidios_funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_subsidios_funcionario` (
  `idItemSubsidiosFuncionario` int(11) NOT NULL AUTO_INCREMENT,
  `valor_subsidio` double DEFAULT NULL,
  `idSubsidioFK` int(11) DEFAULT NULL,
  `idFuncionarioFK` int(11) DEFAULT NULL,
  PRIMARY KEY (`idItemSubsidiosFuncionario`),
  KEY `FkSubsidios` (`idSubsidioFK`),
  KEY `FkFuncionarios` (`idFuncionarioFK`),
  CONSTRAINT `FkFuncionarios` FOREIGN KEY (`idFuncionarioFK`) REFERENCES `tb_funcionario` (`idFuncionario`),
  CONSTRAINT `FkSubsidios` FOREIGN KEY (`idSubsidioFK`) REFERENCES `tb_subsidios` (`idSubsidios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_subsidios_funcionario`
--

LOCK TABLES `tb_item_subsidios_funcionario` WRITE;
/*!40000 ALTER TABLE `tb_item_subsidios_funcionario` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_item_subsidios_funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_venda`
--

DROP TABLE IF EXISTS `tb_item_venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_item_venda` (
  `codigo` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `quantidade` double unsigned NOT NULL,
  `valor_iva` double DEFAULT NULL,
  `motivo_isensao` varchar(150) DEFAULT NULL,
  `desconto` double DEFAULT NULL,
  `total` decimal(30,2) unsigned NOT NULL,
  `codigo_venda` int(11) NOT NULL,
  `codigo_produto` int(11) NOT NULL,
  `fk_preco` int(10) unsigned NOT NULL DEFAULT '1',
  `codigo_isensao` varchar(45) DEFAULT NULL,
  `fk_lugares` int(10) unsigned DEFAULT '1',
  `fk_mesas` int(10) unsigned DEFAULT '1',
  `data_servico` date DEFAULT NULL,
  `valor_retencao` double DEFAULT NULL,
  `obs` varchar(500) DEFAULT NULL,
  `data_entrega` datetime DEFAULT NULL,
  `status_entrega` tinyint(1) DEFAULT '0',
  `posicao` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`codigo`),
  KEY `fk_tb_venda_has_tb_produto_tb_produto1` (`codigo_produto`),
  KEY `fk_tb_venda_has_tb_produto_tb_venda1` (`codigo_venda`),
  KEY `FK_tb_item_venda_preco` (`fk_preco`),
  KEY `FK_tb_item_venda_4_fk_lugares` (`fk_lugares`),
  KEY `FK_tb_item_venda_5_fk_mesas` (`fk_mesas`),
  CONSTRAINT `FK_tb_item_venda_4_fk_lugares` FOREIGN KEY (`fk_lugares`) REFERENCES `tb_lugares` (`pk_lugares`),
  CONSTRAINT `FK_tb_item_venda_5_fk_mesas` FOREIGN KEY (`fk_mesas`) REFERENCES `tb_mesas` (`pk_mesas`),
  CONSTRAINT `FK_tb_item_venda_preco` FOREIGN KEY (`fk_preco`) REFERENCES `tb_preco` (`pk_preco`),
  CONSTRAINT `fk_tb_venda_has_tb_produto_tb_produto1` FOREIGN KEY (`codigo_produto`) REFERENCES `tb_produto` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_tb_venda_has_tb_produto_tb_venda1` FOREIGN KEY (`codigo_venda`) REFERENCES `tb_venda` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_venda`
--

LOCK TABLES `tb_item_venda` WRITE;
/*!40000 ALTER TABLE `tb_item_venda` DISABLE KEYS */;
INSERT INTO `tb_item_venda` VALUES (1,1,14,'',0,'6840.00',1,13,343,'',1,16,'2025-09-10',0,NULL,NULL,0,0),(2,1,14,'',0,'6840.00',2,13,343,'',1,16,'2025-09-10',0,NULL,NULL,0,0),(3,1,14,'',0,'5130.00',3,10,341,'',1,16,'2025-09-10',0,NULL,NULL,0,0),(4,1,14,'',0,'1824.00',4,5,331,'',1,16,'2025-09-10',0,NULL,NULL,0,0),(5,1,14,'',0,'6384.00',5,4,319,'',1,16,'2025-09-10',0,NULL,NULL,0,0),(6,10,14,'',0,'7410.00',6,3,350,'',1,16,'2025-09-10',0,NULL,NULL,0,0),(7,1,14,'',0,'741.00',7,3,349,'',1,16,'2025-09-10',0,NULL,NULL,0,0),(8,1,14,'',0,'6384.00',8,4,319,'',1,16,'2025-09-10',0,NULL,NULL,0,0),(9,5,14,'',0,'31920.00',9,4,355,'',1,16,'2025-09-10',0,NULL,NULL,0,0),(10,1,14,'',0,'5130.00',10,10,361,'',1,16,'2025-09-10',0,NULL,NULL,0,0);
/*!40000 ALTER TABLE `tb_item_venda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_local`
--

DROP TABLE IF EXISTS `tb_local`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_local` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_local`
--

LOCK TABLES `tb_local` WRITE;
/*!40000 ALTER TABLE `tb_local` DISABLE KEYS */;
INSERT INTO `tb_local` VALUES (1,'LOJA');
/*!40000 ALTER TABLE `tb_local` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_lugares`
--

DROP TABLE IF EXISTS `tb_lugares`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_lugares` (
  `pk_lugares` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) NOT NULL,
  PRIMARY KEY (`pk_lugares`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_lugares`
--

LOCK TABLES `tb_lugares` WRITE;
/*!40000 ALTER TABLE `tb_lugares` DISABLE KEYS */;
INSERT INTO `tb_lugares` VALUES (1,'LUGAR 1'),(2,'LUGAR 2'),(3,'LUGAR 3'),(4,'LUGAR 4'),(5,'LUGAR 5'),(6,'LUGAR 6'),(7,'LUGAR 7'),(8,'LUGAR 8'),(9,'LUGAR 9'),(10,'LUGAR 10');
/*!40000 ALTER TABLE `tb_lugares` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_mes_rh`
--

DROP TABLE IF EXISTS `tb_mes_rh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_mes_rh` (
  `pk_mes_rh` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pk_mes_rh`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_mes_rh`
--

LOCK TABLES `tb_mes_rh` WRITE;
/*!40000 ALTER TABLE `tb_mes_rh` DISABLE KEYS */;
INSERT INTO `tb_mes_rh` VALUES (1,'Janeiro'),(2,'Fevereiro'),(3,'Março'),(4,'Abril'),(5,'Maio'),(6,'Junho'),(7,'Julho'),(8,'Agosto'),(9,'Setembro'),(10,'Outubro'),(11,'Novembro'),(12,'Dezembro');
/*!40000 ALTER TABLE `tb_mes_rh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_mesas`
--

DROP TABLE IF EXISTS `tb_mesas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_mesas` (
  `pk_mesas` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `designacao` varchar(45) NOT NULL,
  PRIMARY KEY (`pk_mesas`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_mesas`
--

LOCK TABLES `tb_mesas` WRITE;
/*!40000 ALTER TABLE `tb_mesas` DISABLE KEYS */;
INSERT INTO `tb_mesas` VALUES (1,'MESA 1'),(2,'MESA 2'),(3,'MESA 3'),(4,'MESA 4'),(5,'MESA 5'),(6,'MESA 6'),(7,'MESA 7'),(8,'MESA 8'),(9,'MESA 9'),(10,'MESA 10'),(11,'MESA 11'),(12,'MESA 12'),(13,'MESA 13'),(14,'MESA 14'),(15,'MESA 15'),(16,'MESA 16'),(17,'MESA 17'),(18,'MESA 18'),(19,'MESA 19'),(20,'MESA 20'),(21,'MESA 21'),(22,'MESA 22'),(23,'MESA 23'),(24,'MESA 24'),(25,'MESA 25'),(26,'MESA 26'),(27,'MESA 27'),(28,'MESA 28'),(29,'MESA 29'),(30,'MESA 30'),(31,'MESA 31'),(32,'MESA 32'),(33,'MESA 33'),(34,'MESA 34'),(35,'MESA 35'),(36,'MESA 36'),(37,'MESA 37'),(38,'MESA 38'),(39,'MESA 39'),(40,'MESA 40');
/*!40000 ALTER TABLE `tb_mesas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_municipio`
--

DROP TABLE IF EXISTS `tb_municipio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_municipio` (
  `idMunicipio` int(11) NOT NULL AUTO_INCREMENT,
  `descrisao` varchar(45) DEFAULT NULL,
  `idProvincia` int(11) DEFAULT NULL,
  PRIMARY KEY (`idMunicipio`),
  KEY `FkProvincia` (`idProvincia`),
  CONSTRAINT `FkProvincia` FOREIGN KEY (`idProvincia`) REFERENCES `tb_provincia` (`idProvincia`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_municipio`
--

LOCK TABLES `tb_municipio` WRITE;
/*!40000 ALTER TABLE `tb_municipio` DISABLE KEYS */;
INSERT INTO `tb_municipio` VALUES (1,'BELLAS',1),(2,'CACUACO',1),(3,'LUANDA',1);
/*!40000 ALTER TABLE `tb_municipio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_operacao_sistema`
--

DROP TABLE IF EXISTS `tb_operacao_sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_operacao_sistema` (
  `pk_operacao_sistema` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_abertura` date DEFAULT NULL,
  `hora_abertura` time DEFAULT NULL,
  `data_feicho` date DEFAULT NULL,
  `hora_feicho` time DEFAULT NULL,
  `fk_usuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`pk_operacao_sistema`),
  KEY `tb_operacao_usuario` (`fk_usuario`),
  CONSTRAINT `tb_operacao_usuario` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_operacao_sistema`
--

LOCK TABLES `tb_operacao_sistema` WRITE;
/*!40000 ALTER TABLE `tb_operacao_sistema` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_operacao_sistema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_pagamento_credito`
--

DROP TABLE IF EXISTS `tb_pagamento_credito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_pagamento_credito` (
  `codigo` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `data_pagamento` double NOT NULL,
  `valor` double NOT NULL,
  `codigo_venda` int(11) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_tb_pagamento_credito_1` (`codigo_venda`),
  CONSTRAINT `FK_tb_pagamento_credito_1` FOREIGN KEY (`codigo_venda`) REFERENCES `tb_venda` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_pagamento_credito`
--

LOCK TABLES `tb_pagamento_credito` WRITE;
/*!40000 ALTER TABLE `tb_pagamento_credito` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_pagamento_credito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_pais`
--

DROP TABLE IF EXISTS `tb_pais`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_pais` (
  `idPais` int(11) NOT NULL AUTO_INCREMENT,
  `descrisao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idPais`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_pais`
--

LOCK TABLES `tb_pais` WRITE;
/*!40000 ALTER TABLE `tb_pais` DISABLE KEYS */;
INSERT INTO `tb_pais` VALUES (1,'ANGOLA');
/*!40000 ALTER TABLE `tb_pais` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_parametros`
--

DROP TABLE IF EXISTS `tb_parametros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_parametros` (
  `codigo` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Designacao` varchar(45) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_parametros`
--

LOCK TABLES `tb_parametros` WRITE;
/*!40000 ALTER TABLE `tb_parametros` DISABLE KEYS */;
INSERT INTO `tb_parametros` VALUES (1,'localhost');
/*!40000 ALTER TABLE `tb_parametros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_pedido`
--

DROP TABLE IF EXISTS `tb_pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_pedido` (
  `pk_pedido` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `data_pedido` date NOT NULL,
  `hora_pedido` time NOT NULL,
  `fk_mesas` int(11) unsigned DEFAULT '1',
  `status_pedido` tinyint(1) NOT NULL DEFAULT '0',
  `facturado` enum('Espera','Processado') DEFAULT 'Espera',
  `deposito` double DEFAULT '0',
  `valor_em_falta` double DEFAULT '0',
  PRIMARY KEY (`pk_pedido`),
  KEY `Index_2_pk_mesas` (`fk_mesas`),
  CONSTRAINT `FK_tb_pedido_1_pk_mesas` FOREIGN KEY (`fk_mesas`) REFERENCES `tb_mesas` (`pk_mesas`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_pedido`
--

LOCK TABLES `tb_pedido` WRITE;
/*!40000 ALTER TABLE `tb_pedido` DISABLE KEYS */;
INSERT INTO `tb_pedido` VALUES (1,'2025-09-10','02:15:10',10,0,'Espera',0,0),(2,'2025-09-11','05:27:55',1,0,'Espera',0,0);
/*!40000 ALTER TABLE `tb_pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_permissao`
--

DROP TABLE IF EXISTS `tb_permissao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_permissao` (
  `idPermissao` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `descricao` varchar(100) NOT NULL,
  PRIMARY KEY (`idPermissao`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_permissao`
--

LOCK TABLES `tb_permissao` WRITE;
/*!40000 ALTER TABLE `tb_permissao` DISABLE KEYS */;
INSERT INTO `tb_permissao` VALUES (1,'Venda'),(2,'Listar Usuarios'),(3,'Inventario'),(4,'Relatorio de Vendas por Intervalo de Datas'),(5,'Listar Venda Credito'),(6,'Registro de Entradas no Stock'),(7,'Relatorio Diario'),(8,'Listagens de Todos os Produtos'),(9,'Cadastrar Usuario'),(10,'Anulamento de Factura'),(11,'Stock'),(12,'Produto'),(13,'Fornecedor'),(14,'Categoria'),(15,'Reemprimir Factura'),(16,'Pagamento Credito'),(17,'Entrada de Produtos'),(18,'Listar Venda Por Usuario'),(19,'Permissao'),(20,'Produtos a Actualizar'),(21,'Percentagem de Desconto'),(22,'Cadastrar Cliente'),(23,'Efectuar Encomenda'),(24,'Listagens de Encomendas por Cliente'),(25,'Listagens de Todas Encomendas'),(26,'Adicioanr Saldo'),(27,'Eliminar Encomenda'),(28,'Dados da Empresa'),(29,'Listagens de Todos os Produtos com Desconto'),(30,'Listagem de Vasilhames'),(31,'Cadastro de Cliente'),(32,'Vasilhame'),(33,'Imprimir Credêncial'),(34,'Anulamento de Entrada de Produtos'),(35,'Reciclagem'),(36,'Listar Vasilhames'),(37,'Relatorio de Vendas Por Usuario e Data'),(38,'Relatorio de Venda a Credito'),(39,'Relatorio de Quantidade de Produtos Vendidos'),(40,'Listar Todos os Produtos'),(41,'Listagem de Todos os Produtos com Desconto'),(42,'Compra Por Fazer'),(43,'Relatorio de Pre-Forma'),(44,'Relatorio de Quatidades de Produtos Vendidos'),(45,'Cadastro de Armazem'),(46,'Precos por Percentagem'),(47,'Imprimir Precos'),(48,'Reemprimir Pro-Forma'),(49,'Saldos de Contas'),(50,'Reimprimir Adiantamento de Caixa'),(51,'Reimprimir Saida de Caixa'),(52,'Reimprimir Entrada de Caixa'),(53,'Mapa do Balanco'),(54,'Extrato de Movimentos Bancarios'),(55,'Relatorios de Saidas do Caixa'),(56,'Relatorios de Entradas no Caixa'),(57,'Transferencias Bancarias'),(58,'Levantamento Bancario'),(59,'Deposito Bancario'),(60,'Saida Caixa'),(61,'Entrada Caixa'),(62,'Cadastro Usuario'),(63,'Permissoes de Usuarios'),(64,'Reimprimir Adiantamento'),(65,'Funcionarios Desactivos'),(66,'Funcionarios Activos'),(67,'Mapa de Faltas'),(68,'Folha de Salario'),(69,'Salario dos Funcionarios'),(70,'Contas dos Funcionarios'),(71,'Processamento de Salario'),(72,'Conta do Funcionario'),(73,'Definicao de Salario'),(74,'Adiantamento de Salario'),(75,'Marcacao de Faltas'),(76,'Subsidios'),(77,'Funcionario'),(78,'Definicao de Salario'),(79,'Acerto Stock'),(80,'Relatorio Diario de Todas Vendas em Tempo Real'),(81,'Converter Proformas em Facturas'),(82,'Processar Recibos de Factura'),(83,'Notas de Credito e Debito'),(84,'Gerar SAFT de Vendas'),(85,'Relatorio por Fornecedor'),(86,'Promoção'),(87,'Vendas Detalhadas'),(88,'Compras'),(89,'Encomendas'),(90,'Autorizacao Compras'),(91,'Solicitacao Compras'),(92,'Modelo'),(93,'Marca'),(94,'SubFamilia'),(95,'Familia'),(96,'Grupo'),(97,'Gestao de Turno'),(98,'Unidades'),(99,'Estornos'),(100,'Saidas Produtos'),(101,'Gestao de Mesas'),(102,'Reimprimir Saidas de Produtos'),(103,'Relatorios Saidas de Produtos por Datas'),(104,'Mapa de Existencia'),(105,'Anulamentos Saidas Produtos'),(106,'Gavetas/Prateleiras'),(107,'Relatorio de Todos Produtos'),(108,'Vendas Detalhadas por Clientes'),(109,'Vendas Detalhadas por Usuarios'),(110,'Activar Desconto'),(111,'Reimprimir Compra'),(112,'Relatorio de Vendas por Formas de Pagamentos'),(113,'Relatorio de Todos Servicos'),(114,'Relatorio de Notas de Creditos'),(115,'Relatorio de Acerto de Stock'),(116,'Historico de Bonus da Empresa'),(117,'Relatorio de Compras por Fornecedores'),(118,'Relatorio de Vendas por Cliente e Data'),(119,'Vendas Detalhadas por Cliente'),(120,'Notas Credito Compra'),(121,'Relatorio de Quantidades de Produtos Comprados'),(122,'Transferencia de Armazem'),(123,'Front Office'),(124,'Nota de Levantamento'),(125,'Lista de Clientes'),(126,'Reimprimir Nota Credito'),(127,'Quebras'),(128,'Reactivar Produtos'),(129,'Configuração Sistema'),(130,'Abertura de Caixa'),(131,'Fecho de Caixa'),(132,'Quebras'),(133,'Reactivar Produtos'),(134,'Relatorio Detalhado de Quantidades Vendidos'),(135,'Gerar SAFT de Compras'),(136,'Relatorio Transferencias Armazem'),(137,'Consultas'),(138,'Relatorio Quebras'),(139,'Relatorio Mensal'),(140,'Pedidos Cancelados'),(141,'Stock Consultas'),(142,'Relatorio de Entradas'),(143,'Cancelamento de Entrada');
/*!40000 ALTER TABLE `tb_permissao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_preco`
--

DROP TABLE IF EXISTS `tb_preco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_preco` (
  `pk_preco` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `hora` time DEFAULT NULL,
  `preco_compra` decimal(30,6) NOT NULL,
  `percentagem_ganho` decimal(10,2) NOT NULL,
  `fk_produto` int(11) NOT NULL DEFAULT '1',
  `fk_usuario` int(11) NOT NULL DEFAULT '1',
  `preco_medio` decimal(30,6) DEFAULT NULL,
  `preco_venda` decimal(30,6) NOT NULL,
  `qtd_baixo` int(10) unsigned NOT NULL,
  `qtd_alto` int(10) unsigned NOT NULL,
  `preco_anterior` double DEFAULT NULL,
  `retalho` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`pk_preco`),
  KEY `FK_tb_preco_produto` (`fk_produto`),
  KEY `FK_tb_preco_usuario` (`fk_usuario`),
  CONSTRAINT `FK_tb_preco_produto` FOREIGN KEY (`fk_produto`) REFERENCES `tb_produto` (`codigo`),
  CONSTRAINT `FK_tb_preco_usuario` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=423 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_preco`
--

LOCK TABLES `tb_preco` WRITE;
/*!40000 ALTER TABLE `tb_preco` DISABLE KEYS */;
INSERT INTO `tb_preco` VALUES (1,'2025-09-07',NULL,'250.000000','0.00',3,15,NULL,'350.000000',0,5,0,1),(2,'2025-09-07',NULL,'250.000000','0.00',3,15,NULL,'350.000000',6,214748364,0,1),(3,'2025-09-07',NULL,'0.000000','0.00',4,15,NULL,'0.000000',0,5,0,1),(4,'2025-09-07',NULL,'0.000000','0.00',4,15,NULL,'0.000000',6,214748364,0,1),(5,'2025-09-07',NULL,'0.000000','0.00',439,15,NULL,'0.000000',0,5,0,1),(6,'2025-09-07',NULL,'0.000000','0.00',439,15,NULL,'0.000000',6,214748364,0,1),(7,'2025-09-07',NULL,'500.000000','0.00',3,15,NULL,'650.000000',0,5,0,1),(8,'2025-09-07',NULL,'500.000000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(9,'2025-09-07',NULL,'0.000000','0.00',4,15,NULL,'0.000000',0,5,0,1),(10,'2025-09-07',NULL,'0.000000','0.00',4,15,NULL,'0.000000',6,214748364,0,1),(11,'2025-09-07',NULL,'0.000000','0.00',439,15,NULL,'0.000000',0,5,0,1),(12,'2025-09-07',NULL,'0.000000','0.00',439,15,NULL,'0.000000',6,214748364,0,1),(13,'2025-09-07',NULL,'100.000000','0.00',3,15,NULL,'650.000000',0,5,0,1),(14,'2025-09-07',NULL,'100.000000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(15,'2025-09-07',NULL,'0.000000','0.00',4,15,NULL,'0.000000',0,5,0,1),(16,'2025-09-07',NULL,'0.000000','0.00',4,15,NULL,'0.000000',6,214748364,0,1),(17,'2025-09-07',NULL,'0.000000','0.00',439,15,NULL,'0.000000',0,5,0,1),(18,'2025-09-07',NULL,'0.000000','0.00',439,15,NULL,'0.000000',6,214748364,0,1),(19,'2025-09-08',NULL,'2500.000000','0.00',26,15,NULL,'1750.000000',0,5,0,1),(20,'2025-09-08',NULL,'2500.000000','0.00',26,15,NULL,'1750.000000',6,214748364,0,1),(21,'2025-09-08',NULL,'0.000000','0.00',27,15,NULL,'0.000000',0,5,0,1),(22,'2025-09-08',NULL,'0.000000','0.00',27,15,NULL,'0.000000',6,214748364,0,1),(23,'2025-09-08',NULL,'750.600000','0.00',3,15,NULL,'650.000000',0,5,0,1),(24,'2025-09-08',NULL,'750.600000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(25,'2025-09-08',NULL,'0.000000','0.00',4,15,NULL,'0.000000',0,5,0,1),(26,'2025-09-08',NULL,'0.000000','0.00',4,15,NULL,'0.000000',6,214748364,0,1),(27,'2025-09-08',NULL,'0.000000','0.00',439,15,NULL,'0.000000',0,5,0,1),(28,'2025-09-08',NULL,'0.000000','0.00',439,15,NULL,'0.000000',6,214748364,0,1),(29,'2025-09-08',NULL,'750.600000','0.00',3,15,NULL,'650.000000',0,5,0,1),(30,'2025-09-08',NULL,'750.600000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(31,'2025-09-08',NULL,'0.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(32,'2025-09-08',NULL,'0.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(33,'2025-09-08',NULL,'0.000000','0.00',439,15,NULL,'0.000000',0,5,0,1),(34,'2025-09-08',NULL,'0.000000','0.00',439,15,NULL,'0.000000',6,214748364,0,1),(35,'2025-09-08',NULL,'2500.000000','0.00',26,15,NULL,'1750.000000',0,5,0,1),(36,'2025-09-08',NULL,'2500.000000','0.00',26,15,NULL,'1750.000000',6,214748364,0,1),(37,'2025-09-08',NULL,'7500.000000','0.00',27,15,NULL,'0.000000',0,5,0,1),(38,'2025-09-08',NULL,'7500.000000','0.00',27,15,NULL,'0.000000',6,214748364,0,1),(39,'2025-09-08',NULL,'17500.000000','0.00',5,15,NULL,'0.000000',0,5,0,1),(40,'2025-09-08',NULL,'17500.000000','0.00',5,15,NULL,'0.000000',6,214748364,0,1),(41,'2025-09-08',NULL,'0.000000','0.00',6,15,NULL,'0.000000',0,5,0,1),(42,'2025-09-08',NULL,'0.000000','0.00',6,15,NULL,'0.000000',6,214748364,0,1),(43,'2025-09-08',NULL,'0.000000','0.00',7,15,NULL,'0.000000',0,5,0,1),(44,'2025-09-08',NULL,'0.000000','0.00',7,15,NULL,'0.000000',6,214748364,0,1),(45,'2025-09-08',NULL,'0.000000','0.00',8,15,NULL,'0.000000',0,5,0,1),(46,'2025-09-08',NULL,'0.000000','0.00',8,15,NULL,'0.000000',6,214748364,0,1),(47,'2025-09-08',NULL,'0.000000','0.00',9,15,NULL,'0.000000',0,5,0,1),(48,'2025-09-08',NULL,'0.000000','0.00',9,15,NULL,'0.000000',6,214748364,0,1),(49,'2025-09-08',NULL,'0.000000','0.00',10,15,NULL,'0.000000',0,5,0,1),(50,'2025-09-08',NULL,'0.000000','0.00',10,15,NULL,'0.000000',6,214748364,0,1),(51,'2025-09-08',NULL,'750.600000','0.00',3,15,NULL,'650.000000',0,5,0,1),(52,'2025-09-08',NULL,'750.600000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(53,'2025-09-08',NULL,'7600.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(54,'2025-09-08',NULL,'7600.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(55,'2025-09-08',NULL,'6500.334500','0.00',439,15,NULL,'8500.000000',0,5,0,1),(56,'2025-09-08',NULL,'6500.334500','0.00',439,15,NULL,'8500.000000',6,214748364,0,1),(57,'2025-09-08',NULL,'750.600000','0.00',3,15,NULL,'650.000000',0,5,0,1),(58,'2025-09-08',NULL,'750.600000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(59,'2025-09-08',NULL,'7600.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(60,'2025-09-08',NULL,'7600.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(61,'2025-09-08',NULL,'800.000000','0.00',439,15,NULL,'1315.789473',0,5,0,1),(62,'2025-09-08',NULL,'800.000000','0.00',439,15,NULL,'1315.789473',6,214748364,0,1),(63,'2025-09-08',NULL,'1320.000000','0.00',28,15,NULL,'1500.000000',0,5,0,1),(64,'2025-09-08',NULL,'1320.000000','0.00',28,15,NULL,'1500.000000',6,214748364,0,1),(65,'2025-09-08',NULL,'0.000000','0.00',29,15,NULL,'0.000000',0,5,0,1),(66,'2025-09-08',NULL,'0.000000','0.00',29,15,NULL,'0.000000',6,214748364,0,1),(67,'2025-09-08',NULL,'0.000000','0.00',30,15,NULL,'0.000000',0,5,0,1),(68,'2025-09-08',NULL,'0.000000','0.00',30,15,NULL,'0.000000',6,214748364,0,1),(69,'2025-09-08',NULL,'0.000000','0.00',31,15,NULL,'0.000000',0,5,0,1),(70,'2025-09-08',NULL,'0.000000','0.00',31,15,NULL,'0.000000',6,214748364,0,1),(71,'2025-09-08',NULL,'0.000000','0.00',32,15,NULL,'0.000000',0,5,0,1),(72,'2025-09-08',NULL,'0.000000','0.00',32,15,NULL,'0.000000',6,214748364,0,1),(73,'2025-09-08',NULL,'1320.000000','0.00',28,15,NULL,'1500.000000',0,5,0,1),(74,'2025-09-08',NULL,'1320.000000','0.00',28,15,NULL,'1500.000000',6,214748364,0,1),(75,'2025-09-08',NULL,'3400.000000','0.00',29,15,NULL,'7600.000000',0,5,0,1),(76,'2025-09-08',NULL,'3400.000000','0.00',29,15,NULL,'7600.000000',6,214748364,0,1),(77,'2025-09-08',NULL,'0.000000','0.00',30,15,NULL,'0.000000',0,5,0,1),(78,'2025-09-08',NULL,'0.000000','0.00',30,15,NULL,'0.000000',6,214748364,0,1),(79,'2025-09-08',NULL,'0.000000','0.00',31,15,NULL,'0.000000',0,5,0,1),(80,'2025-09-08',NULL,'0.000000','0.00',31,15,NULL,'0.000000',6,214748364,0,1),(81,'2025-09-08',NULL,'0.000000','0.00',32,15,NULL,'0.000000',0,5,0,1),(82,'2025-09-08',NULL,'0.000000','0.00',32,15,NULL,'0.000000',6,214748364,0,1),(83,'2025-09-08',NULL,'7600.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(84,'2025-09-08',NULL,'7600.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(85,'2025-09-08',NULL,'17500.000000','0.00',5,15,NULL,'20000.000000',0,5,0,1),(86,'2025-09-08',NULL,'17500.000000','0.00',5,15,NULL,'20000.000000',6,214748364,0,1),(87,'2025-09-08',NULL,'0.000000','0.00',6,15,NULL,'0.000000',0,5,0,1),(88,'2025-09-08',NULL,'0.000000','0.00',6,15,NULL,'0.000000',6,214748364,0,1),(89,'2025-09-08',NULL,'0.000000','0.00',7,15,NULL,'0.000000',0,5,0,1),(90,'2025-09-08',NULL,'0.000000','0.00',7,15,NULL,'0.000000',6,214748364,0,1),(91,'2025-09-08',NULL,'0.000000','0.00',8,15,NULL,'0.000000',0,5,0,1),(92,'2025-09-08',NULL,'0.000000','0.00',8,15,NULL,'0.000000',6,214748364,0,1),(93,'2025-09-08',NULL,'0.000000','0.00',9,15,NULL,'0.000000',0,5,0,1),(94,'2025-09-08',NULL,'0.000000','0.00',9,15,NULL,'0.000000',6,214748364,0,1),(95,'2025-09-08',NULL,'0.000000','0.00',10,15,NULL,'0.000000',0,5,0,1),(96,'2025-09-08',NULL,'0.000000','0.00',10,15,NULL,'0.000000',6,214748364,0,1),(97,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(98,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(99,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',0,5,0,1),(100,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',6,214748364,0,1),(101,'2025-09-08',NULL,'0.000000','0.00',22,15,NULL,'0.000000',0,5,0,1),(102,'2025-09-08',NULL,'0.000000','0.00',22,15,NULL,'0.000000',6,214748364,0,1),(103,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',0,5,0,1),(104,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',6,214748364,0,1),(105,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',0,5,0,1),(106,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',6,214748364,0,1),(107,'2025-09-08',NULL,'0.000000','0.00',25,15,NULL,'0.000000',0,5,0,1),(108,'2025-09-08',NULL,'0.000000','0.00',25,15,NULL,'0.000000',6,214748364,0,1),(109,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(110,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(111,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',0,5,0,1),(112,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',6,214748364,0,1),(113,'2025-09-08',NULL,'0.000000','0.00',22,15,NULL,'0.000000',0,5,0,1),(114,'2025-09-08',NULL,'0.000000','0.00',22,15,NULL,'0.000000',6,214748364,0,1),(115,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',0,5,0,1),(116,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',6,214748364,0,1),(117,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',0,5,0,1),(118,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',6,214748364,0,1),(119,'2025-09-08',NULL,'0.000000','0.00',25,15,NULL,'0.000000',0,5,0,1),(120,'2025-09-08',NULL,'0.000000','0.00',25,15,NULL,'0.000000',6,214748364,0,1),(121,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(122,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(123,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',0,5,0,1),(124,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',6,214748364,0,1),(125,'2025-09-08',NULL,'2590.999000','0.00',22,15,NULL,'65000.000000',0,5,0,1),(126,'2025-09-08',NULL,'2590.999000','0.00',22,15,NULL,'65000.000000',6,214748364,0,1),(127,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',0,5,0,1),(128,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',6,214748364,0,1),(129,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',0,5,0,1),(130,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',6,214748364,0,1),(131,'2025-09-08',NULL,'0.000000','0.00',25,15,NULL,'0.000000',0,5,0,1),(132,'2025-09-08',NULL,'0.000000','0.00',25,15,NULL,'0.000000',6,214748364,0,1),(133,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(134,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(135,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',0,5,0,1),(136,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',6,214748364,0,1),(137,'2025-09-08',NULL,'2590.999000','0.00',22,15,NULL,'65000.000000',0,5,0,1),(138,'2025-09-08',NULL,'2590.999000','0.00',22,15,NULL,'65000.000000',6,214748364,0,1),(139,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',0,5,0,1),(140,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',6,214748364,0,1),(141,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',0,5,0,1),(142,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',6,214748364,0,1),(143,'2025-09-08',NULL,'1000.000000','0.00',25,15,NULL,'2500.000000',0,5,0,1),(144,'2025-09-08',NULL,'1000.000000','0.00',25,15,NULL,'2500.000000',6,214748364,0,1),(145,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(146,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(147,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',0,5,0,1),(148,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',6,214748364,0,1),(149,'2025-09-08',NULL,'2590.999000','0.00',22,15,NULL,'65000.000000',0,5,0,1),(150,'2025-09-08',NULL,'2590.999000','0.00',22,15,NULL,'65000.000000',6,214748364,0,1),(151,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',0,5,0,1),(152,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',6,214748364,0,1),(153,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',0,5,0,1),(154,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',6,214748364,0,1),(155,'2025-09-08',NULL,'1000.000000','0.00',25,15,NULL,'2500.000000',0,5,0,1),(156,'2025-09-08',NULL,'1000.000000','0.00',25,15,NULL,'2500.000000',6,214748364,0,1),(157,'2025-09-08',NULL,'0.000000','0.00',34,15,NULL,'0.000000',0,5,0,1),(158,'2025-09-08',NULL,'0.000000','0.00',34,15,NULL,'0.000000',6,214748364,0,1),(159,'2025-09-08',NULL,'0.000000','0.00',35,15,NULL,'0.000000',0,5,0,1),(160,'2025-09-08',NULL,'0.000000','0.00',35,15,NULL,'0.000000',6,214748364,0,1),(161,'2025-09-08',NULL,'200.000000','0.00',36,15,NULL,'1491.228070',0,5,0,1),(162,'2025-09-08',NULL,'200.000000','0.00',36,15,NULL,'1491.228070',6,214748364,0,1),(163,'2025-09-08',NULL,'0.000000','0.00',34,15,NULL,'0.000000',0,5,0,1),(164,'2025-09-08',NULL,'0.000000','0.00',34,15,NULL,'0.000000',6,214748364,0,1),(165,'2025-09-08',NULL,'0.000000','0.00',35,15,NULL,'0.000000',0,5,0,1),(166,'2025-09-08',NULL,'0.000000','0.00',35,15,NULL,'0.000000',6,214748364,0,1),(167,'2025-09-08',NULL,'200.000000','0.00',36,15,NULL,'1491.228070',0,5,0,1),(168,'2025-09-08',NULL,'200.000000','0.00',36,15,NULL,'1491.228070',6,214748364,0,1),(169,'2025-09-08',NULL,'0.000000','0.00',34,15,NULL,'0.000000',0,5,0,1),(170,'2025-09-08',NULL,'0.000000','0.00',34,15,NULL,'0.000000',6,214748364,0,1),(171,'2025-09-08',NULL,'0.000000','0.00',35,15,NULL,'0.000000',0,5,0,1),(172,'2025-09-08',NULL,'0.000000','0.00',35,15,NULL,'0.000000',6,214748364,0,1),(173,'2025-09-08',NULL,'200.000000','0.00',36,15,NULL,'1491.228070',0,5,0,1),(174,'2025-09-08',NULL,'200.000000','0.00',36,15,NULL,'1491.228070',6,214748364,0,1),(175,'2025-09-08',NULL,'0.000000','0.00',34,15,NULL,'0.000000',0,5,0,1),(176,'2025-09-08',NULL,'0.000000','0.00',34,15,NULL,'0.000000',6,214748364,0,1),(177,'2025-09-08',NULL,'0.000000','0.00',35,15,NULL,'0.000000',0,5,0,1),(178,'2025-09-08',NULL,'0.000000','0.00',35,15,NULL,'0.000000',6,214748364,0,1),(179,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(180,'2025-09-08',NULL,'250.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(181,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',0,5,0,1),(182,'2025-09-08',NULL,'0.000000','0.00',21,15,NULL,'0.000000',6,214748364,0,1),(183,'2025-09-08',NULL,'2590.999000','0.00',22,15,NULL,'65000.000000',0,5,0,1),(184,'2025-09-08',NULL,'2590.999000','0.00',22,15,NULL,'65000.000000',6,214748364,0,1),(185,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',0,5,0,1),(186,'2025-09-08',NULL,'0.000000','0.00',23,15,NULL,'0.000000',6,214748364,0,1),(187,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',0,5,0,1),(188,'2025-09-08',NULL,'0.000000','0.00',24,15,NULL,'0.000000',6,214748364,0,1),(189,'2025-09-08',NULL,'1000.000000','0.00',25,15,NULL,'2500.000000',0,5,0,1),(190,'2025-09-08',NULL,'1000.000000','0.00',25,15,NULL,'2500.000000',6,214748364,0,1),(191,'2025-09-08',NULL,'0.000000','0.00',432,15,NULL,'0.000000',0,5,0,1),(192,'2025-09-08',NULL,'0.000000','0.00',432,15,NULL,'0.000000',6,214748364,0,1),(193,'2025-09-08',NULL,'0.000000','0.00',433,15,NULL,'0.000000',0,5,0,1),(194,'2025-09-08',NULL,'0.000000','0.00',433,15,NULL,'0.000000',6,214748364,0,1),(195,'2025-09-08',NULL,'0.000000','0.00',434,15,NULL,'0.000000',0,5,0,1),(196,'2025-09-08',NULL,'0.000000','0.00',434,15,NULL,'0.000000',6,214748364,0,1),(197,'2025-09-08',NULL,'0.000000','0.00',435,15,NULL,'0.000000',0,5,0,1),(198,'2025-09-08',NULL,'0.000000','0.00',435,15,NULL,'0.000000',6,214748364,0,1),(199,'2025-09-08',NULL,'0.000000','0.00',436,15,NULL,'0.000000',0,5,0,1),(200,'2025-09-08',NULL,'0.000000','0.00',436,15,NULL,'0.000000',6,214748364,0,1),(201,'2025-09-08',NULL,'0.000000','0.00',437,15,NULL,'0.000000',0,5,0,1),(202,'2025-09-08',NULL,'0.000000','0.00',437,15,NULL,'0.000000',6,214748364,0,1),(203,'2025-09-08',NULL,'17600.000000','0.00',438,15,NULL,'8500.000000',0,5,0,1),(204,'2025-09-08',NULL,'17600.000000','0.00',438,15,NULL,'8500.000000',6,214748364,0,1),(205,'2025-09-08',NULL,'17500.000000','0.00',5,15,NULL,'20000.000000',0,5,0,1),(206,'2025-09-08',NULL,'17500.000000','0.00',5,15,NULL,'20000.000000',6,214748364,0,1),(207,'2025-09-08',NULL,'17500.000000','0.00',5,15,NULL,'20000.000000',0,5,0,1),(208,'2025-09-08',NULL,'17500.000000','0.00',5,15,NULL,'20000.000000',6,214748364,0,1),(209,'2025-09-08',NULL,'1500.000000','0.00',10,15,NULL,'0.000000',0,5,0,1),(210,'2025-09-08',NULL,'1500.000000','0.00',10,15,NULL,'0.000000',6,214748364,0,1),(211,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',0,5,0,1),(212,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(213,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(214,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(215,'2025-09-08',NULL,'20000.000000','0.00',5,15,NULL,'20000.000000',0,5,0,1),(216,'2025-09-08',NULL,'20000.000000','0.00',5,15,NULL,'20000.000000',6,214748364,0,1),(217,'2025-09-08',NULL,'7000.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(218,'2025-09-08',NULL,'7000.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(219,'2025-09-08',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',0,5,0,1),(220,'2025-09-08',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',6,214748364,0,1),(221,'2025-09-08',NULL,'2500.000000','0.00',25,15,NULL,'2500.000000',0,5,0,1),(222,'2025-09-08',NULL,'2500.000000','0.00',25,15,NULL,'2500.000000',6,214748364,0,1),(223,'2025-09-08',NULL,'1750.000000','0.00',26,15,NULL,'1750.000000',0,5,0,1),(224,'2025-09-08',NULL,'1750.000000','0.00',26,15,NULL,'1750.000000',6,214748364,0,1),(225,'2025-09-08',NULL,'1500.000000','0.00',28,15,NULL,'1500.000000',0,5,0,1),(226,'2025-09-08',NULL,'1500.000000','0.00',28,15,NULL,'1500.000000',6,214748364,0,1),(227,'2025-09-08',NULL,'7600.000000','0.00',29,15,NULL,'7600.000000',0,5,0,1),(228,'2025-09-08',NULL,'7600.000000','0.00',29,15,NULL,'7600.000000',6,214748364,0,1),(229,'2025-09-08',NULL,'1491.228070','0.00',36,15,NULL,'1491.228070',0,5,0,1),(230,'2025-09-08',NULL,'1491.228070','0.00',36,15,NULL,'1491.228070',6,214748364,0,1),(231,'2025-09-08',NULL,'8500.000000','0.00',438,15,NULL,'8500.000000',0,5,0,1),(232,'2025-09-08',NULL,'8500.000000','0.00',438,15,NULL,'8500.000000',6,214748364,0,1),(233,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',0,5,0,1),(234,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',6,214748364,0,1),(235,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',0,5,0,1),(236,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(237,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(238,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(239,'2025-09-08',NULL,'20000.000000','0.00',5,15,NULL,'20000.000000',0,5,0,1),(240,'2025-09-08',NULL,'20000.000000','0.00',5,15,NULL,'20000.000000',6,214748364,0,1),(241,'2025-09-08',NULL,'7000.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(242,'2025-09-08',NULL,'7000.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(243,'2025-09-08',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',0,5,0,1),(244,'2025-09-08',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',6,214748364,0,1),(245,'2025-09-08',NULL,'2500.000000','0.00',25,15,NULL,'2500.000000',0,5,0,1),(246,'2025-09-08',NULL,'2500.000000','0.00',25,15,NULL,'2500.000000',6,214748364,0,1),(247,'2025-09-08',NULL,'1750.000000','0.00',26,15,NULL,'1750.000000',0,5,0,1),(248,'2025-09-08',NULL,'1750.000000','0.00',26,15,NULL,'1750.000000',6,214748364,0,1),(249,'2025-09-08',NULL,'1500.000000','0.00',28,15,NULL,'1500.000000',0,5,0,1),(250,'2025-09-08',NULL,'1500.000000','0.00',28,15,NULL,'1500.000000',6,214748364,0,1),(251,'2025-09-08',NULL,'7600.000000','0.00',29,15,NULL,'7600.000000',0,5,0,1),(252,'2025-09-08',NULL,'7600.000000','0.00',29,15,NULL,'7600.000000',6,214748364,0,1),(253,'2025-09-08',NULL,'1491.228070','0.00',36,15,NULL,'1491.228070',0,5,0,1),(254,'2025-09-08',NULL,'1491.228070','0.00',36,15,NULL,'1491.228070',6,214748364,0,1),(255,'2025-09-08',NULL,'8500.000000','0.00',438,15,NULL,'8500.000000',0,5,0,1),(256,'2025-09-08',NULL,'8500.000000','0.00',438,15,NULL,'8500.000000',6,214748364,0,1),(257,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',0,5,0,1),(258,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',6,214748364,0,1),(259,'2025-09-08',NULL,'7000.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(260,'2025-09-08',NULL,'7000.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(261,'2025-09-08',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',0,5,0,1),(262,'2025-09-08',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',6,214748364,0,1),(263,'2025-09-08',NULL,'2500.000000','0.00',25,15,NULL,'2500.000000',0,5,0,1),(264,'2025-09-08',NULL,'2500.000000','0.00',25,15,NULL,'2500.000000',6,214748364,0,1),(265,'2025-09-08',NULL,'1200.000000','0.00',440,15,NULL,'1450.000000',0,5,NULL,1),(266,'2025-09-08',NULL,'1200.000000','0.00',440,15,NULL,'1450.000000',6,214748364,NULL,1),(267,'2025-09-08',NULL,'2000.000000','0.00',440,15,NULL,'1600.000000',0,5,0,1),(268,'2025-09-08',NULL,'2000.000000','0.00',440,15,NULL,'1600.000000',6,214748364,0,1),(269,'2025-09-08',NULL,'2000.000000','0.00',440,15,NULL,'7500.000000',0,5,0,1),(270,'2025-09-08',NULL,'2000.000000','0.00',440,15,NULL,'7500.000000',6,214748364,0,1),(271,'2025-09-08',NULL,'5000.000000','0.00',440,15,NULL,'8000.000000',0,5,0,1),(272,'2025-09-08',NULL,'5000.000000','0.00',440,15,NULL,'8000.000000',6,214748364,0,1),(273,'2025-09-08',NULL,'20000.000000','0.00',5,15,NULL,'20000.000000',0,5,0,1),(274,'2025-09-08',NULL,'20000.000000','0.00',5,15,NULL,'20000.000000',6,214748364,0,1),(275,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(276,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(277,'2025-09-08',NULL,'1500.000000','0.00',10,15,NULL,'0.000000',0,5,0,1),(278,'2025-09-08',NULL,'1500.000000','0.00',10,15,NULL,'0.000000',6,214748364,0,1),(279,'2025-09-08',NULL,'1000.000000','0.00',440,15,NULL,'8000.000000',0,5,0,1),(280,'2025-09-08',NULL,'1000.000000','0.00',440,15,NULL,'8000.000000',6,214748364,0,1),(281,'2025-09-08',NULL,'2000.000000','0.00',440,15,NULL,'8000.000000',0,5,0,1),(282,'2025-09-08',NULL,'2000.000000','0.00',440,15,NULL,'8000.000000',6,214748364,0,1),(283,'2025-09-08',NULL,'3000.000000','0.00',14,15,NULL,'5000.000000',0,5,0,1),(284,'2025-09-08',NULL,'3000.000000','0.00',14,15,NULL,'5000.000000',6,214748364,0,1),(285,'2025-09-08',NULL,'3500.000000','0.00',440,15,NULL,'8000.000000',0,5,0,1),(286,'2025-09-08',NULL,'3500.000000','0.00',440,15,NULL,'8000.000000',6,214748364,0,1),(287,'2025-09-08',NULL,'1500.000000','0.00',5,15,NULL,'20000.000000',0,5,0,1),(288,'2025-09-08',NULL,'1500.000000','0.00',5,15,NULL,'20000.000000',6,214748364,0,1),(289,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(290,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(291,'2025-09-08',NULL,'1500.000000','0.00',10,15,NULL,'0.000000',0,5,0,1),(292,'2025-09-08',NULL,'1500.000000','0.00',10,15,NULL,'0.000000',6,214748364,0,1),(293,'2025-09-08',NULL,'300.000000','0.00',5,15,NULL,'20000.000000',0,5,0,1),(294,'2025-09-08',NULL,'300.000000','0.00',5,15,NULL,'20000.000000',6,214748364,0,1),(295,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(296,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(297,'2025-09-08',NULL,'1500.000000','0.00',10,15,NULL,'0.000000',0,5,0,1),(298,'2025-09-08',NULL,'1500.000000','0.00',10,15,NULL,'0.000000',6,214748364,0,1),(299,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(300,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(301,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',0,5,0,1),(302,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',6,214748364,0,1),(303,'2025-09-08',NULL,'3500.000000','0.00',440,15,NULL,'7500.000000',0,5,0,1),(304,'2025-09-08',NULL,'3500.000000','0.00',440,15,NULL,'7500.000000',6,214748364,0,1),(305,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',0,5,0,1),(306,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',6,214748364,0,1),(307,'2025-09-08',NULL,'4500.000000','0.00',440,15,NULL,'7500.000000',0,5,0,1),(308,'2025-09-08',NULL,'4500.000000','0.00',440,15,NULL,'7500.000000',6,214748364,0,1),(309,'2025-09-08',NULL,'7000.000000','0.00',20,15,NULL,'7000.000000',0,5,0,1),(310,'2025-09-08',NULL,'7000.000000','0.00',20,15,NULL,'7000.000000',6,214748364,0,1),(311,'2025-09-08',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',0,5,0,1),(312,'2025-09-08',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',6,214748364,0,1),(313,'2025-09-08',NULL,'5000.000000','0.00',24,15,NULL,'6500.000000',0,5,0,1),(314,'2025-09-08',NULL,'5000.000000','0.00',24,15,NULL,'6500.000000',6,214748364,0,1),(315,'2025-09-08',NULL,'2500.000000','0.00',25,15,NULL,'2500.000000',0,5,0,1),(316,'2025-09-08',NULL,'2500.000000','0.00',25,15,NULL,'2500.000000',6,214748364,0,1),(317,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',0,5,0,1),(318,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(319,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(320,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(321,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',0,5,0,1),(322,'2025-09-08',NULL,'1315.789473','0.00',439,15,NULL,'1315.789473',6,214748364,0,1),(323,'2025-09-08',NULL,'2000.000000','0.00',440,15,NULL,'3000.000000',0,5,0,1),(324,'2025-09-08',NULL,'2000.000000','0.00',440,15,NULL,'3000.000000',6,214748364,0,1),(325,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(326,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(327,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(328,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(329,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',0,5,0,1),(330,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',6,214748364,0,1),(331,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(332,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(333,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(334,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(335,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',0,5,0,1),(336,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',6,214748364,0,1),(337,'2025-09-08',NULL,'1000.000000','0.00',8,15,NULL,'2000.000000',0,5,0,1),(338,'2025-09-08',NULL,'1000.000000','0.00',8,15,NULL,'2000.000000',6,214748364,0,1),(339,'2025-09-08',NULL,'1000.000000','0.00',9,15,NULL,'2000.000000',0,5,0,1),(340,'2025-09-08',NULL,'1000.000000','0.00',9,15,NULL,'2000.000000',6,214748364,0,1),(341,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',0,5,0,1),(342,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',6,214748364,0,1),(343,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',0,5,0,1),(344,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',6,214748364,0,1),(345,'2025-09-10',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',0,5,0,1),(346,'2025-09-10',NULL,'65000.000000','0.00',22,15,NULL,'65000.000000',6,214748364,0,1),(347,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',0,5,0,1),(348,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(349,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',0,5,0,1),(350,'2025-09-08',NULL,'650.000000','0.00',3,15,NULL,'650.000000',6,214748364,0,1),(351,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(352,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(353,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(354,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(355,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',0,5,0,1),(356,'2025-09-08',NULL,'5600.000000','0.00',4,15,NULL,'5600.000000',6,214748364,0,1),(357,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(358,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(359,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',0,5,0,1),(360,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',6,214748364,0,1),(361,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',0,5,0,1),(362,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',6,214748364,0,1),(363,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',0,5,0,1),(364,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',6,214748364,0,1),(365,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(366,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(367,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(368,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(369,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(370,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(371,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(372,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(373,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(374,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(375,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(376,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(377,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(378,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(379,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(380,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(381,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(382,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(383,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(384,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(385,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(386,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(387,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',0,5,0,1),(388,'2025-09-08',NULL,'1200.000000','0.00',5,15,NULL,'1600.000000',6,214748364,0,1),(389,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',0,5,0,1),(390,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',6,214748364,0,1),(391,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',0,5,0,1),(392,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',6,214748364,0,1),(393,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',0,5,0,1),(394,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',6,214748364,0,1),(395,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',0,5,0,1),(396,'2025-09-08',NULL,'1000.000000','0.00',7,15,NULL,'2000.000000',6,214748364,0,1),(397,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(398,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(399,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(400,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(401,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(402,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(403,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(404,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(405,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(406,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(407,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',0,5,0,1),(408,'2025-09-08',NULL,'3000.000000','0.00',6,15,NULL,'4000.000000',6,214748364,0,1),(409,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',0,5,0,1),(410,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',6,214748364,0,1),(411,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',0,5,0,1),(412,'2025-09-08',NULL,'3200.000000','0.00',10,15,NULL,'4500.000000',6,214748364,0,1),(413,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',0,5,0,1),(414,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',6,214748364,0,1),(415,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',0,5,0,1),(416,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',6,214748364,0,1),(417,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',0,5,0,1),(418,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',6,214748364,0,1),(419,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',0,5,0,1),(420,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',6,214748364,0,1),(421,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',0,5,0,1),(422,'2025-09-10',NULL,'1200.000000','0.00',13,15,NULL,'6000.000000',6,214748364,0,1);
/*!40000 ALTER TABLE `tb_preco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_pro_forma`
--

DROP TABLE IF EXISTS `tb_pro_forma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_pro_forma` (
  `pk_pro_forma` int(11) NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `fk_cliente` int(11) NOT NULL,
  `fk_usuario` int(11) NOT NULL,
  `total` double DEFAULT NULL,
  PRIMARY KEY (`pk_pro_forma`),
  KEY `fk_tb_pro_forma_tb_cliente1_idx` (`fk_cliente`),
  KEY `fk_tb_pro_forma_tb_usuario1_idx` (`fk_usuario`),
  CONSTRAINT `fk_tb_pro_forma_tb_cliente1` FOREIGN KEY (`fk_cliente`) REFERENCES `tb_cliente` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_pro_forma_tb_usuario1` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_pro_forma`
--

LOCK TABLES `tb_pro_forma` WRITE;
/*!40000 ALTER TABLE `tb_pro_forma` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_pro_forma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_produto`
--

DROP TABLE IF EXISTS `tb_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_produto` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `designacao` varchar(700) DEFAULT NULL,
  `preco` decimal(30,2) DEFAULT NULL,
  `data_fabrico` date NOT NULL DEFAULT '2014-01-01',
  `data_expiracao` date NOT NULL DEFAULT '2014-01-01',
  `codBarra` varchar(200) NOT NULL DEFAULT '0',
  `status` varchar(10) NOT NULL DEFAULT 'false',
  `data_entrada` date NOT NULL DEFAULT '2014-01-01',
  `stocavel` varchar(45) NOT NULL DEFAULT 'true',
  `preco_venda` double DEFAULT '0',
  `percentagem_desconto` double DEFAULT '0',
  `quantidade_desconto` int(10) unsigned DEFAULT '0',
  `codigo_manual` varchar(50) DEFAULT NULL,
  `cod_unidade` int(11) NOT NULL,
  `cod_local` int(11) NOT NULL,
  `cod_fornecedores` int(11) NOT NULL,
  `cod_Tipo_Produto` int(11) DEFAULT NULL,
  `fk_modelo` int(11) DEFAULT NULL,
  `fk_grupo` int(11) DEFAULT NULL,
  `photo` longblob,
  `status_iva` varchar(45) DEFAULT 'true',
  `cozinha` enum('Enviar Ticket','Nao Enviar Ticket','Enviar Sala') DEFAULT 'Nao Enviar Ticket',
  `cod_pai` int(10) unsigned DEFAULT '0',
  `unidade_compra` double DEFAULT '1',
  PRIMARY KEY (`codigo`),
  KEY `fk_tb_produto_tb_tipo_produto` (`cod_Tipo_Produto`),
  KEY `fk_tb_produto_tb_fornecedor1` (`cod_fornecedores`),
  KEY `fk_tb_produto_modelo1_idx` (`fk_modelo`),
  KEY `fk_tb_produto_grupo1_idx` (`fk_grupo`),
  KEY `fk_tb_produto_tb_local1_idx` (`cod_local`),
  KEY `fk_tb_produto_unidade1_idx` (`cod_unidade`),
  CONSTRAINT `fk_tb_produto_grupo1` FOREIGN KEY (`fk_grupo`) REFERENCES `grupo` (`pk_grupo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_produto_modelo1` FOREIGN KEY (`fk_modelo`) REFERENCES `modelo` (`pk_modelo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_produto_tb_fornecedor1` FOREIGN KEY (`cod_fornecedores`) REFERENCES `tb_fornecedor` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_produto_tb_local1` FOREIGN KEY (`cod_local`) REFERENCES `tb_local` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_produto_tb_tipo_produto` FOREIGN KEY (`cod_Tipo_Produto`) REFERENCES `tb_tipo_produto` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_produto_unidade1` FOREIGN KEY (`cod_unidade`) REFERENCES `unidade` (`pk_unidade`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=441 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_produto`
--

LOCK TABLES `tb_produto` WRITE;
/*!40000 ALTER TABLE `tb_produto` DISABLE KEYS */;
INSERT INTO `tb_produto` VALUES (1,'ADIANTAMENTO DE CLIENTE','100.00','2025-09-06','2025-09-06','1','Activo','2025-09-06','true',100,0,0,'44444',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(2,'ALCINO 14T 105L','1000.00','2025-09-06','2025-09-06','2','Activo','2025-09-06','true',1000,0,0,'11430.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(3,'APARAFUSADOR C/CARREGADOR','100.00','2025-09-06','2025-09-06','3','Activo','2025-09-06','true',100,NULL,0,'11570.0',1,1,1,2,1,1,NULL,'true','Nao Enviar Ticket',0,1),(4,'APARAFUSADOR C/CARREGADOR/TID','100.00','2025-09-06','2025-09-06','4','Activo','2025-09-06','true',100,NULL,0,'11571.0',1,1,1,2,1,1,NULL,'true','Nao Enviar Ticket',0,1),(5,'ARAME GALVANIZADO 2.7MMX1OKG','100.00','2025-09-06','2025-09-06','5','Activo','2025-09-06','true',100,NULL,0,'11426.0',1,1,1,3,1,1,NULL,'true','Nao Enviar Ticket',0,1),(6,'ARAME LAMINADO 22X7.8KG','100.00','2025-09-06','2025-09-06','6','Activo','2025-09-06','true',100,NULL,0,'11424.0',1,1,1,3,1,1,NULL,'true','Nao Enviar Ticket',0,1),(7,'ARAME QEHMADO 1.1MM 15-KG-LOCAL','100.00','2025-09-06','2025-09-06','7','Activo','2025-09-06','true',100,NULL,0,'11425.0',1,1,1,3,1,1,NULL,'true','Nao Enviar Ticket',0,1),(8,'ARAME QEHMADO 1.1MM-20 KG','100.00','2025-09-06','2025-09-06','8','Activo','2025-09-06','true',100,NULL,0,'11467.0',1,1,1,3,1,1,NULL,'true','Nao Enviar Ticket',0,1),(9,'ARAME QEHMADO 1.5MM-18 KG','100.00','2025-09-06','2025-09-06','9','Activo','2025-09-06','true',100,NULL,0,'11101.0',1,1,1,3,1,1,NULL,'true','Nao Enviar Ticket',0,1),(10,'ARAME QEMADO 1.1MM-18 KG','100.00','2025-09-06','2025-09-06','10','Activo','2025-09-06','true',100,NULL,0,'11100.0',1,1,1,3,1,1,NULL,'true','Nao Enviar Ticket',0,1),(11,'ARMASAO 15 PCS','100.00','2025-09-06','2025-09-06','11','Activo','2025-09-06','true',100,NULL,0,'11296.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(12,'ARMASAO NO-1','100.00','2025-09-06','2025-09-06','12','Activo','2025-09-06','true',100,NULL,0,'11102.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(13,'AUTO CLISMO','100.00','2025-09-06','2025-09-06','13','Activo','2025-09-06','true',100,NULL,0,'11119.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(14,'AZULEIJO 30X45-35000','100.00','2025-09-06','2025-09-06','14','Activo','2025-09-06','true',100,NULL,0,'11442.0',1,1,1,4,1,1,NULL,'true','Nao Enviar Ticket',0,1),(15,'AZULEIJO 30X45-35001','100.00','2025-09-06','2025-09-06','15','Activo','2025-09-06','true',100,NULL,0,'11443.0',1,1,1,4,1,1,NULL,'true','Nao Enviar Ticket',0,1),(16,'AZULEIJO 30X45-35003','100.00','2025-09-06','2025-09-06','16','Activo','2025-09-06','true',100,NULL,0,'11444.0',1,1,1,4,1,1,NULL,'true','Nao Enviar Ticket',0,1),(17,'AZULEIJO 30X45-35004','100.00','2025-09-06','2025-09-06','17','Activo','2025-09-06','true',100,NULL,0,'11445.0',1,1,1,4,1,1,NULL,'true','Nao Enviar Ticket',0,1),(18,'AZULEIJO 30X60-36000','100.00','2025-09-06','2025-09-06','18','Activo','2025-09-06','true',100,NULL,0,'11447.0',1,1,1,4,1,1,NULL,'true','Nao Enviar Ticket',0,1),(19,'AZULEIJO 30X60-36002','100.00','2025-09-06','2025-09-06','19','Activo','2025-09-06','true',100,NULL,0,'11500.0',1,1,1,4,1,1,NULL,'true','Nao Enviar Ticket',0,1),(20,'BALANCA 20KG','100.00','2025-09-06','2025-09-06','20','Activo','2025-09-06','true',100,NULL,0,'11379.0',1,1,1,5,1,1,NULL,'true','Nao Enviar Ticket',0,1),(21,'BALANCA 30-KG','100.00','2025-09-06','2025-09-06','21','Activo','2025-09-06','true',100,NULL,0,'11501.0',1,1,1,5,1,1,NULL,'true','Nao Enviar Ticket',0,1),(22,'BALAO DE E-BOMBA-AUTO','100.00','2025-09-06','2025-09-06','22','Activo','2025-09-06','true',100,NULL,0,'11103.0',1,1,1,5,1,1,NULL,'true','Nao Enviar Ticket',0,1),(23,'BALAO DE E-BOMBA-NOR','100.00','2025-09-06','2025-09-06','23','Activo','2025-09-06','true',100,NULL,0,'11104.0',1,1,1,5,1,1,NULL,'true','Nao Enviar Ticket',0,1),(24,'BALDE PRETO 10 LITRO','100.00','2025-09-06','2025-09-06','24','Activo','2025-09-06','true',100,NULL,0,'11105.0',1,1,1,5,1,1,NULL,'true','Nao Enviar Ticket',0,1),(25,'BALDE PRETO 10-LITRO-2ND','100.00','2025-09-06','2025-09-06','25','Activo','2025-09-06','true',100,NULL,0,'11000.0',1,1,1,5,1,1,NULL,'true','Nao Enviar Ticket',0,1),(26,'BERBEQUIN DE INPACTO 680W','100.00','2025-09-06','2025-09-06','26','Activo','2025-09-06','true',100,NULL,0,'11564.0',1,1,1,6,1,1,NULL,'true','Nao Enviar Ticket',0,1),(27,'BERBEQUIN NORMAL 650W','100.00','2025-09-06','2025-09-06','27','Activo','2025-09-06','true',100,NULL,0,'11563.0',1,1,1,6,1,1,NULL,'true','Nao Enviar Ticket',0,1),(28,'BOTA DE BORACHA 41','100.00','2025-09-06','2025-09-06','28','Activo','2025-09-06','true',100,NULL,0,'11427.0',1,1,1,7,1,1,NULL,'true','Nao Enviar Ticket',0,1),(29,'BOTA DE BORACHA 42','100.00','2025-09-06','2025-09-06','29','Activo','2025-09-06','true',100,NULL,0,'11428.0',1,1,1,7,1,1,NULL,'true','Nao Enviar Ticket',0,1),(30,'BOTA DE CONSTRUCAO-NO-41','100.00','2025-09-06','2025-09-06','30','Activo','2025-09-06','true',100,NULL,0,'11464.0',1,1,1,7,1,1,NULL,'true','Nao Enviar Ticket',0,1),(31,'BOTA DE CONSTRUCAO-NO-42','100.00','2025-09-06','2025-09-06','31','Activo','2025-09-06','true',100,NULL,0,'11465.0',1,1,1,7,1,1,NULL,'true','Nao Enviar Ticket',0,1),(32,'BOTA DE CONSTRUCAO-NO-43','100.00','2025-09-06','2025-09-06','32','Activo','2025-09-06','true',100,NULL,0,'11466.0',1,1,1,7,1,1,NULL,'true','Nao Enviar Ticket',0,1),(33,'BRASADERA','100.00','2025-09-06','2025-09-06','33','Activo','2025-09-06','true',100,NULL,0,'11481.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(34,'BROCA ELECTRICA 500W','100.00','2025-09-06','2025-09-06','34','Activo','2025-09-06','true',100,NULL,0,'11562.0',1,1,1,8,1,1,NULL,'true','Nao Enviar Ticket',0,1),(35,'BROCA IMPACTO','100.00','2025-09-06','2025-09-06','35','Activo','2025-09-06','true',100,NULL,0,'11572.0',1,1,1,8,1,1,NULL,'true','Nao Enviar Ticket',0,1),(36,'BROQUIM 1050W','100.00','2025-09-06','2025-09-06','36','Activo','2025-09-06','true',100,NULL,0,'11369.0',1,1,1,8,1,1,NULL,'true','Nao Enviar Ticket',0,1),(37,'BROQUIM 900W','100.00','2025-09-06','2025-09-06','37','Activo','2025-09-06','true',100,NULL,0,'11371.0',1,1,1,9,1,1,NULL,'true','Nao Enviar Ticket',0,1),(38,'BUCAPOL/LIGHT-TESTER','100.00','2025-09-06','2025-09-06','38','Activo','2025-09-06','true',100,NULL,0,'11529.0',1,1,1,9,1,1,NULL,'true','Nao Enviar Ticket',0,1),(39,'BUCHA 10 X 50MM (40)-PCS','100.00','2025-09-06','2025-09-06','39','Activo','2025-09-06','true',100,NULL,0,'11417.0',1,1,1,9,1,1,NULL,'true','Nao Enviar Ticket',0,1),(40,'BUCHA 8.0 X 40MM (80)-PCS','100.00','2025-09-06','2025-09-06','40','Activo','2025-09-06','true',100,NULL,0,'11380.0',1,1,1,9,1,1,NULL,'true','Nao Enviar Ticket',0,1),(41,'CABO D ELECTRICO 2X16MM-PRETO','100.00','2025-09-06','2025-09-06','41','Activo','2025-09-06','true',100,NULL,0,'11482.0',1,1,1,10,1,1,NULL,'true','Nao Enviar Ticket',0,1),(42,'CABO ELETRICO 2X2.5MM','100.00','2025-09-06','2025-09-06','42','Activo','2025-09-06','true',100,NULL,0,'11649.0',1,1,1,10,1,1,NULL,'true','Nao Enviar Ticket',0,1),(43,'CABO ELETRICO 3X2.5MM','100.00','2025-09-06','2025-09-06','43','Activo','2025-09-06','true',100,NULL,0,'11651.0',1,1,1,10,1,1,NULL,'true','Nao Enviar Ticket',0,1),(44,'CADEADO MEDIO-CHAVE ESTRELA','100.00','2025-09-06','2025-09-06','44','Activo','2025-09-06','true',100,NULL,0,'11550.0',1,1,1,11,1,1,NULL,'true','Nao Enviar Ticket',0,1),(45,'CADEADO-GRANDE','100.00','2025-09-06','2025-09-06','45','Activo','2025-09-06','true',100,NULL,0,'11538.0',1,1,1,11,1,1,NULL,'true','Nao Enviar Ticket',0,1),(46,'CADEADO-MEDIO','100.00','2025-09-06','2025-09-06','46','Activo','2025-09-06','true',100,NULL,0,'11549.0',1,1,1,11,1,1,NULL,'true','Nao Enviar Ticket',0,1),(47,'CADEADO-NORMAL','100.00','2025-09-06','2025-09-06','47','Activo','2025-09-06','true',100,NULL,0,'11537.0',1,1,1,11,1,1,NULL,'true','Nao Enviar Ticket',0,1),(48,'CADEADO-PEQHENO','100.00','2025-09-06','2025-09-06','48','Activo','2025-09-06','true',100,NULL,0,'11536.0',1,1,1,11,1,1,NULL,'true','Nao Enviar Ticket',0,1),(49,'CAMARA SIMPLES','100.00','2025-09-06','2025-09-06','49','Activo','2025-09-06','true',100,NULL,0,'11498.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(50,'CANISSO LISO 16','100.00','2025-09-06','2025-09-06','50','Activo','2025-09-06','true',100,NULL,0,'11288.0',1,1,1,12,1,1,NULL,'true','Nao Enviar Ticket',0,1),(51,'CANISSO LISO 20','100.00','2025-09-06','2025-09-06','51','Activo','2025-09-06','true',100,NULL,0,'11299.0',1,1,1,12,1,1,NULL,'true','Nao Enviar Ticket',0,1),(52,'CANISSO LISO- 16','100.00','2025-09-06','2025-09-06','52','Activo','2025-09-06','true',100,NULL,0,'11111.0',1,1,1,12,1,1,NULL,'true','Nao Enviar Ticket',0,1),(53,'CANISSO-FLIXIBEL- 16','100.00','2025-09-06','2025-09-06','53','Activo','2025-09-06','true',100,NULL,0,'11249.0',1,1,1,12,1,1,NULL,'true','Nao Enviar Ticket',0,1),(54,'CANISSO-FLIXIBEL- 20','100.00','2025-09-06','2025-09-06','54','Activo','2025-09-06','true',100,NULL,0,'11250.0',1,1,1,12,1,1,NULL,'true','Nao Enviar Ticket',0,1),(55,'CANISSO-FLIXIBEL- 32','100.00','2025-09-06','2025-09-06','55','Activo','2025-09-06','true',100,NULL,0,'11252.0',1,1,1,12,1,1,NULL,'true','Nao Enviar Ticket',0,1),(56,'CANISSO-FLIXIBEL-25','100.00','2025-09-06','2025-09-06','56','Activo','2025-09-06','true',100,NULL,0,'11251.0',1,1,1,12,1,1,NULL,'true','Nao Enviar Ticket',0,1),(57,'CANTONERA 300*350MM','100.00','2025-09-06','2025-09-06','57','Activo','2025-09-06','true',100,NULL,0,'11311.0',1,1,1,13,1,1,NULL,'true','Nao Enviar Ticket',0,1),(58,'CANTONERA 300*350MM-CIZA','100.00','2025-09-06','2025-09-06','58','Activo','2025-09-06','true',100,NULL,0,'11106.0',1,1,1,13,1,1,NULL,'true','Nao Enviar Ticket',0,1),(59,'CAPA DE CHUVA','100.00','2025-09-06','2025-09-06','59','Activo','2025-09-06','true',100,NULL,0,'11391.0',1,1,1,14,1,1,NULL,'true','Nao Enviar Ticket',0,1),(60,'CAPASET','100.00','2025-09-06','2025-09-06','60','Activo','2025-09-06','true',100,NULL,0,'11392.0',1,1,1,15,1,1,NULL,'true','Nao Enviar Ticket',0,1),(61,'CARO DE MAO GRANDE','100.00','2025-09-06','2025-09-06','61','Activo','2025-09-06','true',100,NULL,0,'11109.0',1,1,1,15,1,1,NULL,'true','Nao Enviar Ticket',0,1),(62,'CARO DE MAO MID 2ND','100.00','2025-09-06','2025-09-06','62','Activo','2025-09-06','true',100,NULL,0,'11108.0',1,1,1,15,1,1,NULL,'true','Nao Enviar Ticket',0,1),(63,'CATANA 16','100.00','2025-09-06','2025-09-06','63','Activo','2025-09-06','true',100,NULL,0,'11110.0',1,1,1,16,1,1,NULL,'true','Nao Enviar Ticket',0,1),(64,'CIMENTO COLA 20 KG--SUPER','100.00','2025-09-06','2025-09-06','64','Activo','2025-09-06','true',100,NULL,0,'11257.0',1,1,1,17,1,1,NULL,'true','Nao Enviar Ticket',0,1),(65,'CIMENTO COLA 20KG-PER','100.00','2025-09-06','2025-09-06','65','Activo','2025-09-06','true',100,NULL,0,'11486.0',1,1,1,17,1,1,NULL,'true','Nao Enviar Ticket',0,1),(66,'COLA DE MADERA 1.5L WOODFIX','100.00','2025-09-06','2025-09-06','66','Activo','2025-09-06','true',100,NULL,0,'11356.0',1,1,1,18,1,1,NULL,'true','Nao Enviar Ticket',0,1),(67,'COLA DE MADERA BRETX BRANCA 1KG','100.00','2025-09-06','2025-09-06','67','Activo','2025-09-06','true',100,NULL,0,'11112.0',1,1,1,18,1,1,NULL,'true','Nao Enviar Ticket',0,1),(68,'COLA DE MADERA TOP  BRANCA 1KG','100.00','2025-09-06','2025-09-06','68','Activo','2025-09-06','true',100,NULL,0,'11322.0',1,1,1,18,1,1,NULL,'true','Nao Enviar Ticket',0,1),(69,'COLA DE MADERA WOOD FIX-1/2 LITRO','100.00','2025-09-06','2025-09-06','69','Activo','2025-09-06','true',100,NULL,0,'11353.0',1,1,1,18,1,1,NULL,'true','Nao Enviar Ticket',0,1),(70,'COLA DE MADERA WOOD FIX-1LITRO','100.00','2025-09-06','2025-09-06','70','Activo','2025-09-06','true',100,NULL,0,'11366.0',1,1,1,18,1,1,NULL,'true','Nao Enviar Ticket',0,1),(71,'CORANTE 12-PCS','100.00','2025-09-06','2025-09-06','71','Activo','2025-09-06','true',100,NULL,0,'11171.0',1,1,1,19,1,1,NULL,'true','Nao Enviar Ticket',0,1),(72,'CORRANTE  GRANDE','100.00','2025-09-06','2025-09-06','72','Activo','2025-09-06','true',100,NULL,0,'11502.0',1,1,1,19,1,1,NULL,'true','Nao Enviar Ticket',0,1),(73,'CORTADOR DE TUBO PVC','100.00','2025-09-06','2025-09-06','73','Activo','2025-09-06','true',100,NULL,0,'11566.0',1,1,1,20,1,1,NULL,'true','Nao Enviar Ticket',0,1),(74,'CULEHR DE PEDRERO MID','100.00','2025-09-06','2025-09-06','74','Activo','2025-09-06','true',100,NULL,0,'11372.0',1,1,1,20,1,1,NULL,'true','Nao Enviar Ticket',0,1),(75,'CULETE VERMELHO','100.00','2025-09-06','2025-09-06','75','Activo','2025-09-06','true',100,NULL,0,'11390.0',1,1,1,20,1,1,NULL,'true','Nao Enviar Ticket',0,1),(76,'CULHER DE CONSTRUSAO','100.00','2025-09-06','2025-09-06','76','Activo','2025-09-06','true',100,NULL,0,'11355.0',1,1,1,20,1,1,NULL,'true','Nao Enviar Ticket',0,1),(77,'DISCO DE CORT 118* 230MM FINO','100.00','2025-09-06','2025-09-06','77','Activo','2025-09-06','true',100,NULL,0,'11118.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(78,'DISCO DE CORT 180MM JOGADOR','100.00','2025-09-06','2025-09-06','78','Activo','2025-09-06','true',100,NULL,0,'11120.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(79,'DISCO DE CORT 230MM JOGADOR','100.00','2025-09-06','2025-09-06','79','Activo','2025-09-06','true',100,NULL,0,'11121.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(80,'DISCO DE CORT FINO 180MM','100.00','2025-09-06','2025-09-06','80','Activo','2025-09-06','true',100,NULL,0,'11309.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(81,'DISCO DE CORT MADERA115MM','100.00','2025-09-06','2025-09-06','81','Activo','2025-09-06','true',100,NULL,0,'11122.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(82,'DISCO DE CORT MADERA180MM','100.00','2025-09-06','2025-09-06','82','Activo','2025-09-06','true',100,NULL,0,'11123.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(83,'DISCO DE CORT MADERA230MM','100.00','2025-09-06','2025-09-06','83','Activo','2025-09-06','true',100,NULL,0,'11124.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(84,'DISCO DE CORT MOZICO 115MM','100.00','2025-09-06','2025-09-06','84','Activo','2025-09-06','true',100,NULL,0,'11125.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(85,'DISCO DE CORT MOZICO 115MM 2ND','100.00','2025-09-06','2025-09-06','85','Activo','2025-09-06','true',100,NULL,0,'11305.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(86,'DISCO DE CORT MOZICO 180MM','100.00','2025-09-06','2025-09-06','86','Activo','2025-09-06','true',100,NULL,0,'11126.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(87,'DISCO DE CORT MOZICO 230MM','100.00','2025-09-06','2025-09-06','87','Activo','2025-09-06','true',100,NULL,0,'11127.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(88,'DISCO DE CORT115*1.2MM(10 PCS)-FINO','100.00','2025-09-06','2025-09-06','88','Activo','2025-09-06','true',100,NULL,0,'11256.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(89,'DISCO DE LIMA 180MM','100.00','2025-09-06','2025-09-06','89','Activo','2025-09-06','true',100,NULL,0,'11115.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(90,'DISCO DE LIMA 230*6MM','100.00','2025-09-06','2025-09-06','90','Activo','2025-09-06','true',100,NULL,0,'11290.0',1,1,1,21,1,1,NULL,'true','Nao Enviar Ticket',0,1),(91,'DOBRALICA 3/8  10 PCS APA','100.00','2025-09-06','2025-09-06','91','Activo','2025-09-06','true',100,NULL,0,'11269.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(92,'DOBRALICA 3/8  20 PCS NOR..','100.00','2025-09-06','2025-09-06','92','Activo','2025-09-06','true',100,NULL,0,'11131.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(93,'DOBRALICA 3/8  50 PCS NORMAL','100.00','2025-09-06','2025-09-06','93','Activo','2025-09-06','true',100,NULL,0,'11138.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(94,'DOBRALICA 3/8 25 PCS APA-85MM','100.00','2025-09-06','2025-09-06','94','Activo','2025-09-06','true',100,NULL,0,'11312.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(95,'DOBRALICA 3/8-10-PCS-NORMAL','100.00','2025-09-06','2025-09-06','95','Activo','2025-09-06','true',100,NULL,0,'11362.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(96,'DOBRALICA 4/8  50 PCS-NOR..','100.00','2025-09-06','2025-09-06','96','Activo','2025-09-06','true',100,NULL,0,'11133.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(97,'DOBRALICA 4/8 25 PCS/APA/','100.00','2025-09-06','2025-09-06','97','Activo','2025-09-06','true',100,NULL,0,'11317.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(98,'DOBRALICA 4/8-20-PCS NORMAL','100.00','2025-09-06','2025-09-06','98','Activo','2025-09-06','true',100,NULL,0,'11259.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(99,'DOBRALICA 5/16 10 PCS-APA','100.00','2025-09-06','2025-09-06','99','Activo','2025-09-06','true',100,NULL,0,'11134.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(100,'DOBRALICA 5/16 20 PCS NORMAL','100.00','2025-09-06','2025-09-06','100','Activo','2025-09-06','true',100,NULL,0,'11135.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(101,'DOBRALICA 5/16 50 PCS-NORMAL','100.00','2025-09-06','2025-09-06','101','Activo','2025-09-06','true',100,NULL,0,'11140.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(102,'DOBRALICA DE PORTA DE MADERA GOL/BRANCA','100.00','2025-09-06','2025-09-06','102','Activo','2025-09-06','true',100,NULL,0,'11359.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(103,'DOBRALISA 5/8-10-PCS','100.00','2025-09-06','2025-09-06','103','Activo','2025-09-06','true',100,NULL,0,'11141.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(104,'DOBRALISA DE MADERA 3/8  10 PCS MEDO','100.00','2025-09-06','2025-09-06','104','Activo','2025-09-06','true',100,NULL,0,'11490.0',1,1,1,22,1,1,NULL,'true','Nao Enviar Ticket',0,1),(105,'DULENT 1L -FEDEX','100.00','2025-09-06','2025-09-06','105','Activo','2025-09-06','true',100,NULL,0,'11472.0',1,1,1,23,1,1,NULL,'true','Nao Enviar Ticket',0,1),(106,'DULENT 1L NATIONAL','100.00','2025-09-06','2025-09-06','106','Activo','2025-09-06','true',100,NULL,0,'11470.0',1,1,1,23,1,1,NULL,'true','Nao Enviar Ticket',0,1),(107,'DULENT 1L OSCAR','100.00','2025-09-06','2025-09-06','107','Activo','2025-09-06','true',100,NULL,0,'11377.0',1,1,1,23,1,1,NULL,'true','Nao Enviar Ticket',0,1),(108,'DULENT 4 LITRO OSCAR','100.00','2025-09-06','2025-09-06','108','Activo','2025-09-06','true',100,NULL,0,'11345.0',1,1,1,23,1,1,NULL,'true','Nao Enviar Ticket',0,1),(109,'DULENT 4L FIDEX','100.00','2025-09-06','2025-09-06','109','Activo','2025-09-06','true',100,NULL,0,'11473.0',1,1,1,23,1,1,NULL,'true','Nao Enviar Ticket',0,1),(110,'DULENT 6-LITRO-MAIS FINA','100.00','2025-09-06','2025-09-06','110','Activo','2025-09-06','true',100,NULL,0,'11357.0',1,1,1,23,1,1,NULL,'true','Nao Enviar Ticket',0,1),(111,'ELECTRO BOMBA 0.5HP','100.00','2025-09-06','2025-09-06','111','Activo','2025-09-06','true',100,NULL,0,'11142.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(112,'ELECTRO BOMBA 1.0HP','100.00','2025-09-06','2025-09-06','112','Activo','2025-09-06','true',100,NULL,0,'11143.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(113,'ELECTRO BOMBA 1.5HP','100.00','2025-09-06','2025-09-06','113','Activo','2025-09-06','true',100,NULL,0,'11144.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(114,'ELECTRO BOMBA 2.0HP','100.00','2025-09-06','2025-09-06','114','Activo','2025-09-06','true',100,NULL,0,'11145.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(115,'ELECTRO BOMBA 2.5 HP','100.00','2025-09-06','2025-09-06','115','Activo','2025-09-06','true',100,NULL,0,'11323.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(116,'ELECTROD 2.5M FINOTE','100.00','2025-09-06','2025-09-06','116','Activo','2025-09-06','true',100,NULL,0,'11114.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(117,'ELECTROD 2.5MM CHINA 1.1/2','100.00','2025-09-06','2025-09-06','117','Activo','2025-09-06','true',100,NULL,0,'11148.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(118,'ELECTROD 2.5MM SHARP {CHI','100.00','2025-09-06','2025-09-06','118','Activo','2025-09-06','true',100,NULL,0,'11146.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(119,'ELECTROD 2.5MM-SHARP/ORG/','100.00','2025-09-06','2025-09-06','119','Activo','2025-09-06','true',100,NULL,0,'11147.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(120,'ELECTROD CHINA 1','100.00','2025-09-06','2025-09-06','120','Activo','2025-09-06','true',100,NULL,0,'11358.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(121,'ENCHADA-GRANDE-304 LB','100.00','2025-09-06','2025-09-06','121','Activo','2025-09-06','true',100,NULL,0,'11150.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(122,'ENCHADA-PEQENHO-2.5LBS','100.00','2025-09-06','2025-09-06','122','Activo','2025-09-06','true',100,NULL,0,'11149.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(123,'ENGACHO DE CARRO DE MAO','100.00','2025-09-06','2025-09-06','123','Activo','2025-09-06','true',100,NULL,0,'11421.0',1,1,1,24,1,1,NULL,'true','Nao Enviar Ticket',0,1),(124,'ERRO GRAVAÇÃO EM FATURA INCOMPLETA.','100.00','2025-09-06','2025-09-06','124','Activo','2025-09-06','true',100,NULL,0,'ERRGRV1',1,1,1,25,1,1,NULL,'true','Nao Enviar Ticket',0,1),(125,'ESCADA 7 STEP','100.00','2025-09-06','2025-09-06','125','Activo','2025-09-06','true',100,NULL,0,'11415.0',1,1,1,26,1,1,NULL,'true','Nao Enviar Ticket',0,1),(126,'ESCADA 8 STEP','100.00','2025-09-06','2025-09-06','126','Activo','2025-09-06','true',100,NULL,0,'11416.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(127,'ESCOPE','100.00','2025-09-06','2025-09-06','127','Activo','2025-09-06','true',100,NULL,0,'11151.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(128,'ESCOVA','100.00','2025-09-06','2025-09-06','128','Activo','2025-09-06','true',100,NULL,0,'11174.0',1,1,1,27,1,1,NULL,'true','Nao Enviar Ticket',0,1),(129,'ESPATULA 2','100.00','2025-09-06','2025-09-06','129','Activo','2025-09-06','true',100,NULL,0,'11293.0',1,1,1,28,1,1,NULL,'true','Nao Enviar Ticket',0,1),(130,'ESPATULA 3','100.00','2025-09-06','2025-09-06','130','Activo','2025-09-06','true',100,NULL,0,'11273.0',1,1,1,28,1,1,NULL,'true','Nao Enviar Ticket',0,1),(131,'ESPUMA GRANDRE','100.00','2025-09-06','2025-09-06','131','Activo','2025-09-06','true',100,NULL,0,'11152.0',1,1,1,29,1,1,NULL,'true','Nao Enviar Ticket',0,1),(132,'ESPUMA-NORMAL','100.00','2025-09-06','2025-09-06','132','Activo','2025-09-06','true',100,NULL,0,'11173.0',1,1,1,29,1,1,NULL,'true','Nao Enviar Ticket',0,1),(133,'ESPUMA-PEQHENO','100.00','2025-09-06','2025-09-06','133','Activo','2025-09-06','true',100,NULL,0,'11308.0',1,1,1,30,1,1,NULL,'true','Nao Enviar Ticket',0,1),(134,'EXCENTOR 1 KG','100.00','2025-09-06','2025-09-06','134','Activo','2025-09-06','true',100,NULL,0,'11548.0',1,1,1,30,1,1,NULL,'true','Nao Enviar Ticket',0,1),(135,'EXTENSAO 10M','100.00','2025-09-06','2025-09-06','135','Activo','2025-09-06','true',100,NULL,0,'11384.0',1,1,1,31,1,1,NULL,'true','Nao Enviar Ticket',0,1),(136,'EXTENSAO 20M','100.00','2025-09-06','2025-09-06','136','Activo','2025-09-06','true',100,NULL,0,'11386.0',1,1,1,31,1,1,NULL,'true','Nao Enviar Ticket',0,1),(137,'EXTENSAO 30M','100.00','2025-09-06','2025-09-06','137','Activo','2025-09-06','true',100,NULL,0,'11387.0',1,1,1,31,1,1,NULL,'true','Nao Enviar Ticket',0,1),(138,'EXTENSAO 40M','100.00','2025-09-06','2025-09-06','138','Activo','2025-09-06','true',100,NULL,0,'11388.0',1,1,1,31,1,1,NULL,'true','Nao Enviar Ticket',0,1),(139,'EXTENSAO 50M','100.00','2025-09-06','2025-09-06','139','Activo','2025-09-06','true',100,NULL,0,'11389.0',1,1,1,31,1,1,NULL,'true','Nao Enviar Ticket',0,1),(140,'EXTENSAO GERMANY','100.00','2025-09-06','2025-09-06','140','Activo','2025-09-06','true',100,NULL,0,'11383.0',1,1,1,31,1,1,NULL,'true','Nao Enviar Ticket',0,1),(141,'EXTENSAO GERMANY 2ND','100.00','2025-09-06','2025-09-06','141','Activo','2025-09-06','true',100,NULL,0,'11429.0',1,1,1,31,1,1,NULL,'true','Nao Enviar Ticket',0,1),(142,'EXTENSAO PO QUIMICO 12KG','100.00','2025-09-06','2025-09-06','142','Activo','2025-09-06','true',100,NULL,0,'11422.0',1,1,1,31,1,1,NULL,'true','Nao Enviar Ticket',0,1),(143,'EXTENSAO15M','100.00','2025-09-06','2025-09-06','143','Activo','2025-09-06','true',100,NULL,0,'11385.0',1,1,1,31,1,1,NULL,'true','Nao Enviar Ticket',0,1),(144,'EXTINTOR PO QUIMICO 2KG','100.00','2025-09-06','2025-09-06','144','Activo','2025-09-06','true',100,NULL,0,'11418.0',1,1,1,32,1,1,NULL,'true','Nao Enviar Ticket',0,1),(145,'EXTINTOR PO QUIMICO 4KG','100.00','2025-09-06','2025-09-06','145','Activo','2025-09-06','true',100,NULL,0,'11419.0',1,1,1,32,1,1,NULL,'true','Nao Enviar Ticket',0,1),(146,'FECHADORA   AMARELO {CHI','100.00','2025-09-06','2025-09-06','146','Activo','2025-09-06','true',100,NULL,0,'11153.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(147,'FECHADORA  AZUL PORTU..','100.00','2025-09-06','2025-09-06','147','Activo','2025-09-06','true',100,NULL,0,'11154.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(148,'FECHADORA  AZULE SHO-SIZE/','100.00','2025-09-06','2025-09-06','148','Activo','2025-09-06','true',100,NULL,0,'11155.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(149,'FECHADORA  BRANCO-CD','100.00','2025-09-06','2025-09-06','149','Activo','2025-09-06','true',100,NULL,0,'11156.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(150,'FECHADORA  BUNECO {CHI','100.00','2025-09-06','2025-09-06','150','Activo','2025-09-06','true',100,NULL,0,'11157.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(151,'FECHADORA AMARELA-FERRAGENS','100.00','2025-09-06','2025-09-06','151','Activo','2025-09-06','true',100,NULL,0,'11484.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(152,'FECHADORA DE MADERA','100.00','2025-09-06','2025-09-06','152','Activo','2025-09-06','true',100,NULL,0,'11270.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(153,'FECHADORA DE MADERA -NORMAL','100.00','2025-09-06','2025-09-06','153','Activo','2025-09-06','true',100,NULL,0,'11483.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(154,'FECHADORA DE MADERA 1ST','100.00','2025-09-06','2025-09-06','154','Activo','2025-09-06','true',100,NULL,0,'11160.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(155,'FECHADORA DE MADERA 2ND','100.00','2025-09-06','2025-09-06','155','Activo','2025-09-06','true',100,NULL,0,'11306.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(156,'FECHADORA DE MADERA-BOLA','100.00','2025-09-06','2025-09-06','156','Activo','2025-09-06','true',100,NULL,0,'11300.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(157,'FECHADORA-BRA/BANDIRA','100.00','2025-09-06','2025-09-06','157','Activo','2025-09-06','true',100,NULL,0,'11363.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(158,'FECHADURA DE BONECO -2ND','100.00','2025-09-06','2025-09-06','158','Activo','2025-09-06','true',100,NULL,0,'11255.0',1,1,1,33,1,1,NULL,'true','Nao Enviar Ticket',0,1),(159,'FECHO 3/8 X 1/2  ----- 24 PCS','100.00','2025-09-06','2025-09-06','159','Activo','2025-09-06','true',100,NULL,0,'11503.0',1,1,1,34,1,1,NULL,'true','Nao Enviar Ticket',0,1),(160,'FECHO 3/8 X 1/2 -------12 PCS','100.00','2025-09-06','2025-09-06','160','Activo','2025-09-06','true',100,NULL,0,'11361.0',1,1,1,34,1,1,NULL,'true','Nao Enviar Ticket',0,1),(161,'FECHO 3/8*1/2 ..........10 PCS','100.00','2025-09-06','2025-09-06','161','Activo','2025-09-06','true',100,NULL,0,'11158.0',1,1,1,34,1,1,NULL,'true','Nao Enviar Ticket',0,1),(162,'FECHO 3/8*3/4.......... 12 PCS','100.00','2025-09-06','2025-09-06','162','Activo','2025-09-06','true',100,NULL,0,'11159.0',1,1,1,34,1,1,NULL,'true','Nao Enviar Ticket',0,1),(163,'FECHO 4/8*1','100.00','2025-09-06','2025-09-06','163','Activo','2025-09-06','true',100,NULL,0,'11316.0',1,1,1,34,1,1,NULL,'true','Nao Enviar Ticket',0,1),(164,'FECHO 5/16 X 1/2 ----- 12 PCS','100.00','2025-09-06','2025-09-06','164','Activo','2025-09-06','true',100,NULL,0,'11161.0',1,1,1,34,1,1,NULL,'true','Nao Enviar Ticket',0,1),(165,'FECHO 5/16*1/2-----20 PCS','100.00','2025-09-06','2025-09-06','165','Activo','2025-09-06','true',100,NULL,0,'11116.0',1,1,1,34,1,1,NULL,'true','Nao Enviar Ticket',0,1),(166,'FICH 3 BOCA','100.00','2025-09-06','2025-09-06','166','Activo','2025-09-06','true',100,NULL,0,'11547.0',1,1,1,35,1,1,NULL,'true','Nao Enviar Ticket',0,1),(167,'FICH 5 BOCA','100.00','2025-09-06','2025-09-06','167','Activo','2025-09-06','true',100,NULL,0,'11543.0',1,1,1,35,1,1,NULL,'true','Nao Enviar Ticket',0,1),(168,'FIO DE CONSTRUCAO-GRANDE','100.00','2025-09-06','2025-09-06','168','Activo','2025-09-06','true',100,NULL,0,'11436.0',1,1,1,36,1,1,NULL,'true','Nao Enviar Ticket',0,1),(169,'FIO DE CONSTRUSAO 210D-48','100.00','2025-09-06','2025-09-06','169','Activo','2025-09-06','true',100,NULL,0,'11176.0',1,1,1,36,1,1,NULL,'true','Nao Enviar Ticket',0,1),(170,'FIO DE ELETRICO 2X2.5 MM','100.00','2025-09-06','2025-09-06','170','Activo','2025-09-06','true',100,NULL,0,'11546.0',1,1,1,36,1,1,NULL,'true','Nao Enviar Ticket',0,1),(171,'FITA COLA','100.00','2025-09-06','2025-09-06','171','Activo','2025-09-06','true',100,NULL,0,'11162.0',1,1,1,38,1,1,NULL,'true','Nao Enviar Ticket',0,1),(172,'FITA DE TINTA (4.8) 5 METRO','100.00','2025-09-06','2025-09-06','172','Activo','2025-09-06','true',100,NULL,0,'11551.0',1,1,1,38,1,1,NULL,'true','Nao Enviar Ticket',0,1),(173,'FITA DE TINTA 20- METRO','100.00','2025-09-06','2025-09-06','173','Activo','2025-09-06','true',100,NULL,0,'11523.0',1,1,1,38,1,1,NULL,'true','Nao Enviar Ticket',0,1),(174,'FITA DE TINTA 30- METRO','100.00','2025-09-06','2025-09-06','174','Activo','2025-09-06','true',100,NULL,0,'11522.0',1,1,1,38,1,1,NULL,'true','Nao Enviar Ticket',0,1),(175,'FITA ISOLADOR 10-YARD','100.00','2025-09-06','2025-09-06','175','Activo','2025-09-06','true',100,NULL,0,'11510.0',1,1,1,38,1,1,NULL,'true','Nao Enviar Ticket',0,1),(176,'FITA ISOLADOR 5-YARD','100.00','2025-09-06','2025-09-06','176','Activo','2025-09-06','true',100,NULL,0,'11509.0',1,1,1,38,1,1,NULL,'true','Nao Enviar Ticket',0,1),(177,'FITAMETRICA 10M','100.00','2025-09-06','2025-09-06','177','Activo','2025-09-06','true',100,NULL,0,'11431.0',1,1,1,38,1,1,NULL,'true','Nao Enviar Ticket',0,1),(178,'FITAMETRICA 7.5M','100.00','2025-09-06','2025-09-06','178','Activo','2025-09-06','true',100,NULL,0,'11423.0',1,1,1,38,1,1,NULL,'true','Nao Enviar Ticket',0,1),(179,'FOLIHA DE SERA -------3RD','100.00','2025-09-06','2025-09-06','179','Activo','2025-09-06','true',100,NULL,0,'11354.0',1,1,1,37,1,1,NULL,'true','Nao Enviar Ticket',0,1),(180,'FOLIHA DE SERA .....1ST','100.00','2025-09-06','2025-09-06','180','Activo','2025-09-06','true',100,NULL,0,'11163.0',1,1,1,37,1,1,NULL,'true','Nao Enviar Ticket',0,1),(181,'FOLIHA DE SERA .....2ND','100.00','2025-09-06','2025-09-06','181','Activo','2025-09-06','true',100,NULL,0,'11164.0',1,1,1,37,1,1,NULL,'true','Nao Enviar Ticket',0,1),(182,'GAMELA -PEQENO','100.00','2025-09-06','2025-09-06','182','Activo','2025-09-06','true',100,NULL,0,'11165.0',1,1,1,39,1,1,NULL,'true','Nao Enviar Ticket',0,1),(183,'GAMELA GRANDE','100.00','2025-09-06','2025-09-06','183','Activo','2025-09-06','true',100,NULL,0,'11166.0',1,1,1,39,1,1,NULL,'true','Nao Enviar Ticket',0,1),(184,'GEIA-PEQHENO','100.00','2025-09-06','2025-09-06','184','Activo','2025-09-06','true',100,NULL,0,'11167.0',1,1,1,40,1,1,NULL,'true','Nao Enviar Ticket',0,1),(185,'GIEA GRANDE','100.00','2025-09-06','2025-09-06','185','Activo','2025-09-06','true',100,NULL,0,'11168.0',1,1,1,40,1,1,NULL,'true','Nao Enviar Ticket',0,1),(186,'GRAMPO 200*250 DIS MONTADO','100.00','2025-09-06','2025-09-06','186','Activo','2025-09-06','true',100,NULL,0,'11271.0',1,1,1,41,1,1,NULL,'true','Nao Enviar Ticket',0,1),(187,'GRAMPO 200*250-DOBRADO','100.00','2025-09-06','2025-09-06','187','Activo','2025-09-06','true',100,NULL,0,'11170.0',1,1,1,41,1,1,NULL,'true','Nao Enviar Ticket',0,1),(188,'GRAMPO 200*250-MONTADO','100.00','2025-09-06','2025-09-06','188','Activo','2025-09-06','true',100,NULL,0,'11169.0',1,1,1,41,1,1,NULL,'true','Nao Enviar Ticket',0,1),(189,'LAMBADA 105 WAT-BRANCO','100.00','2025-09-06','2025-09-06','189','Activo','2025-09-06','true',100,NULL,0,'11172.0',1,1,1,42,1,1,NULL,'true','Nao Enviar Ticket',0,1),(190,'LAMPADA 18W','100.00','2025-09-06','2025-09-06','190','Activo','2025-09-06','true',100,NULL,0,'11433.0',1,1,1,42,1,1,NULL,'true','Nao Enviar Ticket',0,1),(191,'LAMPADA 30W','100.00','2025-09-06','2025-09-06','191','Activo','2025-09-06','true',100,NULL,0,'11435.0',1,1,1,42,1,1,NULL,'true','Nao Enviar Ticket',0,1),(192,'LAMPADA 40W','100.00','2025-09-06','2025-09-06','192','Activo','2025-09-06','true',100,NULL,0,'11434.0',1,1,1,42,1,1,NULL,'true','Nao Enviar Ticket',0,1),(193,'LAMPADA 75-W','100.00','2025-09-06','2025-09-06','193','Activo','2025-09-06','true',100,NULL,0,'11474.0',1,1,1,42,1,1,NULL,'true','Nao Enviar Ticket',0,1),(194,'LAMPADA 85-W','100.00','2025-09-06','2025-09-06','194','Activo','2025-09-06','true',100,NULL,0,'11475.0',1,1,1,42,1,1,NULL,'true','Nao Enviar Ticket',0,1),(195,'LAMPADA 9W','100.00','2025-09-06','2025-09-06','195','Activo','2025-09-06','true',100,NULL,0,'11432.0',1,1,1,42,1,1,NULL,'true','Nao Enviar Ticket',0,1),(196,'LAVALOICA DOIS BOCA','100.00','2025-09-06','2025-09-06','196','Activo','2025-09-06','true',100,NULL,0,'11373.0',1,1,1,43,1,1,NULL,'true','Nao Enviar Ticket',0,1),(197,'LAVALOICA UMA BOCA','100.00','2025-09-06','2025-09-06','197','Activo','2025-09-06','true',100,NULL,0,'11175.0',1,1,1,43,1,1,NULL,'true','Nao Enviar Ticket',0,1),(198,'LAVATORIO COM CULUNA','100.00','2025-09-06','2025-09-06','198','Activo','2025-09-06','true',100,NULL,0,'11376.0',1,1,1,43,1,1,NULL,'true','Nao Enviar Ticket',0,1),(199,'LUVAS AMARELA 105G','100.00','2025-09-06','2025-09-06','199','Activo','2025-09-06','true',100,NULL,0,'11412.0',1,1,1,44,1,1,NULL,'true','Nao Enviar Ticket',0,1),(200,'LUVAS PVC','100.00','2025-09-06','2025-09-06','200','Activo','2025-09-06','true',100,NULL,0,'11294.0',1,1,1,44,1,1,NULL,'true','Nao Enviar Ticket',0,1),(201,'LUVAS PVC 180GR','100.00','2025-09-06','2025-09-06','201','Activo','2025-09-06','true',100,NULL,0,'11413.0',1,1,1,44,1,1,NULL,'true','Nao Enviar Ticket',0,1),(202,'LUVAS PVC 50GR','100.00','2025-09-06','2025-09-06','202','Activo','2025-09-06','true',100,NULL,0,'11414.0',1,1,1,44,1,1,NULL,'true','Nao Enviar Ticket',0,1),(203,'MACACAO LARANJA','100.00','2025-09-06','2025-09-06','203','Activo','2025-09-06','true',100,NULL,0,'11393.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(204,'MACETA 1.0KG','100.00','2025-09-06','2025-09-06','204','Activo','2025-09-06','true',100,NULL,0,'11181.0',1,1,1,45,1,1,NULL,'true','Nao Enviar Ticket',0,1),(205,'MACETA 1.5 KG','100.00','2025-09-06','2025-09-06','205','Activo','2025-09-06','true',100,NULL,0,'11420.0',1,1,1,45,1,1,NULL,'true','Nao Enviar Ticket',0,1),(206,'MACETA 2.0 KG','100.00','2025-09-06','2025-09-06','206','Activo','2025-09-06','true',100,NULL,0,'11396.0',1,1,1,45,1,1,NULL,'true','Nao Enviar Ticket',0,1),(207,'MACHADO 1000G','100.00','2025-09-06','2025-09-06','207','Activo','2025-09-06','true',100,NULL,0,'11394.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(208,'MACO DE BORRACHA 8','100.00','2025-09-06','2025-09-06','208','Activo','2025-09-06','true',100,NULL,0,'11113.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(209,'MAGUINA DE SOLDA 2000W','100.00','2025-09-06','2025-09-06','209','Activo','2025-09-06','true',100,NULL,0,'11370.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(210,'MANGEIRA 1/2X40M','100.00','2025-09-06','2025-09-06','210','Activo','2025-09-06','true',100,NULL,0,'11403.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(211,'MANGERA DE AGUA 3/4','100.00','2025-09-06','2025-09-06','211','Activo','2025-09-06','true',100,NULL,0,'11406.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(212,'MANGERA DE AGUA 3/4 25 METRO','100.00','2025-09-06','2025-09-06','212','Activo','2025-09-06','true',100,NULL,0,'11177.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(213,'MANGERA DE AGUA 3/4 50 METRO','100.00','2025-09-06','2025-09-06','213','Activo','2025-09-06','true',100,NULL,0,'11178.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(214,'MANGERA DE NIVEL 10MM-50 METRO','100.00','2025-09-06','2025-09-06','214','Activo','2025-09-06','true',100,NULL,0,'11497.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(215,'MANGERA DE SAIDA 100 METRO','100.00','2025-09-06','2025-09-06','215','Activo','2025-09-06','true',100,NULL,0,'11180.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(216,'MANGERA DE SAIDA 50 METRO','100.00','2025-09-06','2025-09-06','216','Activo','2025-09-06','true',100,NULL,0,'11179.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(217,'MANGUEIRA 3/4X40M','100.00','2025-09-06','2025-09-06','217','Activo','2025-09-06','true',100,NULL,0,'11404.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(218,'MANGUEIRA 3/4X50M','100.00','2025-09-06','2025-09-06','218','Activo','2025-09-06','true',100,NULL,0,'11405.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(219,'MANGUEIRA DE GAS','100.00','2025-09-06','2025-09-06','219','Activo','2025-09-06','true',100,NULL,0,'11459.0',1,1,1,46,1,1,NULL,'true','Nao Enviar Ticket',0,1),(220,'MAQUINA DE SOLDA 130 APH','100.00','2025-09-06','2025-09-06','220','Activo','2025-09-06','true',100,NULL,0,'11561.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(221,'MARRETA 10 KG','100.00','2025-09-06','2025-09-06','221','Activo','2025-09-06','true',100,NULL,0,'11382.0',1,1,1,47,1,1,NULL,'true','Nao Enviar Ticket',0,1),(222,'MARRETA 3.0KG','100.00','2025-09-06','2025-09-06','222','Activo','2025-09-06','true',100,NULL,0,'11117.0',1,1,1,47,1,1,NULL,'true','Nao Enviar Ticket',0,1),(223,'MARRETA 4.0KG','100.00','2025-09-06','2025-09-06','223','Activo','2025-09-06','true',100,NULL,0,'11128.0',1,1,1,47,1,1,NULL,'true','Nao Enviar Ticket',0,1),(224,'MARRETA 5.0KG','100.00','2025-09-06','2025-09-06','224','Activo','2025-09-06','true',100,NULL,0,'11365.0',1,1,1,47,1,1,NULL,'true','Nao Enviar Ticket',0,1),(225,'MARRETA 6.0KG','100.00','2025-09-06','2025-09-06','225','Activo','2025-09-06','true',100,NULL,0,'11129.0',1,1,1,47,1,1,NULL,'true','Nao Enviar Ticket',0,1),(226,'MARRETA 7.0 KG','100.00','2025-09-06','2025-09-06','226','Activo','2025-09-06','true',100,NULL,0,'11381.0',1,1,1,47,1,1,NULL,'true','Nao Enviar Ticket',0,1),(227,'MARTELO-GRA','100.00','2025-09-06','2025-09-06','227','Activo','2025-09-06','true',100,NULL,0,'11130.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(228,'MARTELO-PEQN','100.00','2025-09-06','2025-09-06','228','Activo','2025-09-06','true',100,NULL,0,'11107.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(229,'MASETA 1.5KG','100.00','2025-09-06','2025-09-06','229','Activo','2025-09-06','true',100,NULL,0,'11182.0',1,1,1,48,1,1,NULL,'true','Nao Enviar Ticket',0,1),(230,'MASETA 2.0KG','100.00','2025-09-06','2025-09-06','230','Activo','2025-09-06','true',100,NULL,0,'11183.0',1,1,1,48,1,1,NULL,'true','Nao Enviar Ticket',0,1),(231,'MOSAICO 40X40-44001','100.00','2025-09-06','2025-09-06','231','Activo','2025-09-06','true',100,NULL,0,'11437.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(232,'MOSAICO 40X40-44002','100.00','2025-09-06','2025-09-06','232','Activo','2025-09-06','true',100,NULL,0,'11438.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(233,'MOSAICO 40X40-44003','100.00','2025-09-06','2025-09-06','233','Activo','2025-09-06','true',100,NULL,0,'11439.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(234,'MOSAICO 40X40-44005','100.00','2025-09-06','2025-09-06','234','Activo','2025-09-06','true',100,NULL,0,'11441.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(235,'MOSAICO ANTI DERAPANTE 40X40--0007','100.00','2025-09-06','2025-09-06','235','Activo','2025-09-06','true',100,NULL,0,'11457.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(236,'MOSAICO ANTI-DERAPANTE 40X40--0006','100.00','2025-09-06','2025-09-06','236','Activo','2025-09-06','true',100,NULL,0,'11456.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(237,'MOSAICO ANTI-DERAPANTE 40X40-0005','100.00','2025-09-06','2025-09-06','237','Activo','2025-09-06','true',100,NULL,0,'11455.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(238,'MOSAICO DE QUINTAL 40X40 --0004','100.00','2025-09-06','2025-09-06','238','Activo','2025-09-06','true',100,NULL,0,'11454.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(239,'MOSAICO DE QUINTAL 40X40-0002','100.00','2025-09-06','2025-09-06','239','Activo','2025-09-06','true',100,NULL,0,'11452.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(240,'MOSICO 60X60-66001','100.00','2025-09-06','2025-09-06','240','Activo','2025-09-06','true',100,NULL,0,'11476.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(241,'MOSICO 60X60-66005','100.00','2025-09-06','2025-09-06','241','Activo','2025-09-06','true',100,NULL,0,'11478.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(242,'MOSICO 60X60-66007','100.00','2025-09-06','2025-09-06','242','Activo','2025-09-06','true',100,NULL,0,'11477.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(243,'MOSICO DE QUINTAL 40X40 --0003','100.00','2025-09-06','2025-09-06','243','Activo','2025-09-06','true',100,NULL,0,'11453.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(244,'MOZICO AZULET','100.00','2025-09-06','2025-09-06','244','Activo','2025-09-06','true',100,NULL,0,'11184.0',1,1,1,49,1,1,NULL,'true','Nao Enviar Ticket',0,1),(245,'NIVEL 100 CM','100.00','2025-09-06','2025-09-06','245','Activo','2025-09-06','true',100,NULL,0,'11301.0',1,1,1,50,1,1,NULL,'true','Nao Enviar Ticket',0,1),(246,'NIVEL 30 CM','100.00','2025-09-06','2025-09-06','246','Activo','2025-09-06','true',100,NULL,0,'11137.0',1,1,1,50,1,1,NULL,'true','Nao Enviar Ticket',0,1),(247,'NIVEL 40 CM','100.00','2025-09-06','2025-09-06','247','Activo','2025-09-06','true',100,NULL,0,'11297.0',1,1,1,50,1,1,NULL,'true','Nao Enviar Ticket',0,1),(248,'NIVEL 50 CM','100.00','2025-09-06','2025-09-06','248','Activo','2025-09-06','true',100,NULL,0,'11295.0',1,1,1,50,1,1,NULL,'true','Nao Enviar Ticket',0,1),(249,'NIVEL 60 CM','100.00','2025-09-06','2025-09-06','249','Activo','2025-09-06','true',100,NULL,0,'11185.0',1,1,1,50,1,1,NULL,'true','Nao Enviar Ticket',0,1),(250,'NIVEL 70 CM','100.00','2025-09-06','2025-09-06','250','Activo','2025-09-06','true',100,NULL,0,'11298.0',1,1,1,50,1,1,NULL,'true','Nao Enviar Ticket',0,1),(251,'NIVEL 80 CM','100.00','2025-09-06','2025-09-06','251','Activo','2025-09-06','true',100,NULL,0,'11186.0',1,1,1,50,1,1,NULL,'true','Nao Enviar Ticket',0,1),(252,'OCULOS DE SEGURANCA','100.00','2025-09-06','2025-09-06','252','Activo','2025-09-06','true',100,NULL,0,'11565.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(253,'OTETO FALSO-BIG','100.00','2025-09-06','2025-09-06','253','Activo','2025-09-06','true',100,NULL,0,'11187.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(254,'PA COMPRIDA -12 PCS 2ND','100.00','2025-09-06','2025-09-06','254','Activo','2025-09-06','true',100,NULL,0,'11307.0',1,1,1,51,1,1,NULL,'true','Nao Enviar Ticket',0,1),(255,'PA COMPRIDA 12 PCS 1ST','100.00','2025-09-06','2025-09-06','255','Activo','2025-09-06','true',100,NULL,0,'11188.0',1,1,1,51,1,1,NULL,'true','Nao Enviar Ticket',0,1),(256,'PA CURTA 12-PCS','100.00','2025-09-06','2025-09-06','256','Activo','2025-09-06','true',100,NULL,0,'11189.0',1,1,1,51,1,1,NULL,'true','Nao Enviar Ticket',0,1),(257,'PALUSTRA','100.00','2025-09-06','2025-09-06','257','Activo','2025-09-06','true',100,NULL,0,'11313.0',1,1,1,52,1,1,NULL,'true','Nao Enviar Ticket',0,1),(258,'PARAFUSO 6.3*60','100.00','2025-09-06','2025-09-06','258','Activo','2025-09-06','true',100,NULL,0,'11132.0',1,1,1,53,1,1,NULL,'true','Nao Enviar Ticket',0,1),(259,'PARAFUSO PRETO 3.5 X 25 (250)-PCS','100.00','2025-09-06','2025-09-06','259','Activo','2025-09-06','true',100,NULL,0,'11320.0',1,1,1,53,1,1,NULL,'true','Nao Enviar Ticket',0,1),(260,'PARAFUSO PRETO 3.5 X 32 (200)-PCS','100.00','2025-09-06','2025-09-06','260','Activo','2025-09-06','true',100,NULL,0,'11319.0',1,1,1,53,1,1,NULL,'true','Nao Enviar Ticket',0,1),(261,'PARAFUSO PRETO 3.5 X 38MM(150)-PCS','100.00','2025-09-06','2025-09-06','261','Activo','2025-09-06','true',100,NULL,0,'11318.0',1,1,1,53,1,1,NULL,'true','Nao Enviar Ticket',0,1),(262,'PARAFUSO TORADO 4.0 X 16MM (150)-PCS','100.00','2025-09-06','2025-09-06','262','Activo','2025-09-06','true',100,NULL,0,'11136.0',1,1,1,53,1,1,NULL,'true','Nao Enviar Ticket',0,1),(263,'PARAFUSUE 6.3*60 150 PCS PACOTE','100.00','2025-09-06','2025-09-06','263','Activo','2025-09-06','true',100,NULL,0,'11190.0',1,1,1,53,1,1,NULL,'true','Nao Enviar Ticket',0,1),(264,'PARAFUSUE 6.3*70 150 PCS PACOTE','100.00','2025-09-06','2025-09-06','264','Activo','2025-09-06','true',100,NULL,0,'11191.0',1,1,1,53,1,1,NULL,'true','Nao Enviar Ticket',0,1),(265,'PARAFUSUE ESCREW 3.5*35-PRETA','100.00','2025-09-06','2025-09-06','265','Activo','2025-09-06','true',100,NULL,0,'11314.0',1,1,1,53,1,1,NULL,'true','Nao Enviar Ticket',0,1),(266,'PENCEL 1.5 \"','100.00','2025-09-06','2025-09-06','266','Activo','2025-09-06','true',100,NULL,0,'11524.0',1,1,1,54,1,1,NULL,'true','Nao Enviar Ticket',0,1),(267,'PENCEL 2.0 \"','100.00','2025-09-06','2025-09-06','267','Activo','2025-09-06','true',100,NULL,0,'11525.0',1,1,1,54,1,1,NULL,'true','Nao Enviar Ticket',0,1),(268,'PENCEL 2.5 \"','100.00','2025-09-06','2025-09-06','268','Activo','2025-09-06','true',100,NULL,0,'11526.0',1,1,1,54,1,1,NULL,'true','Nao Enviar Ticket',0,1),(269,'PENCEL 3.0 \"','100.00','2025-09-06','2025-09-06','269','Activo','2025-09-06','true',100,NULL,0,'11527.0',1,1,1,54,1,1,NULL,'true','Nao Enviar Ticket',0,1),(270,'PENCEL 4.0 \"','100.00','2025-09-06','2025-09-06','270','Activo','2025-09-06','true',100,NULL,0,'11540.0',1,1,1,54,1,1,NULL,'true','Nao Enviar Ticket',0,1),(271,'PENCEL 5\"','100.00','2025-09-06','2025-09-06','271','Activo','2025-09-06','true',100,NULL,0,'11541.0',1,1,1,54,1,1,NULL,'true','Nao Enviar Ticket',0,1),(272,'PIKARET 1.8KG-PEQEHNO','100.00','2025-09-06','2025-09-06','272','Activo','2025-09-06','true',100,NULL,0,'11487.0',1,1,1,55,1,1,NULL,'true','Nao Enviar Ticket',0,1),(273,'PIKARET 2.0KG','100.00','2025-09-06','2025-09-06','273','Activo','2025-09-06','true',100,NULL,0,'11194.0',1,1,1,55,1,1,NULL,'true','Nao Enviar Ticket',0,1),(274,'PIKARET 2.5KG','100.00','2025-09-06','2025-09-06','274','Activo','2025-09-06','true',100,NULL,0,'11195.0',1,1,1,54,1,1,NULL,'true','Nao Enviar Ticket',0,1),(275,'PINCEL 1','100.00','2025-09-06','2025-09-06','275','Activo','2025-09-06','true',100,NULL,0,'11272.0',1,1,1,54,1,1,NULL,'true','Nao Enviar Ticket',0,1),(276,'PINCEL 2','100.00','2025-09-06','2025-09-06','276','Activo','2025-09-06','true',100,NULL,0,'11261.0',1,1,1,54,1,1,NULL,'true','Nao Enviar Ticket',0,1),(277,'PISTOLA DE PINTURA ELECTRICA','100.00','2025-09-06','2025-09-06','277','Activo','2025-09-06','true',100,NULL,0,'11573.0',1,1,1,56,1,1,NULL,'true','Nao Enviar Ticket',0,1),(278,'PISTOLA DE SILCONE','100.00','2025-09-06','2025-09-06','278','Activo','2025-09-06','true',100,NULL,0,'11291.0',1,1,1,56,1,1,NULL,'true','Nao Enviar Ticket',0,1),(279,'PNEU COMJANTE','100.00','2025-09-06','2025-09-06','279','Activo','2025-09-06','true',100,NULL,0,'11192.0',1,1,1,57,1,1,NULL,'true','Nao Enviar Ticket',0,1),(280,'PNEU SEM JANTE','100.00','2025-09-06','2025-09-06','280','Activo','2025-09-06','true',100,NULL,0,'11193.0',1,1,1,57,1,1,NULL,'true','Nao Enviar Ticket',0,1),(281,'PONTERO 1ST','100.00','2025-09-06','2025-09-06','281','Activo','2025-09-06','true',100,NULL,0,'11196.0',1,1,1,58,1,1,NULL,'true','Nao Enviar Ticket',0,1),(282,'PONTERO 2ND','100.00','2025-09-06','2025-09-06','282','Activo','2025-09-06','true',100,NULL,0,'11197.0',1,1,1,58,1,1,NULL,'true','Nao Enviar Ticket',0,1),(283,'PREGO DE ACO 10 CM','100.00','2025-09-06','2025-09-06','283','Activo','2025-09-06','true',100,NULL,0,'11202.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(284,'PREGO DE ACO 3 CM','100.00','2025-09-06','2025-09-06','284','Activo','2025-09-06','true',100,NULL,0,'11264.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(285,'PREGO DE ACO 5 CM','100.00','2025-09-06','2025-09-06','285','Activo','2025-09-06','true',100,NULL,0,'11198.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(286,'PREGO DE ACO 6 CM','100.00','2025-09-06','2025-09-06','286','Activo','2025-09-06','true',100,NULL,0,'11199.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(287,'PREGO DE ACO 7 CM','100.00','2025-09-06','2025-09-06','287','Activo','2025-09-06','true',100,NULL,0,'11200.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(288,'PREGO DE ACO 8 CM','100.00','2025-09-06','2025-09-06','288','Activo','2025-09-06','true',100,NULL,0,'11201.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(289,'PREGO DE ACO NO-3','100.00','2025-09-06','2025-09-06','289','Activo','2025-09-06','true',100,NULL,0,'11266.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(290,'PREGO DE CHAPA','100.00','2025-09-06','2025-09-06','290','Activo','2025-09-06','true',100,NULL,0,'11203.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(291,'PREGO NO-2 1ST','100.00','2025-09-06','2025-09-06','291','Activo','2025-09-06','true',100,NULL,0,'11274.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(292,'PREGO QUADRADO  5  CM','100.00','2025-09-06','2025-09-06','292','Activo','2025-09-06','true',100,NULL,0,'11204.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(293,'PREGO QUADRADO  6  CM','100.00','2025-09-06','2025-09-06','293','Activo','2025-09-06','true',100,NULL,0,'11205.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(294,'PREGO QUADRADO  7  CM','100.00','2025-09-06','2025-09-06','294','Activo','2025-09-06','true',100,NULL,0,'11206.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(295,'PREGO QUADRADO  8  CM','100.00','2025-09-06','2025-09-06','295','Activo','2025-09-06','true',100,NULL,0,'11207.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(296,'PREGO QUADRADO 10 CM','100.00','2025-09-06','2025-09-06','296','Activo','2025-09-06','true',100,NULL,0,'11208.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(297,'PREGO QUADRADO 12 CM','100.00','2025-09-06','2025-09-06','297','Activo','2025-09-06','true',100,NULL,0,'11265.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(298,'PREGO REDONDO N-2','100.00','2025-09-06','2025-09-06','298','Activo','2025-09-06','true',100,NULL,0,'11321.0',1,1,1,59,1,1,NULL,'true','Nao Enviar Ticket',0,1),(299,'QULET','100.00','2025-09-06','2025-09-06','299','Activo','2025-09-06','true',100,NULL,0,'11315.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(300,'REBARBADEIRA 2000-WAT','100.00','2025-09-06','2025-09-06','300','Activo','2025-09-06','true',100,NULL,0,'11559.0',1,1,1,60,1,1,NULL,'true','Nao Enviar Ticket',0,1),(301,'REBARBADEIRA 600-WAT','100.00','2025-09-06','2025-09-06','301','Activo','2025-09-06','true',100,NULL,0,'11556.0',1,1,1,60,1,1,NULL,'true','Nao Enviar Ticket',0,1),(302,'REBARBADEIRA 710-WAT','100.00','2025-09-06','2025-09-06','302','Activo','2025-09-06','true',100,NULL,0,'11557.0',1,1,1,60,1,1,NULL,'true','Nao Enviar Ticket',0,1),(303,'REBARBADEIRA 800W','100.00','2025-09-06','2025-09-06','303','Activo','2025-09-06','true',100,NULL,0,'11569.0',1,1,1,60,1,1,NULL,'true','Nao Enviar Ticket',0,1),(304,'REBARBADEIRA 900W','100.00','2025-09-06','2025-09-06','304','Activo','2025-09-06','true',100,NULL,0,'11568.0',1,1,1,60,1,1,NULL,'true','Nao Enviar Ticket',0,1),(305,'REBARBADEIRA 950W','100.00','2025-09-06','2025-09-06','305','Activo','2025-09-06','true',100,NULL,0,'11567.0',1,1,1,60,1,1,NULL,'true','Nao Enviar Ticket',0,1),(306,'REBARBADERA 2000W','100.00','2025-09-06','2025-09-06','306','Activo','2025-09-06','true',100,NULL,0,'11368.0',1,1,1,60,1,1,NULL,'true','Nao Enviar Ticket',0,1),(307,'REBITE 4.8 X 16MM (100)-PCS','100.00','2025-09-06','2025-09-06','307','Activo','2025-09-06','true',100,NULL,0,'11262.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(308,'REDE 3/4X1MX25MX3.3KG','100.00','2025-09-06','2025-09-06','308','Activo','2025-09-06','true',100,NULL,0,'11409.0',1,1,1,61,1,1,NULL,'true','Nao Enviar Ticket',0,1),(309,'REDE 4MMX4MMX1MX10MX9.5KG NET','100.00','2025-09-06','2025-09-06','309','Activo','2025-09-06','true',100,NULL,0,'11410.0',1,1,1,61,1,1,NULL,'true','Nao Enviar Ticket',0,1),(310,'REDE DE FUBA 2X4MMX1MX10M','100.00','2025-09-06','2025-09-06','310','Activo','2025-09-06','true',100,NULL,0,'11411.0',1,1,1,61,1,1,NULL,'true','Nao Enviar Ticket',0,1),(311,'REDEY PLASTICO','100.00','2025-09-06','2025-09-06','311','Activo','2025-09-06','true',100,NULL,0,'11209.0',1,1,1,61,1,1,NULL,'true','Nao Enviar Ticket',0,1),(312,'REDUTOR DE GAS','100.00','2025-09-06','2025-09-06','312','Activo','2025-09-06','true',100,NULL,0,'11408.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(313,'REGUA 2.0M SEM NIVEL-1ST','100.00','2025-09-06','2025-09-06','313','Activo','2025-09-06','true',100,NULL,0,'11210.0',1,1,1,62,1,1,NULL,'true','Nao Enviar Ticket',0,1),(314,'REGUA 2.0M-SEN NIVEL-2ND','100.00','2025-09-06','2025-09-06','314','Activo','2025-09-06','true',100,NULL,0,'11275.0',1,1,1,62,1,1,NULL,'true','Nao Enviar Ticket',0,1),(315,'REGUA 2.5M-SEM NIVEL','100.00','2025-09-06','2025-09-06','315','Activo','2025-09-06','true',100,NULL,0,'11211.0',1,1,1,62,1,1,NULL,'true','Nao Enviar Ticket',0,1),(316,'REGUA 3.0M SEM NIVEL-1ST','100.00','2025-09-06','2025-09-06','316','Activo','2025-09-06','true',100,NULL,0,'11212.0',1,1,1,62,1,1,NULL,'true','Nao Enviar Ticket',0,1),(317,'REGUA 3.0M-SEN NIVEL-2ND','100.00','2025-09-06','2025-09-06','317','Activo','2025-09-06','true',100,NULL,0,'11268.0',1,1,1,62,1,1,NULL,'true','Nao Enviar Ticket',0,1),(318,'RODANA 60','100.00','2025-09-06','2025-09-06','318','Activo','2025-09-06','true',100,NULL,0,'11310.0',1,1,1,63,1,1,NULL,'true','Nao Enviar Ticket',0,1),(319,'RODANA 70','100.00','2025-09-06','2025-09-06','319','Activo','2025-09-06','true',100,NULL,0,'11214.0',1,1,1,63,1,1,NULL,'true','Nao Enviar Ticket',0,1),(320,'RODANA 80','100.00','2025-09-06','2025-09-06','320','Activo','2025-09-06','true',100,NULL,0,'11215.0',1,1,1,63,1,1,NULL,'true','Nao Enviar Ticket',0,1),(321,'ROLLO DE ANTENA 50M','100.00','2025-09-06','2025-09-06','321','Activo','2025-09-06','true',100,NULL,0,'11367.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(322,'ROLLO DE LIXA 120','100.00','2025-09-06','2025-09-06','322','Activo','2025-09-06','true',100,NULL,0,'11494.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(323,'ROLLO DE LIXA 60','100.00','2025-09-06','2025-09-06','323','Activo','2025-09-06','true',100,NULL,0,'11492.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(324,'ROLLO DE LIXA 60MM','100.00','2025-09-06','2025-09-06','324','Activo','2025-09-06','true',100,NULL,0,'11292.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(325,'ROLLO DE LIXA 80','100.00','2025-09-06','2025-09-06','325','Activo','2025-09-06','true',100,NULL,0,'11493.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(326,'ROLLO DE LIXA 80MM','100.00','2025-09-06','2025-09-06','326','Activo','2025-09-06','true',100,NULL,0,'11258.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(327,'ROLLO DE PINTAR 4','100.00','2025-09-06','2025-09-06','327','Activo','2025-09-06','true',100,NULL,0,'11360.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(328,'ROLLO DE PINTAR-DE OLIO','100.00','2025-09-06','2025-09-06','328','Activo','2025-09-06','true',100,NULL,0,'11325.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(329,'ROLO DE PINTAR 6','100.00','2025-09-06','2025-09-06','329','Activo','2025-09-06','true',100,NULL,0,'11217.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(330,'ROLO DE PINTAR 9','100.00','2025-09-06','2025-09-06','330','Activo','2025-09-06','true',100,NULL,0,'11216.0',1,1,1,64,1,1,NULL,'true','Nao Enviar Ticket',0,1),(331,'SACO-AZUL-BRANCO-NORMAL','100.00','2025-09-06','2025-09-06','331','Activo','2025-09-06','true',100,NULL,0,'11506.0',1,1,1,65,1,1,NULL,'true','Nao Enviar Ticket',0,1),(332,'SACO-AZUL-BRANCO-PEQHNO','100.00','2025-09-06','2025-09-06','332','Activo','2025-09-06','true',100,NULL,0,'11505.0',1,1,1,65,1,1,NULL,'true','Nao Enviar Ticket',0,1),(333,'SACO-OTRAS CORE-PEQHNO','100.00','2025-09-06','2025-09-06','333','Activo','2025-09-06','true',100,NULL,0,'11508.0',1,1,1,65,1,1,NULL,'true','Nao Enviar Ticket',0,1),(334,'SACO-OTROS CORE-NORMAL','100.00','2025-09-06','2025-09-06','334','Activo','2025-09-06','true',100,NULL,0,'11507.0',1,1,1,65,1,1,NULL,'true','Nao Enviar Ticket',0,1),(335,'SACO-PRETO-BRANCO-PEQHNO','100.00','2025-09-06','2025-09-06','335','Activo','2025-09-06','true',100,NULL,0,'11504.0',1,1,1,65,1,1,NULL,'true','Nao Enviar Ticket',0,1),(336,'SANEFA BRACA 2.5','100.00','2025-09-06','2025-09-06','336','Activo','2025-09-06','true',100,NULL,0,'11397.0',1,1,1,66,1,1,NULL,'true','Nao Enviar Ticket',0,1),(337,'SANEFA BRANCA 1.5M','100.00','2025-09-06','2025-09-06','337','Activo','2025-09-06','true',100,NULL,0,'11395.0',1,1,1,66,1,1,NULL,'true','Nao Enviar Ticket',0,1),(338,'SANEFA BRANCA 3.0M','100.00','2025-09-06','2025-09-06','338','Activo','2025-09-06','true',100,NULL,0,'11398.0',1,1,1,66,1,1,NULL,'true','Nao Enviar Ticket',0,1),(339,'SANEFA CASTANHA 1.5M','100.00','2025-09-06','2025-09-06','339','Activo','2025-09-06','true',100,NULL,0,'11399.0',1,1,1,66,1,1,NULL,'true','Nao Enviar Ticket',0,1),(340,'SANEFA CASTANHA 2.0M','100.00','2025-09-06','2025-09-06','340','Activo','2025-09-06','true',100,NULL,0,'11400.0',1,1,1,66,1,1,NULL,'true','Nao Enviar Ticket',0,1),(341,'SANEFA CASTANHA 2.5M','100.00','2025-09-06','2025-09-06','341','Activo','2025-09-06','true',100,NULL,0,'11401.0',1,1,1,66,1,1,NULL,'true','Nao Enviar Ticket',0,1),(342,'SANEFA CASTANHA 3.0M','100.00','2025-09-06','2025-09-06','342','Activo','2025-09-06','true',100,NULL,0,'11402.0',1,1,1,66,1,1,NULL,'true','Nao Enviar Ticket',0,1),(343,'SANITA COMPLETA','100.00','2025-09-06','2025-09-06','343','Activo','2025-09-06','true',100,NULL,0,'11375.0',1,1,1,67,1,1,NULL,'true','Nao Enviar Ticket',0,1),(344,'SANITA SEM TAMPA','100.00','2025-09-06','2025-09-06','344','Activo','2025-09-06','true',100,NULL,0,'11374.0',1,1,1,67,1,1,NULL,'true','Nao Enviar Ticket',0,1),(345,'SERROTE DE MADERA 533MM','100.00','2025-09-06','2025-09-06','345','Activo','2025-09-06','true',100,NULL,0,'11139.0',1,1,1,68,1,1,NULL,'true','Nao Enviar Ticket',0,1),(346,'SERROTE-400MM-MEDIO-SUPER-CUT','100.00','2025-09-06','2025-09-06','346','Activo','2025-09-06','true',100,NULL,0,'11560.0',1,1,1,68,1,1,NULL,'true','Nao Enviar Ticket',0,1),(347,'SERROTE-450MM-MEDIO','100.00','2025-09-06','2025-09-06','347','Activo','2025-09-06','true',100,NULL,0,'11558.0',1,1,1,68,1,1,NULL,'true','Nao Enviar Ticket',0,1),(348,'SILCONE-BRANCO','100.00','2025-09-06','2025-09-06','348','Activo','2025-09-06','true',100,NULL,0,'11218.0',1,1,1,69,1,1,NULL,'true','Nao Enviar Ticket',0,1),(349,'SILCONE-PRETO','100.00','2025-09-06','2025-09-06','349','Activo','2025-09-06','true',100,NULL,0,'11219.0',1,1,1,69,1,1,NULL,'true','Nao Enviar Ticket',0,1),(350,'SILCONE-TRANSPARENTE','100.00','2025-09-06','2025-09-06','350','Activo','2025-09-06','true',100,NULL,0,'11220.0',1,1,1,69,1,1,NULL,'true','Nao Enviar Ticket',0,1),(351,'SUPORTO-GOLDENLINE','100.00','2025-09-06','2025-09-06','351','Activo','2025-09-06','true',100,NULL,0,'11513.0',1,1,1,70,1,1,NULL,'true','Nao Enviar Ticket',0,1),(352,'SUPORTO-NORMAL','100.00','2025-09-06','2025-09-06','352','Activo','2025-09-06','true',100,NULL,0,'11512.0',1,1,1,70,1,1,NULL,'true','Nao Enviar Ticket',0,1),(353,'SUPORTO-PRETO','100.00','2025-09-06','2025-09-06','353','Activo','2025-09-06','true',100,NULL,0,'11511.0',1,1,1,70,1,1,NULL,'true','Nao Enviar Ticket',0,1),(354,'SUPORTO-SILVERLINE','100.00','2025-09-06','2025-09-06','354','Activo','2025-09-06','true',100,NULL,0,'11514.0',1,1,1,70,1,1,NULL,'true','Nao Enviar Ticket',0,1),(355,'TALOCHA MID-1ST','100.00','2025-09-06','2025-09-06','355','Activo','2025-09-06','true',100,NULL,0,'11221.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(356,'TAMPA DE SANITA','100.00','2025-09-06','2025-09-06','356','Activo','2025-09-06','true',100,NULL,0,'11468.0',1,1,1,71,1,1,NULL,'true','Nao Enviar Ticket',0,1),(357,'TEST','100.00','2025-09-06','2025-09-06','357','Activo','2025-09-06','true',100,NULL,0,'1000.0',1,1,1,1,1,1,NULL,'true','Nao Enviar Ticket',0,1),(358,'TINTA  DE AGUA 20L ATLANTA VERDE','100.00','2025-09-06','2025-09-06','358','Activo','2025-09-06','true',100,NULL,0,'11231.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(359,'TINTA 20KG  EMILIA  LARANJA','100.00','2025-09-06','2025-09-06','359','Activo','2025-09-06','true',100,NULL,0,'11339.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(360,'TINTA 20KG EMILIA  AMARELO','100.00','2025-09-06','2025-09-06','360','Activo','2025-09-06','true',100,NULL,0,'11223.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(361,'TINTA 20KG EMILIA  AZUL','100.00','2025-09-06','2025-09-06','361','Activo','2025-09-06','true',100,NULL,0,'11224.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(362,'TINTA 20KG EMILIA  BRANCO','100.00','2025-09-06','2025-09-06','362','Activo','2025-09-06','true',100,NULL,0,'11225.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(363,'TINTA 20KG EMILIA  CREAM','100.00','2025-09-06','2025-09-06','363','Activo','2025-09-06','true',100,NULL,0,'11226.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(364,'TINTA 20KG EMILIA  VERDY','100.00','2025-09-06','2025-09-06','364','Activo','2025-09-06','true',100,NULL,0,'11228.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(365,'TINTA 20KG EMILIA CIZENTO','100.00','2025-09-06','2025-09-06','365','Activo','2025-09-06','true',100,NULL,0,'11327.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(366,'TINTA 20L EMILIA  TIJOLO','100.00','2025-09-06','2025-09-06','366','Activo','2025-09-06','true',100,NULL,0,'11227.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(367,'TINTA 4L OSCAR  AZULE','100.00','2025-09-06','2025-09-06','367','Activo','2025-09-06','true',100,NULL,0,'11267.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(368,'TINTA 4L OSCAR 555-VERDY','100.00','2025-09-06','2025-09-06','368','Activo','2025-09-06','true',100,NULL,0,'11238.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(369,'TINTA 4L OSCAR 800-BRANCO','100.00','2025-09-06','2025-09-06','369','Activo','2025-09-06','true',100,NULL,0,'11237.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(370,'TINTA 4L OSCAR VERMELO','100.00','2025-09-06','2025-09-06','370','Activo','2025-09-06','true',100,NULL,0,'11276.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(371,'TINTA 4L OSCAR-296 CIZENTO','100.00','2025-09-06','2025-09-06','371','Activo','2025-09-06','true',100,NULL,0,'11229.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(372,'TINTA 4L OSCAR-345 AMARELHO','100.00','2025-09-06','2025-09-06','372','Activo','2025-09-06','true',100,NULL,0,'11230.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(373,'TINTA 4L OSCAR-743  CASTANHO','100.00','2025-09-06','2025-09-06','373','Activo','2025-09-06','true',100,NULL,0,'11233.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(374,'TINTA 4L OSCAR-812  CREAM','100.00','2025-09-06','2025-09-06','374','Activo','2025-09-06','true',100,NULL,0,'11235.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(375,'TINTA 4L OSCAR-890  PRETO','100.00','2025-09-06','2025-09-06','375','Activo','2025-09-06','true',100,NULL,0,'11236.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(376,'TINTA CASA DE LUXO 15L AMARELA','100.00','2025-09-06','2025-09-06','376','Activo','2025-09-06','true',100,NULL,0,'11239.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(377,'TINTA CASA DE LUXO 15L AZUL','100.00','2025-09-06','2025-09-06','377','Activo','2025-09-06','true',100,NULL,0,'11240.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(378,'TINTA CASA DE LUXO 15L BRANCA','100.00','2025-09-06','2025-09-06','378','Activo','2025-09-06','true',100,NULL,0,'11241.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(379,'TINTA CASA DE LUXO 15L CINZENTO','100.00','2025-09-06','2025-09-06','379','Activo','2025-09-06','true',100,NULL,0,'11242.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(380,'TINTA CASA DE LUXO 15L CREAM','100.00','2025-09-06','2025-09-06','380','Activo','2025-09-06','true',100,NULL,0,'11246.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(381,'TINTA CASA DE LUXO 15L LARAJA','100.00','2025-09-06','2025-09-06','381','Activo','2025-09-06','true',100,NULL,0,'11243.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(382,'TINTA CASA DE LUXO 15L LILAS','100.00','2025-09-06','2025-09-06','382','Activo','2025-09-06','true',100,NULL,0,'11244.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(383,'TINTA CASA DE LUXO 15L ROSA','100.00','2025-09-06','2025-09-06','383','Activo','2025-09-06','true',100,NULL,0,'11245.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(384,'TINTA CASA DE LUXO 15L TIJOLO','100.00','2025-09-06','2025-09-06','384','Activo','2025-09-06','true',100,NULL,0,'11247.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(385,'TINTA CASA DE LUXO 15L VERDY','100.00','2025-09-06','2025-09-06','385','Activo','2025-09-06','true',100,NULL,0,'11248.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(386,'TINTA DE AGUA 20L ATLANTA AZUL','100.00','2025-09-06','2025-09-06','386','Activo','2025-09-06','true',100,NULL,0,'11462.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(387,'TINTA DE AGUA 20L ATLANTA BRANCA','100.00','2025-09-06','2025-09-06','387','Activo','2025-09-06','true',100,NULL,0,'11330.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(388,'TINTA DE AGUA 20L ATLANTA CREAM','100.00','2025-09-06','2025-09-06','388','Activo','2025-09-06','true',100,NULL,0,'11331.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(389,'TINTA DE AGUA 20L ATLANTA-CINZENTO','100.00','2025-09-06','2025-09-06','389','Activo','2025-09-06','true',100,NULL,0,'11496.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(390,'TINTA DE AGUA 20L ATLANTA-TIJOLO','100.00','2025-09-06','2025-09-06','390','Activo','2025-09-06','true',100,NULL,0,'11495.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(391,'TINTA DE AGUA 20L EMILIA-LARANJA','100.00','2025-09-06','2025-09-06','391','Activo','2025-09-06','true',100,NULL,0,'11499.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(392,'TINTA DE AGUA 20L EMILIA-ROSA','100.00','2025-09-06','2025-09-06','392','Activo','2025-09-06','true',100,NULL,0,'11488.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(393,'TINTA DE AGUA 20L-EMILIA TIJOLO','100.00','2025-09-06','2025-09-06','393','Activo','2025-09-06','true',100,NULL,0,'11341.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(394,'TINTA DE AGUA DE CORE-BRANCA','100.00','2025-09-06','2025-09-06','394','Activo','2025-09-06','true',100,NULL,0,'11533.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(395,'TINTA DE AGUA DE CORE-CINZA','100.00','2025-09-06','2025-09-06','395','Activo','2025-09-06','true',100,NULL,0,'11535.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(396,'TINTA DE AGUA DE CORE-CREAM','100.00','2025-09-06','2025-09-06','396','Activo','2025-09-06','true',100,NULL,0,'11534.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(397,'TINTA DE OLEO 4KG-296-CIZ-ATLANTA','100.00','2025-09-06','2025-09-06','397','Activo','2025-09-06','true',100,NULL,0,'11283.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(398,'TINTA DE OLEO 4KG-345-AMA- ATLANTA','100.00','2025-09-06','2025-09-06','398','Activo','2025-09-06','true',100,NULL,0,'11279.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(399,'TINTA DE OLEO 4KG-555-VER-ATLANTA','100.00','2025-09-06','2025-09-06','399','Activo','2025-09-06','true',100,NULL,0,'11287.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(400,'TINTA DE OLEO 4KG-670-VERM-ATLANTA','100.00','2025-09-06','2025-09-06','400','Activo','2025-09-06','true',100,NULL,0,'11286.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(401,'TINTA DE OLEO 4KG-743-CAS- ATLANTA','100.00','2025-09-06','2025-09-06','401','Activo','2025-09-06','true',100,NULL,0,'11282.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(402,'TINTA DE OLEO 4KG-800-BRA-ATLANTA','100.00','2025-09-06','2025-09-06','402','Activo','2025-09-06','true',100,NULL,0,'11281.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(403,'TINTA DE OLEO 4KG-800-REAL','100.00','2025-09-06','2025-09-06','403','Activo','2025-09-06','true',100,NULL,0,'11280.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(404,'TINTA DE OLEO 4KG-812-CRM-ATLANTA','100.00','2025-09-06','2025-09-06','404','Activo','2025-09-06','true',100,NULL,0,'11284.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(405,'TINTA DE OLEO 4KG-890-PRT-ATLANTA','100.00','2025-09-06','2025-09-06','405','Activo','2025-09-06','true',100,NULL,0,'11285.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(406,'TINTA DE OLEO 4L-440-AZUL ATLANTA','100.00','2025-09-06','2025-09-06','406','Activo','2025-09-06','true',100,NULL,0,'11278.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(407,'TINTA DE OLIO 1L EMILIA-BRANCA','100.00','2025-09-06','2025-09-06','407','Activo','2025-09-06','true',100,NULL,0,'11338.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(408,'TINTA DE OLIO 1L OSCAR AMARELO','100.00','2025-09-06','2025-09-06','408','Activo','2025-09-06','true',100,NULL,0,'11344.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(409,'TINTA DE OLIO 1L OSCAR AZUL','100.00','2025-09-06','2025-09-06','409','Activo','2025-09-06','true',100,NULL,0,'11351.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(410,'TINTA DE OLIO 1L OSCAR BRANCA','100.00','2025-09-06','2025-09-06','410','Activo','2025-09-06','true',100,NULL,0,'11352.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(411,'TINTA DE OLIO 1L OSCAR CASTAGNO','100.00','2025-09-06','2025-09-06','411','Activo','2025-09-06','true',100,NULL,0,'11349.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(412,'TINTA DE OLIO 1L OSCAR CINZENTO','100.00','2025-09-06','2025-09-06','412','Activo','2025-09-06','true',100,NULL,0,'11348.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(413,'TINTA DE OLIO 1L OSCAR CREAM','100.00','2025-09-06','2025-09-06','413','Activo','2025-09-06','true',100,NULL,0,'11346.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(414,'TINTA DE OLIO 1L OSCAR PRETO','100.00','2025-09-06','2025-09-06','414','Activo','2025-09-06','true',100,NULL,0,'11347.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(415,'TINTA DE OLIO 1L OSCAR VERDY','100.00','2025-09-06','2025-09-06','415','Activo','2025-09-06','true',100,NULL,0,'11343.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(416,'TINTA DE OLIO 1L OSCAR VERMELO','100.00','2025-09-06','2025-09-06','416','Activo','2025-09-06','true',100,NULL,0,'11350.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(417,'TINTA DE OLIO 1L OSCAR- CHECOLET','100.00','2025-09-06','2025-09-06','417','Activo','2025-09-06','true',100,NULL,0,'11326.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(418,'TINTA DE OLIO 4L CASA DE LUXO-BRANCA','100.00','2025-09-06','2025-09-06','418','Activo','2025-09-06','true',100,NULL,0,'11329.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(419,'TINTA DE OLIO 4L CASA DE LUXO-CINZA','100.00','2025-09-06','2025-09-06','419','Activo','2025-09-06','true',100,NULL,0,'11342.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(420,'TINTA DE OLIO 4L CASA DE LUXO-PRETA','100.00','2025-09-06','2025-09-06','420','Activo','2025-09-06','true',100,NULL,0,'11332.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(421,'TINTA DE OLIO 4L EMILIA BRANCA','100.00','2025-09-06','2025-09-06','421','Activo','2025-09-06','true',100,NULL,0,'11234.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(422,'TINTA DE OLIO 4L EMILIA CASTAGNO','100.00','2025-09-06','2025-09-06','422','Activo','2025-09-06','true',100,NULL,0,'11328.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(423,'TINTA DE OLIO 4L EMILIA PRETO','100.00','2025-09-06','2025-09-06','423','Activo','2025-09-06','true',100,NULL,0,'11334.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(424,'TINTA DE OLIO 4L EMILIA-CREAM','100.00','2025-09-06','2025-09-06','424','Activo','2025-09-06','true',100,NULL,0,'11340.0',1,1,1,76,1,1,NULL,'true','Nao Enviar Ticket',0,1),(425,'TUBO PVC 110 MM','100.00','2025-09-06','2025-09-06','425','Activo','2025-09-06','true',100,NULL,0,'11450.0',1,1,1,72,1,1,NULL,'true','Nao Enviar Ticket',0,1),(426,'TUBO PVC 50 MM','100.00','2025-09-06','2025-09-06','426','Activo','2025-09-06','true',100,NULL,0,'11451.0',1,1,1,72,1,1,NULL,'true','Nao Enviar Ticket',0,1),(427,'TUBO PVC 90MM','100.00','2025-09-06','2025-09-06','427','Activo','2025-09-06','true',100,NULL,0,'11449.0',1,1,1,72,1,1,NULL,'true','Nao Enviar Ticket',0,1),(428,'VASORA -PEQUENO','100.00','2025-09-06','2025-09-06','428','Activo','2025-09-06','true',100,NULL,0,'11491.0',1,1,1,73,1,1,NULL,'true','Nao Enviar Ticket',0,1),(429,'VASSORA MID','100.00','2025-09-06','2025-09-06','429','Activo','2025-09-06','true',100,NULL,0,'11378.0',1,1,1,73,1,1,NULL,'true','Nao Enviar Ticket',0,1),(430,'VERNIS 1L- OSCAR','100.00','2025-09-06','2025-09-06','430','Activo','2025-09-06','true',100,NULL,0,'11324.0',1,1,1,74,1,1,NULL,'true','Nao Enviar Ticket',0,1),(431,'VERNISH 4L OSCAR','100.00','2025-09-06','2025-09-06','431','Activo','2025-09-06','true',100,NULL,0,'11480.0',1,1,1,74,1,1,NULL,'true','Nao Enviar Ticket',0,1),(432,'ZARCAO 1KG EMILIA','100.00','2025-09-06','2025-09-06','432','Activo','2025-09-06','true',100,NULL,0,'11253.0',1,1,1,75,1,1,NULL,'true','Nao Enviar Ticket',0,1),(433,'ZARCAO 1L NATIONAL','100.00','2025-09-06','2025-09-06','433','Activo','2025-09-06','true',100,NULL,0,'11304.0',1,1,1,75,1,1,NULL,'true','Nao Enviar Ticket',0,1),(434,'ZARCAO 1L OSCAR','100.00','2025-09-06','2025-09-06','434','Activo','2025-09-06','true',100,NULL,0,'11232.0',1,1,1,75,1,1,NULL,'true','Nao Enviar Ticket',0,1),(435,'ZARCAO 4KG  OSCAR','100.00','2025-09-06','2025-09-06','435','Activo','2025-09-06','true',100,NULL,0,'11277.0',1,1,1,75,1,1,NULL,'true','Nao Enviar Ticket',0,1),(436,'ZARCAO 4KG NATIONAL','100.00','2025-09-06','2025-09-06','436','Activo','2025-09-06','true',100,NULL,0,'11254.0',1,1,1,75,1,1,NULL,'true','Nao Enviar Ticket',0,1),(437,'ZARCAO 4L OSCAR-CINZENTO','100.00','2025-09-06','2025-09-06','437','Activo','2025-09-06','true',100,NULL,0,'11222.0',1,1,1,75,1,1,NULL,'true','Nao Enviar Ticket',0,1),(438,'ZARCAO-4KG-EMILIA','100.00','2025-09-06','2025-09-06','438','Activo','2025-09-06','true',100,NULL,0,'11337.0',1,1,1,75,1,1,NULL,'true','Nao Enviar Ticket',0,1),(439,'Teste Novo Produto1','1000.00','2025-09-07','2025-09-07','439','Activo','2025-09-07','true',1000,0,0,'439',1,1,1,2,1,1,NULL,'false','Nao Enviar Ticket',0,1),(440,'Novo Produto 01','12000.00','2025-09-08','2025-09-08','440','Activo','2025-09-08','true',12000,0,0,'',1,1,1,2,1,1,NULL,'false','Nao Enviar Ticket',0,1);
/*!40000 ALTER TABLE `tb_produto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_profissao`
--

DROP TABLE IF EXISTS `tb_profissao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_profissao` (
  `idProfissao` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` int(11) DEFAULT NULL,
  PRIMARY KEY (`idProfissao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_profissao`
--

LOCK TABLES `tb_profissao` WRITE;
/*!40000 ALTER TABLE `tb_profissao` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_profissao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_provincia`
--

DROP TABLE IF EXISTS `tb_provincia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_provincia` (
  `idProvincia` int(11) NOT NULL AUTO_INCREMENT,
  `descrisao` varchar(45) DEFAULT NULL,
  `idPais` int(11) DEFAULT NULL,
  PRIMARY KEY (`idProvincia`),
  KEY `FkPais` (`idPais`),
  CONSTRAINT `FkPais` FOREIGN KEY (`idPais`) REFERENCES `tb_pais` (`idPais`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_provincia`
--

LOCK TABLES `tb_provincia` WRITE;
/*!40000 ALTER TABLE `tb_provincia` DISABLE KEYS */;
INSERT INTO `tb_provincia` VALUES (1,'LUANDA',1),(2,'HUAMBO',1),(3,'BENGUELA',1),(4,'HUILA',1),(5,'CABINDA',1),(6,'BENGO',1),(7,'BIÉ',1),(8,'CUNENE',1),(9,'MOXICO',1);
/*!40000 ALTER TABLE `tb_provincia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_saida_vasilhame`
--

DROP TABLE IF EXISTS `tb_saida_vasilhame`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_saida_vasilhame` (
  `pk_saida_vasilhame` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `data_saida` date NOT NULL,
  `hora_saida` time NOT NULL,
  `fk_armazem` int(10) unsigned NOT NULL DEFAULT '1',
  `fk_usuario` int(10) NOT NULL,
  `nome_funcionario` varchar(45) NOT NULL,
  PRIMARY KEY (`pk_saida_vasilhame`),
  KEY `fk_tb_saida_vasilhame_armazem` (`fk_armazem`),
  KEY `fk_tb_saida_vasilhame_usuario` (`fk_usuario`),
  CONSTRAINT `fk_tb_saida_vasilhame_armazem` FOREIGN KEY (`fk_armazem`) REFERENCES `tb_armazem` (`codigo`),
  CONSTRAINT `fk_tb_saida_vasilhame_usuario` FOREIGN KEY (`fk_usuario`) REFERENCES `tb_usuario` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_saida_vasilhame`
--

LOCK TABLES `tb_saida_vasilhame` WRITE;
/*!40000 ALTER TABLE `tb_saida_vasilhame` DISABLE KEYS */;
