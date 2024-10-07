package med.voll.api.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.repository.UsuarioRepository;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("Passou pelo filtro do Spring..");
		
		var tokenJwt = recuperarToken(request);
		
		if(tokenJwt != null) {
			//valida o token e recupera o subject
			var subject = tokenService.getSubject(tokenJwt);
			
			var usuario = usuarioRepository.findByLogin(subject);
			
			var authentication = new UsernamePasswordAuthenticationToken(usuario,null, usuario.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			System.out.println("Subject: "+subject);
		}
		
		
		//necessário seguir o fluxo de filtros para não interromper a requisição aqui
		filterChain.doFilter(request, response);
	}
	
	private String recuperarToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		
		if(authorizationHeader != null) {
			return authorizationHeader.replace("Bearer ", "");
		}
		
		return null;
	}

}
