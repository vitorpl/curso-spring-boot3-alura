package med.voll.api.validacoes.consulta.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.exception.ValidacaoException;
import med.voll.api.dto.DadosAgendamentoConsulta;
import med.voll.api.repository.PacienteRepository;

@Component
public class ValidadorPacienteAtivo implements IValidadorAgendamentoConsulta {

	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Override
	public void validar(DadosAgendamentoConsulta dados) {
		var pacienteEstaAtivo = pacienteRepository.findAtivoById(dados.idPaciente());
		
		if(!pacienteEstaAtivo) {
			throw new ValidacaoException("Consulta não pode ser agendada com paciente excluído");
		}
	}

}
