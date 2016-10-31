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

import org.apache.commons.lang3.StringUtils;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.debt.internal.DefaultDebtRemediationFunction;
import org.w3c.dom.Element;

public  class DebtRemediationFunctionReader {
	public static final String FUNCTION_ATTRIBUTE = "DebtRemediationFunction";
	public static final String GAPMULTIPLIER_ATTRIBUTE = "DebtRemediationGapMultiplier";
	public static final String BASEEFFORT_ATTRIBUTE = "DebtRemediationBaseEffort";


	static DebtRemediationFunction read(Element ruleElement) {
		DebtRemediationFunction result=null;
		String functionName=ruleElement.getAttribute(FUNCTION_ATTRIBUTE) ;
		if(StringUtils.isEmpty(functionName)) {
			return result;
		}

		DebtRemediationFunction.Type functionType=DebtRemediationFunction.Type.valueOf(functionName);
		String coefficient = ruleElement.getAttribute(GAPMULTIPLIER_ATTRIBUTE);
		String  offset = ruleElement.getAttribute(BASEEFFORT_ATTRIBUTE);
		result = new DefaultDebtRemediationFunction(functionType, coefficient, offset);
		return result;
	}
}
