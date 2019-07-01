-- MySQL dump 10.13  Distrib 5.1.48, for Win32 (ia32)
--
-- Host: localhost    Database: SAIKRUPA_DB_PROD_01012019
-- ------------------------------------------------------
-- Server version	5.1.48-community

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `ADDRESS_CODE` int(5) NOT NULL AUTO_INCREMENT,
  `ADDRESS_LINE1` varchar(100) NOT NULL,
  `ADDRESS_LINE2` varchar(100) DEFAULT NULL,
  `ADDRESS_LINE3` varchar(100) DEFAULT NULL,
  `ADDRESS_LANDMARK` varchar(30) DEFAULT NULL,
  `CONTACT_PERSON_CODE` int(5) NOT NULL,
  `CREATED_BY` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ADDRESS_CODE`),
  KEY `ID_CONTACT_PERSON` (`CONTACT_PERSON_CODE`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`CONTACT_PERSON_CODE`) REFERENCES `contact_person` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adhoc_payment_entry`
--

DROP TABLE IF EXISTS `adhoc_payment_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adhoc_payment_entry` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `AD_PAYMENT_ENTRY_NO` int(2) NOT NULL,
  `AD_PAYABLE_AMOUNT` double(10,2) NOT NULL,
  `PAID_AMOUNT` double(10,2) NOT NULL,
  `PAYMENT_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `AD_ADJUSTED` int(1) NOT NULL,
  `CUSTOMER_CODE` int(10) NOT NULL,
  `PAYMENT_MODE` int(4) DEFAULT NULL,
  `CHEQUE_NO` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  KEY `CUSTOMER_CODE` (`CUSTOMER_CODE`),
  CONSTRAINT `adhoc_payment_entry_ibfk_1` FOREIGN KEY (`CUSTOMER_CODE`) REFERENCES `customer` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adhoc_payment_entry`
--

