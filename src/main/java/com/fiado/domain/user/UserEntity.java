package com.fiado.domain.user;


import com.fiado.domain.phone.PhoneNumberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"phoneNumber_value", "phoneNumber_locale"})
        }
)
public class UserEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true)
    private String username;


    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Embedded()
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "phoneNumber_value")),
            @AttributeOverride(name = "locale", column = @Column(name = "phoneNumber_locale"))
    })
    private PhoneNumberEntity phoneNumber;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
