package com.zitlab.palmyra.sqlbuilder.query;

public class Counter {
	private volatile int value;
	
	public Counter(int value) {
		this.value = value;
	}

	public int increment() {
		return ++value;
	}
	
	public int decrement() {
		return --value;
	}
	
	public void set(int value) {
		this.value = value;
	}
	
	public int get() {
		return value;
	}
}
