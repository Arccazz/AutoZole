package com.zukovs.views;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class Room {
    private String cardString = "0,1,2,3,4,5,6,7";
    private String roomID;
    private String roomPassword;

    public SelenideElement roomExitButton = $x("//*[@class=\"top-left-exit\"]");
    public SelenideElement testPanelButton = $x("//button[text()='Testa panelis']");
    public SelenideElement timerToggle = $x("//button[contains(@class, 'test-panel') and contains(text(), 'taimeri')]");
    public SelenideElement testPanelClose = $x("//button[contains(@class, 'test-panel') and contains(text(), 'Aizvērt')]");
    public SelenideElement currentUserBalance = $x("//div[contains(@class, 'firstperson')]//div[@class=\"player-balance-text\"]");
    public SelenideElement currentUserName = $x("//div[contains(@class, 'firstperson')]//div[@class=\"player-name\"]");
    public SelenideElement newBalanceTextbox = $x("//input[@name=\"newBal\"]");
    public SelenideElement testPanelNewBalanceButton = $x("//button[contains(@class, 'test-panel') and contains(text(), 'Mainīt bilanci')]");
    public SelenideElement gameSelectionPassButton = $x("//button[contains(@class, 'selection') and contains(text(), 'Garām')]");
    public SelenideElement gameSelectionTakeButton = $x("//button[contains(@class, 'selection') and contains(text(), 'Ņemt')]");
    public SelenideElement gameSelectionZoleButton = $x("//button[contains(@class, 'selection') and contains(text(), 'Zole')]");
    public SelenideElement notificationCloseButton = $x("//button[contains(text(), 'Labi')]");
    public SelenideElement quitRoundButton = $x("//button[contains(@class, 'quit-round') and contains(text(), 'Atmesties')]");


    public String scoreTableLatestRowXPath = "//div[@class='score-table ']//tr[@class='score-table-row'][last()]";

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomID() {
        return roomID;
    }
}
