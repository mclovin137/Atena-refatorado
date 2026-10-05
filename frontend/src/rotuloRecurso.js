export function rotuloRecurso(refs, resource, id) {
  const r = refs[resource]?.find((v) => String(v.id) === String(id));
  if (!r) return id ?? "—";
  if (resource === "disciplinas")
    return `${rotuloRecurso(refs, "materias", r.materiaId)} • ${rotuloRecurso(refs, "turmas", r.turmaId)}`;
  if (resource === "aulas")
    return `${r.data} • ${rotuloRecurso(refs, "disciplinas", r.disciplinaId)}`;
  return r.nome || r.titulo || id;
}
