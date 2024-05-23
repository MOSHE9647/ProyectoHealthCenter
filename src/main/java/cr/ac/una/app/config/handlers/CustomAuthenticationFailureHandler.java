package cr.ac.una.app.config.handlers;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final String loginPage;

    public CustomAuthenticationFailureHandler(String loginPage) {
        this.loginPage = loginPage;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
 
        String errorMessage = "";
        if (authException instanceof BadCredentialsException) {
            errorMessage = "Usuario y/o Contraseña inválida.";
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } else if (authException instanceof AccountStatusException) {
            errorMessage = "Su cuenta está deshabilitada.";
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            response.sendRedirect(loginPage);
        }

        String exceptionClassName = authException.getClass().getName();
        log.error("Error: {}", exceptionClassName);

        if (StringUtils.isNotBlank(errorMessage)) {
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", errorMessage);
            response.sendRedirect(loginPage + "?error=true");
        }
    }
}