LOCK TABLES `adhoc_payment_entry` WRITE;
/*!40000 ALTER TABLE `adhoc_payment_entry` DISABLE KEYS */;
INSERT INTO `adhoc_payment_entry` VALUES (1000,1,9800.00,4000.00,'2019-01-01 18:30:00',1,1000,1000,NULL),(1001,1,9800.00,9800.00,'2019-01-03 18:30:00',1,1005,1000,NULL);
/*!40000 ALTER TABLE `adhoc_payment_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_role`
--

DROP TABLE IF EXISTS `application_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_role` (
  `CODE` int(11) NOT NULL,
  `NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_role`
--

LOCK TABLES `application_role` WRITE;
/*!40000 ALTER TABLE `application_role` DISABLE KEYS */;
INSERT INTO `application_role` VALUES (1000,'ADMIN_ROLE'),(1001,'EMPLOYEE_ROLE');
/*!40000 ALTER TABLE `application_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_user`
--

DROP TABLE IF EXISTS `application_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_user` (
  `CODE` varchar(10) NOT NULL,
  `PASSCODE` varchar(255) NOT NULL,
  `NAME` varchar(30) NOT NULL,
  `CONTACT` bigint(12) NOT NULL,
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `LAST_MODIFIED_BY` varchar(10) NOT NULL,
  `USER_ROLE` int(11) NOT NULL,
  `LAST_LOGGED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`CODE`),
  KEY `USER_ROLE` (`USER_ROLE`),
  CONSTRAINT `application_user_ibfk_1` FOREIGN KEY (`USER_ROLE`) REFERENCES `application_role` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_user`
--

LOCK TABLES `application_user` WRITE;
/*!40000 ALTER TABLE `application_user` DISABLE KEYS */;
INSERT INTO `application_user` VALUES ('admin','Z8eF4loNOE1JeEjGukAMIQ==','Admin User',9035476943,'2015-12-31 18:30:00','admin',1000,'2015-12-31 18:30:00');
/*!40000 ALTER TABLE `application_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `com_order`
--

DROP TABLE IF EXISTS `com_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `com_order` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `ORDER_STATUS` int(1) NOT NULL,
  `PAYMENT_STATUS` int(1) NOT NULL,
  `DELIVERY_STATUS` int(1) NOT NULL,
  `CUSTOMER_CODE` int(5) NOT NULL,
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATED_BY` varchar(10) NOT NULL,
  `MODIFIED_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `LAST_MODIFIED_BY` varchar(10) NOT NULL,
  PRIMARY KEY (`CODE`),
  KEY `ID_COM_ORDER` (`CODE`),
  KEY `CUSTOMER_CODE` (`CUSTOMER_CODE`),
  CONSTRAINT `com_order_ibfk_1` FOREIGN KEY (`CUSTOMER_CODE`) REFERENCES `customer` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5060 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_order`
--

LOCK TABLES `com_order` WRITE;
/*!40000 ALTER TABLE `com_order` DISABLE KEYS */;
INSERT INTO `com_order` VALUES (5000,0,1,1,1000,'2019-01-01 18:30:00','admin','2019-05-27 07:56:38','admin'),(5001,0,1,1,1001,'2019-01-01 18:30:00','admin','2019-05-27 07:58:14','admin'),(5002,0,1,1,1002,'2019-01-01 18:30:00','admin','2019-05-27 08:00:06','admin'),(5003,0,1,1,1002,'2019-01-01 18:30:00','admin','2019-05-27 08:00:40','admin'),(5004,0,1,1,1003,'2019-01-01 18:30:00','admin','2019-05-27 08:02:02','admin'),(5005,0,1,1,1003,'2019-01-01 18:30:00','admin','2019-05-27 08:04:48','admin'),(5006,0,1,1,1004,'2019-01-02 18:30:00','admin','2019-05-27 08:15:16','admin'),(5007,0,1,1,1004,'2019-01-02 18:30:00','admin','2019-05-27 08:15:41','admin'),(5008,0,2,1,1000,'2019-01-02 18:30:00','admin','2019-05-27 08:18:12','admin'),(5009,0,1,1,1001,'2019-01-02 18:30:00','admin','2019-05-27 08:17:21','admin'),(5010,2,0,1,1005,'2019-01-02 18:30:00','admin','2019-05-27 08:37:09','admin'),(5011,2,0,1,1005,'2019-01-02 18:30:00','admin','2019-05-27 08:37:09','admin'),(5012,0,1,1,1003,'2019-01-02 18:30:00','admin','2019-05-27 08:24:55','admin'),(5013,0,1,1,1006,'2019-01-02 18:30:00','admin','2019-05-27 08:26:07','admin'),(5014,0,1,1,1002,'2019-01-02 18:30:00','admin','2019-05-27 08:27:33','admin'),(5015,0,1,1,1004,'2019-01-03 18:30:00','admin','2019-05-27 08:33:27','admin'),(5016,0,1,1,1004,'2019-01-03 18:30:00','admin','2019-05-27 08:33:51','admin'),(5017,0,1,1,1004,'2019-01-03 18:30:00','admin','2019-05-27 08:34:14','admin'),(5018,0,1,1,1004,'2019-01-03 18:30:00','admin','2019-05-27 08:34:43','admin'),(5019,0,1,1,1007,'2019-01-03 18:30:00','admin','2019-05-27 08:35:55','admin'),(5020,0,1,1,1007,'2019-01-03 18:30:00','admin','2019-05-27 08:36:28','admin'),(5021,0,1,1,1000,'2019-01-04 18:30:00','admin','2019-05-27 08:43:12','admin'),(5022,0,1,1,1008,'2019-01-04 18:30:00','admin','2019-05-27 08:45:47','admin'),(5023,0,1,1,1002,'2019-01-05 18:30:00','admin','2019-05-27 08:47:29','admin'),(5024,0,1,1,1008,'2019-01-05 18:30:00','admin','2019-05-27 08:49:17','admin'),(5025,0,1,1,1009,'2019-01-05 18:30:00','admin','2019-05-27 08:59:38','admin'),(5026,0,1,1,1009,'2019-01-05 18:30:00','admin','2019-05-27 09:00:10','admin'),(5027,0,1,1,1002,'2019-01-06 18:30:00','admin','2019-05-27 09:02:20','admin'),(5028,0,1,1,1002,'2019-01-06 18:30:00','admin','2019-05-27 09:02:45','admin'),(5029,0,1,1,1010,'2019-01-07 18:30:00','admin','2019-05-27 09:05:36','admin'),(5030,0,1,1,1002,'2019-01-09 18:30:00','admin','2019-05-27 09:21:01','admin'),(5031,0,1,1,1002,'2019-01-08 18:30:00','admin','2019-05-27 09:27:36','admin'),(5032,0,1,1,1011,'2019-01-08 18:30:00','admin','2019-05-27 09:29:34','admin'),(5033,0,1,1,1012,'2019-01-08 18:30:00','admin','2019-05-27 09:30:42','admin'),(5034,0,1,1,1012,'2019-01-08 18:30:00','admin','2019-05-27 09:31:17','admin'),(5035,0,1,1,1002,'2019-01-09 18:30:00','admin','2019-05-27 09:34:32','admin'),(5036,0,1,1,1002,'2019-01-09 18:30:00','admin','2019-05-27 09:34:58','admin'),(5037,0,1,1,1010,'2019-01-09 18:30:00','admin','2019-05-27 09:35:33','admin'),(5038,0,1,1,1012,'2019-01-09 18:30:00','admin','2019-05-27 09:37:57','admin'),(5039,0,1,1,1012,'2019-01-09 18:30:00','admin','2019-05-27 09:38:28','admin'),(5040,0,1,1,1011,'2019-01-09 18:30:00','admin','2019-05-27 09:39:24','admin'),(5041,0,1,1,1011,'2019-01-09 18:30:00','admin','2019-05-27 09:40:00','admin'),(5042,0,1,1,1002,'2019-01-10 18:30:00','admin','2019-05-27 09:48:05','admin'),(5043,0,1,1,1002,'2019-01-10 18:30:00','admin','2019-05-27 09:48:36','admin'),(5044,0,1,1,1002,'2019-01-10 18:30:00','admin','2019-05-27 09:49:52','admin'),(5045,0,1,1,1003,'2019-01-10 18:30:00','admin','2019-05-27 09:51:24','admin'),(5046,0,1,1,1010,'2019-01-10 18:30:00','admin','2019-05-27 09:51:58','admin'),(5047,0,1,1,1002,'2019-01-11 18:30:00','admin','2019-05-27 10:06:00','admin'),(5048,0,1,1,1002,'2019-01-11 18:30:00','admin','2019-05-27 10:06:31','admin'),(5049,0,1,1,1002,'2019-01-11 18:30:00','admin','2019-05-27 10:06:58','admin'),(5050,0,1,1,1002,'2019-01-11 18:30:00','admin','2019-05-27 10:07:28','admin'),(5051,0,1,1,1003,'2019-01-11 18:30:00','admin','2019-05-27 10:08:05','admin'),(5052,0,1,1,1010,'2019-01-11 18:30:00','admin','2019-05-27 10:08:39','admin'),(5053,0,1,1,1004,'2019-01-11 18:30:00','admin','2019-05-27 10:09:21','admin'),(5054,0,1,1,1002,'2019-01-12 18:30:00','admin','2019-05-27 10:27:26','admin'),(5055,0,1,1,1002,'2019-01-12 18:30:00','admin','2019-05-27 10:28:07','admin'),(5056,0,1,1,1008,'2019-01-12 18:30:00','admin','2019-05-27 10:30:21','admin'),(5057,0,1,1,1008,'2019-01-12 18:30:00','admin','2019-05-27 10:31:42','admin'),(5058,0,1,1,1004,'2019-01-12 18:30:00','admin','2019-05-27 10:32:38','admin'),(5059,0,1,1,1002,'2019-01-13 18:30:00','admin','2019-05-27 10:36:07','admin');
/*!40000 ALTER TABLE `com_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `com_order_delivery`
--

DROP TABLE IF EXISTS `com_order_delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `com_order_delivery` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `ORDER_CODE` int(5) NOT NULL,
  `DELIVERED_QUANTITY` double(10,2) NOT NULL,
  `ENTRY_NO` int(2) NOT NULL,
  `DELIVERY_RECEIPT_NO` varchar(25) NOT NULL,
  `DELIVERY_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `VEHICLE_CODE` varchar(10) NOT NULL,
  `LAST_MODIFIED_BY` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  KEY `ID_COM_ORDER_DELIVERY` (`CODE`),
  KEY `ORDER_CODE` (`ORDER_CODE`),
  CONSTRAINT `com_order_delivery_ibfk_1` FOREIGN KEY (`ORDER_CODE`) REFERENCES `com_order` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_order_delivery`
--

LOCK TABLES `com_order_delivery` WRITE;
/*!40000 ALTER TABLE `com_order_delivery` DISABLE KEYS */;
/*!40000 ALTER TABLE `com_order_delivery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `com_order_delivery_address`
--

DROP TABLE IF EXISTS `com_order_delivery_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `com_order_delivery_address` (
  `ADDRESS_CODE` int(10) NOT NULL AUTO_INCREMENT,
  `ADDRESS_LINE1` varchar(100) NOT NULL,
  `ADDRESS_LINE2` varchar(100) DEFAULT NULL,
  `ADDRESS_LINE3` varchar(100) DEFAULT NULL,
  `ADDRESS_LANDMARK` varchar(100) DEFAULT NULL,
  `ADDRESS_ZIP` varchar(20) DEFAULT NULL,
  `ORDER_ENTRY_CODE` int(5) NOT NULL,
  `ADDRESS_TYPE` int(1) NOT NULL,
  PRIMARY KEY (`ADDRESS_CODE`),
  KEY `IDX_ORDER_ADDRESS_CODE` (`ADDRESS_CODE`),
  KEY `ORDER_ENTRY_CODE` (`ORDER_ENTRY_CODE`),
  CONSTRAINT `com_order_delivery_address_ibfk_1` FOREIGN KEY (`ORDER_ENTRY_CODE`) REFERENCES `com_order_entry` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1060 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_order_delivery_address`
--

LOCK TABLES `com_order_delivery_address` WRITE;
/*!40000 ALTER TABLE `com_order_delivery_address` DISABLE KEYS */;
INSERT INTO `com_order_delivery_address` VALUES (1000,'Dantapali',NULL,NULL,NULL,NULL,1000,0),(1001,'Sahupada',NULL,NULL,NULL,NULL,1001,0),(1002,'Bhaghasahi',NULL,NULL,NULL,NULL,1002,0),(1003,'Bhaghasahi',NULL,NULL,NULL,NULL,1003,0),(1004,'Jamghati',NULL,NULL,NULL,NULL,1004,0),(1005,'Jamghati',NULL,NULL,NULL,NULL,1005,0),(1006,'Masinapada',NULL,NULL,NULL,NULL,1006,0),(1007,'Masinapada',NULL,NULL,NULL,NULL,1007,0),(1008,'Dantapali',NULL,NULL,NULL,NULL,1008,0),(1009,'Sahupada',NULL,NULL,NULL,NULL,1009,0),(1010,'Thanapada',NULL,NULL,NULL,NULL,1010,0),(1011,'Thanapada',NULL,NULL,NULL,NULL,1011,0),(1012,'Jamghati',NULL,NULL,NULL,NULL,1012,0),(1013,'Jamghati',NULL,NULL,NULL,NULL,1013,0),(1014,'Bahira',NULL,NULL,NULL,NULL,1014,0),(1015,'Masinapada',NULL,NULL,NULL,NULL,1015,0),(1016,'Masinapada',NULL,NULL,NULL,NULL,1016,0),(1017,'Masinapada',NULL,NULL,NULL,NULL,1017,0),(1018,'Masinapada',NULL,NULL,NULL,NULL,1018,0),(1019,'Gudpada',NULL,NULL,NULL,NULL,1019,0),(1020,'Gudpada',NULL,NULL,NULL,NULL,1020,0),(1021,'Dantapali',NULL,NULL,NULL,NULL,1021,0),(1022,'Badikata',NULL,NULL,NULL,NULL,1022,0),(1023,'Bahira',NULL,NULL,NULL,NULL,1023,0),(1024,'Tatarkila',NULL,NULL,NULL,NULL,1024,0),(1025,'Sandhapali',NULL,NULL,NULL,NULL,1025,0),(1026,'Sandhapali',NULL,NULL,NULL,NULL,1026,0),(1027,'Bahira',NULL,NULL,NULL,NULL,1027,0),(1028,'Bahira',NULL,NULL,NULL,NULL,1028,0),(1029,'Sahupada',NULL,NULL,NULL,NULL,1029,0),(1030,'Bahira',NULL,NULL,NULL,NULL,1030,0),(1031,'Bahira',NULL,NULL,NULL,NULL,1031,0),(1032,'Badikata',NULL,NULL,NULL,NULL,1032,0),(1033,'Dantapali',NULL,NULL,NULL,NULL,1033,0),(1034,'Dantapali',NULL,NULL,NULL,NULL,1034,0),(1035,'Bahira',NULL,NULL,NULL,NULL,1035,0),(1036,'Bahira',NULL,NULL,NULL,NULL,1036,0),(1037,'Sahupada',NULL,NULL,NULL,NULL,1037,0),(1038,'Dantapali',NULL,NULL,NULL,NULL,1038,0),(1039,'Dantapali',NULL,NULL,NULL,NULL,1039,0),(1040,'Badikata',NULL,NULL,NULL,NULL,1040,0),(1041,'Badikata',NULL,NULL,NULL,NULL,1041,0),(1042,'Bahira',NULL,NULL,NULL,NULL,1042,0),(1043,'Bahira',NULL,NULL,NULL,NULL,1043,0),(1044,'Bahira',NULL,NULL,NULL,NULL,1044,0),(1045,'Jamghati',NULL,NULL,NULL,NULL,1045,0),(1046,'Sahupada',NULL,NULL,NULL,NULL,1046,0),(1047,'Bahira',NULL,NULL,NULL,NULL,1047,0),(1048,'Bahira',NULL,NULL,NULL,NULL,1048,0),(1049,'Bahira',NULL,NULL,NULL,NULL,1049,0),(1050,'Bahira',NULL,NULL,NULL,NULL,1050,0),(1051,'Jamghati',NULL,NULL,NULL,NULL,1051,0),(1052,'Sahupada',NULL,NULL,NULL,NULL,1052,0),(1053,'Masinapada',NULL,NULL,NULL,NULL,1053,0),(1054,'Kakrichira',NULL,NULL,NULL,NULL,1054,0),(1055,'Kakrichira',NULL,NULL,NULL,NULL,1055,0),(1056,'Tatarkila',NULL,NULL,NULL,NULL,1056,0),(1057,'Badikata',NULL,NULL,NULL,NULL,1057,0),(1058,'Masinapada',NULL,NULL,NULL,NULL,1058,0),(1059,'Bahira',NULL,NULL,NULL,NULL,1059,0);
/*!40000 ALTER TABLE `com_order_delivery_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `com_order_entry`
--

DROP TABLE IF EXISTS `com_order_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `com_order_entry` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `ENTRY_NO` int(2) NOT NULL,
  `QUANTITY` double(10,2) NOT NULL,
  `PRICE` float NOT NULL,
  `DELIVERY_COST` double(10,2) DEFAULT NULL,
  `DISCOUNT` double(10,2) DEFAULT NULL,
  `NOTES` varchar(50) DEFAULT NULL,
  `ORDER_CODE` int(5) NOT NULL,
  `PRODUCT_CODE` int(5) NOT NULL,
  `LAST_MODIFIED_BY` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  KEY `ORDER_CODE` (`ORDER_CODE`),
  KEY `PRODUCT_CODE` (`PRODUCT_CODE`),
  CONSTRAINT `com_order_entry_ibfk_1` FOREIGN KEY (`ORDER_CODE`) REFERENCES `com_order` (`CODE`) ON DELETE CASCADE,
  CONSTRAINT `com_order_entry_ibfk_2` FOREIGN KEY (`PRODUCT_CODE`) REFERENCES `product` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1060 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_order_entry`
--

LOCK TABLES `com_order_entry` WRITE;
/*!40000 ALTER TABLE `com_order_entry` DISABLE KEYS */;
INSERT INTO `com_order_entry` VALUES (1000,1,1100.00,4.1,400.00,10.00,NULL,5000,1000,'admin'),(1001,1,1100.00,4,400.00,0.00,NULL,5001,1000,'admin'),(1002,1,1100.00,4.1,600.00,10.00,NULL,5002,1000,'admin'),(1003,1,1100.00,4.1,600.00,10.00,NULL,5003,1000,'admin'),(1004,1,1100.00,4.1,500.00,10.00,NULL,5004,1000,'admin'),(1005,1,1100.00,4.1,500.00,10.00,NULL,5005,1000,'admin'),(1006,1,1100.00,4.1,1000.00,10.00,NULL,5006,1000,'admin'),(1007,1,1100.00,4.1,1000.00,10.00,NULL,5007,1000,'admin'),(1008,1,1100.00,4.1,400.00,10.00,NULL,5008,1000,'admin'),(1009,1,1100.00,4,400.00,0.00,NULL,5009,1000,'admin'),(1010,1,1100.00,4,500.00,0.00,NULL,5010,1000,'admin'),(1011,1,1100.00,4,500.00,0.00,NULL,5011,1000,'admin'),(1012,1,1100.00,4.1,500.00,10.00,NULL,5012,1000,'admin'),(1013,1,1100.00,4,500.00,0.00,NULL,5013,1000,'admin'),(1014,1,1100.00,4.1,600.00,10.00,NULL,5014,1000,'admin'),(1015,1,1100.00,4.1,1000.00,10.00,NULL,5015,1000,'admin'),(1016,1,1100.00,4.1,1000.00,10.00,NULL,5016,1000,'admin'),(1017,1,1100.00,4.1,1000.00,10.00,NULL,5017,1000,'admin'),(1018,1,1100.00,4.1,1000.00,10.00,NULL,5018,1000,'admin'),(1019,1,1100.00,4.1,400.00,10.00,NULL,5019,1000,'admin'),(1020,1,1100.00,4.1,400.00,10.00,NULL,5020,1000,'admin'),(1021,1,1100.00,4.1,400.00,10.00,NULL,5021,1000,'admin'),(1022,1,1200.00,4.1,500.00,20.00,NULL,5022,1000,'admin'),(1023,1,1100.00,4.1,600.00,10.00,NULL,5023,1000,'admin'),(1024,1,1200.00,4.1,500.00,20.00,NULL,5024,1000,'admin'),(1025,1,1100.00,4.1,0.00,10.00,NULL,5025,1000,'admin'),(1026,1,1100.00,4.1,0.00,10.00,NULL,5026,1000,'admin'),(1027,1,1100.00,4.1,600.00,10.00,NULL,5027,1000,'admin'),(1028,1,1100.00,4.1,600.00,10.00,NULL,5028,1000,'admin'),(1029,1,1100.00,4.1,500.00,10.00,NULL,5029,1000,'admin'),(1030,1,1100.00,4.1,600.00,10.00,NULL,5030,1000,'admin'),(1031,1,1000.00,4.5,600.00,0.00,NULL,5031,1001,'admin'),(1032,1,1000.00,4.4,400.00,0.00,NULL,5032,1001,'admin'),(1033,1,1000.00,4.5,500.00,0.00,NULL,5033,1001,'admin'),(1034,1,1000.00,4.5,500.00,0.00,NULL,5034,1001,'admin'),(1035,1,1100.00,4.1,600.00,10.00,NULL,5035,1000,'admin'),(1036,1,1100.00,4.1,600.00,10.00,NULL,5036,1000,'admin'),(1037,1,1100.00,4.1,500.00,10.00,NULL,5037,1000,'admin'),(1038,1,1000.00,4.5,500.00,0.00,NULL,5038,1001,'admin'),(1039,1,1000.00,4.5,500.00,0.00,NULL,5039,1001,'admin'),(1040,1,1000.00,4.4,400.00,0.00,NULL,5040,1001,'admin'),(1041,1,1000.00,4.4,400.00,0.00,NULL,5041,1001,'admin'),(1042,1,1100.00,4.1,600.00,10.00,NULL,5042,1000,'admin'),(1043,1,1100.00,4.1,600.00,10.00,NULL,5043,1000,'admin'),(1044,1,1100.00,4.1,600.00,10.00,NULL,5044,1000,'admin'),(1045,1,1100.00,4.1,500.00,10.00,NULL,5045,1000,'admin'),(1046,1,1100.00,4.1,500.00,10.00,NULL,5046,1000,'admin'),(1047,1,1100.00,4.1,600.00,10.00,NULL,5047,1000,'admin'),(1048,1,1100.00,4.1,600.00,10.00,NULL,5048,1000,'admin'),(1049,1,1100.00,4.1,600.00,10.00,NULL,5049,1000,'admin'),(1050,1,1100.00,4.1,600.00,10.00,NULL,5050,1000,'admin'),(1051,1,1100.00,4.1,500.00,10.00,NULL,5051,1000,'admin'),(1052,1,1100.00,4.1,500.00,10.00,NULL,5052,1000,'admin'),(1053,1,1100.00,4.1,1000.00,10.00,NULL,5053,1000,'admin'),(1054,1,1100.00,4.1,1000.00,10.00,NULL,5054,1000,'admin'),(1055,1,1100.00,4.1,1000.00,10.00,NULL,5055,1000,'admin'),(1056,1,1200.00,4.1,500.00,20.00,NULL,5056,1000,'admin'),(1057,1,1200.00,4.1,500.00,20.00,NULL,5057,1000,'admin'),(1058,1,1100.00,4.1,1000.00,10.00,NULL,5058,1000,'admin'),(1059,1,1100.00,4.1,600.00,10.00,NULL,5059,1000,'admin');
/*!40000 ALTER TABLE `com_order_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `com_order_payment_entry`
--

DROP TABLE IF EXISTS `com_order_payment_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `com_order_payment_entry` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `PAYMENT_ENTRY_NO` int(2) NOT NULL,
  `PAYABLE_AMOUNT` double(10,2) NOT NULL,
  `PAID_AMOUNT` double(10,2) NOT NULL,
  `PAYMENT_ENTRY_PAYMENT_STATUS` int(1) NOT NULL,
  `PAYMENT_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ORDER_ENTRY_CODE` int(5) NOT NULL,
  PRIMARY KEY (`CODE`),
  KEY `ORDER_ENTRY_CODE` (`ORDER_ENTRY_CODE`),
  CONSTRAINT `com_order_payment_entry_ibfk_1` FOREIGN KEY (`ORDER_ENTRY_CODE`) REFERENCES `com_order_entry` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_order_payment_entry`
--

LOCK TABLES `com_order_payment_entry` WRITE;
/*!40000 ALTER TABLE `com_order_payment_entry` DISABLE KEYS */;
INSERT INTO `com_order_payment_entry` VALUES (1000,1,4900.00,4000.00,2,'2019-01-01 18:30:00',1008),(1001,1,4900.00,4900.00,0,'2019-01-03 18:30:00',1010),(1002,1,4900.00,4900.00,0,'2019-01-03 18:30:00',1011);
/*!40000 ALTER TABLE `com_order_payment_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_person`
--

DROP TABLE IF EXISTS `contact_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_person` (
  `CODE` int(5) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `CONTACT_PRIMARY` varchar(15) NOT NULL,
  `CONTACT_SECONDARY` varchar(15) DEFAULT NULL,
  `TYPE_CODE` int(5) NOT NULL,
  `VENDOR_CODE` int(5) NOT NULL,
  `LAST_MODIFIED_BY` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  KEY `VENDOR_CODE` (`VENDOR_CODE`),
  CONSTRAINT `contact_person_ibfk_1` FOREIGN KEY (`VENDOR_CODE`) REFERENCES `vendor` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_person`
--

LOCK TABLES `contact_person` WRITE;
/*!40000 ALTER TABLE `contact_person` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_person_type`
--

DROP TABLE IF EXISTS `contact_person_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_person_type` (
  `TYPE_CODE` int(5) NOT NULL,
  `TYPE_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`TYPE_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_person_type`
--

LOCK TABLES `contact_person_type` WRITE;
/*!40000 ALTER TABLE `contact_person_type` DISABLE KEYS */;
INSERT INTO `contact_person_type` VALUES (1001,'INVESTOR'),(5001,'VENDOR');
/*!40000 ALTER TABLE `contact_person_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `CONTACT_PRIMARY` varchar(15) NOT NULL,
  `CONTACT_SECONDARY` varchar(15) DEFAULT NULL,
  `LAST_MODIFIED_BY` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  KEY `ID_CUSTOMER` (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=1013 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1000,'Ganga Barik','1234567890',NULL,'admin'),(1001,'Akash Rana','1234567890',NULL,'admin'),(1002,'Pranab Karna','1234567890',NULL,'admin'),(1003,'Naveen Pradhan','1234567890',NULL,'admin'),(1004,'Tanveer','1334567890',NULL,'admin'),(1005,'Meher','1234567890',NULL,'admin'),(1006,'Jayakrushna Meher','1234567890',NULL,'admin'),(1007,'Litu','1234567890',NULL,'admin'),(1008,'Akshaya Rath','123456789',NULL,'admin'),(1009,'Satya Meher','1234567890',NULL,'admin'),(1010,'Kailash Patra','1234567890',NULL,'admin'),(1011,'Badikata High School','1234567890',NULL,'admin'),(1012,'Godabari Barik','1234567890',NULL,'admin');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_vehicle`
--

DROP TABLE IF EXISTS `delivery_vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_vehicle` (
  `CODE` int(11) NOT NULL AUTO_INCREMENT,
  `VEHICLE_NO` varchar(30) NOT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_vehicle`
--

LOCK TABLES `delivery_vehicle` WRITE;
/*!40000 ALTER TABLE `delivery_vehicle` DISABLE KEYS */;
INSERT INTO `delivery_vehicle` VALUES (1000,'0259'),(1001,'9510'),(1002,'EXTERNAL');
/*!40000 ALTER TABLE `delivery_vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `CODE` int(5) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `CONTACT_PRIMARY` varchar(15) NOT NULL,
  `CONTACT_SECONDARY` varchar(15) DEFAULT NULL,
  `ACTIVE` int(1) NOT NULL,
  `JOINING_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `LAST_MODIFIED_BY` varchar(10) DEFAULT NULL,
  `LAST_MODIFIED_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_salary`
--

DROP TABLE IF EXISTS `employee_salary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_salary` (
  `CODE` int(5) NOT NULL AUTO_INCREMENT,
  `EMPLOYEE_CODE` int(11) NOT NULL,
  `EFFECTIVE_FROM` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `EFFECTIVE_TILL` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `SALARY` double(10,2) NOT NULL,
  `ACTIVE_REVISION` int(1) DEFAULT NULL,
  `LAST_MODIFIED_BY` varchar(10) DEFAULT NULL,
  `LAST_MODIFIED_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`CODE`),
  KEY `EMPLOYEE_CODE` (`EMPLOYEE_CODE`),
  CONSTRAINT `employee_salary_ibfk_1` FOREIGN KEY (`EMPLOYEE_CODE`) REFERENCES `employee` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_salary`
--

LOCK TABLES `employee_salary` WRITE;
/*!40000 ALTER TABLE `employee_salary` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_salary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expense`
--

DROP TABLE IF EXISTS `expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expense` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `EXPENSE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `AMOUNT` double(10,2) NOT NULL,
  `VENDOR_CODE` int(5) NOT NULL,
  `INVESTOR_CODE` int(5) NOT NULL,
  `CATEGORY_CODE` int(5) NOT NULL,
  `NOTES` varchar(255) DEFAULT NULL,
  `PAYMEMT_CODE` int(5) NOT NULL,
  `CREATED_BY` varchar(10) NOT NULL,
  `LAST_MODIFIED_BY` varchar(10) NOT NULL,
  `CREATED_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `LAST_MODIFIED_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`CODE`),
  KEY `INVESTOR_CODE` (`INVESTOR_CODE`),
  KEY `VENDOR_CODE` (`VENDOR_CODE`),
  KEY `CATEGORY_CODE` (`CATEGORY_CODE`),
  KEY `PAYMEMT_CODE` (`PAYMEMT_CODE`),
  CONSTRAINT `expense_ibfk_1` FOREIGN KEY (`INVESTOR_CODE`) REFERENCES `investor` (`CODE`) ON DELETE CASCADE,
  CONSTRAINT `expense_ibfk_2` FOREIGN KEY (`VENDOR_CODE`) REFERENCES `vendor` (`CODE`) ON DELETE CASCADE,
  CONSTRAINT `expense_ibfk_3` FOREIGN KEY (`CATEGORY_CODE`) REFERENCES `expense_category` (`EXP_CAT_CODE`) ON DELETE CASCADE,
  CONSTRAINT `expense_ibfk_4` FOREIGN KEY (`PAYMEMT_CODE`) REFERENCES `payment_mode` (`PAYMEMT_CODE`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expense`
--

LOCK TABLES `expense` WRITE;
/*!40000 ALTER TABLE `expense` DISABLE KEYS */;
/*!40000 ALTER TABLE `expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expense_category`
--

DROP TABLE IF EXISTS `expense_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expense_category` (
  `EXP_CAT_CODE` int(5) NOT NULL AUTO_INCREMENT,
  `EXP_CAT_NAME` varchar(50) NOT NULL,
  `EXP_CAT_DESC` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`EXP_CAT_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=1018 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expense_category`
--

LOCK TABLES `expense_category` WRITE;
/*!40000 ALTER TABLE `expense_category` DISABLE KEYS */;
INSERT INTO `expense_category` VALUES (1000,'Labor payment - Brick construction','Labor payment - Brick construction'),(1001,'Labor payment - Brick Delivery','Labor payment - Brick Delivery'),(1002,'Labor payment - Sand','Labor payment - Sand'),(1003,'Labor payment - Lime','Labor payment - Lime'),(1004,'Labor payment - Gypsum','Labor payment - Gypsum'),(1005,'Salary Payment','Employee Salary Payment'),(1006,'Labor payment - Maintenance','Labor payment - Maintenance'),(1007,'Material Procurement - Flyash','Material Procurement - Flyash'),(1008,'Material Procurement - Gypsum','Material Procurement - Gypsum'),(1009,'Material Procurement - Lime','Material Procurement - Lime'),(1010,'Material Procurement - Driver Payment','Material Procurement - Driver Payment'),(1011,'Bill Payment - Lease','Bill Payment - Lease'),(1012,'Bill Payment - Electricity','Bill Payment - Electricity'),(1013,'Accounting - GST','Accounting - GST'),(1014,'Accounting - CA','Accounting - CA'),(1015,'Maintenance - Machine','Maintenance - Machine'),(1016,'Maintenance - electrical','Maintenance - electrical'),(1017,'Maintenance - Vehicle','Maintenance - Vehicle');
/*!40000 ALTER TABLE `expense_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inventory` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `LAST_UPDATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `TOTAL_AVAILABLE_QUANTITY` double(10,2) NOT NULL,
  `TOTAL_RESERVED_QUANTITY` double(10,2) NOT NULL,
  `TOTAL_DAMAGED_QUANTITY` double(10,2) NOT NULL,
  `PRODUCT_CODE` int(5) NOT NULL,
  `LAST_MODIFIED_BY` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  KEY `PRODUCT_CODE` (`PRODUCT_CODE`),
  CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`PRODUCT_CODE`) REFERENCES `product` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1000,'2019-01-12 18:30:00',12845.00,0.00,0.00,1000,'SYSTEM'),(1001,'2019-01-09 18:30:00',7450.00,0.00,0.00,1001,'SYSTEM'),(1002,'2015-12-31 18:30:00',0.00,0.00,0.00,1002,'admin');
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_entry`
--

DROP TABLE IF EXISTS `inventory_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inventory_entry` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `INVENTORY_CODE` int(11) NOT NULL,
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `OPENING_BALANCE` double(10,2) NOT NULL,
  `CLOSING_BALANCE` double(10,2) NOT NULL,
  `QUANTITY_ADDED` double(10,2) NOT NULL,
  `QUANTITY_REDUCED` double(10,2) NOT NULL,
  `QUANTITY_DAMAGED` double(10,2) NOT NULL,
  `QUANTITY_RESERVED` double(10,2) NOT NULL,
  `LAST_MODIFIED_BY` varchar(10) DEFAULT NULL,
  `ORDER_REF` int(10) DEFAULT NULL,
  `LABOUR_PAYMENT_STATUS` int(1) DEFAULT NULL,
  `MACHINE_CODE` int(11) NOT NULL,
  PRIMARY KEY (`CODE`),
  KEY `INVENTORY_CODE` (`INVENTORY_CODE`),
  CONSTRAINT `inventory_entry_ibfk_1` FOREIGN KEY (`INVENTORY_CODE`) REFERENCES `inventory` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1075 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_entry`
--

LOCK TABLES `inventory_entry` WRITE;
/*!40000 ALTER TABLE `inventory_entry` DISABLE KEYS */;
INSERT INTO `inventory_entry` VALUES (1000,1001,'2019-01-05 18:29:59',0.00,3068.00,3068.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1001,1000,'2019-01-01 18:29:59',0.00,18142.00,18142.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1002,1000,'2019-01-02 18:29:59',18142.00,23956.00,5814.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1003,1000,'2019-01-01 18:30:00',23956.00,22856.00,0.00,1100.00,0.00,0.00,'admin',5000,NULL,-1),(1004,1000,'2019-01-01 18:30:00',22856.00,21756.00,0.00,1100.00,0.00,0.00,'admin',5001,NULL,-1),(1005,1000,'2019-01-01 18:30:00',21756.00,20656.00,0.00,1100.00,0.00,0.00,'admin',5002,NULL,-1),(1006,1000,'2019-01-01 18:30:00',20656.00,19556.00,0.00,1100.00,0.00,0.00,'admin',5003,NULL,-1),(1007,1000,'2019-01-01 18:30:00',19556.00,18456.00,0.00,1100.00,0.00,0.00,'admin',5004,NULL,-1),(1008,1000,'2019-01-03 18:29:59',18456.00,23649.00,5193.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1009,1000,'2019-01-01 18:30:00',23649.00,22549.00,0.00,1100.00,0.00,0.00,'admin',5005,NULL,-1),(1010,1000,'2019-01-02 18:30:00',22549.00,21449.00,0.00,1100.00,0.00,0.00,'admin',5006,NULL,-1),(1011,1000,'2019-01-02 18:30:00',21449.00,20349.00,0.00,1100.00,0.00,0.00,'admin',5007,NULL,-1),(1012,1000,'2019-01-02 18:30:00',20349.00,19249.00,0.00,1100.00,0.00,0.00,'admin',5008,NULL,-1),(1013,1000,'2019-01-02 18:30:00',19249.00,18149.00,0.00,1100.00,0.00,0.00,'admin',5009,NULL,-1),(1014,1000,'2019-01-02 18:30:00',18149.00,17049.00,0.00,1100.00,0.00,0.00,'admin',5010,NULL,-1),(1015,1000,'2019-01-02 18:30:00',17049.00,15949.00,0.00,1100.00,0.00,0.00,'admin',5011,NULL,-1),(1016,1000,'2019-01-02 18:30:00',15949.00,14849.00,0.00,1100.00,0.00,0.00,'admin',5012,NULL,-1),(1017,1000,'2019-01-02 18:30:00',14849.00,13749.00,0.00,1100.00,0.00,0.00,'admin',5013,NULL,-1),(1018,1000,'2019-01-02 18:30:00',13749.00,12649.00,0.00,1100.00,0.00,0.00,'admin',5014,NULL,-1),(1019,1000,'2019-01-04 18:29:59',12649.00,17228.00,4579.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1020,1000,'2019-01-03 18:30:00',17228.00,16128.00,0.00,1100.00,0.00,0.00,'admin',5015,NULL,-1),(1021,1000,'2019-01-03 18:30:00',16128.00,15028.00,0.00,1100.00,0.00,0.00,'admin',5016,NULL,-1),(1022,1000,'2019-01-03 18:30:00',15028.00,13928.00,0.00,1100.00,0.00,0.00,'admin',5017,NULL,-1),(1023,1000,'2019-01-03 18:30:00',13928.00,12828.00,0.00,1100.00,0.00,0.00,'admin',5018,NULL,-1),(1024,1000,'2019-01-03 18:30:00',12828.00,11728.00,0.00,1100.00,0.00,0.00,'admin',5019,NULL,-1),(1025,1000,'2019-01-03 18:30:00',11728.00,10628.00,0.00,1100.00,0.00,0.00,'admin',5020,NULL,-1),(1026,1000,'2019-01-04 18:30:00',10628.00,9528.00,0.00,1100.00,0.00,0.00,'admin',5021,NULL,-1),(1027,1000,'2019-01-04 18:30:00',9528.00,8328.00,0.00,1200.00,0.00,0.00,'admin',5022,NULL,-1),(1028,1000,'2019-01-05 18:30:00',8328.00,7228.00,0.00,1100.00,0.00,0.00,'admin',5023,NULL,-1),(1029,1000,'2019-01-05 18:30:00',7228.00,6028.00,0.00,1200.00,0.00,0.00,'admin',5024,NULL,-1),(1030,1000,'2019-01-05 18:30:00',6028.00,4928.00,0.00,1100.00,0.00,0.00,'admin',5025,NULL,-1),(1031,1000,'2019-01-05 18:30:00',4928.00,3828.00,0.00,1100.00,0.00,0.00,'admin',5026,NULL,-1),(1032,1000,'2019-01-06 18:30:00',3828.00,2728.00,0.00,1100.00,0.00,0.00,'admin',5027,NULL,-1),(1033,1000,'2019-01-06 18:30:00',2728.00,1628.00,0.00,1100.00,0.00,0.00,'admin',5028,NULL,-1),(1034,1000,'2019-01-08 18:29:59',1628.00,6806.00,5178.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1035,1000,'2019-01-07 18:30:00',6806.00,5706.00,0.00,1100.00,0.00,0.00,'admin',5029,NULL,-1),(1036,1000,'2019-01-09 18:29:59',5706.00,10437.00,4731.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1037,1000,'2019-01-09 18:30:00',10437.00,9337.00,0.00,1100.00,0.00,0.00,'admin',5030,NULL,-1),(1038,1001,'2019-01-06 18:29:59',3068.00,9218.00,6150.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1039,1001,'2019-01-07 18:29:59',9218.00,15450.00,6232.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1040,1001,'2019-01-08 18:30:00',15450.00,14450.00,0.00,1000.00,0.00,0.00,'admin',5031,NULL,-1),(1041,1001,'2019-01-08 18:30:00',14450.00,13450.00,0.00,1000.00,0.00,0.00,'admin',5032,NULL,-1),(1042,1001,'2019-01-08 18:30:00',13450.00,12450.00,0.00,1000.00,0.00,0.00,'admin',5033,NULL,-1),(1043,1001,'2019-01-08 18:30:00',12450.00,11450.00,0.00,1000.00,0.00,0.00,'admin',5034,NULL,-1),(1044,1000,'2019-01-09 18:30:00',9337.00,8237.00,0.00,1100.00,0.00,0.00,'admin',5035,NULL,-1),(1045,1000,'2019-01-09 18:30:00',8237.00,7137.00,0.00,1100.00,0.00,0.00,'admin',5036,NULL,-1),(1046,1000,'2019-01-09 18:30:00',7137.00,6037.00,0.00,1100.00,0.00,0.00,'admin',5037,NULL,-1),(1047,1000,'2019-01-10 18:29:59',6037.00,14093.00,8056.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1048,1001,'2019-01-09 18:30:00',11450.00,10450.00,0.00,1000.00,0.00,0.00,'admin',5038,NULL,-1),(1049,1001,'2019-01-09 18:30:00',10450.00,9450.00,0.00,1000.00,0.00,0.00,'admin',5039,NULL,-1),(1050,1001,'2019-01-09 18:30:00',9450.00,8450.00,0.00,1000.00,0.00,0.00,'admin',5040,NULL,-1),(1051,1001,'2019-01-09 18:30:00',8450.00,7450.00,0.00,1000.00,0.00,0.00,'admin',5041,NULL,-1),(1052,1000,'2019-01-10 18:30:00',14093.00,12993.00,0.00,1100.00,0.00,0.00,'admin',5042,NULL,-1),(1053,1000,'2019-01-10 18:30:00',12993.00,11893.00,0.00,1100.00,0.00,0.00,'admin',5043,NULL,-1),(1054,1000,'2019-01-10 18:30:00',11893.00,10793.00,0.00,1100.00,0.00,0.00,'admin',5044,NULL,-1),(1055,1000,'2019-01-10 18:30:00',10793.00,9693.00,0.00,1100.00,0.00,0.00,'admin',5045,NULL,-1),(1056,1000,'2019-01-10 18:30:00',9693.00,8593.00,0.00,1100.00,0.00,0.00,'admin',5046,NULL,-1),(1057,1000,'2019-01-11 18:29:59',8593.00,16358.00,7765.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1058,1000,'2019-01-11 18:30:00',16358.00,15258.00,0.00,1100.00,0.00,0.00,'admin',5047,NULL,-1),(1059,1000,'2019-01-11 18:30:00',15258.00,14158.00,0.00,1100.00,0.00,0.00,'admin',5048,NULL,-1),(1060,1000,'2019-01-11 18:30:00',14158.00,13058.00,0.00,1100.00,0.00,0.00,'admin',5049,NULL,-1),(1061,1000,'2019-01-11 18:30:00',13058.00,11958.00,0.00,1100.00,0.00,0.00,'admin',5050,NULL,-1),(1062,1000,'2019-01-11 18:30:00',11958.00,10858.00,0.00,1100.00,0.00,0.00,'admin',5051,NULL,-1),(1063,1000,'2019-01-11 18:30:00',10858.00,9758.00,0.00,1100.00,0.00,0.00,'admin',5052,NULL,-1),(1064,1000,'2019-01-11 18:30:00',9758.00,8658.00,0.00,1100.00,0.00,0.00,'admin',5053,NULL,-1),(1065,1000,'2019-01-12 18:29:59',8658.00,16069.00,7411.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1066,1000,'2019-01-12 18:30:00',16069.00,14969.00,0.00,1100.00,0.00,0.00,'admin',5054,NULL,-1),(1067,1000,'2019-01-12 18:30:00',14969.00,13869.00,0.00,1100.00,0.00,0.00,'admin',5055,NULL,-1),(1068,1000,'2019-01-12 18:30:00',13869.00,12669.00,0.00,1200.00,0.00,0.00,'admin',5056,NULL,-1),(1069,1000,'2019-01-12 18:30:00',12669.00,11469.00,0.00,1200.00,0.00,0.00,'admin',5057,NULL,-1),(1070,1000,'2019-01-12 18:30:00',11469.00,10369.00,0.00,1100.00,0.00,0.00,'admin',5058,NULL,-1),(1071,1000,'2019-01-13 18:29:59',10369.00,16245.00,5876.00,0.00,0.00,0.00,'admin',NULL,NULL,1000),(1072,1000,'2019-01-13 18:30:00',16245.00,15145.00,0.00,1100.00,0.00,0.00,'admin',5059,NULL,-1),(1073,1000,'2019-01-12 18:30:00',15145.00,13945.00,0.00,1200.00,0.00,0.00,'admin',5060,NULL,-1),(1074,1000,'2019-01-12 18:30:00',13945.00,12845.00,0.00,1100.00,0.00,0.00,'admin',5061,NULL,-1);
/*!40000 ALTER TABLE `inventory_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `investment`
--

DROP TABLE IF EXISTS `investment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `investment` (
  `CODE` int(5) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `INVESTED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `AMOUNT` double(10,2) NOT NULL,
  `INVESTOR_CODE` int(5) NOT NULL,
  PRIMARY KEY (`CODE`),
  KEY `INVESTOR_CODE` (`INVESTOR_CODE`),
  CONSTRAINT `investment_ibfk_1` FOREIGN KEY (`INVESTOR_CODE`) REFERENCES `investor` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `investment`
--

LOCK TABLES `investment` WRITE;
/*!40000 ALTER TABLE `investment` DISABLE KEYS */;
/*!40000 ALTER TABLE `investment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `investor`
--

DROP TABLE IF EXISTS `investor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `investor` (
  `CODE` int(5) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `CONTACT_PRIMARY` varchar(15) NOT NULL,
  `CONTACT_SECONDARY` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=1005 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `investor`
--

LOCK TABLES `investor` WRITE;
/*!40000 ALTER TABLE `investor` DISABLE KEYS */;
INSERT INTO `investor` VALUES (1000,'CORPUS','0000','0000'),(1001,'RAJESH TRIPATHY','0000','0000'),(1002,'RAKESH TRIPATHY','0000','0000'),(1003,'PRASUN SARANGI','0000','0000'),(1004,'REENA RANI TRIPATHY','0000','0000');
/*!40000 ALTER TABLE `investor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_mode`
--

DROP TABLE IF EXISTS `payment_mode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_mode` (
  `PAYMEMT_CODE` int(5) NOT NULL AUTO_INCREMENT,
  `PAYMEMT_CODE_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`PAYMEMT_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_mode`
--

LOCK TABLES `payment_mode` WRITE;
/*!40000 ALTER TABLE `payment_mode` DISABLE KEYS */;
INSERT INTO `payment_mode` VALUES (1000,'Cash'),(1001,'On Account'),(1002,'DD / Cheque');
/*!40000 ALTER TABLE `payment_mode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `CODE` int(5) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(30) DEFAULT NULL,
  `SALEABLE` int(1) DEFAULT NULL,
  `CREATED_BY` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1000,'9 INCH - AUTOMATIC','STANDARD 9 INCH BRICK',0,NULL),(1001,'10 INCH - AUTOMATIC','STANDARD 10 INCH BRICK',0,NULL),(1002,'12 INCH BLOCK','12 INCH BLOCK',0,NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_price_row`
--

DROP TABLE IF EXISTS `product_price_row`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_price_row` (
  `CODE` int(10) NOT NULL AUTO_INCREMENT,
  `LAST_UPDATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `BASEPRICE` double(10,2) NOT NULL,
  `VALID_FROM` date DEFAULT NULL,
  `VALID_TILL` date DEFAULT NULL,
  `PRODUCT_CODE` int(5) NOT NULL,
  PRIMARY KEY (`CODE`),
  KEY `PRODUCT_CODE` (`PRODUCT_CODE`),
  CONSTRAINT `product_price_row_ibfk_1` FOREIGN KEY (`PRODUCT_CODE`) REFERENCES `product` (`CODE`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_price_row`
--

LOCK TABLES `product_price_row` WRITE;
/*!40000 ALTER TABLE `product_price_row` DISABLE KEYS */;
INSERT INTO `product_price_row` VALUES (1000,'2015-12-31 18:30:00',4.10,NULL,NULL,1000),(1001,'2015-12-31 18:30:00',4.50,NULL,NULL,1001),(1002,'2015-12-31 18:30:00',15.00,NULL,NULL,1002);
/*!40000 ALTER TABLE `product_price_row` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_machine`
--

DROP TABLE IF EXISTS `production_machine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `production_machine` (
  `CODE` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_machine`
--

LOCK TABLES `production_machine` WRITE;
/*!40000 ALTER TABLE `production_machine` DISABLE KEYS */;
INSERT INTO `production_machine` VALUES (1000,'Machine 1 - Automatic'),(1001,'Machine 2 - Automatic'),(1002,'Machine 3 - Egg Laying');
/*!40000 ALTER TABLE `production_machine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendor`
--

DROP TABLE IF EXISTS `vendor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor` (
  `CODE` int(5) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `CONTACT_PRIMARY` varchar(15) NOT NULL,
  `CONTACT_SECONDARY` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  KEY `ID_VENDOR` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendor`
--

LOCK TABLES `vendor` WRITE;
/*!40000 ALTER TABLE `vendor` DISABLE KEYS */;
/*!40000 ALTER TABLE `vendor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-01 17:30:09
