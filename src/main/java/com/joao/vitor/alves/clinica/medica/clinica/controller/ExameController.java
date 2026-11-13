package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ExameRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ExameResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.ExameService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exames")
public class ExameController {

    private final ExameService exameService;

    public ExameController(ExameService exameService) {
        this.exameService = exameService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<ExameResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(exameService.listar(pageable));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<ExameResponse> adicionar(@RequestBody @Valid ExameRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exameService.criar(req));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<ExameResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ExameRequest req) {
        return ResponseEntity.ok(exameService.atualizar(id, req));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        exameService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
