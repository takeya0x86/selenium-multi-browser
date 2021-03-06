package com.example.selenium;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.common.collect.ImmutableMap;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.network.Network;

/**
 * Google Chrome Desktop
 */
class ChromeTest {

  private ChromeDriver driver;

  private BrowserMobProxy proxy;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.chromedriver().setup();
  }

  @BeforeEach
  void before() throws Exception {
    proxy = new BrowserMobProxyServer();
    proxy.setChainedProxy(new InetSocketAddress(InetAddress.getByName("192.0.2.0"), 3128));
    proxy.chainedProxyAuthorization("admin", "admin", AuthType.BASIC);
    proxy.start();
    var options = new ChromeOptions();
    options.setProxy(ClientUtil.createSeleniumProxy(proxy));
    driver = new ChromeDriver(options);
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
  void testNetworkTraffic() {
    try (var devTools = driver.getDevTools()) {
      devTools.createSession();
      devTools.addListener(Network.responseReceived(), (responseReceived -> {
        assertNotNull(responseReceived);
        assertNotNull(responseReceived.getResponse());
        assertNotNull(responseReceived.getResponse().getTiming());  // failed
      }));
      devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
      driver.get("https://www.seleniumhq.org/");
    }
  }

  @Test
  void testBasicAuth() {
    try (var devTools = driver.getDevTools()) {
      devTools.createSession();
      var usernamePassword = Base64.getMimeEncoder().encodeToString("admin:admin".getBytes());
      devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
      devTools
          .send(Network
              .setExtraHTTPHeaders(ImmutableMap.of("Authorization", "Basic " + usernamePassword)));
      driver.get("https://the-internet.herokuapp.com/basic_auth");
      assertEquals("Basic Auth", driver.findElement(By.tagName("h3")).getText());
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
  void testHttpResponse() {
    proxy.addResponseFilter((response, contents, messageInfo) -> {
      if (response == null) {
        return;
      }
      var status = response.getStatus().code();
      var url = messageInfo.getUrl();
      if (status >= 400) {
        System.out.println("HTTP status error: " + url + ": " + status);
      }
    });
    driver.get("https://the-internet.herokuapp.com");
  }

  @Test
  void testFullPageScreenshot() throws IOException {
    driver.get("https://github.com/");
    var screenShot = driver.getScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc01.png"), REPLACE_EXISTING);

    screenShot = getFullPageScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc02.png"), REPLACE_EXISTING);

    driver.findElement(By.linkText("open source")).click();
    screenShot = driver.getScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc03.png"), REPLACE_EXISTING);

    screenShot = getFullPageScreenshotAs(OutputType.FILE).toPath();
    Files.copy(screenShot, Paths.get("sc04.png"), REPLACE_EXISTING);
  }

  @SuppressWarnings("unchecked")
  public <X> X getFullPageScreenshotAs(OutputType<X> outputType) {
    var layoutMetrics = driver.executeCdpCommand("Page.getLayoutMetrics", Collections.emptyMap());

    var contentSize = (Map<String, Long>) layoutMetrics.get("contentSize");
    var width = contentSize.get("width");
    var height = contentSize.get("height");
    driver.executeCdpCommand("Emulation.setDeviceMetricsOverride",
        ImmutableMap.of("mobile", true, "width", width, "height", height, "deviceScaleFactor", 1));

    var clip = ImmutableMap.of("x", 0, "y", 0, "width", width, "height", height, "scale", 1);
    var result = driver.executeCdpCommand("Page.captureScreenshot", ImmutableMap.of("clip", clip));

    var visualViewport = (Map<String, Long>) layoutMetrics.get("layoutViewport");
    var clientWidth = visualViewport.get("clientWidth");
    var clientHeight = visualViewport.get("clientHeight");
    driver.executeCdpCommand("Emulation.setDeviceMetricsOverride",
        ImmutableMap
            .of("mobile", true, "width", clientWidth, "height", clientHeight, "deviceScaleFactor",
                1));

    var base64 = (String) result.get("data");

    return outputType.convertFromBase64Png(base64);
  }

  @Test
  void testGet() {
    driver.get("http://example.selenium.jp/reserveApp_Renewal/");

    assertEquals("予約情報入力", driver.getTitle());

    var reservePage = new ReservePage(driver);

    reservePage.agreeAndGotoNext();

    assertEquals("お名前が指定されていません", reservePage.getErrorMessage());

    reservePage.returnToIndex();

    reservePage.inputGuestName("テスト太郎");
    reservePage.agreeAndGotoNext();
    assertEquals("宿泊日には、翌日以降の日付を指定してください。", reservePage.getErrorMessage());

    reservePage.returnToIndex();

    reservePage.inputReserveDate(LocalDate.now().plusDays(1), 0);
    reservePage.agreeAndGotoNext();
    assertEquals("宿泊日数が1日以下です", reservePage.getErrorMessage());

    reservePage.returnToIndex();

    reservePage.inputReserveDate(LocalDate.now().plusMonths(3).plusDays(1), 1);
    reservePage.agreeAndGotoNext();
    assertEquals("宿泊日には、3ヶ月以内のお日にちのみ指定できます。", reservePage.getErrorMessage());

    reservePage.returnToIndex();

    reservePage.inputReserveDate(LocalDate.now().plusDays(1), 1);
    reservePage.inputHeadCount(0);
    reservePage.agreeAndGotoNext();
    assertEquals("人数が入力されていません。", reservePage.getErrorMessage());

  }

}
