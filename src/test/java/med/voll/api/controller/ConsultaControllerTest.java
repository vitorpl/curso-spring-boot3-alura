package med.voll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

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

import med.voll.api.dto.DadosAgendamentoConsulta;
import med.voll.api.dto.medico.DadosDetalhamentoConsulta;
import med.voll.api.dto.medico.Especialidade;
import med.voll.api.service.AgendaService;

@SpringBootTest //carrega o contexto do spring para realização dos testes
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;
	
	@Autowired
	private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;
	
	@MockBean
	private AgendaService agendaService;
	
	@Test
	@DisplayName("Deve retornar código 400 - Bad Request, quando enviar informações inválidas")
	@WithMockUser //para evitar erro 403 do security
	void whenInformacoesInvalidasThenRetornarBadRequest() throws Exception {

		var response = mvc.perform(post("/consultas")).andReturn().getResponse();

		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		
		
	}
	
	@Test
	@DisplayName("Deve retornar código 200, quando enviar informações válidas")
	@WithMockUser 
	void whenInformacoesValidasThenRetornarSuccess() throws Exception {
		var data = LocalDateTime.now().plusHours(1);
		var especialidade = Especialidade.CARDIOLOGIA;
		
		var dadosDetalhamento =  new DadosDetalhamentoConsulta(null, 2l, 5l, data, null);
		when(agendaService.agendar(any())).thenReturn(dadosDetalhamento);
		
		var response = mvc.perform(
					post("/consultas")
					.contentType(MediaType.APPLICATION_JSON)
						.content(dadosAgendamentoConsultaJson
									.write(new DadosAgendamentoConsulta(2l, 5l, data, especialidade))
									.getJson())
				)
				.andReturn().getResponse();

		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
		var jsonEsperado = dadosDetalhamentoConsultaJson.write(dadosDetalhamento).getJson();
		
		assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
		
		
	}
	

}
