package com.bilgesucakir.stitchgrapher.parser;

import lombok.Getter;

/**
 * Represents a parsed operation from the crocheting pattern.
 * It contains the type of operation (e.g., single crochet, increase, decrease).
 */
public record ParsedOperation(OperationType type) {
}
