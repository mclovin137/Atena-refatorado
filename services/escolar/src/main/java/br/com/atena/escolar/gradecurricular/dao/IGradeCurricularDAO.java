package br.com.atena.escolar.gradecurricular.dao;

import br.com.atena.escolar.gradecurricular.dominio.GradeCurricular;

import java.util.List;
import java.util.Optional;

public interface IGradeCurricularDAO {
    List<GradeCurricular> listar();

    Optional<GradeCurricular> buscar(Long identificador);

    GradeCurricular salvar(GradeCurricular registro);

    void excluir(Long identificador);
}
