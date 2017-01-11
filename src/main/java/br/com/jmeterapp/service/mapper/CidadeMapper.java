package br.com.jmeterapp.service.mapper;

import br.com.jmeterapp.domain.*;
import br.com.jmeterapp.service.dto.CidadeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Cidade and its DTO CidadeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CidadeMapper {

    @Mapping(source = "estado.id", target = "estadoId")
    @Mapping(source = "estado.nome", target = "estadoNome")
    CidadeDTO cidadeToCidadeDTO(Cidade cidade);

    List<CidadeDTO> cidadesToCidadeDTOs(List<Cidade> cidades);

    @Mapping(source = "estadoId", target = "estado")
    Cidade cidadeDTOToCidade(CidadeDTO cidadeDTO);

    List<Cidade> cidadeDTOsToCidades(List<CidadeDTO> cidadeDTOs);

    default Estado estadoFromId(Long id) {
        if (id == null) {
            return null;
        }
        Estado estado = new Estado();
        estado.setId(id);
        return estado;
    }
}
