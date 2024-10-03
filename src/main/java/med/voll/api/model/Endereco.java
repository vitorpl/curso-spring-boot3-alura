package med.voll.api.model;

import org.springframework.util.StringUtils;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.dto.endereco.DadosEndereco;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
	private String logradouro; 
	private String bairro; 
	private String cep; 
	private String cidade; 
	private String uf; 
	private String complemento;
	private String numero;
	
	public Endereco(DadosEndereco endereco) {

		this.logradouro = endereco.logradouro(); 
		this.bairro = endereco.bairro() ; 
		this.cep = endereco.cep(); 
		this.cidade = endereco.cidade(); 
		this.uf = endereco.uf(); 
		this.complemento = endereco.complemento();
		this.numero = endereco.numero();

	}
	
	public void atualizarDados(DadosEndereco dados) {
		if(StringUtils.hasLength(dados.logradouro())) {
			this.logradouro = dados.logradouro();
		}
		if(StringUtils.hasLength(dados.bairro())) {
			this.bairro = dados.bairro();
		}
		if(StringUtils.hasLength(dados.cep())) {
			this.cep = dados.cep();
		}
		if(StringUtils.hasLength(dados.cidade())) {
			this.cidade = dados.cidade();
		}
		if(StringUtils.hasLength(dados.uf())) {
			this.uf = dados.uf();
		}
		if(StringUtils.hasLength(dados.complemento())) {
			this.complemento = dados.complemento();
		}
		if(StringUtils.hasLength(dados.numero())) {
			this.numero = dados.numero();
		}
	}
	
}
