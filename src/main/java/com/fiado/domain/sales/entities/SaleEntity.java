package com.fiado.domain.sales.entities;

import com.fiado.domain.clients.ClientEntity;
import com.fiado.domain.clients.dto.ClientResponseDto;
import com.fiado.domain.sales.enums.SaleStatus;
import com.fiado.domain.shared.BaseEntity;
import com.fiado.domain.user.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name="sales")
@Entity
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class SaleEntity extends BaseEntity {

    public SaleEntity() {
        this.installments = new ArrayList<>();
    }
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "client_id")
    private ClientEntity client;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal grossAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus status;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstallmentEntity> installments;

    private String description;
}
