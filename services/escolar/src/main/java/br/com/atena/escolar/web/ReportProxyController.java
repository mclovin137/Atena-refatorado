package br.com.atena.escolar.web;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/relatorios")
public class ReportProxyController {
 final RestClient client;final Access access;final BoletimController boletins;
 public ReportProxyController(@Value("${atena.reports-url}")String url,@Value("${atena.service-token}")String token,Access a,BoletimController b,RestClient.Builder builder){var factory=new org.springframework.http.client.JdkClientHttpRequestFactory(java.net.http.HttpClient.newBuilder().connectTimeout(java.time.Duration.ofSeconds(5)).build());factory.setReadTimeout(java.time.Duration.ofSeconds(30));client=builder.clone().baseUrl(url).defaultHeader("X-Service-Token",token).requestFactory(factory).build();access=a;boletins=b;}
 public record Input(Long alunoId,@NotBlank @Pattern(regexp="HISTORICO|BOLETIM|LISTA_ALUNOS|LISTA_PROFESSORES|LISTA_TURMAS")String tipo,@NotBlank @Size(max=100)String requestId){}
 @PostMapping @ResponseStatus(HttpStatus.ACCEPTED) public Map<?,?> create(@Valid @RequestBody Input input,Authentication auth){var p=access.person(auth);if(input.tipo().startsWith("LISTA_"))access.admin(auth);else {if(input.alunoId()==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Informe o aluno");boletins.canReadStudent(input.alunoId(),auth);}Map<String,Object> request=new LinkedHashMap<>();request.put("alunoId",input.alunoId());request.put("tipo",input.tipo());request.put("requestId",input.requestId());request.put("ownerId",p.id);request.put("professorId",p.perfil.equals("PROFESSOR")?p.id:null);request.put("releasedOnly",p.perfil.equals("ALUNO"));return client.post().uri("/api/relatorios").body(request).retrieve().body(Map.class);}
 @GetMapping("/{id}") public Map<?,?> get(@PathVariable Long id,Authentication auth){Map<?,?> r=client.get().uri("/api/relatorios/{id}",id).retrieve().body(Map.class);if(!access.secretary(auth)&&!access.person(auth).id.toString().equals(r.get("ownerId").toString()))access.deny();return r;}
 @GetMapping("/{id}/download") public ResponseEntity<byte[]> download(@PathVariable Long id,@RequestParam(defaultValue="pdf")@Pattern(regexp="pdf|csv|json")String format,Authentication auth){get(id,auth);return client.get().uri("/api/relatorios/{id}/download?format={f}",id,format).retrieve().toEntity(byte[].class);}
}
