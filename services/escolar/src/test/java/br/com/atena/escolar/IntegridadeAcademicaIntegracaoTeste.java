package br.com.atena.escolar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

@SpringBootTest(
        properties = {
            "spring.datasource.url=jdbc:postgresql://localhost:${ATENA_SCHOOL_TEST_PORT:15432}/escolar"
        })
class IntegridadeAcademicaIntegracaoTeste {
    private static final String token = UUID.randomUUID().toString() + UUID.randomUUID();
    @Autowired private JdbcTemplate jdbc;
    @Autowired private DataSource dados;

    @DynamicPropertySource
    static void propriedades(DynamicPropertyRegistry propriedades) {
        propriedades.add("ADMIN_EMAIL", () -> CredenciaisIntegracao.EMAIL);
        propriedades.add("ADMIN_PASSWORD", () -> CredenciaisIntegracao.SENHA);
        propriedades.add("atena.service-token", () -> token);
    }

    @Test
    void cadastrosConcorrentesConservamAssociacaoESubtipos() throws Exception {
        var fixture = criarFixture();
        try {
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM aluno WHERE pessoa_id=?",
                                    Integer.class,
                                    fixture.aluno()))
                    .isEqualTo(1);
            assertThat(
                            jdbc.queryForObject(
                                    "SELECT count(*) FROM professor WHERE pessoa_id=?",
                                    Integer.class,
                                    fixture.professor()))
                    .isEqualTo(1);
            var prontos = new CountDownLatch(2);
            var iniciar = new CountDownLatch(1);
            try (var executor = Executors.newFixedThreadPool(2)) {
                var matricula =
                        executor.submit(
                                () ->
                                        inserirConcorrente(
                                                prontos,
                                                iniciar,
                                                "INSERT INTO"
                                                    + " matricula(turma_id,aluno_id,dependencia)"
                                                    + " VALUES (?,?,false) RETURNING id",
                                                fixture.turma(),
                                                fixture.aluno()));
                var disciplina =
                        executor.submit(
                                () ->
                                        inserirConcorrente(
                                                prontos,
                                                iniciar,
                                                "INSERT INTO"
                                                    + " disciplina(turma_id,materia_id,carga_horaria,professor_id)"
                                                    + " VALUES (?,?,40,?) RETURNING id",
                                                fixture.turma(),
                                                fixture.materia(),
                                                fixture.professor()));
                assertThat(prontos.await(5, TimeUnit.SECONDS)).isTrue();
                iniciar.countDown();
                long matriculaId = matricula.get(10, TimeUnit.SECONDS);
                long disciplinaId = disciplina.get(10, TimeUnit.SECONDS);
                assertThat(
                                jdbc.queryForObject(
                                        """
SELECT count(*) FROM matricula_disciplina WHERE matricula_id=? AND disciplina_id=?
""",
                                        Integer.class,
                                        matriculaId,
                                        disciplinaId))
                        .isEqualTo(1);
                assertThatThrownBy(
                                () ->
                                        jdbc.update(
                                                "UPDATE pessoa SET perfil='SECRETARIA' WHERE id=?",
                                                fixture.aluno()))
                        .isInstanceOf(
                                org.springframework.dao.DataIntegrityViolationException.class);
                assertThatThrownBy(
                                () ->
                                        jdbc.update(
                                                "UPDATE pessoa SET perfil='ALUNO' WHERE id=?",
                                                fixture.professor()))
                        .isInstanceOf(
                                org.springframework.dao.DataIntegrityViolationException.class);
                assertThatThrownBy(
                                () ->
                                        jdbc.update(
                                                """
INSERT INTO matricula(turma_id,aluno_id,dependencia) VALUES (?,?,false)
""",
                                                fixture.outraTurma(),
                                                fixture.professor()))
                        .isInstanceOf(
                                org.springframework.dao.DataIntegrityViolationException.class);
            }
        } finally {
            excluirFixture(fixture);
        }
    }

    @Test
    void associacaoETrocaDeTurmaConcorrentesNuncaConfirmamContextosDivergentes() throws Exception {
        for (boolean associarPrimeiro : new boolean[] {true, false}) {
            var fixture = criarFixture();
            try {
                long matriculaId =
                        jdbc.queryForObject(
                                """
INSERT INTO matricula(turma_id,aluno_id,dependencia) VALUES (?,?,false) RETURNING id
""",
                                Long.class,
                                fixture.turma(),
                                fixture.aluno());
                long disciplinaId =
                        jdbc.queryForObject(
                                """
INSERT INTO disciplina(turma_id,materia_id,carga_horaria) VALUES (?,?,40) RETURNING id
""",
                                Long.class,
                                fixture.turma(),
                                fixture.materia());
                jdbc.update("DELETE FROM matricula_disciplina WHERE matricula_id=?", matriculaId);
                String associar =
                        "INSERT INTO matricula_disciplina(matricula_id,disciplina_id) VALUES (?,?)";
                String trocar = "UPDATE matricula SET turma_id=? WHERE id=?";
                Object[] valoresAssociacao = {matriculaId, disciplinaId};
                Object[] valoresTroca = {fixture.outraTurma(), matriculaId};
                var podeConfirmar = new CountDownLatch(1);
                var primeiraExecutada = new CountDownLatch(1);
                var segundaIniciada = new CountDownLatch(1);
                var pid = new java.util.concurrent.atomic.AtomicInteger();
                try (var executor = Executors.newFixedThreadPool(2)) {
                    var primeira =
                            executor.submit(
                                    () -> {
                                        try (var conexao = dados.getConnection()) {
                                            iniciarTransacao(conexao);
                                            executar(
                                                    conexao,
                                                    associarPrimeiro ? associar : trocar,
                                                    associarPrimeiro
                                                            ? valoresAssociacao
                                                            : valoresTroca);
                                            primeiraExecutada.countDown();
                                            if (!podeConfirmar.await(5, TimeUnit.SECONDS)) {
                                                throw new AssertionError(
                                                        "Segunda transação não chegou ao lock");
                                            }
                                            conexao.commit();
                                            return true;
                                        }
                                    });
                    assertThat(primeiraExecutada.await(5, TimeUnit.SECONDS)).isTrue();
                    var segunda =
                            executor.submit(
                                    () -> {
                                        try (var conexao = dados.getConnection()) {
                                            iniciarTransacao(conexao);
                                            try (var consulta = conexao.createStatement();
                                                    var resultado =
                                                            consulta.executeQuery(
                                                                    "SELECT pg_backend_pid()")) {
                                                resultado.next();
                                                pid.set(resultado.getInt(1));
                                            }
                                            segundaIniciada.countDown();
                                            try {
                                                executar(
                                                        conexao,
                                                        associarPrimeiro ? trocar : associar,
                                                        associarPrimeiro
                                                                ? valoresTroca
                                                                : valoresAssociacao);
                                                conexao.commit();
                                                return "CONFIRMOU";
                                            } catch (SQLException erro) {
                                                conexao.rollback();
                                                return erro.getSQLState();
                                            }
                                        }
                                    });
                    assertThat(segundaIniciada.await(5, TimeUnit.SECONDS)).isTrue();
                    try {
                        aguardarLock(pid.get());
                    } finally {
                        podeConfirmar.countDown();
                    }
                    assertThat(primeira.get(10, TimeUnit.SECONDS)).isTrue();
                    assertThat(segunda.get(10, TimeUnit.SECONDS)).isEqualTo("23514");
                }
                assertThat(
                                jdbc.queryForObject(
                                        """
                                        SELECT count(*) FROM matricula_disciplina md
                                        JOIN matricula m ON m.id=md.matricula_id
                                        JOIN disciplina d ON d.id=md.disciplina_id
                                        WHERE m.id=? AND m.turma_id<>d.turma_id
                                        """,
                                        Integer.class,
                                        matriculaId))
                        .isZero();
            } finally {
                excluirFixture(fixture);
            }
        }
    }

    private long inserirConcorrente(
            CountDownLatch prontos, CountDownLatch iniciar, String sql, Object... valores)
            throws Exception {
        try (var conexao = dados.getConnection()) {
            iniciarTransacao(conexao);
            prontos.countDown();
            if (!iniciar.await(5, TimeUnit.SECONDS))
                throw new AssertionError("Transações não prontas");
            try (var comando = preparar(conexao, sql, valores);
                    var resultado = comando.executeQuery()) {
                resultado.next();
                long id = resultado.getLong(1);
                conexao.commit();
                return id;
            }
        }
    }

    private void iniciarTransacao(Connection conexao) throws SQLException {
        conexao.setAutoCommit(false);
        try (var comando = conexao.createStatement()) {
            comando.execute("SET LOCAL lock_timeout='3s'");
            comando.execute("SET LOCAL statement_timeout='8s'");
        }
    }

    private PreparedStatement preparar(Connection conexao, String sql, Object... valores)
            throws SQLException {
        var comando = conexao.prepareStatement(sql);
        for (int indice = 0; indice < valores.length; indice++)
            comando.setObject(indice + 1, valores[indice]);
        return comando;
    }

    private void executar(Connection conexao, String sql, Object... valores) throws SQLException {
        try (var comando = preparar(conexao, sql, valores)) {
            comando.executeUpdate();
        }
    }

    private void aguardarLock(int pid) throws InterruptedException {
        long limite = System.nanoTime() + Duration.ofSeconds(2).toNanos();
        while (System.nanoTime() < limite) {
            String espera =
                    jdbc.queryForObject(
                            "SELECT wait_event_type FROM pg_stat_activity WHERE pid=?",
                            String.class,
                            pid);
            if ("Lock".equals(espera)) return;
            Thread.sleep(20);
        }
        throw new AssertionError("Contenda de locks não foi observada");
    }

    private Fixture criarFixture() {
        String identificador = UUID.randomUUID().toString();
        long aluno = pessoa(identificador + "a", "ALUNO");
        long professor = pessoa(identificador + "p", "PROFESSOR");
        long curso =
                jdbc.queryForObject(
                        "INSERT INTO curso(nome) VALUES (?) RETURNING id",
                        Long.class,
                        identificador);
        long serie =
                jdbc.queryForObject(
                        "INSERT INTO serie(nome) VALUES (?) RETURNING id",
                        Long.class,
                        identificador);
        long materia =
                jdbc.queryForObject(
                        "INSERT INTO materia(nome) VALUES (?) RETURNING id",
                        Long.class,
                        identificador);
        long turma = turma(curso, serie, identificador + "1");
        long outra = turma(curso, serie, identificador + "2");
        return new Fixture(aluno, professor, curso, serie, materia, turma, outra);
    }

    private long pessoa(String identificador, String perfil) {
        return jdbc.queryForObject(
                """
INSERT INTO pessoa(nome,email,matricula,perfil,ativo) VALUES (?,?,?,?,false) RETURNING id
""",
                Long.class,
                "Fixture acadêmica",
                identificador + "@atena.test",
                identificador,
                perfil);
    }

    private long turma(long curso, long serie, String nome) {
        return jdbc.queryForObject(
                """
                INSERT INTO turma(curso_id,serie_id,ano,nome) VALUES (?,?,2026,?) RETURNING id
                """,
                Long.class,
                curso,
                serie,
                nome);
    }

    private void excluirFixture(Fixture fixture) {
        jdbc.update(
                "DELETE FROM matricula WHERE turma_id IN (?,?)",
                fixture.turma(),
                fixture.outraTurma());
        jdbc.update(
                "DELETE FROM disciplina WHERE turma_id IN (?,?)",
                fixture.turma(),
                fixture.outraTurma());
        jdbc.update("DELETE FROM turma WHERE id IN (?,?)", fixture.turma(), fixture.outraTurma());
        jdbc.update("DELETE FROM materia WHERE id=?", fixture.materia());
        jdbc.update("DELETE FROM serie WHERE id=?", fixture.serie());
        jdbc.update("DELETE FROM curso WHERE id=?", fixture.curso());
        jdbc.update("DELETE FROM pessoa WHERE id IN (?,?)", fixture.aluno(), fixture.professor());
    }

    private record Fixture(
            long aluno,
            long professor,
            long curso,
            long serie,
            long materia,
            long turma,
            long outraTurma) {}
}
