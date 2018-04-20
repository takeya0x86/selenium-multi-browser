package io.github.takeya0x86.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import io.github.bonigarcia.SeleniumExtension;
import io.github.bonigarcia.SeleniumJupiter;
import io.github.bonigarcia.wdm.Architecture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * Microsoft Internet Explorer
 */
@ExtendWith(SeleniumExtension.class)
@EnabledOnOs(WINDOWS)
class InternetExplorerTest {

  @BeforeAll
  static void setUpAll() {
    SeleniumJupiter.config().wdm().setArchitecture(Architecture.X32);
  }

  @Test
  void testGet(InternetExplorerDriver driver) {
    driver.get("https://getbootstrap.com/");

    assertTrue(driver.getTitle().contains("Bootstrap"));
  }

}
