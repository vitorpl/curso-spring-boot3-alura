package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.dto.paciente.DadosAtualizacaoPaciente;
import med.voll.api.dto.paciente.DadosCadastroPaciente;
import med.voll.api.dto.paciente.DadosListagemPaciente;
import med.voll.api.model.Paciente;
import med.voll.api.repository.PacienteRepository;

@RestController
@RequestMapping("pacientes")
public class PacienteController {
	
	@Autowired
	PacienteRepository repository;
	
	@PostMapping
	public void cadastrar(@RequestBody @Valid DadosCadastroPaciente dados) {
		System.out.println("Dados recebidos:\n" + dados);
		
		Paciente paciente = new Paciente(dados);
		
		repository.save(paciente);
	}
	
	@GetMapping
	public Page<DadosListagemPaciente> listar(Pageable paginacao) {
		//return repository.findAll(paginacao).map(DadosListagemPaciente::new);
		return repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
	}
	

	@PutMapping
	@Transactional
	public void atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
		var paciente = repository.findById(dados.id()).orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com o ID: " + dados.id()));
		if(!paciente.getAtivo()) {
			throw new EntityNotFoundException("Paciente não está disponível para edição");
		}
		
		paciente.atualizarDados(dados);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public void excluir(@PathVariable Long id) {
		System.out.println("Remover o paciente de id " + id);
		var paciente = repository.getReferenceById(id);
		paciente.excluir();
	}
}
