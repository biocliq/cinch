package com.zitlab.palmyra.cinch.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.zitlab.palmyra.cinch.pojo.Tuple;

public class NullProcessor implements TokenProcessor {

	@Override
	public void process(JsonParser parser, ParserContext context) throws IOException {
		String key = context.getFieldKey();
		TupleContext ctx = context.get();
		Tuple tuple = ctx.getTuple();
		tuple.set(key, null);
	}
}
