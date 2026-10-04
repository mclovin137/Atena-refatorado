package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import java.nio.file.*;
import java.util.*;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/materiais")
public class MaterialController {
 final MaterialRepository repository;final Access access;final Path directory;
 public MaterialController(MaterialRepository r,Access a,@Value("${UPLOAD_DIR:/data/uploads}") String directory){repository=r;access=a;this.directory=Path.of(directory);}
 @GetMapping public List<Material> list(Authentication a){return repository.findAll().stream().filter(m->access.readable(m,a)).toList();}
 @GetMapping("/{id}") public Material get(@PathVariable Long id,Authentication a){Material m=find(id);access.check(m,a,false);return m;}
 @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE) @ResponseStatus(HttpStatus.CREATED)
 public Material upload(@RequestParam Long disciplinaId,@RequestParam String nome,@RequestParam MultipartFile file,Authentication a) throws IOException {
  access.discipline(disciplinaId,a,true);if(nome.isBlank()||nome.length()>150||file.isEmpty()||file.getSize()>10*1024*1024)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Arquivo ou nome inválido");
  byte[] bytes=file.getBytes();String original=Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase(Locale.ROOT);
  String type;
  if(original.endsWith(".pdf") && bytes.length>=5 && new String(bytes,0,5,java.nio.charset.StandardCharsets.US_ASCII).equals("%PDF-")) type="application/pdf";
  else if(original.endsWith(".png") && bytes.length>=8 && bytes[0]==(byte)137 && bytes[1]==80 && bytes[2]==78 && bytes[3]==71)type="image/png";
  else if((original.endsWith(".jpg")||original.endsWith(".jpeg")) && bytes.length>=3 && bytes[0]==(byte)255 && bytes[1]==(byte)216 && bytes[2]==(byte)255)type="image/jpeg";
  else if(original.endsWith(".doc") && bytes.length>=8 && bytes[0]==(byte)0xD0 && bytes[1]==(byte)0xCF && bytes[2]==(byte)0x11 && bytes[3]==(byte)0xE0)type="application/msword";
  else if(original.endsWith(".zip") && bytes.length>=4 && bytes[0]==80 && bytes[1]==75 && (bytes[2]==3||bytes[2]==5||bytes[2]==7))type="application/zip";
  else if(original.endsWith(".rar") && bytes.length>=7 && bytes[0]==82 && bytes[1]==97 && bytes[2]==114 && bytes[3]==33 && bytes[4]==26 && bytes[5]==7)type="application/vnd.rar";
  else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tipos aceitos: PDF, DOC, ZIP, RAR, PNG e JPEG com assinatura correspondente");
  Files.createDirectories(directory);String filename=UUID.randomUUID().toString();Path target=directory.resolve(filename);Files.write(target,bytes,StandardOpenOption.CREATE_NEW);
  Material m=new Material();m.disciplinaId=disciplinaId;m.nome=nome;m.tipo=type;m.arquivo=filename;m.autorId=access.person(a).id;
  try{return repository.save(m);}catch(RuntimeException e){Files.deleteIfExists(target);throw e;}
 }
 @GetMapping("/{id}/download") public ResponseEntity<byte[]> download(@PathVariable Long id,Authentication a)throws IOException{Material m=find(id);access.check(m,a,false);String ext=switch(m.tipo){case "application/pdf"->".pdf";case "image/png"->".png";case "image/jpeg"->".jpg";case "application/msword"->".doc";case "application/zip"->".zip";default->".rar";};return ResponseEntity.ok().contentType(MediaType.parseMediaType(m.tipo)).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=material-"+id+ext).body(Files.readAllBytes(directory.resolve(m.arquivo)));}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id,Authentication a)throws IOException{Material m=find(id);access.check(m,a,true);repository.delete(m);Files.deleteIfExists(directory.resolve(m.arquivo));}
 Material find(Long id){return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Material não encontrado"));}
}
