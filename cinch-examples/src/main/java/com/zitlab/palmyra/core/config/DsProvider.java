package com.zitlab.palmyra.core.config;


import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class DsProvider {
	public static DataSource getDataSource() {
		
		String url = "jdbc:mariadb://127.0.0.1:3306/pharma?zeroDateTimeBehavior=convertToNull";
		BasicDataSource ds = new BasicDataSource();
		ds.setUsername("ksvraja");
		ds.setPassword("sriraja");
		ds.setUrl(url);
		ds.setMaxIdle(5);
		ds.setMinIdle(5);
		ds.setMaxTotal(5);
		ds.setInitialSize(5);
		ds.setCacheState(true);		
		return ds;
	}
}
