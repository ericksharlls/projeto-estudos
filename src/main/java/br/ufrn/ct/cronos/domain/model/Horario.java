package br.ufrn.ct.cronos.domain.model;

import java.time.LocalTime;

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
@Table(name = "horario")
public class Horario {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long id;

    @Column(name="horario")
    private Integer horario;

    @Column(name="inicio_horario")
    private LocalTime inicioHorario;

    @Column(name="termino_horario")
    private LocalTime terminoHorario;

    @Column(name="horario_intermediario")
    private LocalTime horarioIntermediario;

    @Column(name="turno")
    private String turno;

    @Column(name="inicio_horario_absoluto")
    private LocalTime inicioHorarioAbsoluto;

    @Column(name="termino_horario_absoluto")
    private LocalTime terminoHorarioAbsoluto;

}
