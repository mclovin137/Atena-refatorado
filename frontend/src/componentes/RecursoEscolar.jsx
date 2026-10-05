import React, { useEffect, useState } from "react";
import { adminWrite } from "../resources";
import { api } from "../api";
import Campo from "./Campo";
export default function RecursoEscolar({
  meta,
  user,
  reload,
  page,
  rows,
  busy,
  teacher,
  admin,
  refs,
  label,
  remove,
  guard,
  setNotice,
  references,
}) {
  const [draft, setDraft] = useState(null);
  const canWrite =
    admin ||
    (teacher && !adminWrite.includes(page) && page !== "materiais") ||
    page === "mensagens";
  const canCreate = canWrite && !(teacher && page === "notas");
  useEffect(() => setDraft(null), [user, page]);
  async function save(e) {
    e.preventDefault();
    await guard(async () => {
      let payload = { ...draft };
      delete payload.senha;
      if (page === "pessoas")
        payload = { pessoa: payload, senha: draft.senha || null };
      await api(page + (draft.id ? `/${draft.id}` : ""), {
        method: draft.id ? "PUT" : "POST",
        body: JSON.stringify(payload),
      });
      setDraft(null);
      setNotice("Registro salvo.");
      await reload();
    });
  }
  return (
    <section className="panel">
      <div className="panel-head">
        <p className="subtle">{rows.length} registros disponíveis</p>
        {canCreate && (
          <button
            onClick={() =>
              setDraft(
                Object.fromEntries(
                  meta.fields.map(([key]) => [
                    key,
                    {
                      ativo: true,
                      ano: new Date().getFullYear(),
                      horas: 1,
                      bimestre: 1,
                      tipo: "QUANTITATIVA",
                    }[key] ?? null,
                  ]),
                ),
              )
            }
          >
            + Novo registro
          </button>
        )}
      </div>
      {draft && (
        <form className="record-form" onSubmit={save}>
          <h2>{draft.id ? "Editar registro" : "Novo registro"}</h2>
          <div className="fields">
            {meta.fields.map((f) => (
              <Campo
                key={f[0]}
                definicao={f}
                value={draft[f[0]]}
                onChange={(k, v) => setDraft((d) => ({ ...d, [k]: v }))}
                readOnly={
                  teacher &&
                  page === "notas" &&
                  !["valor", "recuperacao"].includes(f[0])
                }
                refs={refs}
                label={label}
              />
            ))}
          </div>
          <div className="actions">
            <button type="submit" disabled={busy}>
              Salvar registro
            </button>
            <button
              type="button"
              className="secondary"
              onClick={() => setDraft(null)}
            >
              Cancelar
            </button>
          </div>
        </form>
      )}
      {rows.length === 0 ? (
        <div className="empty">
          <strong>Um novo começo</strong>
          <p>Os registros desta área aparecerão aqui.</p>
        </div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                {meta.fields
                  .filter((f) => f[0] !== "senha")
                  .map((f) => (
                    <th key={f[0]}>{f[1]}</th>
                  ))}
                {canWrite && !(page === "mensagens" && !admin) && (
                  <th>Ações</th>
                )}
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => (
                <tr key={row.id}>
                  {meta.fields
                    .filter((f) => f[0] !== "senha")
                    .map(([k, , t]) => (
                      <td key={k}>
                        {t === "checkbox"
                          ? row[k]
                            ? "Sim"
                            : "Não"
                          : refs[t]
                            ? label(t, row[k])
                            : (row[k] ?? "—")}
                      </td>
                    ))}
                  {canWrite && !(page === "mensagens" && !admin) && (
                    <td>
                      <div className="row-actions">
                        <button
                          className="text-button"
                          onClick={() => setDraft({ ...row })}
                        >
                          Editar
                        </button>
                        {!(teacher && page === "notas") && (
                          <button
                            className="text-button danger"
                            onClick={() => remove(row)}
                          >
                            Excluir
                          </button>
                        )}
                      </div>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      {page === "turmas" && admin && (
        <form
          className="inline-form"
          onSubmit={(e) => {
            e.preventDefault();
            const f = new FormData(e.currentTarget);
            guard(async () => {
              await api(`turmas/${f.get("turma")}/aplicar-curriculo`, {
                method: "POST",
                body: JSON.stringify({ nome: f.get("nome") }),
              });
              setNotice(
                "Grade curricular aplicada. Atribua os docentes em Disciplinas.",
              );
              await references();
            });
          }}
        >
          <h3>Aplicar grade curricular à turma</h3>
          <label>
            Turma
            <select name="turma" required>
              {refs.turmas?.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.nome}
                </option>
              ))}
            </select>
          </label>
          <label>
            Nome da grade
            <input name="nome" required />
          </label>
          <button type="submit">Aplicar grade</button>
        </form>
      )}
      {page === "avaliacoes" && (admin || teacher) && (
        <form
          className="inline-form"
          onSubmit={(e) => {
            e.preventDefault();
            const f = new FormData(e.currentTarget);
            guard(async () => {
              await api(
                `disciplinas/${f.get("disciplina")}/consolidar-avaliacoes`,
                {
                  method: "POST",
                  body: JSON.stringify({
                    bimestre: Number(f.get("bimestre")),
                    modo: f.get("modo"),
                  }),
                },
              );
              setNotice("Notas bimestrais consolidadas.");
            });
          }}
        >
          <h3>Consolidar avaliações qualitativas</h3>
          <label>
            Disciplina
            <select name="disciplina" required>
              {refs.disciplinas?.map((d) => (
                <option key={d.id} value={d.id}>
                  {label("disciplinas", d.id)}
                </option>
              ))}
            </select>
          </label>
          <label>
            Bimestre
            <input
              name="bimestre"
              type="number"
              min="1"
              max="4"
              required
              defaultValue="1"
            />
          </label>
          <label>
            Cálculo
            <select name="modo">
              <option value="SOMA">Soma</option>
              <option value="MEDIA">Média</option>
            </select>
          </label>
          <button type="submit">Consolidar</button>
        </form>
      )}
    </section>
  );
}
