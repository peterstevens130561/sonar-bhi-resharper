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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ReSharperRulesAttributeParser {
	private final ReSharperRulesAttributes rulesAttributes;
	private final Logger LOG = LoggerFactory.getLogger(ReSharperRulesAttributeParser.class);

	public ReSharperRulesAttributeParser(ReSharperRulesAttributes rulesAttributes) {
		this.rulesAttributes = rulesAttributes;
	}

	public void parse(String defaultMapping) {
		try {
	        InputStream mappingFileStream = ReSharperRulesAttributeParser.class.getResourceAsStream(defaultMapping);
	        Reader mappingReader = new InputStreamReader(mappingFileStream);
			InputSource source = new InputSource(mappingReader);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			NodeList nodes = (NodeList) xpath.evaluate("//Rule", source, XPathConstants.NODESET);

			if (nodes == null || nodes.getLength() == 0) {
				String logMsg = "No IssueType nodes found in profile file";
				LOG.warn(logMsg);
			} else {

				int count = nodes.getLength();
				LOG.debug("Found " + count + " IssueType nodes (rules)");

				// For each rule we extract the elements
				for (int idxRule = 0; idxRule < count; idxRule++) {
					Element ruleElement = (Element) nodes.item(idxRule);

					String ruleId = ruleElement.getAttribute("Id");
					ReSharperRuleAttributes ruleAttributes = rulesAttributes.create(ruleId);
					String type = ruleElement.getAttribute("RuleType");
					ruleAttributes.setRuleType(type);
					
					String tags = ruleElement.getAttribute("Tags");
					ruleAttributes.setTags(tags);
					
					DebtRemediationFunction debtRemediationFunction = DebtRemediationFunctionReader.read(ruleElement);
					ruleAttributes.setDebtRemediationFunction(debtRemediationFunction);
				}
			}
		} catch (XPathExpressionException e) {
			String logMsg = "xpath exception while parsing resharper config file: " + e.getMessage();
			LOG.warn(logMsg);
			throw new IllegalStateException(logMsg, e);
		}
	}


}