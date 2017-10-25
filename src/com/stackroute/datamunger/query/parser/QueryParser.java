package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryParser {
	private QueryParameter queryParameter = new QueryParameter();
	// Regex for white space.
	private static final String WHITE_SPACE = "\\s+";

	/*
	 * this method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		if (!isNullEmpty(queryString)) {
			// Converting to lowercase
			// queryString = queryString.toLowerCase();

			// extract the name of the file from the query. File name can be
			// found after the "from" clause.
			queryParameter.setFile(this.getFile(queryString));

			// extrsct fields
			List<String> fields = this.getFields(queryString);
			if (null != fields) {
				queryParameter.getFields().addAll(fields);
			}

			// extract the order by fields from the query string.
			List<String> orderByFields = this.getOrderByFields(queryString);
			if (null != orderByFields) {
				queryParameter.getOrderByFields().addAll(orderByFields);
			}

			// extract the group by fields from the query string.
			List<String> groupByFields = this.getGroupByFields(queryString);
			if (null != groupByFields) {
				queryParameter.getGroupByFields().addAll(groupByFields);
			}

			// Extracting Conditions.
			List<Restriction> restrictionList = this.getConditions(queryString);
			if (null != restrictionList) {

				queryParameter.setRestrictions(restrictionList);
			}

			// Extracting logical Operators
			List<String> operatorsList = this.getLogicalOperators(queryString);
			if (null != operatorsList) {
				queryParameter.setLogicalOperators(operatorsList);
			}

			// Extracting Aggregate functions.
			List<AggregateFunction> aggregateList = this.getAggregateFunctions(queryString);
			if (null != aggregateList) {
				queryParameter.getAggregateFunctions().addAll(aggregateList);
			}
		}
		return queryParameter;

	}

	/*
	 * extract the name of the file from the query. File name can be found after
	 * the "from" clause.
	 */
	public String getFile(String queryString) {
		// Method 1
		/*String[] strArray = queryString.trim().split(WHITE_SPACE);
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i].contains(".") && i > 0 && strArray[i - 1].equalsIgnoreCase("from")) {
				return strArray[i];
			}
		}*/

		// Method 2
		String[] fromSplit = queryString.trim().split(" from ");
		if (null != fromSplit && fromSplit.length > 1) {
			String[] tempStrArray = fromSplit[1].split(WHITE_SPACE);
			return tempStrArray[0];
		}
		return null;
	}

	/*
	 * extract the order by fields from the query string. Please note that we
	 * will need to extract the field(s) after "order by" clause in the query,
	 * if at all the order by clause exists. For eg: select
	 * city,winner,team1,team2 from data/ipl.csv order by city from the query
	 * mentioned above, we need to extract "city". Please note that we can have
	 * more than one order by fields.
	 */
	public List<String> getOrderByFields(String queryString) {
		String[] strArray = queryString.trim().split(WHITE_SPACE);
		boolean orderByFound = false;
		for (int i = 1; i < strArray.length; i++) {
			if (strArray[i].equalsIgnoreCase("Order") && strArray[i + 1].equalsIgnoreCase("by")) {
				orderByFound = true;
				i++;
				continue;
			}
			if (orderByFound) {
				return new ArrayList<String>(Arrays.asList(strArray[i].split(",")));
			}
		}
		return null;
	}

	/*
	 * extract the group by fields from the query string. Please note that we
	 * will need to extract the field(s) after "group by" clause in the query,
	 * if at all the group by clause exists. For eg: select
	 * city,max(win_by_runs) from data/ipl.csv group by city from the query
	 * mentioned above, we need to extract "city". Please note that we can have
	 * more than one group by fields.
	 */
	public List<String> getGroupByFields(String queryString) {
		String[] strArray = queryString.trim().split(WHITE_SPACE);
		boolean orderByFound = false;
		for (int i = 1; i < strArray.length; i++) {
			if (strArray[i].equalsIgnoreCase("Group") && strArray[i + 1].equalsIgnoreCase("by")) {
				orderByFound = true;
				i++;
				continue;
			}
			if (orderByFound) {
				return new ArrayList<String>(Arrays.asList(strArray[i].split(",")));
			}
		}
		return null;
	}

	/*
	 * extract the selected fields from the query string. Please note that we
	 * will need to extract the field(s) after "select" clause followed by a
	 * space from the query string. For eg: select city,win_by_runs from
	 * data/ipl.csv from the query mentioned above, we need to extract "city"
	 * and "win_by_runs". Please note that we might have a field containing name
	 * "from_date" or "from_hrs". Hence, consider this while parsing.
	 */
	public List<String> getFields(String queryString) {
		// Method1
		/*
		 * String[] strArray = queryString.trim().split(WHITE_SPACE); for (int i
		 * = 0; i < strArray.length; i++) { if
		 * (strArray[i].equalsIgnoreCase("SELECT")) { continue; } if
		 * (strArray[i].equalsIgnoreCase("FROM")) { break; } return new
		 * ArrayList<String>(Arrays.asList(strArray[i].split(","))); }
		 */

		// Method2
		List<String> fields = new ArrayList<String>();
		String[] selectSplit = queryString.trim().split("select ");
		if (null != selectSplit && selectSplit.length > 1) {
			String[] fromSplit = selectSplit[1].split(" from ");
			if (null != fromSplit) {
				String[] commaSplit = fromSplit[0].split(",");
				if (null != commaSplit) {
					for (int i = 0; i < commaSplit.length; i++) {
						fields.add(commaSplit[i].trim());
					}
				}
			}
		}
		return fields.isEmpty() ? null : fields;
	}

	/*
	 * extract the conditions from the query string(if exists). for each
	 * condition, we need to capture the following: 1. Name of field 2.
	 * condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name
	 * of field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND
	 * operators. Please consider this while parsing the conditions.
	 * 
	 */
	public List<Restriction> getConditions(String queryString) {

		String conditionQuery = this.getConditionsPartQuery(queryString);

		if (null == conditionQuery) {
			return null;
		}

		List<Restriction> restrictionList = new ArrayList<Restriction>();
		String[] strArray = conditionQuery.trim().split(WHITE_SPACE);
		for (int i = 0; i < strArray.length; i++) {
			String temp = "";
			Restriction restriction = null;
			if (strArray[i].contains(">") || strArray[i].contains("<") || strArray[i].contains("!")
					|| strArray[i].contains("=")) {
				restriction = new Restriction();
				temp += strArray[i] + " ";
				String conditionString = "";
				String valueString = "";
				String nameString = "";
				boolean conditionStringFound = false;
				for (int j = 0; j < strArray[i].length(); j++) {
					char ch = strArray[i].charAt(j);
					if (conditionStringFound && (ch == ' ' || (ch + "").matches("^[a-zA-Z]+$"))) {
						valueString += ch;
						continue;
					}
					if (!conditionStringFound && (ch == ' ' || (ch + "").matches("^[a-zA-Z]+$"))) {
						nameString += ch;
						continue;
					}
					conditionString += ch;
					conditionStringFound = true;
				}
				if (!conditionString.trim().isEmpty()) {
					restriction.setCondition(conditionString.trim());
				}
				if (!valueString.trim().isEmpty()) {
					restriction.setPropertyValue(valueString.trim());
				}
				if (!nameString.trim().isEmpty()) {
					restriction.setPropertyName(nameString.trim());
				}

				if (this.isNullEmpty(restriction.getPropertyValue()) && i + 1 < strArray.length
						&& checkForDirtyData(temp)) {
					temp += strArray[i + 1];
					restriction.setPropertyValue(strArray[i + 1]);
				}

				if (i - 1 >= 0 && this.isNullEmpty(restriction.getPropertyName())) {
					temp = strArray[i - 1] + " " + temp;
					restriction.setPropertyName(strArray[i - 1]);
				}
				restrictionList.add(restriction);
			}
		}
		return restrictionList.isEmpty() ? null : restrictionList;
	}

	/*
	 * extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * the query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
	public List<String> getLogicalOperators(String queryString) {
		List<String> conditions = new ArrayList<String>();
		String[] strArray = queryString.trim().split(WHITE_SPACE);
		boolean whereFound = false;
		for (int i = 1; i < strArray.length - 1; i++) {
			if (strArray[i].equalsIgnoreCase("WHERE")) {
				whereFound = true;
			}
			if (whereFound && ("and".equalsIgnoreCase(strArray[i]) || "or".equalsIgnoreCase(strArray[i])
					|| "not".equalsIgnoreCase(strArray[i]))) {
				conditions.add(strArray[i]);
			}
		}
		return conditions.isEmpty() ? null : conditions;
	}

	/*
	 * extract the aggregate functions from the query. The presence of the
	 * aggregate functions can determined if we have either "min" or "max" or
	 * "sum" or "count" or "avg" followed by opening braces"(" after "select"
	 * clause in the query string. in case it is present, then we will have to
	 * extract the same. For each aggregate functions, we need to know the
	 * following: 1. type of aggregate function(min/max/count/sum/avg) 2. field
	 * on which the aggregate function is being applied
	 * 
	 * Please note that more than one aggregate function can be present in a
	 * query
	 * 
	 * 
	 */
	public List<AggregateFunction> getAggregateFunctions(String queryString) {
		List<AggregateFunction> aggregateList = new ArrayList<AggregateFunction>();
		String[] strArray = queryString.trim().split("select ");
		if (null != strArray && strArray.length > 1) {
			String beforeFromQuery = strArray[1].split(" from ")[0];
			if (beforeFromQuery.contains("sum(") || beforeFromQuery.contains("count(")
					|| beforeFromQuery.contains("max(") || beforeFromQuery.contains("min(")
					|| beforeFromQuery.contains("avg(")) {

				String[] aggregateArr = beforeFromQuery.split(",");
				if (null != aggregateArr) {
					for (int j = 0; j < aggregateArr.length; j++) {
						AggregateFunction aggregateFunction = new AggregateFunction();
						String[] splittedStr = aggregateArr[j].split("\\(");
						if (null != splittedStr && splittedStr.length > 1) {
							aggregateFunction.setAggregateName(splittedStr[0]);
							aggregateFunction
									.setAggregateValue(splittedStr[1].substring(0, splittedStr[1].length() - 1));
							aggregateList.add(aggregateFunction);
						}
					}
				}
			}
		}

		return aggregateList.isEmpty() ? null : aggregateList;
	}

	/**
	 * Method to check whether String is null or empty.
	 * 
	 * @param string
	 * @return
	 */
	private boolean isNullEmpty(String string) {
		if (null == string || string.isEmpty()) {
			return true;
		}

		return false;
	}

	/**
	 * Method to get condition part query.
	 * 
	 * @param queryString
	 * @return
	 */
	public String getConditionsPartQuery(String queryString) {
		if (queryString.contains(" where ")) {
			int indexOfWhere = queryString.lastIndexOf(" where ");
			indexOfWhere += "where ".length();
			int indexOfOrderByGroupByKeyword = queryString.length();
			if (queryString.contains(" order by ")) {
				indexOfOrderByGroupByKeyword = queryString.indexOf("order by");
			} else if (queryString.contains(" group by ")) {
				indexOfOrderByGroupByKeyword = queryString.indexOf("group by");
			}
			return queryString.substring(indexOfWhere, indexOfOrderByGroupByKeyword);
		}
		return null;
	}

	/**
	 * Remove Dirty Data from incoming String.
	 * 
	 * @param temp
	 * @return
	 */
	private boolean checkForDirtyData(String temp) {
		boolean hasSpecialChar = false;
		for (int j = 0; j < temp.length(); j++) {
			char ch = temp.charAt(j);
			if (ch == '<' || ch == '>' || ch == '!' || ch == '=') {
				hasSpecialChar = true;
			}
			if ((ch + "").matches("^[a-zA-Z]+$") && hasSpecialChar) {
				return false;
			}
		}
		return true;
	}
}