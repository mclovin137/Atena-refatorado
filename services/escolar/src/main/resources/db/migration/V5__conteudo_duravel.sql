-- Expansão compatível: mantém referências de arquivos T-001 para leitura/retropreenchimento.
ALTER TABLE material ADD COLUMN conteudo bytea;
ALTER TABLE pessoa ADD COLUMN avatar_conteudo bytea;

-- Limites existentes continuam protegidos para caminhos de escrita que não passam por HTTP.
ALTER TABLE material ADD CONSTRAINT material_conteudo_limite
    CHECK (conteudo IS NULL OR octet_length(conteudo) <= 10485760);
ALTER TABLE pessoa ADD CONSTRAINT pessoa_avatar_conteudo_limite
    CHECK (avatar_conteudo IS NULL OR octet_length(avatar_conteudo) <= 83886080);
-- Avatar reencodificado pode superar os 5MB do arquivo de entrada; dimensões4096x4096 limitam memória.
