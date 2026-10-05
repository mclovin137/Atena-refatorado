package br.com.atena.escolar.matricula.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.matricula.builder.MatriculaBuilder;
import br.com.atena.escolar.matricula.dao.IMatriculaDAO;
import br.com.atena.escolar.matricula.dominio.Matricula;
import br.com.atena.escolar.matricula.dto.MatriculaRequisicaoDTO;
import br.com.atena.escolar.matricula.dto.MatriculaRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterMatriculaActor {

    private final IMatriculaDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;
    private final ValidarVinculoEnsinoActor vinculos;

    public ManterMatriculaActor(
            IMatriculaDAO registros,
            AutorizacaoEnsinoActor autorizacao,
            ValidarVinculoEnsinoActor vinculos) {
        this.registros = registros;
        this.autorizacao = autorizacao;
        this.vinculos = vinculos;
    }

    public List<MatriculaRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(MatriculaBuilder::resposta)
                .toList();
    }

    public MatriculaRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return MatriculaBuilder.resposta(registro);
    }

    @Transactional
    public MatriculaRespostaDTO criar(MatriculaRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = MatriculaBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return MatriculaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public MatriculaRespostaDTO atualizar(Long identificador, MatriculaRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = MatriculaBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return MatriculaBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Matricula buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Matricula registro) {
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

    private void exigirPermissao(Matricula registro, boolean escrita) {
        if (!autorizacao.usuario().secretaria()) {
            if (escrita
                    || (autorizacao.usuario().aluno()
                            && !autorizacao.usuario().identificador().equals(registro.alunoId()))) {
                ErroAplicacao.negar();
            }
            autorizacao.exigirTurma(registro.turmaId(), escrita);
        }
    }

    private void validarVinculos(Matricula registro) {
        vinculos.exigirPerfil(registro.alunoId(), "ALUNO");
    }
}
