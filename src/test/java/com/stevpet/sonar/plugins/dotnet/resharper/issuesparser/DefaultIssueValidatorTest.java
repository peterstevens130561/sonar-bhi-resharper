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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.DefaultIssueValidator;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueValidator;

public class DefaultIssueValidatorTest {

    IssueValidator issueValidator;
    private List<InspectCodeIssue> issues;
    @Before
    public void before() {
        issueValidator = new DefaultIssueValidator();
        issues = new ArrayList<>();
    }
    
    @Test
    public void normalIssue_shouldPass() {
        createIssue("RedundantNameQualifier","Qualifier is redundant");

        try {
            issueValidator.validate(issues);
        } catch (Exception e) {
            fail("should not get any exception for a normal issue");
        }
 
    }
    
    @Test
    public void goodNamingIssue_shouldPass() {
        createIssue("InconsistentNaming","Name 'm_isTSR' does not match rule 'Instance fields (private)'. Suggested name is 'm_isTsr'.");
        issueValidator.validate(issues);
        assertFalse("validation should pass",issueValidator.validationFailed());
    }
    
    @Test
    public void wrongNamingIssue_shouldFail() {
        createIssue("InconsistentNaming","Name 'm_isTSR' does not match rule 'Instance fields (private)'. Suggested name is '_misTsr'.");
        issueValidator.validate(issues);
        assertTrue("validation should have gotten failed",issueValidator.validationFailed());       
    }
    
    @Test
    public void multipleIssues_shouldFail() {
        createIssue("InconsistentNaming","Name 'm_isTSR' does not match rule 'Instance fields (private)'. Suggested name is '_misTsr'.");
        createIssue("InconsistentNaming","Name 'm_isTSR' does not match rule 'Instance fields (private)'. Suggested name is 'm_isTsr'.");
        createIssue("Somethinggood","Completely bogus");
        
        issueValidator.validate(issues);
        assertTrue("validation should fail, first issue is wrong",issueValidator.validationFailed());       
    }
    
    @Test
    public void wrongCSharpErrore_shouldFail() {
        createIssue("CSharpErrors","Cannot resolve symbol 'joaSTARSInterface'");
        issueValidator.validate(issues);
        assertFalse("validation should pass",issueValidator.validationFailed());       
    }
    
    @Test
    public void obsoleteCSharpErrore_shouldPass() {
        createIssue("CSharpErrors","Event 'joaJewelUtilitiesUI.Controls.ListView.joaObjectListView.AfterCheck' is obsolete: ");
        issueValidator.validate(issues);
        assertFalse("validation should have passed, obsolete is ok",issueValidator.validationFailed());       
    }
    private void createIssue(String typeId, String message) {
        InspectCodeIssue normalIssue = new InspectCodeIssue();
        normalIssue.setTypeId(typeId);
        normalIssue.setMessage(message);
        normalIssue.setRelativePath("relative");
        issues.add(normalIssue);        
    }
}
