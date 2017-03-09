package com.rlt.automation.tests.admin;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.rlt.automation.base.LoginPage;

public class Login {
	private WebDriver driver;

	@Test
	public void loginTest() throws Exception {
		System.setProperty("webdriver.gecko.driver", "D:\\Git\\rlt-automation\\config\\geckodriver.exe");
        driver = new FirefoxDriver();
        
        driver.manage().window().maximize();

		LoginPage login = new LoginPage(driver);
		login.login("localhost", "sadm", "es");
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
}