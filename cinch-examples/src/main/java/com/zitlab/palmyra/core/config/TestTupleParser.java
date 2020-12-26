package com.zitlab.palmyra.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.zitlab.cinch.schema.Config;
import com.zitlab.cinch.schema.ConfigFactory;
import com.zitlab.cinch.schema.DefaultConfigFactory;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.cinch.jackson.TupleDeserializer;
import com.zitlab.palmyra.cinch.rshandler.TupleFactory;

public class TestTupleParser {

	public static void main(String[] args) throws Exception {

		DataSource ds = DsProvider.getDataSource();

		ConfigFactory factory = new DefaultConfigFactory();
		
		TupleDeserializer deserializer = new TupleDeserializer(TupleFactory.NOOP);

		factory.load("default", ds, "pharma");

		Config config = factory.getConfig();

		File file = new File("D:/temp/input.json");
		
		for (int i = 0; i < 100; i++) {
			FileInputStream fis = new FileInputStream(file);

			List<Tuple> tuples = deserializer.readTuples(fis, config.getTableCfg("DrugGrnEntry"));
			fis.close();
		}
		
		Date start = new Date();
		for (int i = 0; i < 200; i++) {
			FileInputStream fis = new FileInputStream(file);

			List<Tuple> tuples = deserializer.readTuples(fis, config.getTableCfg("DrugGrnEntry"));
			fis.close();
		}
		Date end = new Date();
		
		System.out.println("total time " + (end.getTime() - start.getTime()));
	}

}
