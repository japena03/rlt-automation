
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
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
        driver = new RemoteWebDriver(new URL(hubUrl), capability);
//        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        
        lp = new LoginPage(driver);
        cp = new CentersPage(driver);

        lp.login("localhost","sadm","ems");
        cp.goToGeneralAdmin();
        cp.clickCentersLink();
    }

    @Test
    public void newCenter() throws InterruptedException {
        cp.clickNewButton();
        cp.setName("automation-test");
        cp.clickApplyButton();
        boolean clickNewCenter = cp.clickItem("automation-test");
        Assert.assertEquals(clickNewCenter, true);
        cp.clickDeleteButton();
    }

    @Test
    public void sortEveryField() throws Exception {
        newCenter("00-parent", null, null);
        newCenter("zz-parent", null, null);
        newCenter("00-center", "00-coverage", "00-parent");
        newCenter("zz-center", "zz-coverage", "zz-parent");

        String[] columns = {"Name", "Coverage", "Full Center Name", "Parent Center", "ID", "Child Centers", "Entity Children", "Link Children"};
        cp.displayColumns(columns);

        sort("Name", "ascending");
        Assert.assertEquals(allRows.get(0).get("Name"),"00-center");
        sort("Name", "descending");
        Assert.assertEquals(allRows.get(0).get("Name"),"zz-parent");

        //TODO continue with other fields
    }

    private void newCenter(String name, String coverage, String parent) throws InterruptedException {
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
