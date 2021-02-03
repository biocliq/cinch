package com.zitlab.palmyra.cinchquery.examples;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.pojo.QueryFilter;
import com.zitlab.palmyra.cinch.pojo.Tuple;
import com.zitlab.palmyra.cinch.tuple.dao.TupleDao;
import com.zitlab.palmyra.core.config.DsProvider;
import com.zitlab.palmyra.core.config.TupleDaoProvider;

public class QuerySingleRecord {
	public static Tuple queryData() throws SQLException {
		DataSource dataSource = DsProvider.getDataSource();		
		TupleDao tupleDao = TupleDaoProvider.getDataAccessObject(dataSource);	
		QueryFilter filter=new QueryFilter();
		filter.setFields("name","phoneno");
		Tuple tuple = tupleDao.getById("Customer", 1,filter);				
		return tuple;

	}

	public static void main(String args[]) throws SQLException {
		Tuple tuple = queryData();
		System.out.println(tuple.getAttribute("name"));
	
	}
}
