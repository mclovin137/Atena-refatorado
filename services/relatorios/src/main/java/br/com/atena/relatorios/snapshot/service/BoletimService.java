package br.com.atena.relatorios.snapshot.service;

import java.util.List;

/** O boletim mostra somente o ano mais recente; histórico conserva todos. */
public class BoletimService {
    public int ultimoAno(List<Integer> anos) {
        return anos.stream().mapToInt(Integer::intValue).max().orElse(0);
    }
}
