package com.zitlab.palmyra.cinch.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

public class EndArrayProcessor implements TokenProcessor {

	@Override
	public void process(JsonParser parser, ParserContext context) throws IOException {
		TupleContext ctx = context.pop();
		
		if (ctx.isObjectContext()) {			
			context.setFieldKey(ctx.getKey());
		}else {			

		}
	}
}
