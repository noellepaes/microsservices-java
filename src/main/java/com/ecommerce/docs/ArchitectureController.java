package com.ecommerce.docs;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class ArchitectureController {

    private record ModuleView(
            String id,
            String name,
            String description,
            String basePackage,
            List<String> responsibilities,
            List<String> mainUseCases,
            List<String> mainEndpoints
    ) {}

    private List<ModuleView> modules() {
        return List.of(
                new ModuleView(
                        "product",
                        "Product",
                        "Gerencia produtos, preços e estoque.",
                        "com.ecommerce.product",
                        List.of(
                                "Entidades de produto (domain/model)",
                                "Regra de estoque e disponibilidade",
                                "Repositórios JPA de produto"
                        ),
                        List.of(
                                "CreateProductUseCase",
                                "GetProductUseCase",
                                "UpdateProductUseCase",
                                "DecreaseStockUseCase"
                        ),
                        List.of(
                                "GET /api/products",
                                "GET /api/products/{id}",
                                "POST /api/products",
                                "PUT /api/products/{id}",
                                "POST /api/products/{id}/decrease-stock"
                        )
                ),
                new ModuleView(
                        "customer",
                        "Customer",
                        "Gerencia clientes, cadastro e status.",
                        "com.ecommerce.customer",
                        List.of(
                                "Entidade Customer e status (domain/model)",
                                "Validação de email/CPF únicos",
                                "Repositórios e controllers de cliente"
                        ),
                        List.of(
                                "CreateCustomerUseCase",
                                "GetCustomerUseCase",
                                "UpdateCustomerUseCase",
                                "DeactivateCustomerUseCase"
                        ),
                        List.of(
                                "GET /api/customers",
                                "GET /api/customers/{id}",
                                "POST /api/customers",
                                "PUT /api/customers/{id}",
                                "DELETE /api/customers/{id}"
                        )
                ),
                new ModuleView(
                        "order",
                        "Order",
                        "Gerencia pedidos, itens e fluxo de status.",
                        "com.ecommerce.order",
                        List.of(
                                "Aggregate root Order com itens (domain/model)",
                                "Regras de transição de status (PENDING, PAID, CANCELLED, ...)",
                                "Cálculo de total do pedido"
                        ),
                        List.of(
                                "CreateOrderUseCase",
                                "AddItemToOrderUseCase",
                                "GetOrderUseCase",
                                "PayOrderUseCase",
                                "CancelOrderUseCase"
                        ),
                        List.of(
                                "POST /api/orders",
                                "GET /api/orders/{id}",
                                "GET /api/orders/customer/{customerId}",
                                "POST /api/orders/{id}/items",
                                "POST /api/orders/{id}/pay",
                                "POST /api/orders/{id}/cancel"
                        )
                ),
                new ModuleView(
                        "payment",
                        "Payment",
                        "Responsável pelo processamento de pagamentos.",
                        "com.ecommerce.payment",
                        List.of(
                                "Entidade Payment, status e método (domain/model)",
                                "Processamento via PaymentProcessor (domain/service)",
                                "Integração simulada StripePaymentProcessor (infrastructure/service)"
                        ),
                        List.of(
                                "ProcessPaymentUseCase",
                                "GetPaymentUseCase"
                        ),
                        List.of(
                                "POST /api/payments",
                                "GET /api/payments/{id}",
                                "GET /api/payments/order/{orderId}"
                        )
                ),
                new ModuleView(
                        "shared",
                        "Shared (Kernel Compartilhado)",
                        "Tipos genéricos reutilizados pelos outros módulos.",
                        "com.ecommerce.shared",
                        List.of(
                                "BaseEntity com id/createdAt/updatedAt/version",
                                "Eventos de domínio (DomainEvent, BaseDomainEvent)",
                                "BusinessException e GlobalExceptionHandler"
                        ),
                        List.of(
                                "Uso indireto em todas as entidades e exceções de domínio"
                        ),
                        List.of(
                                "Tratamento global de erros HTTP via GlobalExceptionHandler"
                        )
                )
        );
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("modules", modules());
        return "index";
    }

    @GetMapping("/modules/{id}")
    public String moduleDetails(@PathVariable String id, Model model) {
        ModuleView module = modules().stream()
                .filter(m -> m.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);

        if (module == null) {
            model.addAttribute("error", "Módulo não encontrado: " + id);
            return "module";
        }

        model.addAttribute("module", module);
        return "module";
    }
}

