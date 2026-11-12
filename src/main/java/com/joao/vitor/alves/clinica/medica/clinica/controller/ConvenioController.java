package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.ConvenioRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.ConvenioResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.ConvenioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/convenios")
public class ConvenioController {

    private final ConvenioService convenioService;

    public ConvenioController(ConvenioService convenioService) {
        this.convenioService = convenioService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<ConvenioResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(convenioService.listar(pageable));
    }

    @PostMapping("/criar")
    public ResponseEntity<ConvenioResponse> criar(@RequestBody @Valid ConvenioRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(convenioService.criar(req));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<ConvenioResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ConvenioRequest req) {
        return ResponseEntity.ok(convenioService.atualizar(id, req));
    }

    @DeleteMapping("/desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        convenioService.desativar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
