package med.voll.api.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.voll.api.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

	Boolean existsByPacienteIdAndDataBetween(Long idPaciente, LocalDateTime primeiroHorario,
			LocalDateTime ultimoHorario);

	Boolean existsByMedicoIdAndData(Long idMedico, @NotNull @Future LocalDateTime data);
	
	Boolean existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(Long idMedico, LocalDateTime data);

}
