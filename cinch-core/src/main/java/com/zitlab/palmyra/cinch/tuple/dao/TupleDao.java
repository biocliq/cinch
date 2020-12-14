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
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zitlab.cinch.schema.Config;
import com.zitlab.palmyra.api2db.exception.GenericValidation;
import com.zitlab.palmyra.api2db.exception.MultipleTuplesExistsException;
import com.zitlab.palmyra.api2db.exception.Validation;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleAttribute;
import com.zitlab.palmyra.api2db.pdbc.pojo.TupleType;
import com.zitlab.palmyra.api2db.pojo.Action;
import com.zitlab.palmyra.api2db.pojo.Tuple;
import com.zitlab.palmyra.api2db.pojo.TupleResultSet;
import com.zitlab.palmyra.api2db.pojo.impl.TupleImpl;
import com.zitlab.palmyra.cinch.api2db.audit.ChangeLogger;
import com.zitlab.palmyra.cinch.dao.RecordDao;
import com.zitlab.palmyra.cinch.query.QueryFactory;
import com.zitlab.palmyra.cinch.rshandler.ResultSetHandler;
import com.zitlab.palmyra.cinch.security.AccessVerifier;
import com.zitlab.palmyra.cinch.tuple.queryhelper.DeleteQueryHelper;
import com.zitlab.palmyra.cinch.tuple.queryhelper.InsertQueryHelper;
import com.zitlab.palmyra.cinch.tuple.queryhelper.SelectQueryHelper;
import com.zitlab.palmyra.cinch.tuple.queryhelper.Table;
import com.zitlab.palmyra.cinch.tuple.queryhelper.UpdateQueryHelper;
import com.zitlab.palmyra.cinch.tuple.resulthandler.TupleResultSetHandler;
import com.zitlab.palmyra.cinch.tuple.resulthandler.TupleRowCallbackHandler;

public class TupleDao extends RecordDao {

	private static final Logger logger = LoggerFactory.getLogger(TupleDao.class);
	private Config config;
	private AccessVerifier accessVerifier;
	private String user;

	public TupleDao(Config config, DataSource ds, String user) {
		super(new QueryFactory(ds));
		this.config = config;
		this.user = user;
	}

	public TupleDao(Config config, QueryFactory queryFactory, String user) {
		super(queryFactory);
		this.config = config;
		this.user = user;
	}

	public TupleType getTableCfg(String type) {
		return config.getTableCfg(type);
	}

	public Tuple getById(String type, Object id, TupleFilter filter) {
		TupleType tupleType = config.getTableCfg(type);
		return getById(tupleType, id, filter);
	}

	public Tuple getById(TupleType type, Object id, TupleFilter filter) {
		SelectQueryHelper selectQueryHelper = new SelectQueryHelper(config);
		QueryParams params = selectQueryHelper.getSelectQueryById(type, id, filter);
		Tuple tuple = selectUnique(params, getHandler(type, params.getTableLookup()));
		return tuple;
	}

	public ResultSetHandler<Tuple> getHandler(TupleType type, Map<String, Table> tableMap) {
		TupleResultSetHandler handler = new TupleResultSetHandler();
		handler.setTableCfg(type, tableMap);
		return handler;
	}

	private TupleRowCallbackHandler getHandler(TupleType type, Map<String, Table> tableMap, ResultProcessor<Tuple> rp) {
		TupleRowCallbackHandler handler = new TupleRowCallbackHandler(rp, type);
		handler.setTableCfg(type, tableMap);
		return handler;
	}

	public TupleResultSet query(String ciType, TupleFilter filter) {		
		TupleType type = config.getTableCfg(ciType);
		return query(type, filter);
	}

