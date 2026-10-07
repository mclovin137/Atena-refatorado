package br.com.atena.escolar.importacao.actor;

import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.importado;
import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.pendente;

import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;
import br.com.atena.escolar.serie.dao.ISerieDAO;
import br.com.atena.escolar.serie.dominio.Serie;
import br.com.atena.escolar.turma.dao.ITurmaDAO;
import br.com.atena.escolar.turma.dominio.Turma;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ImportarTurmasLegadoActor {
    private final ISerieDAO series;
    private final ITurmaDAO turmas;

    public ImportarTurmasLegadoActor(ISerieDAO series, ITurmaDAO turmas) {
        this.series = series;
        this.turmas = turmas;
    }

    public RegistroImportacaoDTO executar(
            RegistroImportacaoDTO registro, List<RegistroImportacaoDTO> fontes) {
        String nomeCurso = registro.dados().get("curso");
        var cursos =
                fontes.stream()
                        .filter(
                                fonte ->
                                        fonte.tabela().equals("curso")
                                                && nomeCurso.equals(fonte.dados().get("nome"))
                                                && fonte.destinos().containsKey("cursoId"))
                        .map(fonte -> fonte.destinos().get("cursoId"))
                        .distinct()
                        .toList();
        if (cursos.size() != 1) {
            return pendente(registro, "CURSO_NAO_RESOLVIDO");
        }
        String periodo = registro.dados().get("periodo");
        if (periodo == null || !periodo.matches("[1-9][0-9]?° Ano")) {
            return pendente(registro, "SERIE_NAO_RESOLVIDA");
        }
        var encontradas =
                series.listar().stream().filter(serie -> serie.nome().equals(periodo)).toList();
        if (encontradas.size() > 1) {
            return pendente(registro, "SERIE_AMBIGUA");
        }
        int ano;
        try {
            ano = Integer.parseInt(registro.dados().get("ano"));
        } catch (NumberFormatException erro) {
            return pendente(registro, "ANO_NAO_SUPORTADO");
        }
        if (ano < 1900 || ano > 2200) {
            return pendente(registro, "ANO_NAO_SUPORTADO");
        }
        var serie =
                encontradas.isEmpty()
                        ? series.salvar(new Serie(null, periodo))
                        : encontradas.getFirst();
        String complemento = registro.dados().get("complemento");
        String nome =
                periodo + ((complemento == null || complemento.isBlank()) ? "" : " " + complemento);
        var existentes =
                turmas.listar().stream()
                        .filter(
                                turma ->
                                        turma.cursoId().equals(cursos.getFirst())
                                                && turma.serieId().equals(serie.id())
                                                && turma.ano() == ano
                                                && turma.nome().equals(nome))
                        .toList();
        if (existentes.size() > 1) {
            return pendente(registro, "TURMA_AMBIGUA");
        }
        var turma =
                existentes.isEmpty()
                        ? turmas.salvar(new Turma(null, cursos.getFirst(), serie.id(), ano, nome))
                        : existentes.getFirst();
        return importado(registro, Map.of("turmaId", turma.id(), "serieId", serie.id()));
    }
}
