import React, { useEffect, useState } from "react";
import { resources } from "./resources";
import { api, definirCsrf } from "./api";
import { rotuloRecurso } from "./rotuloRecurso";
import Navegacao from "./componentes/Navegacao";
import Cabecalho from "./componentes/Cabecalho";
import RecursoEscolar from "./componentes/RecursoEscolar";
import VisaoGeral from "./componentes/VisaoGeral";
import Materiais from "./componentes/Materiais";
import Boletim from "./componentes/Boletim";
import PrepararNotas from "./componentes/PrepararNotas";
import Perfil from "./componentes/Perfil";
export default function AreaEscolar({ user, setUser }) {
  const [page, setPage] = useState("inicio"),
    [rows, setRows] = useState([]),
    [refs, setRefs] = useState({}),
    [error, setError] = useState(""),
    [busy, setBusy] = useState(false),
    [notice, setNotice] = useState(""),
    [report, setReport] = useState(null),
    [job, setJob] = useState(null),
    [boletim, setBoletim] = useState(null);
  const admin = user?.perfil === "SECRETARIA",
    teacher = user?.perfil === "PROFESSOR";
  async function guard(action) {
    setBusy(true);
    setError("");
    try {
      await action();
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }
  async function references() {
    const names = [
      "directory",
      "catalogos",
      "turmas",
      "disciplinas",
      "aulas",
      "avaliacoes",
    ];
    const results = await Promise.all(names.map((n) => api(n)));
    const d = Object.fromEntries(names.map((n, i) => [n, results[i]]));
    d.cursos = d.catalogos.cursos;
    d.series = d.catalogos.series;
    d.materias = d.catalogos.materias;
    d.alunos = d.directory.filter((p) => p.perfil === "ALUNO");
    d.professores = d.directory.filter((p) => p.perfil === "PROFESSOR");
    setRefs(d);
  }
  async function reload() {
    if (resources[page]) setRows(await api(page));
    await references();
  }
  useEffect(() => {
    if (user) {
      setError("");
      setNotice("");
      guard(reload);
    }
  }, [user, page]);
  useEffect(() => {
    if (!report || ["COMPLETED", "FAILED", "STOPPED"].includes(report.status))
      return;
    const timer = setTimeout(
      () =>
        api(`relatorios/${report.id}`)
          .then(setReport)
          .catch((e) => setError(e.message)),
      1000,
    );
    return () => clearTimeout(timer);
  }, [report]);
  useEffect(() => {
    if (!job || ["COMPLETED", "FAILED", "STOPPED"].includes(job.status)) return;
    const timer = setTimeout(
      () =>
        api(`jobs/${job.id}`)
          .then(setJob)
          .catch((e) => setError(e.message)),
      1000,
    );
    return () => clearTimeout(timer);
  }, [job]);

  function label(resource, id) {
    return rotuloRecurso(refs, resource, id);
  }
  async function remove(row) {
    await guard(async () => {
      await api(`${page}/${row.id}`, { method: "DELETE" });
      setNotice("Registro excluído.");
      await reload();
    });
  }
  const meta = resources[page];
  return (
    <div className="shell">
      <Navegacao
        page={page}
        setPage={setPage}
        admin={admin}
        guard={guard}
        setUser={setUser}
      />
      <div className="workspace">
        <Cabecalho meta={meta} page={page} user={user} />
        <main>
          {error && (
            <p role="alert" className="error">
              {error}
            </p>
          )}
          {notice && (
            <p role="status" className="notice">
              {notice}
            </p>
          )}
          {busy && (
            <p role="status" className="subtle">
              Atualizando…
            </p>
          )}
          {page === "inicio" && <VisaoGeral refs={refs} setPage={setPage} />}
          {meta && page !== "materiais" && (
            <RecursoEscolar
              key={page}
              meta={meta}
              user={user}
              reload={reload}
              page={page}
              rows={rows}
              busy={busy}
              teacher={teacher}
              admin={admin}
              refs={refs}
              label={label}
              remove={remove}
              guard={guard}
              setNotice={setNotice}
              references={references}
            />
          )}
          {page === "materiais" && (
            <Materiais
              admin={admin}
              teacher={teacher}
              guard={guard}
              reload={reload}
              setNotice={setNotice}
              refs={refs}
              label={label}
              rows={rows}
              remove={remove}
            />
          )}
          {page === "boletim" && (
            <Boletim
              user={user}
              guard={guard}
              setBoletim={setBoletim}
              refs={refs}
              boletim={boletim}
              admin={admin}
              setReport={setReport}
              report={report}
            />
          )}
          {page === "lote" && (
            <PrepararNotas
              guard={guard}
              setJob={setJob}
              refs={refs}
              job={job}
            />
          )}
          {page === "perfil" && (
            <Perfil
              user={user}
              guard={guard}
              setUser={setUser}
              setNotice={setNotice}
              setPage={setPage}
            />
          )}
        </main>
        <footer>Atena · Gestão escolar conectada</footer>
      </div>
    </div>
  );
}
