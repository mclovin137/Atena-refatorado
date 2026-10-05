import React from "react";
import { api, estados } from "../api";
export default function ExportarDocumento({
  user,
  admin,
  refs,
  guard,
  setReport,
  report,
}) {
  return (
    <section className="panel">
      <h2>Exportar um documento</h2>
      <form
        className="inline-form"
        onSubmit={(e) => {
          e.preventDefault();
          const f = new FormData(e.currentTarget);
          guard(async () =>
            setReport(
              await api("relatorios", {
                method: "POST",
                body: JSON.stringify({
                  alunoId:
                    user.perfil === "ALUNO" ? user.id : Number(f.get("aluno")),
                  tipo: f.get("tipo"),
                  requestId: crypto.randomUUID(),
                }),
              }),
            ),
          );
        }}
      >
        {user.perfil !== "ALUNO" && (
          <label>
            Aluno
            <select name="aluno">
              <option value="">Selecione (dispensado nas listagens)</option>
              {refs.alunos?.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.nome}
                </option>
              ))}
            </select>
          </label>
        )}
        <label>
          Documento
          <select name="tipo">
            <option value="HISTORICO">Histórico completo</option>
            <option value="BOLETIM">Boletim</option>
            {admin && (
              <>
                <option value="LISTA_ALUNOS">Listagem de alunos</option>
                <option value="LISTA_PROFESSORES">
                  Listagem de professores
                </option>
                <option value="LISTA_TURMAS">Listagem de turmas</option>
              </>
            )}
          </select>
        </label>
        <button type="submit">Gerar documento</button>
      </form>
      {report && (
        <div className="job-state" role="status">
          <strong>
            Documento #{report.id}: {estados[report.status] || report.status}
          </strong>
          {report.status === "COMPLETED" && (
            <div className="actions">
              {["pdf", "csv", "json"].map((f) => (
                <a
                  key={f}
                  href={`/api/relatorios/${report.id}/download?format=${f}`}
                >
                  Baixar {f.toUpperCase()} ↓
                </a>
              ))}
            </div>
          )}
        </div>
      )}
    </section>
  );
}
