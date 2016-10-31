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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.sonar.api.BatchExtension;
import org.sonar.api.server.rule.RulesDefinition;

public class ReSharperRulesDefinition implements RulesDefinition, BatchExtension {


	@Override
	public void define(Context context) {
		   NewRepository repository = context
				      .createRepository(ReSharperConfiguration.REPOSITORY_KEY + "-" + ReSharperPlugin.LANGUAGE_KEY, ReSharperPlugin.LANGUAGE_KEY)
				      .setName(ReSharperConfiguration.REPOSITORY_NAME);
		    
		  
	        InputStream rulesFileStream = ReSharperRulesDefinition.class.getResourceAsStream(ReSharperConfiguration.DEFAULT_RULES);
	        Reader reader = new InputStreamReader(rulesFileStream);
	        
	        ReSharperRulesAttributes rulesAttributes = new ReSharperRulesAttributes();	        
	        ReSharperRulesAttributeParser ruleAttributeParser = new ReSharperRulesAttributeParser(rulesAttributes);
	        

	       

	        ruleAttributeParser.parse(ReSharperConfiguration.DEFAULT_MAPPING);
	        
	        ReSharperParser parser = new ReSharperNewRepositoryRulesParser(rulesAttributes,repository);
	        parser.parse(reader);
		    repository.done();
	}

}
