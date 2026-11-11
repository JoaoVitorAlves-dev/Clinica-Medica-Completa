package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Convenio;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ConvenioRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ConvenioResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.ConvenioMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ConvenioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConvenioService {

    private final ConvenioRepository convenioRepository;

    public ConvenioService(ConvenioRepository convenioRepository) {
        this.convenioRepository = convenioRepository;
    }

    public Page<ConvenioResponse> listar(Pageable pageable) {
        return convenioRepository.findAll(pageable).map(ConvenioMapper::toDTO);
    }

    public ConvenioResponse criar(ConvenioRequest convenioRequest) {
        Convenio entity = ConvenioMapper.toEntity(convenioRequest);
        return ConvenioMapper.toDTO(convenioRepository.save(entity));
    }

    public ConvenioResponse atualizar(Long id, ConvenioRequest request) {
        Convenio convenio = convenioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!convenio.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        convenio.setCnpj(request.cnpj());
        convenio.setNome(request.nome());

        return ConvenioMapper.toDTO(convenioRepository.save(convenio));
    }

    public void desativar(Long id) {
        Convenio convenio = convenioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!convenio.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        convenio.setAtivo(false);
        convenioRepository.save(convenio);
    }

}
