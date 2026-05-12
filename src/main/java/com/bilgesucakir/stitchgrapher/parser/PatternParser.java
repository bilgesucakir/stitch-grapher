package com.bilgesucakir.stitchgrapher.parser;

import com.bilgesucakir.stitchgrapher.parser.expand.PatternExpander;
import com.bilgesucakir.stitchgrapher.parser.tokenize.PatternTokenizer;
import com.bilgesucakir.stitchgrapher.parser.tokenize.TokenType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Parses a list of stitch pattern rows into a {@link ParsedPattern} consisting of {@link ParsedRow}s.
 * For each non-empty input row the parser tokenizes, expands shorthands and converts operation tokens
 * into ParsedOperation objects while preserving the original row index.
 */
@Component
public class PatternParser {

    private final PatternTokenizer tokenizer;
    private final PatternExpander expander;

    public PatternParser(PatternTokenizer tokenizer, PatternExpander expander) {
        this.tokenizer = tokenizer;
        this.expander = expander;
    }

    public ParsedPattern parse(List<String> rows) {
        List<ParsedRow> parsedRows = IntStream.range(0, rows.size())
            .mapToObj(i -> {
                String row = rows.get(i).trim();
                if (row.isEmpty()) {
                    return null;
                }

                // tokenize -> expand -> filter operation tokens -> map to ParsedOperation
                List<ParsedOperation> operations = expander
                    .expand(tokenizer.tokenize(row))
                    .stream()
                    .filter(t -> t.type() == TokenType.OPERATION)
                    .map(t -> new ParsedOperation(OperationType.from(t.value())))
                    .collect(Collectors.toList());

                return new ParsedRow(i, operations);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return new ParsedPattern(parsedRows);
    }
}