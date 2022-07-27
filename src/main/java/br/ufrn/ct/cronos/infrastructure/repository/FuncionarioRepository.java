package br.ufrn.ct.cronos.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.ct.cronos.domain.model.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findByIdSigaaFuncionario(Long idSigaaFuncionario);
    
}

