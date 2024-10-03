package med.voll.api.model;

import org.springframework.util.StringUtils;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.dto.paciente.DadosAtualizacaoPaciente;
import med.voll.api.dto.paciente.DadosCadastroPaciente;

@Table(name = "PACIENTES")
@Entity(name = "Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
	
	public Paciente(DadosCadastroPaciente dados) {
		this.ativo = true;
		this.nome = dados.nome();
		this.email = dados.email();
		this.telefone = dados.telefone();
		this.cpf = dados.cpf();
		this.endereco = new Endereco(dados.endereco());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String email;
	private String telefone;
	private String cpf;
	Boolean ativo;
	
	@Embedded
	private Endereco endereco;

	public void atualizarDados(@Valid DadosAtualizacaoPaciente dados) {
		if(StringUtils.hasLength(dados.nome())) {
			this.nome = dados.nome();
		}
		
		if(StringUtils.hasLength(dados.telefone())) {
			this.telefone = dados.telefone();
		}
		
		if(StringUtils.hasLength(dados.email())) {
			this.email = dados.email();
		}
		
		if(dados.endereco() != null) {
			this.getEndereco().atualizarDados(dados.endereco());
		}		
	}
	
	public void excluir() {
		this.ativo = false;
	}
}
