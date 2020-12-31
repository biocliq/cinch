package com.zitlab.palmyra.cinch.tuple.queryhelper;

import java.util.function.BiConsumer;

class NoopVerifier implements BiConsumer<String, String> {
	private static final NoopVerifier instance = new NoopVerifier();
	
	@Override
	public void accept(String key, String val) {

	}
	
	public static NoopVerifier instance() {
		return instance;
	}
}