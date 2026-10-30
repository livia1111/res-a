package com.seupacote.ms1.adapter.in.controller;

import com.seupacote.ms1.adapter.in.controller.dto.UserRequestDTO;
import com.seupacote.ms1.adapter.in.controller.dto.UserResponseDTO;
import com.seupacote.ms1.core.domain.User;
import com.seupacote.ms1.core.port.in.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adaptador primário — expõe os casos de uso de User via API REST.
 * POST /users (criar) é público; os demais requerem JWT.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "CRUD completo de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserServicePort userService;

    public UserController(UserServicePort userService) {
        this.userService = userService;
    }

    // ─── POST /users ──────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Cria novo usuário", description = "Endpoint público — não requer JWT.")
    @ApiResponse(responseCode = "201", description = "Usuário criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    public ResponseEntity<UserResponseDTO> criarUsuario(@Valid @RequestBody UserRequestDTO dto) {
        User criado = userService.criarUsuario(toUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.fromDomain(criado));
    }

    // ─── GET /users ───────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Lista todos os usuários")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        List<UserResponseDTO> lista = userService.listarUsuarios()
                .stream()
                .map(UserResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // ─── GET /users/{id} ──────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuário por ID")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<UserResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(UserResponseDTO.fromDomain(userService.buscarPorId(id)));
    }

    // ─── PUT /users/{id} ──────────────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza dados do usuário")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<UserResponseDTO> atualizarUsuario(
            @PathVariable String id,
            @Valid @RequestBody UserRequestDTO dto) {
        User atualizado = userService.atualizarUsuario(id, toUser(dto));
        return ResponseEntity.ok(UserResponseDTO.fromDomain(atualizado));
    }

    // ─── DELETE /users/{id} ───────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove usuário")
    @ApiResponse(responseCode = "204", description = "Usuário removido")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String id) {
        userService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Mapeamento DTO → Domínio ─────────────────────────────────────────────

    private User toUser(UserRequestDTO dto) {
        return new User(dto.getNome(), dto.getEmail(), dto.getSenha());
    }
}
