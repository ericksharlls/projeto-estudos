package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Turma;

@Repository
public interface TurmaRepository extends CustomJpaRepository<Turma, Long>, CustomizedTurmaRepository, JpaSpecificationExecutor<Turma> {
    
}
