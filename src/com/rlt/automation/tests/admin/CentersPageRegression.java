package com.rlt.automation.tests.admin;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.rlt.automation.admin.CentersPage;
import com.rlt.automation.admin.CoveragesPage;
import com.rlt.automation.admin.EntitiesPage;
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
    public void testNewCenterWithCoverage() throws InterruptedException {
    	String coverageName = "automation-coverage";
    	String centerName = "center-with-coverage";
    	
    	CoveragesPage coveragePage = new CoveragesPage(driver);
    	coveragePage.clickCoveragesLink();
    	coveragePage.clickNewButton();
    	coveragePage.setName(coverageName);
    	coveragePage.clickApplyButton();
    	
    	Boolean coverageRow = coveragePage.clickItem(coverageName);
    	Assert.assertTrue(coverageRow);
    	
    	coveragePage.clickCentersLink();
    	cp.clickNewButton();
    	cp.setName(centerName);
    	cp.setCoverage(coverageName);
    	cp.clickApplyButton();
    	
    	Boolean clickCenter = cp.clickItem(centerName);
    	Assert.assertTrue(clickCenter);
    	cp.clickDeleteButton();
    	
    	cp.clickCoveragesLink();
    	
    	coveragePage.clickItem(coverageName);
    	coveragePage.clickDeleteButton();
    }
    
    /* 
     * The production and system centers are required by the system and therefore we should never
     * allow those centers to be deleted. Any system with ID < 1000 is actually not allowed to be deleted.
     */
    @Test
    public void testDeleteProdAndSysCenters() throws InterruptedException {
    	String[] undeleteableCenters = {"system", "production"};
    	String errorMessage = "An error has occurred on the server: [RLTDB0001DVAL] This record is required by the application and can't be deleted.";
    	
    	for(int i = 0; i < undeleteableCenters.length; ++i) {
    		// Click center, ensure we found it and were able to click on it
    		Boolean clickCenter = cp.clickItem(undeleteableCenters[i]);
    		Assert.assertTrue(clickCenter);
    		
    		// Click the delete button then verify we get an alert with "unable to delete" message"
    		// The clickDeleteButton method accepts the initial alert, I must then wait a little bit
    		// for the 2nd alert to pop up
    		cp.clickDeleteButton();
    		Thread.sleep(500);
    		
        	String alertMessage = cp.getAlertText();
        	Assert.assertEquals(alertMessage, errorMessage);
        	cp.acceptAlert();
    	}
    }
    
    /* 
     * When a center is copied only the parent center and coverage should be retained, verify name is null
     */
    @Test
    public void testCopyCenter() throws Exception {
    	String parentCenter = "parent-center";
    	String childCenter = "child-center";
    	String centerCopy = "child-center-copy";
    
    	newCenter(parentCenter, null, null);
    	newCenter(childCenter, null, parentCenter);
    	
    	Boolean childCenterClicked = cp.clickItem(childCenter);
    	Assert.assertTrue(childCenterClicked);
    	
    	cp.clickCopyButton();
    	
    	Assert.assertEquals(cp.getName(), null);
    	Assert.assertEquals(cp.getParentCenter(), parentCenter);
    	
    	cp.setName(centerCopy);
    	cp.clickApplyButton();
    	
    	Assert.assertTrue(cp.clickItem(centerCopy));
    	
    	// Delete centers
    	cp.clickDeleteButton();
    	
    	cp.clickItem(childCenter);
    	cp.clickDeleteButton();
    	
    	cp.clickItem(parentCenter);
    	cp.clickDeleteButton();
    }
    
    /* 
     * If a center has entity references do not allow deletion
     */
    @Test
    public void dontAllowCenterDeleteWithEntityReferences() throws Exception {
    	// Create new center
    	String testCenter = "test";
    	int numberOfCenters = cp.getNumberOfItems();
    	newCenter(testCenter, null, null);
    	Assert.assertEquals(numberOfCenters + 1, cp.getNumberOfItems());
    	
    	// Create entity with center that was just created
    	EntitiesPage entitiesPage = new EntitiesPage(driver);
    	entitiesPage.clickEntitiesLink();
    	String entityName = "test-entity";
    	int numberOfEntities = entitiesPage.getNumberOfItems();
    	
    	entitiesPage.clickNewButton();
    	entitiesPage.setName(entityName);
    	entitiesPage.setCenter(testCenter);
    	entitiesPage.setClass("entity");
    	entitiesPage.clickApplyButton();
    	Assert.assertEquals(numberOfEntities + 1, entitiesPage.getNumberOfItems());
    	
    	// Navigate back to centers and try to delete the center
    	cp.clickCentersLink();
    	cp.clickItem(testCenter);
    	cp.clickReferencesTab();
    	Assert.assertEquals(cp.getNumberOfReferencedEntities(), 1);
    	
    	cp.clickDeleteButton();
    	String errorMessage = "This center can't be deleted because it is in use by 1 or more CDR Collectors.";
    	Thread.sleep(500);
		
    	String alertMessage = cp.getAlertText();
    	Assert.assertEquals(alertMessage, errorMessage);
    	cp.acceptAlert();
    	
    	// Go back to entities and delete entity
    	entitiesPage.clickEntitiesLink();
    	numberOfEntities = entitiesPage.getNumberOfItems();
    	entitiesPage.clickItem(entityName);
    	entitiesPage.clickDeleteButton();
    	Assert.assertEquals(numberOfEntities - 1, entitiesPage.getNumberOfItems());
    	
    	// Now delete the center
    	cp.clickCentersLink();
    	numberOfCenters = cp.getNumberOfItems();
    	cp.clickItem(testCenter);
    	cp.clickDeleteButton();
    	Assert.assertEquals(numberOfCenters - 1, cp.getNumberOfItems());
    }

    @Test
    public void sortEveryField() throws Exception {
        newCenter("00-parent", null, null);
        newCenter("zz-parent", null, null);
        newCenter("00-center", null, "00-parent");
        newCenter("zz-center", null, "zz-parent");

        String[] columns = {"Name", "Coverage", "Full Center Name", "Parent Center", "ID", "Child Centers", "Entity Children", "Link Children", "Users Default", "Domain Name", "Domain Id" };
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
        
        Assert.assertEquals(cp.clickItem(name), true);
        
        Thread.sleep(1000);
    }

    private void sort(String col, String order) throws InterruptedException {
        cp.sort(col, order);
        allRows = cp.getAllRows();
    }
}
