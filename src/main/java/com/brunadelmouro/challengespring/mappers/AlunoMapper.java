package com.brunadelmouro.challengespring.mappers;

import com.brunadelmouro.challengespring.dto.AlunoResponseDTO;
import com.brunadelmouro.challengespring.models.Aluno;
import com.brunadelmouro.challengespring.models.Curso;
import com.brunadelmouro.challengespring.repositories.CursoRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CursoMapper cursoMapper;

    @Autowired
    UniversidadeMapper universidadeMapper;

    public AlunoResponseDTO domainToResponseDTO(Aluno aluno){
        TypeMap<Aluno, AlunoResponseDTO> typeMap = modelMapper.getTypeMap(Aluno.class, AlunoResponseDTO.class);

        if(typeMap == null){
            typeMap = modelMapper.createTypeMap(Aluno.class, AlunoResponseDTO.class);
        }

        AlunoResponseDTO alunoResponseDTO = modelMapper.map(aluno, AlunoResponseDTO.class);

        //------------------------------------------------------------

        alunoResponseDTO.setMedia((aluno.getNota1() + aluno.getNota2() + aluno.getNota3()) / 3);

        alunoResponseDTO.setCurso(cursoMapper.domainToResponseDTO(aluno.getCurso()));

        alunoResponseDTO.setUniversidade(universidadeMapper.domainToResponseDTO(aluno.getUniversidade()));

        return alunoResponseDTO;
    }
}