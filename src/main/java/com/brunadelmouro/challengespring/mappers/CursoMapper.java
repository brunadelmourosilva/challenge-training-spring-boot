package com.brunadelmouro.challengespring.mappers;

import com.brunadelmouro.challengespring.dto.CursoResponseDTO;
import com.brunadelmouro.challengespring.models.Curso;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    ModelMapper modelMapper;

    public CursoMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CursoResponseDTO domainToResponseDTO(Curso curso){
        TypeMap<Curso, CursoResponseDTO> typeMap = modelMapper.getTypeMap(Curso.class, CursoResponseDTO.class);

        if(typeMap == null){
            typeMap = modelMapper.createTypeMap(Curso.class, CursoResponseDTO.class);
        }

        return modelMapper.map(curso, CursoResponseDTO.class);
    }
}
