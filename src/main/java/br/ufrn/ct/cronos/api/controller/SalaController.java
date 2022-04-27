package br.ufrn.ct.cronos.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.api.assembler.SalaInputDisassembler;
import br.ufrn.ct.cronos.api.assembler.SalaModelAssembler;
import br.ufrn.ct.cronos.api.model.SalaModel;
import br.ufrn.ct.cronos.api.model.input.SalaInput;
import br.ufrn.ct.cronos.core.validations.ValidacaoException;
import br.ufrn.ct.cronos.domain.exception.NegocioException;
import br.ufrn.ct.cronos.domain.exception.PerfilSalaTurmaNaoEncontradoException;
import br.ufrn.ct.cronos.domain.exception.PredioNaoEncontradoException;
import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.repository.SalaRepository;
import br.ufrn.ct.cronos.domain.service.CadastroSalaService;

@RestController
@RequestMapping(value = "/salas")
public class SalaController {

    @Autowired
	private SalaRepository salaRepository;

	@Autowired
	private CadastroSalaService cadastroSala;

	@Autowired
	private SalaModelAssembler salaModelAssembler;

	@Autowired
	private SalaInputDisassembler salaInputDisassembler;

	@Autowired
	private SmartValidator validator;
	
	@GetMapping
	public Page<SalaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<Sala> salasPage = salaRepository.findAll(pageable);
        // Criar uma implementação de Page para retorno.. 3 parâmetros são recebidos
        Page<SalaModel> prediosModelPage = new PageImpl<>(
                //1 Parâmetro é a lista q vem do banco.. (O método findAll retorna um objeto Page)
                salaModelAssembler.toCollectionModel(salasPage.getContent()),
                //prediosPage.getContent(),
                //2 Parâmetro é um objeto pageable com as informações setadas do cliente (exs: size, page, sort)
                pageable,
                //3 Parâmetro: total de elementos da lista
                salasPage.getTotalElements()
            );
        
		return prediosModelPage;
	}

	/*
    @GetMapping("/{salaId}")
	public ResponseEntity<Sala> buscar(@PathVariable Long salaId) {
		Optional<Sala> sala = salaRepository.findById(salaId);
		
		if (sala.isPresent()) {
			return ResponseEntity.ok(sala.get());
		}
		
		return ResponseEntity.notFound().build();
	}*/
	@GetMapping("/{salaId}")
    public SalaModel buscar(@PathVariable Long salaId) {
		Sala sala = cadastroSala.buscarOuFalhar(salaId);
		
        return salaModelAssembler.toModel(sala);
    }

	/*
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> adicionar(@RequestBody Sala sala) {
		try {
			sala = cadastroSala.salvar(sala);

			return ResponseEntity.status(HttpStatus.CREATED)
				.body(sala);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	*/
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SalaModel adicionar(@RequestBody @Valid SalaInput salaInput) {
		try {
			Sala sala = salaInputDisassembler.toDomainObject(salaInput);
			sala = cadastroSala.salvar(sala);
			return salaModelAssembler.toModel(sala);
		} catch (PredioNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		} catch (PerfilSalaTurmaNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	/*@PutMapping("/{salaId}")
    public ResponseEntity<?> atualizar(@PathVariable Long salaId,
        @RequestBody Sala sala) {
        try {
			Sala salaAtual = salaRepository.findById(salaId).orElse(null);
			
			if (salaAtual != null) {
				BeanUtils.copyProperties(sala, salaAtual, "id");
				
				salaAtual = cadastroSala.salvar(salaAtual);
				return ResponseEntity.ok(salaAtual);
			}
			
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
    }
	*/
	@PutMapping("/{salaId}")
	public SalaModel atualizar(@PathVariable Long salaId, @RequestBody @Valid SalaInput salaInput) {
		Sala salaAtual = cadastroSala.buscarOuFalhar(salaId);
		//BeanUtils.copyProperties(sala, salaAtual, "id");
		salaInputDisassembler.copyToDomainObject(salaInput, salaAtual);
		
		try {
			salaAtual = cadastroSala.salvar(salaAtual);	
			return salaModelAssembler.toModel(salaAtual);
		} catch (PredioNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		} catch (PerfilSalaTurmaNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		
	}

	/*
	@DeleteMapping("/{salaId}")
    public ResponseEntity<?> remover(@PathVariable Long salaId) {
        try {
            cadastroSala.excluir(salaId);
            return ResponseEntity.noContent().build();
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        
    }
	*/
	@DeleteMapping("/{salaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long salaId) {
        cadastroSala.excluir(salaId);
    }

	@PatchMapping("/{salaId}")
	public Sala atualizarParcial(@PathVariable Long salaId, @RequestBody Map<String, Object> campos, 
				HttpServletRequest request) {
		//Sala salaAtual = salaRepository.findById(salaId).orElse(null);
		Sala salaAtual = cadastroSala.buscarOuFalhar(salaId);
		merge(campos, salaAtual, request);
		
		validate(salaAtual, "restaurante");
		
		//return atualizar(salaId, salaAtual);
		return null;
		/*
		merge(campos, restauranteAtual, request);
		if(salaAtual == null) {
			return ResponseEntity.notFound().build();
		}
		merge(campos, salaAtual);
		return atualizar(salaId, salaAtual);*/
	}

	private void validate(Sala sala, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(sala, objectName);
		validator.validate(sala, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
	}

	private void merge(Map<String, Object> dadosOrigem, Sala salaDestino, HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		
			Sala salaOrigem = objectMapper.convertValue(dadosOrigem, Sala.class);

			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Sala.class, nomePropriedade);
				field.setAccessible(true);
				Object novoValor = ReflectionUtils.getField(field, salaOrigem);
			
				// System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
				ReflectionUtils.setField(field, salaDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
		
	}

	@GetMapping("/por-nome")
	public Page<SalaModel> salasPorNome(@PageableDefault(size = 10) Pageable pageable, String nome, Long predioId) {
		System.out.println("### Nome: " + nome);
		System.out.println("### Prédio: " + predioId);
		Page<Sala> salasPage = salaRepository.findByNomeAndPredioId(nome, predioId, pageable);
        // Criar uma implementação de Page para retorno.. 3 parâmetros são recebidos
        Page<SalaModel> salasModelPage = new PageImpl<>(
                //1 Parâmetro é a lista q vem do banco.. (O método findAll retorna um objeto Page)
                salaModelAssembler.toCollectionModel(salasPage.getContent()),
                //prediosPage.getContent(),
                //2 Parâmetro é um objeto pageable com as informações setadas do cliente (exs: size, page, sort)
                pageable,
                //3 Parâmetro: total de elementos da lista
                salasPage.getTotalElements()
            );
		
		return salasModelPage;
	}
    
}
