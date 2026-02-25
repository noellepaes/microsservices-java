# Ecommerce Monolith Modular

Projeto monolito modular em Java preparado para evoluir para microsserviços.

## 🏗️ Arquitetura

### Estrutura de Módulos (Bounded Contexts)

```
ecommerce/
 ├── product/      # Módulo de Produtos
 ├── customer/     # Módulo de Clientes
 ├── order/        # Módulo de Pedidos
 ├── payment/      # Módulo de Pagamentos
 └── shared/       # Classes compartilhadas
```

### Estrutura DDD por Módulo

Cada módulo segue a estrutura DDD:

```
module/
 ├── domain/          # Camada de domínio
 │    ├── model/      # Entidades e Value Objects
 │    ├── repository/ # Interfaces de repositório
 │    └── event/      # Eventos de domínio
 ├── application/     # Camada de aplicação (Services)
 ├── infrastructure/  # Camada de infraestrutura
 │    └── repository/ # Implementações JPA
 └── presentation/    # Camada de apresentação (Controllers)
```

## 🗄️ Banco de Dados

### Estratégia: PostgreSQL com Schemas Separados

- **1 banco PostgreSQL** com **4 schemas**:
  - `product_schema`
  - `customer_schema`
  - `order_schema`
  - `payment_schema`

Isso força separação de dados e facilita migração futura para bancos separados.

## 🚀 Como Executar

### 1. Subir PostgreSQL com Docker

```bash
docker-compose up -d
```

### 2. Executar a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📋 Endpoints

### Product
- `POST /api/products` - Criar produto
- `GET /api/products/{id}` - Buscar produto
- `GET /api/products` - Listar produtos
- `PUT /api/products/{id}` - Atualizar produto
- `POST /api/products/{id}/decrease-stock` - Reduzir estoque

### Customer
- `POST /api/customers` - Criar cliente
- `GET /api/customers/{id}` - Buscar cliente
- `GET /api/customers` - Listar clientes
- `PUT /api/customers/{id}` - Atualizar cliente
- `DELETE /api/customers/{id}` - Desativar cliente

### Order
- `POST /api/orders?customerId={id}` - Criar pedido
- `GET /api/orders/{id}` - Buscar pedido
- `GET /api/orders/customer/{customerId}` - Listar pedidos do cliente
- `POST /api/orders/{id}/items` - Adicionar item ao pedido
- `POST /api/orders/{id}/pay` - Pagar pedido
- `POST /api/orders/{id}/cancel` - Cancelar pedido

### Payment
- `POST /api/payments?orderId={id}&amount={value}&method={method}` - Criar pagamento
- `GET /api/payments/{id}` - Buscar pagamento
- `GET /api/payments/order/{orderId}` - Listar pagamentos do pedido
- `POST /api/payments/{id}/approve` - Aprovar pagamento
- `POST /api/payments/{id}/reject` - Rejeitar pagamento

## 🔄 Comunicação entre Módulos

Atualmente, os módulos se comunicam através de:

1. **Módulo `shared/`**: Classes comuns (BaseEntity, DomainEvent, Exceptions)
2. **UUIDs**: Referências entre módulos usam UUIDs (ex: Order referencia Customer por UUID)
3. **Futuro**: Eventos de domínio para comunicação assíncrona

## 🎯 Próximos Passos

1. ✅ Estrutura DDD básica
2. ✅ Schemas separados no PostgreSQL
3. ⏳ Implementar eventos de domínio
4. ⏳ Migrar para WebFlux (não bloqueante)
5. ⏳ Extrair para microsserviços

## 📝 Regras de Negócio Implementadas

### Order (Aggregate Root)
- ✅ Só pode ir de `PENDING` → `PAID`
- ✅ Nunca pode ir de `CANCELLED` → `PAID`
- ✅ Pedido pago não pode ser cancelado

### Product
- ✅ Validação de estoque antes de reduzir
- ✅ Produto deve estar ativo e com estoque para estar disponível

### Payment
- ✅ Só pode aprovar/rejeitar pagamentos `PENDING`

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL 15
- Flyway (migrations)
- Lombok
- Maven
