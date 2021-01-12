package com.zitlab.palmyra.core.config;

import java.util.Date;
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
import com.zitlab.palmyra.cinch.tuple.dao.TupleDao;
import com.zitlab.palmyra.cinch.tuple.resulthandler.GenericTupleHandler;
import com.zitlab.palmyra.util.QueryTimer;


public class TestConfigProvider {
	public static void main(String args[]) {
		DataSource ds = DsProvider.getDataSource();
		
		QueryFactory zql2o = new QueryFactory(ds);
				
		SchemaFactory factory = new DefaultSchemaFactory();
				
		
		Date start, end;
		Tuple tuple = null;
		int loopCount = 50000;
		
		
		factory.load("default", ds, "pharma");
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
		TupleType tt = config.getTableCfg("DrugGrnEntry");
		
		TupleDao dao = new TupleDao(factory,ds, "raja");
		GenericTupleHandler hand = new GenericTupleHandler();
		
		for (int i = 0; i < 200; i++) {
			tuples = dao.list(tt, filter);
		}
		
		QueryTimer.reset();
		
		
		QueryTimer.start();
		start = new Date();
		for (int i = 0; i < loopCount; i++) {
			tuples = dao.list(tt, filter);
		}				
		end = new Date();
		System.out.println("loopCount " + loopCount + " Elapsed time " + (end.getTime() - start.getTime()));
		
		QueryTimer.reset();
		Tuple rs = tuples.get(0);
				
		System.out.println(tt.getPrimaryKeyColumnCount());
	}
	
}
