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

import java.io.Reader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition.NewRepository;
import org.sonar.api.server.rule.RulesDefinition.NewRule;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ReSharperNewRepositoryRulesParser implements ReSharperParser {
	private final Logger LOG = LoggerFactory.getLogger(ReSharperNewRepositoryRulesParser.class);
	
	private NewRepository repository;

	private ReSharperRulesAttributes rulesAttributes;

	public ReSharperNewRepositoryRulesParser(ReSharperRulesAttributes rulesAttributes, NewRepository repository) {
		this.rulesAttributes=rulesAttributes;
		this.repository=repository;
	}

	@Override
	public void parse(Reader reader) {
        try {
            InputSource source = new InputSource(reader);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            NodeList nodes = (NodeList) xpath.evaluate("//IssueType",source, XPathConstants.NODESET);

            if (nodes == null || nodes.getLength() == 0)  {
                String logMsg = "No IssueType nodes found in profile file";
                LOG.warn(logMsg);
            }
            else {

                int count = nodes.getLength();
                LOG.debug("Found " + count + " IssueType nodes (rules)" );

                // For each rule we extract the elements
                for (int idxRule = 0; idxRule < count; idxRule++) {
                    Element ruleElement = (Element) nodes.item(idxRule);

                    String ruleId = ruleElement.getAttribute("Id");
                    NewRule newRule = repository.createRule(ruleId);
                    newRule.setName(ruleId);
                    String active = ruleElement.getAttribute("Enabled");
                    

                    String category = ruleElement.getAttribute("Category");

                    String description = ruleElement.getAttribute("Description");
                    if(StringUtils.isEmpty(description)) { 
                    	description=ruleId;
                    }
                    String wikiLink = ruleElement.getAttribute("WikiUrl");
                    if (!StringUtils.isBlank(wikiLink))
                    {
                        description += "<br /><a href='"+wikiLink+"'>" + wikiLink + "</a>";
                    }

                    if (!StringUtils.isBlank(category))
                    {
                        description += "<br />(Category: "+category + ")";
                    }

                    newRule.setHtmlDescription(description);

                    String severity = ruleElement.getAttribute("Severity");
                    String sonarSeverity=ReSharperUtils.translateResharperPriorityIntoSonarSeverity(severity);
                    newRule.setSeverity(sonarSeverity);
                    
                    ReSharperRuleAttributes ruleAttributes = rulesAttributes.get(ruleId);
                    newRule.setType(ruleAttributes.getRuleType());
                }
            }
        } catch (XPathExpressionException e) {
            String logMsg = "xpath exception while parsing resharper config file: " + e.getMessage();
            LOG.warn(logMsg);
            throw new IllegalStateException(logMsg,e);
        }
    }

	private String reSharperSeverityToSonarSeverity(String reSharperSeverity) {
		switch (reSharperSeverity) {
		case "ERROR" : 
			return "BLOCKER" ;
		case "WARNING" :
			return "CRITICAL" ;
		default:
			return "INFO";
		
		}
	}
}
