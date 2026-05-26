package com.bilgesucakir.stitchgrapher.validation;

import com.bilgesucakir.stitchgrapher.exception.ValidationException;
import com.bilgesucakir.stitchgrapher.parser.OperationType;
import com.bilgesucakir.stitchgrapher.parser.ParsedOperation;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.ParsedRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FlatPatternValidatorTest {

    @InjectMocks
    private final FlatPatternValidator validator =
            new FlatPatternValidator();

    @Test
    void validate_unusedStitchesExist_doesNotThrow() {

        // row1: 3sc → produced=3
        ParsedRow row1 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH)
        ));

        // row2: 2sc → consumed=2 (1 unused → valid)
        ParsedRow row2 = new ParsedRow(1, List.of(
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC)
        ));

        ParsedPattern pattern =
                new ParsedPattern(List.of(row1, row2));

        assertDoesNotThrow(() ->
                validator.validate(pattern)
        );
    }

    @Test
    void validate_rowRequiresExactlyPreviousOutput_doesNotThrow() {

        // row1: 3sc → produced=3
        ParsedRow row1 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH)
        ));

        // row2: 3sc → consumed=3 (exactly matches previous output → valid)
        //ch consumes no parent
        ParsedRow row2 = new ParsedRow(1, List.of(
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH)
        ));

        ParsedPattern pattern =
                new ParsedPattern(List.of(row1, row2));

        assertDoesNotThrow(() ->
                validator.validate(pattern)
        );
    }

    @Test
    void validate_rowRequiresMoreThanPreviousOutput_throws() {

        // row1: 3sc → produced=3
        ParsedRow row1 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH)
        ));

        // row2: 2dec → consumed=4 (needs 4 > 3 → invalid)
        ParsedRow row2 = new ParsedRow(1, List.of(
                new ParsedOperation(OperationType.DEC),
                new ParsedOperation(OperationType.DEC)
        ));

        ParsedPattern pattern =
                new ParsedPattern(List.of(row1, row2));

        assertThrowsExactly(ValidationException.class, () ->
                validator.validate(pattern),
                "Row 1 requires 4 stitches but previous row produced only 3"
        );
    }

    @Test
    void validate_nonChainFirstRow_throws(){
        ParsedRow row1 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.CH)
        ));

        ParsedPattern pattern = new ParsedPattern(List.of(row1));

        assertThrowsExactly(ValidationException.class,
                () -> validator.validate(pattern),
                "Row 0 must contain only chains");
    }

    @Test
    void validate_emptyRow_throws(){
        ParsedRow row1 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH)
        ));

        ParsedRow row2 = new ParsedRow(1, List.of());

        ParsedPattern pattern = new ParsedPattern(List.of(row1, row2));

        assertThrowsExactly(ValidationException.class,
                () -> validator.validate(pattern),
                "Row 1 cannot be empty");
    }

    @Test
    void validate_hasMagicRing_throws(){
        ParsedRow row1 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH)
        ));

        ParsedRow row2 = new ParsedRow(1, List.of(
                new ParsedOperation(OperationType.MR),
                new ParsedOperation(OperationType.DEC)
        ));

        ParsedPattern pattern = new ParsedPattern(List.of(row1, row2));

        assertThrowsExactly(ValidationException.class, () ->
                        validator.validate(pattern),
                "Magic rings are not allowed in flat patterns"
        );
    }

    @Test
    void validate_nonFirstRowDoesNotConsumeAnyFromPrevious_throws(){
        ParsedRow row1 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH)
        ));
        ParsedRow row2 = new ParsedRow(1, List.of(
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC)
        ));
        ParsedRow row3 = new ParsedRow(2, List.of(
                new ParsedOperation(OperationType.CH)
        ));

        ParsedPattern pattern = new ParsedPattern(List.of(row1, row2, row3));

        assertThrowsExactly(ValidationException.class, () -> validator.validate(pattern),
                "Row 2 must consume at least 1 stitch from previous row. Chain-only rows must be appended to the previous row."
        );
    }
}