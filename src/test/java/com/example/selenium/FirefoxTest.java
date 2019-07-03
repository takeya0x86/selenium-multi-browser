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
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.firefox.FirefoxDriver;

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
    driver = new FirefoxDriver();
  }

  @AfterEach
  void after() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void testFullPageScreenshot() throws IOException {
    driver.get("https://github.com/");
    Path screenShot = driver.getScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc01.png"), REPLACE_EXISTING);

    screenShot = driver.getFullPageScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc02.png"), REPLACE_EXISTING);

    driver.findElement(By.linkText("open source")).click();
    screenShot = driver.getScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc03.png"), REPLACE_EXISTING);

    screenShot = driver.getFullPageScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc04.png"), REPLACE_EXISTING);
  }


  @Test
  void testGet() {
    driver.get("http://example.selenium.jp/reserveApp_Renewal/");

    assertEquals("予約情報入力", driver.getTitle());
  }

}
