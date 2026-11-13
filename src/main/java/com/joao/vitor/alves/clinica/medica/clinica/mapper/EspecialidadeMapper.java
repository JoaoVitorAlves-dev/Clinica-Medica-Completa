package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Especialidade;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.EspecialidadeRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.EspecialidadeResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EspecialidadeMapper {

    public Especialidade toEntity(EspecialidadeRequest req) {
        Especialidade especialidade = new Especialidade();
        especialidade.setNome(req.nome());
        return especialidade;
    }

    public EspecialidadeResponse toDTO(Especialidade especialidade) {
        return new EspecialidadeResponse(
                especialidade.getId(), especialidade.getNome(), especialidade.getAtiva()
        );
    }

}
