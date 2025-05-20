package com.fiado.domain.sales.repositories;

import com.fiado.domain.sales.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository <PaymentEntity,Long> {
}
