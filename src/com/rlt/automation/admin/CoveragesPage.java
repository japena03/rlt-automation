package com.rlt.automation.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.rlt.automation.util.XpathConstants;

public class CoveragesPage extends BaseAdmin {

	public CoveragesPage(WebDriver driver) {
		super(driver);
	}

	public void setName(String name) {
		WebElement nameField = driver.findElement(By.xpath(XpathConstants.COVERAGES_NAME_FIELD));
		nameField.sendKeys(name);
	}
	
	public void setInheritParentCoverage() {
		WebElement inheritCheckbox = driver.findElement(By.xpath(XpathConstants.COVERAGES_INHERIT_PARENT_COV_CHECKBOX));
		inheritCheckbox.click();
	}

	@Override
	public void goToPage() {
		WebElement coveragesLink = driver.findElement(By.xpath(XpathConstants.ADMIN_COVERAGES_LINK));
    	coveragesLink.click();
        waitForLoad();

        updateGridForPage();
	}
}
