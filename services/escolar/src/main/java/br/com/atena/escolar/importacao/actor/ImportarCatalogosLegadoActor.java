package br.com.atena.escolar.importacao.actor;

import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.importado;
import static br.com.atena.escolar.importacao.helper.ResultadoImportacaoHelper.pendente;

import br.com.atena.escolar.curso.dao.ICursoDAO;
import br.com.atena.escolar.curso.dominio.Curso;
import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;
import br.com.atena.escolar.materia.dao.IMateriaDAO;
import br.com.atena.escolar.materia.dominio.Materia;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ImportarCatalogosLegadoActor {
    private final ICursoDAO cursos;
    private final IMateriaDAO materias;

    public ImportarCatalogosLegadoActor(ICursoDAO cursos, IMateriaDAO materias) {
        this.cursos = cursos;
        this.materias = materias;
    }

    public RegistroImportacaoDTO curso(RegistroImportacaoDTO registro) {
        String nome = registro.dados().get("nome");
        var encontrados =
                cursos.listar().stream().filter(curso -> curso.nome().equals(nome)).toList();
        if (encontrados.size() > 1) {
            return pendente(registro, "CURSO_AMBIGUO");
        }
        var curso =
                encontrados.isEmpty()
                        ? cursos.salvar(new Curso(null, nome))
                        : encontrados.getFirst();
        return importado(registro, Map.of("cursoId", curso.id()));
    }

    public RegistroImportacaoDTO materia(RegistroImportacaoDTO registro) {
        String nome = registro.dados().get("nome");
        var encontrados =
                materias.listar().stream().filter(materia -> materia.nome().equals(nome)).toList();
        if (encontrados.size() > 1) {
            return pendente(registro, "MATERIA_AMBIGUA");
        }
        var materia =
                encontrados.isEmpty()
                        ? materias.salvar(new Materia(null, nome))
                        : encontrados.getFirst();
        return importado(registro, Map.of("materiaId", materia.id()));
    }
}
