package com.rao.userservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmailDto {
    @NotEmpty
    private String originalEmail;
    @NotEmpty
    private String password;
    @NotEmpty
    private String newEmail;
    @NotEmpty
    private String encodedCode;
    @NotEmpty
    private String captcha;
}
