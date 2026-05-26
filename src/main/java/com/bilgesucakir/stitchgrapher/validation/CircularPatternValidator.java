package com.bilgesucakir.stitchgrapher.validation;

import com.bilgesucakir.stitchgrapher.exception.InvalidPatternException;
import com.bilgesucakir.stitchgrapher.exception.ValidationException;
import com.bilgesucakir.stitchgrapher.parser.OperationType;
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

            ensureNonEmptyRow(row);
            ensureMROrCHFirstRow(row);

            int consumed = 0;
            int produced = 0;

            for (ParsedOperation op : row.operations()) {
                consumed += op.type().getRequiredInput();
                produced += op.type().getProducedOutput();
            }

            if (previousOutput != null) {
                if (!wasPreviousMR(pattern, row) && consumed != previousOutput) {
                    throw new ValidationException(
                            "Row " + row.index() +
                                    " must consume exactly " + previousOutput +
                                    " stitches but requires " + consumed
                    );
                }
            }
            previousOutput = produced;
        }
    }

    private boolean wasPreviousMR(ParsedPattern pattern, ParsedRow row) {
        return pattern.rows()
                .get(row.index() - 1)
                .operations().stream()
                .allMatch(op -> op.type() == OperationType.MR);
    }

    private void ensureMROrCHFirstRow(ParsedRow row) {
       if(row.index() == 0){
           boolean isValid = ((row.operations().size() == 1) && row.operations().get(0).type().equals(OperationType.MR)
                   || row.operations().stream().allMatch(o -> o.type().equals(OperationType.CH)));

           if(!isValid){
               throw new ValidationException("Row 0 can have one magic ring or only chains");
           }
       }
    }

    private void ensureNonEmptyRow(ParsedRow row){
        if(row.operations().isEmpty()){
            throw new ValidationException("Row " + row.index() + " cannot be empty");
        }
    }
}
