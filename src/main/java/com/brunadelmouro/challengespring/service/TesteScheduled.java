package com.brunadelmouro.challengespring.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TesteScheduled {

    @Scheduled(fixedDelay = 5000)
    private void testaScheduled(){
        System.out.println("printa linha a cada 5 segundos");
    }
}
