package com.rlt.automation.base;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

	private WebDriver driver;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void login(String host, String user, String password) throws InterruptedException {
		driver.get("http://" + host + ":8080/ems");
		
		// Find User ID & Password fields and login as user/password
        WebElement element = driver.findElement(By.id("LoginField"));
        element.sendKeys(user);
        
        element = driver.findElement(By.id("PasswordField"));
        element.sendKeys(password);
        
        element.submit();
        
        try {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id=\"monitorAlarmsHref\"]")));
		}
		catch(TimeoutException e) {
			throw new IllegalStateException("Failed to login");
		}
	}
}
