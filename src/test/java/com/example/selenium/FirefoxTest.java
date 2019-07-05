package com.example.selenium;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Firefox Desktop
 */
class FirefoxTest {

  private FirefoxDriver driver;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.firefoxdriver().setup();
  }

  @BeforeEach
  void before() {
    FirefoxOptions options = new FirefoxOptions();
    options.setLogLevel(FirefoxDriverLogLevel.TRACE);
    driver = new FirefoxDriver(options);
  }

  @AfterEach
  void after() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void testFullPageScreenshot() throws IOException {
    driver.get("file:///Users/takeya/Desktop/screen/screen_x_long.html");
    Path screenShot = driver.getFullPageScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc01.png"), REPLACE_EXISTING);

  }


  @Test
  void testGet() {
    driver.get("http://example.selenium.jp/reserveApp_Renewal/");

    assertEquals("予約情報入力", driver.getTitle());
  }

}
