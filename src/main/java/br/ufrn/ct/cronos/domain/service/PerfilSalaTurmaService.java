package br.ufrn.ct.cronos.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.ufrn.ct.cronos.domain.exception.EntidadeEmUsoException;
import br.ufrn.ct.cronos.domain.exception.PerfilSalaTurmaNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;

@Service
public class PerfilSalaTurmaService {
	
    private static final String MSG_PERFIL_SALA_TURMA_EM_USO 
            = "Perfil de Sala/Turma de id %d não pode ser removido, pois está em uso.";

	@Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;
	
	public PerfilSalaTurma salvar(PerfilSalaTurma perfilSalaTurma) {
		return perfilSalaTurmaRepository.save(perfilSalaTurma);
	}
	
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

}