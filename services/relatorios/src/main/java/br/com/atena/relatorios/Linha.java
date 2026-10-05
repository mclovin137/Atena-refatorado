package br.com.atena.relatorios;
import jakarta.persistence.*;
@Entity
public class Linha {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)public Long id;
 public Long relatorioId;public int numero;
 @Column(columnDefinition="text")public String conteudo;
}
