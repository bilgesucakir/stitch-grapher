package com.bilgesucakir.stitchgrapher.parser;

import com.bilgesucakir.stitchgrapher.exception.ParseException;
import com.bilgesucakir.stitchgrapher.parser.expand.PatternExpander;
import com.bilgesucakir.stitchgrapher.parser.tokenize.PatternTokenizer;
import com.bilgesucakir.stitchgrapher.parser.tokenize.Token;
import com.bilgesucakir.stitchgrapher.parser.tokenize.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatternParserTest {

    @Mock
    private PatternTokenizer tokenizer;

    @Mock
    private PatternExpander expander;

    @InjectMocks
    private PatternParser parser;

    @Test
    void parse_withMockedTokenizerAndExpander_mapsOperations() {
        String row = "sc hdc";

        // tokenizer returns tokens (could be anything since expander is mocked below)
        when(tokenizer.tokenize(row)).thenReturn(List.of(
                new Token(TokenType.OPERATION, "sc"),
                new Token(TokenType.OPERATION, "hdc")
        ));

        // expander returns the same list (simulating no expansion)
        when(expander.expand(anyList())).thenAnswer(inv -> inv.getArgument(0));

        ParsedPattern parsed = parser.parse(List.of(row));

        assertEquals(1, parsed.getRows().size());
        ParsedRow parsedRow = parsed.getRows().get(0);
        assertEquals(0, parsedRow.getIndex());
        assertEquals(2, parsedRow.getOperations().size());
        assertEquals(OperationType.SC, parsedRow.getOperations().get(0).getType());
        assertEquals(OperationType.HDC, parsedRow.getOperations().get(1).getType());
    }

    @Test
    void parse_multipleRows_preservesIndicesAndOperations() {
        String row1 = "sc hdc";
        String row2 = "dc";

        when(tokenizer.tokenize(row1)).thenReturn(List.of(
                new Token(TokenType.OPERATION, "sc"),
                new Token(TokenType.OPERATION, "hdc")
        ));
        when(tokenizer.tokenize(row2)).thenReturn(List.of(
                new Token(TokenType.OPERATION, "dc")
        ));

        when(expander.expand(anyList())).thenAnswer(inv -> inv.getArgument(0));

        ParsedPattern parsed = parser.parse(List.of(row1, row2));

        assertEquals(2, parsed.getRows().size());
        assertEquals(0, parsed.getRows().get(0).getIndex());
        assertEquals(1, parsed.getRows().get(1).getIndex());
        assertEquals(OperationType.SC, parsed.getRows().get(0).getOperations().get(0).getType());
        assertEquals(OperationType.HDC, parsed.getRows().get(0).getOperations().get(1).getType());
        assertEquals(OperationType.DC, parsed.getRows().get(1).getOperations().get(0).getType());
    }

    @Test
    void parse_skipsEmptyRows_preservesOriginalRowIndices() {
        String r0 = "sc";
        String r1 = ""; // empty row should be skipped
        String r2 = "dc";

        when(tokenizer.tokenize(r0)).thenReturn(List.of(new Token(TokenType.OPERATION, "sc")));
        when(tokenizer.tokenize(r2)).thenReturn(List.of(new Token(TokenType.OPERATION, "dc")));
        when(expander.expand(anyList())).thenAnswer(inv -> inv.getArgument(0));

        ParsedPattern parsed = parser.parse(List.of(r0, r1, r2));

        assertEquals(2, parsed.getRows().size());
        // first parsed row should have original index 0, second parsed row index 2
        assertEquals(0, parsed.getRows().get(0).getIndex());
        assertEquals(2, parsed.getRows().get(1).getIndex());
    }

    @Test
    void parse_unknownOperation_throwsParseException() {
        String row = "foo";
        when(tokenizer.tokenize(row)).thenReturn(List.of(new Token(TokenType.OPERATION, "unknown")));
        when(expander.expand(anyList())).thenAnswer(inv -> inv.getArgument(0));

        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(List.of(row)));
        assertEquals("Unknown token: unknown", ex.getMessage());
    }
}
