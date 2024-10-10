package med.voll.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.dto.DadosAgendamentoConsulta;
import med.voll.api.dto.consulta.DadosCancelamentoConsulta;
import med.voll.api.dto.medico.DadosDetalhamentoConsulta;
import med.voll.api.repository.ConsultaRepository;
import med.voll.api.service.AgendaService;

@RestController
@RequestMapping("/consultas")
@SecurityRequirement(name = "bearer-key") //faz com que o swagger-ui permita adicionar o header para o token de autenticação
public class ConsultaController {
	
	@Autowired
	private AgendaService agendaService;
	
	@Autowired
	private ConsultaRepository consultaRepository;

	@GetMapping
	public ResponseEntity<List<DadosDetalhamentoConsulta>> listarConsultas() {
		
		List<DadosDetalhamentoConsulta> consultas = consultaRepository.findAll().stream().map(DadosDetalhamentoConsulta::new).toList();
		
		return ResponseEntity.ok(consultas);
	}
	
    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoConsulta> agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        System.out.println(dados);
        
        DadosDetalhamentoConsulta dadosAgendamento = agendaService.agendar(dados);
        
        return ResponseEntity.ok(dadosAgendamento);
    }
    
    @PostMapping("/cancelar")
    @Transactional
    public ResponseEntity<?> cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados) {
    	
    	agendaService.cancelar(dados);
    	
    	return ResponseEntity.noContent().build();
    	
    }
    

}