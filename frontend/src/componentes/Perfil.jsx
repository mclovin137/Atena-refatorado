import React from "react";
import { api, definirCsrf } from "../api";
export default function Perfil({ user, guard, setUser, setNotice, setPage }) {
  return (
    <section className="panel">
      <h2>Seus dados de contato</h2>
      <form
        className="inline-form"
        onSubmit={(e) => {
          e.preventDefault();
          const f = new FormData(e.currentTarget);
          guard(async () => {
            setUser(
              await api(`pessoas/${user.id}/avatar`, {
                method: "POST",
                body: f,
              }),
            );
            setNotice("Foto atualizada.");
          });
        }}
      >
        <label>
          Foto pessoal (PNG/JPEG até5MB)
          <input name="file" type="file" accept=".png,.jpg,.jpeg" required />
        </label>
        <button type="submit">Atualizar foto</button>
      </form>
      <p>
        {user.email} · Matrícula {user.matricula}
      </p>
      <form
        className="record-form"
        onSubmit={(e) => {
          e.preventDefault();
          const f = new FormData(e.currentTarget);
          guard(async () => {
            setUser(
              await api("session/profile", {
                method: "PUT",
                body: JSON.stringify(
                  Object.fromEntries(
                    [...f.entries()].filter(([, v]) => v !== ""),
                  ),
                ),
              }),
            );
            setNotice("Contato atualizado.");
          });
        }}
      >
        <div className="fields">
          <label>
            Telefone
            <input
              name="telefone"
              defaultValue={user.telefone || ""}
              maxLength={30}
            />
          </label>
          <label>
            Endereço
            <input
              name="endereco"
              defaultValue={user.endereco || ""}
              maxLength={500}
            />
          </label>
          {[
            ["cep", "CEP"],
            ["pai", "Pai"],
            ["mae", "Mãe"],
            ["estadoCivil", "Estado civil"],
            ["sexo", "Sexo"],
            ["telefoneResponsavel", "Telefone do responsável"],
            ["emailResponsavel", "E-mail do responsável"],
            ["nascimento", "Nascimento"],
          ].map(([key, title]) => (
            <label key={key}>
              {title}
              <input
                name={key}
                type={
                  key === "nascimento"
                    ? "date"
                    : key === "emailResponsavel"
                      ? "email"
                      : "text"
                }
                defaultValue={user[key] || ""}
              />
            </label>
          ))}
        </div>
        <button type="submit">Atualizar contato</button>
      </form>
      <form
        className="record-form"
        onSubmit={(e) => {
          e.preventDefault();
          const f = new FormData(e.currentTarget);
          guard(async () => {
            await api("session/password", {
              method: "PUT",
              body: JSON.stringify({
                atual: f.get("atual"),
                nova: f.get("nova"),
              }),
            });
            setUser(null);
            setPage("inicio");
            const s = await api("session");
            definirCsrf(s.csrfToken);
            setNotice("Senha alterada. Entre novamente.");
          });
        }}
      >
        <h3>Alterar senha</h3>
        <div className="fields">
          <label>
            Senha atual
            <input
              name="atual"
              type="password"
              autoComplete="current-password"
              required
            />
          </label>
          <label>
            Nova senha
            <input
              name="nova"
              type="password"
              autoComplete="new-password"
              required
              minLength={12}
              maxLength={100}
            />
          </label>
        </div>
        <button type="submit">Alterar senha e sair</button>
      </form>
    </section>
  );
}
