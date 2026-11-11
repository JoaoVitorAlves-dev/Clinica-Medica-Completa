package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Convenio;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ConvenioRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ConvenioResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConvenioMapper {

    public Convenio toEntity(ConvenioRequest req) {
        Convenio convenio = new Convenio();
        convenio.setNome(req.nome());
        convenio.setCnpj(req.cnpj());
        convenio.setAtivo(req.ativo());
        return convenio;
    }

    public ConvenioResponse toDTO(Convenio convenio) {
        return new ConvenioResponse(
                convenio.getId(), convenio.getNome(), convenio.getCnpj(), convenio.getAtivo()
        );
    }

}
