package net.aohayou.collector.data.formula;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.aohayou.collector.data.formula.FormulaConverter.Tokenizer;
import static net.aohayou.collector.data.formula.FormulaConverter.Tokenizer.Token;

import static org.junit.Assert.*;

public class FormulaConverterTest {

    @Test
    public void tokenizer_single_number() throws Exception {
        List<Token> tokens = Tokenizer.tokenize("123");
        Token[] expected = {
                new Tokenizer.NumberToken(123)
        };
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void tokenizer_simple_expression() throws Exception {
        List<Token> tokens = Tokenizer.tokenize("1+2*4-3");
        Token[] expected = expectedTokensSimpleExpression();
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void tokenizer_whitespace_simple_expression() throws Exception {
        List<Token> tokens = Tokenizer.tokenize(" 1 +2  * 4 -3 ");
        Token[] expected = expectedTokensSimpleExpression();
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test(expected = InvalidFormulaException.class)
    public void tokenizer_unknown_character() throws Exception {
        Tokenizer.tokenize("1+2*a4-3");
    }

    private Token[] expectedTokensSimpleExpression() {
        Token[] expected = {
                new Tokenizer.NumberToken(1),
                new Tokenizer.AddToken(),
                new Tokenizer.NumberToken(2),
                new Tokenizer.UntilToken(),
                new Tokenizer.NumberToken(4),
                new Tokenizer.RemoveToken(),
                new Tokenizer.NumberToken(3),
        };
        return expected;
    }

    private FormulaConverter.Parser.Node expectedNodesSimpleExpression() {
        return new FormulaConverter.Parser.RemoveOperator(
                new FormulaConverter.Parser.AddOperator(
                        new FormulaConverter.Parser.NumberNode(1),
                        new FormulaConverter.Parser.UntilOperator(
                                new FormulaConverter.Parser.NumberNode(2),
                                new FormulaConverter.Parser.NumberNode(4)
                        )
                ),
                new FormulaConverter.Parser.NumberNode(3)
        );
    }

    @Test
    public void parser_single_number() throws Exception {
        Token token = new Tokenizer.NumberToken(123);
        List<Tokenizer.Token> tokens = Collections.singletonList(token);
        FormulaConverter.Parser parser = new FormulaConverter.Parser(tokens);
        FormulaConverter.Parser.Node result = parser.parse();
        assertEquals(result, new FormulaConverter.Parser.NumberNode(123));
    }

    @Test
    public void parser_simple_expression() throws Exception {
        List<Tokenizer.Token> tokens = Arrays.asList(expectedTokensSimpleExpression());
        FormulaConverter.Parser parser = new FormulaConverter.Parser(tokens);
        FormulaConverter.Parser.Node result = parser.parse();
        assertEquals(result, expectedNodesSimpleExpression());
    }

    @Test (expected = InvalidFormulaException.class)
    public void parser_multiple_until() throws Exception {
        List<Tokenizer.Token> tokens = new ArrayList<>();
        tokens.add(new Tokenizer.NumberToken(1));
        tokens.add(new Tokenizer.UntilToken());
        tokens.add(new Tokenizer.NumberToken(2));
        tokens.add(new Tokenizer.UntilToken());
        tokens.add(new Tokenizer.NumberToken(3));

        FormulaConverter.Parser parser = new FormulaConverter.Parser(tokens);
        parser.parse();
    }

    @Test (expected = InvalidFormulaException.class)
    public void parser_unfinished_range_operation() throws Exception {
        List<Tokenizer.Token> tokens = new ArrayList<>();
        tokens.add(new Tokenizer.NumberToken(1));
        tokens.add(new Tokenizer.AddToken());

        FormulaConverter.Parser parser = new FormulaConverter.Parser(tokens);
        parser.parse();
    }

    @Test (expected = InvalidFormulaException.class)
    public void parser_too_many_numbers() throws Exception {
        List<Tokenizer.Token> tokens = new ArrayList<>();
        tokens.add(new Tokenizer.NumberToken(1));
        tokens.add(new Tokenizer.NumberToken(2));

        FormulaConverter.Parser parser = new FormulaConverter.Parser(tokens);
        parser.parse();
    }

    @Test (expected = InvalidFormulaException.class)
    public void parser_not_enough_numbers() throws Exception {
        List<Tokenizer.Token> tokens = new ArrayList<>();
        tokens.add(new Tokenizer.AddToken());

        FormulaConverter.Parser parser = new FormulaConverter.Parser(tokens);
        parser.parse();
    }
}
