package br.com.atena.escolar.aula.dao;

import br.com.atena.escolar.aula.dominio.Aula;

import java.util.List;
import java.util.Optional;

/** Porta de persistência: isola domínio e casos de uso do Spring Data/JPA. */
public interface IAulaDAO {

    List<Aula> listar();

    Optional<Aula> buscar(Long identificador);

    Aula salvar(Aula registro);

    void excluir(Long identificador);

    List<Aula> daDisciplina(Long disciplinaId);
}
