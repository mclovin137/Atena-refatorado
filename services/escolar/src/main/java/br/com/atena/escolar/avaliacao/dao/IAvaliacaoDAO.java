package br.com.atena.escolar.avaliacao.dao;

import br.com.atena.escolar.avaliacao.dominio.Avaliacao;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IAvaliacaoDAO {

    List<Avaliacao> listar();

    Optional<Avaliacao> buscar(Long identificador);

    Avaliacao salvar(Avaliacao registro);

    void excluir(Long identificador);
}
