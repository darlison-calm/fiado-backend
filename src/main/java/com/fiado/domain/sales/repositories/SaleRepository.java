package com.fiado.domain.sales.repositories;

import com.fiado.domain.sales.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository <SaleEntity,Long> {
}
