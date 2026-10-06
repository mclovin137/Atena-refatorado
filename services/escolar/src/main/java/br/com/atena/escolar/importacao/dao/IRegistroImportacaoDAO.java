package br.com.atena.escolar.importacao.dao;

import br.com.atena.escolar.importacao.dto.RegistroImportacaoDTO;

import java.util.List;
import java.util.UUID;

public interface IRegistroImportacaoDAO {
    void guardar(UUID importacaoId, RegistroImportacaoDTO registro);

    List<RegistroImportacaoDTO> listar(UUID importacaoId);

    void resultado(UUID importacaoId, RegistroImportacaoDTO registro);

    void vincularPessoa(UUID importacaoId, String identificadorLegado, Long pessoaId);

    Long pessoaVinculada(UUID importacaoId, String identificadorLegado);
}
