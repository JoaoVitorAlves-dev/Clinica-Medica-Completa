package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Exame;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Prontuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ExameRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ExameResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExameMapper {

    public Exame toEntity(ExameRequest req, Prontuario prontuario) {
        Exame exame = new Exame();
        exame.setProntuario(prontuario);
        exame.setResultado(req.resultado());
        exame.setTipo(req.tipo());
        return exame;
    }

    public ExameResponse toDTO(Exame exame) {
        return new ExameResponse(
                exame.getId(), exame.getProntuario().getId(), exame.getTipo(), exame.getResultado()
        );
    }
}
