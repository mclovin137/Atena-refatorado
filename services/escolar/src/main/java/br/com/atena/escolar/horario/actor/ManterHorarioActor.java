package br.com.atena.escolar.horario.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.horario.builder.HorarioBuilder;
import br.com.atena.escolar.horario.dao.IHorarioDAO;
import br.com.atena.escolar.horario.dominio.Horario;
import br.com.atena.escolar.horario.dto.HorarioRequisicaoDTO;
import br.com.atena.escolar.horario.dto.HorarioRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterHorarioActor {

    private final IHorarioDAO registros;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterHorarioActor(IHorarioDAO registros, AutorizacaoEnsinoActor autorizacao) {
        this.registros = registros;
        this.autorizacao = autorizacao;
    }

    public List<HorarioRespostaDTO> listar() {
        autorizacao.usuario();
        return registros.listar().stream()
                .filter(this::podeLer)
                .map(HorarioBuilder::resposta)
                .toList();
    }

    public HorarioRespostaDTO consultar(Long identificador) {
        var registro = buscar(identificador);
        exigirPermissao(registro, false);
        return HorarioBuilder.resposta(registro);
    }

    @Transactional
    public HorarioRespostaDTO criar(HorarioRequisicaoDTO requisicao) {
        if (requisicao.id != null) {
            throw ErroAplicacao.entrada("Não informe id ao criar");
        }
        var registro = HorarioBuilder.dominio(requisicao, null);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return HorarioBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public HorarioRespostaDTO atualizar(Long identificador, HorarioRequisicaoDTO requisicao) {
        exigirPermissao(buscar(identificador), true);
        var registro = HorarioBuilder.dominio(requisicao, identificador);
        exigirPermissao(registro, true);
        validarVinculos(registro);
        return HorarioBuilder.resposta(registros.salvar(registro));
    }

    @Transactional
    public void excluir(Long identificador) {
        exigirPermissao(buscar(identificador), true);
        registros.excluir(identificador);
    }

    private Horario buscar(Long identificador) {
        return registros
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Registro não encontrado"));
    }

    private boolean podeLer(Horario registro) {
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

    private void exigirPermissao(Horario registro, boolean escrita) {
        if (!autorizacao.usuario().secretaria()) {
            autorizacao.exigirDisciplina(registro.disciplinaId(), escrita);
        }
    }

    private void validarVinculos(Horario registro) {}
}
