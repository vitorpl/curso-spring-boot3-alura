package med.voll.api.validacoes.consulta.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.exception.ValidacaoException;
import med.voll.api.dto.DadosAgendamentoConsulta;
import med.voll.api.repository.MedicoRepository;

@Component
public class ValidadorMedicoAtivo implements IValidadorAgendamentoConsulta {

	@Autowired
	private MedicoRepository medicoRepository;
	
	
	@Override
	public void validar(DadosAgendamentoConsulta dados) {
		if(dados.idMedico() == null)
			return;
		
		var medicoEstaAtivo = medicoRepository.findAtivoById(dados.idMedico());
		if(medicoEstaAtivo != null  && !medicoEstaAtivo) {
			throw new ValidacaoException("Consulta não pode ser agendada com médico inativo/excluído");
		}
		
	}

}
