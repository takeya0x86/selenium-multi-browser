package com.example.selenium;


import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Google Chrome Desktop
 */
class ChromeTest {

  private WebDriver driver;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.chromedriver().setup();
  }

  @BeforeEach
  void before() {
    driver = new ChromeDriver();
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

  }

}
