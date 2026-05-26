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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CircularPatternValidatorTest {

    @InjectMocks
    private final CircularPatternValidator validator =
            new CircularPatternValidator();

    @Test
    void validate_exactConsumption_doesNotThrow() {

        // row0: MR -> no required input,
        ParsedRow row0 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.MR)
        ));

        // row1: consumed = MR allowed, produced=3
        ParsedRow row1 = new ParsedRow(1, List.of(
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC)
        ));

        // row2: 3sc → consumed=3 (exact match)
        // ch consumes no parent
        ParsedRow row2 = new ParsedRow(2, List.of(
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH)
        ));

        ParsedPattern pattern =
                new ParsedPattern(List.of(row0, row1, row2));

        assertDoesNotThrow(() ->
                validator.validate(pattern)
        );
    }

    @Test
    void validate_notAllStitchesConsumed_throws() {

        ParsedRow row0 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.MR)
        ));

        // row1: 3sc → produced=3
        ParsedRow row1 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC)
        ));

        // row2: 2sc → consumed=2 (1 unused → invalid in circular)
        ParsedRow row2 = new ParsedRow(1, List.of(
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC)
        ));

        ParsedPattern pattern =
                new ParsedPattern(List.of(row0, row1, row2));

        assertThrows(ValidationException.class, () ->
                validator.validate(pattern)
        );
    }

    @Test
    void validate_requiresMoreThanAvailable_throws() {

        ParsedRow row0 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.MR)
        ));

        // row1: 3sc → produced=3
        ParsedRow row1 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.SC)
        ));

        // row2: 2dec → consumed=4 (invalid)
        ParsedRow row2 = new ParsedRow(1, List.of(
                new ParsedOperation(OperationType.DEC),
                new ParsedOperation(OperationType.DEC)
        ));

        ParsedPattern pattern =
                new ParsedPattern(List.of(row0, row1, row2));

        assertThrows(ValidationException.class, () ->
                validator.validate(pattern)
        );
    }

    @Test
    void validate_emptyRow_throws(){
        ParsedRow row0 = new ParsedRow(0, List.of());

        ParsedPattern pattern = new ParsedPattern(List.of(row0));

        assertThrows(ValidationException.class, () ->
                validator.validate(pattern)
        );
    }

    @Test
    void validate_NonPureCHFirstRow_throws(){
        ParsedRow row0 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.CH),
                new ParsedOperation(OperationType.SC),
                new ParsedOperation(OperationType.CH)
        ));

        ParsedPattern pattern = new ParsedPattern(List.of(row0));

        assertThrows(ValidationException.class, () ->
                validator.validate(pattern)
        );
    }

    @Test
    void validate_NonMROnlyFirstRow_throws(){
        ParsedRow row0 = new ParsedRow(0, List.of(
                new ParsedOperation(OperationType.SC)
        ));

        ParsedPattern pattern = new ParsedPattern(List.of(row0));

        assertThrows(ValidationException.class, () ->
                validator.validate(pattern)
        );
    }
}