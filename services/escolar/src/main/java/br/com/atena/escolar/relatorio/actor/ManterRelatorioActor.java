package br.com.atena.escolar.relatorio.actor;

import br.com.atena.escolar.boletim.actor.ConsultarBoletimActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.relatorio.builder.RelatorioBuilder;
import br.com.atena.escolar.relatorio.dao.IRelatorioDAO;
import br.com.atena.escolar.relatorio.dominio.DocumentoRelatorio;
import br.com.atena.escolar.relatorio.dominio.SolicitacaoRelatorio;
import br.com.atena.escolar.relatorio.dto.RelatorioRespostaDTO;
import br.com.atena.escolar.relatorio.dto.SolicitarRelatorioDTO;
import br.com.atena.escolar.usuario.dominio.IUsuarioAtual;

import org.springframework.stereotype.Component;

@Component
public class ManterRelatorioActor {
    private final IRelatorioDAO relatorios;
    private final ConsultarBoletimActor boletins;
    private final IUsuarioAtual usuarioAtual;

    public ManterRelatorioActor(
            IRelatorioDAO relatorios, ConsultarBoletimActor boletins, IUsuarioAtual usuarioAtual) {
        this.relatorios = relatorios;
        this.boletins = boletins;
        this.usuarioAtual = usuarioAtual;
    }

    public RelatorioRespostaDTO iniciar(SolicitarRelatorioDTO solicitacao) {
        var usuario = usuarioAtual.obter();
        if (solicitacao.tipo().startsWith("LISTA_")) {
            if (!usuario.secretaria()) ErroAplicacao.negar();
        } else {
            if (solicitacao.alunoId() == null) throw ErroAplicacao.entrada("Informe o aluno");
            boletins.consultar(solicitacao.alunoId());
        }
        return RelatorioBuilder.resposta(
                relatorios.iniciar(
                        new SolicitacaoRelatorio(
                                solicitacao.alunoId(),
                                solicitacao.tipo(),
                                solicitacao.requestId(),
                                usuario.identificador(),
                                usuario.professor() ? usuario.identificador() : null,
                                usuario.aluno())));
    }

    public RelatorioRespostaDTO consultar(Long id) {
        var usuario = usuarioAtual.obter();
        var relatorio = relatorios.consultar(id);
        if (!usuario.secretaria() && !usuario.identificador().equals(relatorio.ownerId()))
            ErroAplicacao.negar();
        return RelatorioBuilder.resposta(relatorio);
    }

    public DocumentoRelatorio baixar(Long id, String formato) {
        consultar(id);
        return relatorios.baixar(id, formato);
    }
}
