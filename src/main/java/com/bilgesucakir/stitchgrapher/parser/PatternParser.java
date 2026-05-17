package com.bilgesucakir.stitchgrapher.parser;

import com.bilgesucakir.stitchgrapher.parser.expand.PatternExpander;
import com.bilgesucakir.stitchgrapher.parser.tokenize.PatternTokenizer;
import com.bilgesucakir.stitchgrapher.parser.tokenize.Token;
import com.bilgesucakir.stitchgrapher.parser.tokenize.TokenType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class PatternParser {

    private final PatternTokenizer tokenizer;
    private final PatternExpander expander;

    public PatternParser(PatternTokenizer tokenizer, PatternExpander expander) {
        this.tokenizer = tokenizer;
        this.expander = expander;
    }

    public ParsedPattern parse(List<String> rows) {

        if (rows == null) {
            return new ParsedPattern(List.of());
        }

        List<ParsedRow> parsedRows = IntStream.range(0, rows.size())
                .filter(i -> !rows.get(i).trim().isEmpty())
                .mapToObj(i -> {
                    String row = rows.get(i).trim();

                    List<ParsedOperation> operations = expander
                            .expand(tokenizer.tokenize(row))
                            .stream()
                            .filter(t -> t.type() == TokenType.OPERATION)
                            .map(this::toOperation)
                            .toList();

                    return new ParsedRow(i, operations);
                })
                .toList();

        return new ParsedPattern(parsedRows);
    }

    private ParsedOperation toOperation(Token token) {
        return new ParsedOperation(OperationType.from(token.value()));
    }
}