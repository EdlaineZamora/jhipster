package br.com.jmeterapp.web.rest;

import br.com.jmeterapp.JmeterappApp;
import br.com.jmeterapp.domain.Estado;
import br.com.jmeterapp.repository.EstadoRepository;
import br.com.jmeterapp.service.dto.EstadoDTO;
import br.com.jmeterapp.service.mapper.EstadoMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EstadoResource REST controller.
 *
 * @see EstadoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmeterappApp.class)
public class EstadoResourceIntTest {
    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";

    @Inject
    private EstadoRepository estadoRepository;

    @Inject
    private EstadoMapper estadoMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEstadoMockMvc;

    private Estado estado;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EstadoResource estadoResource = new EstadoResource();
        ReflectionTestUtils.setField(estadoResource, "estadoRepository", estadoRepository);
        ReflectionTestUtils.setField(estadoResource, "estadoMapper", estadoMapper);
        this.restEstadoMockMvc = MockMvcBuilders.standaloneSetup(estadoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estado createEntity(EntityManager em) {
        Estado estado = new Estado();
        estado = new Estado()
                .nome(DEFAULT_NOME);
        return estado;
    }

    @Before
    public void initTest() {
        estado = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstado() throws Exception {
        int databaseSizeBeforeCreate = estadoRepository.findAll().size();

        // Create the Estado
        EstadoDTO estadoDTO = estadoMapper.estadoToEstadoDTO(estado);

        restEstadoMockMvc.perform(post("/api/estados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estadoDTO)))
                .andExpect(status().isCreated());

        // Validate the Estado in the database
        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeCreate + 1);
        Estado testEstado = estados.get(estados.size() - 1);
        assertThat(testEstado.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void getAllEstados() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get all the estados
        restEstadoMockMvc.perform(get("/api/estados?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(estado.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get the estado
        restEstadoMockMvc.perform(get("/api/estados/{id}", estado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(estado.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEstado() throws Exception {
        // Get the estado
        restEstadoMockMvc.perform(get("/api/estados/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();

        // Update the estado
        Estado updatedEstado = estadoRepository.findOne(estado.getId());
        updatedEstado
                .nome(UPDATED_NOME);
        EstadoDTO estadoDTO = estadoMapper.estadoToEstadoDTO(updatedEstado);

        restEstadoMockMvc.perform(put("/api/estados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estadoDTO)))
                .andExpect(status().isOk());

        // Validate the Estado in the database
        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeUpdate);
        Estado testEstado = estados.get(estados.size() - 1);
        assertThat(testEstado.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void deleteEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);
        int databaseSizeBeforeDelete = estadoRepository.findAll().size();

        // Get the estado
        restEstadoMockMvc.perform(delete("/api/estados/{id}", estado.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeDelete - 1);
    }
}
