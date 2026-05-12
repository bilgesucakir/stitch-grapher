package com.bilgesucakir.stitchgrapher.validation;

import com.bilgesucakir.stitchgrapher.exception.InvalidPatternException;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import org.springframework.stereotype.Component;

/**
 * Validator for circular crochet patterns.
 * Currently a no-op; extend this to validate stitch counts and topology for circular patterns.
 */
@Component
public class CirculatPatternValidator implements PatternValidator {

    @Override
    public void validate(ParsedPattern pattern) throws InvalidPatternException {

    }
}
