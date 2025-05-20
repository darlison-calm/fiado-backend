package com.fiado.domain.sales.repositories;

import com.fiado.domain.sales.entities.InstallmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentRepository extends JpaRepository <InstallmentEntity,Long> {
}
