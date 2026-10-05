package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
@RestController
public class PessoaAvatarController {
 final PessoaRepository people;final Access access;final Path directory;
 public PessoaAvatarController(PessoaRepository p,Access a,@Value("${UPLOAD_DIR:/data/uploads}")String directory){people=p;access=a;this.directory=Path.of(directory);}
 Pessoa person(Long id,Authentication auth){Pessoa actor=access.person(auth);if(!access.secretary(auth)&&!actor.id.equals(id))access.deny();return people.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pessoa não encontrada"));}
 @PostMapping(value="/api/pessoas/{id}/avatar",consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public Pessoa upload(@PathVariable Long id,@RequestParam MultipartFile file,Authentication auth)throws IOException{
  Pessoa p=person(id,auth);if(file.isEmpty()||file.getSize()>5*1024*1024)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Imagem deve ter até5MB");
  java.awt.image.BufferedImage image;
  try(var input=javax.imageio.ImageIO.createImageInputStream(new ByteArrayInputStream(file.getBytes()))){var readers=javax.imageio.ImageIO.getImageReaders(input);if(!readers.hasNext())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Imagem inválida");var reader=readers.next();try{reader.setInput(input);if(!java.util.Set.of("png","jpeg","jpg").contains(reader.getFormatName().toLowerCase(Locale.ROOT))||reader.getWidth(0)>4096||reader.getHeight(0)>4096)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Imagem PNG/JPEG inválida ou dimensões excedidas");image=reader.read(0);}finally{reader.dispose();}}
  String original=Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase(Locale.ROOT);if(!original.endsWith(".png")&&!original.endsWith(".jpg")&&!original.endsWith(".jpeg"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Use PNG ou JPEG");
  Files.createDirectories(directory);String filename=UUID.randomUUID().toString();Path target=directory.resolve(filename);javax.imageio.ImageIO.write(image,"png",target.toFile());String old=p.avatar;p.avatar=filename;
  try{Pessoa saved=people.save(p);if(old!=null)Files.deleteIfExists(directory.resolve(old));return saved;}catch(RuntimeException e){Files.deleteIfExists(target);throw e;}
 }
 @GetMapping("/api/pessoas/{id}/avatar") public ResponseEntity<byte[]> get(@PathVariable Long id,Authentication auth)throws IOException{Pessoa p=person(id,auth);if(p.avatar==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Foto não cadastrada");return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).header(HttpHeaders.CACHE_CONTROL,"private, no-store").body(Files.readAllBytes(directory.resolve(p.avatar)));}
}
