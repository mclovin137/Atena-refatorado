package br.com.atena.escolar.pessoa.actor;

import br.com.atena.escolar.arquivo.dominio.IArquivoLegadoDAO;
import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.pessoa.builder.PessoaBuilder;
import br.com.atena.escolar.pessoa.dao.IPessoaDAO;
import br.com.atena.escolar.pessoa.dominio.Pessoa;
import br.com.atena.escolar.pessoa.dto.PessoaRespostaDTO;
import br.com.atena.escolar.pessoa.service.ImagemAvatarService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class AvatarPessoaActor {
    private final IPessoaDAO pessoas;
    private final AutorizacaoEnsinoActor autorizacao;
    private final IArquivoLegadoDAO arquivosLegados;
    private final ImagemAvatarService imagens = new ImagemAvatarService();

    public AvatarPessoaActor(
            IPessoaDAO pessoas,
            AutorizacaoEnsinoActor autorizacao,
            IArquivoLegadoDAO arquivosLegados) {
        this.pessoas = pessoas;
        this.autorizacao = autorizacao;
        this.arquivosLegados = arquivosLegados;
    }

    private Pessoa pessoaPermitida(Long identificador) {
        var usuario = autorizacao.usuario();
        if (!usuario.secretaria() && !usuario.identificador().equals(identificador)) {
            ErroAplicacao.negar();
        }
        return pessoas.buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Pessoa não encontrada"));
    }

    @Transactional
    public PessoaRespostaDTO enviar(Long identificador, String nomeOriginal, byte[] bytes) {
        var pessoa = pessoaPermitida(identificador);
        byte[] imagem = imagens.normalizar(nomeOriginal, bytes);
        var alterada =
                PessoaBuilder.dadosPrivados(
                        pessoa, pessoa.senhaHash(), UUID.randomUUID().toString(), imagem);
        return PessoaBuilder.resposta(pessoas.salvar(alterada));
    }

    @Transactional
    public byte[] consultar(Long identificador) {
        var pessoa = pessoaPermitida(identificador);
        if (pessoa.avatar() == null) {
            throw ErroAplicacao.ausente("Foto não cadastrada");
        }
        byte[] bytes = pessoas.lerConteudo(identificador);
        if (bytes == null) {
            bytes = arquivosLegados.ler(pessoa.avatar());
            pessoas.gravarConteudo(identificador, bytes);
        }
        return bytes;
    }
}
