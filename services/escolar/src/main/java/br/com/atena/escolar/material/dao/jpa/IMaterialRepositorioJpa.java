package br.com.atena.escolar.material.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IMaterialRepositorioJpa extends JpaRepository<MaterialEntidadeJpa, Long> {
    @Query(
            "select new br.com.atena.escolar.material.dao.jpa.MaterialEntidadeJpa(registro.id,"
                    + " registro.disciplinaId, registro.nome, registro.tipo, registro.arquivo,"
                    + " registro.autorId) from MaterialEntidadeJpa registro order by registro.id")
    List<MaterialEntidadeJpa> listarMetadados();

    @Query(
            "select new br.com.atena.escolar.material.dao.jpa.MaterialEntidadeJpa(registro.id,"
                    + " registro.disciplinaId, registro.nome, registro.tipo, registro.arquivo,"
                    + " registro.autorId) from MaterialEntidadeJpa registro where registro.id ="
                    + " :identificador")
    Optional<MaterialEntidadeJpa> buscarMetadados(@Param("identificador") Long identificador);

    @Query(
            "select registro.conteudo from MaterialEntidadeJpa registro where registro.id ="
                    + " :identificador")
    byte[] lerConteudo(@Param("identificador") Long identificador);

    @Modifying
    @Transactional
    @Query(
            "update MaterialEntidadeJpa registro set registro.conteudo = :conteudo where"
                    + " registro.id = :identificador")
    void gravarConteudo(
            @Param("identificador") Long identificador, @Param("conteudo") byte[] conteudo);

    @Modifying
    @Transactional
    @Query(
            "update MaterialEntidadeJpa registro set registro.disciplinaId = :disciplinaId,"
                    + " registro.nome = :nome, registro.tipo = :tipo, registro.arquivo = :arquivo,"
                    + " registro.autorId = :autorId where registro.id = :id")
    void atualizarMetadados(
            @Param("id") Long id,
            @Param("disciplinaId") Long disciplinaId,
            @Param("nome") String nome,
            @Param("tipo") String tipo,
            @Param("arquivo") String arquivo,
            @Param("autorId") Long autorId);
}
