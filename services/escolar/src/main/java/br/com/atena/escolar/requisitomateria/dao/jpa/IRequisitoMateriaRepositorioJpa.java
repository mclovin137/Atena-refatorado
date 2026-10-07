package br.com.atena.escolar.requisitomateria.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IRequisitoMateriaRepositorioJpa
        extends JpaRepository<RequisitoMateriaEntidadeJpa, RequisitoMateriaChaveJpa> {
    @Query(
            "SELECT a FROM RequisitoMateriaEntidadeJpa a WHERE a.chave.requisitoId=:id ORDER BY"
                    + " a.chave.materiaId")
    List<RequisitoMateriaEntidadeJpa> listar(@Param("id") Long id);

    @Modifying
    @Query(
            value =
                    "INSERT INTO requisito_materia(requisito_id,materia_id)"
                            + " VALUES(:esquerda,:direita)",
            nativeQuery = true)
    int inserir(@Param("esquerda") Long esquerda, @Param("direita") Long direita);

    @Modifying
    @Query(
            value =
                    "DELETE FROM requisito_materia WHERE requisito_id=:esquerda AND"
                            + " materia_id=:direita",
            nativeQuery = true)
    int excluir(@Param("esquerda") Long esquerda, @Param("direita") Long direita);
}
