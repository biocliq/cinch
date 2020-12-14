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
package com.zitlab.palmyra.cinch.rshandler;

import java.util.ArrayList;
import java.util.List;

import org.simpleflatmapper.util.CheckedConsumer;

public class ListHandler<T> implements CheckedConsumer<T>{

	private List<T> result = new ArrayList<T>();
	
	@Override
	public void accept(T t) {
		result.add(t);
	}
	
	public List<T> list(){
		return result;
	}
}