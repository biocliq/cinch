package com.zitlab.palmyra.cinch.dao;

import javax.sql.DataSource;

public interface DataSourceFactory {
	public DataSource getDataSource(String context);
}
