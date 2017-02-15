import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {
	
    protected WebDriver driver;

    @BeforeClass
    public void init() throws Exception {
        String hubUrl = "http://192.168.86.100:5555/wd/hub";
        DesiredCapabilities capability = DesiredCapabilities.firefox();

        // To test local comment out RemoteWebDriver line and uncomment FirefoxDriver and System.setProperty lines
        // node does not need to be running in order to test locally
//        driver = new RemoteWebDriver(new URL(hubUrl), capability);
        System.setProperty("webdriver.gecko.driver", "D:\\Git\\rlt-automation\\config\\geckodriver.exe");
        driver = new FirefoxDriver();
        
        driver.manage().window().maximize();
        
        LoginPage lp = new LoginPage(driver);
        lp.login("localhost","sadm","ems");
        
        doSpecificSetup();
    }
    
    @AfterClass
    public void afterTest() {
    	driver.quit();
    }
    
    public abstract void doSpecificSetup() throws Exception;
}
