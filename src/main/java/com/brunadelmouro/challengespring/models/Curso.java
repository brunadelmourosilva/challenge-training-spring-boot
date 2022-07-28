package com.brunadelmouro.challengespring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CURSO")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CURSO_ID")
    private Integer id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "SIGLA")
    private String sigla;

    @OneToMany(mappedBy = "curso") //refêrencia com o campo curso na tabela de aluno
    private List<Aluno> alunos = new ArrayList<>();

    @ManyToMany(mappedBy = "cursos")
    private List<Universidade> universidades = new ArrayList<>();

    public Curso() {
    }

    public Curso(final Integer id, final String nome, final String sigla) {
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

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(final List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List<Universidade> getUniversidades() {
        return universidades;
    }

    public void setUniversidades(final List<Universidade> universidades) {
        this.universidades = universidades;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Curso curso = (Curso) o;
        return Objects.equals(id, curso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
