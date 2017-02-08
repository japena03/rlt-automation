
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseAdmin extends BaseSite {
	protected WebDriver driver;
	private String refreshButtonXpath = "((//div[@id=\"gridPager\"])//button[@type=\"button\"])[5]";
	private String adminApplyButtonXpath = "((//div[@class=\"x-panel-footer\"])//button)[3]";
    private String columnsRootXpath = "/html/body/div[@id=\"bodyContentOuter\"]/div/div[@id=\"main\"]/div[@id=\"mainContent\"]/div[@id=\"mainContentGridPanel\"]//div[@id=\"grid\"]//div[@class=\"x-grid3-header\"]//table/tbody/tr";
    private String filterButtonDdXpath = "(//div[@id=\"x-menu-el-FilterRowUI\"]//img)[1]";
    private String bottomPanelRootXpath = "//div[@id=\"gridPager\"]/table/tbody/tr";
    private String columnsMenuXpath = "(//div[@class=\" x-ignore x-menu x-component\"]//a)[3]";
    private HashMap<String,Integer> allColumnHeaders;
    private ArrayList<String> positionToColumnName;
    private ArrayList<HashMap<String, String>> allRows;
    private WebElement gridRoot;

    BaseAdmin(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

	public void clickRefreshButton() {
		WebElement refreshButton = driver.findElement(By.xpath(refreshButtonXpath));
        if(refreshButton.getAttribute("aria-disabled").equals("false")) {
            refreshButton.click();
            waitForLoad();
        }
        else {
            System.out.println("Unable to refresh, button is disabled");
        }
	}
	
	public void clickNewButton() {
        String adminNewButtonXpath = "//*[@id=\"new\"]/tbody/tr[2]/td[2]/em/button";
		WebElement element = driver.findElement(By.xpath(adminNewButtonXpath));
		element.click();
        waitForApplyButton();
	}
	
	public void clickApplyButton() throws InterruptedException {
        WebElement applyButton = driver.findElement(By.xpath(adminApplyButtonXpath));
		applyButton.click();
        Thread.sleep(500);
        //checkForRequiredFields();
		waitForLoad();
        buildAllRows();
	}

    private void clickFilterButton() throws Exception{
        String filterButtonXpath = "(//div[@id=\"mainContentTop\"]//button)[4]";
        WebElement filterButton = driver.findElement(By.xpath(filterButtonXpath));
        filterButton.click();

        Thread.sleep(500);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(filterButtonDdXpath)));
    }
	
	public int createFilter(String column, String comparison, String value) throws Exception {
        String rowsGridRootXpath = "/html/body/div[@id=\"bodyContentOuter\"]/div/div/div[@id=\"mainContent\"]/div[@id=\"mainContentGridPanel\"]//div[@class=\"x-grid3-body\"]";
		String comparisonDropdownButton = "(//div[@id=\"x-menu-el-FilterRowUI\"]//img)[2]";
		String filterColumnValueXpath = "(//div[@class=\" x-view x-combo-list-inner x-component x-unselectable\"])//div[text()=\"" + column + "\"]";
		String filterComparisonValueXpath = "(//div[@class=\" x-view x-combo-list-inner x-component x-unselectable\"])//div[text()=\"" + comparison + "\"]";
		String filterValueXpath = "(//div[@id=\"FilterRowUI\"]//input)[3]";
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element;

        clearFilters();
        clickFilterButton();

		// Select a column from the dropdown based on what was passed in
		element = driver.findElement(By.xpath(filterButtonDdXpath));
		element.click();
		Thread.sleep(1000);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(filterColumnValueXpath)));
		} catch (TimeoutException e) {}
		element = driver.findElement(By.xpath(filterColumnValueXpath));
		element.click();
		Thread.sleep(1000);
		
		// Select a comparison value from the dropdown based on what was passed in
		element = driver.findElement(By.xpath(comparisonDropdownButton));
		element.click();
		Thread.sleep(1000);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(filterComparisonValueXpath)));
		} catch (TimeoutException e) {}
		element = driver.findElement(By.xpath(filterComparisonValueXpath));
		Thread.sleep(1000);
		element.click();
		
		// Input value to filter for
		element = driver.findElement(By.xpath(filterValueXpath));
		element.sendKeys(value);

        waitForLoad();
        int rows = getNumberOfItems();
        return rows;
	}
	
	public void clearFilters() {
		String clearFilterButtonXpath = "(//div[@id=\"mainContentTop\"]//button)[5]";
		WebElement clearFiltersButton = driver.findElement(By.xpath(clearFilterButtonXpath));
		clearFiltersButton.click();
		waitForLoad();
	}

    private void scrollToElement(WebElement element){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void sort(String column, String order) throws InterruptedException {
        String ascendingXpath = "(//div[@class=\" x-ignore x-menu x-component\"]//a)[1]";
        String descendingXpath = "(//div[@class=\" x-ignore x-menu x-component\"]//a)[2]";

        WebElement columnsRoot = driver.findElement(By.xpath(columnsRootXpath));
        List <WebElement> columns = columnsRoot.findElements(By.xpath("./td/div/span"));

        if(columns.size() == 0) {
            System.out.println("Couldn't find column headers, please make sure to first select a report");
            return;
        }

        int i;
        for(i = 0; i < columns.size(); ++i) {
            String columnName = columns.get(i).getText();
            if(columnName.equals(column)) {
                i = i + 1;
                break;
            }
        }

        hoverOverColumnHeaderAndClick(i);

        if(order.equalsIgnoreCase("ascending")) {
            WebElement element = driver.findElement(By.xpath(ascendingXpath));
            element.click();
        }
        else {
            WebElement element = driver.findElement(By.xpath(descendingXpath));
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
        String columnsMenuXpath = "(//div[@class=\" x-ignore x-menu x-component\"]//a)[3]";
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
        WebElement columnsMenu = driver.findElement(By.xpath(columnsMenuXpath));
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
        WebElement logo = driver.findElement(By.xpath(TONE_LOGO_XPATH));
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
        WebElement columnsMenu = driver.findElement(By.xpath(columnsMenuXpath));
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
        String resetColumnsXpath = "(//div[@class=\" x-ignore x-menu x-component\"]//a)[4]";

        hoverOverColumnHeaderAndClick(1);

        /*
           * Find "Reset Columns Preferences" link and click it
           */
        WebElement resetColumns = driver.findElement(By.xpath(resetColumnsXpath));
        resetColumns.click();
        buildColumnsMap();
        buildAllRows();
    }

    private void hoverOverColumnHeaderAndClick(int columnNumber) throws InterruptedException {
        /*
           * Hover over columnNumber column header
           */
        WebElement columnsRoot = driver.findElement(By.xpath(columnsRootXpath));
        WebElement columnHeader = columnsRoot.findElement(By.xpath("./td[" + columnNumber + "]/div"));
        scrollToElement(columnHeader);
        Actions builder = new Actions(driver);
        builder.moveToElement(columnHeader).build().perform();

        /*
           * Click on dropdown button after it appears
           */
        WebElement dropdownButton = columnsRoot.findElement(By.xpath("./td[" + columnNumber + "]/div/a"));
        dropdownButton.click();
        WebDriverWait waitForColumnsLink = new WebDriverWait(driver, 5);
        waitForColumnsLink.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(columnsMenuXpath)));
    }

	public void clickEditButton() {
		String editButtonXpath = "(//button[@type=\"button\"])[11]";
		WebElement element = driver.findElement(By.xpath(editButtonXpath));
		element.click();
	}
	
	public void clickCancelButton() {
		String cancelButtonXpath = "(//button[@type=\"button\"])[12]";
		WebElement element = driver.findElement(By.xpath(cancelButtonXpath));
		element.click();
	}

    public void clickDeleteButton() throws InterruptedException {
        String deleteButtonXpath = "(//div[@id=\"mainContentTop\"]//button)[2]";
        WebElement deleteButton = driver.findElement(By.xpath(deleteButtonXpath));

        if( deleteButton.getAttribute("aria-disabled").equals("true")) {
            System.out.println("Delete button is disabled, active alarms? children objects?");
        }
        else {
            deleteButton.click();
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }
    }

	public void clickCopyButton() throws Exception{
		String copyButtonXpath = "//*[@id=\"copy\"]/tbody/tr[2]/td[2]/em/button";
		WebElement copyButton = driver.findElement(By.xpath(copyButtonXpath));
		copyButton.click();
		waitForApplyButton();
	}

    public int getNumberOfPages() {
        WebElement bottomPanelItems = driver.findElement(By.xpath(bottomPanelRootXpath));

        WebElement numberOfPagesLabel = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-left\"]/table/tbody/tr/td[6]"));
        String ofPagesLabel = numberOfPagesLabel.getText();
        ofPagesLabel = ofPagesLabel.substring(ofPagesLabel.length() - 1);

        return Integer.parseInt(ofPagesLabel);
    }

    public void pageForward() {
        WebElement bottomPanelItems = driver.findElement(By.xpath(bottomPanelRootXpath));
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
        WebElement bottomPanelItems = driver.findElement(By.xpath(bottomPanelRootXpath));
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
        WebElement bottomPanelItems = driver.findElement(By.xpath(bottomPanelRootXpath));
        WebElement displayingLabel = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-right\"/table/tbody/tr/td/table/tbody/tr/td[5]/div"));

        String displayText = displayingLabel.getText();
        displayText = displayText.substring(displayText.length() - 1);

        return Integer.parseInt(displayText);
    }

    public int getResultsPerPage() {
        WebElement bottomPanelItems = driver.findElement(By.xpath(bottomPanelRootXpath));
        WebElement resultsPerPageInputBox = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-right\"/table/tbody/tr/td/table/tbody/tr/td[1]/div/input"));

        String currentResultsPerPage = resultsPerPageInputBox.getText();
        return Integer.parseInt(currentResultsPerPage);
    }

    public void setResultsPerPage(String resultsPerPage) {
        /*
        I don't particularly like the xpath for resultsPerPageOptionsXpath because I think it can easily break, for the time being it's working
        but we should ask development to add an ID to the dropdown with the options for results per page.
         */
        String resultsPerPageOptionsXpath = "//div[starts-with(@class,\"x-combo-list\") and starts-with(@style,\"border-width\")]/div/div[text()=\"" + resultsPerPage + "\"]";
        WebElement bottomPanelItems = driver.findElement(By.xpath(bottomPanelRootXpath));
        WebElement resultsPerPageDropDown = bottomPanelItems.findElement(By.xpath("./td[@class=\"x-toolbar-right\"/table/tbody/tr/td/table/tbody/tr/td[1]/div/img"));

        resultsPerPageDropDown.click();
        WebElement desiredResultsPerPage = driver.findElement(By.xpath(resultsPerPageOptionsXpath));
        desiredResultsPerPage.click();

        buildAllRows();
    }

    protected void buildColumnsMap(){
        allColumnHeaders = new HashMap<String,Integer>();
        positionToColumnName = new ArrayList<String>();
        String columnHeadersRootXpath = "//tr[@class=\"x-grid3-hd-row\"]";

        WebElement columnsRoot = driver.findElement(By.xpath(columnHeadersRootXpath));
        List <WebElement> columnNames = columnsRoot.findElements(By.xpath("./td"));

        for(int i = 0; i < columnNames.size(); ++i){
            String columnName = columnNames.get(i).getText();
            allColumnHeaders.put(columnName, i);
            positionToColumnName.add(columnName);
        }
    }

    protected void buildAllRows(){
        String gridRootXpath = "//div[@class=\"x-grid3-body\"]";
        allRows = new ArrayList<HashMap<String, String>>();

        gridRoot = driver.findElement(By.xpath(gridRootXpath));
        List <WebElement> rows = gridRoot.findElements(By.xpath("./div"));

        for(int i = 0; i < rows.size(); ++i){
            HashMap<String, String> rowEntry = new HashMap<String, String>();
            List<WebElement> values = rows.get(i).findElements(By.xpath("./table/tbody/tr/td/div"));

            /*
                * I have to use allColumnHeaders.size() because the values list returns all columns
                * including the hidden ones which causes an outOfBound index exception
                */
            if(allColumnHeaders.size() == 0 || allColumnHeaders == null) {
                throw new RuntimeException("Something went wrong, we don't have column headers for some reason");
            }

            for(int j = 0; j < allColumnHeaders.size(); ++j){
                String cellValue = values.get(j).getText();
                String columnName = positionToColumnName.get(j);
                rowEntry.put(columnName, cellValue);
            }
            allRows.add(rowEntry);
        }
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

        return false;
    }

    private void waitForApplyButton() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(adminApplyButtonXpath)));
    }

    protected void waitForLoad() {
        ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.findElement(By.xpath(refreshButtonXpath))
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
        String generalTabXpath = "//li[@id=\"detailsTabPanel__tabItemGeneral\"]//span[text()=\"General\"]";
        WebElement generalTab = driver.findElement(By.xpath(generalTabXpath));
        generalTab.click();
    }

    public void clickReferencesTab() {
        String referencesTabXpath = "//li[@id=\"detailsTabPanel__tabItemReferences\"]//span[text()=\"References\"]";
        WebElement referencesTab = driver.findElement(By.xpath(referencesTabXpath));
        referencesTab.click();
    }

    protected void waitForElement(String elementXpath) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementXpath)));
    }

    private void checkForRequiredFields() {
        /*
        I may run into a problem with required fields in non-active tabs as those will be hidden icons. I could potentially search for
        the red lettering on each tab also as a way to identify missed fields.
         */
        String requiredFieldIconXpath = "//div[contains(@class,\"tone-invalid-icon\") and not(contains(@class,\"tone-empty-icon\"))]";
        WebElement requiredField = driver.findElement(By.xpath(requiredFieldIconXpath));
        if(requiredField != null) {
            throw new RuntimeException("Required fields were left blank!");
        }
    }
}