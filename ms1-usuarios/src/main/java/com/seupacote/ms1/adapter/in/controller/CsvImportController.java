package com.seupacote.ms1.adapter.in.controller;

import com.seupacote.ms1.adapter.in.controller.dto.CsvImportResponseDTO;
import com.seupacote.ms1.core.port.in.CsvImportServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Adaptador primário — importação em lote de usuários via CSV.
 * Requer autenticação JWT.
 *
 * Formato do arquivo CSV (com header na primeira linha):
 *   nome,email,senha
 *   João Silva,joao@email.com,minhasenha
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "CRUD completo de usuários")
@SecurityRequirement(name = "bearerAuth")
public class CsvImportController {

    private final CsvImportServicePort csvImportService;

    public CsvImportController(CsvImportServicePort csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Importa usuários via CSV",
        description = """
            Importa usuários em lote a partir de um arquivo CSV.

            **Formato esperado** (com header):
            ```
            nome,email,senha
            João Silva,joao@email.com,senha123
            Maria Santos,maria@email.com,senha456
            ```
            - Linhas com campos vazios são ignoradas
            - E-mails duplicados são ignorados (não geram erro)
            - Retorna a lista de e-mails importados com sucesso
            """
    )
    public ResponseEntity<CsvImportResponseDTO> importarCsv(
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<String> importados = csvImportService.importarCsv(file.getInputStream());
        return ResponseEntity.ok(new CsvImportResponseDTO(importados));
    }
}

