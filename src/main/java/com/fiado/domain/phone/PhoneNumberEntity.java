package com.fiado.domain.phone;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class PhoneNumberEntity {
    @NotEmpty
    private String value;

    @NotEmpty
    private String locale;
}