package com.stackroute.datamunger.query.parser;


/*
 * This class is used for storing name of field, condition and value for 
 * each conditions
 * */
public class Restriction {

	private String propertyName;
	private String propertyValue;
	private String condition;

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName
	 *            the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @param propertyValue
	 *            the propertyValue to set
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/*
	 * public String getPropertyName() { return null; } public String
	 * getPropertyValue() { return null; } public String getCondition() { return
	 * null; }
	 */
}
