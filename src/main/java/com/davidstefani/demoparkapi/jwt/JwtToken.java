package com.davidstefani.demoparkapi.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
////Classe feita somente para gerar o token
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtToken {
    private String token;
}
