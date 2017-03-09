

public class XpathConstants {

	private XpathConstants() {}
	
	// Xpaths available to every page
	public static String TONE_LOGO = "/html/body/div[@id=\"topNavDiv\"]/div[@id=\"titleOuter\"]/img[@id=\"topNavLogo\"]";
	public static String GENERAL_ADMIN_LINK = "//span[@id=\"adminMenu\"]/a[1]";
	
	// Admin xpaths
	public static String ADMIN_REFRESH_BUTTON = "((//div[@id=\"gridPager\"])//button[@type=\"button\"])[5]";
	
    public static String ADMIN_BOTTOM_PANEL_ROOT = "//div[@id=\"gridPager\"]/table/tbody/tr";
    public static String ADMIN_COLUMNS_MENU = "(//div[@class=\" x-ignore x-menu x-component\"]//a)[3]";
    
    public static String ADMIN_NEW_BUTTON = "//*[@id=\"new\"]/tbody/tr[2]/td[2]/em/button";
    public static String ADMIN_DELETE_BUTTON = "(//div[@id=\"mainContentTop\"]//button)[2]";
    public static String ADMIN_COPY_BUTTON = "//*[@id=\"copy\"]/tbody/tr[2]/td[2]/em/button";
    public static String ADMIN_FILTER_BUTTON = "(//div[@id=\"mainContentTop\"]//button)[4]";
    public static String ADMIN_FILTER_COLOUMN_DROPDOWN_BUTTON = "(//div[@id=\"x-menu-el-FilterRowUI\"]//img)[1]";
    public static String ADMIN_FILTER_COMP_DROPDOWN_BUTTON = "(//div[@id=\"x-menu-el-FilterRowUI\"]//img)[2]";
    public static String ADMIN_FILTER_VALUE_TEXTBOX = "(//div[@id=\"FilterRowUI\"]//input)[3]";
    public static String ADMIN_FILTER_CLEAR_BUTTON = "(//div[@id=\"mainContentTop\"]//button)[5]";
    
    public static String ADMIN_GRID_ROOT = "//div[@class=\"x-grid3-body\"]";
    public static String ADMIN_COLUMNS_ROOT = "/html/body/div[@id=\"bodyContentOuter\"]/div/div[@id=\"main\"]/div[@id=\"mainContent\"]/div[@id=\"mainContentGridPanel\"]//div[@id=\"grid\"]//div[@class=\"x-grid3-header\"]//table/tbody/tr";
    public static String ADMIN_COLUMNS_HEADER_ROOT = "//tr[@class=\"x-grid3-hd-row\"]";
    public static String ADMIN_SORT_ASC_BUTTON = "(//div[@class=\" x-ignore x-menu x-component\"]//a)[1]";
    public static String ADMIN_SORT_DESC_BUTTON = "(//div[@class=\" x-ignore x-menu x-component\"]//a)[2]";
    public static String ADMIN_RESET_COLUMNS_BUTTON = "(//div[@class=\" x-ignore x-menu x-component\"]//a)[4]";
    
    public static String ADMIN_EDIT_BUTTON = "//button[@id=\"DetailsPane-Button-Edit\"]";
    public static String ADMIN_CANCEL_BUTTON = "//button[@id=\"DetailsPane-Button-Cancel\"]";
    public static String ADMIN_APPLY_BUTTON = "//button[@id=\"DetailsPane-Button-Apply\"]";
    
    // Tabs 
    public static String ADMIN_DETAILS_GENERAL_TAB = "//li[@id=\"detailsTabPanel__tabItemGeneral\"]//span[text()=\"General\"]";
    public static String ADMIN_DETAILS_REFS_TAB = "//li[@id=\"detailsTabPanel__tabItemReferences\"]//span[text()=\"References\"]";
    
    // Centers Page Details Fields
    public static String CENTERS_NAME_FIELD = "//div[@id=\"detailsTabPanel\"]//td[text()=\"Name: \"]/..//input";
    public static String CENTERS_PARENT_CENTER_FIELD = "//div[@id=\"detailsTabPanel\"]//td[text()=\"Parent Center: \"]/..//input";
    public static String CENTERS_COVERAGE_FIELD = "//div[@id=\"detailsTabPanel\"]//td[text()=\"Coverage: \"]/..//input";
    public static String CENTERS_REFS_CENTER_CHILDREN_BUTTON = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Centers\")]";
    public static String CENTERS_REFS_ENTITIES_BUTTON  = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Entities\")]";
    public static String CENTERS_REFS_RTCP_COLLECTORS_BUTTON = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"RTCP\")]";
    public static String CENTERS_REFS_PQOS_COLLECTORS_BUTTON = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Passive\")]";
    public static String CENTERS_REFS_AVAYA_CDR_COLLECTORS_BUTTON = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Avaya\")]";
    public static String CENTERS_REFS_LINKS_BUTTON = "//div[@id=\"tabItemReferences\"]//button[contains(text(),\"Links\")]";
    public static String CENTERS_PARENT_CENTER_DROPDOWN_BUTTON = "((//div[starts-with(@role, \"combobox\")])//img)[2]";
    public static String CENTERS_COVERAGE_DROPDOWN_BUTTON = "((//div[starts-with(@role, \"combobox\")])//img)[3]";
}
