DROP TABLE IF EXISTS customer;

CREATE TABLE `customer` (
  `customer_id` int AUTO_INCREMENT  PRIMARY KEY,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile_number` varchar(20) NOT NULL,
  `created_date` date DEFAULT NULL
);

INSERT INTO `customer` (`name`,`email`,`mobile_number`,`created_date`)
 VALUES ('Eazy Bytes','anurag@anurag.com','9876548337',CURDATE());


