package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Convenio;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Paciente;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Usuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.PacienteRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.PacienteResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.PacienteMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ConvenioRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.PacienteRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConvenioRepository convenioRepository;

    public PacienteService(PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository, ConvenioRepository convenioRepository) {
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.convenioRepository = convenioRepository;
    }

    // ADMIN FUNÇÕES

    public Page<PacienteResponse> listar(Pageable pageable) {
        return pacienteRepository.findAll(pageable).map(PacienteMapper::toDTO);
    }

    public PacienteResponse cadastrar(PacienteRequest pacienteRequest) {
        Usuario usuario = usuarioRepository.findById(pacienteRequest.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Convenio convenio = convenioRepository.findById(pacienteRequest.convenioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!usuario.getAtivo() || !convenio.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Paciente entity = PacienteMapper.toEntity(pacienteRequest, usuario, convenio);
        return PacienteMapper.toDTO(pacienteRepository.save(entity));
    }

    public PacienteResponse atualizarPorId(UUID id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Convenio convenio = convenioRepository.findById(request.convenioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!paciente.getConvenio().getAtivo() || !paciente.getUsuario().getAtivo() || !usuario.getAtivo() || !convenio.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Paciente entity = PacienteMapper.toEntity(request, usuario, convenio);
        return PacienteMapper.toDTO(pacienteRepository.save(entity));
    }

    public void deletarPorId(UUID id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!paciente.getUsuario().getAtivo() || !paciente.getConvenio().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        pacienteRepository.delete(paciente);
    }

    // USUARIO FUNÇÕES

    public PacienteResponse visualizarCadastro(Authentication auth) {
        Paciente paciente = pacienteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!paciente.getConvenio().getAtivo() || !paciente.getUsuario().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return PacienteMapper.toDTO(paciente);
    }

    public PacienteResponse cadastro(PacienteRequest pacienteRequest, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(pacienteRequest.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Convenio convenio = convenioRepository.findById(pacienteRequest.convenioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (usuario.getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (!usuario.getAtivo() || !convenio.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Paciente entity = PacienteMapper.toEntity(pacienteRequest, usuario, convenio);
        return PacienteMapper.toDTO(pacienteRepository.save(entity));
    }

    public PacienteResponse atualizar(UUID id, PacienteRequest request, Authentication auth) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Convenio convenio = convenioRepository.findById(request.convenioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!paciente.getUsuario().getEmail().equals(auth.getName()) || !usuario.getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!paciente.getUsuario().getAtivo() || !convenio.getAtivo() || !paciente.getConvenio().getAtivo() || !usuario.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        paciente.setTelefone(request.telefone());
        paciente.setDataNascimento(request.dataNascimento());
        paciente.setConvenio(convenio);
        paciente.setUsuario(usuario);

        return PacienteMapper.toDTO(pacienteRepository.save(paciente));
    }

    public void deletar(UUID id, Authentication auth) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!paciente.getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!paciente.getConvenio().getAtivo() || !paciente.getUsuario().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        pacienteRepository.delete(paciente);
    }

}
