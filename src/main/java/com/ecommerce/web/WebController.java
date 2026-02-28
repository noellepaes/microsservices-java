package com.ecommerce.web;

import com.ecommerce.customer.application.dto.CustomerDTO;
import com.ecommerce.customer.application.usecase.GetCustomerUseCase;
import com.ecommerce.order.application.dto.OrderDTO;
import com.ecommerce.order.application.usecase.GetOrderUseCase;
import com.ecommerce.payment.application.dto.PaymentDTO;
import com.ecommerce.payment.application.usecase.GetPaymentUseCase;
import com.ecommerce.product.application.dto.ProductDTO;
import com.ecommerce.product.application.usecase.GetProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final GetCustomerUseCase getCustomerUseCase;
    private final GetProductUseCase getProductUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final GetPaymentUseCase getPaymentUseCase;

    @GetMapping("/customers")
    public String customersPage(Model model) {
        List<CustomerDTO> customers = getCustomerUseCase.findAll();
        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/products")
    public String productsPage(Model model) {
        List<ProductDTO> products = getProductUseCase.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/orders")
    public String ordersPage(Model model) {
        List<OrderDTO> orders = getOrderUseCase.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/payments")
    public String paymentsPage(Model model) {
        List<PaymentDTO> payments = getPaymentUseCase.findAll();
        model.addAttribute("payments", payments);
        return "payments";
    }

    @GetMapping("/orders/customer/{customerId}")
    public String ordersByCustomerPage(@PathVariable UUID customerId, Model model) {
        List<OrderDTO> orders = getOrderUseCase.findByCustomerId(customerId);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/payments/order/{orderId}")
    public String paymentsByOrderPage(@PathVariable UUID orderId, Model model) {
        List<PaymentDTO> payments = getPaymentUseCase.findByOrderId(orderId);
        model.addAttribute("payments", payments);
        return "payments";
    }
}

