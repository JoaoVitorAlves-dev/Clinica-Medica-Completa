package com.joao.vitor.alves.clinica.medica.clinica.mapper;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Endereco;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Usuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.EnderecoRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.EnderecoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnderecoMapper {

    public Endereco toEntity(EnderecoRequest req, Usuario usuario) {
        Endereco endereco = new Endereco();
        endereco.setEstado(req.estado());
        endereco.setCep(req.cep());
        endereco.setCidade(req.cidade());
        endereco.setRua(req.rua());
        endereco.setUsuario(usuario);
        return endereco;
    }

    public EnderecoResponse toDTO(Endereco endereco) {
        return new EnderecoResponse(
                endereco.getId(), endereco.getUsuario().getNome(), endereco.getRua(), endereco.getCidade(),
                endereco.getEstado(), endereco.getCep()
        );
    }

}
