# 🔄 Comunicação entre Módulos

## Estratégia Atual (Monolito Modular)

### 1. Módulo `shared/`

O módulo `shared/` contém classes comuns usadas por todos os módulos:

- **BaseEntity**: Entidade base com `id`, `createdAt`, `updatedAt`, `version`
- **DomainEvent**: Interface para eventos de domínio
- **BaseDomainEvent**: Classe base para eventos
- **BusinessException**: Exceção de negócio
- **GlobalExceptionHandler**: Tratamento global de exceções

### 2. Referências por UUID

Os módulos se comunicam através de **UUIDs** (identificadores):

- `Order` referencia `Customer` por `customerId` (UUID)
- `OrderItem` referencia `Product` por `productId` (UUID)
- `Payment` referencia `Order` por `orderId` (UUID)

**Vantagem**: Facilita migração futura para microsserviços, onde cada serviço terá seu próprio banco.

### 3. Comunicação Síncrona (Atual)

Atualmente, a comunicação é **síncrona** através de:

- **Services**: Cada módulo expõe serviços que podem ser chamados por outros módulos
- **Controllers REST**: Endpoints HTTP para comunicação externa

**Exemplo**:
```java
// OrderService pode chamar CustomerService para validar cliente
Customer customer = customerService.findById(customerId);
```

## 🚀 Estratégia Futura (Microsserviços)

### 1. Eventos de Domínio

Quando migrar para microsserviços, use **Eventos de Domínio** para comunicação assíncrona:

```java
// Exemplo: OrderCreatedEvent
public class OrderCreatedEvent extends BaseDomainEvent {
    private UUID orderId;
    private UUID customerId;
    private BigDecimal totalAmount;
    // ...
}
```

### 2. Message Broker

Use um message broker (RabbitMQ, Kafka, etc.) para publicar/consumir eventos:

- **Order** publica `OrderCreatedEvent`
- **Payment** consome `OrderCreatedEvent` e cria pagamento
- **Product** consome `OrderItemAddedEvent` e reduz estoque

### 3. API Gateway

Para comunicação síncrona entre microsserviços:

- **API Gateway** (Spring Cloud Gateway)
- **Service Discovery** (Eureka, Consul)
- **Circuit Breaker** (Resilience4j)

## 📋 Exemplo de Implementação Futura

### Evento de Domínio

```java
// shared/domain/event/OrderCreatedEvent.java
public class OrderCreatedEvent extends BaseDomainEvent {
    private UUID orderId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> items;
}
```

### Publisher (Order Module)

```java
// order/application/OrderService.java
@Service
public class OrderService {
    private final EventPublisher eventPublisher;
    
    public Order createOrder(UUID customerId) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order = orderRepository.save(order);
        
        // Publica evento
        eventPublisher.publish(new OrderCreatedEvent(
            order.getId(),
            order.getCustomerId(),
            order.getTotalAmount()
        ));
        
        return order;
    }
}
```

### Consumer (Payment Module)

```java
// payment/application/PaymentEventHandler.java
@Component
public class PaymentEventHandler {
    
    @EventListener
    public void handle(OrderCreatedEvent event) {
        // Cria pagamento automaticamente quando pedido é criado
        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        payment.setAmount(event.getTotalAmount());
        paymentRepository.save(payment);
    }
}
```

## 🎯 Resumo

| Aspecto | Monolito Modular (Atual) | Microsserviços (Futuro) |
|---------|-------------------------|------------------------|
| **Comunicação** | Chamadas diretas entre Services | Eventos + API Gateway |
| **Banco de Dados** | 1 PostgreSQL com schemas | 1 banco por serviço |
| **Transações** | Transações ACID locais | Saga Pattern |
| **Deploy** | 1 aplicação | N aplicações |
