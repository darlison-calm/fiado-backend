package com.fiado.domain.sales.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InstallmentRequestDto(
        BigDecimal value,
        LocalDateTime deadline) {
}
