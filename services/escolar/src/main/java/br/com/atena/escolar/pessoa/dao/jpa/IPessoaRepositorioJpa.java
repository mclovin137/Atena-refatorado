package br.com.atena.escolar.pessoa.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IPessoaRepositorioJpa extends JpaRepository<PessoaEntidadeJpa, Long> {
    Optional<PessoaEntidadeJpa> findByEmail(String email);

    @Query(
            "select new br.com.atena.escolar.pessoa.dao.jpa.PessoaEntidadeJpa(registro.id,"
                + " registro.nome, registro.email, registro.matricula, registro.perfil,"
                + " registro.senhaHash, registro.ativo, registro.telefone, registro.endereco,"
                + " registro.documento, registro.nascimento, registro.cep, registro.pai,"
                + " registro.mae, registro.estadoCivil, registro.sexo,"
                + " registro.telefoneResponsavel, registro.emailResponsavel, registro.avatar) from"
                + " PessoaEntidadeJpa registro order by registro.id")
    List<PessoaEntidadeJpa> listarMetadados();

    @Query(
            "select new br.com.atena.escolar.pessoa.dao.jpa.PessoaEntidadeJpa(registro.id,"
                + " registro.nome, registro.email, registro.matricula, registro.perfil,"
                + " registro.senhaHash, registro.ativo, registro.telefone, registro.endereco,"
                + " registro.documento, registro.nascimento, registro.cep, registro.pai,"
                + " registro.mae, registro.estadoCivil, registro.sexo,"
                + " registro.telefoneResponsavel, registro.emailResponsavel, registro.avatar) from"
                + " PessoaEntidadeJpa registro where registro.id = :identificador")
    Optional<PessoaEntidadeJpa> buscarMetadados(@Param("identificador") Long identificador);

    @Query(
            "select registro.avatarConteudo from PessoaEntidadeJpa registro where registro.id ="
                    + " :identificador")
    byte[] lerConteudo(@Param("identificador") Long identificador);

    @Modifying
    @Transactional
    @Query(
            "update PessoaEntidadeJpa registro set registro.avatarConteudo = :conteudo where"
                    + " registro.id = :identificador")
    void gravarConteudo(
            @Param("identificador") Long identificador, @Param("conteudo") byte[] conteudo);

    @Query(
            "select new br.com.atena.escolar.pessoa.dao.jpa.PessoaEntidadeJpa(registro.id,"
                + " registro.nome, registro.email, registro.matricula, registro.perfil,"
                + " registro.senhaHash, registro.ativo, registro.telefone, registro.endereco,"
                + " registro.documento, registro.nascimento, registro.cep, registro.pai,"
                + " registro.mae, registro.estadoCivil, registro.sexo,"
                + " registro.telefoneResponsavel, registro.emailResponsavel, registro.avatar) from"
                + " PessoaEntidadeJpa registro where registro.email = :email")
    Optional<PessoaEntidadeJpa> buscarPorEmailMetadados(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(
            "update PessoaEntidadeJpa registro set registro.nome = :nome, registro.email = :email,"
                + " registro.matricula = :matricula, registro.perfil = :perfil, registro.senhaHash"
                + " = :senhaHash, registro.ativo = :ativo, registro.telefone = :telefone,"
                + " registro.endereco = :endereco, registro.documento = :documento,"
                + " registro.nascimento = :nascimento, registro.cep = :cep, registro.pai = :pai,"
                + " registro.mae = :mae, registro.estadoCivil = :estadoCivil, registro.sexo ="
                + " :sexo, registro.telefoneResponsavel = :telefoneResponsavel,"
                + " registro.emailResponsavel = :emailResponsavel, registro.avatar = :avatar where"
                + " registro.id = :id")
    void atualizarMetadados(
            @Param("id") Long id,
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("matricula") String matricula,
            @Param("perfil") String perfil,
            @Param("senhaHash") String senhaHash,
            @Param("ativo") boolean ativo,
            @Param("telefone") String telefone,
            @Param("endereco") String endereco,
            @Param("documento") String documento,
            @Param("nascimento") LocalDate nascimento,
            @Param("cep") String cep,
            @Param("pai") String pai,
            @Param("mae") String mae,
            @Param("estadoCivil") String estadoCivil,
            @Param("sexo") String sexo,
            @Param("telefoneResponsavel") String telefoneResponsavel,
            @Param("emailResponsavel") String emailResponsavel,
            @Param("avatar") String avatar);
}
