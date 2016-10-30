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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;

import com.stevpet.sonar.plugins.dotnet.resharper.profiles.CSharpRegularReSharperProfileExporter;
import com.stevpet.sonar.plugins.dotnet.resharper.profiles.CSharpRegularReSharperProfileImporter;
import com.stevpet.sonar.plugins.dotnet.resharper.profiles.ReSharperSonarWayProfileCSharp;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.DefaultMicrosoftWindowsEnvironment;
@Properties({
@Property(key = ReSharperConfiguration.MODE, defaultValue = "", name = "ReSharper activation mode", description = "Possible values : empty (means active), 'skip' and 'reuseReport'.", global = false, project = false, type = PropertyType.SINGLE_SELECT_LIST, options = {
        "skip", "reuseReport" }),

@Property(key = ReSharperConfiguration.INCLUDE_ALL_FILES, defaultValue = "true", name = "ReSharper file inclusion mode", description = "Determines if violations are reported on any file (ignores filters and unsupported file types) or only those supported by the dotNet core plugin.", global = false, project = false, type = PropertyType.BOOLEAN),
@Property(key = ReSharperConfiguration.CUSTOM_SEVERITIES_DEFINITON, defaultValue = "", name = "ReSharper custom severities", description = "Add &lt;String&gt; vales from ReSharper's custom definitions (including &lt:wpf:ResourceDictionary&gt;) A restart is required to take affect.", type = PropertyType.TEXT, global = true, project = false),
@Property(key = ReSharperConfiguration.PROFILE_NAME, defaultValue = ReSharperConfiguration.PROFILE_DEFAULT, name = "Profile", description = "Profile to which rules will be saved on restart, if profile does not exist", type = PropertyType.STRING, global = true, project = false),
@Property(key = ReSharperConfiguration.CUSTOM_SEVERITIES_PATH, name = "Path to custom severities settings", description = "Absolute path to file with exported ReSharper settings: RESHARPER, Manage Options...,Import/Export Settiings, Export to file,CodeInspection", type = PropertyType.STRING, global = true, project = false),

@Property(key=ReSharperConfiguration.BUILD_CONFIGURATION_KEY, name="Configuration",type=PropertyType.STRING,global=true,project=true,defaultValue=ReSharperConfiguration.BUILD_CONFIGURATIONS_DEFVALUE),
@Property(key=ReSharperConfiguration.BUILD_PLATFORM_KEY, name="Platform",type=PropertyType.STRING,global=true,project=true,defaultValue=ReSharperConfiguration.BUILD_PLATFORM_DEFVALUE)
})
public class ReSharperPlugin extends SonarPlugin {
	
	  public static final String LANGUAGE_KEY = "cs";
	  public static final String LANGUAGE_NAME = "C#";
    @SuppressWarnings("rawtypes")
	@Override
    public List getExtensions() {
        List imported=Arrays.asList();
        List exported=Arrays.asList(
                ReSharperConfiguration.class,
                DefaultMicrosoftWindowsEnvironment.class,
                CSharpRegularReSharperProfileExporter.class,
                CSharpRegularReSharperProfileImporter.class,
                ReSharperSonarWayProfileCSharp.class,
                ReSharperRulesDefinition.class,
                InspectCodeBatchData.class,

                DefaultReSharperWorkflow.class,
                ReSharperSensor.class);
        List extensions = new ArrayList();
        extensions.addAll(imported);
        extensions.addAll(exported);
        extensions.addAll(ReSharperConfiguration.getProperties());
        return extensions;
    }

}
