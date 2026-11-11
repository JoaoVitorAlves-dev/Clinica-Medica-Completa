package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.RefreshToken;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Usuario;
import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.RoleUsuario;
import com.joao.vitor.alves.clinica.medica.clinica.dto.request.UsuarioRequest;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.RefreshTokenResponse;
import com.joao.vitor.alves.clinica.medica.clinica.dto.response.UsuarioResponse;
import com.joao.vitor.alves.clinica.medica.clinica.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RefreshTokenService refreshTokenService, TokenService tokenService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.refreshTokenService = refreshTokenService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse listar(Authentication auth) {
        Usuario usuario = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail());
    }

    @Transactional
    public RefreshTokenResponse refresh(String token) {
        RefreshToken refreshToken = refreshTokenService.validarERotacionar(token);

        String access = tokenService.gerarToken(refreshToken.getUsuario().getEmail());

        return new RefreshTokenResponse(access, refreshToken.getToken());
    }

    @Transactional
    public UsuarioResponse registrar(UsuarioRequest req) {
        if (usuarioRepository.existsByEmail(req.email()) || usuarioRepository.existsByCpf(req.cpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(req.nome());
        usuario.setEmail(req.email());
        usuario.setCpf(req.cpf());
        usuario.setPassword(passwordEncoder.encode(req.password()));
        usuario.setAtivo(true);
        usuario.setRole(RoleUsuario.USER);
        usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail());
    }

    @Transactional
    public RefreshTokenResponse login(UsuarioRequest req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));

        Usuario usuario = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String access = tokenService.gerarToken(usuario.getEmail());
        String refresh = refreshTokenService.criar(usuario).getToken();

        return new RefreshTokenResponse(access, refresh);
    }

    @Transactional
    public UsuarioResponse atualizarPorId(UUID id, UsuarioRequest req, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!usuario.getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (usuarioRepository.existsByCpf(req.cpf()) || usuarioRepository.existsByCpf(req.cpf()) || usuario.getAtivo() == false) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        usuario.setNome(req.nome());
        usuario.setPassword(passwordEncoder.encode(req.password()));
        usuario.setEmail(req.email());
        usuario.setCpf(req.cpf());

        usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail());
    }

    @Transactional
    public void desativarConta(UUID id, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!usuario.getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (usuario.getAtivo() == false) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

}
