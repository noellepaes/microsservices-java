-- Tabela de produtos
CREATE TABLE IF NOT EXISTS product_schema.products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000),
    price NUMERIC(10,2) NOT NULL,
    stock INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version BIGINT
);

CREATE INDEX idx_products_status ON product_schema.products(status);
CREATE INDEX idx_products_name ON product_schema.products(name);
