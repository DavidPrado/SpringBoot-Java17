package com.davidstefani.demoparkapi.jwt;

import com.davidstefani.demoparkapi.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
///Spring Security utiliza um que se chama UserDetails é o objeto ao qual o Spring utiliza para armazenar as informações do usuario logado
//User é do pacote userdetails
public class JwtUserDetails extends User {

    private Usuario usuario;
    //Metodo construtor para gerar o userdetails
    public JwtUserDetails(Usuario usuario) {
        //AuthorityUtils.createAuthorityList metodo utilizado para passar o perfil do usuario
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }
    //retorna o id do usuario
    public Long getId(){
        return this.usuario.getId();
    }

    //retorna o perfil do usuario
    public String getRole(){
        return  this.usuario.getRole().name();
    }
}
