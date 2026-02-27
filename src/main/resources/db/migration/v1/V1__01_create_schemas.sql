-- Criação dos schemas para cada módulo (Bounded Context)
CREATE SCHEMA IF NOT EXISTS product_schema;
CREATE SCHEMA IF NOT EXISTS customer_schema;
CREATE SCHEMA IF NOT EXISTS order_schema;
CREATE SCHEMA IF NOT EXISTS payment_schema;

-- Comentários para documentação
COMMENT ON SCHEMA product_schema IS 'Schema para o módulo Product - Bounded Context de produtos';
COMMENT ON SCHEMA customer_schema IS 'Schema para o módulo Customer - Bounded Context de clientes';
COMMENT ON SCHEMA order_schema IS 'Schema para o módulo Order - Bounded Context de pedidos';
COMMENT ON SCHEMA payment_schema IS 'Schema para o módulo Payment - Bounded Context de pagamentos';

