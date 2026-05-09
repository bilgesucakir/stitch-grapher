package com.bilgesucakir.stitchgrapher.parser;

import lombok.Getter;
import java.util.List;

/**
 * Represents a parsed pattern, which consists of a list of parsed rows.
 * Each parsed row contains the stitches and their connections for that row in the pattern.
 */
@Getter
public class ParsedPattern {
    private final List<ParsedRow> rows;

    public ParsedPattern(List<ParsedRow> rows) {
        this.rows = rows;
    }
}
