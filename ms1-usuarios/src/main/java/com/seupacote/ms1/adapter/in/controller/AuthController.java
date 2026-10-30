package com.seupacote.ms1.adapter.in.controller;

import com.seupacote.ms1.adapter.in.controller.dto.LoginRequestDTO;
import com.seupacote.ms1.adapter.in.controller.dto.LoginResponseDTO;
import com.seupacote.ms1.core.port.in.AuthServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador primário — endpoint de autenticação.
 * Público: não requer JWT.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Login e geração de token JWT Bearer")
public class AuthController {

    private final AuthServicePort authService;

    public AuthController(AuthServicePort authService) {
        this.authService = authService;
    }

    /**
     * POST /auth/login
     * Retorna token JWT válido por 24h (configurável via jwt.expiration.ms).
     */
    @PostMapping("/login")
    @Operation(
        summary = "Realiza login",
        description = "Autentica e-mail + senha e retorna token JWT para usar nos demais endpoints."
    )
    @ApiResponse(responseCode = "200", description = "Login realizado — token retornado")
    @ApiResponse(responseCode = "400", description = "Campos inválidos")
    @ApiResponse(responseCode = "404", description = "Credenciais inválidas")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        String token = authService.login(dto.getEmail(), dto.getSenha());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}

