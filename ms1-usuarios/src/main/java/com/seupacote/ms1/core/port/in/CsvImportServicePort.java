package com.seupacote.ms1.core.port.in;

import java.io.InputStream;
import java.util.List;

public interface CsvImportServicePort {
    /**
     * Lê um InputStream de CSV (header: nome,email,senha) e cria os usuários.
     *
     * @return lista de e-mails importados com sucesso
     */
    List<String> importarCsv(InputStream csvStream);
}

