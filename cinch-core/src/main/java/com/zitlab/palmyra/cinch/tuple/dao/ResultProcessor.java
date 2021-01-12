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
package com.zitlab.palmyra.cinch.tuple.dao;

import org.simpleflatmapper.util.CheckedConsumer;

import com.zitlab.palmyra.cinch.pojo.TupleResultSet;

public interface ResultProcessor<T> extends CheckedConsumer<T>{

	public void startMetadata(TupleResultSet rs);

	public void endMetadata(TupleResultSet rs);

}
