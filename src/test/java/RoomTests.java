import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.zukovs.Environment;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.zukovs.User;
import com.zukovs.views.Landing;
import com.zukovs.views.Room;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Selenide.*;


public class RoomTests {

    private Map<String, Boolean> testResults;

    private static final Logger log = Logger.getLogger(RoomTests.class.getName());
    private List<WebDriver> openDrivers = new ArrayList<>();
    private User firstUser;
    private User secondUser;
    private User thirdUser;
    private Environment firstUserEnv = null;
    private Environment secondUserEnv = null;
    private Environment thirdUserEnv = null;
    private Landing landingPageObject;
    private Room gameRoom;

    private WebDriver currentDriver;

    @Before
    public void setup() {
        testResults = new HashMap<>();
        firstUser = new User("anna_ezddttv_liepina@tfbnw.net", "testuser");
        secondUser = new User("janis_ojfexdv_berzins@tfbnw.net", "testuser");
        thirdUser = new User("sandra_rzopgvi_kaposta@tfbnw.net", "testuser");
        firstUserEnv = null;
        secondUserEnv = null;
        thirdUserEnv = null;
        try {
            java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            firstUserEnv = new Environment(firstUser, new Point(0,0));
            openDrivers.add(firstUserEnv.getDriver());
            secondUserEnv = new Environment(secondUser, new Point(screenSize.width/2,0));
            openDrivers.add(secondUserEnv.getDriver());
            thirdUserEnv = new Environment(thirdUser, new Point(0,screenSize.height/2));
            openDrivers.add(thirdUserEnv.getDriver());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to setup.");
        }
    }

    @Test
    public void joinRoom() {
        landingPageObject = new Landing();
        gameRoom = new Room();
        WebDriverRunner.setWebDriver(firstUserEnv.getDriver());
        currentDriver = firstUserEnv.getDriver();

        if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("zole/-")) {
            //exit current room
            $x("//*[@class=\"top-left-exit\"]").click();
            $x("//button[text()='Jā']").click();
        }

        //make sure private game is checked
        landingPageObject.gameSelector.click();
        if (!landingPageObject.privateGameCheckbox.isSelected()) {
            landingPageObject.privateGameCheckbox.click();
            landingPageObject.notificationCloseButton.click();
        } else {
            landingPageObject.gameSelector.click();
        }
        landingPageObject.beginGameButton.click();

        String password = "";
        password += landingPageObject.roomPasswordDigit1.text();
        password += landingPageObject.roomPasswordDigit2.text();
        password += landingPageObject.roomPasswordDigit3.text();
        password += landingPageObject.roomPasswordDigit4.text();
        landingPageObject.notificationCloseButton.click();
        gameRoom.setRoomPassword(password);

        landingPageObject.setPrivateGameRowByInitiatorName(getShortenedName(landingPageObject.playerNameNode.text()));

        //join room with other users
        joinPrivateGameWithUser(secondUserEnv, password);
        sleep(1000);
        joinPrivateGameWithUser(thirdUserEnv, password);
        sleep(2000);
        gameRoom.setRoomID(WebDriverRunner.getWebDriver().getCurrentUrl().split("https://spelezoli.lv/zole/")[1]);
        toggleRoomTimer();
        setBaselineBalanceAllUsers();

        //pseudo-test
        verifyCommonPuleAdded();

        //focus on user 2.
        WebDriverRunner.setWebDriver(secondUserEnv.getDriver());
        currentDriver = secondUserEnv.getDriver();
        verifyPersonalPuleAdded();

