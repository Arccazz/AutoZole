import org.junit.jupiter.api.Test;
        import org.openqa.selenium.By;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.chrome.ChromeDriver;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ExampleTest {
    @Test
    public void example() throws InterruptedException {
        int retriesToGetWindow = 100;
        String mainWindowString = "";
        String loginWindowString = "";
        //todo - rename to actual test user
        String testUserName = "";
        String testUserPass = "";

        URL chromeDriverExe = ExampleTest.class.getClassLoader().getResource("chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", chromeDriverExe.getPath());

        WebDriver driver = new ChromeDriver();
        driver.get("https://dev.spelezoli.lv/");
        Set<String> windows;
        do {
            windows = driver.getWindowHandles();
            retriesToGetWindow--;
            TimeUnit.MILLISECONDS.sleep(100);
        } while (windows.size() < 2 && retriesToGetWindow > 0);

        for (String window : windows) {
            driver.switchTo().window(window);
            System.out.println(driver.getCurrentUrl());
            if (driver.getCurrentUrl().startsWith("https://dev.spelezoli")) {
                mainWindowString = window;
            }
            if (driver.getCurrentUrl().startsWith("https://www.facebook")) {
                loginWindowString = window;
                WebElement email = driver.findElement(By.id("email"));
                email.sendKeys(testUserName);
                WebElement pass = driver.findElement(By.id("pass"));
                pass.sendKeys(testUserPass);
                WebElement loginButton = driver.findElement(By.name("login"));
                loginButton.click();
            }
        }
        driver.switchTo().window(mainWindowString);
        //todo - verify landing window open.
        //todo - join specific game
        //todo - execute specific cases...
    }
}
