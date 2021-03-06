package com.rlt.automation.admin;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.rlt.automation.util.Comparison;
import com.rlt.automation.util.XpathConstants;

public abstract class BaseAdmin extends BaseSite { 
    private HashMap<String,Integer> allColumnHeaders;
    private ArrayList<String> positionToColumnName;
    private ArrayList<HashMap<String, String>> allRows;
    private WebElement gridRoot;
    
    BaseAdmin(WebDriver driver){
        super(driver);
    }
    
    public abstract void goToPage();

	public void clickRefreshButton() {
		WebElement refreshButton = driver.findElement(By.xpath(XpathConstants.ADMIN_REFRESH_BUTTON));
        if(refreshButton.getAttribute("aria-disabled").equals("false")) {
            refreshButton.click();
            waitForLoad();
        }
        else {
            System.out.println("Unable to refresh, button is disabled");
        }
	}
	
	public void clickNewButton() {
		WebElement element = driver.findElement(By.xpath(XpathConstants.ADMIN_NEW_BUTTON));
		element.click();
        waitForApplyButton();
	}
	
	public void clickApplyButton() throws InterruptedException {
        WebElement applyButton = driver.findElement(By.xpath(XpathConstants.ADMIN_APPLY_BUTTON));
		applyButton.click();
        Thread.sleep(500);
        //checkForRequiredFields();
		waitForLoad();
        buildAllRows();
	}

