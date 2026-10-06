<?php
/** Contador compartilhado pelos workers da única instância, sem armazenar IP em claro. */
final class LimiteLogin
{
    public function __construct(private string $diretorio, private int $capacidade = 1000) {}

    public function consumir(string $ip, ?int $agora = null): int
    {
        $agora ??= time();
        $mask = umask(0077);
        try {
            if (!is_dir($this->diretorio) && !mkdir($this->diretorio, 0700) && !is_dir($this->diretorio)) throw new RuntimeException('Controle de acesso indisponível');
            if (is_link($this->diretorio) || (fileperms($this->diretorio) & 0777) !== 0700) throw new RuntimeException('Controle de acesso indisponível');
            $path = $this->diretorio . '/contador';
            if (is_link($path)) throw new RuntimeException('Controle de acesso indisponível');
            $arquivo = fopen($path, 'c+');
            if (!$arquivo) throw new RuntimeException('Controle de acesso indisponível');
            try {
                if ((fstat($arquivo)['mode'] & 0777) !== 0600 || !flock($arquivo, LOCK_EX)) throw new RuntimeException('Controle de acesso indisponível');
                $conteudo = stream_get_contents($arquivo);
                $contadores = $conteudo === '' ? [] : json_decode($conteudo, true, 16, JSON_THROW_ON_ERROR);
                foreach ($contadores as $chave => $janela) {
                    if ($agora >= $janela['inicio'] + 60) unset($contadores[$chave]);
                }
                $chave = hash('sha256', $ip);
                if (!isset($contadores[$chave]) && count($contadores) >= $this->capacidade) return 60;
                $contadores[$chave] ??= ['inicio' => $agora, 'tentativas' => 0];
                $janela = &$contadores[$chave];
                if ($janela['tentativas'] >= 20) return max(1, $janela['inicio'] + 60 - $agora);
                $janela['tentativas']++;
                $dados = json_encode($contadores, JSON_THROW_ON_ERROR);
                rewind($arquivo);
                if (!ftruncate($arquivo, 0) || fwrite($arquivo, $dados) !== strlen($dados) || !fflush($arquivo)) throw new RuntimeException('Controle de acesso indisponível');
                return 0;
            } finally {
                flock($arquivo, LOCK_UN);
                fclose($arquivo);
            }
        } finally { umask($mask); }
    }
}
