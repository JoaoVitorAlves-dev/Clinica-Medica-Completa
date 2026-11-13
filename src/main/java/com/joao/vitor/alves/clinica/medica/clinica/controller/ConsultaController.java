package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ConsultaRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ConvenioRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ConsultaResponse;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ConvenioResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.ConsultaService;
import com.joao.vitor.alves.clinica.medica.clinica.service.ConvenioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<ConsultaResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<ConsultaResponse> adicionar(@RequestBody @Valid ConsultaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.adicionar(req));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<ConsultaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ConsultaRequest req) {
        return ResponseEntity.ok(consultaService.atualizar(id, req));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        consultaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
