package br.com.atena.escolar.importacao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.importacao.dominio.FonteCriptografada;
import br.com.atena.escolar.importacao.service.CriptografarFonteService;
import br.com.atena.escolar.importacao.service.LerDumpLegadoService;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

class FonteLegadoTeste {
    private final String chave = Base64.getEncoder().encodeToString(new byte[32]);

    @Test
    void fonteImutavelAutenticadaPreservaLiteraisERejeitaAdulteracao() {
        byte[] origem =
                "INSERT INTO `materia` (`id`,`nome`) VALUES (1,'Lógica; O\\'Brien'),(2,NULL);"
                        .getBytes(StandardCharsets.UTF_8);
        var crypto = new CriptografarFonteService(chave);
        var fonte = crypto.criptografar(UUID.randomUUID(), origem);
        var outra = crypto.criptografar(fonte.identificador(), origem);
        assertThat(fonte.nonce()).hasSize(12).isNotEqualTo(outra.nonce());
        assertThat(new String(fonte.conteudo(), StandardCharsets.UTF_8))
                .doesNotContain("INSERT INTO");
        byte[] recuperada = crypto.descriptografar(fonte);
        assertThat(recuperada).isEqualTo(origem);
        var registros =
                new LerDumpLegadoService().executar(new String(recuperada, StandardCharsets.UTF_8));
        assertThat(registros).hasSize(2);
        assertThat(registros.get(0).campo("nome")).isEqualTo("Lógica; O'Brien");
        assertThat(registros.get(1).campo("nome")).isNull();
        byte[] alterado = fonte.conteudo();
        alterado[0] ^= 1;
        assertThatThrownBy(
                        () ->
                                crypto.descriptografar(
                                        new FonteCriptografada(
                                                fonte.versao(),
                                                fonte.identificador(),
                                                fonte.digest(),
                                                fonte.nonce(),
                                                alterado)))
                .isInstanceOf(ErroAplicacao.class)
                .hasMessage("Fonte privada não pôde ser autenticada");
        assertThatThrownBy(
                        () ->
                                crypto.descriptografar(
                                        new FonteCriptografada(
                                                fonte.versao(),
                                                UUID.randomUUID(),
                                                fonte.digest(),
                                                fonte.nonce(),
                                                fonte.conteudo())))
                .isInstanceOf(ErroAplicacao.class);
        byte[] diferente = new byte[32];
        diferente[0] = 1;
        var chaveErrada =
                new CriptografarFonteService(Base64.getEncoder().encodeToString(diferente));
        assertThatThrownBy(() -> chaveErrada.descriptografar(fonte))
                .isInstanceOf(ErroAplicacao.class);
        assertThatThrownBy(() -> new CriptografarFonteService(""))
                .isInstanceOf(ErroAplicacao.class);
    }

    @Test
    void rejeitaComandosExpressoesEIdentificadoresForaDoContrato() {
        var leitor = new LerDumpLegadoService();
        for (String comando :
                new String[] {
                    "DROP TABLE `materia`;",
                    "INSERT INTO `materia` (`id`,`nome`) VALUES (1,pg_sleep(1));",
                    "INSERT INTO `materia` (`id`,`nome`) VALUES (1,(SELECT 'x'));",
                    "INSERT INTO `segredo` (`id`) VALUES (1);",
                    "INSERT INTO `materia` (`id`,`inexistente`) VALUES (1,'x');",
                    "INSERT INTO `materia` (`id`,`nome`) VALUES (1,'incompleto);",
                    "/*!40101 SET @NAO_CONHECIDO=1 */;",
                    "CREATE TABLE `materia` (`id` int(11) NOT NULL,`nome` varchar(20) DEFAULT"
                            + " now()) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",
                    "ALTER TABLE `materia` ADD PRIMARY KEY (`inexistente`);",
                    "ALTER TABLE `presence` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;",
                    "/*!50000 DROP TABLE materia */;"
                }) {
            assertThatThrownBy(() -> leitor.executar(comando))
                    .isInstanceOf(ErroAplicacao.class)
                    .hasMessage("Formato do arquivo legado não suportado");
        }
    }
}
