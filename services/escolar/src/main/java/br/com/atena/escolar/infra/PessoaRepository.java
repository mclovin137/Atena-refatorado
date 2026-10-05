package br.com.atena.escolar.infra;
public interface PessoaRepository extends org.springframework.data.jpa.repository.JpaRepository<Pessoa,Long> { java.util.Optional<Pessoa> findByEmail(String email); }
