package br.ufrn.ct.cronos;

import org.flywaydb.core.Flyway;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.TestPropertySource;

import br.ufrn.ct.cronos.domain.model.PerfilSalaTurma;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroPerfilSalaTurmaIT {
    
    @Autowired
	private Flyway flyway;

	@LocalServerPort
	private int port;

	@Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;

    private PerfilSalaTurma perfilSalaTurmaConvencional;

    private int quantidadeSalasCadastradas;

    @BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/perfilSalaTurma";

		flyway.migrate();
		prepararDados();
	}

    @Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarPerfilExistente() {
		RestAssured
		.given()
			.pathParam("perfilSalaTurmaId", perfilSalaTurmaConvencional.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{perfilSalaTurmaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", Matchers.equalTo(perfilSalaTurmaConvencional.getNome()));
	}

    private void prepararDados() {
		perfilSalaTurmaConvencional = new PerfilSalaTurma();
		perfilSalaTurmaConvencional.setNome("Convencional");
		perfilSalaTurmaConvencional.setDescricao("Perfil para aulas convencionais");
		perfilSalaTurmaRepository.save(perfilSalaTurmaConvencional);

        PerfilSalaTurma perfil2 = new PerfilSalaTurma();
		perfil2.setNome("Laboratório");
		perfil2.setDescricao("Perfil para caracterizar realização de aulas de informática");
		perfilSalaTurmaRepository.save(perfil2);

		PerfilSalaTurma perfil3 = new PerfilSalaTurma();
		perfil3.setNome("Prancheta");
		perfil3.setDescricao("Perfil para aulas em sala de aula com prancheta");
		perfilSalaTurmaRepository.save(perfil3);

		quantidadeSalasCadastradas = (int) perfilSalaTurmaRepository.count();
	}
}
