package com.zitlab.palmyra.core.config;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.zitlab.cinch.schema.Config;
import com.zitlab.cinch.schema.ConfigFactory;
import com.zitlab.cinch.schema.DefaultConfigFactory;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.FieldList;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.pojo.impl.FieldListImpl;
import com.zitlab.palmyra.cinch.query.Query;
import com.zitlab.palmyra.cinch.query.QueryFactory;
import com.zitlab.palmyra.cinch.tuple.dao.TupleDao;
import com.zitlab.palmyra.cinch.tuple.dao.TupleFilter;
import com.zitlab.palmyra.cinch.tuple.resulthandler.GenericTupleHandler;
import com.zitlab.palmyra.util.QueryTimer;


public class TestConfigProvider {
	public static void main(String args[]) {
		DataSource ds = DsProvider.getDataSource();
		
		QueryFactory zql2o = new QueryFactory(ds);
				
		ConfigFactory factory = new DefaultConfigFactory();
				
		
		Date start, end;
		Tuple tuple = null;
		int loopCount = 2000;
		
		
		factory.load("default", ds, "pharma");
		List<Tuple> tuples = null;
		Config config = factory.getConfig();
		TupleFilter filter = new TupleFilter();
		filter.setLimit(-1);
		FieldList fl = new FieldListImpl();
		fl.addField("grn");
		fl.addField("batchNumber");
		fl.addField("expiryOn");
		fl.addParentField("drugBrandName.dosageSize");
		filter.setFields(fl);
		TupleType tt = config.getTableCfg("DrugGrnEntry");
		
		TupleDao dao = new TupleDao(config,ds, "raja");
		GenericTupleHandler hand = new GenericTupleHandler();
		
		for (int i = 0; i < 200; i++) {
			tuples = dao.list(tt, filter);
		}
		
		QueryTimer.reset();
		
		
//		start = new Date();
//		for (int i = 0; i < loopCount; i++) {			
//			grns = new GrnHandler();
//			 query.executeAndFetch(grns, Grn.class);			 
//		}
//		System.out.println(grns.list().get(0).getBillNumber());
//		end = new Date();
//		System.out.println(end.getTime() - start.getTime());
//		System.out.println("object binding " + QueryTimer.getTimer());
//		QueryTimer.reset();
		
		
		
		
		QueryTimer.start();
		start = new Date();
		for (int i = 0; i < loopCount; i++) {
			tuples = dao.list(tt, filter);
		}				
		end = new Date();
		System.out.println("Elapsed time " + (end.getTime() - start.getTime()));
		
		QueryTimer.reset();
		Tuple rs = tuples.get(0);
				
		System.out.println(tt.getPrimaryKeyColumnCount());
	}
	
}
