package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.MedicoRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.MedicoResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<MedicoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(medicoService.listar(pageable));
    }

    @PostMapping("/criar")
    public ResponseEntity<MedicoResponse> criar(@RequestBody @Valid MedicoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.criar(req));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<MedicoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid MedicoRequest req, Authentication auth) {
        return ResponseEntity.ok(medicoService.atualizarPorId(id, req, auth));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        medicoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
