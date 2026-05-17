package com.bilgesucakir.stitchgrapher.exception;

/**
 * Exception thrown when validation of the crocheting pattern fails.
 * This can occur due to syntax errors, unsupported operations, or logical inconsistencies in the pattern.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
