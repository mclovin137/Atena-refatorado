package br.com.atena.escolar.infra;
public interface AulaRepository extends org.springframework.data.jpa.repository.JpaRepository<Aula,Long> { java.util.List<Aula> findByDisciplinaId(Long disciplinaId); }
