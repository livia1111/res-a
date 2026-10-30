package com.seupacote.ms1.adapter.in.controller.dto;

import java.util.List;

public class CsvImportResponseDTO {

    private final int total;
    private final List<String> emailsImportados;

    public CsvImportResponseDTO(List<String> emailsImportados) {
        this.emailsImportados = emailsImportados;
        this.total = emailsImportados.size();
    }

    public int getTotal()                     { return total; }
    public List<String> getEmailsImportados() { return emailsImportados; }
}

