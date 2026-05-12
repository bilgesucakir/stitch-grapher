package com.bilgesucakir.stitchgrapher.validation;

import com.bilgesucakir.stitchgrapher.exception.InvalidPatternException;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import org.springframework.stereotype.Component;

/**
 * Validator for flat (non-circular) crochet patterns.
 * Currently a no-op; update this class to perform structural checks specific to flat patterns.
 */
@Component
public class FlatPatternValidator implements PatternValidator{

    @Override
    public void validate(ParsedPattern pattern) throws InvalidPatternException {

    }
}
