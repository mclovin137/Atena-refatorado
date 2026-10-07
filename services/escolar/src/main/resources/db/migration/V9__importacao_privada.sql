SET LOCAL lock_timeout = '5s';

CREATE TABLE importacao_fonte (
    id UUID PRIMARY KEY,
    digest CHAR(64) NOT NULL UNIQUE,
    versao INTEGER NOT NULL CHECK (versao = 1),
    nonce BYTEA NOT NULL CHECK (octet_length(nonce) = 12),
    conteudo BYTEA NOT NULL,
    status VARCHAR(40) NOT NULL,
    operacao VARCHAR(40) NOT NULL,
    execucao_id BIGINT,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE importacao_registro (
    importacao_id UUID NOT NULL REFERENCES importacao_fonte(id) ON DELETE RESTRICT,
    tabela VARCHAR(40) NOT NULL,
    ordinal INTEGER NOT NULL CHECK (ordinal > 0),
    origem_id VARCHAR(150),
    status VARCHAR(30) NOT NULL,
    motivo VARCHAR(80),
    dados JSONB NOT NULL,
    destinos JSONB NOT NULL DEFAULT '{}'::jsonb,
    PRIMARY KEY (importacao_id, tabela, ordinal)
);
CREATE INDEX importacao_registro_status_idx ON importacao_registro(importacao_id, status);

CREATE TABLE importacao_vinculo_pessoa (
    importacao_id UUID NOT NULL REFERENCES importacao_fonte(id) ON DELETE RESTRICT,
    identificador_legado VARCHAR(150) NOT NULL,
    pessoa_id BIGINT NOT NULL REFERENCES pessoa(id) ON DELETE RESTRICT,
    PRIMARY KEY (importacao_id, identificador_legado)
);
