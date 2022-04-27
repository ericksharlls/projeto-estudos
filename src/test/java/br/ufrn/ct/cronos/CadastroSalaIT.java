package br.ufrn.ct.cronos;

import org.flywaydb.core.Flyway;

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
import br.ufrn.ct.cronos.domain.model.Predio;
import br.ufrn.ct.cronos.domain.model.Sala;
import br.ufrn.ct.cronos.domain.repository.PerfilSalaTurmaRepository;
import br.ufrn.ct.cronos.domain.repository.PredioRepository;
import br.ufrn.ct.cronos.domain.repository.SalaRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasItem;

@ExtendWith(SpringExtension.class) //faz com q o contexto do Spring seja levantado no momento da execução dos testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroSalaIT {

    @Autowired
	private Flyway flyway;

	@LocalServerPort
	private int port;

	@Autowired
	private SalaRepository salaRepository;
    @Autowired
	private PerfilSalaTurmaRepository perfilSalaTurmaRepository;
    @Autowired
	private PredioRepository predioRepository;

    private Sala quintaSalaCadastrada;
    private Predio predioSetorAulasIV;
    private PerfilSalaTurma perfilSalaTurmaConvencional;

    private int quantidadeSalasCadastradas;

    @BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/salas";

		flyway.migrate();
		prepararDados();
	}

    @Test
	public void deveRetornarSalaNaPaginaCorreta_QuandoRealizarConsultaPaginada() {
		given()
            .queryParam("size", "2")
            .queryParam("page", "2")
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("content.nome", hasItem(equalTo(quintaSalaCadastrada.getNome())));
	}

    private void prepararDados() {
        criarPredioDoSetorDeAulasIV();
        criarPerfilSalaTurmaConvencional();

        Sala sala01 = new Sala();
		sala01.setNome("A1");
		sala01.setDescricao("Sala A1");
		sala01.setCapacidade(50);
		sala01.setDistribuir(true);
		sala01.setPerfilSalaTurma(perfilSalaTurmaConvencional);
		sala01.setPredio(predioSetorAulasIV);
		sala01.setTipoQuadro("Branco");
		sala01.setUtilizarNaDistribuicao(true);
		sala01.setUtilizarNoAgendamento(false);
		salaRepository.save(sala01);

        Sala sala02 = new Sala();
		sala02.setNome("A2");
		sala02.setDescricao("Sala A2");
		sala02.setCapacidade(50);
		sala02.setDistribuir(true);
		sala02.setPerfilSalaTurma(perfilSalaTurmaConvencional);
		sala02.setPredio(predioSetorAulasIV);
		sala02.setTipoQuadro("Negro");
		sala02.setUtilizarNaDistribuicao(true);
		sala02.setUtilizarNoAgendamento(false);
		salaRepository.save(sala02);

        Sala sala03 = new Sala();
		sala03.setNome("A3");
		sala03.setDescricao("Sala A3");
		sala03.setCapacidade(50);
		sala03.setDistribuir(false);
		sala03.setPerfilSalaTurma(perfilSalaTurmaConvencional);
		sala03.setPredio(predioSetorAulasIV);
		sala03.setTipoQuadro("Negro");
		sala03.setUtilizarNaDistribuicao(false);
		sala03.setUtilizarNoAgendamento(true);
		salaRepository.save(sala03);

        Sala sala04 = new Sala();
		sala04.setNome("A4");
		sala04.setDescricao("Sala A4");
		sala04.setCapacidade(50);
		sala04.setDistribuir(false);
		sala04.setPerfilSalaTurma(perfilSalaTurmaConvencional);
		sala04.setPredio(predioSetorAulasIV);
		sala04.setTipoQuadro("Negro");
		sala04.setUtilizarNaDistribuicao(false);
		sala04.setUtilizarNoAgendamento(true);
		salaRepository.save(sala04);

        quintaSalaCadastrada = new Sala();
		quintaSalaCadastrada.setNome("A5");
		quintaSalaCadastrada.setDescricao("Sala A5");
		quintaSalaCadastrada.setCapacidade(50);
		quintaSalaCadastrada.setDistribuir(false);
		quintaSalaCadastrada.setPerfilSalaTurma(perfilSalaTurmaConvencional);
		quintaSalaCadastrada.setPredio(predioSetorAulasIV);
		quintaSalaCadastrada.setTipoQuadro("Negro");
		quintaSalaCadastrada.setUtilizarNaDistribuicao(false);
		quintaSalaCadastrada.setUtilizarNoAgendamento(true);
		salaRepository.save(quintaSalaCadastrada);

		quantidadeSalasCadastradas = (int) salaRepository.count();
	}

    private void criarPredioDoSetorDeAulasIV(){
        predioSetorAulasIV = new Predio();
		predioSetorAulasIV.setNome("Setor de Aulas IV");
		predioSetorAulasIV.setDescricao("Setor de Aulas IV");
		predioRepository.save(predioSetorAulasIV);
    }

    private void criarPerfilSalaTurmaConvencional(){
		perfilSalaTurmaConvencional = new PerfilSalaTurma();
		perfilSalaTurmaConvencional.setNome("Convencional");
		perfilSalaTurmaConvencional.setDescricao("Perfil para aulas convencionais");
		perfilSalaTurmaRepository.save(perfilSalaTurmaConvencional);
	}
    
}
