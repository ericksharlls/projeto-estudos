package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long>, CustomizedSalaRepository {

    //Link com issue aberta sobre problema com o JOIN FETCH no count query
    //https://github.com/spring-projects/spring-data-jpa/issues/2348
    @Query(value = "SELECT s FROM Sala s JOIN FETCH s.predio JOIN FETCH s.perfilSalaTurma",
              countQuery = "SELECT count(s.id) FROM Sala s JOIN s.predio JOIN s.perfilSalaTurma")
	Page<Sala> findAll(Pageable pageable);

    /*
    @Query(value = "SELECT s FROM Sala s JOIN FETCH s.predio JOIN FETCH s.perfilSalaTurma "+ 
                        " WHERE s.nome like %:nome% AND s.predio.id = :id",
              countQuery = "SELECT count(s.id) FROM Sala s JOIN s.predio JOIN s.perfilSalaTurma "+ 
                        " WHERE s.nome like %:nome% AND s.predio.id = :id")
	List<Sala> consultarPorNomeEPredio(@Param("nome") String nome, @Param("id") Long predioId, Pageable pageable);
    */

}
