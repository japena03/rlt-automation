
public class XpathConstants {

	private XpathConstants() {}
	
	// Xpaths available to every page
	public static String TONE_LOGO = "/html/body/div[@id=\"topNavDiv\"]/div[@id=\"titleOuter\"]/img[@id=\"topNavLogo\"]";
	public static String GENERAL_ADMIN_LINK = "//span[@id=\"adminMenu\"]/a[1]";
	
	// Admin xpaths
	public static String ADMIN_REFRESH_BUTTON = "((//div[@id=\"gridPager\"])//button[@type=\"button\"])[5]";
	
}
