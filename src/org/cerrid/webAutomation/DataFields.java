package org.cerrid.webAutomation;

public class DataFields {
	private double calculatedSpreadValue;
	private double calculatedWalValue;
	private double calculatedPV01Value;
	private String indicesType;
	private String indices;
	private double inputPrice;

	public DataFields(String indicesType, String indices, double inputPrice) {
		this.indicesType = indicesType;
		this.indices = indices;
		this.inputPrice = inputPrice;
	}

	public double getCalculatedSpreadValue() {
		return calculatedSpreadValue;
	}

	public void setCalculatedSpreadValue(double calculatedSpreadValue) {
		this.calculatedSpreadValue = calculatedSpreadValue;
	}

	public double getCalculatedWalValue() {
		return calculatedWalValue;
	}

	public void setCalculatedWalValue(double calculatedWalValue) {
		this.calculatedWalValue = calculatedWalValue;
	}

	public double getCalculatedPV01Value() {
		return calculatedPV01Value;
	}

	public void setCalculatedPV01Value(double calculatedPV01Value) {
		this.calculatedPV01Value = calculatedPV01Value;
	}

	public String getIndicesType() {
		return indicesType;
	}

	public void setIndicesType(String indicesType) {
		this.indicesType = indicesType;
	}

	public String getIndices() {
		return indices;
	}

	public void setIndices(String indices) {
		this.indices = indices;
	}

	public double getInputPrice() {
		return inputPrice;
	}

	public void setInputPrice(double inputPrice) {
		this.inputPrice = inputPrice;
	}

}
