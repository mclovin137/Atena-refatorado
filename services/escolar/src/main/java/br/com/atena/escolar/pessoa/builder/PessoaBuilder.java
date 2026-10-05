package br.com.atena.escolar.pessoa.builder;

import br.com.atena.escolar.pessoa.dominio.Pessoa;
import br.com.atena.escolar.pessoa.dto.PessoaRequisicaoDTO;
import br.com.atena.escolar.pessoa.dto.PessoaRespostaDTO;

public final class PessoaBuilder {

    private PessoaBuilder() {}

    public static Pessoa dominio(PessoaRequisicaoDTO requisicao, Long identificador) {
        return new Pessoa(
                identificador,
                requisicao.nome,
                requisicao.email,
                requisicao.matricula,
                requisicao.perfil,
                null,
                requisicao.ativo,
                requisicao.telefone,
                requisicao.endereco,
                requisicao.documento,
                requisicao.nascimento,
                requisicao.cep,
                requisicao.pai,
                requisicao.mae,
                requisicao.estadoCivil,
                requisicao.sexo,
                requisicao.telefoneResponsavel,
                requisicao.emailResponsavel,
                null,
                null);
    }

    public static PessoaRespostaDTO resposta(Pessoa registro) {
        return new PessoaRespostaDTO(
                registro.id(),
                registro.nome(),
                registro.email(),
                registro.matricula(),
                registro.perfil(),
                registro.ativo(),
                registro.telefone(),
                registro.endereco(),
                registro.documento(),
                registro.nascimento(),
                registro.cep(),
                registro.pai(),
                registro.mae(),
                registro.estadoCivil(),
                registro.sexo(),
                registro.telefoneResponsavel(),
                registro.emailResponsavel(),
                registro.avatar() != null);
    }

    public static Pessoa dadosPrivados(
            Pessoa requisicao, String senhaHash, String avatar, byte[] avatarConteudo) {
        return new Pessoa(
                requisicao.id(),
                requisicao.nome(),
                requisicao.email(),
                requisicao.matricula(),
                requisicao.perfil(),
                senhaHash,
                requisicao.ativo(),
                requisicao.telefone(),
                requisicao.endereco(),
                requisicao.documento(),
                requisicao.nascimento(),
                requisicao.cep(),
                requisicao.pai(),
                requisicao.mae(),
                requisicao.estadoCivil(),
                requisicao.sexo(),
                requisicao.telefoneResponsavel(),
                requisicao.emailResponsavel(),
                avatar,
                avatarConteudo);
    }
}
