package com.fiado.domain.clients;

import com.fiado.domain.shared.BaseEntity;
import com.fiado.domain.user.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name="clients"
)
public class ClientEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String address;

    private String phoneNumber;

    private String observation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private UserEntity user;
}
