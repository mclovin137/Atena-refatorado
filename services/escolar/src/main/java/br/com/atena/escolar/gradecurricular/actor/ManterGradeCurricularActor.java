package br.com.atena.escolar.gradecurricular.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.curso.dao.ICursoDAO;
import br.com.atena.escolar.gradecurricular.builder.GradeCurricularBuilder;
import br.com.atena.escolar.gradecurricular.dao.IGradeCurricularDAO;
import br.com.atena.escolar.gradecurricular.dominio.GradeCurricular;
import br.com.atena.escolar.gradecurricular.dto.GradeCurricularRequisicaoDTO;
import br.com.atena.escolar.gradecurricular.dto.GradeCurricularRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterGradeCurricularActor {
    private final IGradeCurricularDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;
    private final ICursoDAO referencias;

    public ManterGradeCurricularActor(
            IGradeCurricularDAO registros,
            AutorizacaoEnsinoActor autorizacao,
            ICursoDAO referencias) {
        this.registros = registros;
        this.autorizacao = autorizacao;
        this.referencias = referencias;
    }

    public List<GradeCurricularRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream().map(GradeCurricularBuilder::resposta).toList();
    }

    public GradeCurricularRespostaDTO consultar(Long identificador) {
        autorizacao.usuario();
        return GradeCurricularBuilder.resposta(buscar(identificador));
    }

    @Transactional
    public GradeCurricularRespostaDTO criar(GradeCurricularRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        if (requisicao.id != null) throw ErroAplicacao.entrada("Não informe id ao criar");
        return salvar(GradeCurricularBuilder.dominio(requisicao, null));
    }

    @Transactional
    public GradeCurricularRespostaDTO atualizar(
            Long identificador, GradeCurricularRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        buscar(identificador);
        if (requisicao.id != null && !requisicao.id.equals(identificador))
            throw ErroAplicacao.entrada("id difere do recurso");
        return salvar(GradeCurricularBuilder.dominio(requisicao, identificador));
    }

    @Transactional
    public void excluir(Long identificador) {
        autorizacao.exigirSecretaria();
        buscar(identificador);
        registros.excluir(identificador);
    }

    private GradeCurricularRespostaDTO salvar(GradeCurricular registro) {
        if (referencias.buscar(registro.cursoId()).isEmpty())
            throw ErroAplicacao.ausente("Referência não encontrada");
        return GradeCurricularBuilder.resposta(registros.salvar(registro));
    }

    private GradeCurricular buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }
}
