package com.example.selenium;


import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Google Chrome Desktop
 */
class ChromeTest {

  private WebDriver driver;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.chromedriver().setup();
  }

  @BeforeEach
  void before() {
    driver = new ChromeDriver();
  }

  @AfterEach
  void after() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void testGet() {
    driver.get("http://example.selenium.jp/reserveApp_Renewal/");

    assertEquals("予約情報入力", driver.getTitle());

    ReservePage reservePage = new ReservePage(driver);

    reservePage.agreeAndGotoNext();

    assertEquals("お名前が指定されていません", reservePage.getErrorMessage());
  }

}
