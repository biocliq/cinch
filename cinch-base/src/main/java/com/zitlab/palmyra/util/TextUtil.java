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

/**
 * @author ksvraja
 *
 */
public final class TextUtil {

	public static String camelCase(String column) {
		return camelCase(column, false);
	}
	
	public static String camelCase(String column, boolean firstLetterUpperCase) {
		char[] result = new char[column.length()];
		char[] org = column.toCharArray();
		char c;
		int j = 0;
		int length = result.length;
		for (int i = 0; i < length; i++, j++) {
			c = org[i];
			if (c == '_' || c == ' ') {
				i++;
				if (i < length)
					c = Character.toUpperCase(org[i]);
			} else {
				c = Character.toLowerCase(c);
			}
			result[j] = c;
		}
		if(firstLetterUpperCase) {
			result[0] = Character.toUpperCase(result[0]);
		}
		return new String(result, 0, j);
	}

	public static String initCase(String column) {
		char[] result = new char[column.length()];
		char[] org = column.toLowerCase().toCharArray();
		char c;
		boolean upperFlag = true;
		for (int i = 0; i < result.length; i++) {
			c = org[i];
			if (c == '_') {
				c = ' ';
				upperFlag = true;
			} else if (upperFlag) {
				c = Character.toUpperCase(c);
				upperFlag = false;
			}
			result[i] = c;
		}
		return new String(result);
	}

	public static String replaceAll(String oldVal, char oldChar, char newChar) {
		char[] value = oldVal.toCharArray();
		if (oldChar != newChar) {
			int len = value.length;
			int i = -1;
			char[] val = value; /* avoid getfield opcode */
			char buf[] = new char[len];

			while (++i < len) {
				if (val[i] == oldChar) {
					buf[i] = newChar;
				} else
					buf[i] = val[i];
			}
			return new String(buf);
		}
		return oldVal;
	}

	public static String getLast(String value, char delimiter, int count) {
		char[] source = value.toCharArray();
		int len = source.length;

		int i = -1;
		int cntIndex = 0;

		while (++i < len && cntIndex < count) {
			if (source[i] == delimiter) {
				cntIndex++;
			}
		}
		if(cntIndex >= count)
			return value.substring(i);
		else
			throw new RuntimeException(cntIndex +  " Occurrence of " + delimiter + " is in " + value);
	}

	public static String trim(String oldVal, char oldChar) {
		char[] value = oldVal.toCharArray();

		int len = value.length;
		int i = -1;
		char[] val = value; /* avoid getfield opcode */
		char buf[] = new char[len];
		int j = 0;
		while (++i < len) {
			if (val[i] == oldChar) {
				continue;
			} else
				buf[j++] = val[i];
		}
		return new String(buf, 0, j);
	}

	public static String append(char delimiter, String... values) {
		boolean pre = false;
		StringBuilder builder = StringBuilderCache.get();
		for (String value : values) {
			if (pre) {
				builder.append(delimiter);
			} else {
				pre = true;
			}
			builder.append(value);
		}
		return StringBuilderCache.release(builder);
	}

	public static String append(String delimiter, String... values) {
		boolean pre = false;
		StringBuilder builder = StringBuilderCache.get();
		for (String value : values) {
			if (pre) {
				builder.append(delimiter);
			} else {
				pre = true;
			}
			builder.append(value);
		}
		return StringBuilderCache.release(builder);
	}

	public static String appendAll(String... values) {
		StringBuilder builder = StringBuilderCache.get();
		int index = 0;
		int size = values.length;
		for (index = 0; index < size; index++) {
			builder.append(values[index]);
		}
		return StringBuilderCache.release(builder);
	}

	public static String replaceLast(char delimiter, String value, String suffix) {
		int idx = value.lastIndexOf(delimiter);
		StringBuilder builder = StringBuilderCache.get();
		if (idx < 0) {
			builder.append(value);
		} else {
			builder.append(value.substring(0, idx));
		}
		builder.append(delimiter).append(suffix);
		return StringBuilderCache.release(builder);
	}

	public static String getFirst(String value, char delimiter) {
		int idx = value.indexOf(delimiter);
		if (idx > 0) {
			return value.substring(0, idx);
		} else
			return value;
	}

	public static String trimLast(String value, char delimiter) {
		int idx = value.lastIndexOf(delimiter);
		if (idx > 0) {
			return value.substring(0, idx);
		} else
			return value;
	}

	public static String replaceLast(String value, String suffix, char delimiter) {
		int idx = value.lastIndexOf(delimiter);
		StringBuilder sb = StringBuilderCache.get();

		if (idx > 0) {
			sb.append(value, 0, idx + 1).append(suffix).toString();
		} else
			sb.append(value).append(delimiter).append(suffix).toString();

		return StringBuilderCache.release(sb);
	}

	public static String getLast(String value, char delimiter) {
		int idx = value.lastIndexOf(delimiter);
		return value.substring(idx + 1, value.length());
	}

	public static void appendAll(StringBuilder builder, String... values) {
		int index = 0;
		int size = values.length;
		for (index = 0; index < size; index++) {
			builder.append(values[index]);
		}
	}
}
