package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Especialidade;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.EspecialidadeRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.EspecialidadeResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.EspecialidadeMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.EspecialidadeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository) {
        this.especialidadeRepository = especialidadeRepository;
    }

    public Page<EspecialidadeResponse> listar(Pageable pageable) {
        return especialidadeRepository.findAll(pageable).map(EspecialidadeMapper::toDTO);
    }

    public EspecialidadeResponse criar(EspecialidadeRequest request) {
        Especialidade entity = EspecialidadeMapper.toEntity(request);
        return EspecialidadeMapper.toDTO(especialidadeRepository.save(entity));
    }

    public EspecialidadeResponse atualizar(Long id, EspecialidadeRequest request) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        especialidade.setNome(request.nome());
        return EspecialidadeMapper.toDTO(especialidadeRepository.save(especialidade));
    }

    public void desativar(Long id) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!especialidade.getAtiva()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        especialidade.setAtiva(false);
        especialidadeRepository.save(especialidade);
    }

}
