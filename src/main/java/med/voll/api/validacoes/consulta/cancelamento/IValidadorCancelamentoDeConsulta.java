package med.voll.api.validacoes.consulta.cancelamento;

import med.voll.api.dto.consulta.DadosCancelamentoConsulta;

public interface IValidadorCancelamentoDeConsulta {

    public void validar(DadosCancelamentoConsulta dados);

}