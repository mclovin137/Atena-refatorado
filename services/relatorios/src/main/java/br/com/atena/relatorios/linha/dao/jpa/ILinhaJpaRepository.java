package br.com.atena.relatorios.linha.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ILinhaJpaRepository extends JpaRepository<LinhaEntidade, Long> {
    List<LinhaEntidade> findByRelatorioIdOrderByNumero(Long relatorioId);

    Optional<LinhaEntidade> findByRelatorioIdAndNumero(Long relatorioId, int numero);
}
