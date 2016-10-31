package com.stevpet.sonar.plugins.dotnet.resharper;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RulesDefinition.NewRule;
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
		ruleElement.setAttribute(reader.COEFFICIENT_ATTRIBUTE, "5min" );
		DebtRemediationFunction function = reader.read(ruleElement);
		assertNotNull(function);
		assertEquals(5,function.coefficient().toMinutes());
	}
	
	@Test
	public void isLinear()
	{
		ruleElement.setAttribute(reader.FUNCTION_ATTRIBUTE, DebtRemediationFunction.Type.LINEAR.toString());
		ruleElement.setAttribute(reader.COEFFICIENT_ATTRIBUTE, "20min" );
		DebtRemediationFunction function = reader.read(ruleElement);
		assertNotNull(function);
		assertEquals(20,function.coefficient().toMinutes());
	}
	
	@Test
	public void isWithOffiset()
	{
		ruleElement.setAttribute(reader.FUNCTION_ATTRIBUTE, DebtRemediationFunction.Type.LINEAR_OFFSET.toString());
		ruleElement.setAttribute(reader.COEFFICIENT_ATTRIBUTE, "30min" );
		ruleElement.setAttribute(reader.OFFSET_ATTRIBUTE, "40min" );
		DebtRemediationFunction function = reader.read(ruleElement);
		assertNotNull(function);
		assertEquals(30,function.coefficient().toMinutes());
		assertEquals(40,function.offset().toMinutes());
	}
}
