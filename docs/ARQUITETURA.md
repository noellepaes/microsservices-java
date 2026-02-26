# 🏗️ Arquitetura da Aplicação

## 📋 Visão Geral

Esta é uma aplicação **monolito modular** em Java 21, preparada para evoluir para microsserviços. A arquitetura segue **DDD (Domain-Driven Design)** com separação clara de responsabilidades.

## 🎯 Princípios Arquiteturais

### 1. **Separação por Bounded Contexts**
Cada módulo (`product`, `customer`, `order`, `payment`) é um **Bounded Context** isolado:
- Seu próprio schema no banco de dados
- Suas próprias regras de negócio
- Suas próprias exceções de domínio
- Comunicação via UUIDs (preparado para microsserviços)

### 2. **Camadas DDD**

Cada módulo segue a estrutura:

```
module/
 ├── domain/          # 🧠 Coração do negócio
 │    ├── model/      # Entidades com regras de negócio
 │    ├── service/    # Interfaces para serviços externos
 │    ├── repository/ # Interfaces de repositório
 │    └── exception/  # Exceções específicas do domínio
 │
 ├── application/     # 🔄 Orquestração
 │    ├── usecase/    # Casos de uso (orquestram o domínio)
 │    └── dto/        # DTOs internos da aplicação
 │
 ├── infrastructure/  # 🔧 Implementações técnicas
 │    ├── persistence/# Implementações JPA
 │    └── service/    # Implementações de serviços externos
 │
 └── presentation/    # 🌐 Interface HTTP
      └── controller/ # Controllers REST + Request DTOs
```

## 🔄 Como Funciona

### Fluxo de uma Requisição

```
HTTP Request
    ↓
Controller (presentation)
    ↓
UseCase (application)
    ↓
Domain Model (domain) ← Regras de negócio aqui!
    ↓
Repository (infrastructure)
    ↓
Database
```

### Exemplo: Criar um Pedido

1. **Controller** (`OrderController.createOrder`)
   - Recebe `OrderRequest` (DTO HTTP)
   - Chama o UseCase

2. **UseCase** (`CreateOrderUseCase.execute`)
   - Cria a entidade `Order` (domínio)
   - Salva via `OrderRepository`
   - Retorna `OrderDTO` (DTO interno)

3. **Domain** (`Order`)
   - **Regras de negócio aqui!**
   - Ex: `order.pay()` valida se pode pagar
   - Lança `OrderException` se regra violada

4. **Infrastructure** (`JpaOrderRepository`)
   - Implementa `OrderRepository` (interface do domínio)
   - Persiste no banco via JPA

## 🧠 Sobre o Shared

### O que DEVE estar no Shared?

✅ **Apenas genéricos** que todos os módulos precisam:

- `BaseEntity` - Entidade base com UUID, timestamps, version
- `DomainEvent` / `BaseDomainEvent` - Base para eventos de domínio
- `BusinessException` - Exceção genérica de negócio
- `GlobalExceptionHandler` - Tratamento global de exceções

### O que NÃO deve estar no Shared?

❌ **Nada específico de um módulo**:

- ❌ `OrderException` - Cada módulo tem suas próprias exceções
- ❌ `PaymentStatus` - Cada módulo tem seus próprios enums
- ❌ DTOs específicos - Cada módulo tem seus próprios DTOs

### Por que isso é importante?

**Shared é o núcleo mais baixo**. Ele não pode depender dos módulos, senão quebra a arquitetura:

```
❌ ERRADO:
shared → order (dependência invertida!)

✅ CORRETO:
order → shared (módulos dependem do shared)
```

## 🎯 Exceções de Domínio

### Arquitetura Correta

Todas as exceções de domínio **herdam de `BusinessException`**:

```java
// ✅ CORRETO
public class OrderException extends BusinessException {
    public OrderException(String message) {
        super(message);
    }
}
```

**Por quê?**

1. `GlobalExceptionHandler` trata apenas `BusinessException`
2. Não precisa importar exceções específicas
3. Shared não depende dos módulos
4. Polimorfismo funciona automaticamente

