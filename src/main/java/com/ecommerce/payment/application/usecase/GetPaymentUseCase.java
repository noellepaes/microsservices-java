package com.ecommerce.payment.application.usecase;

import com.ecommerce.payment.application.dto.PaymentDTO;
import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPaymentUseCase {
    
    private final PaymentRepository repository;
    
    @Transactional(readOnly = true)
    public PaymentDTO findById(UUID id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado"));
        return PaymentDTO.from(payment);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentDTO> findByOrderId(UUID orderId) {
        return repository.findByOrderId(orderId).stream()
                .map(PaymentDTO::from)
                .collect(Collectors.toList());
    }
}
