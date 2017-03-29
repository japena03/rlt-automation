package com.rlt.automation.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.rlt.automation.util.XpathConstants;

public class CentersPage extends BaseAdmin {

    public CentersPage (WebDriver driver) {
        super(driver);
    }

    public void setName(String name) {
        WebElement nameField = driver.findElement(By.xpath(XpathConstants.CENTERS_NAME_FIELD));
        if(isFieldReadonly(nameField)) {
            throw new RuntimeException("Field is not editable");
        }
        nameField.sendKeys(name);
    }
    
    public String getName() {
    	String name = driver.findElement(By.xpath(XpathConstants.CENTERS_NAME_FIELD)).getText();
    
    	return name.equals("") ? null : name;
    }

    public void setParentCenter(String parentCenter) throws Exception {
        selectFromDropdown(parentCenter, XpathConstants.CENTERS_PARENT_CENTER_DROPDOWN_BUTTON);
    }
    
    public String getParentCenter() {
    	return driver.findElement(By.xpath(XpathConstants.CENTERS_PARENT_CENTER_FIELD)).getAttribute("value");
    }

    public void setCoverage(String coverage) throws InterruptedException {
        selectFromDropdown(coverage, XpathConstants.CENTERS_COVERAGE_DROPDOWN_BUTTON);
    }

    public void clickReferencedCenters() {
        WebElement referencedCenters = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_CENTER_CHILDREN_BUTTON));
        referencedCenters.click();
    }

    public int getNumberOfReferencedCenters() {
        WebElement referencedCenters = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_CENTER_CHILDREN_BUTTON));
        String numberOfReferencedCenters = referencedCenters.getText();
        int i = numberOfReferencedCenters.indexOf(' ');
        numberOfReferencedCenters = numberOfReferencedCenters.substring(0, i);

        return Integer.parseInt(numberOfReferencedCenters);
    }

    public void clickReferencedEntities() {
        WebElement referencedEntities = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_ENTITIES_BUTTON));
        referencedEntities.click();
    }

    public int getNumberOfReferencedEntities() {
        WebElement referencedEntities = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_ENTITIES_BUTTON));
        String numberOfReferencedEntities = referencedEntities.getText();
        int i = numberOfReferencedEntities.indexOf(' ');
        numberOfReferencedEntities = numberOfReferencedEntities.substring(0, i);

        return Integer.parseInt(numberOfReferencedEntities);
    }

    public void clickReferencedRtcpCollectors() {
        WebElement referencedRtcpCollectors = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_RTCP_COLLECTORS_BUTTON));
        referencedRtcpCollectors.click();
    }

    public int getNumberOfReferencedRtcpCollectors() {
        WebElement referencedRtcp = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_RTCP_COLLECTORS_BUTTON));
        String numberOfReferencedRtcp = referencedRtcp.getText();
        int i = numberOfReferencedRtcp.indexOf(' ');
        numberOfReferencedRtcp = numberOfReferencedRtcp.substring(0, i);

        return Integer.parseInt(numberOfReferencedRtcp);
    }

    public void clickReferencedPqosCollectors() {
        WebElement referencedPqosCollectors = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_PQOS_COLLECTORS_BUTTON));
        referencedPqosCollectors.click();
    }

    public int getNumberOfReferencedPqosCollectors() {
        WebElement referencedPqos = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_PQOS_COLLECTORS_BUTTON));
        String numberOfReferencedPqos = referencedPqos.getText();
        int i = numberOfReferencedPqos.indexOf(' ');
        numberOfReferencedPqos = numberOfReferencedPqos.substring(0, i);

        return Integer.parseInt(numberOfReferencedPqos);
    }

    public void clickReferencedAvayaCdrCollectors() {
        WebElement referencedAvayaCdrCollectors = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_AVAYA_CDR_COLLECTORS_BUTTON));
        referencedAvayaCdrCollectors.click();
    }

    public int getNumberOfReferencedAvayaCdrCollectors() {
        WebElement referencedAvayaCdr = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_AVAYA_CDR_COLLECTORS_BUTTON));
        String numberOfReferencedAvayaCdr = referencedAvayaCdr.getText();
        int i = numberOfReferencedAvayaCdr.indexOf(' ');
        numberOfReferencedAvayaCdr = numberOfReferencedAvayaCdr.substring(0, i);

        return Integer.parseInt(numberOfReferencedAvayaCdr);
    }

    public void clickReferencedLinks() {
        WebElement referencedLinks = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_LINKS_BUTTON));
        referencedLinks.click();
    }

    public int getNumberOfReferencedLinks() {
        WebElement referencedLinks = driver.findElement(By.xpath(XpathConstants.CENTERS_REFS_LINKS_BUTTON));
        String numberOfReferencedLinks = referencedLinks.getText();
        int i = numberOfReferencedLinks.indexOf(' ');
        numberOfReferencedLinks = numberOfReferencedLinks.substring(0, i);

        return Integer.parseInt(numberOfReferencedLinks);
    }

	@Override
	public void goToPage() {
		WebElement centersLink = driver.findElement(By.xpath(XpathConstants.ADMIN_CENTERS_LINK));
        centersLink.click();
        waitForLoad();

        updateGridForPage();
		
	}
}