package br.com.atena.relatorios.relatorio.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface IRelatorioJpaRepository extends JpaRepository<RelatorioEntidade, Long> {
    Optional<RelatorioEntidade> findByOwnerIdAndRequestId(Long ownerId, String requestId);

    @Modifying
    @Transactional
    @Query("update Relatorio r set r.executionId=:execution where r.id=:id")
    void registrarExecucao(@Param("id") Long id, @Param("execution") Long execution);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("update Relatorio r set r.nome=:nome,r.snapshot=:snapshot where r.id=:id")
    void registrarSnapshot(
            @Param("id") Long id, @Param("nome") String nome, @Param("snapshot") String snapshot);
}
