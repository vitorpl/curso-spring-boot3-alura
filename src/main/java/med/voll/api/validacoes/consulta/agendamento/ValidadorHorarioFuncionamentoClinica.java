package med.voll.api.validacoes.consulta.agendamento;

import java.time.DayOfWeek;

import org.springframework.stereotype.Component;

import med.voll.api.domain.exception.ValidacaoException;
import med.voll.api.dto.DadosAgendamentoConsulta;

@Component
public class ValidadorHorarioFuncionamentoClinica implements IValidadorAgendamentoConsulta {
	
	//poderia ter uma tabela no banco de dados para armazenar esses dados
	private static final int HORA_ABERTURA_CLINICA = 7;
	private static final int HORA_FECHAMENTO_CLINICA = 19;

	public void validar(DadosAgendamentoConsulta dados) {
		var dataConsulta = dados.data();

		var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
		var antesDaAberturaDaClinica = dataConsulta.getHour() < HORA_ABERTURA_CLINICA;
		var depoisDoEncerramentoDaClinica = dataConsulta.getHour() > HORA_FECHAMENTO_CLINICA;

		if (domingo || antesDaAberturaDaClinica || depoisDoEncerramentoDaClinica) {
			throw new ValidacaoException("Consulta fora do horário de funcionamento da clínica");
		}
	}
}
