package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// @Configuration
// @EnableWebSecurity
public class SecurityConfig {

	/* versões anteriores ao spring 3.1
	@Bean
	public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
		return http.csrf().disable().
				sessionManagement().
				sessionCreationPolicy(SessionCreationPolicy.STATELESS).
				and().build();
	}
	*/
	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    return http.csrf(csrf -> csrf.disable())
//	    		 .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
//	    		 .authorizeHttpRequests(auth -> auth
//	                 .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()  // Permite acesso ao console H2
//	                 //.requestMatchers(new AntPathRequestMatcher("/login/**")).permitAll()  // Permite acesso ao console H2
//	                 //.requestMatchers(HttpMethod.POST, "/login").permitAll()
//	                 .anyRequest().authenticated()  // As outras requisições precisam ser autenticadas
//	             )
//	            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//	            .build();
//	}
	
	@Autowired
	private SecurityFilter securityFilter;
	
    @Bean // Exportar para que seja possível injetar em outra classe
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) //necessário para que o h2-console utilize iframe
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                		.requestMatchers(HttpMethod.POST, "/login").permitAll() // Libera o acesso ao endpoint de login
                		.requestMatchers("/h2-console/**").permitAll() // Libera o acesso ao h2-console
                		.requestMatchers("/swagger-ui.html").permitAll() // Libera o acesso ao SpringDoc
                        .anyRequest().authenticated() // Bloqueia os demais endpoints
                    )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //indica que deve chamar meu filter securityFilter antes do filter padrão do spring security (UsernamePasswordAuthenticationFilter)
                .build();
    }
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/login"))
        		;
//        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/login"))
//                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-ui.html", "/webjars/**");
    }
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

}
