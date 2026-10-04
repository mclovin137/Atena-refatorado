// Contratos de formulário correspondem aos recursos escolares existentes.
const name=['nome','Nome','text',true];
export const resources={
 pessoas:{label:'Pessoas',group:'Secretaria',fields:[name,['email','E-mail','email',true],['matricula','Matrícula','text',true],['perfil','Perfil','role',true],['senha','Senha (mínimo 12 caracteres)','password'],['ativo','Conta ativa','checkbox'],['telefone','Telefone','tel'],['endereco','Endereço','text'],['documento','Documento','text'],['nascimento','Nascimento','date'],['cep','CEP','text'],['pai','Pai','text'],['mae','Mãe','text'],['estadoCivil','Estado civil','text'],['sexo','Sexo','text'],['telefoneResponsavel','Telefone responsável','tel'],['emailResponsavel','E-mail responsável','email']]},
 unidades:{label:'Unidade escolar',group:'Secretaria',fields:[name,['endereco','Endereço','text'],['telefone','Telefone','tel']]},
 cursos:{label:'Cursos',group:'Secretaria',fields:[name]},series:{label:'Séries',group:'Secretaria',fields:[name]},materias:{label:'Matérias',group:'Secretaria',fields:[name]},
 curriculos:{label:'Grades curriculares',group:'Secretaria',fields:[name,['materiaId','Matéria','materias',true],['cargaHoraria','Carga horária','number',true]]},
 turmas:{label:'Turmas',group:'Ensino',fields:[name,['cursoId','Curso','cursos',true],['serieId','Série','series',true],['ano','Ano letivo','number',true]]},
 disciplinas:{label:'Disciplinas e docentes',group:'Ensino',fields:[['turmaId','Turma','turmas',true],['materiaId','Matéria','materias',true],['professorId','Professor','professores'],['cargaHoraria','Carga horária','number',true]]},
 matriculas:{label:'Matrículas e dependências',group:'Secretaria',fields:[['turmaId','Turma','turmas',true],['alunoId','Aluno','alunos',true],['dependencia','Matrícula de dependência','checkbox']]},
 horarios:{label:'Horários',group:'Ensino',fields:[['disciplinaId','Disciplina','disciplinas',true],['diaSemana','Dia (1 segunda a 7 domingo)','number',true],['inicio','Início','time',true],['fim','Fim','time',true]]},
 aulas:{label:'Planejamento e diário',group:'Ensino',fields:[['disciplinaId','Disciplina','disciplinas',true],['data','Data','date',true],['conteudo','Conteúdo ministrado','textarea',true],['planejamento','Planejamento da aula','textarea'],['horas','Horas de aula','number',true]]},
 presencas:{label:'Frequência',group:'Ensino',fields:[['aulaId','Aula','aulas',true],['alunoId','Aluno','alunos',true],['faltas','Horas de ausência','number',true]]},
 avaliacoes:{label:'Avaliações',group:'Ensino',fields:[['disciplinaId','Disciplina','disciplinas',true],name,['bimestre','Bimestre','number',true],['valor','Valor máximo (0 a 100)','number',true],['data','Data','date',true],['tipo','Tipo','assessment',true]]},
 'notas-avaliacoes':{label:'Notas das avaliações',group:'Ensino',fields:[['avaliacaoId','Avaliação','avaliacoes',true],['alunoId','Aluno','alunos',true],['valor','Nota obtida','number',true]]},
 notas:{label:'Notas e recuperação',group:'Ensino',fields:[['disciplinaId','Disciplina','disciplinas',true],['alunoId','Aluno','alunos',true],['bimestre','Bimestre','number',true],['valor','Nota bimestral (0 a 100)','number'],['recuperacao','Recuperação (0 a 60)','number'],['prazo','Prazo para lançamento','date'],['liberada','Lançamento e consulta liberados','checkbox']]},
 materiais:{label:'Materiais',group:'Comunidade',fields:[]},
 eventos:{label:'Calendário',group:'Comunidade',fields:[['titulo','Título','text',true],['inicio','Início','date',true],['fim','Fim','date',true],['descricao','Descrição','textarea']]},
 mensagens:{label:'Mensagens',group:'Comunidade',fields:[['destinatarioId','Destinatário','directory',true],['texto','Mensagem','textarea',true]]}
};
export const secretaryOnly=['pessoas','unidades','cursos','series','materias','curriculos','matriculas'];
export const adminWrite=[...secretaryOnly,'turmas','disciplinas','horarios','eventos'];
