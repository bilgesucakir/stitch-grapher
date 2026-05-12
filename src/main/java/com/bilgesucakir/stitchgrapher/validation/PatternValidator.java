package com.bilgesucakir.stitchgrapher.validation;

import com.bilgesucakir.stitchgrapher.exception.InvalidPatternException;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;

public interface PatternValidator {
    void validate(ParsedPattern pattern) throws InvalidPatternException;
}
