package com.brunadelmouro.challengespring.dto;

import com.brunadelmouro.challengespring.models.Curso;
import com.brunadelmouro.challengespring.models.Universidade;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AlunoResponseDTO {

    private String nome;
    private Double media;

    private CursoResponseDTO curso;
    private UniversidadeResponseDTO universidade;

    public AlunoResponseDTO() {
    }

    public AlunoResponseDTO(final String nome, final Double media, final CursoResponseDTO curso, final UniversidadeResponseDTO universidade) {
        this.nome = nome;
        this.media = media;
        this.curso = curso;
        this.universidade = universidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public Double getMedia() {
        return media;
    }

    public CursoResponseDTO getCurso() {
        return curso;
    }

    public void setCurso(final CursoResponseDTO curso) {
        this.curso = curso;
    }

    public void setMedia(final Double media) {
        this.media = media;
    }

    public UniversidadeResponseDTO getUniversidade() {
        return universidade;
    }

    public void setUniversidade(final UniversidadeResponseDTO universidade) {
        this.universidade = universidade;
    }
}
