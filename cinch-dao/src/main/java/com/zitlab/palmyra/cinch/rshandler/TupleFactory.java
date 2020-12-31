package com.zitlab.palmyra.cinch.rshandler;

import com.zitlab.palmyra.api2db.pojo.Tuple;

public interface TupleFactory {
	public static final TupleFactory NOOP = new NoopFactory();
	
	public Tuple instance();
	
	public Tuple instance (String type);

	public Tuple instance (String type, int attCount);
	
	public Tuple instance (String type, Object id);
	
	public Tuple instance (int attCount);
	
	public void release(Tuple tuple);
	
	class NoopFactory implements TupleFactory{

		@Override
		public Tuple instance() {
			return new Tuple();
		}

		@Override
		public Tuple instance(String type) {
			return new Tuple(type);
		}

		@Override
		public Tuple instance(String type, int attCount) {
			return new Tuple(type, attCount);
		}

		@Override
		public Tuple instance(String type, Object id) {
			Tuple tuple = new Tuple(type);
			tuple.setId(id);
			return tuple;
		}

		@Override
		public Tuple instance(int attCount) {
			return new Tuple(attCount);
		}

		@Override
		public void release(Tuple tuple) {
					
		}
	}
}
