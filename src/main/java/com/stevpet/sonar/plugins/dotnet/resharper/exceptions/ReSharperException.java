package com.stevpet.sonar.plugins.dotnet.resharper.exceptions;



public class ReSharperException extends IllegalStateException{

    public ReSharperException(String msg) {
        super(msg);
    }
}
