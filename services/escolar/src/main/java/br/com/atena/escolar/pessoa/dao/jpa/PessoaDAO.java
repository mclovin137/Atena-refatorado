package br.com.atena.escolar.pessoa.dao.jpa;

import br.com.atena.escolar.comum.dao.helper.ErroPersistenciaHelper;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;
import br.com.atena.escolar.pessoa.dominio.Pessoa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class PessoaDAO implements IPessoaDAO {

    private final IPessoaRepositorioJpa repositorio;

    public PessoaDAO(IPessoaRepositorioJpa repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Pessoa> listar() {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.listarMetadados().stream().map(this::dominio).toList();
                });
    }

    @Override
    public Optional<Pessoa> buscar(Long identificador) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.buscarMetadados(identificador).map(this::dominio);
                });
    }

    @Override
    @Transactional
    public Pessoa salvar(Pessoa registro) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    if (registro.id() == null) {
                        return dominio(repositorio.saveAndFlush(entidade(registro)));
                    }
                    repositorio.atualizarMetadados(
                            registro.id(),
                            registro.nome(),
                            registro.email(),
                            registro.matricula(),
                            registro.perfil(),
                            registro.senhaHash(),
                            registro.ativo(),
                            registro.telefone(),
                            registro.endereco(),
                            registro.documento(),
                            registro.nascimento(),
                            registro.cep(),
                            registro.pai(),
                            registro.mae(),
                            registro.estadoCivil(),
                            registro.sexo(),
                            registro.telefoneResponsavel(),
                            registro.emailResponsavel(),
                            registro.avatar());
                    if (registro.avatarConteudo() != null) {
                        repositorio.gravarConteudo(registro.id(), registro.avatarConteudo());
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

    @Override
    public Optional<Pessoa> buscarPorEmail(String email) {
        return ErroPersistenciaHelper.executar(
                () -> {
                    return repositorio.buscarPorEmailMetadados(email).map(this::dominio);
                });
    }

    private Pessoa dominio(PessoaEntidadeJpa entidade) {
        return new Pessoa(
                entidade.id,
                entidade.nome,
                entidade.email,
                entidade.matricula,
                entidade.perfil,
                entidade.senhaHash,
                entidade.ativo,
                entidade.telefone,
                entidade.endereco,
                entidade.documento,
                entidade.nascimento,
                entidade.cep,
                entidade.pai,
                entidade.mae,
                entidade.estadoCivil,
                entidade.sexo,
                entidade.telefoneResponsavel,
                entidade.emailResponsavel,
                entidade.avatar,
                entidade.avatarConteudo);
    }

    private PessoaEntidadeJpa entidade(Pessoa registro) {
        PessoaEntidadeJpa entidade = new PessoaEntidadeJpa();
        entidade.id = registro.id();
        entidade.nome = registro.nome();
        entidade.email = registro.email();
        entidade.matricula = registro.matricula();
        entidade.perfil = registro.perfil();
        entidade.senhaHash = registro.senhaHash();
        entidade.ativo = registro.ativo();
        entidade.telefone = registro.telefone();
        entidade.endereco = registro.endereco();
        entidade.documento = registro.documento();
        entidade.nascimento = registro.nascimento();
        entidade.cep = registro.cep();
        entidade.pai = registro.pai();
        entidade.mae = registro.mae();
        entidade.estadoCivil = registro.estadoCivil();
        entidade.sexo = registro.sexo();
        entidade.telefoneResponsavel = registro.telefoneResponsavel();
        entidade.emailResponsavel = registro.emailResponsavel();
        entidade.avatar = registro.avatar();
        entidade.avatarConteudo = registro.avatarConteudo();
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
