import React from "react";
export default function VisaoGeral({ refs, setPage }) {
  return (
    <>
      <div className="stat-grid">
        <article>
          <span>Turmas em atividade</span>
          <strong>{refs.turmas?.length || 0}</strong>
          <small>Seu contexto escolar</small>
        </article>
        <article>
          <span>Disciplinas</span>
          <strong>{refs.disciplinas?.length || 0}</strong>
          <small>Currículo e docentes</small>
        </article>
        <article>
          <span>Aulas registradas</span>
          <strong>{refs.aulas?.length || 0}</strong>
          <small>O aprendizado em movimento</small>
        </article>
      </div>
      <section className="panel overview">
        <span className="eyebrow">SEU DIA NA ESCOLA</span>
        <h2>Planeje. Acompanhe. Aprenda.</h2>
        <p>
          Consulte as aulas e o calendário, acompanhe as notas e mantenha a
          comunicação com a sua comunidade.
        </p>
        <div className="quick">
          <button onClick={() => setPage("aulas")}>Abrir diário →</button>
          <button className="secondary" onClick={() => setPage("boletim")}>
            Ver resultados
          </button>
        </div>
      </section>
    </>
  );
}
