package cr.ac.una.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import cr.ac.una.app.config.handlers.CustomAuthenticationFailureHandler;
import cr.ac.una.app.service.UsuarioService;

/**
 * Esta clase es la encargada de todos los aspectos
 * de la Seguridad dentro de la aplicación, así como
 * del Inicio de Sesión dentro del Sistema.
 * 
 * @author Isaac Herrera
 */

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	UsuarioService usuarioService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
		return httpSecurity
			.csrf(Customizer.withDefaults())
			.authorizeHttpRequests(auth -> {
				auth.requestMatchers(
					"/register**",
					"/login**",
					"/css/**",
					"/js/**",
					"/img/**",
					"/api/v1/paciente/register"
				).permitAll();
				auth.anyRequest().authenticated();
			})
			.sessionManagement(session -> {
				session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
			})
			.formLogin((formLogin) ->
				formLogin
					.loginPage("/login")
					.loginProcessingUrl("/auth/v1/login")
					.failureHandler(authenticationFailureHandler("/login"))
					.permitAll()
			)
			.logout((logout) ->
				logout
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessUrl("/login?logout")
					.deleteCookies("JSESSIONID")
					.permitAll()
			)
			.build()
		;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    public AuthenticationFailureHandler authenticationFailureHandler(String loginPage) {
        return new CustomAuthenticationFailureHandler(loginPage);
    }

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
		httpSecurity
			.getSharedObject(AuthenticationManagerBuilder.class)
			.userDetailsService(usuarioService)
			.passwordEncoder(passwordEncoder)
		;
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
	}

}