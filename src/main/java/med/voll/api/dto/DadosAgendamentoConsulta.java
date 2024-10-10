package med.voll.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.voll.api.dto.medico.Especialidade;

/**
 * Em alguns casos os campos json enviados podem não estar no padrão de nomenclatura correto
 * Ex: id_medico, medico_id, medico
 * Nesses casos pode-se utilizar a anotação @JsonAlias(“id_medico”)
 * Ainda é possível aceitar outros nomes Ex:
 * @JsonAlias({“medico_id”, “id_medico”, “medico”, “idMedico”}) 
 */
public record DadosAgendamentoConsulta(
		
		@JsonAlias({"medico_id", "id_medico", "medico", "idMedico"})
		Long idMedico,

        @NotNull
        Long idPaciente,

        @NotNull
        @Future
        //@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime data,

        Especialidade especialidade) {
}
