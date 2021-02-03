package com.zitlab.palmyra.cinchquery.examples;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.pojo.QueryFilter;
import com.zitlab.palmyra.cinch.pojo.Tuple;
import com.zitlab.palmyra.cinch.tuple.dao.TupleDao;
import com.zitlab.palmyra.core.config.DsProvider;
import com.zitlab.palmyra.core.config.TupleDaoProvider;

public class QueryAddlCriteria {
	public static List<Tuple> queryData() throws SQLException {
		DataSource dataSource = DsProvider.getDataSource();		
		TupleDao tupleDao = TupleDaoProvider.getDataAccessObject(dataSource);		
		QueryFilter queryFilter = new QueryFilter();
		queryFilter.addOrderAsc("name");		
		List<Tuple> tuples = tupleDao.list("Customer", queryFilter);
		return tuples;

	}

	public static void main(String args[]) throws SQLException {
		List<Tuple> tuples = queryData();
		for (int i = 0; i < tuples.size(); i++)
			System.out.println(tuples.get(i).getAttribute("name"));
	
	}
}
