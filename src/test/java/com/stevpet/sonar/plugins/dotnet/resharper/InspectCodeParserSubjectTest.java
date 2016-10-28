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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.InspectCodeParserSubject;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueObserver;
public class InspectCodeParserSubjectTest {

    private File report;
    private InspectCodeParserSubject parser = new InspectCodeParserSubject();
    private List<InspectCodeIssue> issues = new ArrayList<InspectCodeIssue>();
    private IssueObserver issueObserver = new IssueObserver(issues);
    @Before
    public void before() {
        report=TestUtils.getResource("/InspectCode/resharper-report.xml"); 
        report=TestUtils.getResource("/InspectCode/resharper-report.xml");     
        parser.registerObserver(issueObserver);
        // When
        parser.parseFile(report);
    }
    
    @Test
    public void expectNineIssues() {
        assertEquals("expect 9 issues",9,issues.size());
    }

    @Test
    public void CheckFirstIssue() {
        InspectCodeIssue firstIssue=issues.get(0);
        assertEquals("SuggestUseVarKeywordEvident",firstIssue.getTypeId());
        assertEquals("BasicControls/Tools/GraphicTools.cs",firstIssue.getRelativeUnixPath());
        assertEquals("41",firstIssue.getLine());
        assertEquals("Use implicitly typed local variable declaration",firstIssue.getMessage());
    }
    
    @Test
    public void CheckLastIssue() {
        InspectCodeIssue firstIssue=issues.get(8);
        assertEquals("ResxNotResolved",firstIssue.getTypeId());
        assertEquals("BasicControls.TestApp/Form1.resx",firstIssue.getRelativeUnixPath());
        assertEquals("532",firstIssue.getLine());
        assertEquals("Cannot resolve symbol 'splitContainer1.Panel2'",firstIssue.getMessage());
    }
}
