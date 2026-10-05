package br.com.atena.escolar.sessao.actor;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.pessoa.builder.PessoaBuilder;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;
import br.com.atena.escolar.pessoa.dominio.Pessoa;
import br.com.atena.escolar.pessoa.dto.PessoaRespostaDTO;
import br.com.atena.escolar.sessao.dao.ISessaoDAO;
import br.com.atena.escolar.sessao.dto.LoginRequisicaoDTO;
import br.com.atena.escolar.sessao.dto.PerfilRequisicaoDTO;
import br.com.atena.escolar.sessao.dto.SenhaRequisicaoDTO;
import br.com.atena.escolar.sessao.dto.SessaoRespostaDTO;
import br.com.atena.escolar.usuario.dominio.IUsuarioAtual;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ManterSessaoActor {
    private final ISessaoDAO sessoes;
    private final IPessoaDAO pessoas;
    private final IUsuarioAtual usuarioAtual;
    private final PasswordEncoder senhas;

    public ManterSessaoActor(
            ISessaoDAO sessoes,
            IPessoaDAO pessoas,
            IUsuarioAtual usuarioAtual,
            PasswordEncoder senhas) {
        this.sessoes = sessoes;
        this.pessoas = pessoas;
        this.usuarioAtual = usuarioAtual;
        this.senhas = senhas;
    }

    public SessaoRespostaDTO consultar() {
        String email = sessoes.emailAutenticado();
        var pessoa = email == null ? null : pessoas.buscarPorEmail(email).orElseThrow();
        return new SessaoRespostaDTO(
                sessoes.tokenCsrf(), pessoa == null ? null : PessoaBuilder.resposta(pessoa));
    }

    public PessoaRespostaDTO entrar(LoginRequisicaoDTO requisicao) {
        String email = sessoes.autenticar(requisicao.email(), requisicao.senha());
        return PessoaBuilder.resposta(pessoas.buscarPorEmail(email).orElseThrow());
    }

    public void sair() {
        sessoes.encerrar();
    }

    private Pessoa propriaPessoa() {
        return pessoas.buscar(usuarioAtual.obter().identificador()).orElseThrow();
    }

    @Transactional
    public PessoaRespostaDTO atualizarPerfil(PerfilRequisicaoDTO requisicao) {
        var pessoa = propriaPessoa();
        var atualizada =
                new Pessoa(
                        pessoa.id(),
                        pessoa.nome(),
                        pessoa.email(),
                        pessoa.matricula(),
                        pessoa.perfil(),
                        pessoa.senhaHash(),
                        pessoa.ativo(),
                        requisicao.telefone(),
                        requisicao.endereco(),
                        pessoa.documento(),
                        requisicao.nascimento() == null
                                ? pessoa.nascimento()
                                : requisicao.nascimento(),
                        requisicao.cep() == null ? pessoa.cep() : requisicao.cep(),
                        requisicao.pai() == null ? pessoa.pai() : requisicao.pai(),
                        requisicao.mae() == null ? pessoa.mae() : requisicao.mae(),
                        requisicao.estadoCivil() == null
                                ? pessoa.estadoCivil()
                                : requisicao.estadoCivil(),
                        requisicao.sexo() == null ? pessoa.sexo() : requisicao.sexo(),
                        requisicao.telefoneResponsavel() == null
                                ? pessoa.telefoneResponsavel()
                                : requisicao.telefoneResponsavel(),
                        requisicao.emailResponsavel() == null
                                ? pessoa.emailResponsavel()
                                : requisicao.emailResponsavel(),
                        pessoa.avatar(),
                        null);
        return PessoaBuilder.resposta(pessoas.salvar(atualizada));
    }

    @Transactional
    public void atualizarSenha(SenhaRequisicaoDTO requisicao) {
        var pessoa = propriaPessoa();
        if (!senhas.matches(requisicao.atual(), pessoa.senhaHash())) {
            throw new ErroAplicacao(
                    ErroAplicacao.CategoriaEnum.NAO_AUTENTICADO, "Senha atual inválida");
        }
        var alterada =
                PessoaBuilder.dadosPrivados(
                        pessoa, senhas.encode(requisicao.nova()), pessoa.avatar(), null);
        pessoas.salvar(alterada);
        sessoes.encerrar();
    }
}
