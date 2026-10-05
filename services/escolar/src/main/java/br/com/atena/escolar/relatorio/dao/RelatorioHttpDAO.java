package br.com.atena.escolar.relatorio.dao;

import br.com.atena.escolar.relatorio.dominio.DocumentoRelatorio;
import br.com.atena.escolar.relatorio.dominio.ErroIntegracaoRelatorio;
import br.com.atena.escolar.relatorio.dominio.Relatorio;
import br.com.atena.escolar.relatorio.dominio.SolicitacaoRelatorio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.function.Supplier;

@Component
public class RelatorioHttpDAO implements IRelatorioDAO {
    private final RestClient cliente;

    public RelatorioHttpDAO(
            @Value("${atena.reports-url}") String url,
            @Value("${atena.service-token}") String token,
            RestClient.Builder builder) {
        var fabrica =
                new JdkClientHttpRequestFactory(
                        HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build());
        fabrica.setReadTimeout(Duration.ofSeconds(30));
        cliente =
                builder.clone()
                        .baseUrl(url)
                        .defaultHeader("X-Service-Token", token)
                        .requestFactory(fabrica)
                        .build();
    }

    public Relatorio iniciar(SolicitacaoRelatorio solicitacao) {
        return executar(
                () ->
                        cliente.post()
                                .uri("/api/relatorios")
                                .body(solicitacao)
                                .retrieve()
                                .body(Relatorio.class));
    }

    public Relatorio consultar(Long id) {
        return executar(
                () ->
                        cliente.get()
                                .uri("/api/relatorios/{id}", id)
                                .retrieve()
                                .body(Relatorio.class));
    }

    public DocumentoRelatorio baixar(Long id, String formato) {
        var resposta =
                executar(
                        () ->
                                cliente.get()
                                        .uri(
                                                "/api/relatorios/{id}/download?format={formato}",
                                                id,
                                                formato)
                                        .retrieve()
                                        .toEntity(byte[].class));
        return new DocumentoRelatorio(
                resposta.getBody(),
                resposta.getHeaders().getFirst("Content-Type"),
                resposta.getHeaders().getFirst("Content-Disposition"));
    }

    private <T> T executar(Supplier<T> operacao) {
        try {
            return operacao.get();
        } catch (RestClientResponseException erro) {
            throw new ErroIntegracaoRelatorio(
                    erro.getStatusCode().value(), "Serviço de relatórios recusou a operação", erro);
        } catch (ResourceAccessException erro) {
            throw new ErroIntegracaoRelatorio(503, "Serviço de relatórios indisponível", erro);
        }
    }
}
