package br.ufrn.ct.cronos;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import br.ufrn.ct.cronos.infrastructure.repository.CustomJpaRepositoryImpl;

// Por padrão, SimpleJpaRepository é a classe padrão de repositório JPA.. aqui alteramos para CustomJpaRepositoryImpl 
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
@EnableCaching
public class CronosApiApplication {

	public static void main(String[] args) {
		// Para nao depender do TimeZone do SO, configuro ele na própria aplicação
		// Configuração do TimeZone na própria aplicação, para não depender do TimeZone do SO
		TimeZone.setDefault(TimeZone.getTimeZone("America/Fortaleza"));
		SpringApplication.run(CronosApiApplication.class, args);
	}

}
