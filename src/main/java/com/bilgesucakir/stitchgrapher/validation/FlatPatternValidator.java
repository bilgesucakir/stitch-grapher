package com.bilgesucakir.stitchgrapher.validation;
import com.bilgesucakir.stitchgrapher.exception.ValidationException;
import com.bilgesucakir.stitchgrapher.parser.OperationType;
import com.bilgesucakir.stitchgrapher.parser.ParsedOperation;

import com.bilgesucakir.stitchgrapher.exception.InvalidPatternException;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.ParsedRow;
import org.springframework.stereotype.Component;

/**
 * Validator for flat (non-circular) crochet patterns.
 */
@Component
public class FlatPatternValidator implements PatternValidator{

    @Override
    public void validate(ParsedPattern pattern) throws InvalidPatternException {

        Integer previousOutput = null;

        for (ParsedRow row : pattern.rows()) {

            // empty row check
            ensureNonEmptyRow(row);

            // first row must be chain only
            // later rows cannot be chain only
            ensureChainOnlyFirstRowAndNonChainOnlyLaterRows(row);


            // magic ring operation check
            ensureNonMROperationType(row);

            int consumed = 0;
            int produced = 0;
            for (ParsedOperation op : row.operations()) {
                consumed += op.type().getRequiredInput();
                produced += op.type().getProducedOutput();
            }

            if (previousOutput != null && consumed > previousOutput) {
                throw new ValidationException(
                        "Row " + row.index() +
                                " requires " + consumed +
                                " stitches but previous row produced only " + previousOutput
                );
            }

            previousOutput = produced;
        }
    }

    private void ensureChainOnlyFirstRowAndNonChainOnlyLaterRows(ParsedRow row) {
        if(row.index() == 0 && row.operations().stream().anyMatch(o-> !o.type().equals(OperationType.CH))){
            throw new ValidationException("Row 0 should contain chains only");
        }
        if(row.index() != 0 && row.operations().stream().allMatch(o-> o.type().equals(OperationType.CH))){
            throw new ValidationException("Row " + row.index() + " must consume at least 1 stitch from previous row. " +
                    "Chain-only rows must be appended to the previous row.");
        }
    }

    private void ensureNonEmptyRow(ParsedRow row){
        if(row.operations().isEmpty()){
            throw new ValidationException("Row " + row.index() + " cannot be empty");
        }
    }

    private void ensureNonMROperationType(ParsedRow row){
        if(row.operations().stream().anyMatch(o -> o.type().equals(OperationType.MR))){
            throw new ValidationException("Magic rings are not allowed in flat patterns");
        }
    }
}
