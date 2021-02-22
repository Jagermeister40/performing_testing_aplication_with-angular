CREATE DATABASE results;

USE results;

DROP TABLE IF EXISTS main;

CREATE TABLE main (
  name_of_test varchar(256) NOT NULL,
  type_of_test varchar(256) NOT NULL,
  result_of_test varchar(256)
) DEFAULT CHARSET=latin1;
