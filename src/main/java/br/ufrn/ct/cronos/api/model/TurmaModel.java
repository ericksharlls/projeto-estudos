package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class TurmaModel {

    private Long id;
    private String nomeDisciplina;
    private String codigoDisciplina;
    private String horario;
    private Integer capacidade;
    private String numero;
    private String sala;
    private Boolean distribuir;
    private PerfilSalaTurmaModel perfil;
    private PredioModel predio;
    private PeriodoResumoModel periodo;
    private DepartamentoModel departamento;
    private Set<FuncionarioModel> docentes = new HashSet<>();
    
}
