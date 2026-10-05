package br.com.atena.escolar.autorizacao.service;

import br.com.atena.escolar.autorizacao.dominio.IAutorizacaoPerfil;
import br.com.atena.escolar.usuario.dominio.PerfilEnum;

/** Três variantes reais de capacidades, sem árvore genérica por tipo de recurso. */
public final class AutorizacaoPerfilFactory {

    public IAutorizacaoPerfil selecionar(PerfilEnum perfil) {
        return switch (perfil) {
            case SECRETARIA -> new AutorizacaoSecretariaService();
            case PROFESSOR -> new AutorizacaoProfessorService();
            case ALUNO -> new AutorizacaoAlunoService();
        };
    }
}
