package com.seupacote.ms1.core.service;

import com.seupacote.ms1.configuration.exception.ConflictException;
import com.seupacote.ms1.core.domain.User;
import com.seupacote.ms1.core.port.in.CsvImportServicePort;
import com.seupacote.ms1.core.port.in.UserServicePort;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso: importação em lote de usuários via CSV.
 *
 * Formato esperado do arquivo (com linha de header):
 *   nome,email,senha
 *   João Silva,joao@email.com,senha123
 *
 * Linhas com campos vazios são ignoradas.
 * E-mails duplicados também são ignorados (não geram erro).
 */
@Service
public class CsvImportService implements CsvImportServicePort {

    private final UserServicePort userService;

    public CsvImportService(UserServicePort userService) {
        this.userService = userService;
    }

    @Override
    public List<String> importarCsv(InputStream csvStream) {
        List<String> importados = new ArrayList<>();

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("nome", "email", "senha")
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        try (Reader reader = new InputStreamReader(csvStream, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(reader, format)) {

            for (CSVRecord record : parser) {
                String nome  = record.get("nome");
                String email = record.get("email");
                String senha = record.get("senha");

                // Pula linha se qualquer campo obrigatório estiver vazio
                if (isBlank(nome) || isBlank(email) || isBlank(senha)) {
                    continue;
                }

                try {
                    userService.criarUsuario(new User(nome, email, senha));
                    importados.add(email);
                } catch (ConflictException e) {
                    // E-mail duplicado — ignora e continua o processamento
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo CSV: " + e.getMessage(), e);
        }

        return importados;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}

