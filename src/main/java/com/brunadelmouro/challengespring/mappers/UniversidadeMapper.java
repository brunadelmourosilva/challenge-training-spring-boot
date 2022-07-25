package com.brunadelmouro.challengespring.mappers;

import com.brunadelmouro.challengespring.dto.UniversidadeResponseDTO;
import com.brunadelmouro.challengespring.models.Universidade;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniversidadeMapper {

    @Autowired
    ModelMapper modelMapper;

    public UniversidadeResponseDTO domainToResponseDTO(Universidade universidade){
        TypeMap<Universidade, UniversidadeResponseDTO> typeMap = modelMapper.getTypeMap(Universidade.class, UniversidadeResponseDTO.class);

        if(typeMap == null){
            typeMap = modelMapper.createTypeMap(Universidade.class, UniversidadeResponseDTO.class);
        }

        return modelMapper.map(universidade, UniversidadeResponseDTO.class);
    }
}
