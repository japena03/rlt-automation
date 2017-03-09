package com.rlt.automation.tests.admin;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.rlt.automation.admin.CentersPage;
import com.rlt.automation.tests.BaseTest;
import com.rlt.automation.util.Comparison;

import java.util.ArrayList;
import java.util.HashMap;

public class CentersPageRegression extends BaseTest {
    private CentersPage cp;
    private ArrayList<HashMap<String, String>> allRows;
    
	@Override
	public void doSpecificSetup() throws Exception {
		cp = new CentersPage(driver);
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
    	Assert.assertEquals(rows, 1);
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
