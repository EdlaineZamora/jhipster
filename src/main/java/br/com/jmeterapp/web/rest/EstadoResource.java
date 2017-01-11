package br.com.jmeterapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.jmeterapp.domain.Estado;

import br.com.jmeterapp.repository.EstadoRepository;
import br.com.jmeterapp.web.rest.util.HeaderUtil;
import br.com.jmeterapp.web.rest.util.PaginationUtil;
import br.com.jmeterapp.service.dto.EstadoDTO;
import br.com.jmeterapp.service.mapper.EstadoMapper;
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
 * REST controller for managing Estado.
 */
@RestController
@RequestMapping("/api")
public class EstadoResource {

    private final Logger log = LoggerFactory.getLogger(EstadoResource.class);
        
    @Inject
    private EstadoRepository estadoRepository;

    @Inject
    private EstadoMapper estadoMapper;

    /**
     * POST  /estados : Create a new estado.
     *
     * @param estadoDTO the estadoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new estadoDTO, or with status 400 (Bad Request) if the estado has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/estados",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EstadoDTO> createEstado(@RequestBody EstadoDTO estadoDTO) throws URISyntaxException {
        log.debug("REST request to save Estado : {}", estadoDTO);
        if (estadoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("estado", "idexists", "A new estado cannot already have an ID")).body(null);
        }
        Estado estado = estadoMapper.estadoDTOToEstado(estadoDTO);
        estado = estadoRepository.save(estado);
        EstadoDTO result = estadoMapper.estadoToEstadoDTO(estado);
        return ResponseEntity.created(new URI("/api/estados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("estado", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estados : Updates an existing estado.
     *
     * @param estadoDTO the estadoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated estadoDTO,
     * or with status 400 (Bad Request) if the estadoDTO is not valid,
     * or with status 500 (Internal Server Error) if the estadoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/estados",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EstadoDTO> updateEstado(@RequestBody EstadoDTO estadoDTO) throws URISyntaxException {
        log.debug("REST request to update Estado : {}", estadoDTO);
        if (estadoDTO.getId() == null) {
            return createEstado(estadoDTO);
        }
        Estado estado = estadoMapper.estadoDTOToEstado(estadoDTO);
        estado = estadoRepository.save(estado);
        EstadoDTO result = estadoMapper.estadoToEstadoDTO(estado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("estado", estadoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estados : get all the estados.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of estados in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/estados",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EstadoDTO>> getAllEstados(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Estados");
        Page<Estado> page = estadoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/estados");
        return new ResponseEntity<>(estadoMapper.estadosToEstadoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /estados/:id : get the "id" estado.
     *
     * @param id the id of the estadoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the estadoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/estados/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EstadoDTO> getEstado(@PathVariable Long id) {
        log.debug("REST request to get Estado : {}", id);
        Estado estado = estadoRepository.findOne(id);
        EstadoDTO estadoDTO = estadoMapper.estadoToEstadoDTO(estado);
        return Optional.ofNullable(estadoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /estados/:id : delete the "id" estado.
     *
     * @param id the id of the estadoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/estados/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEstado(@PathVariable Long id) {
        log.debug("REST request to delete Estado : {}", id);
        estadoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("estado", id.toString())).build();
    }

}
