package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Convenio;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Paciente;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Usuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.PacienteRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.PacienteResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PacienteMapper {

    public Paciente toEntity(PacienteRequest req, Usuario usuario, Convenio convenio) {
        Paciente paciente = new Paciente();
        paciente.setUsuario(usuario);
        paciente.setConvenio(convenio);
        paciente.setDataNascimento(req.dataNascimento());
        paciente.setTelefone(req.telefone());
        return paciente;
    }

    public PacienteResponse toDTO(Paciente paciente) {
        return new PacienteResponse(
                paciente.getId(), paciente.getUsuario().getNome(), paciente.getConvenio().getNome(),
                paciente.getDataNascimento(), paciente.getTelefone()
        );
    }

}
