package com.example.demo.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class SalaryCheckService {
    private final String firstUrl = "https://app.dakika.com.tr/payroll/protected/lessright/commands.xhtml?selectedCommandName=ViewEmploymentCommand&selectedItem=120712577&activeItem=120712577&actionMethod=protected%2Flessright%2Fcommands.xhtml%3AcommandsAction.navigateToCommand%28%29&cid=3488";
    private WebDriver driver;

    private Map<String, String> values;

    public void openSalaryPage() throws InterruptedException, IOException {
        System.out.println("OPEN CHOME CALISTI");
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = getDriver();
        driver.get("https://login.dakika.com.tr/login");
        WebElement username = driver.findElement(By.xpath("//*[@id=\"username\"]"));
        WebElement password = driver.findElement(By.xpath("//*[@id=\"password\"]"));
        username.sendKeys(getValues().get("username"));
        password.sendKeys(getValues().get("password"));
        Thread.sleep(2000);
        WebElement submit = driver.findElement(By.xpath("//*[@id=\"btn\"]"));
        submit.click();
        Thread.sleep(2000);
        driver.navigate().to(firstUrl);
        Thread.sleep(2000);
        openSalaryPageAndGetField(driver);

    }


    public String openSalaryPageAndGetField(WebDriver driver) throws InterruptedException, IOException {
        driver = getDriver();
        refreshPage(driver);
        Thread.sleep(2000);
        WebElement salaryPage = driver.findElement(By.xpath("/html/body/span/div[2]/div/div[2]/div/div/table/tbody/tr/td[1]/form/div/div/table[2]/tbody/tr[1]/td/table/tbody/tr/td[4]/table/tbody/tr/td[2]/table/tbody/tr/td"));
        salaryPage.click();
        WebElement salaryField = driver.findElement(By.xpath("/html/body/span/div[2]/div/div[2]/div/div/table/tbody/tr/td[1]/form/div/div/table[2]/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr[5]/td[2]"));
        System.out.println(salaryField.getText());
        System.out.println("REFRESH CALISTI");
        if (isSalaryChanged(salaryField.getText().replace(".","").replace(",",""))) {
            toSms("+905454364778",salaryField.getText());
            System.out.println("SALARY UPDATE SMS  SEND");
        }

        return salaryField.getText();
    }

    private void refreshPage(WebDriver driver) {
        driver.navigate().refresh();
    }

    public WebDriver getDriver() {
        if (driver == null) {
            driver = new ChromeDriver();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            System.out.println("YENI NESNE OLUSTU");
        }
        return driver;
    }

    public Map<String, String> getValues() throws IOException {
        if (values == null) {
            values=new HashMap<>();
            Properties prop = new Properties();
            prop.load(new FileInputStream("src/main/java/config.properties"));
            values.put("username", prop.getProperty("username"));
            values.put("password", prop.getProperty("password"));
        }
        return values;
    }

    private void toSms(String toNumber,String salaryNew) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("config.properties"));

        System.out.println(prop.getProperty("accountSid"));
        System.out.println(prop.getProperty("AuthToken"));

        Twilio.init(prop.getProperty("accountSid"), prop.getProperty("AuthToken"));

        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(toNumber),
                        new com.twilio.type.PhoneNumber(prop.getProperty("phoneNumber")),//prop.getProperty("accountSid")
                        "Salary Update new salary is : " + salaryNew)
                .create();
    }


    public Boolean isSalaryChanged(String salary) {
        if (salary.isBlank()) return false;
        Integer oldSalary=198125;
        Integer salaryInt= Integer.valueOf(salary);
        if(salaryInt>0 && salaryInt>oldSalary) return true;
        else return false;
    }

}
