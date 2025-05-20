package com.fiado.domain.sales.interfaces;

import com.fiado.domain.sales.entities.SaleEntity;
import com.fiado.domain.sales.dtos.SaleCreateRequestDto;

import java.util.UUID;

public interface SaleService {
    SaleEntity createSale(SaleCreateRequestDto saleDto, UUID userId);
}
