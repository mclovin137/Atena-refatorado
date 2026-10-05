package br.com.atena.escolar.nota.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface INotaRepositorioJpa extends JpaRepository<NotaEntidadeJpa, Long> {
    List<NotaEntidadeJpa> findByDisciplinaIdAndAlunoIdOrderByBimestre(
            Long disciplinaId, Long alunoId);

    boolean existsByDisciplinaIdAndAlunoIdAndBimestre(
            Long disciplinaId, Long alunoId, int bimestre);

    @Modifying
    @Query(
            value =
                    "INSERT INTO nota(disciplina_id, aluno_id, bimestre, liberada) VALUES"
                        + " (:disciplinaId, :alunoId, :bimestre, false) ON CONFLICT (disciplina_id,"
                        + " aluno_id, bimestre) DO NOTHING",
            nativeQuery = true)
    void prepararPeriodo(
            @Param("disciplinaId") Long disciplinaId,
            @Param("alunoId") Long alunoId,
            @Param("bimestre") int bimestre);
}
