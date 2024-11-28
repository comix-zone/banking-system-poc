-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS transaction;

-- Create table
CREATE TABLE IF NOT EXISTS transaction.transaction (
    t_id UUID PRIMARY KEY,
    t_correlation_id UUID,
    t_account_id UUID NOT NULL,
    t_transaction_type INT NOT NULL,
    t_amount NUMERIC(20, 6) NOT NULL,
    t_current_balance NUMERIC(20, 6) NOT NULL,
    t_currency VARCHAR(4) NOT NULL,
    t_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );
CREATE INDEX idx_t_correlation_id ON transaction.transaction (t_correlation_id);
CREATE INDEX idx_t_account_id ON transaction.transaction (t_account_id);
CREATE INDEX idx_t_created_at ON transaction.transaction (t_created_at);