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
		assertEquals(1,repository.size());
	}
}
