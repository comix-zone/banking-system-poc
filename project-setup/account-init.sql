-- Create the schema
CREATE SCHEMA IF NOT EXISTS account;

-- Create the account_owner table
CREATE TABLE account.account_owner (
   ao_id UUID PRIMARY KEY,
   ao_personal_id VARCHAR(255) NOT NULL,
   ao_name VARCHAR(255) NOT NULL,
   ao_surname VARCHAR(255) NOT NULL,
   ao_email VARCHAR(255),
   ao_phone VARCHAR(255) NOT NULL,
   ao_address VARCHAR(255) NOT NULL,
   ao_account_status INT NOT NULL DEFAULT 0,
   ao_currency VARCHAR(4) NOT NULL
);