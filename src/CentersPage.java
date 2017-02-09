import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedCondition;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.rmi.server.ExportException;

public class CentersPage extends BaseAdmin {

    String nameFieldXpath = "//div[@id=\"detailsTabPanel\"]//td[text()=\"Name: \"]/..//input";
    String parentCenterFieldXpath = "//div[@id=\"detailsTabPanel\"]//td[text()=\"Parent Center: \"]/..//input";
    String coverageFieldXpath = "//div[@id=\"detailsTabPanel\"]//td[text()=\"Coverage: \"]/..//input";
    String centersReferencedXpath = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Centers\")]";
    String entitiesReferencedXpath = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Entities\")]";
    String rtcpCollectorsReferencedXpath = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"RTCP\")]";
    String pqosCollectorsReferencedXpath = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Passive\")]";
    String avayaCdrCollectorsReferencedXpath = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Avaya\")]";
    String linksReferencedXpath = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Links\")]";

    public CentersPage (WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void clickCentersLink() {
        String centersXpath = "((//div[@class=\"x-tree3-node-ct\"])[1])/*[1]";

        WebElement centersLink = driver.findElement(By.xpath(centersXpath));
        centersLink.click();
        waitForLoad();

        buildColumnsMap();
        buildAllRows();
    }

    public void setName(String name) {
        WebElement nameField = driver.findElement(By.xpath(nameFieldXpath));
        if(isFieldReadonly(nameField)) {
            throw new RuntimeException("Field is not editable");
        }
        nameField.sendKeys(name);
    }

    public void setParentCenter(String parentCenter) throws InterruptedException {
        String parentCenterCombolistXpath = "//div[starts-with(@class,\"x-combo-list\") and text()=\"" + parentCenter + "\"]";
        WebElement parentCenterField = driver.findElement(By.xpath(parentCenterFieldXpath));
        parentCenterField.sendKeys(parentCenter);
        waitForElement(parentCenterCombolistXpath);

        WebElement parentCenterCombolist = driver.findElement(By.xpath(parentCenterCombolistXpath));
        parentCenterCombolist.click();
    }

    public void setCoverage(String coverage) throws InterruptedException {
        String coverageCombolistXpath = "//div[starts-with(@class,\"x-combo-list\") and text()=\"" + coverage + "\"]";
        WebElement coverageField = driver.findElement(By.xpath(coverageFieldXpath));
        coverageField.sendKeys(coverage);
        waitForElement(coverageCombolistXpath);

        WebElement coverageCombolist = driver.findElement(By.xpath(coverageCombolistXpath));
        coverageCombolist.click();
    }

    public void clickReferencedCenters() {
        WebElement referencedCenters = driver.findElement(By.xpath(centersReferencedXpath));
        referencedCenters.click();
    }

    public int getNumberOfReferencedCenters() {
        WebElement referencedCenters = driver.findElement(By.xpath(centersReferencedXpath));
        String numberOfReferencedCenters = referencedCenters.getText();
        int i = numberOfReferencedCenters.indexOf(' ');
        numberOfReferencedCenters = numberOfReferencedCenters.substring(0, i);

        return Integer.parseInt(numberOfReferencedCenters);
    }

    public void clickReferencedEntities() {
        WebElement referencedEntities = driver.findElement(By.xpath(entitiesReferencedXpath));
        referencedEntities.click();
    }

    public int getNumberOfReferencedEntities() {
        WebElement referencedEntities = driver.findElement(By.xpath(entitiesReferencedXpath));
        String numberOfReferencedEntities = referencedEntities.getText();
        int i = numberOfReferencedEntities.indexOf(' ');
        numberOfReferencedEntities = numberOfReferencedEntities.substring(0, i);

        return Integer.parseInt(numberOfReferencedEntities);
    }

    public void clickReferencedRtcpCollectors() {
        WebElement referencedRtcpCollectors = driver.findElement(By.xpath(rtcpCollectorsReferencedXpath));
        referencedRtcpCollectors.click();
    }

    public int getNumberOfReferencedRtcpCollectors() {
        WebElement referencedRtcp = driver.findElement(By.xpath(rtcpCollectorsReferencedXpath));
        String numberOfReferencedRtcp = referencedRtcp.getText();
        int i = numberOfReferencedRtcp.indexOf(' ');
        numberOfReferencedRtcp = numberOfReferencedRtcp.substring(0, i);

        return Integer.parseInt(numberOfReferencedRtcp);
    }

    public void clickReferencedPqosCollectors() {
        WebElement referencedPqosCollectors = driver.findElement(By.xpath(pqosCollectorsReferencedXpath));
        referencedPqosCollectors.click();
    }

    public int getNumberOfReferencedPqosCollectors() {
        WebElement referencedPqos = driver.findElement(By.xpath(rtcpCollectorsReferencedXpath));
        String numberOfReferencedPqos = referencedPqos.getText();
        int i = numberOfReferencedPqos.indexOf(' ');
        numberOfReferencedPqos = numberOfReferencedPqos.substring(0, i);

        return Integer.parseInt(numberOfReferencedPqos);
    }

    public void clickReferencedAvayaCdrCollectors() {
        WebElement referencedAvayaCdrCollectors = driver.findElement(By.xpath(avayaCdrCollectorsReferencedXpath));
        referencedAvayaCdrCollectors.click();
    }

    public int getNumberOfReferencedAvayaCdrCollectors() {
        WebElement referencedAvayaCdr = driver.findElement(By.xpath(avayaCdrCollectorsReferencedXpath));
        String numberOfReferencedAvayaCdr = referencedAvayaCdr.getText();
        int i = numberOfReferencedAvayaCdr.indexOf(' ');
        numberOfReferencedAvayaCdr = numberOfReferencedAvayaCdr.substring(0, i);

        return Integer.parseInt(numberOfReferencedAvayaCdr);
    }

    public void clickReferencedLinks() {
        WebElement referencedLinks = driver.findElement(By.xpath(linksReferencedXpath));
        referencedLinks.click();
    }

    public int getNumberOfReferencedLinks() {
        WebElement referencedLinks = driver.findElement(By.xpath(linksReferencedXpath));
        String numberOfReferencedLinks = referencedLinks.getText();
        int i = numberOfReferencedLinks.indexOf(' ');
        numberOfReferencedLinks = numberOfReferencedLinks.substring(0, i);

        return Integer.parseInt(numberOfReferencedLinks);
    }
}