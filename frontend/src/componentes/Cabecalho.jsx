import React from "react";
export default function Cabecalho({ meta, page, user }) {
  return (
    <header>
      <div>
        <span className="eyebrow">ANO LETIVO / {new Date().getFullYear()}</span>
        <h1>
          {meta?.label ||
            {
              inicio: "Uma escola, muitas possibilidades.",
              boletim: "Boletim e histórico",
              lote: "Preparar notas",
              perfil: "Meu perfil",
              importacao: "Importação do legado",
            }[page]}
        </h1>
      </div>
      <div className="identity">
        <span className="avatar">
          {user.avatarPresente ? (
            <img src={`/api/pessoas/${user.id}/avatar`} alt="" />
          ) : (
            user.nome?.[0]
          )}
        </span>
        <div>
          <strong>{user.nome}</strong>
          <small>{user.perfil}</small>
        </div>
      </div>
    </header>
  );
}
