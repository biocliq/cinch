package com.zitlab.palmyra.cinch.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;

public class ValueProcessor implements TokenProcessor{

	@Override
	public void process(JsonParser parser, ParserContext context) throws IOException {
		String value = parser.getText();
		String key = context.getFieldKey();
		TupleContext ctx = context.get();
		Tuple tuple = ctx.getTuple();
		TupleType tupleType = ctx.getTupleType();
		
		if(null != tupleType && null != value) {
			TupleAttribute attrib = tupleType.getField(key);
			if(null != attrib) {
				tuple.set(key, attrib.getConverter().convert(value));
				return;
			}
		}
		tuple.set(key, value);
	}
}
