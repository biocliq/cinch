/*******************************************************************************
 * Copyright 2020 BioCliq Technologies
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package com.zitlab.palmyra.cinch.tuple.dao;

import java.io.IOException;
import java.util.List;

import org.simpleflatmapper.util.CheckedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.palmyra.cinch.dao.RecordDao;
import com.zitlab.palmyra.cinch.dao.query.QueryFactory;
import com.zitlab.palmyra.cinch.dao.rshandler.ListHandler;
import com.zitlab.palmyra.cinch.pojo.GenericItem;
import com.zitlab.palmyra.cinch.pojo.NativeQuery;
import com.zitlab.palmyra.cinch.pojo.Tuple;
import com.zitlab.palmyra.cinch.pojo.TupleResultSet;
import com.zitlab.palmyra.cinch.tuple.resulthandler.GenericResultHandler;
import com.zitlab.palmyra.cinch.tuple.resulthandler.GenericRowCallbackHandler;

/**
 * This class has to be tested.
 * @author srive
 *
 */
public class NativeQueryDao extends RecordDao {

	private static final Logger logger = LoggerFactory.getLogger(NativeQueryDao.class);

	public NativeQueryDao(QueryFactory factory) {
		super(factory);
	}

	public List<GenericItem> list(NativeQuery query) {
		if(logger.isTraceEnabled())
			logger.trace("Executing query {}, parameters:{}", query.getQuery(), query.getParams());
		return select(query, new GenericResultHandler());
	}
	
	public List<GenericItem> list(String query, Object... params) {
		if(logger.isTraceEnabled())
			logger.trace("Executing query {}, parameters:{}", query, params);
		return select(query, new GenericResultHandler(), params);
	}
	
	public void list(NativeQuery query, CheckedConsumer<Tuple> rp) throws IOException {
		if(logger.isTraceEnabled())
			logger.trace("Executing query {}, parameters:{}", query.getQuery(), query.getParams());
		select(query, getHandler(rp));
	}
	
	public TupleResultSet query(NativeQuery query) throws IOException {
		if(logger.isTraceEnabled())
			logger.trace("Executing query {}, parameters:{}", query.getQuery(), query.getParams());
		ListHandler<Tuple> rp = new ListHandler<Tuple>();
		select(query, getHandler(rp));
		TupleResultSet rs = new TupleResultSet();
		rs.setOffset(query.getOffset());
		if (null != query.getCountQuery()) {
			rs.setTotal(getCount(query));
		}
		rs.setResult(rp.list());
		return rs;
	}
	
	private GenericRowCallbackHandler getHandler(CheckedConsumer<Tuple> rp) {
		GenericRowCallbackHandler handler = new GenericRowCallbackHandler(rp);
		return handler;
	}
}