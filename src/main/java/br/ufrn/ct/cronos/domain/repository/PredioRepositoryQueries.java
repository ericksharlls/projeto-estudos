package br.ufrn.ct.cronos.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.ufrn.ct.cronos.domain.model.Predio;

// Interface criada para representar queries customizadas do Repositório de Prédio
// Uma nomenclatura bem utilizada, o nome da classe poderia ser CustomizedPredioRepository
public interface PredioRepositoryQueries {

    // Sua implementação é com JPQL
    List<Predio> find(String nome, String descricao);

    // Sua implementação é com Criteria API
    List<Predio> buscar(String nome, String descricao);

    // Sua implementação é com Specifications
    public List<Predio> findNomeAndDescricao(String nome, String descricao);

    // Sua implementação é com JPQL (filtros dinamicos) e paginação
    public Page<Predio> findComPaginacaoJPQLDinamico(String nome, String descricao, Pageable pageable);

    // Sua implementação é com Criteria (filtros dinamicos) e paginação
    public Page<Predio> findComPaginacaoCriteriaDinamico(String nome, String descricao, Pageable pageable);
    
}
