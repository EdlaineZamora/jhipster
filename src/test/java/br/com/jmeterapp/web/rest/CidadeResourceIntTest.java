package br.com.jmeterapp.web.rest;

import br.com.jmeterapp.JmeterappApp;
import br.com.jmeterapp.domain.Cidade;
import br.com.jmeterapp.repository.CidadeRepository;
import br.com.jmeterapp.service.dto.CidadeDTO;
import br.com.jmeterapp.service.mapper.CidadeMapper;

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
 * Test class for the CidadeResource REST controller.
 *
 * @see CidadeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmeterappApp.class)
public class CidadeResourceIntTest {
    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";

    @Inject
    private CidadeRepository cidadeRepository;

    @Inject
    private CidadeMapper cidadeMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCidadeMockMvc;

    private Cidade cidade;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CidadeResource cidadeResource = new CidadeResource();
        ReflectionTestUtils.setField(cidadeResource, "cidadeRepository", cidadeRepository);
        ReflectionTestUtils.setField(cidadeResource, "cidadeMapper", cidadeMapper);
        this.restCidadeMockMvc = MockMvcBuilders.standaloneSetup(cidadeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cidade createEntity(EntityManager em) {
        Cidade cidade = new Cidade();
        cidade = new Cidade()
                .nome(DEFAULT_NOME);
        return cidade;
    }

    @Before
    public void initTest() {
        cidade = createEntity(em);
    }

    @Test
    @Transactional
    public void createCidade() throws Exception {
        int databaseSizeBeforeCreate = cidadeRepository.findAll().size();

        // Create the Cidade
        CidadeDTO cidadeDTO = cidadeMapper.cidadeToCidadeDTO(cidade);

        restCidadeMockMvc.perform(post("/api/cidades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cidadeDTO)))
                .andExpect(status().isCreated());

        // Validate the Cidade in the database
        List<Cidade> cidades = cidadeRepository.findAll();
        assertThat(cidades).hasSize(databaseSizeBeforeCreate + 1);
        Cidade testCidade = cidades.get(cidades.size() - 1);
        assertThat(testCidade.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void getAllCidades() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get all the cidades
        restCidadeMockMvc.perform(get("/api/cidades?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cidade.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getCidade() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);

        // Get the cidade
        restCidadeMockMvc.perform(get("/api/cidades/{id}", cidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cidade.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCidade() throws Exception {
        // Get the cidade
        restCidadeMockMvc.perform(get("/api/cidades/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCidade() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);
        int databaseSizeBeforeUpdate = cidadeRepository.findAll().size();

        // Update the cidade
        Cidade updatedCidade = cidadeRepository.findOne(cidade.getId());
        updatedCidade
                .nome(UPDATED_NOME);
        CidadeDTO cidadeDTO = cidadeMapper.cidadeToCidadeDTO(updatedCidade);

        restCidadeMockMvc.perform(put("/api/cidades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cidadeDTO)))
                .andExpect(status().isOk());

        // Validate the Cidade in the database
        List<Cidade> cidades = cidadeRepository.findAll();
        assertThat(cidades).hasSize(databaseSizeBeforeUpdate);
        Cidade testCidade = cidades.get(cidades.size() - 1);
        assertThat(testCidade.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void deleteCidade() throws Exception {
        // Initialize the database
        cidadeRepository.saveAndFlush(cidade);
        int databaseSizeBeforeDelete = cidadeRepository.findAll().size();

        // Get the cidade
        restCidadeMockMvc.perform(delete("/api/cidades/{id}", cidade.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Cidade> cidades = cidadeRepository.findAll();
        assertThat(cidades).hasSize(databaseSizeBeforeDelete - 1);
    }
}
