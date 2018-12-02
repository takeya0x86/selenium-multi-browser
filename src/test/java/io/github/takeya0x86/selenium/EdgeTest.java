package io.github.takeya0x86.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

/**
 * Microsoft Edge
 */
@EnabledOnOs(WINDOWS)
class EdgeTest {

  private WebDriver driver;

  @BeforeEach
  void before() {
    driver = new EdgeDriver();
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
