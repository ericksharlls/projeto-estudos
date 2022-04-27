package br.ufrn.ct.cronos.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PredioModel {
    private Long id;

    private String nome;

    private String descricao;
    
    // Alteração 02 na feature refactor/PredioModel
}
