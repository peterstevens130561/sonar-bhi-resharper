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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.server.debt.DebtRemediationFunction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DebtRemediationFunctionReaderTest {

	private Element ruleElement;

	DebtRemediationFunctionReader reader ;
	
	@Before()
	public void before() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		ruleElement = document.createElement("Rule");

	}

	@Test
	public void notSpecified() 
	{
		DebtRemediationFunction function = reader.read(ruleElement);
		assertNull("should not be set as the function is not specified",function);
	}
	
	@Test
	public void isConstant()
	{
		ruleElement.setAttribute(reader.FUNCTION_ATTRIBUTE, DebtRemediationFunction.Type.CONSTANT_ISSUE.toString());
		ruleElement.setAttribute(reader.BASEEFFORT_ATTRIBUTE, "5min" );
		DebtRemediationFunction function = reader.read(ruleElement);
		assertNotNull(function);
		assertEquals("5min",function.baseEffort());
	}
	
	@Test
	public void isLinear()
	{
		ruleElement.setAttribute(reader.FUNCTION_ATTRIBUTE, DebtRemediationFunction.Type.LINEAR.toString());
		ruleElement.setAttribute(reader.GAPMULTIPLIER_ATTRIBUTE, "20min" );
		DebtRemediationFunction function = reader.read(ruleElement);
		assertNotNull(function);
		assertEquals("20min",function.gapMultiplier());
	}
	
	@Test
	public void isWithOffiset()
	{
		ruleElement.setAttribute(reader.FUNCTION_ATTRIBUTE, DebtRemediationFunction.Type.LINEAR_OFFSET.toString());
		ruleElement.setAttribute(reader.GAPMULTIPLIER_ATTRIBUTE, "30min" );
		ruleElement.setAttribute(reader.BASEEFFORT_ATTRIBUTE, "40min" );
		DebtRemediationFunction function = reader.read(ruleElement);
		assertNotNull(function);
		assertEquals("30min",function.gapMultiplier());
		assertEquals("40min",function.baseEffort());
	}
}
