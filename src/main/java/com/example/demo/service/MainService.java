package com.example.demo.service;

import com.example.demo.service.SalaryCheckService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Service
public class MainService {
    private final SalaryCheckService salaryCheckService;

    public MainService(SalaryCheckService salaryCheckService) {
        this.salaryCheckService = salaryCheckService;
    }

    public void mainProccess() throws InterruptedException, IOException {

        try {
            salaryCheckService.openSalaryPageAndGetField(salaryCheckService.getDriver());
        } catch (Exception e) {
            salaryCheckService.openSalaryPage();
        }
    }


}
