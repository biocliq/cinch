package com.zitlab.palmyra.cinch.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.pojo.impl.TupleImpl;
import com.zitlab.palmyra.cinch.rshandler.TupleFactory;

public class TupleDeserializer {
	
	private TupleFactory factory;
	
	public TupleDeserializer(TupleFactory factory) {
		this.factory = factory;
	}
	
	public TupleImpl readTuple(InputStream inputStream, TupleType type) throws Exception {
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser parser = jsonFactory.createParser(inputStream);
		return parseTuple(parser, type);
	}

	public TupleImpl parseTuple(JsonParser parser, TupleType tupleType) throws IOException {
		JsonToken token;
		TupleImpl tuple = factory.instance();
		parser.nextToken();
		ParserContext context = new ParserContext(null, tupleType, tuple);
		context.setFactory(factory);

		while ((token = parser.nextToken()) != null) {
			TokenProcessor processor = ProcessorFactory.get(token.id());
			processor.process(parser, context);
		}
		return tuple;
	}

	public List<Tuple> readTuples(InputStream inputStream, TupleType type) throws Exception {

		JsonFactory jsonFactory = new JsonFactory();
		JsonParser parser = jsonFactory.createParser(inputStream);
		return parseTuples(parser, type);
	}

	public List<Tuple> parseTuples(JsonParser parser, TupleType tupleType) throws IOException {
		JsonToken token;
		TupleImpl tuple = factory.instance();
		parser.nextToken();
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		ParserContext context = new ParserContext(null, tupleType, tuple, tuples);
		context.setFactory(factory);
		
		while ((token = parser.nextToken()) != null) {
			TokenProcessor processor = ProcessorFactory.get(token.id());
			processor.process(parser, context);
		}
		return tuples;
	}
}
