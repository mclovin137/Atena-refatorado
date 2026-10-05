import React from "react";
import { api, estados } from "../api";
export default function PrepararNotas({ guard, setJob, refs, job }) {
  return (
    <section className="panel">
      <h2>Preparar os quatro bimestres da turma</h2>
      <p>
        Cria apenas notas ainda ausentes. Você pode executar novamente após
        matricular novos alunos.
      </p>
      <form
        className="inline-form"
        onSubmit={(e) => {
          e.preventDefault();
          const f = new FormData(e.currentTarget);
          guard(async () =>
            setJob(
              await api("jobs/inicializar-notas", {
                method: "POST",
                body: JSON.stringify({
                  turmaId: Number(f.get("turma")),
                  requestId: crypto.randomUUID(),
                }),
              }),
            ),
          );
        }}
      >
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
        <button type="submit">Preparar notas</button>
      </form>
      {job && (
        <div className="job-state" role="status">
          Execução #{job.id}:{" "}
          <strong>{estados[job.status] || job.status}</strong> · {job.readCount}{" "}
          matrículas processadas
        </div>
      )}
    </section>
  );
}
