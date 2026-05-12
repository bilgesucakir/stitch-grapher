package com.bilgesucakir.stitchgrapher.exception;

/**
 * Custom exception class for handling parsing errors in the stitch pattern parser.
 */
public class ParseException extends RuntimeException{
    public ParseException(String message) {
        super(message);
    }
}
