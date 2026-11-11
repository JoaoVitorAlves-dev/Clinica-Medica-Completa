package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Consulta;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Prontuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ProntuarioRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ProntuarioResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProntuarioMapper {

    public Prontuario toEntity(ProntuarioRequest req, Consulta consulta) {
        Prontuario prontuario = new Prontuario();
        prontuario.setQueixa(req.queixa());
        prontuario.setConsulta(consulta);
        prontuario.setDiagnostico(req.diagnostico());
        return prontuario;
    }

    public ProntuarioResponse toDTO(Prontuario prontuario) {
        return new ProntuarioResponse(
                prontuario.getId(), prontuario.getConsulta().getId(), prontuario.getQueixa(), prontuario.getDiagnostico()
        );
    }

}
