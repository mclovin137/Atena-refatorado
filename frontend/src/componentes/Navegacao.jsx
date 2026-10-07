import React from "react";
import { resources, secretaryOnly } from "../resources";
import { api, definirCsrf } from "../api";
export default function Navegacao({ page, setPage, admin, guard, setUser }) {
  return (
    <aside>
      <a href="#inicio" className="brand" onClick={() => setPage("inicio")}>
        A<span>tena</span>
      </a>
      <p className="sidebar-caption">GESTÃO ESCOLAR</p>
      <nav aria-label="Navegação principal">
        <button
          className={page === "inicio" ? "active" : ""}
          onClick={() => setPage("inicio")}
        >
          Visão geral
        </button>
        {["Secretaria", "Ensino", "Comunidade"].map((group) => (
          <React.Fragment key={group}>
            {(admin || group !== "Secretaria") && (
              <span className="nav-group">{group}</span>
            )}
            {Object.entries(resources)
              .filter(
                ([key, r]) =>
                  r.group === group && (admin || !secretaryOnly.includes(key)),
              )
              .map(([key, r]) => (
                <button
                  key={key}
                  className={page === key ? "active" : ""}
                  onClick={() => setPage(key)}
                >
                  {r.label}
                </button>
              ))}
          </React.Fragment>
        ))}
        <span className="nav-group">Resultados</span>
        <button
          className={page === "boletim" ? "active" : ""}
          onClick={() => setPage("boletim")}
        >
          Boletim e histórico
        </button>
        {admin && (
          <button
            className={page === "lote" ? "active" : ""}
            onClick={() => setPage("lote")}
          >
            Preparar notas
          </button>
        )}
        {admin && (
          <button
            className={page === "importacao" ? "active" : ""}
            onClick={() => setPage("importacao")}
          >
            Importação do legado
          </button>
        )}
        <button onClick={() => setPage("perfil")}>Meu perfil</button>
      </nav>
      <button
        className="logout"
        onClick={() =>
          guard(async () => {
            await api("session/logout", { method: "POST" });
            setUser(null);
            setPage("inicio");
            const s = await api("session");
            definirCsrf(s.csrfToken);
          })
        }
      >
        Sair da conta
      </button>
    </aside>
  );
}
