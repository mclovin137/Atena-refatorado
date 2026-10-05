package br.com.atena.relatorios;
import jakarta.persistence.*;
@Entity
public class Relatorio {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)public Long id;
 public Long alunoId;public Long ownerId;public Long professorId;public boolean releasedOnly;public String tipo;public String requestId;public Long executionId;public String nome; @Column(columnDefinition="text") public String snapshot;
}
