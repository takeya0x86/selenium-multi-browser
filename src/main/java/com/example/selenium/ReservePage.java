package com.example.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ReservePage {

  private WebDriver driver;

  public ReservePage(WebDriver driver) {
    this.driver = driver;
  }

  public void inputGuestName(String guestName) {
    WebElement guestNameInput = driver.findElement(By.id("guestname"));
    guestNameInput.clear();
    guestNameInput.sendKeys(guestName);
  }

  public void agreeAndGotoNext() {
    WebElement agreeAndGotoNextButton = driver.findElement(By.id("agree_and_goto_next"));
    agreeAndGotoNextButton.click();
  }

  public String getErrorMessage() {
    WebElement errorCheckResult = driver.findElement(By.id("errorcheck_result"));
    return errorCheckResult.getText();
  }

  public void returnToIndex() {
    WebElement returnToIndexButton = driver.findElement(By.id("returnto_index"));
    returnToIndexButton.click();
  }
}
