package br.com.atena.escolar.faculdade.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.faculdade.builder.FaculdadeBuilder;
import br.com.atena.escolar.faculdade.dao.IFaculdadeDAO;
import br.com.atena.escolar.faculdade.dominio.Faculdade;
import br.com.atena.escolar.faculdade.dto.FaculdadeRequisicaoDTO;
import br.com.atena.escolar.faculdade.dto.FaculdadeRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterFaculdadeActor {
    private final IFaculdadeDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterFaculdadeActor(IFaculdadeDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<FaculdadeRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream().map(FaculdadeBuilder::resposta).toList();
    }

    public FaculdadeRespostaDTO consultar(Long identificador) {
        autorizacao.usuario();
        return FaculdadeBuilder.resposta(buscar(identificador));
    }

    @Transactional
    public FaculdadeRespostaDTO criar(FaculdadeRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        if (requisicao.id != null) throw ErroAplicacao.entrada("Não informe id ao criar");
        return salvar(FaculdadeBuilder.dominio(requisicao, null));
    }

    @Transactional
    public FaculdadeRespostaDTO atualizar(Long identificador, FaculdadeRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        buscar(identificador);
        if (requisicao.id != null && !requisicao.id.equals(identificador))
            throw ErroAplicacao.entrada("id difere do recurso");
        return salvar(FaculdadeBuilder.dominio(requisicao, identificador));
    }

    @Transactional
    public void excluir(Long identificador) {
        autorizacao.exigirSecretaria();
        buscar(identificador);
        registros.excluir(identificador);
    }

    private FaculdadeRespostaDTO salvar(Faculdade registro) {
        return FaculdadeBuilder.resposta(registros.salvar(registro));
    }

    private Faculdade buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }
}
