SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE predio;
TRUNCATE TABLE perfil_sala_turma;
TRUNCATE TABLE periodo;
TRUNCATE TABLE sala;
TRUNCATE TABLE departamento;
TRUNCATE TABLE turma;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO predio (nome_predio, descricao_predio) VALUES 
('Prédio Administrativo do CT', 'Prédio Administrativo do Centro de Tecnologia'),
('Setor de Aulas IV', 'Setor de Aulas IV'),
('NTI', 'Núcleo de Tecnologia Industrial'),
('LARHISA', 'Laboratório de Recursos Hídricos e Saneamento Ambiental'),
('CTEC', 'Complexo Tecnológico de Engenharia');

INSERT INTO perfil_sala_turma (nome_perfil_sala_turma, descricao_perfil_sala_turma) VALUES 
('Convencional','Perfil para aulas convencionais'), 
('Laboratório','Perfil para caracterizar realização de aulas de informática'), 
('Prancheta','Perfil para aulas em sala de aula com prancheta');

INSERT INTO periodo (nome_periodo, descricao_periodo, data_inicio_periodo, data_termino_periodo, is_periodo_letivo, ano_periodo, numero_periodo) VALUES 
('PERÍODO LETIVO - 2022.1','PERÍODO LETIVO de 2022.1','2022-02-01','2022-06-18',1,2022,1);

INSERT INTO sala (nome_sala, descricao_sala, capacidade_sala, tipo_quadro_sala, utilizar_distribuicao, utilizar_agendamento, distribuir, id_predio, id_perfil) VALUES 
("A1", "Sala A1", 30, "Branco", true, true, true, 1, 1),
("A2", "Sala A2", 50, "Negro", true, true, true, 1, 1),
("A3", "Sala A3", 35, "Negro", true, true, true, 2, 2),
("A4", "Sala A4", 35, "Branco", true, true, true, 2, 2),
("A5", "Sala A5", 35, "Branco", true, true, true, 2, 2);

-- INSERT INTO departamento (id_departamento, nome_departamento, descricao_departamento) VALUES 
-- (1,'Departamento de Arquitetura','Departamento do Curso de Arquitetura e Urbanismo'),
-- (2,'Departamento de Engenharia Biomédica','Departamento do Curso de Engenharia Biomédica'),
-- (3,'Departamento de Engenharia Civil','Departamento do Curso de Engenharia Civil'),
-- (4,'Departamento de Engenharia de Computação e Automação','Departamento do Curso de Engenharia de Computação e Automação'),
-- (5,'Departamento de Engenharia de Comunicações','Departamento do Curso de Comunicações'),(6,'Departamento de Engenharia de Materiais','Departamento do Curso de Engenharia de Materiais'),(7,'Departamento de Engenharia de Petróleo','Departamento do Curso de Engenharia de Petróleo'),(8,'Departamento de Engenharia Elétrica','Departamento do Curso de Engenharia Elétrica'),(9,'Departamento de Engenharia Mecânica','Departamento do Curso de Engenharia Mecânica'),(10,'Departamento de Engenharia Produção','Departamento do Curso de Engenharia de Produção'),(11,'Departamento de Engenharia Química','Departamento do Curso de Engenharia Mecânica'),(12,'Departamento de Engenharia Têxtil','Departamento do Curso de Engenharia Têxtil'),(13,'Coordenação do Curso de Engenharia Mecatrônica','Coordenação do Curso de Engenharia Mecatrônica'),
-- (14,'DIVERSOS','Departamento para uma turma qualquer');

-- INSERT INTO turma (id_turma, codigo_componente_turma, nome_componente_turma, nome_docente_turma, horario_turma,
--  capacidade_turma, numero_turma, alunos_matriculados_turma, distribuir, local, id_perfil, id_predio, id_periodo, id_departamento, id_sala_temp, id_turma_sigaa) VALUES 
-- (3366,'ARQ0002','DESENHO TECNICO','A DEFINIR DOCENTE','24T34',25,'1',0,0,'INDEFINIDO',3,1,1,1,0,0),
-- (3367,'ARQ0002','DESENHO TECNICO','A DEFINIR DOCENTE','4M12',25,'2',0,0,'INDEFINIDO',3,1,1,1,0,0),
-- (3368,'ARQ0002','DESENHO TECNICO','A DEFINIR DOCENTE','35T34',25,'3',0,0,'INDEFINIDO',3,1,1,1,0,0);
