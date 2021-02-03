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

public class QuerytestNull {
	public static List<Tuple> queryData() throws SQLException {
		DataSource dataSource = DsProvider.getDataSource();		
		TupleDao tupleDao = TupleDaoProvider.getDataAccessObject(dataSource);	
		QueryFilter queryFilter=new QueryFilter();
		queryFilter.addCondition(new ConditionBuilder().and().isNull("phoneNo").isNotNull("name").build());
		List<Tuple> tuple = tupleDao.list("Customer",queryFilter);				
		return tuple;

	}

	public static void main(String args[]) throws SQLException {
		List<Tuple> tuple = queryData();
		System.out.println(tuple.get(0).getAttribute("name"));
	
	}
}
