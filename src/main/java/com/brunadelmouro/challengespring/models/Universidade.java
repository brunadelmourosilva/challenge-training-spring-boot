package com.brunadelmouro.challengespring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Universidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String sigla;

    @ManyToMany
    @JoinTable(name = "UNIVERSIDADE_HAS_CURSO",
                joinColumns = @JoinColumn(name = "universidade_id"), //lado dominante
                inverseJoinColumns = @JoinColumn(name = "curso_id")) //lado domidado
    private List<Curso> cursos = new ArrayList<>();

    @OneToMany(mappedBy = "universidade")
    private List<Aluno> alunos = new ArrayList<>();

    public Universidade() {
    }

    public Universidade(final Integer id, final String nome, final String sigla) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(final String sigla) {
        this.sigla = sigla;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(final List<Curso> cursos) {
        this.cursos = cursos;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(final List<Aluno> alunos) {
        this.alunos = alunos;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Universidade curso = (Universidade) o;
        return Objects.equals(id, curso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
