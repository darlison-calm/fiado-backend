package com.fiado.domain.user.entities;


import com.fiado.domain.clients.ClientEntity;
import com.fiado.domain.phone.PhoneNumberEntity;
import com.fiado.domain.user.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"phoneNumber_value", "phoneNumber_locale"} , name = "users_unique_phone_number_idx"),
                @UniqueConstraint(columnNames =  {"email"}, name = "users_unique_email_idx"),
                @UniqueConstraint(columnNames = {"username"}, name = "users_unique_username_idx")
        }
)
public class UserEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;

    @Embedded()
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "phoneNumber_value")),
            @AttributeOverride(name = "locale", column = @Column(name = "phoneNumber_locale"))
    })
    private PhoneNumberEntity phoneNumber;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ClientEntity> clients;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
