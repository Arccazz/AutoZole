package com.zukovs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HelperMethods {
    private static final Logger log = Logger.getLogger(HelperMethods.class.getName());
    public static final String PROD_URL_START = "https://spelezoli";
    public static final String DEV_URL_START = "https://dev.spelezoli";
    public static final String FB_URL_START = "https://www.facebook";
    private static final String ONLINE_USER_TABLE = "online-users-table-body";
    private static final String LOBBY_BACKGROUND = "lobby-background";
    private static final String LOGIN_EMAIL_BOX = "email";
    private static final String LOGIN_PASS_BOX = "pass";
    private static final String LOGIN_BUTTON = "login";
    public static String loginAndGetMainWindow(WebDriver driver, String testUserMail, String testUserPass,
                                               String environment) throws InterruptedException {
        int retriesToGetWindow = 100;
        String mainWindowString = "";

        Set<String> windows;
        do {
            windows = driver.getWindowHandles();
            retriesToGetWindow--;
            TimeUnit.MILLISECONDS.sleep(100);
        } while (windows.size() < 2 && retriesToGetWindow > 0);
        if (windows.size() < 2) {
            log.severe("Failed to retrieve 2 windows (main and login).");
            return "";
        }

        for (String window : windows) {
            driver.switchTo().window(window);
            if ("dev".equals(environment)) {
                if (driver.getCurrentUrl().startsWith(DEV_URL_START)) {
                    mainWindowString = window;
                }
            } else if ("prod".equals(environment)){
                if (driver.getCurrentUrl().startsWith(PROD_URL_START)) {
                    mainWindowString = window;
                }
            } else {
                log.severe("Unsupported environment: " + environment);
                return "";
            }

            if (driver.getCurrentUrl().startsWith(FB_URL_START)) {
                WebElement email = driver.findElement(By.id(LOGIN_EMAIL_BOX));
                email.sendKeys(testUserMail);
                WebElement pass = driver.findElement(By.id(LOGIN_PASS_BOX));
                pass.sendKeys(testUserPass);
                WebElement loginButton = driver.findElement(By.name(LOGIN_BUTTON));
                loginButton.click();
            }
        }
        driver.switchTo().window(mainWindowString);
        TimeUnit.SECONDS.sleep(3);
        if (elementExists(driver, By.className(LOBBY_BACKGROUND))) {
            log.info("Logged in successfully.");
            return mainWindowString;
        } else {
            log.severe("Did not find main window element.");
            return "";
        }
    }

    public static boolean userIsInLoggedInList(WebDriver driver, String name) {
        WebElement onlineTable = driver.findElement(By.className(ONLINE_USER_TABLE));
        WebElement foundUser = onlineTable.findElement(By.xpath("//div[contains(text(),'"+name+"')]"));
        return foundUser.isDisplayed();
    }



    private static boolean elementExists(WebDriver driver, By type) {
        try {
            driver.findElement(type);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }
}
