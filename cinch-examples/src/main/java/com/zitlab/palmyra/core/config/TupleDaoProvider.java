package com.zitlab.palmyra.core.config;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.schema.DefaultSchemaFactory;
import com.zitlab.palmyra.cinch.schema.SchemaFactory;
import com.zitlab.palmyra.cinch.tuple.dao.TupleDao;

public class TupleDaoProvider {
	public static TupleDao getDataAccessObject(DataSource dataSource) {
		SchemaFactory factory = new DefaultSchemaFactory();
		factory.load("default", dataSource, "orderingsystem");
		TupleDao dao = new TupleDao(factory, dataSource, "postgres");
		return dao;
	}

	
}
