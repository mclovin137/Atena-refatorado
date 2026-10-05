package br.com.atena.escolar.notaavaliacao.dao;

import br.com.atena.escolar.notaavaliacao.dominio.NotaAvaliacao;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface INotaAvaliacaoDAO {

    List<NotaAvaliacao> listar();

    Optional<NotaAvaliacao> buscar(Long identificador);

    NotaAvaliacao salvar(NotaAvaliacao registro);

    void excluir(Long identificador);

    List<NotaAvaliacao> daAvaliacao(Long avaliacaoId);
}
