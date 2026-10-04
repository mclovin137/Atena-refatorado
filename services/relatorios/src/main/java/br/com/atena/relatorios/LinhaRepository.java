package br.com.atena.relatorios;
public interface LinhaRepository extends org.springframework.data.jpa.repository.JpaRepository<Linha,Long>{java.util.List<Linha> findByRelatorioIdOrderByNumero(Long relatorioId);java.util.Optional<Linha> findByRelatorioIdAndNumero(Long relatorioId,int numero);}
