package br.com.atena.escolar.importacao.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface IImportacaoFonteRepositorioJpa
        extends JpaRepository<ImportacaoFonteEntidadeJpa, UUID> {
    @Query("select f.id from ImportacaoFonteEntidadeJpa f where f.digest = :digest")
    Optional<UUID> identificarDigest(@Param("digest") String digest);
}
