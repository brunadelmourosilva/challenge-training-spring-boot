package com.brunadelmouro.challengespring.models;

import com.brunadelmouro.challengespring.enums.Status;

import javax.persistence.*;

@Entity
@Table(name = "JOB")
public class Job {

    @Column(name = "JOB_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NOME_ARQUIVO")
    private String nomeArquivo;

    @Column(name = "URL_ARQUIVO")
    private String urlArquivo;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Job() {
    }

    public Job(final Integer id, final String nomeArquivo, final String urlArquivo, final Status status) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.urlArquivo = urlArquivo;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(final String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getUrlArquivo() {
        return urlArquivo;
    }

    public void setUrlArquivo(final String urlArquivo) {
        this.urlArquivo = urlArquivo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }
}
