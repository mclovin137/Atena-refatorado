package br.com.atena.escolar.importacao.actor;

import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.importado;
import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.pendente;

import br.com.atena.escolar.curriculo.dao.ICurriculoDAO;
import br.com.atena.escolar.curriculo.dominio.Curriculo;
import br.com.atena.escolar.gradecurricular.dao.IGradeCurricularDAO;
import br.com.atena.escolar.gradecurricular.dominio.GradeCurricular;
import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ImportarGradesLegadoActor {
    private final IGradeCurricularDAO grades;
    private final ICurriculoDAO curriculos;

    public ImportarGradesLegadoActor(IGradeCurricularDAO grades, ICurriculoDAO curriculos) {
        this.grades = grades;
        this.curriculos = curriculos;
    }

    public RegistroImportacaoDTO executar(
            RegistroImportacaoDTO registro, List<RegistroImportacaoDTO> fontes) {
        String nome = registro.dados().get("nome");
        var cursosReferidos =
                fontes.stream()
                        .filter(
                                fonte ->
                                        fonte.tabela().equals("turmas")
                                                && nome.equals(fonte.dados().get("grade")))
                        .map(fonte -> fonte.dados().get("curso"))
                        .distinct()
                        .toList();
        if (cursosReferidos.size() != 1) {
            return pendente(registro, "GRADE_CURSO_AMBIGUO");
        }
        var cursos =
                fontes.stream()
                        .filter(
                                fonte ->
                                        fonte.tabela().equals("curso")
                                                && cursosReferidos
                                                        .getFirst()
                                                        .equals(fonte.dados().get("nome"))
                                                && fonte.destinos().containsKey("cursoId"))
                        .map(fonte -> fonte.destinos().get("cursoId"))
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
        if (cursos.size() != 1 || materias.size() != 1) {
            return pendente(registro, "GRADE_REFERENCIA_NAO_RESOLVIDA");
        }
        Integer carga = cargaHoraria(registro);
        if (carga == null) {
            return pendente(registro, "CARGA_HORARIA_NAO_SUPORTADA");
        }
        var existentes =
                grades.listar().stream()
                        .filter(
                                grade ->
                                        grade.nome().equals(nome)
                                                && grade.cursoId().equals(cursos.getFirst()))
                        .toList();
        var componentes =
                curriculos.listar().stream()
                        .filter(
                                curriculo ->
                                        curriculo.nome().equals(nome)
                                                && curriculo
                                                        .materiaId()
                                                        .equals(materias.getFirst()))
                        .toList();
        if (!componentes.isEmpty()
                && (componentes.size() != 1
                        || existentes.isEmpty()
                        || !existentes.getFirst().id().equals(componentes.getFirst().gradeId())
                        || componentes.getFirst().cargaHoraria() != carga)) {
            return pendente(registro, "COMPONENTE_EXISTENTE_INCOMPATIVEL");
        }
        var grade =
                existentes.isEmpty()
                        ? grades.salvar(new GradeCurricular(null, nome, cursos.getFirst()))
                        : existentes.getFirst();
        var componente =
                componentes.isEmpty()
                        ? curriculos.salvar(
                                new Curriculo(null, nome, materias.getFirst(), carga, grade.id()))
                        : componentes.getFirst();
        return importado(registro, Map.of("gradeId", grade.id(), "curriculoId", componente.id()));
    }

    static Integer cargaHoraria(RegistroImportacaoDTO registro) {
        try {
            int carga = Integer.parseInt(registro.dados().get("Ch"));
            return carga >= 1 && carga <= 2000 ? carga : null;
        } catch (NumberFormatException erro) {
            return null;
        }
    }
}
