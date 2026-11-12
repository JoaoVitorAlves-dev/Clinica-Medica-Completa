package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Consulta;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Prontuario;
import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.StatusConsulta;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ProntuarioRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ProntuarioResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.ProntuarioMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ConsultaRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ProntuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final ConsultaRepository consultaRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository, ConsultaRepository consultaRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.consultaRepository = consultaRepository;
    }

    public Page<ProntuarioResponse> listar(Pageable pageable) {
        return prontuarioRepository.findAll(pageable).map(ProntuarioMapper::toDTO);
    }

    public ProntuarioResponse criar(ProntuarioRequest req) {
        Consulta consulta = consultaRepository.findById(req.consultaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (consulta.getStatus().equals(StatusConsulta.CANCELADA)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }
        if (!consulta.getPaciente().getConvenio().getAtivo() || !consulta.getPaciente().getUsuario().getAtivo()
                || !consulta.getMedico().getUsuario().getAtivo() || consulta.getMedico().getEspecialidade().getAtiva()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Prontuario entity = ProntuarioMapper.toEntity(req, consulta);
        return ProntuarioMapper.toDTO(prontuarioRepository.save(entity));
    }

    public ProntuarioResponse atualizar(Long id, ProntuarioRequest req) {
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Consulta consulta = consultaRepository.findById(req.consultaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!prontuario.getConsulta().getMedico().getUsuario().getAtivo() || !prontuario.getConsulta().getMedico().getEspecialidade().getAtiva()
                || !prontuario.getConsulta().getPaciente().getUsuario().getAtivo() || !prontuario.getConsulta().getPaciente().getConvenio().getAtivo()
                || !consulta.getMedico().getEspecialidade().getAtiva() || !consulta.getMedico().getUsuario().getAtivo() || !consulta.getPaciente().getConvenio().getAtivo()
                || !consulta.getPaciente().getUsuario().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        prontuario.setQueixa(req.queixa());
        prontuario.setConsulta(consulta);
        prontuario.setDiagnostico(req.diagnostico());

        return ProntuarioMapper.toDTO(prontuarioRepository.save(prontuario));
    }

    public void deletar(Long id) {
        prontuarioRepository.deleteById(id);
    }

}
