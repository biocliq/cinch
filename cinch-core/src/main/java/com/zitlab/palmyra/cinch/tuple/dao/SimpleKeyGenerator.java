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

public class SimpleKeyGenerator implements KeyGenerator {

	@Override
	public String generateKey(String format, Number sequence) {		
		String seq = String.valueOf(sequence);
		if(seq.length() >= format.length()){
			throw new RuntimeException("sequence length should be less than the key format length");
		}
		int keyLength = format.length();

		StringBuilder key = new StringBuilder(format);
		key.replace(keyLength - seq.length(), keyLength, seq);

		return key.toString().replaceAll("X", "0").replaceAll("x", "0");
	}

	@Override
	public String generateKey(String prefix, int sequence, int length) {
			
		String seq = String.valueOf(sequence);
		if(length < seq.length() + prefix.length()){
			throw new RuntimeException("Key length should be greater than prefix and sequence combined");
		}
		char[] key = new char[length];
		int index = 0;
		for (index = 0; index < length; index++) {
			if (index < prefix.length()) {
				for (char c : prefix.toCharArray()) {
					key[index] = c;
					index++;
				}
				index--;
			}else if(index < (length - seq.length())){
				key[index] = '0';
			}else{
				for (char c : seq.toCharArray()) {
					key[index] = c;
					index++;
				}
			}
		}
		return String.valueOf(key);
	}

}
