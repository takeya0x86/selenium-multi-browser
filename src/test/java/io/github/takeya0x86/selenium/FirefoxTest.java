package io.github.takeya0x86.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Firefox Desktop
 */
class FirefoxTest {

  private WebDriver driver;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.firefoxdriver().setup();
  }

  @BeforeEach
  void before() {
    driver = new FirefoxDriver();
  }

  @AfterEach
  void after() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void testGet() {
    driver.get("https://getbootstrap.com/");

    assertTrue(driver.getTitle().contains("Bootstrap"));
  }

}
