package br.com.atena.escolar.presenca.actor;

import br.com.atena.escolar.aula.dao.IAulaDAO;
import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.matricula.actor.ValidarVinculoEnsinoActor;
import br.com.atena.escolar.presenca.dominio.Presenca;
import br.com.atena.escolar.presenca.service.ValidarPresencaService;

import org.springframework.stereotype.Component;

@Component
public class AutorizacaoPresencaActor {
    private final IAulaDAO aulas;
    private final AutorizacaoEnsinoActor autorizacao;
    private final ValidarVinculoEnsinoActor vinculos;

    public AutorizacaoPresencaActor(
            IAulaDAO aulas,
            AutorizacaoEnsinoActor autorizacao,
            ValidarVinculoEnsinoActor vinculos) {
        this.aulas = aulas;
        this.autorizacao = autorizacao;
        this.vinculos = vinculos;
    }

    public void exigir(Presenca presenca, boolean escrita) {
        if (autorizacao.usuario().secretaria()) {
            return;
        }
        var aula =
                aulas.buscar(presenca.aulaId())
                        .orElseThrow(() -> ErroAplicacao.ausente("Aula não encontrada"));
        autorizacao.exigirAluno(presenca.alunoId(), aula.disciplinaId(), escrita);
    }

    public void validarVinculos(Presenca presenca) {
        var aula =
                aulas.buscar(presenca.aulaId())
                        .orElseThrow(() -> ErroAplicacao.entrada("Aula não encontrada"));
        vinculos.exigirMatricula(aula.disciplinaId(), presenca.alunoId());
        ValidarPresencaService.validar(presenca.faltas(), aula.horas());
    }
}
