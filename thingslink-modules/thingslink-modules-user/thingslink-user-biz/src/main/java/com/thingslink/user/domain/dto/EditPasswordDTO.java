package com.thingslink.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditPasswordDTO {
    @NotBlank(message = "[oldPassword] {validate.notnull}")
    private String oldPassword;

    @NotBlank(message = "[newPassword] {validate.notnull}")
    private String newPassword;
}
