package br.com.atena.escolar.boletim.dominio;

import br.com.atena.escolar.unidade.dominio.Unidade;

import java.util.List;

public record BoletimPreparado(
        Long alunoId,
        String nome,
        String matricula,
        String documento,
        Unidade unidade,
        List<DisciplinaPreparada> linhas) {
    public BoletimPreparado {
        linhas = List.copyOf(linhas);
    }
}
