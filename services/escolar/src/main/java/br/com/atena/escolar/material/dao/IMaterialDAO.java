package br.com.atena.escolar.material.dao;

import br.com.atena.escolar.material.dominio.Material;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IMaterialDAO {

    List<Material> listar();

    Optional<Material> buscar(Long identificador);

    Material salvar(Material registro);

    void excluir(Long identificador);

    byte[] lerConteudo(Long identificador);

    void gravarConteudo(Long identificador, byte[] conteudo);
}
