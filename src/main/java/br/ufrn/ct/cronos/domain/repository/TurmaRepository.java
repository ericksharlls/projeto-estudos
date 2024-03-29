package br.ufrn.ct.cronos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Turma;

@Repository
public interface TurmaRepository extends CustomJpaRepository<Turma, Long>, CustomizedTurmaRepository, JpaSpecificationExecutor<Turma> {
    
    @Query("from Turma t WHERE t.departamento.id = :departamentoId AND t.periodo.id = :periodoId")
    List<Turma> findByDepartamentoAndPeriodo(@Param("departamentoId") Long departamentoId, @Param("periodoId") Long periodoId);

    Turma findByIdTurmaSIGAA(Long idTurmaSIGAA);

}
