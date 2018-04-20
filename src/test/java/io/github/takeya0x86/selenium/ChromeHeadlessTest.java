package io.github.takeya0x86.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bonigarcia.Options;
import io.github.bonigarcia.SeleniumExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Google Chrome Desktop Headless
 */
@ExtendWith(SeleniumExtension.class)
class ChromeHeadlessTest {

  @Options
  private ChromeOptions options = new ChromeOptions();

  {
    options.setHeadless(true);
  }

  @Test
  void testGet(ChromeDriver driver) {
    driver.get("https://getbootstrap.com/");

    assertTrue(driver.getTitle().contains("Bootstrap"));
  }

}
