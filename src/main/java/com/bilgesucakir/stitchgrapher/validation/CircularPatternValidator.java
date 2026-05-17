package com.bilgesucakir.stitchgrapher.validation;

import com.bilgesucakir.stitchgrapher.exception.InvalidPatternException;
import com.bilgesucakir.stitchgrapher.exception.ValidationException;
import com.bilgesucakir.stitchgrapher.parser.ParsedOperation;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.ParsedRow;
import org.springframework.stereotype.Component;

/**
 * Validator for circular crochet patterns.
 */
@Component
public class CircularPatternValidator implements PatternValidator {

    @Override
    public void validate(ParsedPattern pattern) throws InvalidPatternException {

        Integer previousOutput = null;

        for (ParsedRow row : pattern.rows()) {

            int rr = 0;
            int ro = 0;

            for (ParsedOperation op : row.operations()) {
                rr += op.type().getRequiredInput();
                ro += op.type().getProducedOutput();
            }

            if (previousOutput != null && rr != previousOutput) {
                int rowNum = row.index() + 1;
                throw new ValidationException(
                        "Row " + rowNum +
                                " must consume exactly " + previousOutput +
                                " stitches but requires " + rr
                );
            }

            previousOutput = ro;
        }
    }
}
