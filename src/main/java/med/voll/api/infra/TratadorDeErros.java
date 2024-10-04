package med.voll.api.infra;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
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
		
		
		//errors.stream().map(er -> new DadosErroValicacao(er.getField(), er.getDefaultMessage()));
		
		 List<ErroValicacao> list = errors.stream().map(ErroValicacao::new).toList();
		
		return ResponseEntity.badRequest().body(list);
		
	}
	
	private record ErroValicacao(String campo, String mensagem) {
		public ErroValicacao(FieldError error) {
			this(error.getField(), error.getDefaultMessage());
		}
	}
}
