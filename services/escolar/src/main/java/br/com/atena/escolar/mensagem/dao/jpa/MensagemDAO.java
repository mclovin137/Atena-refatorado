package br.com.atena.escolar.mensagem.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.mensagem.dao.IMensagemDAO;
import br.com.atena.escolar.mensagem.dominio.Mensagem;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MensagemDAO implements IMensagemDAO {

    private final IMensagemRepositorioJpa repositorio;

    public MensagemDAO(IMensagemRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Mensagem> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findAll(Sort.by("id")).stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Mensagem> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.findById(identificador).map(this::dominio);
                });
    }

    @Override
    public Mensagem salvar(Mensagem registro) {
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

    private Mensagem dominio(MensagemEntidadeJpa entidade) {
        return new Mensagem(
                entidade.id,
                entidade.remetenteId,
                entidade.destinatarioId,
                entidade.texto,
                entidade.enviadaEm);
    }

    private MensagemEntidadeJpa entidade(Mensagem registro) {
        MensagemEntidadeJpa entidade = new MensagemEntidadeJpa();
        entidade.id = registro.id();
        entidade.remetenteId = registro.remetenteId();
        entidade.destinatarioId = registro.destinatarioId();
        entidade.texto = registro.texto();
        entidade.enviadaEm = registro.enviadaEm();
        return entidade;
    }
}
