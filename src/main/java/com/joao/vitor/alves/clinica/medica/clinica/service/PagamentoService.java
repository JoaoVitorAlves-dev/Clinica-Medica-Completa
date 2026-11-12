package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Consulta;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Pagamento;
import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.StatusConsulta;
import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.StatusPagamento;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.PagamentoRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.PagamentoResponse;
import com.joao.vitor.alves.clinica.medica.clinica.mapper.PagamentoMapper;
import com.joao.vitor.alves.clinica.medica.clinica.repository.ConsultaRepository;
import com.joao.vitor.alves.clinica.medica.clinica.repository.PagamentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ConsultaRepository consultaRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, ConsultaRepository consultaRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.consultaRepository = consultaRepository;
    }

    public Page<PagamentoResponse> listar(Pageable pageable, Authentication auth) {
        return pagamentoRepository.findByConsultaPacienteUsuarioEmail(auth.getName(), pageable).map(PagamentoMapper::toDTO);
    }

    public PagamentoResponse criar(PagamentoRequest req, Authentication auth) {
        Consulta consulta = consultaRepository.findById(req.consultaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!consulta.getPaciente().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!consulta.getMedico().getUsuario().getAtivo() || !consulta.getPaciente().getConvenio().getAtivo() || !consulta.getPaciente().getUsuario().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (consulta.getStatus().equals(StatusConsulta.CANCELADA)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }

        Pagamento entity = PagamentoMapper.toEntity(req, consulta);
        entity.setStatus(StatusPagamento.PENDENTE);
        return PagamentoMapper.toDTO(pagamentoRepository.save(entity));
    }

    public PagamentoResponse atualizar(UUID id, PagamentoRequest req, Authentication auth) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Consulta consulta = consultaRepository.findById(req.consultaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!pagamento.getConsulta().getPaciente().getUsuario().getEmail().equals(auth.getName()) || !consulta.getPaciente().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (pagamento.getStatus() != StatusPagamento.PENDENTE || pagamento.getStatus().equals(StatusPagamento.CANCELADO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (pagamento.getConsulta().getStatus().equals(StatusConsulta.CANCELADA) || consulta.getStatus().equals(StatusConsulta.CANCELADA)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }
        if (!pagamento.getConsulta().getPaciente().getUsuario().getAtivo() || !pagamento.getConsulta().getMedico().getUsuario().getAtivo()
                || !consulta.getPaciente().getUsuario().getAtivo() || !consulta.getMedico().getUsuario().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        pagamento.setMetodo(req.metodo());
        pagamento.setConsulta(consulta);

        return PagamentoMapper.toDTO(pagamentoRepository.save(pagamento));
    }

    public void cancelar(UUID id, Authentication auth) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!pagamento.getConsulta().getPaciente().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (pagamento.getStatus() != StatusPagamento.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }
        if (!pagamento.getConsulta().getPaciente().getUsuario().getAtivo() || !pagamento.getConsulta().getPaciente().getConvenio().getAtivo() ||
                !pagamento.getConsulta().getMedico().getUsuario().getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        pagamento.setStatus(StatusPagamento.CANCELADO);
        pagamentoRepository.save(pagamento);
    }

    public void deletar(UUID id, Authentication auth) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!pagamento.getConsulta().getPaciente().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        pagamentoRepository.delete(pagamento);
    }

}
