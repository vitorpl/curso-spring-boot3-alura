package med.voll.api.validacoes.consulta.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.exception.ValidacaoException;
import med.voll.api.dto.DadosAgendamentoConsulta;
import med.voll.api.repository.ConsultaRepository;

@Component
public class ValidadorPacienteSemOutraConsultaNoDia implements IValidadorAgendamentoConsulta {

	private static final int HORA_ABERTURA_CLINICA = 7;
	private static final int ULTIMA_HORA_AGENDAVEL = 18;
	
	@Autowired
	private ConsultaRepository consultaRepository;

	@Override
	public void validar(DadosAgendamentoConsulta dados) {

		var primeiroHorario = dados.data().withHour(HORA_ABERTURA_CLINICA);
		var ultimoHorario = dados.data().withHour(ULTIMA_HORA_AGENDAVEL);

		var pacientePossuiOutraConsultaNoDia = consultaRepository.existsByPacienteIdAndDataBetween(dados.idPaciente(),
				primeiroHorario, ultimoHorario);
		
		if(pacientePossuiOutraConsultaNoDia) {
			throw new ValidacaoException("Paciente já possui uma consulta agendada nesse dia");
		}
	}

}
