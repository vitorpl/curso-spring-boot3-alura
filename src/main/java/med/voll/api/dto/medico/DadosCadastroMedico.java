package med.voll.api.dto.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.dto.endereco.DadosEndereco;

public record DadosCadastroMedico(
		
		@NotBlank		
		String nome,
		
		@NotBlank
		@Email
		String email,
		
		@NotBlank
		String telefone,
		
		@NotBlank
		@Pattern(regexp = "\\d{4,6}") //de 4 a 6 dígitos
		String crm,
		
		@NotNull
		Especialidade especialidade,
		
		@NotNull
		@Valid DadosEndereco endereco
		) {

}
