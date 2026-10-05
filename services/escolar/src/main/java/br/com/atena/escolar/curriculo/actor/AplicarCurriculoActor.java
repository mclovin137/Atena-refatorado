package br.com.atena.escolar.curriculo.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.curriculo.dao.ICurriculoDAO;
import br.com.atena.escolar.curriculo.dto.AplicarCurriculoRequisicaoDTO;
import br.com.atena.escolar.disciplina.builder.DisciplinaBuilder;
import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.disciplina.dominio.Disciplina;
import br.com.atena.escolar.disciplina.dto.DisciplinaRespostaDTO;
import br.com.atena.escolar.turma.dao.ITurmaDAO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class AplicarCurriculoActor {
    private final ICurriculoDAO curriculos;
    private final IDisciplinaDAO disciplinas;
    private final ITurmaDAO turmas;
    private final AutorizacaoEnsinoActor autorizacao;

    public AplicarCurriculoActor(
            ICurriculoDAO curriculos,
            IDisciplinaDAO disciplinas,
            ITurmaDAO turmas,
            AutorizacaoEnsinoActor autorizacao) {
        this.curriculos = curriculos;
        this.disciplinas = disciplinas;
        this.turmas = turmas;
        this.autorizacao = autorizacao;
    }

    @Transactional
    public List<DisciplinaRespostaDTO> executar(
            Long turmaId, AplicarCurriculoRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        turmas.buscar(turmaId).orElseThrow(() -> ErroAplicacao.ausente("Turma não encontrada"));
        var grade =
                curriculos.listar().stream()
                        .filter(curriculo -> curriculo.nome().equals(requisicao.nome()))
                        .toList();
        if (grade.isEmpty()) {
            throw ErroAplicacao.ausente("Grade não encontrada");
        }
        for (var componente : grade) {
            boolean existe =
                    disciplinas.daTurma(turmaId).stream()
                            .anyMatch(
                                    disciplina ->
                                            disciplina.materiaId().equals(componente.materiaId()));
            if (!existe) {
                disciplinas.salvar(
                        new Disciplina(
                                null,
                                turmaId,
                                componente.materiaId(),
                                null,
                                componente.cargaHoraria()));
            }
        }
        return disciplinas.daTurma(turmaId).stream().map(DisciplinaBuilder::resposta).toList();
    }
}
