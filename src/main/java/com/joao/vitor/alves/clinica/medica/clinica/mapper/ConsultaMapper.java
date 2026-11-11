package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Consulta;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Medico;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Paciente;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ConsultaRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ConsultaResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConsultaMapper {

    public Consulta toEntity(ConsultaRequest req, Medico medico, Paciente paciente) {
        Consulta consulta = new Consulta();
        consulta.setMedico(medico);
        consulta.setDataHora(req.dataHora());
        consulta.setPaciente(paciente);
        consulta.setStatus(req.status());
        consulta.setValor(req.valor());
        return consulta;
    }

    public ConsultaResponse toDTO(Consulta consulta) {
        return new ConsultaResponse(
                consulta.getId(), consulta.getPaciente().getUsuario().getNome(), consulta.getMedico().getUsuario().getNome(),
                consulta.getDataHora(), consulta.getStatus(), consulta.getValor()
        );
    }

}
