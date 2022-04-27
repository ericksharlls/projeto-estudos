
CREATE DATABASE db_cronos DEFAULT CHARACTER SET utf8;

USE db_cronos;

-- -----------------------------------------------------
-- TABELA 'predio'
-- -----------------------------------------------------
CREATE TABLE predio (
  id_predio TINYINT NOT NULL AUTO_INCREMENT,
  nome_predio VARCHAR(50) NOT NULL,
  descricao_predio VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_predio)
) engine=InnoDB default charset utf8;

-- -----------------------------------------------------
-- TABELA 'periodo'
-- -----------------------------------------------------
CREATE TABLE periodo (
  id_periodo TINYINT NOT NULL AUTO_INCREMENT,
  nome_periodo VARCHAR(30) NOT NULL,
  descricao_periodo VARCHAR(50) NOT NULL,
  data_inicio_periodo DATE NOT NULL,
  data_termino_periodo DATE NOT NULL,
  is_periodo_letivo TINYINT(1) NOT NULL,
  ano_periodo SMALLINT NOT NULL,
  numero_periodo TINYINT NOT NULL,
  PRIMARY KEY (id_periodo)
) engine=InnoDB default charset utf8;

-- -----------------------------------------------------
-- Tabela 'perfil_sala_turma'
-- -----------------------------------------------------
CREATE TABLE perfil_sala_turma (
  id_perfil_sala_turma TINYINT NOT NULL AUTO_INCREMENT,
  nome_perfil_sala_turma VARCHAR(50) NOT NULL,
  descricao_perfil_sala_turma VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_perfil_sala_turma)
) engine=InnoDB default charset utf8;

-- -----------------------------------------------------
-- Table 'sala'
-- -----------------------------------------------------
CREATE TABLE sala (
  id_sala INT NOT NULL AUTO_INCREMENT,
  nome_sala VARCHAR(50) NOT NULL,
  descricao_sala VARCHAR(100) NOT NULL,
  capacidade_sala SMALLINT NOT NULL,
  tipo_quadro_sala VARCHAR(30) NOT NULL,
  utilizar_distribuicao TINYINT NOT NULL,
  utilizar_agendamento TINYINT NOT NULL,
  distribuir TINYINT NOT NULL,
  id_predio TINYINT NOT NULL,
  id_perfil TINYINT NOT NULL,
  PRIMARY KEY (id_sala),
  INDEX fk_sala_predio_idx (id_predio ASC) VISIBLE,
  INDEX fk_sala_perfil_sala_turma1_idx (id_perfil ASC) VISIBLE,
  CONSTRAINT fk_sala_predio
    FOREIGN KEY (id_predio)
    REFERENCES db_cronos.predio (id_predio)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_sala_perfil_sala_turma1
    FOREIGN KEY (id_perfil)
    REFERENCES db_cronos.perfil_sala_turma (id_perfil_sala_turma)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

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

INSERT INTO sala (nome_sala, descricao_sala, capacidade_sala, tipo_quadro_sala, utilizar_distribuicao, utilizar_agendamento, distribuir, id_predio, id_perfil) VALUES 
("A1", "Sala A1", 30, "Branco", true, true, true, 1, 1),
("A2", "Sala A2", 50, "Negro", true, true, true, 1, 1);