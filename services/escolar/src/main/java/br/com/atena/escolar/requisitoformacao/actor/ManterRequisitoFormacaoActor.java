package br.com.atena.escolar.requisitoformacao.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.gradecurricular.dao.IGradeCurricularDAO;
import br.com.atena.escolar.requisitoformacao.builder.RequisitoFormacaoBuilder;
import br.com.atena.escolar.requisitoformacao.dao.IRequisitoFormacaoDAO;
import br.com.atena.escolar.requisitoformacao.dominio.RequisitoFormacao;
import br.com.atena.escolar.requisitoformacao.dto.RequisitoFormacaoRequisicaoDTO;
import br.com.atena.escolar.requisitoformacao.dto.RequisitoFormacaoRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterRequisitoFormacaoActor {
    private final IRequisitoFormacaoDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;
    private final IGradeCurricularDAO referencias;

    public ManterRequisitoFormacaoActor(
            IRequisitoFormacaoDAO registros,
            AutorizacaoEnsinoActor autorizacao,
            IGradeCurricularDAO referencias) {
        this.registros = registros;
        this.autorizacao = autorizacao;
        this.referencias = referencias;
    }

    public List<RequisitoFormacaoRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream().map(RequisitoFormacaoBuilder::resposta).toList();
    }

    public RequisitoFormacaoRespostaDTO consultar(Long identificador) {
        autorizacao.usuario();
        return RequisitoFormacaoBuilder.resposta(buscar(identificador));
    }

    @Transactional
    public RequisitoFormacaoRespostaDTO criar(RequisitoFormacaoRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        if (requisicao.id != null) throw ErroAplicacao.entrada("Não informe id ao criar");
        return salvar(RequisitoFormacaoBuilder.dominio(requisicao, null));
    }

    @Transactional
    public RequisitoFormacaoRespostaDTO atualizar(
            Long identificador, RequisitoFormacaoRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        buscar(identificador);
        if (requisicao.id != null && !requisicao.id.equals(identificador))
            throw ErroAplicacao.entrada("id difere do recurso");
        return salvar(RequisitoFormacaoBuilder.dominio(requisicao, identificador));
    }

    @Transactional
    public void excluir(Long identificador) {
        autorizacao.exigirSecretaria();
        buscar(identificador);
        registros.excluir(identificador);
    }

    private RequisitoFormacaoRespostaDTO salvar(RequisitoFormacao registro) {
        if (referencias.buscar(registro.gradeId()).isEmpty())
            throw ErroAplicacao.ausente("Referência não encontrada");
        return RequisitoFormacaoBuilder.resposta(registros.salvar(registro));
    }

    private RequisitoFormacao buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }
}
