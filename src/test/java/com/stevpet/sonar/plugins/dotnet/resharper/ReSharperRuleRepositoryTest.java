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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.config.Settings;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;

import com.stevpet.sonar.plugins.dotnet.resharper.ReSharperConfiguration;
import com.stevpet.sonar.plugins.dotnet.resharper.ReSharperRuleRepository;

public class ReSharperRuleRepositoryTest {

    @Mock private Settings settings;
    @Before
    public void before() {
        initMocks(this);
    }
    
    @Test
    public void shouldLoadTheDefaultRules() {
        List<Rule> rules = createDefaultRuleRepository();
        
        assertRepositoryIsComplete(rules);
        
    }


    
    @Test
    public void shouldLoadTheCustomRules() {
        when(settings.getString(ReSharperConfiguration.CUSTOM_RULES_PROP_KEY)).thenReturn("bogus");
        
        List<Rule> rules = createDefaultRuleRepository();
        
        assertRepositoryIsComplete(rules);
        
    }
    
    @Test
    public void rulesShouldBeUnique() {
        List<Rule> rules = createDefaultRuleRepository();
        Collection<String> ruleKeys = new ArrayList<>();
        for(Rule rule : rules) {
            assertTrue("key should not be in list" + rule.toString(),!ruleKeys.contains(rule.getKey()));
            ruleKeys.add(rule.getKey());
        }
        assertRepositoryIsComplete(rules);
        
    }

	private List<Rule> createDefaultRuleRepository() {
		RuleRepository ruleRepository = new ReSharperRuleRepository("resharper","cs",settings);
        List<Rule> rules = ruleRepository.createRules();
		return rules;
	}
	
	private void assertRepositoryIsComplete(List<Rule> rules) {
		assertNotNull("expect list of rules",rules);
        assertEquals("rules to be found in defaultrules",810,rules.size());
	}
}
