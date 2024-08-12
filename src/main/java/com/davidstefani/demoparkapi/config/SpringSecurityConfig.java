package com.davidstefani.demoparkapi.config;

import com.davidstefani.demoparkapi.jwt.JwtAuthenticationEntryPoint;
import com.davidstefani.demoparkapi.jwt.JwtAuthorizationFilter;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableMethodSecurity  // Ativa de uma forma global a seguração do spring security no contexto do sistema
@EnableWebMvc // @EnableWebMvc é usada para habilitar o Spring MVC em um aplicativo e funciona importando a Configuração do Spring MVC do WebMvcConfigurationSupport.
@Configuration //@Configuration define a classe como uma classe de configuração
public class SpringSecurityConfig {

    private static final String[] DOCUMENTATION_OPENAPI = {
            "/docs/index.html",
            "/docs-park.html", "/docs-park/**",
            "/v3/api-docs/**",
            "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**",
            "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) //token de validação que chamado csrf é desativado para uma aplicação steless
                .formLogin(form -> form.disable()) //formulario de login é desativado, pois não havera um formulario de login
                .httpBasic(basic -> basic.disable()) //httpBasic tipo de altenticação que existe no spring que não tem segurança nenhum e por isso é desativado
                .authorizeHttpRequests(auth -> auth //Sistema de autorização
                        .requestMatchers(HttpMethod.POST, "api/v1/usuarios").permitAll() //requestMatchers define permissões de acesso para cada ,metodo atraves do mapeamento do API
                        .requestMatchers(HttpMethod.POST, "api/v1/auth").permitAll() //permitAll() permite que qualquer usuario utilize o metodo mapeado na API
                        .requestMatchers(DOCUMENTATION_OPENAPI).permitAll()
                        .anyRequest().authenticated() // anyRequest().authenticated() qualquer outra solicitação no sistema irá necessitar se autorização
                ).sessionManagement( // sessionManagement configuração de gerenciamento de sessão no sistema
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //sessionCreationPolicy defini a politica de sessão do sistema que é STATELESS
                ).addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro criado na aplicação na classe JwtAuthorizationFilter para a aplicação validar o token, usuario e senha em requisições na aplicação
                .exceptionHandling( ex -> ex //Função utilizada para filtrar e retornar erro 401 informando que o cliente não está autorizado(logado) a utilizar a API
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                ).build();// build retorna a configuração do SecurityFilterChain
    }
    //Metodo que estancia o filtro criado na classe JwtAuthorizationFilter
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter();
    }

    @Bean //Definir o tipo de criptografia que será utilizada no sistema que será BCryptPasswordEncoder
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean // Metodo referente ao gerenciamento de autenticação e o AuthenticationConfiguration faz parte da biblioteca de segurança do spring security
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
