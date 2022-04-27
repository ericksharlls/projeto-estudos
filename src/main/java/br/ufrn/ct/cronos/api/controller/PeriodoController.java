package br.ufrn.ct.cronos.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.domain.model.Periodo;
import br.ufrn.ct.cronos.domain.repository.PeriodoRepository;
import br.ufrn.ct.cronos.domain.service.CadastroPeriodoService;

@RestController
@RequestMapping(value = "/periodos")
public class PeriodoController {

	@Autowired
	private PeriodoRepository periodoRepository;
	
	@Autowired
	private CadastroPeriodoService cadastroPeriodo;

	@GetMapping
	public List<Periodo> listar() {
		return periodoRepository.findAll();
	}

	@GetMapping("/{idPeriodo}")
	public ResponseEntity<Periodo> buscarPorId(@PathVariable Long idPeriodo) {
		Optional<Periodo> periodo = periodoRepository.findById(idPeriodo);

		if (!(periodo.isEmpty())) {
			return ResponseEntity.ok(periodo.get());
		}
		
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Periodo cadastrar(@RequestBody @Valid Periodo periodo) {
		return cadastroPeriodo.salvar(periodo);
	}

	@PutMapping("/{idPeriodo}")
	public Periodo atualizar(@PathVariable Long idPeriodo, @RequestBody Periodo periodo) {
		Periodo periodoAtual = cadastroPeriodo.buscar(idPeriodo);
		
		BeanUtils.copyProperties(periodo, periodoAtual, "id");
		
		return cadastroPeriodo.salvar(periodoAtual);
	}

	@DeleteMapping("/{idPeriodo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long idPeriodo) {
		cadastroPeriodo.excluir(idPeriodo);
	}
}
