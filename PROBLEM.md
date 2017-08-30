### Problem Statement

Read the query string and construct `QueryParameter` object. The `QueryParameter` consists of all the parameters what we extracted in the previous assignment. 

a. `Restriction Class`

	i.  Properties : propertyName, propertyValue, propertyPosition, condition etc.,

	ii. Constructor:  create constructor with all the properties

	iii. Generate getter/setter methods for all the methods

b.  `LogicalOperations` :  List of Strings

c.  `AggregateFunctions` Class:

	i.   Properties : field, function, result

	ii.  Constructor:  create cons(Build QueryParameter)	

    iii. Generate getter/setter methods for all the methods

d. `QueryParameter` Class
	
	i. Properties : queryString, queryString, List< Restriction>, logicalOperators[], groupBy, orderBy, List< AggregateFunctions >

e. `QueryParser` Class

	i. Properties
		1. QueryParameter
	ii. Methods
		1. Public QueryParameter parseQuery(String  queryString)

	Note : This method takes query string as input and build QueryParameter object
