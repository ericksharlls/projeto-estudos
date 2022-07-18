
DROP TABLE IF EXISTS `status_importacao_turmas` ;

CREATE TABLE `status_importacao_turmas` (
  `id` TINYINT NOT NULL,
  `identificador` VARCHAR(40) NOT NULL,
  `descricao` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -----------------------------------------------------
-- Table `importacoes_turmas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `importacoes_turmas` ;

CREATE TABLE `importacoes_turmas` (
  `id` SMALLINT NOT NULL,
  `horario_ultima_operacao` DATETIME NOT NULL,
  `id_departamento` MEDIUMINT NOT NULL,
  `id_status` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_importacoes_turmas_departamento_idx` (`id_departamento` ASC) VISIBLE,
  INDEX `fk_importacoes_turmas_status_importacao_turmas1_idx` (`id_status` ASC) VISIBLE,
  CONSTRAINT `fk_importacoes_turmas_departamento`
    FOREIGN KEY (`id_departamento`)
    REFERENCES `departamento` (`id_departamento`),
  CONSTRAINT `fk_importacoes_turmas_status_importacao_turmas1`
    FOREIGN KEY (`id_status`)
    REFERENCES `status_importacao_turmas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Table `historico_importacoes_turmas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `historico_importacoes_turmas` ;

CREATE TABLE `historico_importacoes_turmas` (
  `id_historico_importacao_turmas` MEDIUMINT NOT NULL,
  `id_status_importacao_turmas` TINYINT NOT NULL,
  `registrado_em` DATETIME NOT NULL,
  PRIMARY KEY (`id_historico_importacao_turmas`),
  INDEX `fk_historico_importacoes_turmas_status_importacao_turmas1_idx` (`id_status_importacao_turmas` ASC) VISIBLE,
  CONSTRAINT `fk_historico_importacoes_turmas_status_importacao_turmas1`
    FOREIGN KEY (`id_status_importacao_turmas`)
    REFERENCES `status_importacao_turmas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
