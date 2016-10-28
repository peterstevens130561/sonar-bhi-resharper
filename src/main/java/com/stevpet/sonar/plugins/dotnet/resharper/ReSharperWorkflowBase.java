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

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.InspectCodeResultsParser;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueValidationException;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueValidator;
import com.stevpet.sonar.plugins.dotnet.resharper.saver.InspectCodeIssuesSaver;

public class ReSharperWorkflowBase implements ResharperWorkflow {
    private Logger LOG = LoggerFactory.getLogger(ReSharperWorkflowBase.class);
    private InspectCodeResultsParser inspectCodeResultsParser;
    private InspectCodeIssuesSaver inspectCodeIssuesSaver;
    private InspectCodeRunner inspectCodeRunner;
    private List<InspectCodeIssue> issues;
    private IssueValidator issueValidator;

    public ReSharperWorkflowBase(
    		InspectCodeResultsParser inspectCodeResultsParser,
            InspectCodeIssuesSaver inspectCodeIssuesSaver, 
            InspectCodeRunner inspectCodeRunner, 
            IssueValidator issueValidator) {
        this.inspectCodeResultsParser = inspectCodeResultsParser;
        this.inspectCodeIssuesSaver = inspectCodeIssuesSaver;
        this.inspectCodeRunner = inspectCodeRunner;
        this.issueValidator = issueValidator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.resharper.ResharperWorkflow#execute()
     */
    @Deprecated
    @Override
    public void execute() {
        inspectSolution();
        inspectCodeIssuesSaver.saveIssues(issues);
    }

    public void inspectSolution() {
        for (int retryCnt = 0; retryCnt < 1; retryCnt++) {
            File reportFile = inspectCodeRunner.inspectCode();
            issues = inspectCodeResultsParser.parse(reportFile);
            issueValidator.validate(issues);
            if (!issueValidator.validationFailed()) {
                break;
            }
            IssueValidationException exception = issueValidator.getException();
            LOG.warn("IssueValidation failed {} on try {}", exception.getMessage(),retryCnt+1);
            inspectCodeRunner.dropCache();
        }
        if(issueValidator.validationFailed()) {
            throw issueValidator.getException();
        }
    }

    @Override
    public void executeModule(Project module) {
        inspectSolution();
        inspectCodeIssuesSaver.saveModuleIssues(issues, module);
    }

}
