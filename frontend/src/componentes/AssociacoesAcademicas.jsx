import React, { useEffect, useState } from "react";
import { api } from "../api";

export default function AssociacoesAcademicas({
  page,
  rows,
  refs,
  label,
  guard,
  busy,
}) {
  const [parentId, setParentId] = useState("");
  const [associacoes, setAssociacoes] = useState([]);
  const [versao, setVersao] = useState(0);
  const [erro, setErro] = useState("");
  const requisito = page === "requisitos-formacao";
  const campo = requisito ? "materiaId" : "disciplinaId";
  const endpoint = `${page}/${parentId}/materias`;
  const matricula = rows.find((row) => String(row.id) === parentId);
  const opcoes = requisito
    ? refs.materias || []
    : (refs.disciplinas || []).filter(
        (oferta) => oferta.turmaId === matricula?.turmaId,
      );

  useEffect(() => {
    let ativo = true;
    setAssociacoes([]);
    setErro("");
    if (parentId) {
      api(endpoint)
        .then((resultado) => {
          if (ativo) setAssociacoes(resultado);
        })
        .catch((falha) => {
          if (ativo) setErro(falha.message);
        });
    }
    return () => {
      ativo = false;
    };
  }, [endpoint, versao]);

  return (
    <section className="panel">
      <h2>{requisito ? "Matérias do requisito" : "Matérias da matrícula"}</h2>
      {!requisito && (
        <p className="subtle">
          Este cadastro conserva notas, frequência e resultados existentes.
        </p>
      )}
      {erro && (
        <p className="error" role="alert">
          {erro}
        </p>
      )}
      <label>
        {requisito ? "Requisito" : "Matrícula"}
        <select
          value={parentId}
          disabled={busy}
          onChange={(event) => setParentId(event.target.value)}
        >
          <option value="">Selecione</option>
          {rows.map((row) => (
            <option key={row.id} value={row.id}>
              {requisito
                ? row.nome
                : `${label("alunos", row.alunoId)} • ${label("turmas", row.turmaId)}`}
            </option>
          ))}
        </select>
      </label>
      {parentId && (
        <>
          <form
            className="inline-form"
            onSubmit={(event) => {
              event.preventDefault();
              const form = new FormData(event.currentTarget);
              guard(async () => {
                await api(endpoint, {
                  method: "POST",
                  body: JSON.stringify({
                    [campo]: Number(form.get("associado")),
                  }),
                });
                setVersao((atual) => atual + 1);
              });
            }}
          >
            <label>
              {requisito ? "Matéria" : "Disciplina da turma"}
              <select name="associado" required disabled={busy}>
                <option value="">Selecione</option>
                {opcoes.map((opcao) => (
                  <option key={opcao.id} value={opcao.id}>
                    {label(requisito ? "materias" : "disciplinas", opcao.id)}
                  </option>
                ))}
              </select>
            </label>
            <button type="submit" disabled={busy}>
              Associar matéria
            </button>
          </form>
          <ul>
            {associacoes.map((associacao) => (
              <li key={associacao[campo]}>
                {label(
                  requisito ? "materias" : "disciplinas",
                  associacao[campo],
                )}{" "}
                <button
                  className="secondary"
                  disabled={busy}
                  onClick={() =>
                    guard(async () => {
                      await api(`${endpoint}/${associacao[campo]}`, {
                        method: "DELETE",
                      });
                      setVersao((atual) => atual + 1);
                    })
                  }
                >
                  Remover associação
                </button>
              </li>
            ))}
          </ul>
          {associacoes.length === 0 && (
            <p className="subtle">Nenhuma associação cadastrada.</p>
          )}
        </>
      )}
    </section>
  );
}
