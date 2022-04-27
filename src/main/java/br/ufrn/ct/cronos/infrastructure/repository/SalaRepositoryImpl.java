package br.ufrn.ct.cronos.infrastructure.repository;

import java.util.List;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.repository.CustomizedSalaRepository;

@Repository
public class SalaRepositoryImpl implements CustomizedSalaRepository {

    @PersistenceContext
    private EntityManager manager;

    public List<Sala> consultarPorPredio(Long predioId) {
        var jpql = "from Sala where predio.id = :predioId";
		
		return manager.createQuery(jpql, Sala.class)
				.setParameter("predioId", predioId)
				.getResultList();
    }

    @Override
    public Page<Sala> findByNomeAndPredioId(String nome, Long predioId, Pageable pageable) {
        // é melhor concatenar String com StringBuilder
        var jpql = new StringBuilder();

		jpql.append("SELECT s FROM Sala s JOIN FETCH s.predio JOIN FETCH s.perfilSalaTurma where 0 = 0 ");
		
		var parametros = new HashMap<String, Object>();
		
		if (StringUtils.hasText(nome)) {
			jpql.append("and s.nome like :nome ");
			parametros.put("nome", "%" + nome + "%");
		}

        if (predioId != null) {
			jpql.append("and s.predio.id = :predioId ");
			parametros.put("predioId", predioId);
		}
        
        // Como o método createQuery() recebe uma String e não um StringBuilder, 
        // o método toString() do StringBuilder é chamado
        TypedQuery<Sala> query = manager
				.createQuery(jpql.toString(), Sala.class);
		
        // Faz um for no mapa: para cada iteração no mapa, pega o par chave-valor,
        // e seta na query a chave como um parâmetro, e o valor como o valor do parâmetro
		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
	    query.setMaxResults(pageable.getPageSize());
        List<Sala> salas = query.getResultList();

        Page<Sala> salasPage = new PageImpl<>(
                salas, 
                pageable, 
                getTotalCountFindByNomeAndPredioId(nome, predioId, pageable));

        return salasPage;
    }

    private Long getTotalCountFindByNomeAndPredioId(String nome, Long predioId, Pageable pageable) {
        var jpql = new StringBuilder();
		jpql.append("SELECT count(s.id) FROM Sala s JOIN s.predio JOIN s.perfilSalaTurma where 0 = 0 ");
		
		var parametros = new HashMap<String, Object>();
		
		if (StringUtils.hasText(nome)) {
			jpql.append("and s.nome like :nome ");
			parametros.put("nome", "%" + nome + "%");
		}

        if (predioId != null) {
			jpql.append("and s.predio.id = :predioId ");
			parametros.put("predioId", predioId);
		}

        TypedQuery<Long> query = manager
				.createQuery(jpql.toString(), Long.class);
		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

        return query.getSingleResult();
    }
    
}
