package br.ufrn.ct.cronos.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "sala")
public class Sala {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala")
    private Long id;

    //@NotBlankAndSizeForString(max = 50)
    @Column(name = "nome_sala")
    private String nome;

    //@NotBlankAndSizeForString(max = 100)
    @Column(name = "descricao_sala")
    private String descricao;

    //@NotNullAndRangeForNumber(min = 1, max = 500)
    @Column(name = "capacidade_sala")
    private Integer capacidade;

    //@NotBlankAndSizeForString(max = 30)
    @Column(name = "tipo_quadro_sala")
    private String tipoQuadro;

    //@NotNull
    @Column(name = "utilizar_distribuicao")
    private Boolean utilizarNaDistribuicao;

    //@NotNull
    @Column(name = "utilizar_agendamento")
    private Boolean utilizarNoAgendamento;

    //@NotNull
    @Column(name = "distribuir")
    private Boolean distribuir;

    //@JsonIgnoreProperties(value = {"nome", "descricao"}, allowGetters = true)
    //@Valid
    //@ConvertGroup(from = Default.class, to = Groups.PredioId.class)
    @ManyToOne
	@JoinColumn(name = "id_predio")
	private Predio predio;

    //@JsonIgnoreProperties(value = {"nome", "descricao"}, allowGetters = true)
    //@Valid
    //@ConvertGroup(from = Default.class, to = Groups.PerfilSalaTurmaId.class)
    @ManyToOne
	@JoinColumn(name = "id_perfil")
	private PerfilSalaTurma perfilSalaTurma;
    
}
