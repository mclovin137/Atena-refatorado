package br.com.atena.escolar.curso.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.curso.builder.CursoBuilder;
import br.com.atena.escolar.curso.dao.ICursoDAO;
import br.com.atena.escolar.curso.dominio.Curso;
import br.com.atena.escolar.curso.dto.CursoRequisicaoDTO;
import br.com.atena.escolar.curso.dto.CursoRespostaDTO;
import br.com.atena.escolar.faculdade.dao.IFaculdadeDAO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterCursoActor {

    private final ICursoDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;
    private final IFaculdadeDAO faculdades;

    public ManterCursoActor(
            ICursoDAO registros, AutorizacaoEnsinoActor autorizacao, IFaculdadeDAO faculdades) {
        this.registros = registros;
        this.autorizacao = autorizacao;
        this.faculdades = faculdades;
    }

    public List<CursoRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(CursoBuilder::resposta)
                .toList();
    }

    public CursoRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return CursoBuilder.resposta(registro);
    }

    @Transactional
    public CursoRespostaDTO criar(CursoRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = CursoBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        return CursoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public CursoRespostaDTO atualizar(Long identificador, CursoRequisicaoDTO requisicao) {
        var atual = buscar(identificador);
        exigirPermissao(atual, true);
        var registro =
                new Curso(
                        identificador,
                        requisicao.nome,
                        requisicao.temFaculdadeInformada()
                                ? requisicao.faculdadeId
                                : atual.faculdadeId());
        exigirPermissao(registro, true);
        return CursoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Curso buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Curso registro) {
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

    private void exigirPermissao(Curso registro, boolean escrita) {
        autorizacao.usuario();
        if (escrita) {
            autorizacao.exigirSecretaria();
            if (registro.faculdadeId() != null
                    && faculdades.buscar(registro.faculdadeId()).isEmpty())
                throw ErroAplicacao.ausente("Faculdade não encontrada");
        }
    }
}
