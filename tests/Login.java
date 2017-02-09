
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

public class Login {

        @Test
        public void loginTest() throws Exception{
            String hubUrl = "http://192.168.86.101:4444/wd/hub";
            System.setProperty("webdriver.gecko.driver","D:\\Git\\rlt-automation\\config\\geckodriver.exe");
            
            DesiredCapabilities capability = DesiredCapabilities.firefox();
            capability.setCapability("marionette", true);
            
            WebDriver driver = new RemoteWebDriver(new URL(hubUrl), capability);
            driver.manage().window().maximize();
		    
//            LoginPage login = new LoginPage(driver);
//		    login.login("localhost", "sadm", "ems");
            driver.get("https://www.google.com/");
    }
}