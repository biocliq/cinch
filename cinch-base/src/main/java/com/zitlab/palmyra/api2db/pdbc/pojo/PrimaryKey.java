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
package com.zitlab.palmyra.api2db.pdbc.pojo;

import java.util.Map;

public class PrimaryKey {
	private Object key;
	private boolean composite = false;

	public Object getValue() {
		return key;
	}
	
	public boolean isComposite() {
		return composite;
	}

	public void setValue(Number key) {
		this.key = key;
		composite = false;
	}

	public void setValue(String key) {
		this.key = key;
		composite = false;
	}

	public void setValue(Map<String, Object> key) {
		this.key = key;
		composite = true;
	}
}
