package med.voll.api.validacoes.consulta.cancelamento;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.domain.exception.ValidacaoException;
import med.voll.api.dto.consulta.DadosCancelamentoConsulta;
import med.voll.api.repository.ConsultaRepository;

@Component("ValidadorHorarioAntecedenciaCancelamento")
public class ValidadorHorarioAntecedencia implements IValidadorCancelamentoDeConsulta {

    @Autowired
    private ConsultaRepository repository;

    @Override
    public void validar(DadosCancelamentoConsulta dados) {
        var consulta = repository.getReferenceById(dados.idConsulta());
        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();

        if (diferencaEmHoras < 24) {
            throw new ValidacaoException("Consulta somente pode ser cancelada com antecedência mínima de 24h!");
        }
    }
}