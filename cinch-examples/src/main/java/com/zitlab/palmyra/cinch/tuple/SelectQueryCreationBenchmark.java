package com.zitlab.palmyra.cinch.tuple;

import java.util.List;

import javax.sql.DataSource;

import com.zitlab.palmyra.cinch.dao.query.QueryFactory;
import com.zitlab.palmyra.cinch.dbmeta.TupleType;
import com.zitlab.palmyra.cinch.pojo.FieldList;
import com.zitlab.palmyra.cinch.pojo.Tuple;
import com.zitlab.palmyra.cinch.pojo.TupleFilter;
import com.zitlab.palmyra.cinch.schema.DefaultSchemaFactory;
import com.zitlab.palmyra.cinch.schema.Schema;
import com.zitlab.palmyra.cinch.schema.SchemaFactory;
import com.zitlab.palmyra.cinch.schema.config.TTUniqueKey;
import com.zitlab.palmyra.cinch.tuple.queryhelper.SelectQueryHelper;
import com.zitlab.palmyra.core.config.DsProvider;

public class SelectQueryCreationBenchmark {

	SchemaFactory factory = new DefaultSchemaFactory();
	TupleType tupleType;
	private int count = 3;
	
	public void createQuery() {
		
		Tuple tuple = new Tuple();
		tuple.set("statecode", "31");
		tuple.set("slnohhd", "02");
		tuple.set("towncode", "003");
		tuple.set("blockno", "0004");
		tuple.set("subebno", "0040");
		tuple.set("tehsilcode", "02");
		tuple.set("wardid", "003");
		tuple.set("districtcode", "02");
	
		tuple.setTupleType(tupleType);
		TupleFilter filter = new TupleFilter();
		filter.setCriteria(tuple);
		
		for (int i = 0; i < 100000; i++) {
			SelectQueryHelper helper = new SelectQueryHelper(factory);
			helper.getSelectQueryByUQKey(tuple, filter);
//			helper.getSearchQuery(tupleType, filter);
		}
		
		long time = System.nanoTime();
		for (int i = 0; i < count; i++) {
			SelectQueryHelper helper = new SelectQueryHelper(factory);
//			helper.getSearchQuery(tupleType, new TupleFilter());
			helper.getSelectQueryByUQKey(tuple, filter);
		}
		
		System.out.println((System.nanoTime() - time) / 1000);
	}

	public void init() {
		DataSource ds = DsProvider.getDataSource();

		QueryFactory zql2o = new QueryFactory(ds);

		Tuple tuple = null;
		int loopCount = 50000;

		factory.load("default", ds, "palmyra", "cmt_lookup", "app_common", "npr_2021");
		List<Tuple> tuples = null;
		Schema config = factory.getConfig();
		TupleFilter filter = new TupleFilter();
		filter.setLimit(-1);
		FieldList fl = new FieldList();
		fl.addField("grn");
		fl.addField("batchNumber");
		fl.addField("expiryOn");
		fl.addParentField("drugBrandName.dosageSize");
		filter.setFields(fl);
		tupleType = config.getTableCfg("NprHousehold2021");

	}

	public static void main(String args[]) {
		SelectQueryCreationBenchmark mark = new SelectQueryCreationBenchmark();
		mark.init();
		mark.createQuery();
	}
}
