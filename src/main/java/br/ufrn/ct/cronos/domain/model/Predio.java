package br.ufrn.ct.cronos.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "predio")
public class Predio {
    
    //@NotNull(groups = Groups.PredioId.class)
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_predio")
    private Long id;

    //@NotBlankAndSizeForString(max = 50)
    @Column(name = "nome_predio")
    private String nome;
    
    //@NotBlankAndSizeForString(max = 100)
    @Column(name = "descricao_predio")
    private String descricao;

}
