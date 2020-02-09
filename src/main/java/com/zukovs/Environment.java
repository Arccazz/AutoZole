package com.zukovs;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.net.URL;

import static com.codeborne.selenide.Selenide.*;

public class Environment {
    private String prodURL = "https://spelezoli.lv/";
    private String devURL = "https://dev.spelezoli.lv/";
    private User currentUser;
    private WebDriver driver;

    public Environment(User user, Point pos) throws Exception {
        URL chromeDriverExe = Environment.class.getClassLoader().getResource("chromedriver.exe");
        if (chromeDriverExe != null) {
            System.setProperty("webdriver.chrome.driver", chromeDriverExe.getPath());
        } else {
            throw new Exception();
        }

        currentUser = user;
        driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        driver.manage().window().setSize(new Dimension(screenSize.width/2,screenSize.height/2));
        driver.manage().window().setPosition(pos);
        open(getProdURL());
        switchTo().window(1);
        Selenide.switchTo().window(1).findElement(By.xpath("//div[@id=\"loginform\"]//div[@class=\"clearfix form_row\"]//input[@id=\"email\"]")).sendKeys(currentUser.getMail());
        Selenide.switchTo().window(1).findElement(By.xpath("//div[@id=\"loginform\"]//div[@class=\"clearfix form_row\"]//input[@id=\"pass\"]")).sendKeys(currentUser.getPassword());
        Selenide.switchTo().window(1).findElement(By.xpath("//div[@id=\"loginform\"]//div[@class=\"form_row clearfix\"]//input[@name=\"login\"]")).click();
        switchTo().window(0);
    }

    public String getDevURL() {
        return devURL;
    }

    public String getProdURL() {
        return prodURL;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
