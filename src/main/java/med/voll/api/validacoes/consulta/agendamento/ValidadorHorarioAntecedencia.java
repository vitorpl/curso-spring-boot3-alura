package med.voll.api.validacoes.consulta.agendamento;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import med.voll.api.domain.exception.ValidacaoException;
import med.voll.api.dto.DadosAgendamentoConsulta;

@Component
public class ValidadorHorarioAntecedencia implements IValidadorAgendamentoConsulta {

	public void validar(DadosAgendamentoConsulta dados) {
		var dataConsulta = dados.data();
		var agora = LocalDateTime.now();
		var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();
		
		if(diferencaEmMinutos < 30) {
			throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos");
		}
		
	}
}
