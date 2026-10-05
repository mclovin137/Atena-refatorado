package br.com.atena.escolar.boletim.dominio;

import java.util.List;

/** Regra escolar independente de transporte e persistência. */
public record ResultadoEscolar(double media, double parcial, double frequencia, String situacao) {
    public static ResultadoEscolar calcular(
            List<Double> notas, List<Double> recuperacoes, int faltas, int cargaHoraria) {
        double soma = 0;
        int preenchidos = 0;
        for (int i = 0; i < 4; i++) {
            Double nota = notas.get(i);
            if (nota != null) {
                preenchidos++;
                Double rec = recuperacoes.get(i);
                soma += nota < 60 && rec != null && rec > nota ? Math.min(60, rec) : nota;
            }
        }
        double frequencia =
                cargaHoraria == 0
                        ? 100
                        : Math.max(0, 100.0 * (cargaHoraria - faltas) / cargaHoraria);
        String situacao =
                preenchidos < 4
                        ? "EM_ANDAMENTO"
                        : frequencia < 75
                                ? "REPROVADO_FREQUENCIA"
                                : soma / 4 >= 60 ? "APROVADO" : "REPROVADO_NOTA";
        return new ResultadoEscolar(
                soma / 4, preenchidos == 0 ? 0 : soma / preenchidos, frequencia, situacao);
    }
}
