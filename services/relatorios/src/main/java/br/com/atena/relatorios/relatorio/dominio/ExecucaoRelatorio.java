package br.com.atena.relatorios.relatorio.dominio;

public record ExecucaoRelatorio(Long id, String status) {
    public boolean falhou() {
        return "FAILED".equals(status);
    }

    public boolean concluida() {
        return "COMPLETED".equals(status);
    }
}
