package com.joao.vitor.alves.clinica.medica.clinica.controller;

import com.joao.vitor.alves.clinica.medica.clinica.dto.request.RefreshTokenRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.UsuarioRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.RefreshTokenResponse;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.UsuarioResponse;
import com.joao.vitor.alves.clinica.medica.clinica.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listar")
    public ResponseEntity<UsuarioResponse> listar(Authentication auth) {
        return ResponseEntity.ok(usuarioService.listar(auth));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(usuarioService.refresh(request.refreshToken()));
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody @Valid UsuarioRequest usuarioRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(usuarioRequest));
    }

    @PostMapping("/logar")
    public ResponseEntity<RefreshTokenResponse> logar(@RequestBody @Valid UsuarioRequest usuarioRequest) {
        return ResponseEntity.ok(usuarioService.login(usuarioRequest));
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable UUID id, @RequestBody @Valid UsuarioRequest usuarioRequest, Authentication auth) {
        return ResponseEntity.ok(usuarioService.atualizarPorId(id, usuarioRequest, auth));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> desativar(@PathVariable UUID id, Authentication auth) {
        usuarioService.desativarConta(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
