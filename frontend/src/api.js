let csrf = "";
export const estados = {
  STARTING: "Preparando",
  STARTED: "Processando",
  COMPLETED: "Concluído",
  FAILED: "Falhou",
  STOPPED: "Interrompido",
  STOPPING: "Interrompendo",
  UNKNOWN: "Estado desconhecido",
};
export async function api(path, options = {}) {
  const headers = { ...options.headers };
  if (options.body && !(options.body instanceof FormData))
    headers["Content-Type"] = "application/json";
  if (options.method && options.method !== "GET")
    headers["X-CSRF-TOKEN"] = csrf;
  const response = await fetch(`/api/${path}`, { ...options, headers });
  if (!response.ok) {
    const error = await response
      .json()
      .catch(() => ({ message: "Não foi possível concluir a operação" }));
    throw new Error(error.message);
  }
  if (response.status === 204) return null;
  const conteudo = await response.text();
  if (conteudo === "") return null;
  return JSON.parse(conteudo);
}

export function definirCsrf(token) {
  csrf = token;
}
