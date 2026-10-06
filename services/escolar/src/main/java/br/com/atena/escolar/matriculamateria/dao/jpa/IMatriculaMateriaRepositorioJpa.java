package br.com.atena.escolar.matriculamateria.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMatriculaMateriaRepositorioJpa
        extends JpaRepository<MatriculaMateriaEntidadeJpa, MatriculaMateriaChaveJpa> {
    @Query(
            "SELECT a FROM MatriculaMateriaEntidadeJpa a WHERE a.chave.matriculaId=:id ORDER BY"
                    + " a.chave.disciplinaId")
    List<MatriculaMateriaEntidadeJpa> listar(@Param("id") Long id);

    @Modifying
    @Query(
            value =
                    "INSERT INTO matricula_disciplina(matricula_id,disciplina_id)"
                            + " VALUES(:esquerda,:direita)",
            nativeQuery = true)
    int inserir(@Param("esquerda") Long esquerda, @Param("direita") Long direita);

    @Modifying
    @Query(
            value =
                    "DELETE FROM matricula_disciplina WHERE matricula_id=:esquerda AND"
                            + " disciplina_id=:direita",
            nativeQuery = true)
    int excluir(@Param("esquerda") Long esquerda, @Param("direita") Long direita);
}
