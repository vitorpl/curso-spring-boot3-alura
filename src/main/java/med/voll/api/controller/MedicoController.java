package med.voll.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import med.voll.api.dto.medico.DadosAtualizacaoMedico;
import med.voll.api.dto.medico.DadosCadastroMedico;
import med.voll.api.dto.medico.DadosListagemMedico;
import med.voll.api.model.Medico;
import med.voll.api.repository.MedicoRepository;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private MedicoRepository repository;
	
	@PostMapping
	@Transactional
	public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
		System.out.println(dados);
		
		Medico medico = new Medico(dados);
		
		repository.save(medico);
	}
	
	/**
	 * Os parâmetros de paginação podem ainda ser customizados no application.properties
	 * anoção opcional @PageableDefault pode atribuir parâmetros iniciais da paginação
	 */
	@GetMapping
//	public Page<DadosListagemMedico> listar(Pageable paginacao) {
	public Page<DadosListagemMedico> listar(@PageableDefault(size = 1, sort = "nome") Pageable paginacao) {
		//return repository.findAll(paginacao).stream().map(DadosListagemMedico::new).toList();
		//return repository.findAll(paginacao).map(DadosListagemMedico::new);
		
		return repository.findByAtivoTrue(paginacao).map(DadosListagemMedico::new);
	}
	
	@PutMapping
	@Transactional
	public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
		var medico = repository.findById(dados.id()).orElseThrow(() -> new EntityNotFoundException("Médico não encontrado com o ID: " + dados.id()));
		//var medico = repository.getReferenceById(dados.id());
		
		medico.atualizarDados(dados);
		//repository.sa
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public void excluir(@PathVariable Long id) {
		System.out.println("Remover o médico de id " + id);

		//var medico = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Médico não encontrado com o ID: " + id));
		var medico = repository.getReferenceById(id);
		medico.excluir();
		//repository.save(medico);
		
		//repository.deleteById(id);
	}
}
