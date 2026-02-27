# Ecommerce Monolith Modular

Projeto **monolito modular** em **Java 21** (um único projeto Maven/Spring Boot), preparado para evoluir para microsserviços.

## 🧱 Estrutura (1 projeto, módulos por pacote)

O projeto é **um único app executável** e os Bounded Contexts ficam separados por pacote dentro de `src/main/java`:

```
src/main/java/com/ecommerce/
 ├── product/
 ├── customer/
 ├── order/
 ├── payment/
 └── shared/
```

### Estrutura DDD dentro de cada módulo

Cada contexto segue:

```
module/
 ├── domain/
 │    ├── model/
 │    ├── service/
 │    ├── repository/
 │    └── exception/
 ├── application/
 │    ├── usecase/
 │    └── dto/
 ├── infrastructure/
 │    └── repository/
 └── presentation/
      ├── *Controller.java
      └── *Request.java
```

## 🗄️ Banco de Dados

### Estratégia: PostgreSQL com Schemas Separados

- **1 banco PostgreSQL** com **4 schemas**:
  - `product_schema`
  - `customer_schema`
  - `order_schema`
  - `payment_schema`

### Flyway como Fonte Única de Verdade

Migrations em `src/main/resources/db/migration/v1/`:
- `V1__01_create_schemas.sql`
- `V1__02_create_customer_tables.sql`
- `V1__03_create_product_tables.sql`
- `V1__04_create_order_tables.sql`
- `V1__05_create_payment_tables.sql`

## 🚀 Como Executar

### 1. Subir PostgreSQL com Docker

```bash
docker-compose up -d
```

### 2. Compilar o Projeto

```bash
mvn clean install
```

### 3. Executar a Aplicação

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
- `POST /api/orders` - Criar pedido
- `GET /api/orders/{id}` - Buscar pedido
- `GET /api/orders/customer/{customerId}` - Listar pedidos do cliente
- `POST /api/orders/{id}/items` - Adicionar item ao pedido
- `POST /api/orders/{id}/pay` - Pagar pedido
- `POST /api/orders/{id}/cancel` - Cancelar pedido

### Payment
- `POST /api/payments` - Criar pagamento
- `GET /api/payments/{id}` - Buscar pagamento
- `GET /api/payments/order/{orderId}` - Listar pagamentos do pedido

## 🔄 Comunicação entre Módulos

### Atual (Monolito Modular)

- **Módulo `shared/`**: Classes comuns (BaseEntity, DomainEvent, BusinessException)
- **Referências por UUID**: Cada módulo referencia outros por UUID
- **Comunicação síncrona**: Services podem chamar outros Services diretamente

### Futuro (Microsserviços)

- **Eventos de domínio**: `OrderCreatedEvent`, `PaymentApprovedEvent`, etc.
- **Message broker**: RabbitMQ/Kafka para comunicação assíncrona
- **API Gateway**: Para comunicação síncrona entre serviços

## 🎯 Regras de Negócio Implementadas

### Order (Aggregate Root)
- ✅ Só pode ir de `PENDING` → `PAID`
- ✅ Nunca pode ir de `CANCELLED` → `PAID`
- ✅ Pedido pago não pode ser cancelado

### Product
- ✅ Validação de estoque antes de reduzir
- ✅ Produto deve estar ativo e com estoque para estar disponível

### Payment
- ✅ Só pode aprovar pagamentos `PENDING`

## 🛠️ Tecnologias

- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL 15
- Flyway (migrations)
- Lombok
- Maven (multi-módulo)

## 📝 Próximos Passos

1. ✅ Estrutura multi-módulo
2. ✅ Schemas separados
3. ✅ Flyway como fonte única
4. ⏳ Implementar eventos de domínio
5. ⏳ Migrar para WebFlux (não bloqueante)
6. ⏳ Extrair para microsserviços

## 📚 Documentação

- `docs/ARQUITETURA.md` - Explicação detalhada da arquitetura
- `docs/COMUNICACAO_MODULOS.md` - Como os módulos se comunicam
- `docs/GUIA_RAPIDO.md` - Guia rápido de testes
- `MIGRACAO_ESTRUTURA.md` - Guia para migrar arquivos para a nova estrutura
