package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Especialidade;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Medico;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Usuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.MedicoRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.MedicoResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.MedicoMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.EspecialidadeRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.MedicoRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;

    public MedicoService(MedicoRepository medicoRepository, UsuarioRepository usuarioRepository, EspecialidadeRepository especialidadeRepository) {
        this.medicoRepository = medicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.especialidadeRepository = especialidadeRepository;
    }

    public Page<MedicoResponse> listar(Pageable pageable) {
        return medicoRepository.findAll(pageable).map(MedicoMapper::toDTO);
    }

    public MedicoResponse criar(MedicoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Especialidade especialidade = especialidadeRepository.findById(request.especialidadeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!usuario.getAtivo() || !especialidade.getAtiva()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Medico entity = MedicoMapper.toEntity(request, usuario, especialidade);
        return MedicoMapper.toDTO(medicoRepository.save(entity));
    }

    public MedicoResponse atualizarPorId(Long id, MedicoRequest request, Authentication auth) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Especialidade especialidade = especialidadeRepository.findById(request.especialidadeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!medico.getUsuario().getEmail().equals(auth.getName()) || !usuario.getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!medico.getUsuario().getAtivo() || !usuario.getAtivo() || !especialidade.getAtiva()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        medico.setEspecialidade(especialidade);
        medico.setCrm(request.crm());
        medico.setUsuario(usuario);

        return MedicoMapper.toDTO(medicoRepository.save(medico));
    }

    public void deletar(Long id) {
        medicoRepository.deleteById(id);
    }

}
