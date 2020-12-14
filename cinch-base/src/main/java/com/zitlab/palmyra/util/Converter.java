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
package com.zitlab.palmyra.util;

import java.math.BigDecimal;

public class Converter {
	public static BigDecimal asDecimal(Object obj) {
		try {
			if (null == obj)
				return null;
			else
				return (BigDecimal) obj;
		} catch (ClassCastException e) {
			return new BigDecimal(Double.valueOf(obj.toString()));
		}
	}

	public static Double asDouble(Object obj) {
		try {
			if (null == obj)
				return null;
			return (Double) obj;
		} catch (ClassCastException e) {
			return Double.parseDouble(obj.toString());
		}
	}

	public static Float asFloat(Object obj) {
		try {
			if (null == obj)
				return null;
			else
				return (Float) obj;
		} catch (ClassCastException e) {
			return Float.parseFloat(obj.toString());
		}
	}

	public static Integer asInt(Object obj) {
		try {
			if (null == obj)
				return null;
			else if (obj instanceof Integer)
				return (Integer) obj;
			else
				return Integer.parseInt(obj.toString());
		} catch (ClassCastException e) {
			return null;
		}
	}

	public static Long asLong(Object obj) {
		try {
			if (null == obj)
				return null;
			else
				return (Long) obj;
		} catch (ClassCastException e) {
			return Long.parseLong(obj.toString());
		}
	}

	public static String asString(Object obj) {
		if (null == obj)
			return null;
		else if (obj instanceof String)
			return (String) obj;
		else
			return obj.toString();
	}
}
