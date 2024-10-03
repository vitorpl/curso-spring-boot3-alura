package med.voll.api.model;


import org.springframework.util.StringUtils;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.dto.medico.DadosAtualizacaoMedico;
import med.voll.api.dto.medico.DadosCadastroMedico;
import med.voll.api.dto.medico.Especialidade;

@Table(name = "MEDICOS")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String telefone;
	private String crm;
	private Boolean ativo;
	
	@Enumerated(EnumType.STRING)
	Especialidade especialidade;
	
	@Embedded
	Endereco endereco;

	public Medico(DadosCadastroMedico dados) {
		this.ativo = true;
		this.nome = dados.nome();
		this.email = dados.email();
		this.telefone = dados.telefone();
		this.crm = dados.crm();
		this.endereco = new Endereco(dados.endereco());
		this.especialidade = dados.especialidade();		
	}

	public void atualizarDados(DadosAtualizacaoMedico dados) {
		
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
