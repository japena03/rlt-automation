package com.rlt.automation.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.rlt.automation.util.XpathConstants;

public class EntitiesPage extends BaseAdmin {

	
	public EntitiesPage(WebDriver driver) {
		super(driver);
	}

	public void setName(String name) {
        WebElement nameField = driver.findElement(By.xpath(XpathConstants.ENTITIES_NAME_FIELD));
        if(isFieldReadonly(nameField)) {
            throw new RuntimeException("Field is not editable");
        }
        nameField.sendKeys(name);
    }
    
    public String getName() {
    	String name = driver.findElement(By.xpath(XpathConstants.ENTITIES_NAME_FIELD)).getText();
    
    	return name.equals("") ? null : name;
    }
    
    public void setCenter(String name) {        
        selectFromDropdown(name, XpathConstants.ENTITIES_CENTER_DROPDOWN_BUTTON);
    }
    
    public void setClass(String name) {
    	selectFromDropdown(name, XpathConstants.ENTITIES_CLASS_DROPDOWN_BUTTON);
    }

	@Override
	public void goToPage() {
		WebElement entitiesLink = driver.findElement(By.xpath(XpathConstants.ADMIN_ENTITIES_LINK));
    	entitiesLink.click();
        waitForLoad();
        
        updateGridForPage();
	}
}
