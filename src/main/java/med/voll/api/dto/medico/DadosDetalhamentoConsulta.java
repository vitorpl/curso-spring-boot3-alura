package med.voll.api.dto.medico;

import java.time.LocalDateTime;

import med.voll.api.dto.consulta.MotivoCancelamentoConsulta;
import med.voll.api.model.Consulta;

public record DadosDetalhamentoConsulta(
		Long id,
		Long idMedico,
		Long idPaciente,
		LocalDateTime dataConsulta,
		MotivoCancelamentoConsulta motivoCancelamento
		) {
	
	public DadosDetalhamentoConsulta(Consulta consulta) {
		this(consulta.getId(), 
				consulta.getMedico().getId(), 
				consulta.getPaciente().getId(),
				consulta.getData(),
				consulta.getMotivoCancelamento());
	}

}
