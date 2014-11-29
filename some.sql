--USING POSTGRESQL

CREATE DATABASE products WITH
  OWNER = postgres
	ENCODING = 'UTF8'
	TABLESPACE = pg_default
	LC_COLLATE = 'Spanish_Bolivarian Republic of Venezuela.1252'
	LC_CTYPE = 'Spanish_Bolivarian Republic of Venezuela.1252'
	CONNECTION LIMIT = -1;

CREATE TABLE product
(
	id_product INTEGER,
	qu_product INTEGER,
	na_product VARCHAR(20),
	de_product VARCHAR(20),
	CONSTRAINT product_pk PRIMARY KEY(id_product)
);

