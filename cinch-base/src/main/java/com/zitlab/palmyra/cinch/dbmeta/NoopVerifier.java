package com.zitlab.palmyra.cinch.dbmeta;

import java.util.function.BiConsumer;

class NoopVerifier implements BiConsumer<String, Object> {
	private static final NoopVerifier instance = new NoopVerifier();
	
	@Override
	public void accept(String key, Object val) {

	}
	
	public static NoopVerifier instance() {
		return instance;
	}
}