package br.com.jmeterapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.jmeterapp.domain.Cidade;

import br.com.jmeterapp.repository.CidadeRepository;
import br.com.jmeterapp.web.rest.util.HeaderUtil;
import br.com.jmeterapp.web.rest.util.PaginationUtil;
import br.com.jmeterapp.service.dto.CidadeDTO;
import br.com.jmeterapp.service.mapper.CidadeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Cidade.
 */
@RestController
@RequestMapping("/api")
public class CidadeResource {

    private final Logger log = LoggerFactory.getLogger(CidadeResource.class);
        
    @Inject
    private CidadeRepository cidadeRepository;

    @Inject
    private CidadeMapper cidadeMapper;

    /**
     * POST  /cidades : Create a new cidade.
     *
     * @param cidadeDTO the cidadeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cidadeDTO, or with status 400 (Bad Request) if the cidade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cidades",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CidadeDTO> createCidade(@RequestBody CidadeDTO cidadeDTO) throws URISyntaxException {
        log.debug("REST request to save Cidade : {}", cidadeDTO);
        if (cidadeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cidade", "idexists", "A new cidade cannot already have an ID")).body(null);
        }
        Cidade cidade = cidadeMapper.cidadeDTOToCidade(cidadeDTO);
        cidade = cidadeRepository.save(cidade);
        CidadeDTO result = cidadeMapper.cidadeToCidadeDTO(cidade);
        return ResponseEntity.created(new URI("/api/cidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cidade", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cidades : Updates an existing cidade.
     *
     * @param cidadeDTO the cidadeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cidadeDTO,
     * or with status 400 (Bad Request) if the cidadeDTO is not valid,
     * or with status 500 (Internal Server Error) if the cidadeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cidades",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CidadeDTO> updateCidade(@RequestBody CidadeDTO cidadeDTO) throws URISyntaxException {
        log.debug("REST request to update Cidade : {}", cidadeDTO);
        if (cidadeDTO.getId() == null) {
            return createCidade(cidadeDTO);
        }
        Cidade cidade = cidadeMapper.cidadeDTOToCidade(cidadeDTO);
        cidade = cidadeRepository.save(cidade);
        CidadeDTO result = cidadeMapper.cidadeToCidadeDTO(cidade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cidade", cidadeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cidades : get all the cidades.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cidades in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cidades",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CidadeDTO>> getAllCidades(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cidades");
        Page<Cidade> page = cidadeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cidades");
        return new ResponseEntity<>(cidadeMapper.cidadesToCidadeDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /cidades/:id : get the "id" cidade.
     *
     * @param id the id of the cidadeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cidadeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cidades/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CidadeDTO> getCidade(@PathVariable Long id) {
        log.debug("REST request to get Cidade : {}", id);
        Cidade cidade = cidadeRepository.findOne(id);
        CidadeDTO cidadeDTO = cidadeMapper.cidadeToCidadeDTO(cidade);
        return Optional.ofNullable(cidadeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cidades/:id : delete the "id" cidade.
     *
     * @param id the id of the cidadeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cidades/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCidade(@PathVariable Long id) {
        log.debug("REST request to delete Cidade : {}", id);
        cidadeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cidade", id.toString())).build();
    }

}
