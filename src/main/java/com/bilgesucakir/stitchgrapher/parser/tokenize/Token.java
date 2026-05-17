package com.bilgesucakir.stitchgrapher.parser.tokenize;

/**
 * Represents a token produced by the tokenizer during the parsing of a crocheting pattern.
 * Each token has a type (e.g., stitch, number, symbol) and a value (the actual text).
 */
public record Token(TokenType type, String value) {
}
