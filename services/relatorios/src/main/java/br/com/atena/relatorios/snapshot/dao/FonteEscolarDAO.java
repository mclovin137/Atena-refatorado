package br.com.atena.relatorios.snapshot.dao;

import br.com.atena.relatorios.relatorio.dominio.SolicitacaoRelatorio;
import br.com.atena.relatorios.snapshot.dto.SnapshotDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Repository
public class FonteEscolarDAO implements IFonteEscolarDAO {
    private final RestClient cliente;

    public FonteEscolarDAO(
            RestClient.Builder builder,
            @Value("${atena.school-url}") String origem,
            @Value("${atena.service-token}") String token) {
        HttpClient clienteHttp =
                HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        JdkClientHttpRequestFactory requisicoes = new JdkClientHttpRequestFactory(clienteHttp);
        requisicoes.setReadTimeout(Duration.ofSeconds(30));
        cliente =
                builder.clone()
                        .baseUrl(origem)
                        .requestFactory(requisicoes)
                        .defaultHeader("X-Service-Token", token)
                        .build();
    }

    @Override
    public SnapshotDTO carregar(SolicitacaoRelatorio solicitacao) {
        if (solicitacao.tipo().lista()) {
            return cliente.get()
                    .uri("/api/internal/listas/{tipo}", solicitacao.tipo().name())
                    .retrieve()
                    .body(SnapshotDTO.class);
        }
        return cliente.get()
                .uri(
                        uri -> {
                            uri.path("/api/internal/snapshot/{alunoId}")
                                    .queryParam("releasedOnly", solicitacao.releasedOnly());
                            if (solicitacao.professorId() != null) {
                                uri.queryParam("professorId", solicitacao.professorId());
                            }
                            return uri.build(solicitacao.alunoId());
                        })
                .retrieve()
                .body(SnapshotDTO.class);
    }
}
