package br.com.atena.escolar.listagem.actor;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.listagem.dto.LinhaListaDTO;
import br.com.atena.escolar.listagem.dto.ListaRespostaDTO;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;
import br.com.atena.escolar.turma.dao.ITurmaDAO;

import org.springframework.stereotype.Component;

@Component
public class ConsultarListaActor {
    private final IPessoaDAO pessoas;
    private final ITurmaDAO turmas;

    public ConsultarListaActor(IPessoaDAO pessoas, ITurmaDAO turmas) {
        this.pessoas = pessoas;
        this.turmas = turmas;
    }

    public ListaRespostaDTO consultar(String tipo) {
        if (tipo.equals("LISTA_TURMAS"))
            return new ListaRespostaDTO(
                    tipo,
                    turmas.listar().stream()
                            .map(t -> new LinhaListaDTO(t.id(), t.nome(), null, t.ano()))
                            .toList());
        String perfil =
                switch (tipo) {
                    case "LISTA_ALUNOS" -> "ALUNO";
                    case "LISTA_PROFESSORES" -> "PROFESSOR";
                    default -> throw ErroAplicacao.entrada("Lista desconhecida");
                };
        return new ListaRespostaDTO(
                tipo,
                pessoas.listar().stream()
                        .filter(p -> p.perfil().equals(perfil))
                        .map(p -> new LinhaListaDTO(p.id(), p.nome(), p.matricula(), null))
                        .toList());
    }
}
