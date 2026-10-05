package br.com.atena.escolar.boletim.actor;

import br.com.atena.escolar.boletim.builder.BoletimBuilder;
import br.com.atena.escolar.boletim.dao.IBoletimDAO;
import br.com.atena.escolar.boletim.dto.BoletimRespostaDTO;
import br.com.atena.escolar.boletim.service.PrepararBoletimService;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.usuario.dominio.IUsuarioAtual;

import org.springframework.stereotype.Component;

@Component
public class ConsultarBoletimActor {
    private final IBoletimDAO boletins;
    private final IUsuarioAtual usuarioAtual;

    public ConsultarBoletimActor(IBoletimDAO boletins, IUsuarioAtual usuarioAtual) {
        this.boletins = boletins;
        this.usuarioAtual = usuarioAtual;
    }

    public BoletimRespostaDTO consultar(Long alunoId) {
        var usuario = usuarioAtual.obter();
        var dados = boletins.carregar(alunoId);
        if (usuario.aluno() && !usuario.identificador().equals(alunoId)) {
            ErroAplicacao.negar();
        }
        if (usuario.professor()
                && dados.disciplinas().stream()
                        .noneMatch(
                                disciplina ->
                                        usuario.identificador().equals(disciplina.professorId()))) {
            ErroAplicacao.negar();
        }
        return BoletimBuilder.resposta(
                PrepararBoletimService.preparar(
                        dados,
                        usuario.professor() ? usuario.identificador() : null,
                        usuario.aluno()));
    }

    public BoletimRespostaDTO snapshot(Long alunoId, Long professorId, boolean somenteLiberadas) {
        return BoletimBuilder.resposta(
                PrepararBoletimService.preparar(
                        boletins.carregar(alunoId), professorId, somenteLiberadas));
    }
}
