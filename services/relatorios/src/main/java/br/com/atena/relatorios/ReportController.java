package br.com.atena.relatorios;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
@RestController @RequestMapping("/api/relatorios")
public class ReportController {
 final RelatorioRepository reports;final LinhaRepository lines;final JobLauncher launcher;final Job job;final JobExplorer explorer;final ObjectMapper mapper;
 public ReportController(RelatorioRepository r,LinhaRepository l,JobLauncher launcher,@Qualifier("consolidarHistorico")Job job,JobExplorer e,ObjectMapper m){reports=r;lines=l;this.launcher=launcher;this.job=job;explorer=e;mapper=m;}
 public record Input(Long alunoId,@NotNull Long ownerId,Long professorId,boolean releasedOnly,@NotBlank @Pattern(regexp="HISTORICO|BOLETIM|LISTA_ALUNOS|LISTA_PROFESSORES|LISTA_TURMAS")String tipo,@NotBlank @Size(max=100)String requestId){}
 @PostMapping @ResponseStatus(HttpStatus.ACCEPTED) public Map<String,Object> launch(@Valid @RequestBody Input input)throws Exception{
  if(!input.tipo().startsWith("LISTA_")&&input.alunoId()==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Informe o aluno");
  Optional<Relatorio> existing=reports.findByOwnerIdAndRequestId(input.ownerId(),input.requestId());Relatorio r=existing.orElseGet(Relatorio::new);
  if(existing.isPresent()&&r.executionId!=null){JobExecution e=explorer.getJobExecution(r.executionId);if(e!=null&&e.getStatus()!=BatchStatus.FAILED)return view(r);}
  if(existing.isEmpty()){r.alunoId=input.alunoId();r.ownerId=input.ownerId();r.professorId=input.professorId();r.releasedOnly=input.releasedOnly();r.tipo=input.tipo();r.requestId=input.requestId();reports.save(r);}
  JobExecution e=launcher.run(job,new JobParametersBuilder().addLong("reportId",r.id).toJobParameters());r.executionId=e.getId();reports.execution(r.id,r.executionId);return view(r);
 }
 @GetMapping("/{id}") public Map<String,Object> get(@PathVariable Long id){return view(find(id));}
 Map<String,Object> view(Relatorio r){Map<String,Object> out=new LinkedHashMap<>();out.put("id",r.id);out.put("ownerId",r.ownerId);out.put("alunoId",r.alunoId);out.put("tipo",r.tipo);JobExecution e=r.executionId==null?null:explorer.getJobExecution(r.executionId);out.put("status",e==null?"STARTING":e.getStatus().name());out.put("executionId",r.executionId);return out;}
 @GetMapping("/{id}/download") public ResponseEntity<byte[]> download(@PathVariable Long id,@RequestParam(defaultValue="pdf")String format)throws Exception{Relatorio r=find(id);JobExecution e=r.executionId==null?null:explorer.getJobExecution(r.executionId);if(e==null||e.getStatus()!=BatchStatus.COMPLETED)throw new ResponseStatusException(HttpStatus.CONFLICT,"Relatório ainda não concluído");List<Map<String,Object>> rows=new ArrayList<>();for(Linha l:lines.findByRelatorioIdOrderByNumero(id))rows.add(mapper.readValue(l.conteudo,new com.fasterxml.jackson.core.type.TypeReference<>(){}));byte[] data;MediaType type;
  if(format.equals("json")){data=mapper.writeValueAsBytes(Map.of("nome",r.nome,"tipo",r.tipo,"linhas",rows));type=MediaType.APPLICATION_JSON;}
  else if(format.equals("csv")){StringBuilder csv=new StringBuilder("registro;conteudo\n");for(int n=0;n<rows.size();n++)csv.append(n+1).append(';').append(csv(mapper.writeValueAsString(rows.get(n)))).append('\n');data=csv.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);type=MediaType.parseMediaType("text/csv;charset=UTF-8");}
  else if(format.equals("pdf")){data=pdf(r,rows);type=MediaType.APPLICATION_PDF;}
  else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Formato inválido");return ResponseEntity.ok().contentType(type).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=atena-"+id+"."+format).body(data);
 }
 static String csv(String s){if(!s.isEmpty()&&"=+-@\t\r".indexOf(s.charAt(0))>=0)s="'"+s;return "\""+s.replace("\"","\"\"")+"\"";}
 @SuppressWarnings("unchecked") static String describe(Map<String,Object> row){
  if(row.containsKey("disciplina")){Map<String,Object> result=(Map<String,Object>)row.get("resultado");return row.get("disciplina")+" | Turma: "+row.getOrDefault("turma","")+" | Ano: "+row.get("ano")+" | Bimestres: "+row.get("notas")+" | Recuperação: "+row.getOrDefault("recuperacoes","")+" | Média anual: "+result.get("media")+" | Média parcial: "+result.getOrDefault("parcial","")+" | Frequência: "+result.getOrDefault("frequencia","")+"% | Situação: "+result.get("situacao").toString().replace('_',' ');}
  return "Nome: "+row.getOrDefault("nome","")+" | Matrícula: "+row.getOrDefault("matricula","")+" | Ano: "+row.getOrDefault("ano","");
 }
 byte[] pdf(Relatorio r,List<Map<String,Object>> rows)throws IOException{try(PDDocument doc=new PDDocument()){List<String> texts=new ArrayList<>();texts.add("ATENA - "+r.tipo);texts.add("Aluno / lista: "+r.nome);
 if(r.snapshot!=null){Map<String,Object> snapshot=mapper.readValue(r.snapshot,new com.fasterxml.jackson.core.type.TypeReference<>(){});Object school=snapshot.get("unidade");if(school instanceof Map<?,?> unit && unit.get("nome")!=null){texts.add("Unidade escolar: "+unit.get("nome"));if(unit.get("cnpj")!=null&&!unit.get("cnpj").toString().isBlank())texts.add("CNPJ: "+unit.get("cnpj"));texts.add("Endereço: "+unit.get("endereco")+" | Telefone: "+unit.get("telefone"));}if(snapshot.get("matricula")!=null)texts.add("Matrícula: "+snapshot.get("matricula"));if(snapshot.get("documento")!=null&&!snapshot.get("documento").toString().isBlank())texts.add("CPF / documento: "+snapshot.get("documento"));}
 texts.add("Gerado pela execução "+r.executionId);for(Map<String,Object> row:rows){String text=describe(row);for(int start=0;start<text.length();start+=85)texts.add(text.substring(start,Math.min(text.length(),start+85)));texts.add("");}for(int start=0;start<texts.size();start+=48){PDPage page=new PDPage();doc.addPage(page);try(PDPageContentStream content=new PDPageContentStream(doc,page)){content.beginText();content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA),10);content.setLeading(14);content.newLineAtOffset(40,750);for(String line:texts.subList(start,Math.min(texts.size(),start+48))){String safe=line.replaceAll("[^\\x20-\\x7E\\xA0-\\xFF]","?");content.showText(safe);content.newLine();}content.endText();}}ByteArrayOutputStream out=new ByteArrayOutputStream();doc.save(out);return out.toByteArray();}}
 Relatorio find(Long id){return reports.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Relatório não encontrado"));}
}
