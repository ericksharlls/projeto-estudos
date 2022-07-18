package br.ufrn.ct.cronos.api.controller;

import br.ufrn.ct.cronos.api.model.TurmaModel;
import br.ufrn.ct.cronos.core.data.PageableTranslator;
import br.ufrn.ct.cronos.api.assembler.TurmaModelAssembler;
import br.ufrn.ct.cronos.domain.service.CadastroTurmaService;
import br.ufrn.ct.cronos.domain.filter.TurmaFilter;
import br.ufrn.ct.cronos.domain.model.Turma;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping(value = "/turmas")
public class TurmaController {

	@Autowired
	private CadastroTurmaService cadastroTurma;

    @Autowired
	private TurmaModelAssembler turmaModelAssembler;

    @GetMapping
	public Page<TurmaModel> pesquisar(TurmaFilter filtro, @PageableDefault(size = 10) Pageable pageable) {
		pageable = traduzirPageable(pageable);
		Page<Turma> turmasPage = cadastroTurma.pesquisar(filtro, pageable);
		
		Page<TurmaModel> turmasModelPage = new PageImpl<>(
			//1 Parâmetro é a lista q vem do banco.. (O método findAll retorna um objeto Page)
			turmaModelAssembler.toCollectionModel(turmasPage.getContent()),
			//prediosPage.getContent(),
			//2 Parâmetro é um objeto pageable com as informações setadas do cliente (exs: size, page, sort)
			pageable,
			//3 Parâmetro: total de elementos da lista
			turmasPage.getTotalElements()
		);

		return turmasModelPage;
	}

	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = Map.of(
				"codigoDisciplina", "codigoDisciplina",
				"nomeDisciplina", "nomeDisciplina"
			);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}
    
}
