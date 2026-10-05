package br.com.atena.escolar.boletim.dao;

import br.com.atena.escolar.boletim.dominio.DadosBoletim;

/** Fronteira coesa de consulta consistente entre recursos escolares. */
public interface IBoletimDAO {
    DadosBoletim carregar(Long alunoId);
}
