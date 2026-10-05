package br.com.atena.escolar.notaavaliacao.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface INotaAvaliacaoRepositorioJpa
        extends JpaRepository<NotaAvaliacaoEntidadeJpa, Long> {
    List<NotaAvaliacaoEntidadeJpa> findByAvaliacaoId(Long avaliacaoId);
}
