package med.voll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import med.voll.api.dto.endereco.DadosEndereco;
import med.voll.api.dto.medico.DadosCadastroMedico;
import med.voll.api.dto.medico.DadosDetalhamentoMedico;
import med.voll.api.dto.medico.Especialidade;
import med.voll.api.model.Medico;
import med.voll.api.repository.MedicoRepository;

@SpringBootTest //carrega o contexto do spring para realização dos testes
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;
	
	@Autowired
	private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;
	
	@MockBean
	private MedicoRepository medicoRepository;

	@Test
	@DisplayName("Quando tentar cadastrar um médico sem passar os dados, deve retornar um Bad Request")
	@WithMockUser
	void whenCadastrarMedicoSemDadosThenDeveRetornarBadRequest() throws Exception {
		
		var response = mvc.perform(post("/medicos")).andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		
	}

	
	@Test
	@DisplayName("Quando tentar cadastrar um médico deve retornar 200 ok e os detalhes do médico salvo")
	@WithMockUser
	void whenCadastrarMedicoSemDadosThenDeveRetornarOk() throws Exception {
		
		var dadosEndereco = new DadosEndereco("Rua teste", "Boa Vista", "82560050", "Curitiba", "PR",  "casa", "100");
		var novoMedico = new DadosCadastroMedico("Joao", "teste@teste.com", "41339665445", "757575", Especialidade.ORTOPEDIA, dadosEndereco);
		var medico = new Medico(novoMedico);
		var novoMedicoRetorno = new DadosDetalhamentoMedico(medico);

		var jsonNovoMedico = dadosCadastroMedicoJson.write(novoMedico).getJson();
		var jsonRetorno = dadosDetalhamentoMedicoJson.write(novoMedicoRetorno).getJson();
		
		
		when(medicoRepository.save(medico)).thenReturn(medico);
		
		var response = mvc.perform(
				post("/medicos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonNovoMedico)
				).andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.getContentAsString()).isEqualTo(jsonRetorno);
	}

	
}
