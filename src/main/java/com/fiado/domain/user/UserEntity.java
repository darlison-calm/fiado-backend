package com.fiado.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "users"
)
public class UserEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @NotBlank(message = "Nome completo é obrigatório")
    private String fullName;

    @Email
    @Column(unique = true)
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Senha é obrigatório")
    private String password;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    @NotBlank(message = "Celular é obrigatório")
    private String phoneNumber;
}
