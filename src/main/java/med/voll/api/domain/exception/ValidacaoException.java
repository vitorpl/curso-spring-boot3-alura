package med.voll.api.domain.exception;

public class ValidacaoException extends RuntimeException {

	private static final long serialVersionUID = -4427777865706344683L;

	public ValidacaoException(String mensagem) {
		super(mensagem);
	}
}
