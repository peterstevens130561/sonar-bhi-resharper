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

import org.sonar.api.resources.Project;

public interface ResharperWorkflow {

    /**
     * Should perform what is needed to get the resharper issues into the current project. For the
     * first project that will mean that it has to run inspectcode.
     */
    void execute();

    /**
     * Should perform what is needed to get the resharper issues into the current module (childProject). For the
     * first module that will mean that it has to run inspectcode.
     * @param module
     */
    void executeModule(Project module);

}