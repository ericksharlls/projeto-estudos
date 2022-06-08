package br.ufrn.ct.cronos.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Horario;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    public Horario findByTurnoAndHorario(String turno, Integer horario);
    
}
