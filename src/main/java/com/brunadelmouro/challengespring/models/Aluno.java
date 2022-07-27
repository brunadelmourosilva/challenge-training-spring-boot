package com.brunadelmouro.challengespring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String matricula;
    private Date dataMatricula;
    private String nome;
    private Double nota1;
    private Double nota2;
    private Double nota3;

    private Double media;

    @ManyToOne
    @JoinColumn(name = "curso_id", referencedColumnName = "cursoId") //foreign key
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "universidade_id")
    private Universidade universidade;

    public Aluno() {
    }

    public Aluno(final Integer id, final String matricula, final Date dataMatricula, final String nome, final Double nota1, final Double nota2, final Double nota3) {
        this.id = id;
        this.matricula = matricula;
        this.dataMatricula = dataMatricula;
        this.nome = nome;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(final String matricula) {
        this.matricula = matricula;
    }

    public Date getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(final Date dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public Double getNota1() {
        return nota1;
    }

    public void setNota1(final Double nota1) {
        this.nota1 = nota1;
    }

    public Double getNota2() {
        return nota2;
    }

    public void setNota2(final Double nota2) {
        this.nota2 = nota2;
    }

    public Double getNota3() {
        return nota3;
    }

    public void setNota3(final Double nota3) {
        this.nota3 = nota3;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(final Curso curso) {
        this.curso = curso;
    }

    public Universidade getUniversidade() {
        return universidade;
    }

    public void setUniversidade(final Universidade universidade) {
        this.universidade = universidade;
    }

    public Double getMedia() {
        return media;
    }

    public void setMedia(final Double media) {
        this.media = media;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Aluno aluno = (Aluno) o;
        return Objects.equals(id, aluno.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
