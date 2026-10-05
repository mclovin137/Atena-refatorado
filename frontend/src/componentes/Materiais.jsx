import React from "react";
import { api } from "../api";
export default function Materiais({
  admin,
  teacher,
  guard,
  reload,
  setNotice,
  refs,
  label,
  rows,
  remove,
}) {
  return (
    <section className="panel">
      {(admin || teacher) && (
        <form
          className="record-form"
          onSubmit={(e) => {
            e.preventDefault();
            const form = e.currentTarget;
            guard(async () => {
              await api("materiais", {
                method: "POST",
                body: new FormData(form),
              });
              form.reset();
              await reload();
              setNotice("Material enviado.");
            });
          }}
        >
          <h2>Compartilhar um material</h2>
          <div className="fields">
            <label>
              Disciplina
              <select name="disciplinaId" required>
                {refs.disciplinas?.map((d) => (
                  <option key={d.id} value={d.id}>
                    {label("disciplinas", d.id)}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Nome
              <input name="nome" required maxLength={150} />
            </label>
            <label>
              Arquivo (até 10 MB)
              <input
                type="file"
                name="file"
                accept=".pdf,.doc,.zip,.rar,.png,.jpg,.jpeg"
                required
              />
            </label>
          </div>
          <button type="submit">Enviar material</button>
        </form>
      )}
      <div className="material-grid">
        {rows.map((m) => (
          <article key={m.id}>
            <span className="eyebrow">
              {label("disciplinas", m.disciplinaId)}
            </span>
            <h3>{m.nome}</h3>
            <a href={`/api/materiais/${m.id}/download`}>Baixar arquivo ↓</a>
            {(admin || teacher) && (
              <button className="text-button danger" onClick={() => remove(m)}>
                Excluir
              </button>
            )}
          </article>
        ))}
      </div>
      {!rows.length && <p className="empty">Nenhum material disponível.</p>}
    </section>
  );
}
