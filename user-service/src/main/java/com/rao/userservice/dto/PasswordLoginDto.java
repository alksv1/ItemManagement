package com.rao.userservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordLoginDto {
    @NotEmpty
    private String account;
    @NotEmpty
    private String password;
}
