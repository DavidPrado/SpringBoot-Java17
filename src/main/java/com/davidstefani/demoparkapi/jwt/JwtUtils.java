package com.davidstefani.demoparkapi.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtils {

    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "dGZYWcjC5O0oCwoigw6R5ajCEBBjjqXq";
    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 24;
    public static final long EXPIRE_MINUTES = 0;

    private JwtUtils(){
    }
    //metodo que vai pegar a chave a vai gerar a chave automaticamente.
    private static Key generateKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        //o metido hmacShaKeyFor será responsavel por preparar a SECRET_KEY para gerar um token
    }

    //metodo para expirar o token
    private static Date toExpireDate(Date start){
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    //metodo que vai gerar o token retornando como objeto JwtToken
    public static JwtToken createToken(String username, String role){
        //Primeiro passo gera a data inicial do token
        Date issuedAt = new Date();
        //Segundo passo gera a data limite do token com o metodo toExpireDate
        Date limit = toExpireDate(issuedAt);

        //Para gerar o token utiliza alguns recursos da biblioteca Jwt
        String token = Jwts.builder() ///builder é um metido utilizado para acessar os metodos para a geração do token
                .setHeaderParam("typ", "JWT") //metodo para indicar que o token é um JWT
                .setSubject(username)//setSubject é para passar o nome do usuario ou id do usuario para confirmar se esse token é realmente de um usuario existente na aplicação fazendo consulta no banco de dados
                .setIssuedAt(issuedAt)//setIssuedAt é data de geração do token
                .setExpiration(limit)//setExpiration data limite do token para expirar
                .signWith(generateKey(), SignatureAlgorithm.HS256) // signWith add a acinatura do token com o metodo criado generateKey() e como segundo parametro a criptografia do token que é SignatureAlgorithm.HS256
                .claim("role", role) //claim metodo de chave e valor que pode ser utilizado quando não existe algum metodo para isso
                .compact();//transforma esse objeto em uma string com o padrão base64 separando cada um dos pontos

        return new JwtToken(token); //retorna o token
    }
    //Para recuperar o conteudo do token é utilizado o metodo getClaimsFromToken
    private static Claims getClaimsFromToken(String token){
        try {
            //Metodo de assinatura para verificar se o token é compativel com a nossa assinatura
            return Jwts.parser().setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();

        }catch (JwtException ex){
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return null;
    }
    //retorna o username do token
    public static String getUsernameFromToken(String token){
        return getClaimsFromToken(token).getSubject();
    }
    //metodo para testar a validade do token
    public static  boolean isTokenValid(String token){
        try {
             Jwts.parser().setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token));
             return true;
        }catch (JwtException ex){
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return false;
    }
    ///metido para retornar o token sem o BEARER do token
    private static String refactorToken(String token){
        if (token.contains(JWT_BEARER)){
            return token.substring(JWT_BEARER.length());
        }

        return token;
    }
}
