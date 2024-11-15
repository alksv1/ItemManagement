package com.rao.userservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
    @NotEmpty
    private String email;
    private String originalPassword;
    @NotEmpty
    private String newPassword;
}
