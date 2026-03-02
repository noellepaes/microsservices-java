package com.ecommerce.config;

import com.ecommerce.customer.application.dto.CustomerDTO;
import com.ecommerce.customer.application.usecase.CreateCustomerUseCase;
import com.ecommerce.order.application.dto.OrderDTO;
import com.ecommerce.order.application.usecase.CreateOrderUseCase;
import com.ecommerce.order.application.usecase.AddItemToOrderUseCase;
import com.ecommerce.payment.application.usecase.ProcessPaymentUseCase;
import com.ecommerce.payment.domain.model.PaymentMethod;
import com.ecommerce.product.application.dto.ProductDTO;
import com.ecommerce.product.application.usecase.CreateProductUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class MockDataInitializer implements CommandLineRunner {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final CreateOrderUseCase createOrderUseCase;
    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final ProcessPaymentUseCase processPaymentUseCase;

    @Override
    public void run(String... args) {
        log.info("Inicializando dados mockados...");

        try {
            // Criar clientes mockados
            CustomerDTO customer1 = createCustomerUseCase.execute(new CustomerDTO(
                    null, "João Silva", "joao.silva@email.com", "12345678900", null, null, null
            ));
            log.info("Cliente criado: {}", customer1.id());

            CustomerDTO customer2 = createCustomerUseCase.execute(new CustomerDTO(
                    null, "Maria Santos", "maria.santos@email.com", "98765432100", null, null, null
            ));
            log.info("Cliente criado: {}", customer2.id());

            // Criar produtos mockados
            ProductDTO product1 = createProductUseCase.execute(new ProductDTO(
                    null, "Notebook Dell", "Notebook Dell Inspiron 15", 
                    new BigDecimal("3500.00"), 10, null, null, null
            ));
            log.info("Produto criado: {} - {}", product1.id(), product1.name());

            ProductDTO product2 = createProductUseCase.execute(new ProductDTO(
                    null, "Mouse Logitech", "Mouse sem fio Logitech MX Master 3", 
                    new BigDecimal("450.00"), 25, null, null, null
            ));
            log.info("Produto criado: {} - {}", product2.id(), product2.name());

            ProductDTO product3 = createProductUseCase.execute(new ProductDTO(
                    null, "Teclado Mecânico", "Teclado mecânico RGB", 
                    new BigDecimal("650.00"), 15, null, null, null
            ));
            log.info("Produto criado: {} - {}", product3.id(), product3.name());

            // Criar pedidos mockados
            OrderDTO order1 = createOrderUseCase.execute(customer1.id());
            order1 = addItemToOrderUseCase.execute(order1.id(), product1.id(), product1.name(), 1, product1.price());
            order1 = addItemToOrderUseCase.execute(order1.id(), product2.id(), product2.name(), 2, product2.price());
            log.info("Pedido criado: {} - Total: {}", order1.id(), order1.totalAmount());

            OrderDTO order2 = createOrderUseCase.execute(customer2.id());
            order2 = addItemToOrderUseCase.execute(order2.id(), product3.id(), product3.name(), 1, product3.price());
            log.info("Pedido criado: {} - Total: {}", order2.id(), order2.totalAmount());

            // Criar pagamentos mockados
            BigDecimal order1Total = order1.totalAmount();
            processPaymentUseCase.execute(order1.id(), order1Total, PaymentMethod.CREDIT_CARD);
            log.info("Pagamento processado para pedido: {}", order1.id());

            log.info("Dados mockados inicializados com sucesso!");
            log.info("Acesse o Swagger UI em: http://localhost:8080/swagger-ui.html");

        } catch (Exception e) {
            log.error("Erro ao inicializar dados mockados", e);
        }
    }
}
