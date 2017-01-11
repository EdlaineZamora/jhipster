package br.com.jmeterapp.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Estado entity.
 */
public class EstadoDTO implements Serializable {

    private Long id;

    private String nome;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EstadoDTO estadoDTO = (EstadoDTO) o;

        if ( ! Objects.equals(id, estadoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EstadoDTO{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            '}';
    }
}
