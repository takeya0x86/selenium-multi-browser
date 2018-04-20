package io.github.takeya0x86.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import io.github.bonigarcia.SeleniumExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.edge.EdgeDriver;

/**
 * Microsoft Edge
 */
@ExtendWith(SeleniumExtension.class)
@EnabledOnOs(WINDOWS)
class EdgeTest {

  @Test
  void testGet(EdgeDriver driver) {
    driver.get("https://getbootstrap.com/");

    assertTrue(driver.getTitle().contains("Bootstrap"));
  }

}
