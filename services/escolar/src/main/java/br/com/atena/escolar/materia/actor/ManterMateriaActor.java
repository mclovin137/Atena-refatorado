package br.com.atena.escolar.materia.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.materia.builder.MateriaBuilder;
import br.com.atena.escolar.materia.dao.IMateriaDAO;
import br.com.atena.escolar.materia.dominio.Materia;
import br.com.atena.escolar.materia.dto.MateriaRequisicaoDTO;
import br.com.atena.escolar.materia.dto.MateriaRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterMateriaActor {

    private final IMateriaDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterMateriaActor(IMateriaDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<MateriaRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(MateriaBuilder::resposta)
                .toList();
    }

    public MateriaRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return MateriaBuilder.resposta(registro);
    }

    @Transactional
    public MateriaRespostaDTO criar(MateriaRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = MateriaBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        return MateriaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public MateriaRespostaDTO atualizar(Long identificador, MateriaRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = MateriaBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        return MateriaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Materia buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Materia registro) {
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

    private void exigirPermissao(Materia registro, boolean escrita) {
        autorizacao.usuario();
        if (escrita) {
            autorizacao.exigirSecretaria();
        }
    }
}
