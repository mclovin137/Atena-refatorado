SET LOCAL lock_timeout = '5s';
INSERT INTO aluno(pessoa_id) SELECT id FROM pessoa WHERE perfil='ALUNO' ON CONFLICT DO NOTHING;
INSERT INTO professor(pessoa_id) SELECT id FROM pessoa WHERE perfil='PROFESSOR' ON CONFLICT DO NOTHING;
INSERT INTO matricula_disciplina(matricula_id,disciplina_id)
    SELECT m.id,d.id FROM matricula m JOIN disciplina d ON d.turma_id=m.turma_id
    ON CONFLICT DO NOTHING;
