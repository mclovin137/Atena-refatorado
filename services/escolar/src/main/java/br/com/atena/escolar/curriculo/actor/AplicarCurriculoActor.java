package br.com.atena.escolar.curriculo.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.curriculo.dao.ICurriculoDAO;
import br.com.atena.escolar.curriculo.dto.AplicarCurriculoRequisicaoDTO;
import br.com.atena.escolar.disciplina.builder.DisciplinaBuilder;
import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.disciplina.dominio.Disciplina;
import br.com.atena.escolar.disciplina.dto.DisciplinaRespostaDTO;
import br.com.atena.escolar.gradecurricular.dao.IGradeCurricularDAO;
import br.com.atena.escolar.turma.dao.ITurmaDAO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AplicarCurriculoActor {
    private final ICurriculoDAO curriculos;
    private final IDisciplinaDAO disciplinas;
    private final ITurmaDAO turmas;
    private final AutorizacaoEnsinoActor autorizacao;
    private final IGradeCurricularDAO grades;

    public AplicarCurriculoActor(
            ICurriculoDAO curriculos,
            IDisciplinaDAO disciplinas,
            ITurmaDAO turmas,
            AutorizacaoEnsinoActor autorizacao,
            IGradeCurricularDAO grades) {
        this.curriculos = curriculos;
        this.disciplinas = disciplinas;
        this.turmas = turmas;
        this.autorizacao = autorizacao;
        this.grades = grades;
    }

    @Transactional
    public List<DisciplinaRespostaDTO> executar(
            Long turmaId, AplicarCurriculoRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        var turma =
                turmas.buscar(turmaId)
                        .orElseThrow(() -> ErroAplicacao.ausente("Turma não encontrada"));
        var grade =
                curriculos.listar().stream()
                        .filter(curriculo -> curriculo.nome().equals(requisicao.nome()))
                        .toList();
        if (grade.isEmpty()) {
            throw ErroAplicacao.ausente("Grade não encontrada");
        }
        var gradesPorId =
                grades.listar().stream()
                        .collect(
                                Collectors.toMap(
                                        br.com.atena.escolar.gradecurricular.dominio.GradeCurricular
                                                ::id,
                                        Function.identity()));
        for (var componente : grade) {
            if (componente.gradeId() != null) {
                var vinculo = gradesPorId.get(componente.gradeId());
                if (vinculo == null) {
                    throw ErroAplicacao.ausente("Grade curricular não encontrada");
                }
                if (!vinculo.cursoId().equals(turma.cursoId())) {
                    throw ErroAplicacao.conflito("Grade curricular não pertence ao curso da turma");
                }
            }
        }
        var materiasOfertadas =
                disciplinas.daTurma(turmaId).stream()
                        .map(Disciplina::materiaId)
                        .collect(Collectors.toCollection(HashSet::new));
        for (var componente : grade) {
            if (materiasOfertadas.add(componente.materiaId())) {
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
