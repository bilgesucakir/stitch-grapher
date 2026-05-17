package com.bilgesucakir.stitchgrapher.validation;

import com.bilgesucakir.stitchgrapher.exception.InvalidPatternException;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;

/**
 * Interface for validating a parsed crocheting pattern.
 * Implementations of this interface will check the structure and content of the ParsedPattern to ensure it adheres to the expected format and rules of crocheting patterns.
 */
public interface PatternValidator {
    void validate(ParsedPattern pattern) throws InvalidPatternException;
}
