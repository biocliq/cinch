package com.zitlab.palmyra.cinch.rshandler;

import com.zitlab.palmyra.api2db.pojo.impl.TupleImpl;

public interface TupleFactory {
	public static final TupleFactory NOOP = new NoopFactory();
	
	public TupleImpl instance();
	
	public TupleImpl instance (String type);

	public TupleImpl instance (String type, int attCount);
	
	public TupleImpl instance (String type, Object id);
	
	public TupleImpl instance (int attCount);
	
	public void release(TupleImpl tuple);
	
	class NoopFactory implements TupleFactory{

		@Override
		public TupleImpl instance() {
			return new TupleImpl();
		}

		@Override
		public TupleImpl instance(String type) {
			return new TupleImpl(type);
		}

		@Override
		public TupleImpl instance(String type, int attCount) {
			return new TupleImpl(type, attCount);
		}

		@Override
		public TupleImpl instance(String type, Object id) {
			TupleImpl tuple = new TupleImpl(type);
			tuple.setId(id);
			return tuple;
		}

		@Override
		public TupleImpl instance(int attCount) {
			return new TupleImpl(attCount);
		}

		@Override
		public void release(TupleImpl tuple) {
					
		}
	}
}
