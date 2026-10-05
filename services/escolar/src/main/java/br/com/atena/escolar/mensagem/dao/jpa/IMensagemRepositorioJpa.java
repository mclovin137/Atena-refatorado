package br.com.atena.escolar.mensagem.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMensagemRepositorioJpa extends JpaRepository<MensagemEntidadeJpa, Long> {}
