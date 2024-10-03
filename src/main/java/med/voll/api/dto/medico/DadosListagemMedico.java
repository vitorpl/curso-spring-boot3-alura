package med.voll.api.dto.medico;

import med.voll.api.model.Medico;

public record DadosListagemMedico(Long id, String nome, String email, String crm, Especialidade especialidade, Boolean ativo) {

	public DadosListagemMedico(Medico medico) {
		this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade(), medico.getAtivo());
	}
}
