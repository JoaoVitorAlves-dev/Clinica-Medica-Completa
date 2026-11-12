package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Prontuario;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Receita;
import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.StatusConsulta;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ReceitaRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ReceitaResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.ReceitaMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ProntuarioRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ReceitaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ProntuarioRepository prontuarioRepository;

    public ReceitaService(ReceitaRepository receitaRepository, ProntuarioRepository prontuarioRepository) {
        this.receitaRepository = receitaRepository;
        this.prontuarioRepository = prontuarioRepository;
    }

    public Page<ReceitaResponse> listar(Pageable pageable) {
        return receitaRepository.findAll(pageable).map(ReceitaMapper::toDTO);
    }

    public ReceitaResponse criar(ReceitaRequest req) {
        Prontuario prontuario = prontuarioRepository.findById(req.prontuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!prontuario.getConsulta().getPaciente().getUsuario().getAtivo() || !prontuario.getConsulta().getPaciente().getConvenio().getAtivo()
                || !prontuario.getConsulta().getMedico().getUsuario().getAtivo() || !prontuario.getConsulta().getMedico().getEspecialidade().getAtiva() || prontuario.getConsulta().getStatus().equals(StatusConsulta.CANCELADA)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Receita entity = ReceitaMapper.toEntity(req, prontuario);
        return ReceitaMapper.toDTO(receitaRepository.save(entity));
    }

    public ReceitaResponse atualizar(Long id, ReceitaRequest req) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (receita.getProntuario().getConsulta().getStatus().equals(StatusConsulta.CANCELADA) || !receita.getProntuario().getConsulta().getMedico().getEspecialidade().getAtiva()
                || !receita.getProntuario().getConsulta().getMedico().getUsuario().getAtivo() || !receita.getProntuario().getConsulta().getPaciente().getUsuario().getAtivo()
                || !receita.getProntuario().getConsulta().getPaciente().getConvenio().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Prontuario prontuario = prontuarioRepository.findById(req.prontuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (prontuario.getConsulta().getStatus().equals(StatusConsulta.CANCELADA) || !prontuario.getConsulta().getPaciente().getConvenio().getAtivo()
                || !prontuario.getConsulta().getPaciente().getUsuario().getAtivo() || !prontuario.getConsulta().getMedico().getEspecialidade().getAtiva() || !prontuario.getConsulta().getMedico().getUsuario().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        receita.setProntuario(prontuario);
        receita.setPosologia(req.posologia());
        receita.setMedicamento(req.medicamento());

        return ReceitaMapper.toDTO(receitaRepository.save(receita));
    }

    public void deletar(Long id) {
        receitaRepository.deleteById(id);
    }
}
