import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.URL;
import java.util.logging.Logger;

import static com.zukovs.HelperMethods.loginAndGetMainWindow;
import static com.zukovs.HelperMethods.userIsInLoggedInList;

public class LoginAndOnlineTest {
    private static final Logger log = Logger.getLogger(LoginAndOnlineTest.class.getName());
    @Test
    public void loginAndOnlineTest() throws InterruptedException {
        URL chromeDriverExe = LoginAndOnlineTest.class.getClassLoader().getResource("chromedriver.exe");
        if (chromeDriverExe != null) {
            System.setProperty("webdriver.chrome.driver", chromeDriverExe.getPath());
        } else {
            Assertions.fail("Failure with 'chromedriver.exe'. Verify that it exists.");
        }

        WebDriver driverForFirstUser = new ChromeDriver();
        driverForFirstUser.get("https://dev.spelezoli.lv/");
        String firstUserMail = "andris.garkalns@khoros.com";
        String firstUserPass = "Qweytr$123";
        String firstUserName = "Khoros B";
        String mainWindowFirstUser = loginAndGetMainWindow(driverForFirstUser, firstUserMail, firstUserPass, "dev");

        WebDriver driverForSecondUser = new ChromeDriver();
        driverForSecondUser.get("https://spelezoli.lv/");
        String secondUserMail = "anna_ezddttv_liepina@tfbnw.net";
        String secondUserPass = "testuser";
        String secondUserName = "Anna L";
        String mainWindowSecondUser = loginAndGetMainWindow(driverForSecondUser, secondUserMail, secondUserPass, "prod");

        WebDriver driverForThirdUser = new ChromeDriver();
        driverForThirdUser.get("https://spelezoli.lv/");
        String thirdUserMail = "janis_ojfexdv_berzins@tfbnw.net";
        String thirdUserPass = "testuser";
        String thirdUserName = "Jānis B";
        String mainWindowThirdUser = loginAndGetMainWindow(driverForThirdUser, thirdUserMail, thirdUserPass, "prod");

        //verify all users logged in and visible online
        if (userIsInLoggedInList(driverForFirstUser, secondUserName) && userIsInLoggedInList(driverForFirstUser, thirdUserName))
        {
            log.info("First user sees both other users logged in.");
        } else {
            Assertions.fail("One of the users was not seen in online list.");
        }
        //todo - prod doesn't have this layout
//        if (userIsInLoggedInList(driverForSecondUser, firstUserName) && userIsInLoggedInList(driverForSecondUser, thirdUserName))
//        {
//            log.info("Second user sees both other users logged in.");
//        } else {
//            Assertions.fail("One of the users was not seen in online list.");
//        }
//        if (userIsInLoggedInList(driverForThirdUser, firstUserName) && userIsInLoggedInList(driverForThirdUser, secondUserName))
//        {
//            log.info("Third user sees both other users logged in.");
//        } else {
//            Assertions.fail("One of the users was not seen in online list.");
//        }

        driverForFirstUser.close();
        driverForSecondUser.close();
        driverForThirdUser.close();
        assert true;
    }
}