	/**
	 * @param tupleType
	 * @param filter
	 * @param addlFilter
	 * @return
	 */
	public TupleResultSet query(TupleType tupleType, TupleFilter filter) {
		SelectQueryHelper selectQueryHelper = new SelectQueryHelper(config);
		QueryParams params = selectQueryHelper.getSearchQuery(tupleType, filter);
		params.setExpectedResultSetSize(filter.getLimit());
		List<Tuple> tuples = select(params, getHandler(tupleType, params.getTableLookup()));
		TupleResultSet rs = new TupleResultSet();
		rs.setResult(tuples);
		rs.setOffset(filter.getOffset());
		if (filter.isTotal()) {
			rs.setTotal(getCount(params));
		}
		return rs;
	}

	public List<Tuple> list(TupleType tupleType, TupleFilter filter) {
		SelectQueryHelper selectQueryHelper = new SelectQueryHelper(config);
		QueryParams params = selectQueryHelper.getSearchQuery(tupleType, filter);
		params.setExpectedResultSetSize(filter.getLimit());
		List<Tuple> tuples = select(params, getHandler(tupleType, params.getTableLookup()));
		return tuples;
	}

	public void list(TupleType tupleType, TupleFilter filter, ResultProcessor<Tuple> rp) throws IOException {
		SelectQueryHelper selectQueryHelper = new SelectQueryHelper(config);
		QueryParams params = selectQueryHelper.getSearchQuery(tupleType, filter);
		select(params, getHandler(tupleType, params.getTableLookup(), rp));
	}

	public void query(TupleType tupleType, TupleFilter filter, ResultProcessor<Tuple> rp) throws IOException {
		SelectQueryHelper selectQueryHelper = new SelectQueryHelper(config);
		QueryParams params = selectQueryHelper.getSearchQuery(tupleType, filter);
		params.setExpectedResultSetSize(filter.getLimit());
		TupleResultSet rs = new TupleResultSet();
		rs.setOffset(filter.getOffset());
		if (filter.isTotal()) {
			rs.setTotal(getCount(params));
		}
		rp.startMetadata(rs);
		select(params, getHandler(tupleType, params.getTableLookup(), rp));
		rp.endMetadata(rs);
	}

	/**
	 * @param item
	 * @return
	 */
	public List<Tuple> searchByKey(Tuple item, TupleFilter filter) {
		TupleType tupleType = item.getTupleType();
		SelectQueryHelper selectQueryHelper = new SelectQueryHelper(config);
		QueryParams params = selectQueryHelper.getSelectQueryByUQKey(item, filter);
		if (null != params) {
			if (logger.isTraceEnabled())
				logger.trace("searchByKey Query {}", params.getQuery());
			List<Tuple> tuples = select(params, getHandler(tupleType, params.getTableLookup()));
			return tuples;
		} else {
			if (logger.isTraceEnabled())
				logger.trace("Search By key - Query not generated for item {}", item.getType());
			return null;
		}
	}

	public Tuple getUniqueItem(Tuple item, TupleFilter filter) {
		Tuple result = null;
		List<Tuple> items = searchByKey(item, filter);
		if (null != items) {
			if (items.size() == 1) {
				result = items.get(0);
			} else if (items.size() > 1) {
				TupleType tupleType = item.getTupleType();
				// TODO add logic to provide list of unique keys/values in the error message;
				throw new MultipleTuplesExistsException(tupleType.getName(),
						items.size() + " records has been found for the given type " + tupleType.getName());
			} else if (0 == items.size()) {
				return null;
			}
		}
		// This statement will be reached if no unique keys are present in the item.
		// TODO Add a log statement ??
		return result;
	}

	public Tuple getUniqueItem(TupleType ttype, String key, Object value, TupleFilter filter) {
		TupleImpl criteria = new TupleImpl();
		criteria.setTupleType(ttype);
		criteria.setAttribute(key, value);
		return getUniqueItem(criteria, filter);
	}

