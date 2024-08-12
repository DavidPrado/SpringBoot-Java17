package com.davidstefani.demoparkapi.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
///Classe para interceptar quelquer requisição e validar o token
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService detailsService;
    //Metodo que vai interceptar as requisições
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);//recurar o token a partir do cabeçalho da requisição
            if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)){ // verifica se o token tem a instrução 'BEARER '
                log.info("JWT Token está nulo, vazio ou não iniciado com 'Bearer '.");
                filterChain.doFilter(request, response); // retorna a resposta da requisição sobre o erro
                return;
            }

            if(!JwtUtils.isTokenValid(token)){ //Verifica se é um token valido
                log.warn("JWT Token está invalido ou expirado.");
                filterChain.doFilter(request, response);// retorna a resposta da requisição sobre o erro
                return;
            }

            String username = JwtUtils.getUsernameFromToken(token); // retorna usuario do token
            toAuthentication(request, username);
            filterChain.doFilter(request, response); // Metodo de finalização de autenticação do token
    }

    private void toAuthentication(HttpServletRequest request, String username) {
        UserDetails userDetails = detailsService.loadUserByUsername(username); //retorna o usuario via detailsService
        //Verifica usuario no contexto do Spring
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); ///Passando o objeto de requisição para o Spring Security para autenticação e assim unir as operações de requisição e segurança
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); ///SecurityContextHolder classe que dá acesso ao contexto do Spring Security, e metodo que é utilizado para todas as autenticações do usuario
    }
}
