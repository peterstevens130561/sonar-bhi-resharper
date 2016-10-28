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

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.DefaultInspectCodeResultsParser;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.DefaultIssueValidator;
import com.stevpet.sonar.plugins.dotnet.resharper.saver.DefaultInspectCodeIssuesSaver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultReSharperWorkflow extends ReSharperWorkflowBase implements
		BatchExtension {

	public DefaultReSharperWorkflow(ResourcePerspectives resourcePerspectives,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			Settings settings, FileSystem fileSystem,
			ReSharperConfiguration reSharperConfiguration,
			InspectCodeBatchData inspectCodeBatchData) {
		super(new DefaultInspectCodeResultsParser(),
				new DefaultInspectCodeIssuesSaver(
						resourcePerspectives,
						microsoftWindowsEnvironment),
				new DefaultInspectCodeRunner(settings,
						microsoftWindowsEnvironment, fileSystem,
						new ReSharperCommandBuilder(),
						new LockedWindowsCommandLineExecutor(
								new DefaultProcessLock()),
						reSharperConfiguration, inspectCodeBatchData),
				new DefaultIssueValidator());
	}

}
