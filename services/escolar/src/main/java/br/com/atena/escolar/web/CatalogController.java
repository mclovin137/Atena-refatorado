package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import java.util.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
@RestController
public class CatalogController {
 final CursoRepository cursos;final SerieRepository series;final MateriaRepository materias;final PessoaRepository people;
 public CatalogController(CursoRepository c,SerieRepository s,MateriaRepository m,PessoaRepository p){cursos=c;series=s;materias=m;people=p;}
 @GetMapping("/api/catalogos") @Cacheable("catalogos") public Map<String,Object> catalogs(){return Map.of("cursos",cursos.findAll(),"series",series.findAll(),"materias",materias.findAll());}
 @GetMapping("/api/directory") public List<Map<String,Object>> directory(){return people.findAll().stream().filter(p->p.ativo).map(p->Map.<String,Object>of("id",p.id,"nome",p.nome,"perfil",p.perfil)).toList();}
}
