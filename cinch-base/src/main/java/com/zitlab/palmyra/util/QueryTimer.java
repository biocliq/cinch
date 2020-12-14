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

public class QueryTimer {
	private static long time = 0;
	private static long start, end;

	public static void start() {
		start = System.nanoTime();
	}

	public static void pause() {
		end = System.nanoTime();
		time += (end - start);
	}

	public static void reset() {
		time = 0;
	}
	
	public static long getTimer() {
		return time/(1000 * 1000);
	}
}
