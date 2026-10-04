ALTER TABLE pessoa ADD COLUMN cep varchar(12),ADD COLUMN pai varchar(150),ADD COLUMN mae varchar(150),ADD COLUMN estado_civil varchar(40),ADD COLUMN sexo varchar(30),ADD COLUMN telefone_responsavel varchar(30),ADD COLUMN email_responsavel varchar(180),ADD COLUMN avatar varchar(255);
CREATE UNIQUE INDEX pessoa_documento_unico ON pessoa(documento) WHERE documento IS NOT NULL AND documento <> '';
