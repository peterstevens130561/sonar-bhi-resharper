/*
 * Sonar .NET Plugin :: ReSharper
 * Copyright (C) 2013 John M. Wright
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugins.dotnet.resharper.profiles;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.profiles.ProfileExporter;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;


import com.stevpet.sonar.plugins.dotnet.resharper.ReSharperConfiguration;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that allows to export a Sonar profile into a ReSharper rule definition file.
 */
public abstract class ReSharperProfileExporter extends ProfileExporter {

    protected ReSharperProfileExporter(String languageKey) {
        super(ReSharperConfiguration.REPOSITORY_KEY + "-" + languageKey, ReSharperConfiguration.REPOSITORY_NAME);
        setSupportedLanguages(languageKey);
        setMimeType("application/xml");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportProfile(RulesProfile profile, Writer writer) {
        try {
            printRules(profile, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Error while generating the ReSharper profile to export: " + profile, e);
        }
    }

    private void printRules(RulesProfile profile, Writer writer) throws IOException {
        //Create a file that matches the format of the ReSharper inspectcode.exe output

        writer.append("<Report>\n");
        writer.append("  <IssueTypes>\n");

        List<ActiveRule> activeRules = profile.getActiveRulesByRepository(getKey());
        List<ReSharperRule> rules = transformIntoReSharperRules(activeRules);

        // print out each rule
        for (ReSharperRule rule : rules) {
            printRule(writer, rule);
        }

        writer.append("  </IssueTypes>\n");
        writer.append("</Report>");
    }


    private void printRule(Writer writer, ReSharperRule resharperRule) throws IOException {
        // This is generally what the output will look like:
        //        <IssueType Id="ClassNeverInstantiated.Global"
        //                   Enabled="True"
        //                   Description="Class is never instantiated: Non-private accessibility"
        //                   Severity="SUGGESTION" />

 
        writer.append("    <IssueType");
        writer.append(" Id=\"");
        writer.append(StringEscapeUtils.escapeXml(resharperRule.getId()));

        
        writer.append("\" Enabled=\"");
        writer.append(StringEscapeUtils.escapeXml(String.valueOf(resharperRule.isEnabled())));

        String category = resharperRule.getCategory();
        if (category != null && !StringUtils.isBlank(category)) {
            writer.append("\" Category=\"");
            writer.append(StringEscapeUtils.escapeXml(category));
        }

        String wiki = resharperRule.getWikiLink();
        if (wiki != null && !StringUtils.isBlank(wiki)) {
            writer.append("\" WikiUrl=\"");
            writer.append(StringEscapeUtils.escapeXml( wiki));
        }

        writer.append("\" Description=\"");
        writer.append(StringEscapeUtils.escapeXml(resharperRule.getDescription()));
        writer.append("\" Severity=\"");
        writer.append(StringEscapeUtils.escapeXml(resharperRule.getSeverity().toString()));
        writer.append("\"/>\n");
    }

    private List<ReSharperRule> transformIntoReSharperRules(List<ActiveRule> activeRulesByPlugin) {
        List<ReSharperRule> result = new ArrayList<ReSharperRule>();

        for (ActiveRule activeRule : activeRulesByPlugin) {
            ReSharperRule resharperRule = ReSharperRule.createFromActiveRule(activeRule);
            result.add(resharperRule);
        }
        return result;
    }



}
