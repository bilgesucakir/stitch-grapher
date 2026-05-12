package com.bilgesucakir.stitchgrapher.parser.expand;

import com.bilgesucakir.stitchgrapher.exception.ParseException;
import com.bilgesucakir.stitchgrapher.parser.tokenize.Token;
import com.bilgesucakir.stitchgrapher.parser.tokenize.TokenType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatternExpander {

    public List<Token> expand(List<Token> tokens) {
        List<Token> expanded = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            // group: ( ... ) xN
            if (token.type() == TokenType.LEFT_PARENTHESIS) {
                List<Token> group = new ArrayList<>();
                i++;

                while (i < tokens.size() && tokens.get(i).type() != TokenType.RIGHT_PARENTHESIS) {
                    Token current = tokens.get(i);
                    if (current.type() != TokenType.COMMA) {
                        group.add(current);
                    }
                    i++;
                }

                if (i >= tokens.size()) {
                    throw new ParseException("Unclosed group");
                }

                // expect xN after ')'
                if (i + 1 >= tokens.size() || tokens.get(i + 1).type() != TokenType.REPEAT) {
                    throw new ParseException("Expected repeat after group");
                }

                Token repeatToken = tokens.get(++i);
                int repeatCount = Integer.parseInt(repeatToken.value().substring(1));

                List<Token> expandedGroup = expandNumericPrefixes(group);
                for (int r = 0; r < repeatCount; r++) {
                    expanded.addAll(expandedGroup);
                }

                continue;
            }

            // ignore commas outside groups
            if (token.type() == TokenType.COMMA) {
                continue;
            }

            expanded.add(token);
        }

        return expandNumericPrefixes(expanded);
    }

    private List<Token> expandNumericPrefixes(List<Token> tokens) {

        List<Token> expanded = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            //separate number token preceding an operation
            if (token.type() == TokenType.NUMBER) {
                if (i + 1 >= tokens.size()) {
                    throw new ParseException("Number without operation");
                }

                Token operation = tokens.get(++i);
                if (operation.type() != TokenType.OPERATION) {
                    throw new ParseException("Expected operation after number");
                }

                int count;
                try {
                    count = Integer.parseInt(token.value());
                } catch (NumberFormatException ex) {
                    throw new ParseException("Invalid number: " + token.value());
                }

                for (int j = 0; j < count; j++) {
                    expanded.add(operation);
                }

                continue;
            }

            expanded.add(token);
        }

        return expanded;
    }
}