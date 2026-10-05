package br.com.atena.escolar.mensagem.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.mensagem.builder.MensagemBuilder;
import br.com.atena.escolar.mensagem.dao.IMensagemDAO;
import br.com.atena.escolar.mensagem.dominio.Mensagem;
import br.com.atena.escolar.mensagem.dto.MensagemRequisicaoDTO;
import br.com.atena.escolar.mensagem.dto.MensagemRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ManterMensagemActor {

    private final IMensagemDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterMensagemActor(IMensagemDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<MensagemRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(MensagemBuilder::resposta)
                .toList();
    }

    public MensagemRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return MensagemBuilder.resposta(registro);
    }

    @Transactional
    public MensagemRespostaDTO criar(MensagemRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro =
                new Mensagem(
                        null,
                        autorizacao.usuario().identificador(),
                        requisicao.destinatarioId,
                        requisicao.texto,
                        LocalDateTime.now());
        exigirPermissao(registro, true);
        return MensagemBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public MensagemRespostaDTO atualizar(Long identificador, MensagemRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        exigirPermissao(buscar(identificador), true);
        var registro = MensagemBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        return MensagemBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        autorizacao.exigirSecretaria();
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Mensagem buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Mensagem registro) {
        try {
            exigirPermissao(registro, false);
            return true;
        } catch (ErroAplicacao erro) {
            if (erro.categoria() == ErroAplicacao.CategoriaEnum.ACESSO_NEGADO) {
                return false;
            }
            throw erro;
        }
    }

    private void exigirPermissao(Mensagem registro, boolean escrita) {
        var usuario = autorizacao.usuario();
        if (usuario.secretaria()) {
            return;
        }
        boolean propria = usuario.identificador().equals(registro.remetenteId());
        if ((escrita && !propria)
                || (!escrita
                        && !propria
                        && !usuario.identificador().equals(registro.destinatarioId()))) {
            ErroAplicacao.negar();
        }
    }
}
