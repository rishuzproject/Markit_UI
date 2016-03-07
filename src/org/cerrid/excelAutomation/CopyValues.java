package org.cerrid.excelAutomation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cerrid.webAutomation.DataFields;
import org.cerrid.webAutomation.WebSiteConstants;

public class CopyValues {

	private int pasteStartColumnNum = -1;
	private int pasteStartRowNum = -1;
	Logger logger = Logger.getLogger(CopyValues.class);

	private File getLatestFilefromDir(String dirPath) {

		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		List<File> cdsFiles = new ArrayList<>();
		if (files == null || files.length == 0) {
			return null;
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
		return lastModifiedFile;
	}

	public List<PriceData> readDataFile(File latestFile, String riskFilePath) {
		try {
			FileInputStream fileToReadFrom = new FileInputStream(latestFile);
			FileInputStream fileToPasteTo = new FileInputStream(new File(riskFilePath));

			XSSFWorkbook workbookToReadFrom = new XSSFWorkbook(fileToReadFrom);
			XSSFWorkbook workbookToPasteTo = new XSSFWorkbook(fileToPasteTo);

			XSSFSheet sheetToReadFrom = workbookToReadFrom.getSheetAt(0);
			XSSFSheet sheetToPasteTo = workbookToPasteTo.getSheet(WebSiteConstants.RISK_FILE_COPY_SHEET_NAME);

			List<PriceData> dfList = new ArrayList<>();

			getPasteNums(sheetToPasteTo);
			getRequiredIndices(sheetToPasteTo, dfList);
			deletePreviousData(sheetToPasteTo);

			PriceData price = null;

			for (int rowNum = sheetToReadFrom.getFirstRowNum() + 1, countOfRow = 0; rowNum <= sheetToReadFrom
					.getLastRowNum(); rowNum++, countOfRow++) {
				Row r = sheetToReadFrom.getRow(rowNum);
				if (r == null) {
					continue;
				}
				price = new PriceData();
				for (int cn = 0, countOfColumn = 0; cn < r.getLastCellNum(); cn++, countOfColumn++) {
					Cell sourceCell = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
					XSSFRow destR = sheetToPasteTo.getRow(pasteStartRowNum + countOfRow);
					if (destR == null) {
						destR = sheetToPasteTo.createRow(pasteStartRowNum + countOfRow);
					}
					XSSFCell destCell = destR.createCell(pasteStartColumnNum + countOfColumn);
					XSSFCellStyle style = workbookToPasteTo.createCellStyle();
					style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
					switch (countOfColumn) {
					case 0:
						destCell.setCellValue(sourceCell.getRichStringCellValue());
						price.setInvestment(sourceCell.getStringCellValue());
						break;
					case 1:
						destCell.setCellStyle(style);
						destCell.setCellValue(Double.parseDouble(sourceCell.getStringCellValue()));
						price.setPrice(100 - Double.parseDouble(sourceCell.getStringCellValue()));
						break;
					default:
						destCell.setCellValue(sourceCell.getRichStringCellValue());
						break;
					}
				}
				calculateAveragePrice(dfList, price);
			}

			fileToReadFrom.close();
			FileOutputStream out = new FileOutputStream(new File(riskFilePath));
			workbookToPasteTo.write(out);
			out.close();
			workbookToReadFrom.close();
			workbookToPasteTo.close();
			return dfList;

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}

	private void deletePreviousData(XSSFSheet sheet) {
		for (int rowNum = sheet.getFirstRowNum() + 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
			Row r = sheet.getRow(rowNum);
			if (r == null) {
				continue;
			} else {
				for (int k = 0; k < 4; k++) {
					Cell c = r.getCell(pasteStartColumnNum + k, Row.RETURN_BLANK_AS_NULL);
					if (c != null) {
						r.removeCell(c);
					}
				}
			}
		}
	}

	private void getRequiredIndices(XSSFSheet sheet, List<PriceData> dfList) {
		for (int rowNum = sheet.getFirstRowNum() + 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
			Row r = sheet.getRow(rowNum);
			if (r == null) {
				continue;
			} else {
				Cell c = r.getCell(0, Row.RETURN_BLANK_AS_NULL);
				if (c != null && c.getStringCellValue().startsWith(WebSiteConstants.INDICES_TYPE_CMBX)) {
					PriceData pd = new PriceData();
					pd.setInvestment(c.getStringCellValue());
					pd.setCount(0);
					pd.setPrice(0);
					pd.setTotal(0);
					dfList.add(pd);
				}
			}
		}
	}

	private void calculateAveragePrice(List<PriceData> dfList, PriceData price) {
		for (PriceData pd : dfList) {
			if (pd.getInvestment().equalsIgnoreCase(price.getInvestment())) {
				pd.setCount(pd.getCount() + 1);
				pd.setTotal(pd.getTotal() + price.getPrice());
				pd.setPrice(pd.getTotal() / pd.getCount());
			}
		}
	}

	private void getPasteNums(XSSFSheet sheetToPasteTo) {
		for (int rowNum = sheetToPasteTo.getFirstRowNum(); rowNum <= sheetToPasteTo.getLastRowNum(); rowNum++) {
			Row r = sheetToPasteTo.getRow(rowNum);
			if (r == null) {
				continue;
			}
			for (int cn = 0; cn < r.getLastCellNum(); cn++) {
				Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
				if (c == null) {
					// The spreadsheet is empty in this cell
				} else {
					try {
						if (c.getStringCellValue().equalsIgnoreCase("Investment")) {
							pasteStartColumnNum = cn + 3;
							pasteStartRowNum = rowNum + 1;
							return;
						}
					} catch (Exception e) {
						logger.error(e);
						e.printStackTrace();
						continue;
					}
				}
			}
		}
	}

	public List<DataFields> copyDataFromExcel(String priceFilePath, String riskFilePath, String indexType) {
		logger.info("## copyDataFromExcel --> priceFilePath ->" + priceFilePath + "  --riskFilePath ->" + riskFilePath);
		CopyValues copyValues = new CopyValues();
		File latestFile = copyValues.getLatestFilefromDir(priceFilePath);
		List<PriceData> pdList = copyValues.readDataFile(latestFile, riskFilePath);
		List<DataFields> dataList = new ArrayList<>();
		DataFields df = null;
		for (PriceData pd : pdList) {
			df = new DataFields(indexType, pd.getInvestment(), pd.getPrice());
			dataList.add(df);
		}
		BufferedWriter out = null;
		logger.info("## Before try");
		try {
			logger.info("## Writing pshell.at");
			FileWriter fstream = new FileWriter("pshell.bat");
			out = new BufferedWriter(fstream);
			logger.info("## Creating pshell.ps1");
			exportResource("pshell.ps1");
			String path = "start cmd.exe /c \"powershell -ExecutionPolicy ByPass .\\pshell.ps1 '" + riskFilePath + "'\"";
			logger.info("## in try path ->" + path);
			out.write("\n" + path);
			out.close();
			Runtime.getRuntime().exec("cmd /k start pshell.bat");
		} catch (IOException e) {
			logger.error(e);
			System.err.println("Error: " + e.getMessage());
		}
		return dataList;
	}

	private void exportResource(String resourceName) {
		InputStream stream = null;
		OutputStream resStreamOut = null;
		String jarFolder = "";
		try {
			stream = CopyValues.class.getResourceAsStream(resourceName);
			if (stream == null) {
				logger.info("Could not find resource " + resourceName);
				throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
			}
			int readBytes;
			byte[] buffer = new byte[4096];
			File currentDir = new File("");
			jarFolder = currentDir.getAbsolutePath();
			logger.info("exporting  resource tp" + jarFolder + "/" + resourceName);
			resStreamOut = new FileOutputStream(jarFolder + "/" + resourceName);
			while ((readBytes = stream.read(buffer)) > 0) {
				resStreamOut.write(buffer, 0, readBytes);
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			try {
				stream.close();
				resStreamOut.close();
			} catch (IOException e) {
				logger.error(" ## Exception While closing Streams" + e);
				e.printStackTrace();
			}

		}
	}
}
