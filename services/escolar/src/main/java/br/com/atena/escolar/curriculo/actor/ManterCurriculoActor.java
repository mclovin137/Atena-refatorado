package br.com.atena.escolar.curriculo.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.curriculo.builder.CurriculoBuilder;
import br.com.atena.escolar.curriculo.dao.ICurriculoDAO;
import br.com.atena.escolar.curriculo.dominio.Curriculo;
import br.com.atena.escolar.curriculo.dto.CurriculoRequisicaoDTO;
import br.com.atena.escolar.curriculo.dto.CurriculoRespostaDTO;
import br.com.atena.escolar.gradecurricular.dao.IGradeCurricularDAO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterCurriculoActor {

    private final ICurriculoDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;
    private final IGradeCurricularDAO grades;

    public ManterCurriculoActor(
            ICurriculoDAO registros,
            AutorizacaoEnsinoActor autorizacao,
            IGradeCurricularDAO grades) {
        this.registros = registros;
        this.autorizacao = autorizacao;
        this.grades = grades;
    }

    public List<CurriculoRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(CurriculoBuilder::resposta)
                .toList();
    }

    public CurriculoRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return CurriculoBuilder.resposta(registro);
    }

    @Transactional
    public CurriculoRespostaDTO criar(CurriculoRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = CurriculoBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        return CurriculoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public CurriculoRespostaDTO atualizar(Long identificador, CurriculoRequisicaoDTO requisicao) {
        var atual = buscar(identificador);
        exigirPermissao(atual, true);
        var registro =
                new Curriculo(
                        identificador,
                        requisicao.nome,
                        requisicao.materiaId,
                        requisicao.cargaHoraria,
                        requisicao.temGradeInformada() ? requisicao.gradeId : atual.gradeId());
        exigirPermissao(registro, true);
        return CurriculoBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Curriculo buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Curriculo registro) {
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

    private void exigirPermissao(Curriculo registro, boolean escrita) {
        autorizacao.usuario();
        if (escrita) {
            autorizacao.exigirSecretaria();
            if (registro.gradeId() != null && grades.buscar(registro.gradeId()).isEmpty())
                throw ErroAplicacao.ausente("Grade não encontrada");
        }
    }
}
