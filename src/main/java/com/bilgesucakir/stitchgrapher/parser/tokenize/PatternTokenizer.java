package com.bilgesucakir.stitchgrapher.parser.tokenize;

import com.bilgesucakir.stitchgrapher.exception.ParseException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizer for parsing crochet pattern strings into a list of tokens.
 * It recognizes numbers, operations, repeat counts, and special symbols like parentheses and commas.
 */
@Component
public class PatternTokenizer {

    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            // whitespace
            if (Character.isWhitespace(c)) {
                flushCurrentToken(current, tokens);
                continue;
            }

            // special symbols: (, ), ,
            if (c == '(' || c == ')' || c == ',') {
                flushCurrentToken(current, tokens);
                switch (c) {
                    case '(' -> tokens.add(new Token(TokenType.LEFT_PARENTHESIS, "("));
                    case ')' -> tokens.add(new Token(TokenType.RIGHT_PARENTHESIS, ")"));
                    case ',' -> tokens.add(new Token(TokenType.COMMA, ","));
                }
                continue;
            }

            current.append(c);
        }

        flushCurrentToken(current, tokens);
        return tokens;

    }

    private void flushCurrentToken(StringBuilder current, List<Token> tokens) {
        if (current.isEmpty()) {
            return;
        }

        String value = current.toString();
        TokenType type;

        if (value.matches("\\d+")) {
            type = TokenType.NUMBER;
        }
        else if (value.matches("\\d+[a-zA-Z]+")) {
            String numberPart = value.replaceAll("[a-zA-Z]+", "");
            String operationPart = value.replaceAll("\\d+", "");

            tokens.add(new Token(TokenType.NUMBER, numberPart));

            if (!isOperation(operationPart)) {
                throw new ParseException("Unknown operation: " + operationPart);
            }

            tokens.add(new Token(TokenType.OPERATION, operationPart));
            current.setLength(0);
            return;
        }
        else if (value.matches("x\\d+")) {
            type = TokenType.REPEAT;
        }
        else if (isOperation(value)) {
            type = TokenType.OPERATION;
        } else {
            throw new ParseException("Unknown token: " + value);
        }

        tokens.add(new Token(type, value));
        current.setLength(0);
    }

    private boolean isOperation(String value) {
        return switch (value.toLowerCase()) {
            case "sc", "inc", "dec", "hdc", "dc", "htr", "tr", "slst", "ch" -> true;
            default -> false;
        };
    }
}