    private void clickFilterButton() throws Exception{
        WebElement filterButton = driver.findElement(By.xpath(XpathConstants.ADMIN_FILTER_BUTTON));
        filterButton.click();

        Thread.sleep(500);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathConstants.ADMIN_FILTER_COLOUMN_DROPDOWN_BUTTON)));
    }
	
    //TODO column parameter should be in a constants class or enum 
	public int createFilter(String column, Comparison comparison, String value) throws Exception {
		String filterColumnValueXpath = "(//div[@class=\" x-view x-combo-list-inner x-component x-unselectable\"])//div[text()=\"" + column + "\"]";
		String filterComparisonValueXpath = "(//div[@class=\" x-view x-combo-list-inner x-component x-unselectable\"])//div[text()=\"" + comparison.getKey() + "\"]";
		
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element;

        clearFilters();
        clickFilterButton();

		// Select a column from the dropdown based on what was passed in
		element = driver.findElement(By.xpath(XpathConstants.ADMIN_FILTER_COLOUMN_DROPDOWN_BUTTON));
		element.click();
		Thread.sleep(1000);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(filterColumnValueXpath)));
		} catch (TimeoutException e) {}
		element = driver.findElement(By.xpath(filterColumnValueXpath));
		element.click();
		Thread.sleep(1000);
		
		// Select a comparison value from the dropdown based on what was passed in
		element = driver.findElement(By.xpath(XpathConstants.ADMIN_FILTER_COMP_DROPDOWN_BUTTON));
		element.click();
		Thread.sleep(1000);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(filterComparisonValueXpath)));
		} catch (TimeoutException e) {}
		element = driver.findElement(By.xpath(filterComparisonValueXpath));
		Thread.sleep(1000);
		element.click();
		
		// Input value to filter for
		element = driver.findElement(By.xpath(XpathConstants.ADMIN_FILTER_VALUE_TEXTBOX));
		element.sendKeys(value);

        waitForLoad();
        
        // Wait for "displaying 1 of x" label to update
        // TODO We should not have to wait 1 second plus this is potentially bad when it takes longer than 1 second for it to load, a webdriverwait condition should be used
        Thread.sleep(1000L);
        
        return getNumberOfItems();
	}
	
	public void clearFilters() {
		WebElement clearFiltersButton = driver.findElement(By.xpath(XpathConstants.ADMIN_FILTER_CLEAR_BUTTON));
		clearFiltersButton.click();
		waitForLoad();
	}

    private void scrollToElement(WebElement element){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void sort(String column, String order) throws InterruptedException {
        WebElement columnsRoot = driver.findElement(By.xpath(XpathConstants.ADMIN_COLUMNS_ROOT));
        List <WebElement> columns = columnsRoot.findElements(By.xpath("./td/div/span"));

        if(columns.size() == 0) {
            System.out.println("Couldn't find column headers, please make sure to first select a report");
            return;
        }

        int i;
        for(i = 1; i <= columns.size(); ++i) {
            String columnName = columns.get(i).getText();
            if(columnName.equals(column)) {
                break;
            }
        }

        hoverOverColumnHeaderAndClick(i);

        if(order.equalsIgnoreCase("ascending")) {
            WebElement element = driver.findElement(By.xpath(XpathConstants.ADMIN_SORT_ASC_BUTTON));
            element.click();
        }
        else {
            WebElement element = driver.findElement(By.xpath(XpathConstants.ADMIN_SORT_DESC_BUTTON));
            element.click();
        }

        waitForLoad();
        buildAllRows();
    }

    /*
     TODO: Combine methods to display or hide columns rather than having 2 different methods
           and having to maintain both of them.
      */
    public void displayColumns(String[] columns) throws Exception {
        /*
           * Check whether column is already displayed otherwise add to the list
           * of columns that we will display
           */
        ArrayList<String> columnsToDisplay = new ArrayList<String>();

        for(int i = 0; i < columns.length; ++i) {
            if(allColumnHeaders.containsKey(columns[i])){
                System.out.println("Column " + columns[i] + " is already displayed!");
            }
            else {
                columnsToDisplay.add(columns[i]);
            }
        }

        if(columnsToDisplay.isEmpty()){
            System.out.println("All columns are already displayed!");
            return;
        }

        hoverOverColumnHeaderAndClick(1);

        /*
           * Hover over columns menu button then move to the right so the columns display
           * window doesn't disappear on us.
           */
        WebElement columnsMenu = driver.findElement(By.xpath(XpathConstants.ADMIN_COLUMNS_MENU));
        Actions hoverOverColumnsMenu = new Actions(driver);
        hoverOverColumnsMenu.moveToElement(columnsMenu).build().perform();
        Actions movePointerRight = new Actions(driver);
        movePointerRight.moveByOffset(100, 0).build().perform();


        /*
           * Find the column to display and click checkbox
           */
        for(int i = 0; i < columnsToDisplay.size(); ++i) {
            String columnToDisplayXpath = "/html/body/div[contains(@class,\"x-ignore x-menu x-component\")][2]/div[contains(@class,\"menu\")]/div/a[text()=\"" + columnsToDisplay.get(i) + "\"]/img";
            WebElement columnToDisplay = driver.findElement(By.xpath(columnToDisplayXpath));
            columnToDisplay.click();
            Thread.sleep(250);
        }

        clickAway();
        buildColumnsMap();
        buildAllRows();
    }

    private void clickAway() throws InterruptedException {
        /*
        * Click away to make somewhere
        */
        WebElement logo = driver.findElement(By.xpath(XpathConstants.TONE_LOGO));
        logo.click();
        Thread.sleep(250);
    }

    public void hideColumns(String[] columnsToHide) throws Exception {
        /*
           * Check whether column is already displayed
           */
        ArrayList<String> finalColumnsToHide = new ArrayList<String>();

        for(int i = 0; i < columnsToHide.length; ++i) {
            boolean columnIsAlreadyDisplayed = allColumnHeaders.containsKey(columnsToHide[i]);
            if(columnIsAlreadyDisplayed == true){
                finalColumnsToHide.add(columnsToHide[i]);
            }
            else {
                System.out.println("Column " + columnsToHide[i] + " is already hidden!");
            }
        }

        if(finalColumnsToHide.isEmpty()){
            System.out.println("All columns are already hidden!");
            return;
        }

        hoverOverColumnHeaderAndClick(1);

        /*
           * Hover over columns menu button then move to the right so the columns display
           * window doesn't disappear on us.
           */
        WebElement columnsMenu = driver.findElement(By.xpath(XpathConstants.ADMIN_COLUMNS_MENU));
        Actions builder = new Actions(driver);
        builder.moveToElement(columnsMenu).build().perform();
        Actions movePointerRight = new Actions(driver);
        movePointerRight.moveByOffset(100, 0).build().perform();

        /*
           * Find the column to display and click checkbox
           */
        for(int i = 0; i < finalColumnsToHide.size(); ++i) {
            String columnToDisplayXpath = "/html/body/div[contains(@class,\"x-ignore x-menu x-component\")][2]/div[contains(@class,\"menu\")]/div/a[text()=\"" + finalColumnsToHide.get(i) + "\"]/img";
            WebElement columnToDisplay = driver.findElement(By.xpath(columnToDisplayXpath));
            columnToDisplay.click();
            Thread.sleep(500);
        }

        clickAway();
        buildColumnsMap();
        buildAllRows();
    }

    public void resetColumns() throws Exception {
        hoverOverColumnHeaderAndClick(1);

        /*
           * Find "Reset Columns Preferences" link and click it
           */
        WebElement resetColumns = driver.findElement(By.xpath(XpathConstants.ADMIN_RESET_COLUMNS_BUTTON));
        resetColumns.click();
        buildColumnsMap();
        buildAllRows();
    }

    private void hoverOverColumnHeaderAndClick(int columnNumber) throws InterruptedException {
        /*
           * Hover over columnNumber column header
           */
        WebElement columnsRoot = driver.findElement(By.xpath(XpathConstants.ADMIN_COLUMNS_ROOT));
        WebElement columnHeader = columnsRoot.findElement(By.xpath("./td[" + columnNumber + "]/div"));
        scrollToElement(columnHeader);
        Actions builder = new Actions(driver);
        builder.moveToElement(columnHeader).build().perform(); // Bug in selenium, refer to https://github.com/SeleniumHQ/selenium/issues/2285 

        /*
           * Click on dropdown button after it appears
           */
        WebElement dropdownButton = columnsRoot.findElement(By.xpath("./td[" + columnNumber + "]/div/a"));
        dropdownButton.click();
        WebDriverWait waitForColumnsLink = new WebDriverWait(driver, 5);
        waitForColumnsLink.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathConstants.ADMIN_COLUMNS_MENU)));
    }

	public void clickEditButton() {
		WebElement element = driver.findElement(By.xpath(XpathConstants.ADMIN_EDIT_BUTTON));
		element.click();
	}
	
	public void clickCancelButton() {
		WebElement element = driver.findElement(By.xpath(XpathConstants.ADMIN_CANCEL_BUTTON));
		element.click();
	}

    public void clickDeleteButton() throws InterruptedException {
        WebElement deleteButton = driver.findElement(By.xpath(XpathConstants.ADMIN_DELETE_BUTTON));

        if( deleteButton.getAttribute("aria-disabled").equals("true")) {
        	throw new InterruptedException("Delete button is disabled, active alarms? children objects?");
        }
        else {
            deleteButton.click();
            
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.ignoring(NoAlertPresentException.class);
            wait.until(ExpectedConditions.alertIsPresent());
            
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }
        
        //TODO Use a driverwait here, should not be using thread sleep. If we don't wait a bit before the end of the method the center doesn't get deleted properly
        Thread.sleep(1000);
        
        // Rebuild grid items since item was deleted
        waitForLoad();
        buildAllRows();
    }

	public void clickCopyButton() throws Exception{
		WebElement copyButton = driver.findElement(By.xpath(XpathConstants.ADMIN_COPY_BUTTON));
		copyButton.click();
		waitForApplyButton();
		
		//TODO for some reason wait for apply button isn't sufficient
		Thread.sleep(1000);
	}

    public int getNumberOfPages() {
        WebElement bottomPanelItems = driver.findElement(By.xpath(XpathConstants.ADMIN_BOTTOM_PANEL_ROOT));

        WebElement numberOfPagesLabel = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-left\"]/table/tbody/tr/td[6]"));
        String ofPagesLabel = numberOfPagesLabel.getText();
        ofPagesLabel = ofPagesLabel.substring(ofPagesLabel.length() - 1);

        return Integer.parseInt(ofPagesLabel);
    }

    public void pageForward() {
        WebElement bottomPanelItems = driver.findElement(By.xpath(XpathConstants.ADMIN_BOTTOM_PANEL_ROOT));
        WebElement pageForwardButton = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-left\"]/table/tbody/tr/td[8]/button"));

        if(pageForwardButton.getAttribute("aria-disabled").equals("true")) {
            System.out.println("No more pages");
        }
        else {
            pageForwardButton.click();
        }

        buildAllRows();
    }

    public void pageBack() {
        WebElement bottomPanelItems = driver.findElement(By.xpath(XpathConstants.ADMIN_BOTTOM_PANEL_ROOT));
        WebElement pageBackButton = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-left\"]/table/tbody/tr/td[2]/button"));

        if(pageBackButton.getAttribute("aria-disabled").equals("true")) {
            System.out.println("No more pages");
        }
        else {
            pageBackButton.click();
        }

        buildAllRows();
    }

    public int getNumberOfItems() {
        WebElement bottomPanelItems = driver.findElement(By.xpath(XpathConstants.ADMIN_BOTTOM_PANEL_ROOT));
        WebElement displayingLabel = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-right\"]/table/tbody/tr/td/table/tbody/tr/td[5]/div"));

        String displayText = displayingLabel.getText();
        String[] displayTextPart = displayText.split(" ");

        return Integer.parseInt(displayTextPart[5]);
    }

    public int getResultsPerPage() {
        WebElement bottomPanelItems = driver.findElement(By.xpath(XpathConstants.ADMIN_BOTTOM_PANEL_ROOT));
        WebElement resultsPerPageInputBox = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-right\"]/table/tbody/tr/td/table/tbody/tr/td[1]/div/input"));

        String currentResultsPerPage = resultsPerPageInputBox.getText();
        return Integer.parseInt(currentResultsPerPage);
    }

    public void setResultsPerPage(String resultsPerPage) {
        /*
        I don't particularly like the xpath for resultsPerPageOptionsXpath because I think it can easily break, for the time being it's working
        but we should ask development to add an ID to the dropdown with the options for results per page.
         */
        String resultsPerPageOptionsXpath = "//div[starts-with(@class,\"x-combo-list\") and starts-with(@style,\"border-width\")]/div/div[text()=\"" + resultsPerPage + "\"]";
        WebElement bottomPanelItems = driver.findElement(By.xpath(XpathConstants.ADMIN_BOTTOM_PANEL_ROOT));
        WebElement resultsPerPageDropDown = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-right\"]/table/tbody/tr/td/table/tbody/tr/td[1]/div/img"));

        resultsPerPageDropDown.click();
        WebElement desiredResultsPerPage = driver.findElement(By.xpath(resultsPerPageOptionsXpath));
        desiredResultsPerPage.click();

        buildAllRows();
    }

    protected void buildColumnsMap(){
        allColumnHeaders = new HashMap<String,Integer>();
        positionToColumnName = new ArrayList<String>();

        WebElement columnsRoot = driver.findElement(By.xpath(XpathConstants.ADMIN_COLUMNS_HEADER_ROOT));
        List <WebElement> columnNames = columnsRoot.findElements(By.xpath("./td"));

        for(int i = 0; i < columnNames.size(); ++i){
            String columnName = columnNames.get(i).getText();
            allColumnHeaders.put(columnName, i);
            positionToColumnName.add(columnName);
        }
    }

    protected Integer buildAllRows(){
        allRows = new ArrayList<HashMap<String, String>>();

        gridRoot = driver.findElement(By.xpath(XpathConstants.ADMIN_GRID_ROOT));
        List <WebElement> rows = gridRoot.findElements(By.xpath("./div"));

        /**
         *  When the grid is empty we'll still return 1 single row which is of class type "x-grid-empty". In 
         *  this case don't do anything just return 0 as the number of rows built
         */
        if(rows.size() == 1 && ((WebElement)rows.get(0)).getAttribute("class").equals("x-grid-empty"))
        	return 0;
        	
        for(int i = 0; i < rows.size(); ++i) {
            HashMap<String, String> rowEntry = new HashMap<String, String>();
            List<WebElement> values = rows.get(i).findElements(By.xpath("./table/tbody/tr/td/div"));

            /*
                * I have to use allColumnHeaders.size() because the values list returns all columns
                * including the hidden ones which causes an outOfBound index exception
                */
            if(allColumnHeaders == null || allColumnHeaders.size() == 0) {
                throw new RuntimeException("Something went wrong, we don't have column headers for some reason");
            }

            for(int j = 0; j < allColumnHeaders.size(); ++j){
                String cellValue = values.get(j).getText();
                String columnName = positionToColumnName.get(j);
                rowEntry.put(columnName, cellValue);
            }
            allRows.add(rowEntry);
        }
        
        return allRows.size();
    }

    public ArrayList<HashMap<String, String>> getAllRows() {
        return allRows;
    }

    public boolean clickItem(String name) {
        if(!allColumnHeaders.containsKey("Name")) {
            throw new RuntimeException("\"Name\" column must be displayed");
        }

        String columnName;
        int rowInGrid;
        for(int i = 0; i < allRows.size(); ++i) {
            columnName = allRows.get(i).get("Name");
            if(columnName.equals(name)) {
                rowInGrid = i + 1;
                WebElement rowToClick = gridRoot.findElement(By.xpath("./div[" + rowInGrid + "]"));
                rowToClick.click();
                waitForApplyButton();
                return true;
            }
        }

        //TODO if we're unable to find the row to click we should actually throw an error not just return false
        return false;
    }

    private void waitForApplyButton() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XpathConstants.ADMIN_APPLY_BUTTON)));
        
        //TODO this is not appropriate but the wait until condition doesnt seem to be working 
        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    protected void waitForLoad() {
        ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.findElement(By.xpath(XpathConstants.ADMIN_REFRESH_BUTTON))
                        .getAttribute("aria-disabled").equals("false");
            }
        };

        try {
            WebDriverWait wait = new WebDriverWait(driver, 60);
            wait.until(condition);
        } catch(TimeoutException e) {
            System.out.println("Something failed to load");
            e.printStackTrace();
        }
    }

    protected boolean isFieldReadonly(WebElement field) {
        String readOnly = field.getAttribute("readonly");
        if(readOnly == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public void clickGeneralTab() {
        WebElement generalTab = driver.findElement(By.xpath(XpathConstants.ADMIN_DETAILS_GENERAL_TAB));
        generalTab.click();
    }

    public void clickReferencesTab() {
        WebElement referencesTab = driver.findElement(By.xpath(XpathConstants.ADMIN_DETAILS_REFS_TAB));
        referencesTab.click();
    }

    protected void waitForElement(String elementXpath) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementXpath)));
    }
    
    public String getAlertText() {
    	return driver.switchTo().alert().getText();
    }
    
    public void acceptAlert() {
    	driver.switchTo().alert().accept();
    }
    
