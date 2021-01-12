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
package com.zitlab.palmyra.cinch.core.audit;

public interface ChangeLogger {
	
	public static final ChangeLogger NOOP = new NoopChangeLogger();
	
	public void addLog(ChangeLog log);
	
	public void commit(String user);
	
	public void reset(String user);
	
	public static class NoopChangeLogger implements ChangeLogger{

		@Override
		public void addLog(ChangeLog log) {
						
		}

		@Override
		public void commit(String user) {
						
		}

		@Override
		public void reset(String user) {
					
		}
		
	}
}
