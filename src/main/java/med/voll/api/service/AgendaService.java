package med.voll.api.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.exception.ValidacaoException;
import med.voll.api.dto.DadosAgendamentoConsulta;
import med.voll.api.dto.consulta.DadosCancelamentoConsulta;
import med.voll.api.dto.consulta.MotivoCancelamentoConsulta;
import med.voll.api.dto.medico.DadosDetalhamentoConsulta;
import med.voll.api.model.Consulta;
import med.voll.api.model.Medico;
import med.voll.api.repository.ConsultaRepository;
import med.voll.api.repository.MedicoRepository;
import med.voll.api.repository.PacienteRepository;
import med.voll.api.validacoes.consulta.agendamento.IValidadorAgendamentoConsulta;
import med.voll.api.validacoes.consulta.cancelamento.IValidadorCancelamentoDeConsulta;

@Service
public class AgendaService {

	@Autowired
	private ConsultaRepository consultaRepository;
	
	@Autowired 
	private MedicoRepository medicoRepository;
	
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	private List<IValidadorAgendamentoConsulta> validadores;
	
	@Autowired
	private List<IValidadorCancelamentoDeConsulta> validadoresCancelamento;
		
	@Transactional
	public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
		
		if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
			throw new ValidacaoException("Id do médico informado não existe.");
		}

		if(dados.idPaciente() != null && !pacienteRepository.existsById(dados.idPaciente())) {
			throw new ValidacaoException("Id do paciente informado não existe.");
		}
		
		if(dados.data().isBefore(LocalDateTime.now())) {
			throw new ValidacaoException("A data do agendamento deve ser posterior à data atual.");
		}
		
		executarValidacoesDeNegocio(dados);
		
		var medico = atribuirOuEscolherMedicoAleatorio(dados); 
		if(medico == null) {
			throw new ValidacaoException("Não existe médico disponível nessa data");
		}
		
		
		var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
		var consulta = new Consulta(null, medico, paciente, dados.data(), null);
		
		
		consultaRepository.save(consulta);
		
		return new DadosDetalhamentoConsulta(consulta);
		
	}

	private void executarValidacoesDeNegocio(DadosAgendamentoConsulta dados) {

        /*List<IValidador<DadosAgendamentoConsulta>> validadores = new ArrayList<>();
        
        validadores.add(new ValidadorHorarioAntecedencia());
        validadores.add(new ValidadorHorarioFuncionamentoClinica());
        validadores.add(new ValidadorMedicoAtivo());
        validadores.add(new ValidadorMedicoComOutraConsultaNoMesmoHorario());
        validadores.add(new ValidadorPacienteAtivo());
        validadores.add(new ValidadorPacienteSemOutraConsultaNoDia());
        for (IValidador<DadosAgendamentoConsulta> validador : validadores) {
            validador.validar(dados);
        }
        */
		
		/*
		 * O Spring permite injetar todas as classes que implementam uma dada interface.
		 */
        validadores.forEach(v -> v.validar(dados));
	}

	private Medico atribuirOuEscolherMedicoAleatorio(DadosAgendamentoConsulta dados) {

		if(dados.idMedico() != null) {
			return medicoRepository.getReferenceById(dados.idMedico());
		}
		
		if(dados.especialidade() == null) {
			throw new ValidacaoException("Especialidade obrigatória quando o médico não for escolhido!");
		}
		
		return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
		
	}

	@Transactional
	public void cancelar(@Valid DadosCancelamentoConsulta dados) {
		
		if(!consultaRepository.existsById(dados.idConsulta())) {
			throw new ValidacaoException("Consulta não encontrada para o id: "+dados.idConsulta());
		}
		
		var consulta = consultaRepository.getReferenceById(dados.idConsulta());
		
//		LocalDateTime dataAgendadaMenos24h = consulta.getData().minus(Duration.ofHours(24));
//		boolean cancelandoMenosDe24HorasAntes = LocalDateTime.now().isAfter(dataAgendadaMenos24h);
//		
//		if(ehCancelamentoDoPaciente(dados) && cancelandoMenosDe24HorasAntes) {
//			throw new ValidacaoException("Paciente somente poderá cancelar consulta com 24 horas de antecedência");
//		}
		
		validadoresCancelamento.forEach(v -> v.validar(dados));
		
		consulta.setMotivoCancelamento(dados.motivo());		
		
		//consultaRepository.save(consulta);
	}

//	private boolean ehCancelamentoDoPaciente(@Valid DadosCancelamentoConsulta dados) {
//		return dados.motivo().equals(MotivoCancelamentoConsulta.DESISTENCIA) || 
//				dados.motivo().equals(MotivoCancelamentoConsulta.OUTROS);
//	}
}
