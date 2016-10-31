package com.stevpet.sonar.plugins.dotnet.resharper;

import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.debt.DebtRemediationFunction;
import org.sonar.api.utils.Duration;
import org.w3c.dom.Element;

public  class DebtRemediationFunctionReader {
	public static final String FUNCTION_ATTRIBUTE = "DebtRemediationFunction";
	public static final String COEFFICIENT_ATTRIBUTE = "DebtRemediationCoefficient";
	public static final String OFFSET_ATTRIBUTE = "DebtRemediationOffset";


	static DebtRemediationFunction read(Element ruleElement) {
		DebtRemediationFunction result=null;
		String functionName=ruleElement.getAttribute(FUNCTION_ATTRIBUTE) ;
		if(StringUtils.isEmpty(functionName)) {
			return result;
		}
		DebtRemediationFunction.Type functionType=DebtRemediationFunction.Type.valueOf(functionName);
		Duration coefficient = Duration.decode(ruleElement.getAttribute(COEFFICIENT_ATTRIBUTE), 24);
		Duration offset = Duration.decode(ruleElement.getAttribute(OFFSET_ATTRIBUTE), 24);
		
		result=DebtRemediationFunction.create(functionType, coefficient, offset);

		return result;
	}
}
