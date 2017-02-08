
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.URL;

public class Login {

        @Test
        public void loginTest() throws Exception{
		    //WebDriver driver = new FirefoxDriver();
            String hubUrl = "http://localhost:4444/wd/hub";
            DesiredCapabilities capability = DesiredCapabilities.firefox();
            WebDriver driver = new RemoteWebDriver(new URL(hubUrl), capability);
            driver.manage().window().maximize();
		    LoginPage login = new LoginPage(driver);
		    login.login("rhino", "sadm", "ems");
    }
}