package com.stackroute.datamunger.query.parser;


/* This class is used for storing name of field, aggregate function for 
 * each aggregate function
 * */
public class AggregateFunction {

	private String aggregateName;
	private String aggregateValue;

	/**
	 * @param aggregateName
	 *            the aggregateName to set
	 */
	public void setAggregateName(String aggregateName) {
		this.aggregateName = aggregateName;
	}

	/**
	 * @param aggregateValue
	 *            the aggregateValue to set
	 */
	public void setAggregateValue(String aggregateValue) {
		this.aggregateValue = aggregateValue;
	}

	public String getField() {
		return aggregateValue;
	}

	public String getFunction() {
		return aggregateName;
	}
}