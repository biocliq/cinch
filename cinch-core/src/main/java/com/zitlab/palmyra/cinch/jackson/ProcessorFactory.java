package com.zitlab.palmyra.cinch.jackson;

public class ProcessorFactory {

	private static final ValueProcessor valueProcessor = new ValueProcessor();
	private static final NoopProcessor noopProcessor = new NoopProcessor();
	
	private static final TokenProcessor[] processors = { noopProcessor, // 0 - No token
			new StartObjectProcessor(), // 1 - Start Object
			new EndObjectProcessor(), // 2 - End Object
			new StartArrayProcessor(), // 3 - Start Array
			new EndArrayProcessor(), // 4 - End Array
			new FieldProcessor(), // 5 - field name
			valueProcessor, // 6 - String
			valueProcessor, // 7 - Number Int
			valueProcessor, // 8 - Number Float
			valueProcessor, // 9 - Id True
			valueProcessor, // 10 - Id False
			valueProcessor, // 11 - Null value
			null // 12 - Embedded Object
	};

	public static TokenProcessor get(int i) {
		return processors[i];
	}
}
