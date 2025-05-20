package com.fiado.domain.sales.entities;
import com.fiado.domain.sales.enums.IntallmentStatus;
import com.fiado.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Table(name = "installments")
@Entity
@Data
public class InstallmentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "sale_id")
    private SaleEntity sale;

    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntallmentStatus status;

    @Column(precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @OneToMany(mappedBy = "installment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();;

    private LocalDateTime deadLine;
}
