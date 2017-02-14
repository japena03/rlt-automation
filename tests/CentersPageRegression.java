
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CentersPageRegression {
    private LoginPage lp;
    private DesiredCapabilities capability;
    private WebDriver driver;
    private CentersPage cp;
    ArrayList<HashMap<String, String>> allRows;

    @BeforeTest
    public void setup() throws Exception {
        String hubUrl = "http://192.168.86.100:5555/wd/hub";
        capability = DesiredCapabilities.firefox();

        // To test local comment out RemoteWebDriver line and uncomment FirefoxDriver and System.setProperty lines
        // node does not need to be running in order to test locally
        driver = new RemoteWebDriver(new URL(hubUrl), capability);
//        System.setProperty("webdriver.gecko.driver", "D:\\Git\\rlt-automation\\config\\geckodriver.exe");
//        driver = new FirefoxDriver();
        
        driver.manage().window().maximize();
        
        lp = new LoginPage(driver);
        cp = new CentersPage(driver);

        lp.login("localhost","sadm","ems");
        cp.goToGeneralAdmin();
        cp.clickCentersLink();
    }
    
    @BeforeMethod
    public void beforeEachTest() throws InterruptedException {
    	driver.navigate().refresh();
    	
    	//TODO Very bad idea to explicitly wait 3 seconds in between test, i need to use a webdriverwait call here
    	Thread.sleep(3000L);
    }
    
    @AfterTest
    public void afterTest() {
    	driver.quit();
    }

    @Test
    public void newCenter() throws InterruptedException {
        cp.clickNewButton();
        cp.setName("automation-test");
        cp.clickApplyButton();
        boolean clickNewCenter = cp.clickItem("automation-test");
        Assert.assertEquals(clickNewCenter, true);
        cp.clickDeleteButton();
        
        //TODO Use a driverwait here, should not be using thread sleep. If we don't wait a bit before the end of the method the center doesn't get deleted properly
        Thread.sleep(1000L);
    }

    @Test
    public void sortEveryField() throws Exception {
        newCenter("00-parent", null, null);
        newCenter("zz-parent", null, null);
        newCenter("00-center", null, "00-parent");
        newCenter("zz-center", null, "zz-parent");

        String[] columns = {"Name", "Coverage", "Full Center Name", "Parent Center", "ID", "Child Centers", "Entity Children", "Link Children"};
        cp.displayColumns(columns);

        sort("Name", "ascending");
        Assert.assertEquals(allRows.get(0).get("Name"),"00-center");
        sort("Name", "descending");
        Assert.assertEquals(allRows.get(0).get("Name"),"zz-parent");

        //TODO continue with other fields
    }
    
    @Test
    public void filterFields() throws Exception {
    	int rows = cp.createFilter("Name", Comparison.EQUAL_TO, "system");
    	Assert.assertEquals(1, rows);
    }

    private void newCenter(String name, String coverage, String parent) throws Exception {
        cp.clickNewButton();
        cp.setName(name);
        if(parent != null) {
            cp.setParentCenter(parent);
        }
        if(coverage != null) {
            cp.setCoverage(coverage);
        }
        cp.clickApplyButton();
    }

    private void sort(String col, String order) throws InterruptedException {
        cp.sort(col, order);
        allRows = cp.getAllRows();
    }
}
