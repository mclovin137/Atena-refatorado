package br.com.atena.escolar.aula.actor;

import br.com.atena.escolar.aula.builder.AulaBuilder;
import br.com.atena.escolar.aula.dao.IAulaDAO;
import br.com.atena.escolar.aula.dominio.Aula;
import br.com.atena.escolar.aula.dto.AulaRequisicaoDTO;
import br.com.atena.escolar.aula.dto.AulaRespostaDTO;
import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterAulaActor {

    private final IAulaDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterAulaActor(IAulaDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<AulaRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(AulaBuilder::resposta)
                .toList();
    }

    public AulaRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return AulaBuilder.resposta(registro);
    }

    @Transactional
    public AulaRespostaDTO criar(AulaRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = AulaBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        return AulaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public AulaRespostaDTO atualizar(Long identificador, AulaRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = AulaBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        return AulaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Aula buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Aula registro) {
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

    private void exigirPermissao(Aula registro, boolean escrita) {
        if (!autorizacao.usuario().secretaria()) {
            autorizacao.exigirDisciplina(registro.disciplinaId(), escrita);
        }
    }
}
