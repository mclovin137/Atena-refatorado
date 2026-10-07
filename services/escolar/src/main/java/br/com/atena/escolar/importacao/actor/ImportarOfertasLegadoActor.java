package br.com.atena.escolar.importacao.actor;

import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.importado;
import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.pendente;

import br.com.atena.escolar.disciplina.dao.IDisciplinaDAO;
import br.com.atena.escolar.disciplina.dominio.Disciplina;
import br.com.atena.escolar.importacao.dao.IRegistroImportacaoDAO;
import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ImportarOfertasLegadoActor {
    private final IDisciplinaDAO disciplinas;
    private final IRegistroImportacaoDAO origens;
    private final IPessoaDAO pessoas;

    public ImportarOfertasLegadoActor(
            IDisciplinaDAO disciplinas, IRegistroImportacaoDAO origens, IPessoaDAO pessoas) {
        this.disciplinas = disciplinas;
        this.origens = origens;
        this.pessoas = pessoas;
    }

    public RegistroImportacaoDTO executar(
            UUID id, RegistroImportacaoDTO registro, List<RegistroImportacaoDTO> fontes) {
        var turmas =
                fontes.stream()
                        .filter(
                                fonte ->
                                        fonte.tabela().equals("turmas")
                                                && registro.dados()
                                                        .get("idTurma")
                                                        .equals(fonte.dados().get("idClass"))
                                                && fonte.destinos().containsKey("turmaId"))
                        .map(fonte -> fonte.destinos().get("turmaId"))
                        .distinct()
                        .toList();
        var materias =
                fontes.stream()
                        .filter(
                                fonte ->
                                        fonte.tabela().equals("materia")
                                                && registro.dados()
                                                        .get("materia")
                                                        .equals(fonte.dados().get("nome"))
                                                && fonte.destinos().containsKey("materiaId"))
                        .map(fonte -> fonte.destinos().get("materiaId"))
                        .distinct()
                        .toList();
        if (turmas.size() != 1 || materias.size() != 1) {
            return pendente(registro, "OFERTA_REFERENCIA_NAO_RESOLVIDA");
        }
        Long professorId = null;
        String referencia = registro.dados().get("idProf");
        if (referencia != null && !referencia.isBlank()) {
            professorId = origens.pessoaVinculada(id, referencia);
            if (professorId == null
                    || pessoas.buscar(professorId).isEmpty()
                    || !pessoas.buscar(professorId).orElseThrow().perfil().equals("PROFESSOR")) {
                return pendente(registro, "PROFESSOR_NAO_VINCULADO");
            }
        }
        Integer carga = ImportarGradesLegadoActor.cargaHoraria(registro);
        if (carga == null) {
            return pendente(registro, "CARGA_HORARIA_NAO_SUPORTADA");
        }
        var existentes =
                disciplinas.daTurma(turmas.getFirst()).stream()
                        .filter(oferta -> oferta.materiaId().equals(materias.getFirst()))
                        .toList();
        if (!existentes.isEmpty()
                && (existentes.size() != 1
                        || existentes.getFirst().cargaHoraria() != carga
                        || !java.util.Objects.equals(
                                existentes.getFirst().professorId(), professorId))) {
            return pendente(registro, "OFERTA_EXISTENTE_INCOMPATIVEL");
        }
        var oferta =
                existentes.isEmpty()
                        ? disciplinas.salvar(
                                new Disciplina(
                                        null,
                                        turmas.getFirst(),
                                        materias.getFirst(),
                                        professorId,
                                        carga))
                        : existentes.getFirst();
        return importado(registro, Map.of("disciplinaId", oferta.id()));
    }
}
