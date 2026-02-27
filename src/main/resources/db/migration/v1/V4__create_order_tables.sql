-- Tabela de pedidos
CREATE TABLE IF NOT EXISTS order_schema.orders (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_amount NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version BIGINT
);

-- Tabela de itens do pedido
CREATE TABLE IF NOT EXISTS order_schema.order_items (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES order_schema.orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_orders_customer_id ON order_schema.orders(customer_id);
CREATE INDEX idx_orders_status ON order_schema.orders(status);
CREATE INDEX idx_order_items_order_id ON order_schema.order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_schema.order_items(product_id);

