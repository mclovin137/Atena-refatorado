package br.com.atena.escolar.catalogo.actor;

import br.com.atena.escolar.catalogo.dto.CatalogoRespostaDTO;
import br.com.atena.escolar.catalogo.dto.PessoaDiretorioRespostaDTO;
import br.com.atena.escolar.curso.builder.CursoBuilder;
import br.com.atena.escolar.curso.dao.ICursoDAO;
import br.com.atena.escolar.materia.builder.MateriaBuilder;
import br.com.atena.escolar.materia.dao.IMateriaDAO;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;
import br.com.atena.escolar.serie.builder.SerieBuilder;
import br.com.atena.escolar.serie.dao.ISerieDAO;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarCatalogoActor {
    private final ICursoDAO cursos;
    private final ISerieDAO series;
    private final IMateriaDAO materias;
    private final IPessoaDAO pessoas;

    public ConsultarCatalogoActor(
            ICursoDAO cursos, ISerieDAO series, IMateriaDAO materias, IPessoaDAO pessoas) {
        this.cursos = cursos;
        this.series = series;
        this.materias = materias;
        this.pessoas = pessoas;
    }

    @Cacheable("catalogos")
    public CatalogoRespostaDTO catalogos() {
        return new CatalogoRespostaDTO(
                cursos.listar().stream().map(CursoBuilder::resposta).toList(),
                series.listar().stream().map(SerieBuilder::resposta).toList(),
                materias.listar().stream().map(MateriaBuilder::resposta).toList());
    }

    public List<PessoaDiretorioRespostaDTO> diretorio() {
        return pessoas.listar().stream()
                .filter(pessoa -> pessoa.ativo())
                .map(
                        pessoa ->
                                new PessoaDiretorioRespostaDTO(
                                        pessoa.id(), pessoa.nome(), pessoa.perfil()))
                .toList();
    }
}
