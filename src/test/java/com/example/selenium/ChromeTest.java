package com.example.selenium;


import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.ImmutableMap;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Google Chrome Desktop
 */
class ChromeTest {

  private ChromeDriver driver;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.chromedriver().setup();
  }

  @BeforeEach
  void before() {
    ChromeOptions options = new ChromeOptions();
    Map<String, String> mobileEmulation = ImmutableMap.of("deviceName", "iPhone X");
    options.setExperimentalOption("mobileEmulation", mobileEmulation);
    driver = new ChromeDriver(options);
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
    Map<String, Object> layoutMetrics = driver
        .executeCdpCommand("Page.getLayoutMetrics", Collections.emptyMap());

    Map<String, Long> contentSize = (Map<String, Long>) layoutMetrics.get("contentSize");
    long width = contentSize.get("width");
    long height = contentSize.get("height");
    driver.executeCdpCommand("Emulation.setDeviceMetricsOverride",
        ImmutableMap.of("mobile", true, "width", width, "height", height, "deviceScaleFactor", 1));

    Map<String, Object> clip = ImmutableMap
        .of("x", 0, "y", 0, "width", width, "height", height, "scale", 1);
    Map<String, Object> result = driver.executeCdpCommand("Page.captureScreenshot", ImmutableMap.of("clip", clip));

    Map<String, Long> visualViewport = (Map<String, Long>) layoutMetrics.get("layoutViewport");
    long clientWidth = visualViewport.get("clientWidth");
    long clientHeight = visualViewport.get("clientHeight");
    driver.executeCdpCommand("Emulation.setDeviceMetricsOverride",
        ImmutableMap.of("mobile", true, "width", clientWidth, "height", clientHeight, "deviceScaleFactor", 1));

    String base64 = (String) result.get("data");

    return outputType.convertFromBase64Png(base64);
  }

  @Test
  void testGet() {
    driver.get("http://example.selenium.jp/reserveApp_Renewal/");

    assertEquals("予約情報入力", driver.getTitle());

    ReservePage reservePage = new ReservePage(driver);

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
