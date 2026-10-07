<?php
/** Nome mantido para preservar chamadas do código legado; conexão exclusivamente PostgreSQL. */
class MySql
{
    private static ?PDO $pdo = null;

    public static function conectar(): PDO
    {
        if (self::$pdo !== null) return self::$pdo;
        $dsn = getenv('LEGADO_DB_DSN') ?: '';
        $password = getenv('LEGADO_DB_PASSWORD') ?: '';
        if (!str_starts_with($dsn, 'pgsql:') || $password === '') {
            throw new RuntimeException('Banco legado não configurado');
        }
        $options = [];
        foreach (explode(';', substr($dsn, 6)) as $part) {
            if ($part === '') continue;
            $pair = explode('=', $part, 2);
            if (count($pair) !== 2 || isset($options[$pair[0]])) throw new RuntimeException('DSN PostgreSQL inválido');
            $options[$pair[0]] = $pair[1];
        }
        if (getenv('LEGADO_ENV') === 'production') {
            if (($options['sslmode'] ?? '') !== 'verify-full' || !isset($options['sslrootcert']) || !is_file($options['sslrootcert'])) {
                throw new RuntimeException('TLS PostgreSQL obrigatório');
            }
        }
        self::$pdo = new PDO($dsn, getenv('LEGADO_DB_USER') ?: 'legado', $password, [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_EMULATE_PREPARES => false,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_BOTH,
        ]);
        return self::$pdo;
    }
}
