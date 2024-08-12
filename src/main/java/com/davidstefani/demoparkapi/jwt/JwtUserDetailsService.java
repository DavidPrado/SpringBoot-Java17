package com.davidstefani.demoparkapi.jwt;

import com.davidstefani.demoparkapi.entity.Usuario;
import com.davidstefani.demoparkapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//UserDetailsService classe do Spring Security utilizada para localizar o usuario
@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    //metodo utilizado para localizar o usuario no banco de dados
    //Este metodo pode ser utilizada para passar o usuario atual logado na sessão do spring
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username); //faz a consulto pelo usuario
        return new JwtUserDetails(usuario);//retonar um JwtUserDetails
    }

    //Metodo utilizado para gerar o token
    public JwtToken getTokenAuthenticated(String username){
        //Primeiro passo retornar o perfil do usuario
        Usuario.Role role = usuarioService.buscarRolePorUsername(username);
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));// Chama o metodo para criar o Token feito na classe JwtUtils
    }


}
