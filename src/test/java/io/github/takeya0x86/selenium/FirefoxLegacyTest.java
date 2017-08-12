package io.github.takeya0x86.selenium;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Firefox Desktop ESR
 */
public class FirefoxLegacyTest {

  private static WebDriver driver = null;

  @BeforeClass
  public static void setUpAll() throws Exception {
    FirefoxOptions options = new FirefoxOptions();
    options.setLegacy(true);
    driver = new FirefoxDriver(options);
  }

  @Before
  public void setUp() throws Exception {
    driver.manage().deleteAllCookies();
  }

  @AfterClass
  public static void tearDownAll() throws Exception {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  public void testGet() throws Exception {
    driver.get("https://v4-alpha.getbootstrap.com/");

    assertThat(driver.getTitle(), containsString("Bootstrap"));
  }

  @Test
  public void testTakeScreenShot() throws Exception {
    driver.get("https://v4-alpha.getbootstrap.com/");

    Path screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
    Path dist = Paths.get("build", "firefox_legacy.png");
    Files.copy(screenShot, dist);

    assertThat(Files.exists(dist), is(true));
  }

}
