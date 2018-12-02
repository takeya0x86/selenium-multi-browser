package com.example.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * Microsoft Internet Explorer
 */
@EnabledOnOs(WINDOWS)
class InternetExplorerTest {

  private WebDriver driver;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.iedriver().arch32().setup();
  }

  @BeforeEach
  void before() {
    driver = new InternetExplorerDriver();
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
  }

}
