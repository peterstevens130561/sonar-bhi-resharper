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

import org.apache.commons.lang3.StringUtils;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;

public class DefaultIssueValidator implements IssueValidator {
    private IssueValidationException exception;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.IssueValidator
     * #validate(java.util.List)
     */
    @Override
    public void validate(List<InspectCodeIssue> issues) {
        exception=null;
        for (InspectCodeIssue issue : issues) {
            validateIssue(issue);
            if(exception!=null) {
                break;
            }
        }
    }

    private void validateIssue(InspectCodeIssue issue) {
        String message = issue.getMessage();
        if (StringUtils.isEmpty(message)) {
            return;
        }
        String typeId = issue.getTypeId();
        if (StringUtils.isEmpty(typeId)) {
            return;
        }

        if (typeId.equalsIgnoreCase("InconsistentNaming")
                && message.contains("does not match rule 'Instance fields (private)'. Suggested name is '_m")) {
            exception=new IssueValidationException(issue);
        }
    }

    @Override
    public boolean validationFailed() {
        return exception!=null;
    }

    @Override
    public IssueValidationException getException() {
        return exception;
    }
}
