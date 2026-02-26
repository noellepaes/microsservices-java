package com.ecommerce.payment.infrastructure.service;

import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.model.PaymentStatus;
import com.ecommerce.payment.domain.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StripePaymentProcessor implements PaymentProcessor {
    
    @Override
    public void process(Payment payment) {
        log.info("Processando pagamento {} via Stripe", payment.getId());
        
        // Simulação de processamento de pagamento
        // Em produção, aqui seria a integração real com Stripe
        
        try {
            // Simula chamada à API do Stripe
            boolean success = simulateStripeCall(payment);
            
            if (success) {
                payment.approve();
                log.info("Pagamento {} aprovado", payment.getId());
            } else {
                payment.fail();
                log.warn("Pagamento {} falhou", payment.getId());
            }
        } catch (Exception e) {
            payment.fail();
            log.error("Erro ao processar pagamento {}", payment.getId(), e);
            throw e;
        }
    }
    
    private boolean simulateStripeCall(Payment payment) {
        // Simulação: aprova pagamentos com valor > 0
        return payment.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0;
    }
}
