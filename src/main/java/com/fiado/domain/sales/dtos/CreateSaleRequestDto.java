package com.fiado.domain.sales.dtos;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record CreateSaleRequestDto(
        @NotNull(message = "Cliente é obrigatório")
        Long clientId,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor bruto deve ser maior que zero")
        BigDecimal grossAmount,

        @DecimalMin(value = "0.00", message = "Taxa de juros não pode ser negativa")
        BigDecimal interestRate,

        @NotNull(message = "Valor total é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor bruto deve ser maior que zero")
        BigDecimal totalAmount,

        String description,

        List<InstallmentRequestDto> installments
) {}
