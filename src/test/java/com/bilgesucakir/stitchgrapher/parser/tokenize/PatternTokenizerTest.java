package com.bilgesucakir.stitchgrapher.parser.tokenize;

import com.bilgesucakir.stitchgrapher.exception.ParseException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for PatternTokenizer.
 */
public class PatternTokenizerTest {

    private final PatternTokenizer tokenizer = new PatternTokenizer();

    @Test
    void tokenize_negativeNumber_throwsParseException() {
        ParseException ex = assertThrows(ParseException.class, () -> tokenizer.tokenize("-3"));
        assertEquals("Unknown token: -3", ex.getMessage());
    }

    @Test
    void tokenize_simpleOperations_returnsOperationTokens() {
        List<Token> tokens = tokenizer.tokenize("sc hdc dc");
        List<String> values = tokens.stream().map(Token::value).collect(Collectors.toList());
        List<TokenType> types = tokens.stream().map(Token::type).collect(Collectors.toList());

        assertEquals(List.of(TokenType.OPERATION, TokenType.OPERATION, TokenType.OPERATION), types);
        assertEquals(List.of("sc", "hdc", "dc"), values);
    }

    @Test
    void tokenize_parenthesesAndRepeat_parsesCorrectly() {
        List<Token> tokens = tokenizer.tokenize("(sc, ch) x3");
        List<TokenType> types = tokens.stream().map(Token::type).collect(Collectors.toList());
        List<String> values = tokens.stream().map(Token::value).collect(Collectors.toList());

        assertEquals(List.of(TokenType.LEFT_PARENTHESIS, TokenType.OPERATION, TokenType.COMMA, TokenType.OPERATION, TokenType.RIGHT_PARENTHESIS, TokenType.REPEAT), types);
        assertEquals(List.of("(", "sc", ",", "ch", ")", "x3"), values);
    }

    @Test
    void tokenize_combinedNumberAndOperation_splitsNumberAndOperation() {
        List<Token> tokens = tokenizer.tokenize("20sc");
        List<TokenType> types = tokens.stream().map(Token::type).collect(Collectors.toList());
        List<String> values = tokens.stream().map(Token::value).collect(Collectors.toList());

        assertEquals(List.of(TokenType.NUMBER, TokenType.OPERATION), types);
        assertEquals(List.of("20", "sc"), values);
    }

    @Test
    void tokenize_separateNumberAndOperation_parsesNumberThenOperation() {
        List<Token> tokens = tokenizer.tokenize("20 sc");
        List<TokenType> types = tokens.stream().map(Token::type).collect(Collectors.toList());
        List<String> values = tokens.stream().map(Token::value).collect(Collectors.toList());

        assertEquals(List.of(TokenType.NUMBER, TokenType.OPERATION), types);
        assertEquals(List.of("20", "sc"), values);
    }

    @Test
    void tokenize_repeatToken_parsesRepeat() {
        List<Token> tokens = tokenizer.tokenize("x3");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.REPEAT, tokens.get(0).type());
        assertEquals("x3", tokens.get(0).value());
    }

    @Test
    void tokenize_scCommaDc_parsesOperationCommaOperation() {
        List<Token> tokens = tokenizer.tokenize("sc, dc");
        List<TokenType> types = tokens.stream().map(Token::type).collect(Collectors.toList());
        List<String> values = tokens.stream().map(Token::value).collect(Collectors.toList());

        assertEquals(List.of(TokenType.OPERATION, TokenType.COMMA, TokenType.OPERATION), types);
        assertEquals(List.of("sc", ",", "dc"), values);
    }

    @Test
    void tokenize_unknownToken_throwsParseException() {
        ParseException ex = assertThrows(ParseException.class, () -> tokenizer.tokenize("foo"));
        assertEquals("Unknown token: foo", ex.getMessage());
    }

    @Test
    void tokenize_emptyInput_returnsEmptyList() {
        List<Token> tokens = tokenizer.tokenize("");
        assertEquals(0, tokens.size());
    }

    @Test
    void tokenize_whitespaceHandling_trimsAndTokensCorrectly() {
        List<Token> tokens = tokenizer.tokenize("  sc   dc  ");
        List<String> values = tokens.stream().map(Token::value).collect(Collectors.toList());
        assertEquals(List.of("sc", "dc"), values);
    }
}
