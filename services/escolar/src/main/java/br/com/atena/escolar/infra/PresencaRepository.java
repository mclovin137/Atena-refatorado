package br.com.atena.escolar.infra;
public interface PresencaRepository extends org.springframework.data.jpa.repository.JpaRepository<Presenca,Long> { java.util.List<Presenca> findByAulaId(Long aulaId); }
