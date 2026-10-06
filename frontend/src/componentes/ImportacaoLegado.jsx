import React, { useEffect, useState } from "react";
import { api } from "../api";

const ativos = ["ANALISANDO", "APLICANDO", "REPROCESSANDO"];
const estados = {
  ANALISANDO: "Analisando",
  PLANEJADA: "Pronta para aplicar",
  APLICANDO: "Aplicando",
  REPROCESSANDO: "Reprocessando",
  CONCLUIDA: "Concluída",
  CONCLUIDA_COM_PENDENCIAS: "Concluída com pendências",
  FAILED: "Falhou",
};

export default function ImportacaoLegado({ guard, busy, refs }) {
  const [importacao, setImportacao] = useState(null);
  const [erro, setErro] = useState("");
  useEffect(() => {
    if (!importacao || !ativos.includes(importacao.status)) return;
    let ativo = true;
    const timer = setTimeout(
      () =>
        api(`importacoes/${importacao.id}`)
          .then((resultado) => {
            if (ativo) setImportacao(resultado);
          })
          .catch((falha) => {
            if (ativo) setErro(falha.message);
          }),
      1000,
    );
    return () => {
      ativo = false;
      clearTimeout(timer);
    };
  }, [importacao]);

  async function acao(nome) {
    await guard(async () =>
      setImportacao(
        await api(`importacoes/${importacao.id}/${nome}`, { method: "POST" }),
      ),
    );
  }

  return (
    <section className="panel">
      <h2>Importar arquivo histórico</h2>
      <p className="subtle">
        A análise prepara um plano antes de modificar os cadastros. Referências
        incompletas permanecem pendentes.
      </p>
      {erro && (
        <p role="alert" className="error">
          {erro}
        </p>
      )}
      <form
        className="inline-form"
        onSubmit={(event) => {
          event.preventDefault();
          const arquivo = new FormData(event.currentTarget);
          guard(async () => {
            setErro("");
            setImportacao(
              await api("importacoes/analisar", {
                method: "POST",
                body: arquivo,
              }),
            );
          });
        }}
      >
        <label>
          Arquivo SQL (até 10 MiB)
          <input
            name="file"
            type="file"
            accept=".sql"
            required
            disabled={busy}
          />
        </label>
        <button type="submit" disabled={busy}>
          Analisar arquivo
        </button>
      </form>
      <form
        className="inline-form"
        onSubmit={(event) => {
          event.preventDefault();
          const identificador = new FormData(event.currentTarget).get("id");
          guard(async () =>
            setImportacao(
              await api(`importacoes/${encodeURIComponent(identificador)}`),
            ),
          );
        }}
      >
        <label>
          Identificador da importação
          <input name="id" required />
        </label>
        <button disabled={busy} type="submit">
          Consultar importação
        </button>
      </form>
      {importacao && (
        <>
          <p role="status">{estados[importacao.status] || importacao.status}</p>
          <p className="subtle">Identificador: {importacao.id}</p>
          <div className="actions">
            {importacao.status === "PLANEJADA" && (
              <button disabled={busy} onClick={() => acao("aplicar")}>
                Aplicar plano
              </button>
            )}
            {importacao.status === "FAILED" && (
              <button disabled={busy} onClick={() => acao("retomar")}>
                Retomar execução
              </button>
            )}
            {importacao.status === "CONCLUIDA_COM_PENDENCIAS" && (
              <button disabled={busy} onClick={() => acao("reprocessar")}>
                Reprocessar vínculos resolvidos
              </button>
            )}
          </div>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Tabela de origem</th>
                  <th>Estado</th>
                  <th>Motivo</th>
                  <th>Quantidade</th>
                </tr>
              </thead>
              <tbody>
                {importacao.contagens.map((contagem) => (
                  <tr
                    key={`${contagem.tabela}/${contagem.status}/${contagem.motivo}`}
                  >
                    <td>{contagem.tabela}</td>
                    <td>{contagem.status}</td>
                    <td>{contagem.motivo || "—"}</td>
                    <td>{contagem.quantidade}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {["PLANEJADA", "CONCLUIDA_COM_PENDENCIAS"].includes(
            importacao.status,
          ) && (
            <form
              className="inline-form"
              onSubmit={(event) => {
                event.preventDefault();
                const form = new FormData(event.currentTarget);
                guard(async () =>
                  setImportacao(
                    await api(`importacoes/${importacao.id}/vinculos-pessoas`, {
                      method: "POST",
                      body: JSON.stringify({
                        identificadorLegado: form.get("origem"),
                        pessoaId: Number(form.get("pessoa")),
                      }),
                    }),
                  ),
                );
              }}
            >
              <h3>Vincular referência histórica a uma pessoa existente</h3>
              <label>
                Identificador legado
                <input name="origem" required maxLength={150} />
              </label>
              <label>
                Pessoa
                <select name="pessoa" required>
                  <option value="">Selecione</option>
                  {(refs.directory || []).map((pessoa) => (
                    <option key={pessoa.id} value={pessoa.id}>
                      {pessoa.nome} • {pessoa.perfil}
                    </option>
                  ))}
                </select>
              </label>
              <button disabled={busy} type="submit">
                Registrar vínculo
              </button>
            </form>
          )}
        </>
      )}
    </section>
  );
}
