import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

    /*
     * this class should includes methods to go to all pages, help, logout, etc.
     */

public class BaseSite {
	private WebDriver driver;

    public BaseSite(WebDriver driver){
        this.driver = driver;
    }

    private void clickOnAdministrationLink() throws InterruptedException {
        String administrationButtonXpath =  "//a[@id=\"adminHref\"]";
        WebElement administrationDropdown = driver.findElement(By.xpath(administrationButtonXpath));
        administrationDropdown.click();

        WebDriverWait waitForLinks = new WebDriverWait(driver,5);
        waitForLinks.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathConstants.GENERAL_ADMIN_LINK)));
    }

	public void goToGeneralAdmin() throws InterruptedException {
        clickOnAdministrationLink();
        WebElement generalAdminLink = driver.findElement(By.xpath(XpathConstants.GENERAL_ADMIN_LINK));
        generalAdminLink.click();

        //TODO: Introduce a wait before returning control, the sleep is just a temporary solution
        Thread.sleep(3000);
    }
}