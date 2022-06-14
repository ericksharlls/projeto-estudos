package br.ufrn.ct.cronos.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.ct.cronos.domain.model.Horario;
import br.ufrn.ct.cronos.domain.service.CadastroHorarioService;

@RestController
@RequestMapping(value = "/horarios", produces = MediaType.APPLICATION_JSON_VALUE)

public class HorarioController {

    @Autowired
    private CadastroHorarioService horarioService;

    @GetMapping
	public List<Horario> listar(){
		
		return horarioService.listar();
	}
    
}
