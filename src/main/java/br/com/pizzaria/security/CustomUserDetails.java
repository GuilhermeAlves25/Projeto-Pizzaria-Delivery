package br.com.pizzaria.security;

import br.com.pizzaria.model.Cliente;
import br.com.pizzaria.model.Funcionario;
import br.com.pizzaria.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    // Este método é a chave! Ele permite o acesso ao nosso objeto Usuario completo.
    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (usuario instanceof Funcionario) {
            Funcionario funcionario = (Funcionario) usuario;
            return Stream.of(new SimpleGrantedAuthority("ROLE_" + funcionario.getCargo().name()))
                    .collect(Collectors.toList());
        } else if (usuario instanceof Cliente) {
            return Stream.of(new SimpleGrantedAuthority("ROLE_CLIENTE"))
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override public String getPassword() { return usuario.getSenha(); }
    @Override public String getUsername() { return usuario.getEmail(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}