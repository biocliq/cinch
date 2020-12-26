package com.zitlab.palmyra.core.config;

import java.util.List;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.CinchORM;

public class TestWhereClause {
	public static void main(String args[]) {
		DataSource ds = DsProvider.getDataSource();
		CinchORM orm = new CinchORM(ds);
		
		orm.loadSchemas("palmyra", "pharma");
		
		List<Grn> list = orm.query(Grn.class)
				.addCriteria("billNumber", "SA*")
				.list();
		
		System.out.println(list.size());
	}
}
