package br.com.atena.escolar.matriculamateria.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.disciplina.dominio.Disciplina;
import br.com.atena.escolar.matricula.dao.IMatriculaDAO;
import br.com.atena.escolar.matricula.dominio.Matricula;
import br.com.atena.escolar.matriculamateria.dao.IMatriculaMateriaDAO;
import br.com.atena.escolar.matriculamateria.dominio.MatriculaMateria;
import br.com.atena.escolar.matriculamateria.dto.MatriculaMateriaRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ManterMatriculaMateriaActor {
    private final IMatriculaMateriaDAO associacoes;
    private final IMatriculaDAO matriculas;
    private final IDisciplinaDAO disciplinas;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterMatriculaMateriaActor(
            IMatriculaMateriaDAO associacoes,
            IMatriculaDAO matriculas,
            IDisciplinaDAO disciplinas,
            AutorizacaoEnsinoActor autorizacao) {
        this.associacoes = associacoes;
        this.matriculas = matriculas;
        this.disciplinas = disciplinas;
        this.autorizacao = autorizacao;
    }

    public List<MatriculaMateriaRespostaDTO> listar(Long matriculaId) {
        var matricula = buscarMatricula(matriculaId);
        exigirLeitura(matricula);
        Map<Long, Disciplina> ofertas =
                disciplinas.daTurma(matricula.turmaId()).stream()
                        .collect(Collectors.toMap(Disciplina::id, Function.identity()));
        return associacoes.listar(matriculaId).stream()
                .map(associacao -> ofertas.get(associacao.disciplinaId()))
                .filter(this::podeLerOferta)
                .map(oferta -> resposta(matriculaId, oferta))
                .toList();
    }

    @Transactional
    public MatriculaMateriaRespostaDTO associar(Long matriculaId, Long disciplinaId) {
        autorizacao.exigirSecretaria();
        var matricula = buscarMatricula(matriculaId);
        var oferta =
                disciplinas
                        .buscar(disciplinaId)
                        .orElseThrow(() -> ErroAplicacao.ausente("Disciplina não encontrada"));
        if (!matricula.turmaId().equals(oferta.turmaId())) {
            throw ErroAplicacao.conflito("Disciplina não pertence à turma da matrícula");
        }
        associacoes.inserir(new MatriculaMateria(matriculaId, disciplinaId));
        return resposta(matriculaId, oferta);
    }

    @Transactional
    public void excluir(Long matriculaId, Long disciplinaId) {
        autorizacao.exigirSecretaria();
        buscarMatricula(matriculaId);
        if (!associacoes.excluir(new MatriculaMateria(matriculaId, disciplinaId))) {
            throw ErroAplicacao.ausente("Associação não encontrada");
        }
    }

    private Matricula buscarMatricula(Long matriculaId) {
        return matriculas
                .buscar(matriculaId)
                .orElseThrow(() -> ErroAplicacao.ausente("Matrícula não encontrada"));
    }

    private void exigirLeitura(Matricula matricula) {
        var usuario = autorizacao.usuario();
        if (usuario.aluno() && !usuario.identificador().equals(matricula.alunoId())) {
            ErroAplicacao.negar();
        }
        autorizacao.exigirTurma(matricula.turmaId(), false);
    }

    private boolean podeLerOferta(Disciplina oferta) {
        if (oferta == null) {
            throw ErroAplicacao.conflito("Associação incompatível com a turma da matrícula");
        }
        var usuario = autorizacao.usuario();
        return !usuario.professor() || usuario.identificador().equals(oferta.professorId());
    }

    private MatriculaMateriaRespostaDTO resposta(Long matriculaId, Disciplina oferta) {
        return new MatriculaMateriaRespostaDTO(matriculaId, oferta.id(), oferta.materiaId());
    }
}