//    public void clickCentersLink() {
//        WebElement centersLink = driver.findElement(By.xpath(XpathConstants.ADMIN_CENTERS_LINK));
//        centersLink.click();
//        waitForLoad();
//
//        updateGridForPage();
//    }
    
//    public void clickEntitiesLink() {
//    	WebElement entitiesLink = driver.findElement(By.xpath(XpathConstants.ADMIN_ENTITIES_LINK));
//    	entitiesLink.click();
//        waitForLoad();
//        
//        updateGridForPage();
//    }
    
//    public void clickCoveragesLink() {
//    	WebElement coveragesLink = driver.findElement(By.xpath(XpathConstants.ADMIN_COVERAGES_LINK));
//    	coveragesLink.click();
//        waitForLoad();
//
//        updateGridForPage();
//    }
    
    /* 
     * Update the grid for the calling object
     */
    protected void updateGridForPage() {
    	buildColumnsMap();
        buildAllRows();
    }
    
    protected void selectFromDropdown(String name, String dropDownButtonXpath) {
    	String nameInComboListXpath = "//div[starts-with(@class,\"x-combo-list\") and text()=\"" + name + "\"]";
    	
    	WebElement dropdownbutton = driver.findElement(By.xpath(dropDownButtonXpath));
        dropdownbutton.click();
        
        WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(nameInComboListXpath)));
        
        WebElement combolist = driver.findElement(By.xpath(nameInComboListXpath));
        combolist.click();
    }
}