package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Exame;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Prontuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ExameRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ExameResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.ExameMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ExameRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ProntuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExameService {

    private final ExameRepository exameRepository;
    private final ProntuarioRepository prontuarioRepository;

    public ExameService(ExameRepository exameRepository, ProntuarioRepository prontuarioRepository) {
        this.exameRepository = exameRepository;
        this.prontuarioRepository = prontuarioRepository;
    }

    public Page<ExameResponse> listar(Pageable pageable) {
        return exameRepository.findAll(pageable).map(ExameMapper::toDTO);
    }

    public ExameResponse criar(ExameRequest req) {
        Prontuario prontuario = prontuarioRepository.findById(req.prontuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!prontuario.getConsulta().getMedico().getUsuario().getAtivo() || !prontuario.getConsulta().getPaciente().getUsuario().getAtivo() ||
                !prontuario.getConsulta().getPaciente().getConvenio().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Exame entity = ExameMapper.toEntity(req, prontuario);
        return ExameMapper.toDTO(exameRepository.save(entity));
    }

    public ExameResponse atualizar(Long id, ExameRequest req) {
        Exame exame = exameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Prontuario prontuario = prontuarioRepository.findById(req.prontuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!exame.getProntuario().getConsulta().getPaciente().getConvenio().getAtivo() || !exame.getProntuario().getConsulta().getPaciente().getUsuario().getAtivo() ||
        !exame.getProntuario().getConsulta().getMedico().getUsuario().getAtivo() || !exame.getProntuario().getConsulta().getMedico().getEspecialidade().getAtiva()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (!prontuario.getConsulta().getMedico().getEspecialidade().getAtiva() || !prontuario.getConsulta().getMedico().getUsuario().getAtivo()
         || !prontuario.getConsulta().getPaciente().getUsuario().getAtivo() || !prontuario.getConsulta().getPaciente().getConvenio().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        exame.setTipo(req.tipo());
        exame.setResultado(req.resultado());
        exame.setProntuario(prontuario);

        return ExameMapper.toDTO(exameRepository.save(exame));
    }

    public void deletar(Long id) {
        Exame exame = exameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        exameRepository.delete(exame);
    }

}
