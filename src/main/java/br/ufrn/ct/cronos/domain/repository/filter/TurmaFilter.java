package br.ufrn.ct.cronos.domain.repository.filter;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TurmaFilter {

    private Long idPeriodo;
    private Long idDepartamento;
    private Long idPredio;
    private String codigoComponenteCurricular;
    private String nomeComponenteCurricular;
    private String nomeDocente;
    
}
