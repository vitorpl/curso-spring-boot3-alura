package med.voll.api.validacoes.consulta.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.exception.ValidacaoException;
import med.voll.api.dto.DadosAgendamentoConsulta;
import med.voll.api.repository.ConsultaRepository;

@Component
public class ValidadorMedicoComOutraConsultaNoMesmoHorario implements IValidadorAgendamentoConsulta {

	@Autowired
	private ConsultaRepository consultaRepository;
	
	@Override
	public void validar(DadosAgendamentoConsulta dados) {

		var medicoPossuiOutraConsultaNoMesmoHorario = consultaRepository.existsByMedicoIdAndData(
				dados.idMedico(), dados.data());
		
		if(medicoPossuiOutraConsultaNoMesmoHorario) {
			throw new ValidacaoException("Médico já possui outra consulta agendada nesse mesmo horário");
		}
		
	}

}
