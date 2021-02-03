package com.zitlab.palmyra.cinchquery.examples;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.pojo.QueryFilter;
import com.zitlab.palmyra.cinch.pojo.Tuple;
import com.zitlab.palmyra.cinch.tuple.dao.TupleDao;
import com.zitlab.palmyra.core.config.DsProvider;
import com.zitlab.palmyra.core.config.TupleDaoProvider;

public class QueryWithOffsetLimit {
	public static List<Tuple> queryData() throws SQLException {
		DataSource dataSource = DsProvider.getDataSource();		
		TupleDao tupleDao = TupleDaoProvider.getDataAccessObject(dataSource);		

		QueryFilter queryFilter = new QueryFilter();
		//queryFilter.addCondition(" order_id = 1 and quantity > 2 ");	
		queryFilter.setLimit(50);
		queryFilter.setOffset(100);
		
		List<Tuple> tuples = tupleDao.list("Orderitem", queryFilter);
		return tuples;

	}

	public static void main(String args[]) throws SQLException {
		List<Tuple> tuples = queryData();
		for (int i = 0; i < tuples.size(); i++)
			System.out.println(tuples.get(i).getAttribute("id"));
	}
}
