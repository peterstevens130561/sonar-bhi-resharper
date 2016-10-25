package com.stevpet.sonar.plugins.dotnet.resharper.exceptions;



/**
 * In general this class should be inherited to give a precise exception. It will be caught at the sensor analyse
 */
public class InspectCodeException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    public InspectCodeException(String msg, Exception inner) {
        super(msg,inner);
    }

    public InspectCodeException(String msg) {
        super(msg);
    }
}
