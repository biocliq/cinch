package com.zitlab.palmyra.core.config;


import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class DsProvider {
	public static DataSource getDataSource() {

		String url = "jdbc:postgresql://localhost:5432/postgres?zeroDateTimeBehavior=convertToNull";
		BasicDataSource ds = new BasicDataSource();
		ds.setUsername("postgres");
		ds.setPassword("biocliq");
		ds.setDriverClassName("org.postgresql.Driver");
		ds.setUrl(url);
		ds.setMaxIdle(5);
		ds.setMinIdle(5);
		ds.setMaxTotal(5);
		ds.setInitialSize(5);
		ds.setCacheState(true);	
		return ds;
	}
}
