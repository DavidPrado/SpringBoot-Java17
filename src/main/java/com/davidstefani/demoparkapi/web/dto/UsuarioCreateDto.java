package com.davidstefani.demoparkapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDto {

    @NotBlank
    @Email(message = "Formato do e-mail está invalido")
    private String username;
    @NotBlank
    @Size(min = 6, max = 6)
    private String password;
}
