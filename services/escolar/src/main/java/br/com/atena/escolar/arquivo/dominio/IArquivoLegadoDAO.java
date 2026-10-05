package br.com.atena.escolar.arquivo.dominio;

/** Fronteira filesystem somente para compatibilidade com dados T-001, não novos uploads. */
public interface IArquivoLegadoDAO {
    byte[] ler(String identificador);

    void excluir(String identificador);
}
