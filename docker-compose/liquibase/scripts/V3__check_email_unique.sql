--liquibase formatted sql

--changeset dev:credit_ya_27082025_2300
CREATE UNIQUE INDEX idx_email ON customers(email);