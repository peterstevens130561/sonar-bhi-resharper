package com.stevpet.sonar.plugins.dotnet.resharper.saver;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.MessageException;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultInspectCodeIssuesSaver implements InspectCodeIssuesSaver {

    protected Logger log = LoggerFactory.getLogger(DefaultInspectCodeIssuesSaver.class);
    private ResourcePerspectives resourcePerspectives;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    public DefaultInspectCodeIssuesSaver(ResourcePerspectives resourcePerspectives, MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.resourcePerspectives = resourcePerspectives;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }

    /**
     * Also injects the logger, for testing purposes only
     * @param resourcePerspectives
     * @param microsoftWindowsEnvironment
     * @param log
     */
    DefaultInspectCodeIssuesSaver(ResourcePerspectives resourcePerspectives, MicrosoftWindowsEnvironment microsoftWindowsEnvironment,Logger log) {
        this.resourcePerspectives = resourcePerspectives;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.log = log;
    }
    @Override
    public void saveIssues(List<InspectCodeIssue> issues) {
        for (InspectCodeIssue issue : issues) {
            saveIssue(issue);
        }
    }

    @Override
    public void saveModuleIssues(List<InspectCodeIssue> issues, Project module) {
        if(microsoftWindowsEnvironment.isUnitTestProject(module)) {
            return;
        }
        
        for (InspectCodeIssue issue : issues) {
            saveModuleIssue(issue,module);
        } 
    }
    
    private void saveModuleIssue(InspectCodeIssue inspectCodeIssue, Project module) {
        String relativeIssuePath = inspectCodeIssue.getRelativeUnixPath();
        String beginPath = module.getPath() + "/";
        if (!relativeIssuePath.startsWith(beginPath)) {
            return;
        }
        int offset = beginPath.length();
        String childPath = relativeIssuePath.substring(offset);
        saveIssuable(inspectCodeIssue, childPath);
    }

    private void saveIssue(InspectCodeIssue inspectCodeIssue) {
        String relativePath = inspectCodeIssue.getRelativePath();
        saveIssuable(inspectCodeIssue, relativePath);
    }

    private void saveIssuable(InspectCodeIssue inspectCodeIssue, String relativePath) {
        if(isIssueOfTestFile(inspectCodeIssue)) {
            log.debug("ignoring test file {}",relativePath);
            return;
        }
        Issuable issuable = createIssuable(relativePath);
        if(issuable==null) {
            return;
        }
        int line = Integer.parseInt(inspectCodeIssue.getLine());
        RuleKey key = RuleKey.of("resharper-cs", inspectCodeIssue.getTypeId());
        String message = inspectCodeIssue.getMessage();
        Issue issue = issuable.newIssueBuilder()
                .ruleKey(key)
                .line(line)
                .message(message).build();
        try {
            issuable.addIssue(issue);
        } catch (MessageException e) {
            log.warn("exception thrown during saving issue: {} ",e.getMessage());
            Pattern pattern = Pattern.compile("The rule 'resharper-cs:(.*)' does not exist.");
            Matcher matcher = pattern.matcher(e.getMessage());
            if(matcher.find()) {
            	String missingRule=matcher.group(1);
            	StringBuilder sb = new StringBuilder();
            	sb.append("The rule '").append(missingRule).append("' is missing from the SonarQube Re# ruleset, and has to be added\n")
            	.append(" to the plugin. See src/main/java/resources/ReSharper/DefaultRules.ReSharper");
            	log.error(sb.toString());
            }
            
            
        }
    }


    private Issuable createIssuable( String relativePath) {
        
        File myResource = File.create(relativePath);
        if (myResource == null) {
            log.debug("could not resolve " + relativePath);
            return null;
        }
        Issuable issuable = resourcePerspectives.as(Issuable.class, myResource);
        if (issuable == null) {
            log.debug("could not create issuable for " + relativePath);
            return null;
        }
        return issuable;
    }
    

    private boolean isIssueOfTestFile(InspectCodeIssue inspectCodeIssue) {
        String relativePath = inspectCodeIssue.getRelativePath();
        java.io.File sourceFile=new java.io.File(microsoftWindowsEnvironment.getCurrentSolution().getSolutionDir(),relativePath);
        List<java.io.File> files = microsoftWindowsEnvironment.getUnitTestSourceFiles();
        return files.contains(sourceFile);    
    }


}
