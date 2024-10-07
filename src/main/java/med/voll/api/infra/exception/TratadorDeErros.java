package med.voll.api.infra.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorDeErros {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> tratarErro404() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> tratarErro400(MethodArgumentNotValidException ex) {
		List<FieldError> errors = ex.getFieldErrors();

		// errors.stream().map(er -> new DadosErroValicacao(er.getField(),
		// er.getDefaultMessage()));

		List<ErroValicacao> list = errors.stream().map(ErroValicacao::new).toList();

		return ResponseEntity.badRequest().body(list);

	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> tratarErro400(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> tratarErroBadCredentials() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> tratarErroAuthentication() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> tratarErroAcessoNegado() {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> tratarErro500(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + ex.getLocalizedMessage());
	}

	private record ErroValicacao(String campo, String mensagem) {
		public ErroValicacao(FieldError error) {
			this(error.getField(), error.getDefaultMessage());
		}
	}
}
