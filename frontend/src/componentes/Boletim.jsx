import React from "react";
import { api } from "../api";
import ExportarDocumento from "./ExportarDocumento";
export default function Boletim({
  user,
  guard,
  setBoletim,
  refs,
  boletim,
  admin,
  setReport,
  report,
}) {
  return (
    <>
      <section className="panel">
        <form
          className="inline-form"
          onSubmit={(e) => {
            e.preventDefault();
            const id = new FormData(e.currentTarget).get("aluno");
            guard(async () => setBoletim(await api(`boletins/${id}`)));
          }}
        >
          <label>
            Aluno
            {user.perfil === "ALUNO" ? (
              <input name="aluno" type="hidden" value={user.id} />
            ) : (
              <select name="aluno" required>
                <option value="">Selecione</option>
                {refs.alunos?.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.nome}
                  </option>
                ))}
              </select>
            )}
            {user.perfil === "ALUNO" && <strong>{user.nome}</strong>}
          </label>
          <button type="submit">Consultar resultados</button>
        </form>
        {boletim && (
          <>
            <h2>{boletim.nome}</h2>
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Disciplina / ano</th>
                    <th>Bimestres</th>
                    <th>Média final</th>
                    <th>Frequência</th>
                    <th>Situação</th>
                  </tr>
                </thead>
                <tbody>
                  {boletim.linhas.map((l, i) => (
                    <tr key={i}>
                      <td>
                        {l.disciplina} / {l.ano}
                      </td>
                      <td>{l.notas.map((n) => n ?? "—").join(" · ")}</td>
                      <td>{l.resultado.media.toFixed(1)}</td>
                      <td>{l.resultado.frequencia.toFixed(1)}%</td>
                      <td>{l.resultado.situacao.replaceAll("_", " ")}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        )}
      </section>
      <ExportarDocumento
        user={user}
        admin={admin}
        refs={refs}
        guard={guard}
        setReport={setReport}
        report={report}
      />
    </>
  );
}
