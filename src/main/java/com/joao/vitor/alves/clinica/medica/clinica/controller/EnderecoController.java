package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.EnderecoRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.EnderecoResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<EnderecoResponse>> listar(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(enderecoService.listar(pageable, auth));
    }

    @PostMapping("/criar")
    public ResponseEntity<EnderecoResponse> criar(@RequestBody @Valid EnderecoRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.criar(req, auth));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<EnderecoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid EnderecoRequest req, Authentication auth) {
        return ResponseEntity.ok(enderecoService.atualizar(id, req, auth));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication auth) {
        enderecoService.deletar(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
