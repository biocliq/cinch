package com.zitlab.palmyra.cinch.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

public class NoopProcessor implements TokenProcessor{
	@Override
	public void process(JsonParser parser, ParserContext context) throws IOException {
			
	}
}
