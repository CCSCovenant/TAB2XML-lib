package com.tab2xml.custom_exceptions;

@SuppressWarnings("serial")
public class InvalidScoreTypeException extends TXMLException {
    public InvalidScoreTypeException(String message) {
        super(message);
    }
}
