package br.ufrn.ct.cronos.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.ufrn.ct.cronos.domain.model.Sala;

public interface CustomizedSalaRepository {

    public List<Sala> consultarPorPredio(Long predioId);
    Page<Sala> findByNomeAndPredioId(String nome, Long predioId, Pageable pageable);
    
}
