package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.ImportacaoTurmas;

@Repository
public interface ImportacaoTurmasRepository extends JpaRepository<ImportacaoTurmas, Long> {
    
}
