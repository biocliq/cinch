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

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringBuilderCache {
	private static final int MAX_SIZE = 256;
	private static StringBuilder[] cache = new StringBuilder[MAX_SIZE];
	private static AtomicInteger readIndex = new AtomicInteger(0);
	private static AtomicInteger writeIndex = new AtomicInteger(0);
	private static final Logger logger = LoggerFactory.getLogger(StringBuilderCache.class);

	public static StringBuilder get() {
		int i, j;
		StringBuilder result = null;
		int loopCount = 0;
		while (loopCount < 5) {
			loopCount++;
			i = readIndex.get();
			if (null == cache[i]) {
				return new StringBuilder();
			}
			if (i < MAX_SIZE - 1)
				j = i + 1;
			else
				j = 0;
			if (readIndex.compareAndSet(i, j)) {
				result = cache[i];
				if (null != result) {
					cache[i] = null;
					return result;
				} else {
					return new StringBuilder();
				}
			}
		}
		logger.error("Loop detected while getting from Cache {}", loopCount);
		return new StringBuilder();
	}

	public static String release(StringBuilder sb) {
		String result = sb.toString();
		sb.setLength(0);
		int i, j, loopCount;
		i = writeIndex.get();

		if (null != cache[i]) {
			return result;
		}

		j = 0;
		loopCount = 0;
		while (loopCount < 5) {
			loopCount++;
			i = writeIndex.get();
			if (i < MAX_SIZE - 1)
				j = i + 1;
			else
				j = 0;
			if (writeIndex.compareAndSet(i, j)) {
				break;
			}
		}
		if (loopCount <= 5) {
			if (null == cache[i]) {
				cache[i] = sb;
			}
		}else {
			logger.error("Loop detected while releasing to Cache {}", loopCount);
		}
		return result;
	}

	public static AtomicInteger getReadIndex() {
		return readIndex;
	}

	public static AtomicInteger getWriteIndex() {
		return writeIndex;
	}

}
