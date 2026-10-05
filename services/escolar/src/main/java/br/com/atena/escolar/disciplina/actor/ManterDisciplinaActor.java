package br.com.atena.escolar.disciplina.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.disciplina.builder.DisciplinaBuilder;
import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.disciplina.dominio.Disciplina;
import br.com.atena.escolar.disciplina.dto.DisciplinaRequisicaoDTO;
import br.com.atena.escolar.disciplina.dto.DisciplinaRespostaDTO;
import br.com.atena.escolar.matricula.actor.ValidarVinculoEnsinoActor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterDisciplinaActor {

    private final IDisciplinaDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;
    private final ValidarVinculoEnsinoActor vinculos;

    public ManterDisciplinaActor(
            IDisciplinaDAO registros,
            AutorizacaoEnsinoActor autorizacao,
            ValidarVinculoEnsinoActor vinculos) {
        this.registros = registros;
        this.autorizacao = autorizacao;
        this.vinculos = vinculos;
    }

    public List<DisciplinaRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(DisciplinaBuilder::resposta)
                .toList();
    }

    public DisciplinaRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return DisciplinaBuilder.resposta(registro);
    }

    @Transactional
    public DisciplinaRespostaDTO criar(DisciplinaRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = DisciplinaBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return DisciplinaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public DisciplinaRespostaDTO atualizar(Long identificador, DisciplinaRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = DisciplinaBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return DisciplinaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Disciplina buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Disciplina registro) {
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

    private void exigirPermissao(Disciplina registro, boolean escrita) {
        if (autorizacao.usuario().secretaria()) {
            return;
        }
        autorizacao.exigirDisciplina(registro.id(), escrita);
    }

    private void validarVinculos(Disciplina registro) {
        if (registro.professorId() != null) {
            vinculos.exigirPerfil(registro.professorId(), "PROFESSOR");
        }
    }
}
