package br.com.pizzaria.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();




            if (authorityName.equals("ROLE_ENTREGADOR")) {
                response.sendRedirect("/entregador/entregas");
                return; // Encerra a execução
            }


            if (authorityName.equals("ROLE_GERENTE") ||
                    authorityName.equals("ROLE_CAIXA") ||
                    authorityName.equals("ROLE_COZINHA")) {


                response.sendRedirect("/funcionario/dashboard");
                return;
            }


            if (authorityName.equals("ROLE_CLIENTE")) {
                response.sendRedirect("/cliente/cardapio");
                return;
            }

            if (authorityName.equals("ROLE_GERENTE") || authorityName.equals("ROLE_CAIXA")) {
                response.sendRedirect("/funcionario/dashboard");
                return;
            }
        }


        response.sendRedirect("/home");
    }
}