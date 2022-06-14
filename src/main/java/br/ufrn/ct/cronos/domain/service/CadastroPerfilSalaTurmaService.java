package br.ufrn.ct.cronos.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.PerfilSalaTurmaNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;

@Service
public class CadastroPerfilSalaTurmaService {

    private static final String MSG_PERFIL_SALA_TURMA_EM_USO 
        = "Prédio de id %d não pode ser removido, pois está em uso";

    private static final String CACHE_FIND_ALL
        = "CadastroPerfilSalaTurmaService.findAll";

    private static final String CACHE_FIND_BY_ID = "CadastroPerfilSalaTurmaService.buscar";

    @Autowired
    private PerfilSalaTurmaRepository perfilSalaTurmaRepository;

    @CacheEvict(value = CACHE_FIND_ALL, allEntries = true)
    @Transactional
    public PerfilSalaTurma salvar(PerfilSalaTurma perfilSalaTurma) {
        return perfilSalaTurmaRepository.save(perfilSalaTurma);
    }

    @Cacheable(key = "'allPerfis'", value = CACHE_FIND_ALL)
    public Page<PerfilSalaTurma> findAll(Pageable pageable) {
        return perfilSalaTurmaRepository.findAll(pageable);
    }

    @CacheEvict(value = CACHE_FIND_ALL, allEntries = true)
    @Transactional
    public void excluir(Long perfilSalaTurmaId) {
        try {
            perfilSalaTurmaRepository.deleteById(perfilSalaTurmaId);   
        } catch (EmptyResultDataAccessException e){
            throw new PerfilSalaTurmaNaoEncontradoException(perfilSalaTurmaId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_PERFIL_SALA_TURMA_EM_USO, perfilSalaTurmaId)
            );
        }
    }
    
    @Cacheable(key = "#perfilSalaTurmaId",value = CACHE_FIND_BY_ID)
    public PerfilSalaTurma buscar(Long perfilSalaTurmaId) {
		return perfilSalaTurmaRepository.findById(perfilSalaTurmaId)
			.orElseThrow(() -> new PerfilSalaTurmaNaoEncontradoException(perfilSalaTurmaId));
	}
    
}
