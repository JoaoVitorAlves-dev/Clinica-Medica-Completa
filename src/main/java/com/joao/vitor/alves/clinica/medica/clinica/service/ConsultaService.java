package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Consulta;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Medico;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Paciente;
import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.StatusConsulta;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ConsultaRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ConsultaResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.ConsultaMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ConsultaRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.MedicoRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    public Page<ConsultaResponse> listar(Pageable pageable) {
        return consultaRepository.findAll(pageable).map(ConsultaMapper::toDTO);
    }

    public ConsultaResponse adicionar(ConsultaRequest req) {
        Paciente paciente = pacienteRepository.findById(req.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Medico medico = medicoRepository.findById(req.medicoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!paciente.getUsuario().getAtivo() || !paciente.getConvenio().getAtivo() || !medico.getUsuario().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (consultaRepository.existsByDataHora(req.dataHora()) || req.status().equals(StatusConsulta.CANCELADA)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Consulta entity = ConsultaMapper.toEntity(req, medico, paciente);
        return ConsultaMapper.toDTO(consultaRepository.save(entity));
    }

    public ConsultaResponse atualizar(Long id, ConsultaRequest req) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Medico medico = medicoRepository.findById(req.medicoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Paciente paciente = pacienteRepository.findById(req.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!consulta.getMedico().getUsuario().getAtivo() || !consulta.getPaciente().getUsuario().getAtivo()
                || !medico.getUsuario().getAtivo() || !paciente.getConvenio().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (consultaRepository.existsByDataHora(req.dataHora())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        consulta.setPaciente(paciente);
        consulta.setValor(req.valor());
        consulta.setStatus(req.status());
        consulta.setDataHora(req.dataHora());
        consulta.setMedico(medico);

        return ConsultaMapper.toDTO(consultaRepository.save(consulta));
    }

    public void cancelar(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (consulta.getStatus().equals(StatusConsulta.CANCELADA) || consulta.getStatus().equals(StatusConsulta.REALIZADA)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }

    public void deletar(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        consultaRepository.delete(consulta);
    }

}
