package com.bilgesucakir.stitchgrapher.parser.expand;

import com.bilgesucakir.stitchgrapher.exception.ParseException;
import com.bilgesucakir.stitchgrapher.parser.tokenize.Token;
import com.bilgesucakir.stitchgrapher.parser.tokenize.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test scaffold for PatternExpander.
 * One test implemented as requested; add more tests as needed.
 */
@ExtendWith(MockitoExtension.class)
public class PatternExpanderTest {

    @InjectMocks
    private PatternExpander expander;


    @Test
    void expand_groupRepeat_repeatsGroupCorrectly() {

        List<Token> input = List.of(
                new Token(TokenType.LEFT_PARENTHESIS, "("),
                new Token(TokenType.OPERATION, "sc"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.OPERATION, "dc"),
                new Token(TokenType.RIGHT_PARENTHESIS, ")"),
                new Token(TokenType.REPEAT, "x2")
        );

        List<Token> expanded = expander.expand(input);

        List<String> ops = expanded.stream()
                .filter(t -> t.type() == TokenType.OPERATION)
                .map(Token::value)
                .collect(Collectors.toList());

        assertEquals(List.of("sc", "dc", "sc", "dc"), ops);
    }

    @Test
    void expand_commaSeparatedOperations_ignoresCommas() {
        List<Token> input = List.of(
                new Token(TokenType.OPERATION, "sc"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.OPERATION, "dc"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.OPERATION, "hdc")
        );

        List<Token> expanded = expander.expand(input);

        List<String> ops = expanded.stream()
                .filter(t -> t.type() == TokenType.OPERATION)
                .map(Token::value)
                .collect(Collectors.toList());

        assertEquals(List.of("sc", "dc", "hdc"), ops);
    }

    @Test
    void expand_numericPrefix_repeatsOperation() {
        List<Token> input = List.of(
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.OPERATION, "sc")
        );

        List<Token> expanded = expander.expand(input);

        List<String> ops = expanded.stream()
                .filter(t -> t.type() == TokenType.OPERATION)
                .map(Token::value)
                .collect(Collectors.toList());

        assertEquals(List.of("sc", "sc", "sc"), ops);
    }

    @Test
    void expand_groupCommaMultiplierCombined_expandsCorrectly() {
        List<Token> input = List.of(
                new Token(TokenType.LEFT_PARENTHESIS, "("),
                new Token(TokenType.OPERATION, "sc"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.OPERATION, "dc"),
                new Token(TokenType.RIGHT_PARENTHESIS, ")"),
                new Token(TokenType.REPEAT, "x2"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.OPERATION, "hdc"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.OPERATION, "sc")
        );

        List<Token> expanded = expander.expand(input);

        List<String> ops = expanded.stream()
                .filter(t -> t.type() == TokenType.OPERATION)
                .map(Token::value)
                .collect(Collectors.toList());

        assertEquals(List.of("sc", "dc", "sc", "dc", "hdc", "hdc", "hdc", "sc"), ops);
    }

    @Test
    void expand_unclosedGroup_throwsParseException() {
        List<Token> input = List.of(
                new Token(TokenType.LEFT_PARENTHESIS, "("),
                new Token(TokenType.OPERATION, "sc")
                // missing right parenthesis
        );

        ParseException ex = assertThrows(ParseException.class, () -> expander.expand(input));
        assertEquals("Unclosed group", ex.getMessage());
    }

    @Test
    void expand_groupWithoutRepeat_throwsParseException() {
        List<Token> input = List.of(
                new Token(TokenType.LEFT_PARENTHESIS, "("),
                new Token(TokenType.OPERATION, "sc"),
                new Token(TokenType.RIGHT_PARENTHESIS, ")")
                // missing repeat token
        );

        ParseException ex = assertThrows(ParseException.class, () -> expander.expand(input));
        assertEquals("Expected repeat after group", ex.getMessage());
    }

    // --- New tests added below ---

    @Test
    void expand_numberWithoutOperation_throwsParseException() {
        List<Token> input = List.of(
                new Token(TokenType.NUMBER, "2")
        );

        ParseException ex = assertThrows(ParseException.class, () -> expander.expand(input));
        assertEquals("Number without operation", ex.getMessage());
    }

    @Test
    void expand_zeroRepeat_producesNoOperations() {
        List<Token> input = List.of(
                new Token(TokenType.LEFT_PARENTHESIS, "("),
                new Token(TokenType.OPERATION, "sc"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.OPERATION, "ch"),
                new Token(TokenType.RIGHT_PARENTHESIS, ")"),
                new Token(TokenType.REPEAT, "x0")
        );

        List<Token> expanded = expander.expand(input);

        List<String> ops = expanded.stream()
                .filter(t -> t.type() == TokenType.OPERATION)
                .map(Token::value)
                .collect(Collectors.toList());

        assertEquals(List.of(), ops);
    }

}
