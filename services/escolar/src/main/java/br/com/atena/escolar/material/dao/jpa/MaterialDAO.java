package br.com.atena.escolar.material.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.material.dao.IMaterialDAO;
import br.com.atena.escolar.material.dominio.Material;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class MaterialDAO implements IMaterialDAO {

    private final IMaterialRepositorioJpa repositorio;

    public MaterialDAO(IMaterialRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Material> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.listarMetadados().stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Material> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.buscarMetadados(identificador).map(this::dominio);
                });
    }

    @Override
    @Transactional
    public Material salvar(Material registro) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    if (registro.id() == null) {
                        return dominio(repositorio.saveAndFlush(entidade(registro)));
                    }
                    repositorio.atualizarMetadados(
                            registro.id(),
                            registro.disciplinaId(),
                            registro.nome(),
                            registro.tipo(),
                            registro.arquivo(),
                            registro.autorId());
                    if (registro.conteudo() != null) {
                        repositorio.gravarConteudo(registro.id(), registro.conteudo());
                    }
                    return registro;
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

    private Material dominio(MaterialEntidadeJpa entidade) {
        return new Material(
                entidade.id,
                entidade.disciplinaId,
                entidade.nome,
                entidade.tipo,
                entidade.arquivo,
                entidade.autorId,
                entidade.conteudo);
    }

    private MaterialEntidadeJpa entidade(Material registro) {
        MaterialEntidadeJpa entidade = new MaterialEntidadeJpa();
        entidade.id = registro.id();
        entidade.disciplinaId = registro.disciplinaId();
        entidade.nome = registro.nome();
        entidade.tipo = registro.tipo();
        entidade.arquivo = registro.arquivo();
        entidade.autorId = registro.autorId();
        entidade.conteudo = registro.conteudo();
        return entidade;
    }

    @Override
    public byte[] lerConteudo(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.lerConteudo(identificador);
                });
    }

    @Override
    public void gravarConteudo(Long identificador, byte[] conteudo) {
        ErroPersistenciaHelper.executar(
                () -> {
                    repositorio.gravarConteudo(identificador, conteudo);

                    return null;
                });
    }
}
