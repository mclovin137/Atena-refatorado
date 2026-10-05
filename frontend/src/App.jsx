import React, { useEffect, useRef, useState } from "react";
import { api, definirCsrf } from "./api";
import Login from "./componentes/Login";
import AreaEscolar from "./AreaEscolar";

export default function App() {
  const [sessao, setSessao] = useState({ user: null, geracao: 0 });
  const { user, geracao } = sessao;
  const [ready, setReady] = useState(false);
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);
  const identidade = useRef(sessao);

  function atualizarUsuario(proximo) {
    const trocouConta =
      (identidade.current.user?.id ?? null) !== (proximo?.id ?? null);
    const proximaSessao = {
      user: proximo,
      geracao: identidade.current.geracao + (trocouConta ? 1 : 0),
    };
    identidade.current = proximaSessao;
    setSessao(proximaSessao);
  }

  function atualizarUsuarioDaSessao(proximo) {
    // Respostas de uma área já encerrada não podem restaurar a conta anterior.
    if (identidade.current.geracao === geracao) atualizarUsuario(proximo);
  }

  async function guard(action) {
    setBusy(true);
    setError("");
    try {
      await action();
    } catch (erro) {
      setError(erro.message);
    } finally {
      setBusy(false);
    }
  }

  useEffect(() => {
    let ativo = true;
    api("session")
      .then((sessao) => {
        if (!ativo) return;
        definirCsrf(sessao.csrfToken);
        atualizarUsuario(sessao.user || null);
      })
      .catch((erro) => {
        if (ativo) setError(erro.message);
      })
      .finally(() => {
        if (ativo) setReady(true);
      });
    return () => {
      ativo = false;
    };
  }, []);

  if (!ready)
    return (
      <main className="loading" role="status">
        Carregando Atena…
      </main>
    );
  if (!user)
    return (
      <Login
        error={error}
        busy={busy}
        guard={guard}
        setUser={atualizarUsuarioDaSessao}
      />
    );

  // Logout/troca de conta desmontam todos os dados e setters da sessão anterior.
  return (
    <AreaEscolar key={geracao} user={user} setUser={atualizarUsuarioDaSessao} />
  );
}
