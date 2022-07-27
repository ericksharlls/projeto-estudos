package br.ufrn.ct.cronos.domain.repository;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.dto.DepartamentoDTO;
import br.ufrn.ct.cronos.domain.model.dto.TurmaDTO;

@Repository
public interface TurmaApiUfrnRepository {

    public List<DepartamentoDTO> retornaUnidadesAcademicasPorIdCentro(Integer idCentro);

    public List<DepartamentoDTO> retornaUnidadesPorNome(String nome);

    public List<TurmaDTO> retornaTurmasAbertasPorSiglaNivelEnsinoIdDepartamentoAnoPeriodo(Set<String> siglanivelEnsino, Integer idDepartamento, Integer ano, Integer periodo);
    
}
