package br.com.atena.escolar.boletim.dominio;

import br.com.atena.escolar.pessoa.dominio.Pessoa;
import br.com.atena.escolar.unidade.dominio.Unidade;

import java.util.List;

public record DadosBoletim(
        Pessoa aluno, Unidade unidade, List<DadosDisciplinaBoletim> disciplinas) {
    public DadosBoletim {
        disciplinas = List.copyOf(disciplinas);
    }
}