	public int delete(Tuple item, ChangeLogger changeLogger) {
		QueryParams params = DeleteQueryHelper.getDeleteQueryByID(item, changeLogger, getUser(), config.getDialect());
		if (null != params) {
			return delete(params);
		} else {
			logger.debug("Delete by Id for {} with id {} - Query not generated ", item.getType(), item.getId());
			return 0;
		}
	}

	/**
	 * This method shall only be called from TupleDataProcessor
	 * 
	 * @param item
	 * @param changeLogger
	 */
	public void save(Tuple item, ChangeLogger changeLogger) {

		// retrieve cfgItemtype structure from the cache
		TupleType tupleType = item.getTupleType();

		if (null != tupleType) {
			Tuple dbItem = item.getDbTuple();

			if (null == dbItem) {
				Object id = item.getId();
				if (null != id)
					dbItem = getById(tupleType, id, null);
//				else
//					dbItem = getUniqueItem(item, null);
			}
			if (null != dbItem) {
				item.setActionCode(Action.UPDATE);
				item.removeAttribute("createdBy");
				item.removeAttribute("createdOn");
				item.removeAttribute("lastUpdOn");
				item.removeAttribute("lastUpdBy");
				update(item, dbItem, changeLogger);
			} else {
				Date upd = new Date();
				if (null != tupleType.getField("createdBy")) {
					item.setAttribute("createdBy", getUser());
					item.setAttribute("createdOn", upd);
					item.setAttribute("lastUpdOn", upd);
					item.setAttribute("lastUpdBy", getUser());
				}
				item.setActionCode(Action.CREATE);
				insertInternal(item);
				item.setDbTuple(item);
			}

		} else {
			throw new RuntimeException("CI Type " + item.getType() + " not found in configuration");
		}
		item.setDbExists(true);
	}

	private String getUser() {	
		return user;
	}

	private void update(Tuple item, Tuple dbItem, ChangeLogger auditLogger) {
		TupleType tupleType = item.getTupleType();
		UpdateQueryHelper updateQueryHelper = new UpdateQueryHelper(accessVerifier.getNonUpdatableFields(item),
				auditLogger);
		QueryParams params = updateQueryHelper.getUpdateQueryByID(tupleType, item, dbItem, getUser(), config.getDialect(),
				this);
		if (null != params) {
			update(params);
		}
	}

	private void insertInternal(Tuple item) {
		TupleType tupleType = item.getTupleType();
		if (tupleType.hasSeqField() && null == item.getAttribute(tupleType.getSeqFieldName())) {
			String assetTag = getNextCITag(item);
			item.setAttribute(tupleType.getSeqField().getAttribute(), assetTag);
		}

		InsertQueryHelper insertQueryHelper = new InsertQueryHelper(accessVerifier.getNonInsertableFields(item));

		QueryParams params = insertQueryHelper.getInsertStatement(tupleType, item, config.getDialect(), this);
		if (null != params) {
			if (tupleType.isAutoIncrementPkey()) {
				TupleAttribute pKey = tupleType.getAutoIncrementkey();
				Long id = insert(params, Long.class, pKey.getColumnName());
				if (null != id)
					item.setAttribute(pKey.getAttribute(), id);
			} else
				insert(params);
		}else {
			logger.warn("No column values provided for {}, Check the input attributes, Record insertion ignored", tupleType.getName());
		}
	}

	/**
	 * update the CI Tag number
	 * 
	 * @param item
	 * @param cfgItemType
	 */
	private String getNextCITag(Tuple item) {
		TupleType type = item.getTupleType();

		Integer argument = type.getId();
		Long nextValue = selectFirst("select `next_val`(?) as next_seq", Long.class, argument);

		if (null == nextValue) {
			throw new GenericValidation(Validation.MISSING_SEQUENCE, "No sequence assigned for CIType " + type.getName());
		}

		String key = new SimpleKeyGenerator().generateKey(type.getSeqFormat(), nextValue);
		return key;
	}

	public void setAccessVerifier(AccessVerifier accessVerifier) {
		this.accessVerifier = accessVerifier;
	}
}