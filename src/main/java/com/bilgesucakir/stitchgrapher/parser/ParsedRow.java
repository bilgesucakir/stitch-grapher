package com.bilgesucakir.stitchgrapher.parser;

import lombok.Getter;

import java.util.List;

/**
 * Represents a parsed row of stitches from the input pattern.
 * Each ParsedRow has an index (the row number) and a list of ParsedOperation objects that represent the individual stitch operations in that row.
 */
public record ParsedRow(int index, List<ParsedOperation> operations) {
}