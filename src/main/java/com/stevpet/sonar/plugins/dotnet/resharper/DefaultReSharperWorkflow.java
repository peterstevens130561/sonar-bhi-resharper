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
