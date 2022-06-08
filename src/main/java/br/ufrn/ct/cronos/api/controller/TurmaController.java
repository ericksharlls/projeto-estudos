package br.ufrn.ct.cronos.api.controller;

import java.util.List;

import br.ufrn.ct.cronos.api.model.TurmaModel;
import br.ufrn.ct.cronos.api.assembler.TurmaModelAssembler;

import br.ufrn.ct.cronos.domain.repository.filter.TurmaFilter;
import br.ufrn.ct.cronos.domain.service.CadastroTurmaService;
import br.ufrn.ct.cronos.domain.model.Turma;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping(value = "/turmas")
public class TurmaController {

	@Autowired
	private CadastroTurmaService cadastroTurma;

    @Autowired
	private TurmaModelAssembler turmaModelAssembler;

    @GetMapping
	public List<TurmaModel> pesquisar(TurmaFilter filtro) {
		List<Turma> turmas = cadastroTurma.pesquisar(filtro);
		
		return turmaModelAssembler.toCollectionModel(turmas);
	}
    
}
