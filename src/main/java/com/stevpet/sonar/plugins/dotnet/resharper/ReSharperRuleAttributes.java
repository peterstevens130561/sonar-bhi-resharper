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

import org.eclipse.aether.util.StringUtils;
import org.sonar.api.rules.RuleType;

public class ReSharperRuleAttributes {
	private final String id ;
	private String severity ;
	private RuleType ruleType= RuleType.CODE_SMELL;
	private String tags;
	
	public  ReSharperRuleAttributes(String id) {
		this.id=id;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	public void setRuleType(String type) {
		if(StringUtils.isEmpty(type)) {
			this.ruleType=RuleType.CODE_SMELL;
		}
		ruleType=RuleType.valueOf(type);
	}
	
	public RuleType getRuleType() {
		return ruleType;
	}

	public void setTags(String tags) {
		this.tags=tags;
	}
	
	public String getTags() {
		return tags;
	}
}
