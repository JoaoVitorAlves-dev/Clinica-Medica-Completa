package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Prontuario;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Receita;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ReceitaRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ReceitaResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReceitaMapper {

    public Receita toEntity(ReceitaRequest req, Prontuario prontuario) {
        Receita receita = new Receita();
        receita.setMedicamento(req.medicamento());
        receita.setPosologia(req.posologia());
        receita.setProntuario(prontuario);
        return receita;
    }

    public ReceitaResponse toDTO(Receita receita) {
        return new ReceitaResponse(
                receita.getId(), receita.getProntuario().getId(), receita.getMedicamento(), receita.getPosologia()
        );
    }

}
