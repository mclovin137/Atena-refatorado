package br.com.atena.relatorios.relatorio.dominio;

import java.util.Objects;

public record SolicitacaoRelatorio(
        Long alunoId,
        Long ownerId,
        Long professorId,
        boolean releasedOnly,
        TipoRelatorioEnum tipo,
        String requestId) {
    public SolicitacaoRelatorio {
        Objects.requireNonNull(ownerId, "Informe o responsável");
        Objects.requireNonNull(tipo, "Informe o tipo");
        if (requestId == null || requestId.isBlank() || requestId.length() > 100) {
            throw new IllegalArgumentException("Identificador da solicitação inválido");
        }
        if (!tipo.lista() && alunoId == null) {
            throw new ErroRelatorio(ErroRelatorio.CategoriaEnum.INVALIDO, "Informe o aluno");
        }
    }
}
