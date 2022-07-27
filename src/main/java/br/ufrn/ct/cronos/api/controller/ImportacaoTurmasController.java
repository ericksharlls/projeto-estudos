package br.ufrn.ct.cronos.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.api.model.input.ImportacaoTurmasInput;
import br.ufrn.ct.cronos.domain.service.ImportarTurmasService;

@EnableAsync
@RestController
@RequestMapping(value = "/api-ufrn/turmas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImportacaoTurmasController {

    @Autowired
    private ImportarTurmasService importarTurmasService;

    @PostMapping("/importacao")
    @ResponseStatus(HttpStatus.CREATED)
	public void importarTurmas(@RequestBody @Valid ImportacaoTurmasInput importacaoTurmasInput) {
        importarTurmasService.agendarImportacoes(importacaoTurmasInput.getSiglasNivelEnsino(), importacaoTurmasInput.getIdsUnidades(), importacaoTurmasInput.getIdPeriodo());
        importarTurmasService.executarAssincronamenteImportacoes();
        System.out.println("#### Terminou o CONTROLLER ####");
	}
    
}
