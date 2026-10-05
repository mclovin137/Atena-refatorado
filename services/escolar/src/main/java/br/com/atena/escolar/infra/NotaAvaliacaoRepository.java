package br.com.atena.escolar.infra;
public interface NotaAvaliacaoRepository extends org.springframework.data.jpa.repository.JpaRepository<NotaAvaliacao,Long> { java.util.List<NotaAvaliacao> findByAvaliacaoId(Long avaliacaoId); }
