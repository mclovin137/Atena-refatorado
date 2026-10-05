import React from "react";
import { api } from "../api";
export default function Login({ error, busy, guard, setUser }) {
  return (
    <main className="login">
      <section className="welcome">
        <span className="eyebrow">ATENA / GESTÃO ESCOLAR</span>
        <h1>
          O aprendizado
          <br />
          começa com
          <br />
          <em>organização.</em>
        </h1>
        <p>Uma escola conectada. Cursos, aulas e resultados no mesmo lugar.</p>
        <span className="footer-note">Secretaria · Professores · Alunos</span>
      </section>
      <section className="login-form">
        <span className="brand">
          A<span>tena</span>
        </span>
        <h2>Entre na sua escola</h2>
        <p>Use as credenciais fornecidas pela secretaria.</p>
        {error && (
          <p role="alert" className="error">
            {error}
          </p>
        )}
        <form
          onSubmit={(e) => {
            e.preventDefault();
            const values = new FormData(e.currentTarget);
            guard(async () => {
              setUser(
                await api("session/login", {
                  method: "POST",
                  body: JSON.stringify({
                    email: values.get("email"),
                    senha: values.get("senha"),
                  }),
                }),
              );
            });
          }}
        >
          <label>
            E-mail
            <input name="email" type="email" autoComplete="username" required />
          </label>
          <label>
            Senha
            <input
              name="senha"
              type="password"
              autoComplete="current-password"
              required
            />
          </label>
          <button type="submit" disabled={busy}>
            Entrar →
          </button>
        </form>
        <small>Acesso pessoal e protegido</small>
      </section>
    </main>
  );
}
