package com.zitlab.palmyra.cinchquery.examples;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.pojo.QueryFilter;
import com.zitlab.palmyra.cinch.pojo.Tuple;
import com.zitlab.palmyra.cinch.tuple.condition.ConditionBuilder;
import com.zitlab.palmyra.cinch.tuple.dao.TupleDao;
import com.zitlab.palmyra.core.config.DsProvider;
import com.zitlab.palmyra.core.config.TupleDaoProvider;

public class QueryCustomCondition {
	public static List<Tuple> queryData() throws SQLException {
		DataSource dataSource = DsProvider.getDataSource();		
		TupleDao tupleDao = TupleDaoProvider.getDataAccessObject(dataSource);	
		QueryFilter filter=new QueryFilter();
		filter.sqlExpression("name = 'John' and age > '18'");
		List<Tuple> tuple = tupleDao.list("Customer",filter);				
		return tuple;

	}

	public static void main(String args[]) throws SQLException {
		List<Tuple> tuple = queryData();
		if(tuple.size()>0)
			System.out.println(tuple.get(0).getAttribute("name"));
		else
			System.out.println("Record is empty");
	
	}
}
