package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import med.voll.api.model.Usuario;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	private final String ISSUE = "API vollmed";

	public String gerarToken(Usuario usuario) {
		try {
			Algorithm algoritmo = Algorithm.HMAC256(secret);

			String token = JWT.create()
					.withIssuer(ISSUE) // identificação da aplicação
					.withSubject(usuario.getLogin())
					//.withClaim(null, null) //se eu quiser passar mais informações n vezes na chave. (chave, valor)
					.withClaim("id", usuario.getId())
					.withExpiresAt(dataExpiracao())
					.sign(algoritmo);

			return token;
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar o token jwt " + exception);
		}
		
	}
	
	public String getSubject(String tokenJwt) {
		DecodedJWT decodedJWT;
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer(ISSUE)
		        .build();
		        
		    decodedJWT = verifier.verify(tokenJwt);
		    
		    return decodedJWT.getSubject();
		} catch (JWTVerificationException exception){
		    throw new RuntimeException("Token JWT inválido ou expirado.");
		}
	}

	private Instant dataExpiracao() {
		// TODO Auto-generated method stub
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
	
}
