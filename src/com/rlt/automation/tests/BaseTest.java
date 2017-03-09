package com.rlt.automation.tests;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import com.rlt.automation.base.LoginPage;

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
    
    @AfterMethod
	public void takeScreenshotOnFailure(ITestResult testResult) throws IOException {
		if(testResult.getStatus() == ITestResult.FAILURE) {
			try {
				WebDriver augmentedDriver = new Augmenter().augment(driver);
				File source = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE); 
				String path = "test-output" + File.separator + "screenshots" + File.separator + source.getName();
				File screenshot = new File(path);
				FileUtils.copyFile(source, screenshot);
				System.out.println("Copied screenshot file to: " + screenshot.getAbsolutePath());
			}
			catch (IOException e) {
				throw new IllegalStateException("Failed to capture screenshot", e);
			}
		}
	}
    
    public abstract void doSpecificSetup() throws Exception;
}
