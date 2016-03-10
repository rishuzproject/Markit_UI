package org.cerrid.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.cerrid.excelAutomation.CopyValues;
import org.cerrid.excelAutomation.SetCalculatedValues;
import org.cerrid.webAutomation.DataFields;
import org.cerrid.webAutomation.TestClass;
import org.cerrid.webAutomation.WebSiteConstants;

public class AutomationController {
	static Logger logger = Logger.getLogger(AutomationController.class);

	public Map<String, String> controller(String username, String password, String priceFilePath, String riskFilePath,
			String indicexType) {
		logger.info(":: Entered in controller ");
		Map<String, String> responseMap = new HashMap<>();
		TestClass testClass = null;
		try {
			CopyValues copyValues = new CopyValues();
			List<DataFields> dataList = copyValues.copyDataFromExcel(priceFilePath, riskFilePath, indicexType);
			testClass = new TestClass(username, password, riskFilePath, priceFilePath, dataList);
			testClass.login();
			testClass.changePage();
			testClass.calculateValues();
			SetCalculatedValues setCalculatedValues = new SetCalculatedValues();
			setCalculatedValues.readDataFile(dataList, riskFilePath);
			responseMap.put("status", "success");
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(" ---------------------------EXCEPTION - START------------------------ ");
			logger.log(Level.FATAL, e.getMessage(), e);
			logger.info(" ---------------------------EXCEPTION - END------------------------ ");
			responseMap.put("status", "fail");
			String message = "";
			if (e.getMessage().contains("Timeout")) {
				message = "Connection Timeout, Please Check Connection.";
			} else {
				message = e.getMessage();
			}
			responseMap.put("reason", message);
			return responseMap;
		} finally {
			if (null != testClass)
				logger.info("Log out ..");
			testClass.logout();
		}
	}

	public static void main(String[] args) {
		File dir = new File("D:\\cds");
		File[] files = dir.listFiles();
		List<File> cdsFiles = new ArrayList<>();
		if (files == null || files.length == 0) {
		}

		File lastModifiedFile;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().startsWith(WebSiteConstants.CDS_FILE_START_STRING)) {
				cdsFiles.add(files[i]);
			}
		}
		lastModifiedFile = cdsFiles.get(0);
		for (File file : cdsFiles) {
			if (lastModifiedFile.lastModified() < file.lastModified()) {
				lastModifiedFile = file;
			}
		}
		System.out.println(lastModifiedFile.getName());
	}
}
