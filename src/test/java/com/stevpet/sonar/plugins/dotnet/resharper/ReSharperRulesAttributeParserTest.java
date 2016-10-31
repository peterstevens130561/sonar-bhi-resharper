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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReSharperRulesAttributeParserTest {

	private ReSharperRulesAttributes repository = new ReSharperRulesAttributes();
	private ReSharperRulesAttributeParser parser = new ReSharperRulesAttributeParser(repository);

	
	@Test
	public void readMapping ()
	{
		parser.parse(ReSharperConfiguration.DEFAULT_MAPPING);
		assertEquals(4,repository.size());
	}
}
