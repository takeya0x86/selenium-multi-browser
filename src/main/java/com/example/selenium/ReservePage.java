package com.example.selenium;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ReservePage {

  private WebDriver driver;

  public ReservePage(WebDriver driver) {
    this.driver = driver;
  }

  public void inputReserveDate(LocalDate date, int term) {
    WebElement datePick = driver.findElement(By.id("datePick"));
    datePick.clear();
    datePick.sendKeys(date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
    Select termSelect = new Select(driver.findElement(By.id("reserve_term")));
    termSelect.selectByIndex(term);
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
