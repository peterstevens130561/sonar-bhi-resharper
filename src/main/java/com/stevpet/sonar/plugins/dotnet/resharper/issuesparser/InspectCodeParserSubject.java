package com.stevpet.sonar.plugins.dotnet.resharper.issuesparser;


public class InspectCodeParserSubject extends XmlParserSubject  {
    
    @Override
    public String[] getHierarchy() {
        String[] hierarchy= { "Information","InspectionScope","IssueTypes","Project","Issues"};
        return hierarchy;
    }

}
    
