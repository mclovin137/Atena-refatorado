package br.com.atena.escolar.pessoa.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.pessoa.builder.PessoaBuilder;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;
import br.com.atena.escolar.pessoa.dominio.Pessoa;
import br.com.atena.escolar.pessoa.dto.CadastroPessoaRequisicaoDTO;
import br.com.atena.escolar.pessoa.dto.PessoaRespostaDTO;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterPessoaActor {
    private final IPessoaDAO pessoas;
    private final AutorizacaoEnsinoActor autorizacao;
    private final PasswordEncoder senhas;

    public ManterPessoaActor(
            IPessoaDAO pessoas, AutorizacaoEnsinoActor autorizacao, PasswordEncoder senhas) {
        this.pessoas = pessoas;
        this.autorizacao = autorizacao;
        this.senhas = senhas;
    }

    public List<PessoaRespostaDTO> listar() {
        var usuario = autorizacao.usuario();
        return pessoas.listar().stream()
                .filter(
                        pessoa ->
                                usuario.secretaria() || pessoa.id().equals(usuario.identificador()))
                .map(PessoaBuilder::resposta)
                .toList();
    }

    public PessoaRespostaDTO consultar(Long identificador) {
        var pessoa = buscar(identificador);
        var usuario = autorizacao.usuario();
        if (!usuario.secretaria() && !pessoa.id().equals(usuario.identificador())) {
            ErroAplicacao.negar();
        }
        return PessoaBuilder.resposta(pessoa);
    }

    @Transactional
    public PessoaRespostaDTO criar(CadastroPessoaRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        if (requisicao.pessoa().id != null || requisicao.senha() == null) {
            throw ErroAplicacao.entrada("Senha obrigatória e id gerado pelo sistema");
        }
        var pessoa = PessoaBuilder.dominio(requisicao.pessoa(), null);
        pessoa = PessoaBuilder.dadosPrivados(pessoa, senhas.encode(requisicao.senha()), null, null);
        return PessoaBuilder.resposta(pessoas.salvar(pessoa));
    }

    @Transactional
    public PessoaRespostaDTO atualizar(Long identificador, CadastroPessoaRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        var anterior = buscar(identificador);
        var pessoa = PessoaBuilder.dominio(requisicao.pessoa(), identificador);
        if (identificador.equals(autorizacao.usuario().identificador())
                && (!pessoa.ativo() || !"SECRETARIA".equals(pessoa.perfil()))) {
            throw ErroAplicacao.conflito("Não desative ou remova seu próprio perfil de secretaria");
        }
        if (!anterior.perfil().equals(pessoa.perfil())) {
            throw ErroAplicacao.conflito("Perfil de uma pessoa não pode mudar após cadastro");
        }
        if (!anterior.matricula().equals(pessoa.matricula())) {
            throw ErroAplicacao.conflito("Matrícula é imutável");
        }
        String senhaHash =
                requisicao.senha() == null
                        ? anterior.senhaHash()
                        : senhas.encode(requisicao.senha());
        pessoa =
                PessoaBuilder.dadosPrivados(
                        pessoa, senhaHash, anterior.avatar(), anterior.avatarConteudo());
        return PessoaBuilder.resposta(pessoas.salvar(pessoa));
    }

    @Transactional
    public void excluir(Long identificador) {
        autorizacao.exigirSecretaria();
        if (identificador.equals(autorizacao.usuario().identificador())) {
            throw ErroAplicacao.conflito("Não exclua sua própria conta");
        }
        buscar(identificador);
        pessoas.excluir(identificador);
    }

    public Pessoa buscar(Long identificador) {
        return pessoas.buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Pessoa não encontrada"));
    }
}
