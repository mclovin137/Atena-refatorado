package br.com.atena.escolar.aula.dao.jpa;

import br.com.atena.escolar.aula.dao.IAulaDAO;
import br.com.atena.escolar.aula.dominio.Aula;
import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AulaDAO implements IAulaDAO {

    private final IAulaRepositorioJpa repositorio;

    public AulaDAO(IAulaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Aula> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Aula> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Aula salvar(Aula registro) {
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
    public List<Aula> daDisciplina(Long disciplinaId) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findByDisciplinaId(disciplinaId).stream()
                            .map(this::dominio)
                            .toList();
                });
    }

    private Aula dominio(AulaEntidadeJpa entidade) {
        return new Aula(
                entidade.id,
                entidade.disciplinaId,
                entidade.data,
                entidade.conteudo,
                entidade.planejamento,
                entidade.horas);
    }

    private AulaEntidadeJpa entidade(Aula registro) {
        AulaEntidadeJpa entidade = new AulaEntidadeJpa();
        entidade.id = registro.id();
        entidade.disciplinaId = registro.disciplinaId();
        entidade.data = registro.data();
        entidade.conteudo = registro.conteudo();
        entidade.planejamento = registro.planejamento();
        entidade.horas = registro.horas();
        return entidade;
    }
}
