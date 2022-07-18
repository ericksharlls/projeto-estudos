package br.ufrn.ct.cronos.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.ufrn.ct.cronos.core.utils.ManipuladorHorarioTurma;
import br.ufrn.ct.cronos.domain.filter.TurmaFilter;
import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.model.Turma;
import br.ufrn.ct.cronos.domain.repository.SalaRepository;
import br.ufrn.ct.cronos.domain.repository.TurmaRepository;
import br.ufrn.ct.cronos.infrastructure.repository.spec.TurmaSpecs;

@Service
public class CadastroTurmaService {

    @Autowired
	private TurmaRepository turmaRepository;

    @Autowired
	private SalaRepository salaRepository;

    @Autowired
    private CadastroHorarioService horarioService;

    @Autowired
    private ManipuladorHorarioTurma manipuladorHorarioTurma;

    public Page<Turma> pesquisar(TurmaFilter filtro, Pageable pageable) {
        //List<Turma> turmas = turmaRepository.findAll(TurmaSpecs.usandoFiltro(filtro), pageable);
        Page<Turma> turmasPage = turmaRepository.findAll(TurmaSpecs.usandoFiltro(filtro), pageable);

        for (Turma turma : turmasPage.getContent()) {
            List<Sala> salas = this.salaRepository.findByTurma(turma.getId());
            if (salas.size() > 0) {
                Sala[] arraySalas = (Sala[]) salas.toArray(new Sala[salas.size()]);
                turma.setSala("");
                for (int i = 0; i < arraySalas.length; i++) {
                    turma.setSala(turma.getSala() + arraySalas[i].getNome());
                    for (int h = 0; h < manipuladorHorarioTurma.contadorDeGruposComSabado(turma.getHorario()); h++) {
                        String grupo = manipuladorHorarioTurma.retornaGrupoComSabado(turma.getHorario(), h);
                        String turno = manipuladorHorarioTurma.retornaTurno(grupo);
                        String[] horariosDoGrupo = manipuladorHorarioTurma.retornaArrayHorarios(grupo);
                        String[] diasDoGrupo = manipuladorHorarioTurma.retornaArrayDias(grupo);

                        List<String> stringsDias = new ArrayList<String>(0);
     	                for (int z = 0; z < diasDoGrupo.length; z++) {
     	                     stringsDias.add(diasDoGrupo[z]);
     	                }
     	                List<Long> idsHorarios = new ArrayList<Long>(0);
     	                for (int z = 0; z < horariosDoGrupo.length; z++) {
     	                    idsHorarios.add(this.horarioService.findByTurnoAndHorario(turno, Integer.parseInt(horariosDoGrupo[z])).getId());
     	                }
     	                List<String> horarios =
     	                     this.turmaRepository.getHorariosPorTurmaESala(turma, arraySalas[i], turno, idsHorarios, stringsDias);
     	                String hs = "";
     	                String dias = "";
                        for (int r = 0; r < horarios.size(); r++) {
                            if (!manipuladorHorarioTurma.jaExisteHorario(hs, manipuladorHorarioTurma.retornaHorario(horarios.get(r)))) {
                               hs += manipuladorHorarioTurma.retornaHorario(horarios.get(r));
                            }
                            if (r == horarios.size() - 1) {
                               String dia = manipuladorHorarioTurma.retornaDia(horarios.get(r));
                               if (!manipuladorHorarioTurma.jaExisteDia(dias, dia)) {
                                  dias += dia;
                               }
                               turma.setSala(turma.getSala() + " (" + dias + manipuladorHorarioTurma.retornaTurno(horarios.get(r)) + hs + ") ");
                            } else {
                               String dia = manipuladorHorarioTurma.retornaDia(horarios.get(r));
                               if (!manipuladorHorarioTurma.jaExisteDia(dias, dia)) {
                                  dias += dia;
                               }
                            }
                        }
                    }
                }
            } else {
                turma.setSala("INDEFINIDO");
            }
        }
        
		return turmasPage;
	}
    
}
