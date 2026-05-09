package com.bilgesucakir.stitchgrapher.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses the input pattern from a list of strings into a structured format.
 * Each string represents a row of stitches, and each token in the string represents a stitch operation.
 * The parser converts these tokens into a list of ParsedRow objects, which contain the row index and the list of operations for that row.
 */
public class PatternParser {

    //TODO: consider inputs such as (inc, sc)x6 for a line. So there can exist other tokens than operation type.
    public ParsedPattern parse(List<String> rows) {
        List<ParsedRow> parsedRows = new ArrayList<>();
        for (int i=0; i<rows.size(); i++) {
            String row = rows.get(i).trim();
            if (row.isEmpty()) {
                continue;
            }

            String[] tokens = row.split("\\s+");
            List<ParsedOperation> operations = new ArrayList<>();
            for (String token : tokens) {

                OperationType type = parseToken(token);
                operations.add(new ParsedOperation(type));
            }

            parsedRows.add(new ParsedRow(i, operations));
        }

        return new ParsedPattern(parsedRows);
    }

    private OperationType parseToken(String token) {
        return switch (token.toLowerCase()) {
            case "sc" -> OperationType.SC;
            case "hdc" -> OperationType.HDC;
            case "dc" -> OperationType.DC;
            case "htr" -> OperationType.HTR;
            case "tr" -> OperationType.TR;
            case "slst" -> OperationType.SLST;

            case "inc" -> OperationType.INC;
            case "dec" -> OperationType.DEC;

            default -> throw new ParseException("Unknown token: " + token);
        };
    }
}