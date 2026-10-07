SET LOCAL lock_timeout = '5s';
ALTER TABLE matricula VALIDATE CONSTRAINT matricula_subtipo_aluno;
ALTER TABLE disciplina VALIDATE CONSTRAINT disciplina_subtipo_professor;
