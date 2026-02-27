-- Tabela de pagamentos
CREATE TABLE IF NOT EXISTS payment_schema.payments (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    method VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version BIGINT
);

CREATE INDEX idx_payments_order_id ON payment_schema.payments(order_id);
CREATE INDEX idx_payments_status ON payment_schema.payments(status);
CREATE INDEX idx_payments_method ON payment_schema.payments(method);

