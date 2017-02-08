import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
        // TODO: Don't use sleep method, use webdriverwait
        Thread.sleep(5000);
	}

}
