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

package com.zitlab.palmyra.cinch.exception;

import com.zitlab.palmyra.exception.CodedException;

public class RecordException extends CodedException{
	private String ciType;
	private Object id;
	
	private static final long serialVersionUID = 1L;
	
	private RecordException(String errorCode, String message, String ciType, Object id) {
		super(errorCode, message);
		this.ciType = ciType;
		this.id = id;
	}
	
	private RecordException(String message, String ciType, Object id) {
		super(Validation.OTHERS.getCode(), message);
		this.ciType = ciType;
		this.id = id;
	}

	public String getCiType() {
		return ciType;
	}

	public Object getId() {
		return id;
	}

	public class CreateForbidden extends RecordException {		
		private static final long serialVersionUID = 1L;

		public CreateForbidden(String message, String ciType, Object id) {
			super(message, ciType, id);
		}
		
		public CreateForbidden(String message, String ciType) {
			super(message, ciType, null);
		}
	}
	
	public class UpdateForbidden extends RecordException {		
		private static final long serialVersionUID = 1L;

		public UpdateForbidden(String message, String ciType, Object id) {
			super(message, ciType, id);
		}
		
		public UpdateForbidden(String message, String ciType) {
			super(message, ciType, null);
		}
	}
	
	public class DeleteForbidden extends RecordException {		
		private static final long serialVersionUID = 1L;

		public DeleteForbidden(String message, String ciType, Object id) {
			super(message, ciType, id);
		}
		
		public DeleteForbidden(String message, String ciType) {
			super(message, ciType, null);
		}
	}
	
	public static class NotFound extends RecordException {		
		private static final long serialVersionUID = 1L;

		public NotFound(String errorCode, String message, String ciType, Object id) {
			super(errorCode, message, ciType, id);
		}
		
		public NotFound(String message, String ciType, Object id) {
			super(message, ciType, id);
		}
		
		public NotFound(String message, String ciType) {
			super(message, ciType, null);
		}
	}
}

