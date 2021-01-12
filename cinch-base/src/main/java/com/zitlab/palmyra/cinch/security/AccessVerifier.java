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
package com.zitlab.palmyra.cinch.security;

import java.util.Collection;
import java.util.Collections;

import com.zitlab.palmyra.cinch.pojo.Tuple;

public interface AccessVerifier {
	
	public static final AccessVerifier NOOP = new NoopVerifier();
	
	public Collection<String> getNonUpdatableFields(Tuple item);

	public Collection<String> getNonInsertableFields(Tuple item);

	public boolean isInsertable(Tuple item);

	public boolean isUpdatable(Tuple item);

	public boolean isDeletable(Tuple item);
	
	public static class NoopVerifier implements AccessVerifier{		
		
		@SuppressWarnings("unchecked")
		@Override
		public Collection<String> getNonUpdatableFields(Tuple item) {
			return Collections.EMPTY_LIST;
		}

		@SuppressWarnings("unchecked")
		@Override		
		public Collection<String> getNonInsertableFields(Tuple item) {
			return Collections.EMPTY_LIST;
		}

		@Override
		public boolean isInsertable(Tuple item) {
			return true;
		}

		@Override
		public boolean isUpdatable(Tuple item) {
			return true;
		}

		@Override
		public boolean isDeletable(Tuple item) {
			return true;
		}
		
	}
}
