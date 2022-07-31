package com.brunadelmouro.challengespring.service.impl;

import com.brunadelmouro.challengespring.enums.Status;
import com.brunadelmouro.challengespring.repositories.JobRepository;
import com.brunadelmouro.challengespring.service.AlunoService;
import com.brunadelmouro.challengespring.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class JobServiceImpl implements JobService {

    JobRepository jobRepository;
    AlunoService alunoService;

    public JobServiceImpl(final JobRepository jobRepository, final AlunoService alunoService) {
        this.jobRepository = jobRepository;
        this.alunoService = alunoService;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    @Override
    public void saveSheetToArchiveInformation() {
        log.info("Iniciando processamento de jobs...");

        final var arquivos = jobRepository.findByStatus(Status.AGUARDANDO_PROCESSAMENTO);

        if(arquivos.isEmpty()){
            log.info("Não há arquivos para processar...");
        } else{
            arquivos.forEach(arquivo -> {
                    alunoService.importSheetToDatabase(arquivo);
                    arquivo.setStatus(Status.PROCESSADO);
                    jobRepository.save(arquivo);
            });
            log.info("Processamento conluído");
        }
    }
}
