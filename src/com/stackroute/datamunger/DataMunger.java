package com.stackroute.datamunger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.QueryParser;

public class DataMunger {
	public static void main(String[] args) throws Exception {
		
		//read the query from the user
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String queryString = br.readLine();
		
		//create an object of QueryParser class
		QueryParser queryParser = new QueryParser();
		
		/*
		 * call parseQuery() method of the class by passing the query string which will
		 * return object of QueryParameter
		 */
		QueryParameter queryParameter = queryParser.parseQuery(queryString);
		}

	

}
