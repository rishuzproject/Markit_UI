package org.cerrid.excelAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cerrid.webAutomation.DataFields;
import org.cerrid.webAutomation.WebSiteConstants;

public class SetCalculatedValues {

	static Logger logger = Logger.getLogger(SetCalculatedValues.class);
	private int startingRowNum;
	private int indexColumnNum;
	private int spreadColumnNum;
	private int walColumnNum;
	private int pv01ColumnNum;

	public int getStartingRowNum() {
		return startingRowNum;
	}

	public void setStartingRowNum(int startingRowNum) {
		this.startingRowNum = startingRowNum;
	}

	public int getIndexColumnNum() {
		return indexColumnNum;
	}

	public void setIndexColumnNum(int indexColumnNum) {
		this.indexColumnNum = indexColumnNum;
	}

	public int getSpreadColumnNum() {
		return spreadColumnNum;
	}

	public void setSpreadColumnNum(int spreadColumnNum) {
		this.spreadColumnNum = spreadColumnNum;
	}

	public int getWalColumnNum() {
		return walColumnNum;
	}

	public void setWalColumnNum(int walColumnNum) {
		this.walColumnNum = walColumnNum;
	}

	public int getPv01ColumnNum() {
		return pv01ColumnNum;
	}

	public void setPv01ColumnNum(int pv01ColumnNum) {
		this.pv01ColumnNum = pv01ColumnNum;
	}

	private void getStartingRowColumn(XSSFSheet sheet) {

		Iterator<Row> rowIterator = sheet.iterator();
		int rowNum = 0;
		while (rowIterator.hasNext()) {
			rowNum++;
			Row r = rowIterator.next();
			if (r == null) {
				continue;
			}

			int lastColumn = r.getLastCellNum();

			for (int cn = 0; cn < lastColumn; cn++) {
				Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
				if (c == null) {
					// The spreadsheet is empty in this cell
				} else {
					try {
						if (c.getStringCellValue().equalsIgnoreCase(WebSiteConstants.RISK_CALC_SPREAD_COLUMN)) {
							startingRowNum = rowNum + 1;
							indexColumnNum = cn - 12;
							spreadColumnNum = cn;
							walColumnNum = cn + 1;
							pv01ColumnNum = cn + 2;
						}
					} catch (Exception e) {
						logger.error(e);
						continue;
					}
				}
			}
		}
	}

	public void readDataFile(List<DataFields> dataFieldsList, String riskPath) {
		try {
			FileInputStream file = new FileInputStream(new File(riskPath));

			XSSFWorkbook workbook = new XSSFWorkbook(file);

			XSSFSheet sheet = workbook.getSheet("FinalUploadSheet");

			getStartingRowColumn(sheet);
			setNewValues(sheet, dataFieldsList);
			file.close();
			FileOutputStream out = new FileOutputStream(new File(riskPath));
			workbook.write(out);
			out.close();
			workbook.close();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	private void setNewValues(XSSFSheet sheet, List<DataFields> dataFieldsList) {
		for (int rowNum = startingRowNum; rowNum < sheet.getLastRowNum(); rowNum++) {
			Row r = sheet.getRow(rowNum);
			if (r == null) {
				continue;
			}

			Cell c = r.getCell(indexColumnNum);
			for (DataFields dataField : dataFieldsList) {
				try {
					if (c.getStringCellValue().equalsIgnoreCase(dataField.getIndices())) {
						Cell spreadCell = r.getCell(spreadColumnNum);
						Cell walCell = r.getCell(walColumnNum);
						Cell pv01Cell = r.getCell(pv01ColumnNum);
						spreadCell.setCellType(0);
						walCell.setCellType(0);
						pv01Cell.setCellType(0);
						spreadCell.setCellValue(dataField.getCalculatedSpreadValue());
						walCell.setCellValue(dataField.getCalculatedWalValue());
						pv01Cell.setCellValue(dataField.getCalculatedPV01Value());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
