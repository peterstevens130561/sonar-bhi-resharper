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
package com.stevpet.sonar.plugins.dotnet.resharper.issuesparser;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;

public class IssueObserver extends BaseParserObserver {
    private InspectCodeIssue issue;
    private List<InspectCodeIssue> issues;
    public IssueObserver(List<InspectCodeIssue> inspectCodeIssues) {
        this.issues = inspectCodeIssues;
        setPattern("Issues/Project/Issue");
    }   
    
    /**
     * mandatory typeId
     * @param value
     */
    @AttributeMatcher(elementName="Issue", attributeName = "TypeId")
    public void typeIdMatcher(String value) {
        issue=new InspectCodeIssue();
        issue.setTypeId(value);
        issue.setLine("1"); // in case there is no line attribute
    }
    
    /**
     * mandatory file attribute
     * @param value
     */
    @AttributeMatcher(elementName="Issue", attributeName = "File")
    public void fileMatcher(String value) {
        issue.setRelativePath(value);
    }
    /**
     * Optional line attribute
     * @param value
     */
    @AttributeMatcher(elementName="Issue", attributeName="Line")
    public void lineMatcher(String value) {
        issue.setLine(value);
    }
    
    /**
     * Optional msaage
     * @param value
     */
    @AttributeMatcher(elementName="Issue", attributeName="Message") 
    public void messageMachter(String value) {
        issue.setMessage(value);
        issues.add(issue);
    }
}