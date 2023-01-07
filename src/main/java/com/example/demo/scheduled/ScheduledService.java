package com.example.demo.scheduled;

import com.example.demo.service.MainService;
import com.example.demo.service.SalaryCheckService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@EnableScheduling
@Component
public class ScheduledService {
    private final SalaryCheckService service;
    private final MainService mainService;

    public ScheduledService(SalaryCheckService service, MainService mainService) {
        this.service = service;
        this.mainService = mainService;
    }

    @Scheduled(fixedRate = 50000, initialDelay = 1000)
    private void prints() throws InterruptedException, IOException {
        mainService.mainProccess();
    }
}
