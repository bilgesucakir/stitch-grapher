package com.bilgesucakir.stitchgrapher.exception;

/*
 * Custom exception to indicate invalid stitch patterns.
 * This can be thrown when the input pattern string cannot be parsed correctly,
 * or when it contains unsupported stitch types or syntax errors.
 */
public class InvalidPatternException extends RuntimeException {
    public InvalidPatternException(String message) {
        super(message);
    }
}
