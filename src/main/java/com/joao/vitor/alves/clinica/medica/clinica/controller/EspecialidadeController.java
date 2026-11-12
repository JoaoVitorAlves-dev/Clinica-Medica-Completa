package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.EspecialidadeRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.EspecialidadeResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.EspecialidadeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    public EspecialidadeController(EspecialidadeService especialidadeService) {
        this.especialidadeService = especialidadeService;
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<EspecialidadeResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(especialidadeService.listar(pageable));
    }

    @PostMapping("/criar")
    public ResponseEntity<EspecialidadeResponse> criar(@RequestBody @Valid EspecialidadeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeService.criar(req));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<EspecialidadeResponse> atualizar(@PathVariable Long id, @RequestBody @Valid EspecialidadeRequest req) {
        return ResponseEntity.ok(especialidadeService.atualizar(id, req));
    }

    @DeleteMapping("/desativar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        especialidadeService.desativar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
