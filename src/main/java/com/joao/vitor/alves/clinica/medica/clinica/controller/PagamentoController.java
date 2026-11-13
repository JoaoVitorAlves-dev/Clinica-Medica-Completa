package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.PagamentoRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.PagamentoResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<PagamentoResponse>> listar(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(pagamentoService.listar(pageable, auth));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<PagamentoResponse> adicionar(@RequestBody @Valid PagamentoRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.criar(req, auth));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<PagamentoResponse> atualizar(@PathVariable UUID id, @RequestBody @Valid PagamentoRequest req, Authentication auth) {
        return ResponseEntity.ok(pagamentoService.atualizar(id, req, auth));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, Authentication auth) {
        pagamentoService.deletar(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
