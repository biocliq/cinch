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
package com.zitlab.cinch.schema.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.zitlab.generic.file.FileStorage;


public class FileSchemaLoader{
	
	public List<TTCfg> loadRelations(String baseFolder){	
		
		createIfNotExists(baseFolder);
		FileStorage storage = new FileStorage(baseFolder);
//		Predicate<Path> filter = (file -> file.getFileName().endsWith(".json"));
//		storage.setFilter(filter);
		SchemaConfigFileProcessor loader = new SchemaConfigFileProcessor();
		storage.setProcessor(loader);
		storage.process();
		return loader.getConfList();
	}
	
	private void createIfNotExists(String base) {
		Path path = Paths.get(base);
		File folder = path.toFile();
		if(!folder.exists()) {
			folder.mkdirs();
		}
	}
}
