package com.zukovs.views;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class Landing {
    public SelenideElement gameSelector = $x("//button[@class='game-type-select dropdown-toggle btn btn-secondary']");
    public SelenideElement privateGameCheckbox = $x("//label[@class = 'game-type-input']//div[@class='game-type-text' and text()='Privāta']//preceding-sibling::span[@class='checkmark']");
    public SelenideElement beginGameButton = $x("//button[@class='start-game-button btn btn-link']");

    public SelenideElement roomPasswordDigit1 = $x("//div[@class='room-password-digit'][1]");
    public SelenideElement roomPasswordDigit2 = $x("//div[@class='room-password-digit'][2]");
    public SelenideElement roomPasswordDigit3 = $x("//div[@class='room-password-digit'][3]");
    public SelenideElement roomPasswordDigit4 = $x("//div[@class='room-password-digit'][4]");
    public SelenideElement notificationCloseButton = $x("//button[@class='btn notification-footer-button btn btn-secondary'][1]");

    public SelenideElement joinGameButton;

    public SelenideElement privateGamePasswordInputDigit1 = $x("//input[@id='digit1']");
    public SelenideElement privateGamePasswordInputDigit2 = $x("//input[@id='digit2']");
    public SelenideElement privateGamePasswordInputDigit3 = $x("//input[@id='digit3']");
    public SelenideElement privateGamePasswordInputDigit4 = $x("//input[@id='digit4']");

    public SelenideElement playerNameNode = $x("//*[@class='menu-player-name']");
    public SelenideElement playerBalanceNode = $x("//*[@class='menu-player-balance-text']");

    public void setPrivateGameRowByInitiatorName(String firstnameAndLastnameInitial) {
        joinGameButton = $x("//tbody[@class='rooms-table-body']//tr[descendant::div[@class='rooms-table-player-name' and text()='"+firstnameAndLastnameInitial+"']]//button");
    }
}
