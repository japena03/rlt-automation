
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class Login {

	private WebDriver driver;

	@AfterTest
	public void afterTest() {
		driver.quit();
	}

	@Test
	public void loginTest() throws Exception {
		String hubUrl = "http://192.168.86.100:5555/wd/hub";

		DesiredCapabilities capability = DesiredCapabilities.firefox();
		capability.setCapability("marionette", true);

		driver = new RemoteWebDriver(new URL(hubUrl), capability);
		driver.manage().window().maximize();

		// LoginPage login = new LoginPage(driver);
		// login.login("localhost", "sadm", "ems");
		driver.get("https://www.google.com/");
	}
}