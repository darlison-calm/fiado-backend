package com.fiado.domain.sales.dtos;
import java.math.BigDecimal;
import java.util.List;

public record SaleCreateRequestDto(
        Long clientId,
        BigDecimal grossAmount,
        BigDecimal interestRate,
        BigDecimal totalAmount,
        String description,
        List<InstallmentRequestDto> installments
) {}
