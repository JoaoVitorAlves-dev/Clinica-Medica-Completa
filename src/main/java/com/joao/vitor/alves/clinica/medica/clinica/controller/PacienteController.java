package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.PacienteRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.PacienteResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // ADMIN FUNÇÕES

    @GetMapping("/listar")
    public ResponseEntity<Page<PacienteResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(pacienteService.listar(pageable));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<PacienteResponse> cadastrar(@RequestBody @Valid PacienteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.cadastrar(req));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<PacienteResponse> atualizarPorId(@PathVariable UUID id, @RequestBody @Valid PacienteRequest req) {
        return ResponseEntity.ok(pacienteService.atualizarPorId(id, req));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        pacienteService.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // USUARIO FUNÇÕES

    @GetMapping("/visualizar")
    public ResponseEntity<PacienteResponse> visualizarCadastro(Authentication auth) {
        return ResponseEntity.ok(pacienteService.visualizarCadastro(auth));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<PacienteResponse> cadastro(@RequestBody @Valid PacienteRequest pacienteRequest, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.cadastro(pacienteRequest, auth));
    }

    @PatchMapping("/atualizarcadastro/{id}")
    public ResponseEntity<PacienteResponse> atualizar(@PathVariable UUID id, @RequestBody @Valid PacienteRequest req, Authentication auth) {
        return ResponseEntity.ok(pacienteService.atualizar(id, req, auth));
    }

    @DeleteMapping("/deletarcadastro/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, Authentication auth) {
        pacienteService.deletar(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
