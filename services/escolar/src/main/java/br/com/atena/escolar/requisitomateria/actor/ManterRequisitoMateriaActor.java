package br.com.atena.escolar.requisitomateria.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.materia.dao.IMateriaDAO;
import br.com.atena.escolar.requisitoformacao.dao.IRequisitoFormacaoDAO;
import br.com.atena.escolar.requisitomateria.dao.IRequisitoMateriaDAO;
import br.com.atena.escolar.requisitomateria.dominio.RequisitoMateria;
import br.com.atena.escolar.requisitomateria.dto.RequisitoMateriaRespostaDTO;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ManterRequisitoMateriaActor {
    private final IRequisitoMateriaDAO associacoes;
    private final IRequisitoFormacaoDAO requisitos;
    private final IMateriaDAO materias;
    private final AutorizacaoEnsinoActor autorizacao;

    public ManterRequisitoMateriaActor(
            IRequisitoMateriaDAO associacoes,
            IRequisitoFormacaoDAO requisitos,
            IMateriaDAO materias,
            AutorizacaoEnsinoActor autorizacao) {
        this.associacoes = associacoes;
        this.requisitos = requisitos;
        this.materias = materias;
        this.autorizacao = autorizacao;
    }

    public List<RequisitoMateriaRespostaDTO> listar(Long requisitoId) {
        autorizacao.usuario();
        exigirRequisito(requisitoId);
        return associacoes.listar(requisitoId).stream().map(this::resposta).toList();
    }

    @Transactional
    public RequisitoMateriaRespostaDTO associar(Long requisitoId, Long materiaId) {
        autorizacao.exigirSecretaria();
        exigirRequisito(requisitoId);
        materias.buscar(materiaId)
                .orElseThrow(() -> ErroAplicacao.ausente("Matéria não encontrada"));
        var associacao = new RequisitoMateria(requisitoId, materiaId);
        associacoes.inserir(associacao);
        return resposta(associacao);
    }

    @Transactional
    public void excluir(Long requisitoId, Long materiaId) {
        autorizacao.exigirSecretaria();
        exigirRequisito(requisitoId);
        if (!associacoes.excluir(new RequisitoMateria(requisitoId, materiaId))) {
            throw ErroAplicacao.ausente("Associação não encontrada");
        }
    }

    private void exigirRequisito(Long requisitoId) {
        requisitos
                .buscar(requisitoId)
                .orElseThrow(() -> ErroAplicacao.ausente("Requisito não encontrado"));
    }

    private RequisitoMateriaRespostaDTO resposta(RequisitoMateria associacao) {
        return new RequisitoMateriaRespostaDTO(associacao.requisitoId(), associacao.materiaId());
    }
}