        int failureCount = 0;
        for (Map.Entry<String, Boolean> entry : testResults.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            if (value) {
                log.info("\tTest [" + key + "] passed");
            } else {
                log.severe("\tTest [" + key + "] failed, room: ["+gameRoom.getRoomID()+"]");
                failureCount++;
            }
        }
        if (failureCount>0) {
            Assert.fail("Failures exist, check logs.");
        }
    }

    private String getCurrentUserShortenedName() {
        return getShortenedName(gameRoom.currentUserName.text());
    }

    private String getShortenedName(String fullName) {
        String shortenedPlayerName = fullName.split(" ")[0];
        return shortenedPlayerName += " " + fullName.split(" ")[1].charAt(0);
    }

    private void joinPrivateGameWithUser(Environment user, String password) {
        WebDriverRunner.setWebDriver(user.getDriver());
        currentDriver = user.getDriver();
        landingPageObject.joinGameButton.click();
        landingPageObject.privateGamePasswordInputDigit1.setValue(String.valueOf(password.charAt(0)));
        landingPageObject.privateGamePasswordInputDigit2.setValue(String.valueOf(password.charAt(1)));
        landingPageObject.privateGamePasswordInputDigit3.setValue(String.valueOf(password.charAt(2)));
        landingPageObject.privateGamePasswordInputDigit4.setValue(String.valueOf(password.charAt(3)));
        landingPageObject.notificationCloseButton.click();
        if (landingPageObject.notificationCloseButton.exists()) {
            System.out.println();
        }
    }

    private int getCurrentUserBalance() {
        return Integer.parseInt(gameRoom.currentUserBalance.text());
    }

    private void setBaselineBalanceAllUsers() {
        WebDriverRunner.setWebDriver(firstUserEnv.getDriver());
        currentDriver = firstUserEnv.getDriver();
        gameRoom.testPanelButton.click();
        gameRoom.newBalanceTextbox.setValue("10000");
        gameRoom.testPanelNewBalanceButton.click();
        firstUser.setBalance(getCurrentUserBalance());
        gameRoom.testPanelClose.click();

        WebDriverRunner.setWebDriver(secondUserEnv.getDriver());
        currentDriver = secondUserEnv.getDriver();
        gameRoom.testPanelButton.click();
        gameRoom.newBalanceTextbox.setValue("10000");
        gameRoom.testPanelNewBalanceButton.click();
        secondUser.setBalance(getCurrentUserBalance());
        gameRoom.testPanelClose.click();

        WebDriverRunner.setWebDriver(thirdUserEnv.getDriver());
        currentDriver = thirdUserEnv.getDriver();
        gameRoom.testPanelButton.click();
        gameRoom.newBalanceTextbox.setValue("10000");
        gameRoom.testPanelNewBalanceButton.click();
        thirdUser.setBalance(getCurrentUserBalance());
        gameRoom.testPanelClose.click();
    }

    private void toggleRoomTimer() {
        gameRoom.testPanelButton.click();
        gameRoom.timerToggle.click();
        gameRoom.testPanelClose.click();
    }

    private void verifyCommonPuleAdded() {
        WebDriverRunner.setWebDriver(firstUserEnv.getDriver());
        currentDriver = firstUserEnv.getDriver();
        gameRoom.gameSelectionPassButton.click();
        WebDriverRunner.setWebDriver(secondUserEnv.getDriver());
        currentDriver = secondUserEnv.getDriver();
        gameRoom.gameSelectionPassButton.click();
        WebDriverRunner.setWebDriver(thirdUserEnv.getDriver());
        currentDriver = thirdUserEnv.getDriver();
        gameRoom.gameSelectionPassButton.click();

        confirmPopup();

        if ($x(gameRoom.scoreTableLatestRowXPath + "//div[text()=' P ']").exists()) {
            testResults.put("CommonPuleAddedCorrectly", true);
        } else {
            takeSnapshot(currentDriver, "CommonPuleAddedCorrectly");
            testResults.put("CommonPuleAddedCorrectly", false);
        }
    }

    private void verifyPersonalPuleAdded() {
        gameRoom.gameSelectionTakeButton.waitUntil(Condition.appears,10000).click();
        Actions actions = new Actions(secondUserEnv.getDriver());
        actions.moveToElement($x("//div[contains(@class, 'card ')]")).click().build().perform();
        sleep(1000);
        actions.moveToElement($x("//div[contains(@class, 'card ')]")).click().build().perform();
        gameRoom.quitRoundButton.click();

        confirmPopup();

        WebDriverRunner.setWebDriver(secondUserEnv.getDriver());
        currentDriver = secondUserEnv.getDriver();
        if ($x(gameRoom.scoreTableLatestRowXPath + "//div[text()='" + getCurrentUserShortenedName() + "']").exists()) {
            testResults.put("PersonalPuleAddedCorrectly", true);
        } else {
            takeSnapshot(currentDriver, "PersonalPuleAddedCorrectly");
            testResults.put("PersonalPuleAddedCorrectly", false);
        }

        if ($x(gameRoom.scoreTableLatestRowXPath + "//td[@class='score-table-col'][2]").text().equals("-8") &&
                $x(gameRoom.scoreTableLatestRowXPath + "//td[@class='score-table-col'][1]").text().equals("4") &&
                $x(gameRoom.scoreTableLatestRowXPath + "//td[@class='score-table-col'][3]").text().equals("4")
        ) {
            testResults.put("BigLossNoSticksPointsCalculatedCorrectly", true);
        } else {
            takeSnapshot(currentDriver, "BigLossNoSticksPointsCalculatedCorrectly");
            testResults.put("BigLossNoSticksPointsCalculatedCorrectly", false);
        }
    }

    private void takeSnapshot(WebDriver currentDriver, String filename) {
        TakesScreenshot scrShot =((TakesScreenshot) currentDriver);
        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File destination = new File("build/reports/tests/" + filename + ".png");
        try {
            FileUtils.copyFile(srcFile, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void confirmPopup() {
        sleep(700);
        SelenideElement button = gameRoom.notificationCloseButton;
        if (button.exists()) {
            button.click();
        }
        sleep(1500);
    }

    @After
    public void cleanup() {
        openDrivers.forEach(WebDriver::close);
    }
}
