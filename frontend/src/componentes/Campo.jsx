import React from "react";
export default function Campo({
  definicao,
  value,
  onChange,
  readOnly = false,
  refs,
  label,
}) {
  const [key, title, type, required] = definicao;
  let opts = refs[type];
  if (type === "role")
    opts = ["ALUNO", "PROFESSOR", "SECRETARIA"].map((nome) => ({
      id: nome,
      nome,
    }));
  if (type === "assessment")
    opts = ["QUANTITATIVA", "QUALITATIVA"].map((nome) => ({ id: nome, nome }));
  function change(evento) {
    const entrada = evento.target.value;
    if (type === "checkbox") {
      onChange(key, evento.target.checked);
      return;
    }
    if (type !== "number" && !opts) {
      onChange(key, entrada);
      return;
    }
    if (entrada === "") {
      onChange(key, null);
      return;
    }
    onChange(
      key,
      type === "role" || type === "assessment" ? entrada : Number(entrada),
    );
  }
  return (
    <label key={key} className={type === "checkbox" ? "check" : ""}>
      {title}
      {type === "checkbox" ? (
        <input
          type="checkbox"
          checked={Boolean(value)}
          onChange={change}
          disabled={readOnly}
        />
      ) : opts ? (
        <select
          value={value ?? ""}
          onChange={change}
          required={required}
          disabled={readOnly}
        >
          <option value="">Selecione</option>
          {opts.map((o) => (
            <option key={o.id} value={o.id}>
              {label(type, o.id) == o.id ? o.nome : label(type, o.id)}
            </option>
          ))}
        </select>
      ) : type === "textarea" ? (
        <textarea
          value={value ?? ""}
          onChange={change}
          required={required}
          maxLength={4000}
          readOnly={readOnly}
        />
      ) : (
        <input
          type={type}
          value={value ?? ""}
          onChange={change}
          required={required}
          step={type === "number" ? "any" : undefined}
          maxLength={type === "password" ? 100 : 4000}
          minLength={type === "password" ? 12 : undefined}
          readOnly={readOnly}
        />
      )}
    </label>
  );
}
