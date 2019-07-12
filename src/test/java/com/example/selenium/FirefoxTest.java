package com.example.selenium;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.auth.AuthType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Firefox Desktop
 */
class FirefoxTest {

  private FirefoxDriver driver;

  private BrowserMobProxy proxy;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.firefoxdriver().setup();
  }

  @BeforeEach
  void before() {
    proxy = new BrowserMobProxyServer();
    proxy.start();
    FirefoxOptions options = new FirefoxOptions();
    options.setProxy(ClientUtil.createSeleniumProxy(proxy));
    driver = new FirefoxDriver(options);
  }

  @AfterEach
  void after() {
    if (driver != null) {
      driver.quit();
    }
    if (proxy != null) {
      proxy.stop();
    }
  }

  @Test
  void testBasicAuthUsingProxy() {
    proxy.autoAuthorization("the-internet.herokuapp.com", "admin", "admin", AuthType.BASIC);
    driver.get("https://the-internet.herokuapp.com/basic_auth");
    assertEquals("Basic Auth", driver.findElement(By.tagName("h3")).getText());
    proxy.stopAutoAuthorization("the-internet.herokuapp.com");
  }

  @Test
  void testFullPageScreenshot() throws IOException, InterruptedException {
    driver.get("https://the-internet.herokuapp.com/infinite_scroll");
    Thread.sleep(3000);
    Path screenShot = driver.getFullPageScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc01.png"), REPLACE_EXISTING);
  }


  @Test
  void testGet() {
    driver.get("http://example.selenium.jp/reserveApp_Renewal/");

    assertEquals("予約情報入力", driver.getTitle());
  }

}
