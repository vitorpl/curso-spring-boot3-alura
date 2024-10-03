package med.voll.api.dto.paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import med.voll.api.dto.endereco.DadosEndereco;

public record DadosAtualizacaoPaciente(
		
		@NotNull		
		Long id,
		
		String nome,
		@Email
		String email,
		String telefone,
		DadosEndereco endereco
		
		) {

}
