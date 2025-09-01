--liquibase formatted sql

--changeset dev:credit_ya_30082025_2300
ALTER TABLE customers
ADD COLUMN document VARCHAR(30) NOT NULL UNIQUE;
