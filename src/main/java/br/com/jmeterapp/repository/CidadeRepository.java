package br.com.jmeterapp.repository;

import br.com.jmeterapp.domain.Cidade;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cidade entity.
 */
@SuppressWarnings("unused")
public interface CidadeRepository extends JpaRepository<Cidade,Long> {

}
