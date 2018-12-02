package com.example.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.condition.OS.MAC;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Safari Desktop
 */
@EnabledOnOs(MAC)
class SafariTest {

  private WebDriver driver;

  @BeforeEach
  void before() {
    driver = new SafariDriver();
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
