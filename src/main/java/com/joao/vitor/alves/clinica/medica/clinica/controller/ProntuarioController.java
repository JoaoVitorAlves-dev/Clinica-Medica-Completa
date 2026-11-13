package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ProntuarioRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ProntuarioResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.ProntuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<ProntuarioResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(prontuarioService.listar(pageable));
    }

    @PostMapping("/criar")
    public ResponseEntity<ProntuarioResponse> criar(@RequestBody @Valid ProntuarioRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prontuarioService.criar(req));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<ProntuarioResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ProntuarioRequest req) {
        return ResponseEntity.ok(prontuarioService.atualizar(id, req));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        prontuarioService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
