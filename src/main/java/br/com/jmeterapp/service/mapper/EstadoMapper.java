package br.com.jmeterapp.service.mapper;

import br.com.jmeterapp.domain.*;
import br.com.jmeterapp.service.dto.EstadoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Estado and its DTO EstadoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EstadoMapper {

    EstadoDTO estadoToEstadoDTO(Estado estado);

    List<EstadoDTO> estadosToEstadoDTOs(List<Estado> estados);

    Estado estadoDTOToEstado(EstadoDTO estadoDTO);

    List<Estado> estadoDTOsToEstados(List<EstadoDTO> estadoDTOs);
}
