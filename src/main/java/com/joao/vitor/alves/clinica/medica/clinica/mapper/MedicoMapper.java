package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Especialidade;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Medico;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Usuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.MedicoRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.MedicoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MedicoMapper {

    public Medico toEntity(MedicoRequest req, Usuario usuario, Especialidade especialidade) {
        Medico medico = new Medico();
        medico.setUsuario(usuario);
        medico.setCrm(req.crm());
        medico.setEspecialidade(especialidade);
        return medico;
    }

    public MedicoResponse toDTO(Medico medico) {
        return new MedicoResponse(
                medico.getId(), medico.getUsuario().getNome() ,medico.getCrm(), medico.getEspecialidade().getNome()
        );
    }

}
