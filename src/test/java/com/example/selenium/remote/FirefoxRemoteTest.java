package com.example.selenium.remote;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Firefox Remote
 */
class FirefoxRemoteTest {

  private WebDriver driver;

  @BeforeEach
  void before() throws Exception {
    var options = new FirefoxOptions();
    options.setCapability("zal:name", "STAR-HOTEL-E2ETest-firefox");
    options.setCapability("zal:build", "#1");
    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
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
