package br.com.atena.escolar.presenca.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.presenca.dao.IPresencaDAO;
import br.com.atena.escolar.presenca.dominio.Presenca;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PresencaDAO implements IPresencaDAO {

    private final IPresencaRepositorioJpa repositorio;

    public PresencaDAO(IPresencaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Presenca> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Presenca> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Presenca salvar(Presenca registro) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return dominio(repositorio.saveAndFlush(entidade(registro)));
                });
    }

    @Override
    public void excluir(Long identificador) {
        ErroPersistenciaHelper.executar(
                () -> {
                    repositorio.deleteById(identificador);
                    repositorio.flush();

                    return null;
                });
    }

    @Override
    public List<Presenca> daAula(Long aulaId) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findByAulaId(aulaId).stream().map(this::dominio).toList();
                });
    }

    private Presenca dominio(PresencaEntidadeJpa entidade) {
        return new Presenca(entidade.id, entidade.aulaId, entidade.alunoId, entidade.faltas);
    }

    private PresencaEntidadeJpa entidade(Presenca registro) {
        PresencaEntidadeJpa entidade = new PresencaEntidadeJpa();
        entidade.id = registro.id();
        entidade.aulaId = registro.aulaId();
        entidade.alunoId = registro.alunoId();
        entidade.faltas = registro.faltas();
        return entidade;
    }
}
