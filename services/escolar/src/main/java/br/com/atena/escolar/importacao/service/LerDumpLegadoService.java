package br.com.atena.escolar.importacao.service;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.importacao.dominio.RegistroLegado;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/** Leitor limitado ao dump contratado. O resultado é dado, nunca SQL para JDBC. */
public final class LerDumpLegadoService {
    public static final Set<String> TABELAS =
            Set.of(
                    "arquivos",
                    "atividades",
                    "calendario",
                    "chat",
                    "curso",
                    "diario",
                    "diariocontroler",
                    "emprestimolivros",
                    "escolar",
                    "evento",
                    "gradecurricular",
                    "grades",
                    "horaio",
                    "livros",
                    "materia",
                    "matriculados",
                    "notas",
                    "participarevento",
                    "perguntas",
                    "presence",
                    "tarefas",
                    "tracking",
                    "turmas",
                    "user");
    private static final Map<String, Set<String>> COLUNAS =
            Map.ofEntries(
                    Map.entry(
                            "arquivos",
                            Set.of("id", "idTurma", "idProf", "nome", "data", "arquivo")),
                    Map.entry(
                            "atividades",
                            Set.of(
                                    "id",
                                    "idTurma",
                                    "IdProf",
                                    "idAluno",
                                    "matter",
                                    "nome",
                                    "data",
                                    "dataEntrega")),
                    Map.entry("calendario", Set.of("id", "conteudo", "data")),
                    Map.entry(
                            "chat",
                            Set.of(
                                    "id",
                                    "idSender",
                                    "idDestiny",
                                    "messagem",
                                    "vizualizada",
                                    "data")),
                    Map.entry("curso", Set.of("id", "nome")),
                    Map.entry(
                            "diario",
                            Set.of("id", "periodo01", "periodo02", "periodo03", "periodo04")),
                    Map.entry("diariocontroler", Set.of("id", "idClass", "bimestre", "estado")),
                    Map.entry(
                            "emprestimolivros",
                            Set.of(
                                    "id",
                                    "idUser",
                                    "idLivro",
                                    "nomeLivro",
                                    "dataEmprestimo",
                                    "dataValidade",
                                    "estado")),
                    Map.entry(
                            "escolar",
                            Set.of(
                                    "id",
                                    "nome",
                                    "address",
                                    "cnpj",
                                    "diretor",
                                    "telefone",
                                    "telefoneSuporte",
                                    "email",
                                    "timeAluno",
                                    "horarioAbertura",
                                    "horarioFechamento")),
                    Map.entry("evento", Set.of("id", "nome", "data", "local", "vagas", "duracao")),
                    Map.entry(
                            "gradecurricular",
                            Set.of("id", "idTurma", "grade", "idProf", "Ch", "materia", "estado")),
                    Map.entry("grades", Set.of("id", "nome", "materia", "Ch")),
                    Map.entry(
                            "horaio", Set.of("id", "idTurma", "idProf", "data", "dia", "periodo")),
                    Map.entry("livros", Set.of("id", "nome", "autor", "lancamento", "estoque")),
                    Map.entry("materia", Set.of("id", "nome")),
                    Map.entry("matriculados", Set.of("id", "idAluno", "idTurma", "estado")),
                    Map.entry(
                            "notas",
                            Set.of(
                                    "id",
                                    "idTurma",
                                    "idAluno",
                                    "n1",
                                    "n2",
                                    "n3",
                                    "n4",
                                    "r1",
                                    "r2",
                                    "r3",
                                    "r4",
                                    "media",
                                    "mediaParcial")),
                    Map.entry("participarevento", Set.of("id", "idUser", "idEvento")),
                    Map.entry(
                            "perguntas",
                            Set.of(
                                    "id",
                                    "materia",
                                    "conteudo",
                                    "dificuldade",
                                    "periodo",
                                    "pergunta",
                                    "resposta")),
                    Map.entry(
                            "presence",
                            Set.of(
                                    "id",
                                    "idTurma",
                                    "idProf",
                                    "idMateria",
                                    "idAluno",
                                    "periodo",
                                    "data",
                                    "faltas",
                                    "conteudo")),
                    Map.entry(
                            "tarefas",
                            Set.of(
                                    "id",
                                    "idAluno",
                                    "idTurma",
                                    "idMateria",
                                    "idProf",
                                    "nome",
                                    "valor",
                                    "valorObtido",
                                    "periodo",
                                    "estado")),
                    Map.entry(
                            "tracking", Set.of("id", "idUser", "ip", "phone", "date", "location")),
                    Map.entry(
                            "turmas",
                            Set.of(
                                    "id",
                                    "idClass",
                                    "ano",
                                    "curso",
                                    "periodo",
                                    "complemento",
                                    "grade",
                                    "estado",
                                    "block",
                                    "senha")),
                    Map.entry(
                            "user",
                            Set.of(
                                    "id",
                                    "cargo",
                                    "solitacao",
                                    "matricular",
                                    "matriculado",
                                    "nome",
                                    "email",
                                    "senha",
                                    "hast",
                                    "telefone",
                                    "address",
                                    "cpf",
                                    "dataNacimento",
                                    "estadoCivil",
                                    "sex",
                                    "imagem",
                                    "pai",
                                    "mae",
                                    "emailResponsavel",
                                    "telefoneResponsavel",
                                    "ativado")));
    private static final Set<String> PROLOGO =
            Set.of(
                    "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */",
                    "/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */",
                    "/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */",
                    "/*!40101 SET NAMES utf8mb4 */",
                    "/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */",
                    "/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */",
                    "/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */");
    private static final Pattern INSERT =
            Pattern.compile(
                    "INSERT\\s+INTO\\s+`([a-z]+)`\\s*\\(([^)]+)\\)\\s+VALUES\\s*(.*)",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern IDENTIFICADOR = Pattern.compile("`([A-Za-z][A-Za-z0-9_]*)`");
    private static final Pattern NUMERO = Pattern.compile("-?[0-9]+(?:\\.[0-9]+)?");
    private static final Pattern DDL =
            Pattern.compile(
                    "(?:CREATE\\s+TABLE|ALTER\\s+TABLE)\\s+`([a-z]+)`\\s+(.*)",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public List<RegistroLegado> executar(String fonte) {
        var resultado = new ArrayList<RegistroLegado>();
        Map<String, Integer> ordinais = new LinkedHashMap<>();
        for (String instrucao : instrucoes(fonte)) {
            var insert = INSERT.matcher(instrucao);
            if (insert.matches()) {
                String tabela = insert.group(1);
                exigirTabela(tabela);
                List<String> colunas = colunas(insert.group(2));
                if (!COLUNAS.get(tabela).containsAll(colunas)) {
                    throw invalido();
                }
                for (List<String> valores : linhas(insert.group(3))) {
                    if (valores.size() != colunas.size()) {
                        throw invalido();
                    }
                    Map<String, String> campos = new LinkedHashMap<>();
                    for (int indice = 0; indice < colunas.size(); indice++) {
                        campos.put(colunas.get(indice), valores.get(indice));
                    }
                    int ordinal = ordinais.merge(tabela, 1, Integer::sum);
                    resultado.add(new RegistroLegado(tabela, ordinal, campos));
                }
            } else {
                exigirDeclaracaoConhecida(instrucao);
            }
        }
        return List.copyOf(resultado);
    }

    private List<String> instrucoes(String fonte) {
        var resultado = new ArrayList<String>();
        var atual = new StringBuilder();
        boolean aspas = false;
        for (int indice = 0; indice < fonte.length(); indice++) {
            char caractere = fonte.charAt(indice);
            if (aspas && caractere == '\\') {
                if (++indice >= fonte.length()) {
                    throw invalido();
                }
                atual.append(caractere).append(fonte.charAt(indice));
                continue;
            }
            if (caractere == '\'') {
                aspas = !aspas;
            }
            if (!aspas
                    && caractere == '-'
                    && indice + 1 < fonte.length()
                    && fonte.charAt(indice + 1) == '-') {
                int fim = fonte.indexOf('\n', indice);
                indice = fim < 0 ? fonte.length() : fim;
                atual.append(' ');
                continue;
            }
            if (!aspas
                    && caractere == '/'
                    && indice + 1 < fonte.length()
                    && fonte.charAt(indice + 1) == '*') {
                int fim = fonte.indexOf("*/", indice + 2);
                if (fim < 0) {
                    throw invalido();
                }
                String comentario = fonte.substring(indice, fim + 2);
                if (comentario.startsWith("/*!")
                        && !PROLOGO.contains(comentario.replaceAll("\\s+", " "))) {
                    throw invalido();
                }
                indice = fim + 1;
                atual.append(' ');
                continue;
            }
            if (!aspas && caractere == ';') {
                adicionar(resultado, atual);
                continue;
            }
            atual.append(caractere);
        }
        if (aspas) {
            throw invalido();
        }
        adicionar(resultado, atual);
        return resultado;
    }

    private void adicionar(List<String> instrucoes, StringBuilder atual) {
        String instrucao = atual.toString().trim();
        if (!instrucao.isEmpty()) {
            instrucoes.add(instrucao);
        }
        atual.setLength(0);
    }

    private List<String> colunas(String texto) {
        var resultado = new ArrayList<String>();
        for (String coluna : texto.split(",", -1)) {
            var identificador = IDENTIFICADOR.matcher(coluna.trim());
            if (!identificador.matches() || resultado.contains(identificador.group(1))) {
                throw invalido();
            }
            resultado.add(identificador.group(1));
        }
        return resultado;
    }

    private List<List<String>> linhas(String texto) {
        var leitor = new Literais(texto);
        var resultado = new ArrayList<List<String>>();
        do {
            leitor.exigir('(');
            var valores = new ArrayList<String>();
            do {
                valores.add(leitor.valor());
            } while (leitor.consumir(','));
            leitor.exigir(')');
            resultado.add(valores);
        } while (leitor.consumir(','));
        if (!leitor.terminou()) {
            throw invalido();
        }
        return resultado;
    }

    private void exigirDeclaracaoConhecida(String instrucao) {
        if (instrucao.matches("(?is)SET\\s+(?:SQL_MODE|time_zone)\\s*=\\s*(?:'[^']*'|\"[^\"]*\")")
                || instrucao.matches("(?is)START\\s+TRANSACTION|COMMIT")) {
            return;
        }
        var ddl = DDL.matcher(instrucao);
        if (!ddl.matches()) {
            throw invalido();
        }
        exigirTabela(ddl.group(1));
        String corpo = ddl.group(2);
        if (instrucao.regionMatches(true, 0, "CREATE", 0, 6)) {
            exigirSchema(ddl.group(1), corpo);
            return;
        }
        if (ddl.group(1).equals("presence") || !COLUNAS.get(ddl.group(1)).contains("id")) {
            throw invalido();
        }
        if (corpo.matches("(?is)ADD\\s+PRIMARY\\s+KEY\\s*\\(\\s*`id`\\s*\\)")
                || corpo.matches(
                        "(?is)MODIFY\\s+`id`\\s+int\\(11\\)\\s+NOT\\s+NULL\\s+AUTO_INCREMENT(?:\\s*,\\s*AUTO_INCREMENT=\\d+)?")) {
            return;
        }
        throw invalido();
    }

    private void exigirSchema(String tabela, String corpo) {
        var estrutura =
                Pattern.compile("(?is)\\((.*)\\)\\s+ENGINE=InnoDB\\s+DEFAULT\\s+CHARSET=utf8mb4")
                        .matcher(corpo);
        if (!estrutura.matches()) {
            throw invalido();
        }
        var declaradas = new HashSet<String>();
        for (String definicao : estrutura.group(1).split(",", -1)) {
            var coluna =
                    Pattern.compile(
                                    "(?is)\\s*`([A-Za-z][A-Za-z0-9_]*)`\\s+"
                                            + "(?:int\\(11\\)|varchar\\([0-9]+\\)|date)\\s+"
                                            + "(?:NOT\\s+NULL|DEFAULT\\s+NULL)\\s*")
                            .matcher(definicao);
            if (!coluna.matches() || !declaradas.add(coluna.group(1))) {
                throw invalido();
            }
        }
        if (!declaradas.equals(COLUNAS.get(tabela))) {
            throw invalido();
        }
    }

    private void exigirTabela(String tabela) {
        if (!TABELAS.contains(tabela)) {
            throw invalido();
        }
    }

    private static ErroAplicacao invalido() {
        return ErroAplicacao.entrada("Formato do arquivo legado não suportado");
    }

    private static final class Literais {
        private final String texto;
        private int posicao;

        private Literais(String texto) {
            this.texto = texto;
        }

        private String valor() {
            espacos();
            if (posicao < texto.length() && texto.charAt(posicao) == '\'') {
                return string();
            }
            int inicio = posicao;
            while (posicao < texto.length()
                    && texto.charAt(posicao) != ','
                    && texto.charAt(posicao) != ')') {
                posicao++;
            }
            String valor = texto.substring(inicio, posicao).trim();
            if (valor.equalsIgnoreCase("NULL")) {
                return null;
            }
            if (!NUMERO.matcher(valor).matches()) {
                throw invalido();
            }
            return valor;
        }

        private String string() {
            posicao++;
            var valor = new StringBuilder();
            while (posicao < texto.length()) {
                char caractere = texto.charAt(posicao++);
                if (caractere == '\'') {
                    if (posicao < texto.length() && texto.charAt(posicao) == '\'') {
                        posicao++;
                        valor.append('\'');
                        continue;
                    }
                    return valor.toString();
                }
                if (caractere == '\\') {
                    if (posicao >= texto.length()) {
                        throw invalido();
                    }
                    caractere =
                            switch (texto.charAt(posicao++)) {
                                case '0' -> '\0';
                                case 'n' -> '\n';
                                case 'r' -> '\r';
                                case 't' -> '\t';
                                case 'Z' -> 26;
                                case '\\' -> '\\';
                                case '\'' -> '\'';
                                case '"' -> '"';
                                default -> throw invalido();
                            };
                }
                valor.append(caractere);
            }
            throw invalido();
        }

        private void exigir(char esperado) {
            if (!consumir(esperado)) {
                throw invalido();
            }
        }

        private boolean consumir(char esperado) {
            espacos();
            if (posicao < texto.length() && texto.charAt(posicao) == esperado) {
                posicao++;
                return true;
            }
            return false;
        }

        private boolean terminou() {
            espacos();
            return posicao == texto.length();
        }

        private void espacos() {
            while (posicao < texto.length() && Character.isWhitespace(texto.charAt(posicao))) {
                posicao++;
            }
        }
    }
}
