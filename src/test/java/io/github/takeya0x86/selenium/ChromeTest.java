package io.github.takeya0x86.selenium;


import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bonigarcia.SeleniumExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Google Chrome Desktop
 */
@ExtendWith(SeleniumExtension.class)
class ChromeTest {

  @Test
  void testGet(ChromeDriver driver) {
    driver.get("https://getbootstrap.com/");

    assertTrue(driver.getTitle().contains("Bootstrap"));
  }

}
