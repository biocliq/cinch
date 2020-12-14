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

package com.zitlab.palmyra.api2db.pojo;

public final class Action {
	
	private Action() {}
	
	public static final String CREATE_S = "create";
	public static final String UPDATE_S = "update";
	public static final String DELETE_S = "delete";
	public static final String SAVE_S = "save";
	public static final String NONE_S = "none";

	public static final int PARENT_ACTION = 0;
	public static final int UPDATE = 1;
	public static final int CREATE = 2;
	public static final int DELETE = -1;
	public static final int NONE = -2;
	public static final int NULL = -3;
	public static final int SAVE = 3;
	public static final int CREATE_IFNOT_EXISTS = 4;
	public static final int NO_PROCESS = 5;
	
	public static String getString(int action){
		switch(action) {
		case CREATE:
			return CREATE_S;
		case UPDATE:
			return UPDATE_S;
		case DELETE:
			return DELETE_S;
		case NONE:
			return NONE_S;
		case SAVE:
			return SAVE_S;
		default:
			return null;
		}
	}
	
	public static Integer getInt(String action) {
		switch (action) {
		case CREATE_S:
			return CREATE;
		case DELETE_S:		
			return DELETE;
		case UPDATE_S:
			return UPDATE;
		case NONE_S:
			return NONE;
		case SAVE_S:
			return SAVE;
		default:
			return null;
		}
	}
	
	public static Integer getRelInt(String action) {
		switch (action) {
		case CREATE_S:
			return CREATE;
		case DELETE_S:		
			return DELETE;
		case UPDATE_S:
			return UPDATE;
		case SAVE_S:
			return SAVE;
		default:
			return PARENT_ACTION;
		}
	}
}
