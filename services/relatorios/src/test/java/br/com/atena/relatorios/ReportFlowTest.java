package br.com.atena.relatorios;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;
import com.sun.net.httpserver.HttpServer;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
@SpringBootTest(properties={"spring.datasource.url=jdbc:postgresql://localhost:15432/relatorios"}) @AutoConfigureMockMvc
class ReportFlowTest {
 static final String token=UUID.randomUUID().toString()+UUID.randomUUID();static final AtomicBoolean fail=new AtomicBoolean(false);
 static final HttpServer school=startSchool();
 static final String snapshot="{\"nome\":\"José da Silva\",\"alunoId\":1,\"matricula\":\"ALU001\",\"documento\":\"123.456.789-00\",\"unidade\":{\"nome\":\"Escola Atena\",\"cnpj\":\"12.345.678/0001-90\",\"endereco\":\"Rua da escola\",\"telefone\":\"000000\"},\"linhas\":[{\"disciplina\":\"Matemática\",\"ano\":2026,\"notas\":[60,70,80,90],\"resultado\":{\"media\":75,\"situacao\":\"APROVADO\"}}]}";
 static HttpServer startSchool(){try{var s=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);s.createContext("/api/internal",e->{int code=token.equals(e.getRequestHeaders().getFirst("X-Service-Token"))?(fail.get()?503:200):401;byte[] body=snapshot.getBytes(java.nio.charset.StandardCharsets.UTF_8);e.getResponseHeaders().add("Content-Type","application/json");e.sendResponseHeaders(code,body.length);e.getResponseBody().write(body);e.close();});s.start();return s;}catch(Exception e){throw new RuntimeException(e);}}
 @DynamicPropertySource static void props(DynamicPropertyRegistry r){r.add("atena.service-token",()->token);r.add("atena.school-url",()->"http://127.0.0.1:"+school.getAddress().getPort());}
 @Autowired MockMvc mvc;@Autowired ObjectMapper mapper;@Autowired RelatorioRepository reports;@Autowired LinhaRepository lines;final List<Long> ids=new ArrayList<>();
 @AfterEach void clean(){for(Long id:ids){lines.deleteAll(lines.findByRelatorioIdOrderByNumero(id));reports.deleteById(id);}fail.set(false);}
 @AfterAll static void stop(){school.stop(0);}
 JsonNode create(String type,String request)throws Exception{return mapper.readTree(mvc.perform(post("/api/relatorios").header("X-Service-Token",token).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(Map.of("alunoId",1,"ownerId",1,"tipo",type,"requestId",request)))).andExpect(status().isAccepted()).andReturn().getResponse().getContentAsByteArray());}
 JsonNode getReport(long id)throws Exception{return mapper.readTree(mvc.perform(get("/api/relatorios/"+id).header("X-Service-Token",token)).andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray());}
 void await(long id,String expected)throws Exception{for(int i=0;i<150;i++){String state=getReport(id).get("status").asText();if(state.equals(expected))return;if(state.equals("FAILED")&&!expected.equals("FAILED"))fail("Relatório falhou");Thread.sleep(100);}fail("Relatório não atingiu "+expected);}
 @Test void consolidarExportarEAutorizar()throws Exception{
  mvc.perform(get("/actuator/health")).andExpect(status().isOk());mvc.perform(get("/actuator/info").header("X-Service-Token",token)).andExpect(status().isOk());mvc.perform(get("/actuator/metrics").header("X-Service-Token",token)).andExpect(status().isOk());mvc.perform(post("/api/relatorios").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isUnauthorized());mvc.perform(get("/api/relatorios/1")).andExpect(status().isUnauthorized());
  String request=UUID.randomUUID().toString();long id=create("HISTORICO",request).get("id").asLong();ids.add(id);await(id,"COMPLETED");assertEquals(1,lines.findByRelatorioIdOrderByNumero(id).size());assertNotNull(reports.findById(id).orElseThrow().snapshot);assertEquals(id,create("HISTORICO",request).get("id").asLong());
  byte[] pdf=mvc.perform(get("/api/relatorios/"+id+"/download").header("X-Service-Token",token)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_PDF)).andReturn().getResponse().getContentAsByteArray();try(var document=Loader.loadPDF(pdf)){String text=new PDFTextStripper().getText(document);assertTrue(text.contains("José da Silva"));assertTrue(text.contains("Matemática"));assertTrue(text.contains("Escola Atena"));assertTrue(text.contains("12.345.678/0001-90"));assertTrue(text.contains("123.456.789-00"));assertTrue(text.contains("ALU001"));}
  mvc.perform(get("/api/relatorios/"+id+"/download?format=json").header("X-Service-Token",token)).andExpect(status().isOk()).andExpect(jsonPath("$.linhas[0].resultado.media").value(75));mvc.perform(get("/api/relatorios/"+id+"/download?format=csv").header("X-Service-Token",token)).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("registro;conteudo")));mvc.perform(get("/api/relatorios/"+id+"/download?format=xml").header("X-Service-Token",token)).andExpect(status().isBadRequest());mvc.perform(get("/api/relatorios/999999").header("X-Service-Token",token)).andExpect(status().isNotFound());
  for(String type:List.of("BOLETIM","LISTA_ALUNOS","LISTA_PROFESSORES","LISTA_TURMAS")){long next=create(type,UUID.randomUUID().toString()).get("id").asLong();ids.add(next);await(next,"COMPLETED");mvc.perform(get("/api/relatorios/"+next+"/download").header("X-Service-Token",token)).andExpect(status().isOk());}
  mvc.perform(post("/api/relatorios").header("X-Service-Token",token).contentType(MediaType.APPLICATION_JSON).content("{\"ownerId\":1,\"tipo\":\"INVALIDO\",\"requestId\":\"x\"}")).andExpect(status().isBadRequest());
 }
 @Test void falhaERetomadaPersistidasSemDuplicar()throws Exception{fail.set(true);String request=UUID.randomUUID().toString();long id=create("HISTORICO",request).get("id").asLong();ids.add(id);await(id,"FAILED");mvc.perform(get("/api/relatorios/"+id+"/download").header("X-Service-Token",token)).andExpect(status().isConflict());fail.set(false);assertEquals(id,create("HISTORICO",request).get("id").asLong());await(id,"COMPLETED");assertEquals(1,lines.findByRelatorioIdOrderByNumero(id).size());}
}
