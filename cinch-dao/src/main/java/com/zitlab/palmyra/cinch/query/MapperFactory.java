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
package com.zitlab.palmyra.cinch.query;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.simpleflatmapper.jdbc.JdbcMapper;
import org.simpleflatmapper.jdbc.JdbcMapperFactory;
import org.simpleflatmapper.map.PropertyNameMatcherFactory;
import org.simpleflatmapper.map.mapper.DefaultPropertyNameMatcherFactory;

public class MapperFactory {
	private static HashMap<String, JdbcMapper<Object>> mapperCache = new HashMap<String, JdbcMapper<Object>>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> JdbcMapper<T> getMapper(Type type) {
		JdbcMapper mapper;
		String name = type.getTypeName();
		mapper = mapperCache.get(name);
		if (null != mapper)
			return mapper;
		else {
			PropertyNameMatcherFactory factory = DefaultPropertyNameMatcherFactory.CASE_SENSITIVE_EXACT_MATCH;
			mapper = JdbcMapperFactory.newInstance().propertyNameMatcherFactory(factory).ignorePropertyNotFound()
					.newMapper(type);
			mapperCache.put(name, mapper);
		}
		return mapper;
	}
	
	public static <T> JdbcMapper<T> getMapper(Class<T> classType) {
		return getMapper((Type) classType);
	}

}
