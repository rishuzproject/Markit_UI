package org.cerrid.webAutomation;

public class WebSiteConstants {

	public static final String url = "https://beta.hub.com/";

	public static final String USER_NAME_ELEMENT_ID = "user_id";
	public static final String PASSWORD_ELEMENT_ID = "user_password";

	public static final String PROVIDER_ELEMENT_XPATH = "//*[@id='hub-dock']/div[1]/div[6]";
	public static final String SETTING_ELEMENT_XPATH = "//*[@id='hub-dock']/div[1]/div[1]";
	public static final String MARKIT_ELEMENT_XPATH = "//*[@id='hub-dock-pane']/div[2]/div/div[2]/div/div[1]/div/div/div[4]/div[2]";

	// change name
	public static final String FIRST_IFRAME_ID = "provider-frameUXS";
	public static final String SECOND_IFRAME_ID = "MFrame";

	// make enum
	public static final String INDICES_TYPE_CMBX = "CMBX";
	public static final String INDICES_TYPE_CMBX_TAB_XPATH = "//*[@id='content-panel']/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr/td[3]/div";
	public static final String INDICES_TYPE_CMBX_XPATH = "//*[@id='gwt-debug-CMBX_indexListBox']/select";

	public static final String PORTFOLIO_HEADER_CLASS_NAME = "portfoliosHeader";

	public static final String SPREAD_ID = "gwt-debug-CMBX_spread";
	public static final String PRICE_ID = "gwt-debug-CMBX_price";
	public static final String CALCULATE_BUTTON_ID = "gwt-debug-CMBX_calcBtn";

	public static final String SPREAD_VALUE_XPATH = "//*[@id='content-panel']/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td[6]";

	public static final String WAL_VALUE_XPATH = "//*[@id='content-panel']/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td[7]";

	public static final String PV01_VALUE_XPATH = "//*[@id='content-panel']/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td[8]";

	public static final String RISK_CALC_SPREAD_COLUMN = "Markit Spread";

	public static final String RISK_FILE_COPY_SHEET_NAME = "PricingDatafrom_GloebeOp";

	public static final String CDS_FILE_START_STRING = "cds_prices";
	
	public static final String REDIRECT_URL = "https://beta.hub.com/?compatibility=8&openprovider=UXS#/provider";
	
	public static final String WAIT_IMAGE_XPATH = "//*[@id='content-panel']/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/div/table/tbody/tr/td/div/table/tbody/tr/td[2]/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/div/div/img";
	
	public static final String TAB_CLASS_NAME = "MNavigationBarLabelSmall";
	
	public static final String LOGOUT_DROPDOWN = "blueAuctionMenuButton";
	 
	public static final String LOGOUT_BUTTON = "gwt-Anchor";
	
	public static final String LOGIN_BUTTON = "loginButton";
}
