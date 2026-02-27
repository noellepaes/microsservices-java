-- Tabela de clientes
CREATE TABLE IF NOT EXISTS customer_schema.customers (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version BIGINT
);

CREATE INDEX idx_customers_email ON customer_schema.customers(email);
CREATE INDEX idx_customers_cpf ON customer_schema.customers(cpf);
CREATE INDEX idx_customers_status ON customer_schema.customers(status);

