package br.com.atena.escolar.material.actor;

import br.com.atena.escolar.arquivo.dominio.ConteudoArquivo;
import br.com.atena.escolar.arquivo.dominio.IArquivoLegadoDAO;
import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.material.builder.MaterialBuilder;
import br.com.atena.escolar.material.dao.IMaterialDAO;
import br.com.atena.escolar.material.dominio.Material;
import br.com.atena.escolar.material.dto.MaterialRespostaDTO;
import br.com.atena.escolar.material.service.MaterialService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class ManterMaterialActor {
    private final IMaterialDAO materiais;
    private final AutorizacaoEnsinoActor autorizacao;
    private final IArquivoLegadoDAO arquivosLegados;
    private final MaterialService formatos = new MaterialService();

    public ManterMaterialActor(
            IMaterialDAO materiais,
            AutorizacaoEnsinoActor autorizacao,
            IArquivoLegadoDAO arquivosLegados) {
        this.materiais = materiais;
        this.autorizacao = autorizacao;
        this.arquivosLegados = arquivosLegados;
    }

    public List<MaterialRespostaDTO> listar() {
        autorizacao.usuario();
        return materiais.listar().stream()
                .filter(this::podeLer)
                .map(MaterialBuilder::resposta)
                .toList();
    }

    private boolean podeLer(Material material) {
        try {
            exigir(material, false);
            return true;
        } catch (ErroAplicacao erro) {
            if (erro.categoria() == ErroAplicacao.CategoriaEnum.ACESSO_NEGADO) {
                return false;
            }
            throw erro;
        }
    }

    public MaterialRespostaDTO consultar(Long identificador) {
        var material = buscar(identificador);
        exigir(material, false);
        return MaterialBuilder.resposta(material);
    }

    @Transactional
    public MaterialRespostaDTO enviar(
            Long disciplinaId, String nome, String nomeOriginal, byte[] conteudo) {
        autorizacao.exigirDisciplina(disciplinaId, true);
        String tipo = formatos.identificarTipo(nomeOriginal, conteudo);
        var material =
                new Material(
                        null,
                        disciplinaId,
                        nome,
                        tipo,
                        UUID.randomUUID().toString(),
                        autorizacao.usuario().identificador(),
                        conteudo);
        return MaterialBuilder.resposta(materiais.salvar(material));
    }

    @Transactional
    public ConteudoArquivo baixar(Long identificador) {
        var material = buscar(identificador);
        exigir(material, false);
        byte[] bytes = materiais.lerConteudo(identificador);
        if (bytes == null) {
            bytes = arquivosLegados.ler(material.arquivo());
            materiais.gravarConteudo(identificador, bytes);
        }
        return new ConteudoArquivo(
                bytes,
                material.tipo(),
                "material-" + identificador + formatos.extensao(material.tipo()));
    }

    @Transactional
    public void excluir(Long identificador) {
        var material = buscar(identificador);
        exigir(material, true);
        materiais.excluir(identificador);
        arquivosLegados.excluir(material.arquivo());
    }

    private Material buscar(Long identificador) {
        return materiais
                .buscar(identificador)
                .orElseThrow(() -> ErroAplicacao.ausente("Material não encontrado"));
    }

    private void exigir(Material material, boolean escrita) {
        if (!autorizacao.usuario().secretaria()) {
            autorizacao.exigirDisciplina(material.disciplinaId(), escrita);
        }
    }
}
