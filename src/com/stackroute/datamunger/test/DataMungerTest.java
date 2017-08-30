package com.stackroute.datamunger.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stackroute.datamunger.query.parser.AggregateFunction;
import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.QueryParser;
import com.stackroute.datamunger.query.parser.Restriction;

public class DataMungerTest {

	private static QueryParser queryParser;
	private static QueryParameter queryParameter;
	private String queryString;

	@Before
	public void setup() {
		// setup methods runs, before every test case runs
		// This method is used to initialize the required variables
		queryParser = new QueryParser();

	}

	@After
	public void teardown() {
		// teardown method runs, after every test case run
		// This method is to clear the initialized variables
		queryParser = null;

	}

	@Test
	public void testGetFileName() {
		queryString = "select * from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);
		assertEquals(
				"testGetFileName(): File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		display(queryString, queryParameter);
	}

	@Test
	public void testGetFileNameFailure() {
		queryString = "select * from ipl1.csv";
		queryParameter = queryParser.parseQuery(queryString);
		assertNotEquals(
				"testGetFileNameFailure(): File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		assertNotNull(
				"testGetFileNameFailure(): File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				queryParameter.getFile());
		display(queryString, queryParameter);
	}

	@Test
	public void testGetFields() {
		queryString = "select city, winner, team1,team2 from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);
		List<String> expectedFields = new ArrayList<>();
		expectedFields.add("city");
		expectedFields.add("winner");
		expectedFields.add("team1");
		expectedFields.add("team2");
		assertArrayEquals(
				"testGetFields() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				expectedFields.toArray(), queryParameter.getFields().toArray());
		display(queryString, queryParameter);
	}

	@Test
	public void testGetFieldsFailure() {
		queryString = "select city, winner, team1,team2 from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);
		assertNotNull(
				"testGetFieldsFailure() : Invalid Column / Field values. Please note that the query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				queryParameter.getFields());
		display(queryString, queryParameter);
	}

	@Test
	public void testGetFieldsAndRestrictions() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();
		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		Boolean status = false;
		if (restrictions.get(0).getPropertyName().contains("season")
				&& restrictions.get(0).getPropertyValue().contains("2014")
				&& restrictions.get(0).getCondition().contains(">")) {
			status = true;
		}

		assertEquals(
				"testGetFieldsAndRestrictions() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		assertEquals(
				"testGetFieldsAndRestrictions() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());
		assertEquals(
				"testGetFieldsAndRestrictions() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				true, status);

		display(queryString, queryParameter);
	}

	@Test
	public void testGetFieldsAndRestrictionsFailure() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();
		assertNotNull(
				"testGetFieldsAndRestrictions() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

		display(queryString, queryParameter);
	}

	@Test
	public void testGetFieldsAndMultipleRestrictions() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<String> logicalop = new ArrayList<String>();
		logicalop.add("and");

		Boolean status = false;
		int counter = 0;
		if (restrictions.get(0).getPropertyName().contains("season")
				&& restrictions.get(0).getPropertyValue().contains("2014")
				&& restrictions.get(0).getCondition().contains(">")) {
			counter++;
		}
		if (restrictions.get(1).getPropertyName().contains("city")
				&& restrictions.get(1).getPropertyValue().contains("Bangalore")
				&& restrictions.get(1).getCondition().contains("=")) {
			counter++;
		}
		if (counter > 1) {
			status = true;
		}

		assertEquals(
				"testGetFieldsAndMultipleRestrictions() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		assertEquals(
				"testGetFieldsAndMultipleRestrictions() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());
		assertEquals(
				"testGetFieldsAndMultipleRestrictions() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				true, status);
		assertEquals(
				"testGetFieldsAndMultipleRestrictions() : Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc.",
				logicalop, queryParameter.getLogicalOperators());

		display(queryString, queryParameter);
	}

	@Test
	public void testGetFieldsAndMultipleRestrictionsFailures() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();
		assertNotNull(
				"testGetFieldsAndMultipleRestrictionsFailures() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

		display(queryString, queryParameter);
	}

	@Test
	public void testGetFieldsAndMultipleRestrictions2() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 or city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<String> logicalop = new ArrayList<String>();
		logicalop.add("or");

		Boolean status = false;
		int counter = 0;
		List<Restriction> restrictions = queryParameter.getRestrictions();
		if (restrictions.get(0).getPropertyName().contains("season")
				&& restrictions.get(0).getPropertyValue().contains("2014")
				&& restrictions.get(0).getCondition().contains(">")) {
			counter++;
		}
		if (restrictions.get(1).getPropertyName().contains("city")
				&& restrictions.get(1).getPropertyValue().contains("Bangalore")
				&& restrictions.get(1).getCondition().contains("=")) {
			counter++;
		}
		if (counter > 1) {
			status = true;
		}

		assertEquals(
				"testGetFieldsAndMultipleRestrictions2() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		assertEquals(
				"testGetFieldsAndMultipleRestrictions2() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());
		assertEquals(
				"testGetFieldsAndMultipleRestrictions2() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				true, status);
		assertEquals(
				"testGetFieldsAndMultipleRestrictions2() :  Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				logicalop, queryParameter.getLogicalOperators());
	}

	@Test
	public void testGetFieldsAndMultipleRestrictions2Failure() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 or city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();
		assertNotNull(
				"testGetFieldsAndMultipleRestrictions2Failure() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

		display(queryString, queryParameter);
	}

	@Test
	public void testGetFieldsAndThreeRestrictions() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore' or city ='Delhi'";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> logicalOperators = new ArrayList<String>();
		logicalOperators.add("and");
		logicalOperators.add("or");

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		Boolean status = false;
		int counter = 0;
		List<Restriction> restrictions = queryParameter.getRestrictions();
		if (restrictions.get(0).getPropertyName().contains("season")
				&& restrictions.get(0).getPropertyValue().contains("2014")
				&& restrictions.get(0).getCondition().contains(">")) {
			counter++;
		}
		if (restrictions.get(1).getPropertyName().contains("city")
				&& restrictions.get(1).getPropertyValue().contains("Bangalore")
				&& restrictions.get(1).getCondition().contains("=")) {
			counter++;
		} 
		if (restrictions.get(2).getPropertyName().contains("city")
				&& restrictions.get(2).getPropertyValue().contains("Delhi")
				&& restrictions.get(2).getCondition().contains("=")) {
			counter++;
		}
		if (counter > 2) {
			status = true;
		}

		assertEquals(
				"testGetFieldsAndThreeRestrictions() :  File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		assertEquals(
				"testGetFieldsAndThreeRestrictions() :  Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());
		
		assertEquals(
				"testGetFieldsAndThreeRestrictions() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				true, status);
		assertEquals(
				"testGetFieldsAndThreeRestrictions() :  Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				logicalOperators, queryParameter.getLogicalOperators());

	}

	@Test
	public void testGetFieldsAndThreeRestrictionsFailure() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore' or city ='Delhi'";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();
		assertNotNull(
				"testGetFieldsAndThreeRestrictionsFailure() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

		display(queryString, queryParameter);
	}

	@Test
	public void testGetAggregateFunctions() {
		queryString = "select count(city),avg(win_by_runs),min(season),max(win_by_wickets) from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);
		List<AggregateFunction> aggregateFunctions = queryParameter.getAggregateFunctions();

		int counter = 0;
		boolean status = false;
		for (AggregateFunction agr : queryParameter.getAggregateFunctions()) {
			if (agr.getField().contains("city") && agr.getFunction().contains("count")) {
				counter++;
			} else if (agr.getField().contains("win_by_runs") && agr.getFunction().contains("avg")) {
				counter++;
			} else if (agr.getField().contains("season") && agr.getFunction().contains("min")) {
				counter++;
			} else if (agr.getField().contains("win_by_wickets") && agr.getFunction().contains("max")) {
				counter++;
			}
		}
		if (counter > 3) {
			status = true;
		}

		assertEquals(
				"testGetAggregateFunctions() : Hint: extract the aggregate functions from the query. The presence of the aggregate functions can determined if we have either 'min' or 'max' or 'sum' or 'count' or 'avg' followed by opening braces'(' after 'select' clause in the query. string. in case it is present, then we will have to extract the same. For each aggregate functions, we need to know the following: 1. type of aggregate function(min/max/count/sum/avg), 2. field on which the aggregate function is being applied. Please note that more than one aggregate function can be present in a query",
				true, status);
		assertEquals(
				"testGetAggregateFunctions() :  File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		display(queryString, queryParameter);
	}

	
	@Test
	public void testGetAggregateFunctionsFailure() {
		queryString = "select count(city),avg(win_by_runs),min(season),max(win_by_wickets) from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);
		List<AggregateFunction> aggregateFunctions = queryParameter.getAggregateFunctions();
		assertNotNull(
				"testGetAggregateFunctions() : Hint: extract the aggregate functions from the query. The presence of the aggregate functions can determined if we have either 'min' or 'max' or 'sum' or 'count' or 'avg' followed by opening braces'(' after 'select' clause in the query. string. in case it is present, then we will have to extract the same. For each aggregate functions, we need to know the following: 1. type of aggregate function(min/max/count/sum/avg), 2. field on which the aggregate function is being applied. Please note that more than one aggregate function can be present in a query",
				aggregateFunctions);
		display(queryString, queryParameter);
	}

	@Test
	public void testGetGroupByClause() {
		queryString = "select city,winner,player_match from ipl.csv group by city";
		queryParameter = queryParser.parseQuery(queryString);
		List<String> fields = new ArrayList<String>();
		fields.add("city");

		assertEquals(
				"testGetGroupByClause() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				fields, queryParameter.getGroupByFields());

		fields.add("winner");
		fields.add("player_match");

		assertEquals(
				"testGetGroupByClause() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());
		
		assertEquals(
				"testGetGroupByClause() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				null, queryParameter.getRestrictions());
		assertEquals(
				"testGetGroupByClause() : Logical Operators should be null. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				null, queryParameter.getLogicalOperators());

		display(queryString, queryParameter);

	}

	@Test
	public void testGetGroupByClauseFailure() {
		queryString = "select city,avg(win_by_runs) from ipl.csv group by city";
		queryParameter = queryParser.parseQuery(queryString);
		assertNotNull(
				"testGetGroupByClause() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				queryParameter.getGroupByFields());
		display(queryString, queryParameter);
	}

	@Test
	public void testGetGroupByOrderByClause() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city='Bangalore' group by winner order by city";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<String> logicalOperators = new ArrayList<String>();
		logicalOperators.add("and");

		List<String> orderByFields = new ArrayList<String>();
		orderByFields.add("city");

		List<String> groupByFields = new ArrayList<String>();
		groupByFields.add("winner");

		Boolean status = false;
		int counter = 0;
		if (restrictions.get(0).getPropertyName().contains("season")
				&& restrictions.get(0).getPropertyValue().contains("2014")
				&& restrictions.get(0).getCondition().contains(">")) {
			counter++;
		}
		if (restrictions.get(1).getPropertyName().contains("city")
				&& restrictions.get(1).getPropertyValue().contains("Bangalore")
				&& restrictions.get(1).getCondition().contains("=")) {
			counter++;
		}
		if (counter > 1) {
			status = true;
		}

		assertEquals(
				"testGetGroupByOrderByClause() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		assertEquals(
				"testGetGroupByOrderByClause() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());
		assertEquals(
				"testGetGroupByOrderByClause() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				true, status);
		assertEquals(
				"testGetGroupByOrderByClause() : Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc.",
				logicalOperators, queryParameter.getLogicalOperators());

		assertEquals(
				"testGetGroupByOrderByClause() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				groupByFields, queryParameter.getGroupByFields());

		assertEquals(
				"testGetOrderByClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields, queryParameter.getOrderByFields());

		display(queryString, queryParameter);

	}

	@Test
	public void testGetGroupByOrderByClauseFailure() {
		queryString = "select city,winner,team1,team2 from ipl.csv where season > 2016 and city='Bangalore' group by winner order by city";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();
		assertNotNull(
				"testGetGroupByOrderByClause() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				queryParameter.getGroupByFields());
		assertNotNull(
				"testGetGroupByOrderByClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists.",
				queryParameter.getOrderByFields());
		assertNotNull(
				"testGetGroupByOrderByClause() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

		display(queryString, queryParameter);
	}

	@Test
	public void testGetOrderByAndWhereConditionClause() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore' order by city";
		queryParameter = queryParser.parseQuery(queryString);

		List<Restriction> restrictions = queryParameter.getRestrictions();

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<String> logicalOperators = new ArrayList<String>();
		logicalOperators.add("and");

		List<String> orderByFields = new ArrayList<String>();
		orderByFields.add("city");

		Boolean status = false;
		int counter = 0;
		if (restrictions.get(0).getPropertyName().contains("season")
				&& restrictions.get(0).getPropertyValue().contains("2014")
				&& restrictions.get(0).getCondition().contains(">")) {
			counter++;
		}
		if (restrictions.get(1).getPropertyName().contains("city")
				&& restrictions.get(1).getPropertyValue().contains("Bangalore")
				&& restrictions.get(1).getCondition().contains("=")) {
			counter++;
		}
		if (counter > 1) {
			status = true;
		}

		assertEquals(
				"testGetOrderByAndWhereConditionClause() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		assertEquals(
				"testGetOrderByAndWhereConditionClause() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());
		
		assertEquals(
				"testGetOrderByAndWhereConditionClause() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				true, status);
		assertEquals(
				"testGetOrderByAndWhereConditionClause() : Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc.",
				logicalOperators, queryParameter.getLogicalOperators());

		assertEquals(
				"testGetOrderByAndWhereConditionClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields, queryParameter.getOrderByFields());

		display(queryString, queryParameter);

	}

	@Test
	public void testGetOrderByAndWhereConditionClauseFailure() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore' order by city";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();
		assertNotNull(
				"testGetOrderByAndWhereConditionClauseFailure() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);
		assertNotNull(
				"testGetOrderByAndWhereConditionClauseFailure() :Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists.",
				queryParameter.getOrderByFields());
		display(queryString, queryParameter);
	}

	@Test
	public void testGetOrderByClause() {
		queryString = "select city,winner,player_match from ipl.csv order by city";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> orderByFields = new ArrayList<String>();
		orderByFields.add("city");

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		assertEquals(
				"testGetOrderByClause() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFile());
		assertEquals(
				"testGetOrderByClause() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());
		
		assertEquals(
				"testGetOrderByClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields, queryParameter.getOrderByFields());

		display(queryString, queryParameter);
	}

	@Test
	public void testGetOrderByClauseFailure() {
		queryString = "select city,winner,team1,team2,player_match from ipl.csv order by city";
		queryParameter = queryParser.parseQuery(queryString);
		List<String> orderByFields = queryParameter.getOrderByFields();
		assertNotNull(
				"testGetOrderByClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields);
		display(queryString, queryParameter);
	}

	private void display(String queryString, QueryParameter queryParameter) {
		System.out.println("\nQuery : " + queryString);
		System.out.println("--------------------------------------------------");
		System.out.println("File:" + queryParameter.getFile());
		List<String> fields = queryParameter.getFields();
		System.out.println("Selected field(s):");
		if (fields == null || fields.isEmpty()) {
			System.out.println("*");
		} else {
			for (String field : fields) {
				System.out.println("\t" + field);
			}
		}

		List<Restriction> restrictions = queryParameter.getRestrictions();

		if (restrictions != null && !restrictions.isEmpty()) {
			System.out.println("Where Conditions : ");
			int conditionCount = 1;
			for (Restriction restriction : restrictions) {
				System.out.println("\tCondition : " + conditionCount++);
				System.out.println("\t\tName : " + restriction.getPropertyName());
				System.out.println("\t\tCondition : " + restriction.getCondition());
				System.out.println("\t\tValue : " + restriction.getPropertyValue());
			}
		}
		List<AggregateFunction> aggregateFunctions = queryParameter.getAggregateFunctions();
		if (aggregateFunctions != null && !aggregateFunctions.isEmpty()) {

			System.out.println("Aggregate Functions : ");
			int funtionCount = 1;
			for (AggregateFunction aggregateFunction : aggregateFunctions) {
				System.out.println("\t Aggregate Function : " + funtionCount++);
				System.out.println("\t\t function : " + aggregateFunction.getFunction());
				System.out.println("\t\t  field : " + aggregateFunction.getField());
			}

		}

		List<String> orderByFields = queryParameter.getOrderByFields();
		if (orderByFields != null && !orderByFields.isEmpty()) {

			System.out.println(" Order by fields : ");
			for (String orderByField : orderByFields) {
				System.out.println("\t " + orderByField);

			}

		}

		List<String> groupByFields = queryParameter.getGroupByFields();
		if (groupByFields != null && !groupByFields.isEmpty()) {

			System.out.println(" Group by fields : ");
			for (String groupByField : groupByFields) {
				System.out.println("\t " + groupByField);

			}

		}
	}
}