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

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.zitlab.generic.file.Processor;
import com.zitlab.palmyra.api2db.exception.GenericValidation;
import com.zitlab.palmyra.api2db.exception.Validation;

public class SchemaConfigFileProcessor implements Processor {
	private static final ObjectReader pageReader = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readerFor(TTCfg.class);

	List<TTCfg> confList = new ArrayList<TTCfg>();

	@Override
	public boolean process(String key, Path path) {
		try {
			TTCfg cfg = pageReader.readValue(path.toFile());
			String filename = path.getFileName().toString();
			String citype = filename.substring(0, filename.length() - 5);
			if (citype.equals(cfg.getType()))
				confList.add(cfg);
			else {
				throw new GenericValidation(Validation.OTHERS,
						"Filename " + path.toString() + " does not match with the citype " + cfg.getType());
			}
		} catch (IOException e) {
			throw new GenericValidation(Validation.UNKNOWN,"Error while loading " + path.getFileName(), e);
		}
		return true;
	}

	public List<TTCfg> getConfList() {
		return confList;
	}
}