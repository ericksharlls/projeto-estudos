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
  UNIQUE INDEX nome_sala_UNIQUE (nome_sala ASC) VISIBLE,
  INDEX fk_sala_predio_idx (id_predio ASC) VISIBLE,
  INDEX fk_sala_perfil_sala_turma1_idx (id_perfil ASC) VISIBLE,
  CONSTRAINT fk_sala_predio
    FOREIGN KEY (id_predio)
    REFERENCES predio (id_predio)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_sala_perfil_sala_turma1
    FOREIGN KEY (id_perfil)
    REFERENCES perfil_sala_turma (id_perfil_sala_turma)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)engine=InnoDB default charset utf8;

CREATE TABLE departamento (
  id_departamento mediumint(9) NOT NULL,
  nome_departamento varchar(90) DEFAULT NULL,
  descricao_departamento varchar(110) DEFAULT NULL,
  PRIMARY KEY (id_departamento)
)engine=InnoDB default charset utf8;

CREATE TABLE turma (
  id_turma smallint(6) NOT NULL AUTO_INCREMENT,
  codigo_componente_turma varchar(20) NOT NULL,
  nome_componente_turma varchar(90) NOT NULL,
  nome_docente_turma varchar(80) DEFAULT NULL,
  horario_turma varchar(50) NOT NULL,
  capacidade_turma smallint(6) NOT NULL,
  numero_turma varchar(5) NOT NULL,
  alunos_matriculados_turma smallint(6) NOT NULL DEFAULT '0',
  distribuir tinyint(1) NOT NULL DEFAULT '1',
  local varchar(30) NOT NULL DEFAULT 'INDEFINIDO',
  id_perfil tinyint(4) NOT NULL,
  id_predio tinyint(4) NOT NULL DEFAULT '1',
  id_periodo tinyint(4) NOT NULL,
  id_departamento mediumint(9) NOT NULL,
  id_sala_temp smallint(6) DEFAULT NULL,
  id_turma_sigaa int(11) NOT NULL,
  PRIMARY KEY (id_turma),
  UNIQUE KEY turma_unica (codigo_componente_turma,horario_turma,numero_turma,id_periodo),
  KEY `fk_turma_tipo1_idx` (`id_perfil`),
  KEY `fk_turma_predio1_idx` (`id_predio`),
  KEY `fk_turma_periodo1_idx` (`id_periodo`),
  KEY `fk_turma_departamento1_idx` (`id_departamento`),
  CONSTRAINT `fk_turma_departamento1` FOREIGN KEY (`id_departamento`) REFERENCES `departamento` (`id_departamento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_turma_periodo1` FOREIGN KEY (`id_periodo`) REFERENCES `periodo` (`id_periodo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_turma_predio1` FOREIGN KEY (`id_predio`) REFERENCES `predio` (`id_predio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_turma_tipo1` FOREIGN KEY (`id_perfil`) REFERENCES `perfil_sala_turma` (`id_perfil_sala_turma`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
