package br.com.atena.escolar.importacao.actor;

import br.com.atena.escolar.importacao.dominio.RegistroLegado;
import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Classifica a origem sem escrever o domínio ativo e remove segredos do staging. */
@Component
public class AnalisarRegistroLegadoActor {
    private static final Set<String> TRADUZIVEIS =
            Set.of("curso", "materia", "grades", "turmas", "gradecurricular", "livros", "evento");
    private static final Map<String, List<String>> OBRIGATORIOS =
            Map.of(
                    "curso",
                    List.of("nome"),
                    "materia",
                    List.of("nome"),
                    "turmas",
                    List.of("idClass", "ano", "curso", "periodo", "grade"),
                    "grades",
                    List.of("nome", "materia", "Ch"),
                    "gradecurricular",
                    List.of("idTurma", "materia", "Ch"));
    private static final Set<String> SEGREDOS = Set.of("senha", "hast");

    public RegistroImportacaoDTO executar(RegistroLegado origem) {
        var dados = new LinkedHashMap<>(origem.campos());
        SEGREDOS.forEach(dados::remove);
        String motivo = null;
        String estado = "PLANEJADO";
        if (!TRADUZIVEIS.contains(origem.tabela())) {
            estado = "PENDENTE";
            motivo = motivoPendencia(origem.tabela());
        }
        boolean incompleto =
                OBRIGATORIOS.getOrDefault(origem.tabela(), List.of()).stream()
                        .anyMatch(campo -> dados.get(campo) == null || dados.get(campo).isBlank());
        if (incompleto) {
            estado = "REJEITADO";
            motivo = "DADO_OBRIGATORIO_AUSENTE";
        }
        return new RegistroImportacaoDTO(
                origem.tabela(),
                origem.ordinal(),
                origem.campo("id"),
                estado,
                motivo,
                dados,
                Map.of());
    }

    private String motivoPendencia(String tabela) {
        return switch (tabela) {
            case "user" -> "PERFIL_NAO_MAPEADO";
            case "emprestimolivros", "participarevento" -> "PESSOA_NAO_VINCULADA";
            default -> "SEM_EQUIVALENCIA_COMPROVADA";
        };
    }
}
