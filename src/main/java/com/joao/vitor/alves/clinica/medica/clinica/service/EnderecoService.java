package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Endereco;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Usuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.EnderecoRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.EnderecoResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.EnderecoMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.EnderecoRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, UsuarioRepository usuarioRepository) {
        this.enderecoRepository = enderecoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Page<EnderecoResponse> listar(Pageable pageable, Authentication auth) {
        return enderecoRepository.findByUsuarioEmail(auth.getName(), pageable).map(EnderecoMapper::toDTO);
    }

    public EnderecoResponse criar(EnderecoRequest enderecoRequest, Authentication auth) {
        Usuario usuario = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!usuario.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (enderecoRequest.usuarioId() != null && !enderecoRequest.usuarioId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Endereco entity = EnderecoMapper.toEntity(enderecoRequest, usuario);
        return EnderecoMapper.toDTO(enderecoRepository.save(entity));
    }

    public EnderecoResponse atualizar(Long id, EnderecoRequest enderecoRequest, Authentication auth) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Usuario usuario = usuarioRepository.findById(enderecoRequest.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!endereco.getUsuario().getEmail().equals(auth.getName()) || !usuario.getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!endereco.getUsuario().getAtivo() || !usuario.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        endereco.setCidade(enderecoRequest.cidade());
        endereco.setRua(enderecoRequest.rua());
        endereco.setCep(enderecoRequest.cep());
        endereco.setEstado(enderecoRequest.estado());

        return EnderecoMapper.toDTO(enderecoRepository.save(endereco));
    }

    public void deletar(Long id, Authentication auth) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!endereco.getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        enderecoRepository.delete(endereco);
    }

}
