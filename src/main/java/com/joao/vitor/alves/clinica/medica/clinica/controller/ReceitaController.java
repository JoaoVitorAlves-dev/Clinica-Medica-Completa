package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ReceitaRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ReceitaResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.ReceitaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<ReceitaResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(receitaService.listar(pageable));
    }

    @PostMapping("/criar")
    public ResponseEntity<ReceitaResponse> criar(@RequestBody @Valid ReceitaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaService.criar(req));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<ReceitaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ReceitaRequest req) {
        return ResponseEntity.ok(receitaService.atualizar(id, req));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        receitaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
