package com.brunadelmouro.challengespring.mappers;

import com.brunadelmouro.challengespring.dto.AlunoResponseDTO;
import com.brunadelmouro.challengespring.models.Aluno;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {

    ModelMapper modelMapper;
    CursoMapper cursoMapper;
    UniversidadeMapper universidadeMapper;

    public AlunoMapper(final ModelMapper modelMapper, final CursoMapper cursoMapper, final UniversidadeMapper universidadeMapper) {
        this.modelMapper = modelMapper;
        this.cursoMapper = cursoMapper;
        this.universidadeMapper = universidadeMapper;
    }

    public AlunoResponseDTO domainToResponseDTO(Aluno aluno){
        TypeMap<Aluno, AlunoResponseDTO> typeMap = modelMapper.getTypeMap(Aluno.class, AlunoResponseDTO.class);

        if(typeMap == null){
            typeMap = modelMapper.createTypeMap(Aluno.class, AlunoResponseDTO.class);
        }

        AlunoResponseDTO alunoResponseDTO = modelMapper.map(aluno, AlunoResponseDTO.class);

        //------------------------------------------------------------

        alunoResponseDTO.setCurso(cursoMapper.domainToResponseDTO(aluno.getCurso()));

        alunoResponseDTO.setUniversidade(universidadeMapper.domainToResponseDTO(aluno.getUniversidade()));

        return alunoResponseDTO;
    }
}