/*
 * Resharper Plugin
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.stevpet.sonar.plugins.dotnet.resharper;

import java.util.HashMap;
import java.util.Map;

public class ReSharperRulesAttributes {

	Map<String,ReSharperRuleAttributes> dictionary = new HashMap<>();
	ReSharperRuleAttributes create(String key) {
		ReSharperRuleAttributes value = new ReSharperRuleAttributes(key);
		dictionary.put(key, value);
		return value;
	}
	
	ReSharperRuleAttributes get(String key) {
		if(dictionary.containsKey(key)) {
			return dictionary.get(key);
		} else {
			return new ReSharperRuleAttributes(key);
		}
	}

	public int size() {
		return dictionary.size();
	}
}
