package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Consulta;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Pagamento;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.PagamentoRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.PagamentoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PagamentoMapper {

    public Pagamento toEntity(PagamentoRequest req, Consulta consulta) {
        Pagamento pagamento = new Pagamento();
        pagamento.setConsulta(consulta);
        pagamento.setMetodo(req.metodo());
        return pagamento;
    }

    public PagamentoResponse toDTO(Pagamento pagamento) {
        return new PagamentoResponse(
                pagamento.getId(), pagamento.getConsulta().getId(), pagamento.getMetodo(), pagamento.getStatus()
        );
    }

}