### Fluxo de Exceção

```
Domain Model (Order.pay())
    ↓
Lança OrderException extends BusinessException
    ↓
GlobalExceptionHandler captura BusinessException
    ↓
Retorna HTTP 400 com mensagem
```

## 🗄️ Banco de Dados

### Estratégia: Schemas Separados

**1 banco PostgreSQL** com **4 schemas**:

- `product_schema` - Produtos
- `customer_schema` - Clientes  
- `order_schema` - Pedidos
- `payment_schema` - Pagamentos

### Flyway como Fonte Única de Verdade

✅ **Flyway cria tudo**:
- Schemas (V1)
- Tabelas (V2, V3, V4, V5)
- Índices
- Constraints

❌ **Hibernate NÃO cria**:
- `spring.jpa.hibernate.ddl-auto=validate`
- Apenas valida que o schema existe

### Por quê?

1. **Versionamento** - Controle de mudanças no banco
2. **Consistência** - Mesmo schema em todos os ambientes
3. **Microsserviços** - Fácil migrar para bancos separados
4. **Histórico** - Rastreabilidade de mudanças

## 🔄 Comunicação entre Módulos

### Atual (Monolito Modular)

**Referências por UUID**:

- `Order.customerId` → UUID (não entidade `Customer`)
- `OrderItem.productId` → UUID (não entidade `Product`)
- `Payment.orderId` → UUID (não entidade `Order`)

**Vantagem**: Facilita migração futura para microsserviços.

### Futuro (Microsserviços)

**Eventos de Domínio**:

- `Order` publica `OrderCreatedEvent`
- `Payment` consome `OrderCreatedEvent`
- Comunicação assíncrona via message broker

## 📦 Estrutura de um Módulo Completo

### Exemplo: Payment

```
payment/
 ├── domain/
 │     ├── model/
 │     │     ├── Payment.java          ← Regras: approve(), fail()
 │     │     ├── PaymentStatus.java
 │     │     └── Money.java
 │     ├── service/
 │     │     └── PaymentProcessor.java ← Interface (gateway externo)
 │     ├── repository/
 │     │     └── PaymentRepository.java ← Interface
 │     └── exception/
 │           └── PaymentFailedException.java ← extends BusinessException
 │
 ├── application/
 │     ├── usecase/
 │     │     ├── ProcessPaymentUseCase.java ← Orquestra
 │     │     └── GetPaymentUseCase.java
 │     └── dto/
 │           └── PaymentDTO.java ← DTO interno
 │
 ├── infrastructure/
 │     ├── persistence/
 │     │     └── JpaPaymentRepository.java ← Implementa PaymentRepository
 │     └── service/
 │           └── StripePaymentProcessor.java ← Implementa PaymentProcessor
 │
 └── presentation/
       ├── PaymentController.java
       └── PaymentRequest.java ← DTO HTTP
```

## 🎯 Regras de Negócio no Domínio

**Regra de ouro**: Regras de negócio **sempre no domínio**, nunca no controller ou service.

### Exemplo: Payment

```java
// ✅ CORRETO - Regra no domínio
public class Payment {
    public void approve() {
        if (this.status != PaymentStatus.PENDING) {
            throw new PaymentFailedException("Pagamento já processado");
        }
        this.status = PaymentStatus.APPROVED;
    }
}

// ❌ ERRADO - Regra no controller
@PostMapping("/approve")
public void approve(@PathVariable UUID id) {
    Payment payment = repository.findById(id);
    if (payment.getStatus() != PENDING) { // ❌ Regra no controller!
        throw new Exception("...");
    }
    payment.setStatus(APPROVED);
}
```

## 🚀 Próximos Passos

1. ✅ Estrutura DDD completa
2. ✅ Schemas separados
3. ✅ Flyway como fonte única
4. ⏳ Eventos de domínio (quando necessário)
5. ⏳ Migrar para WebFlux (não bloqueante)
6. ⏳ Extrair para microsserviços
