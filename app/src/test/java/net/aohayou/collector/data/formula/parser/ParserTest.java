package net.aohayou.collector.data.formula.parser;

import net.aohayou.collector.data.formula.InvalidFormulaException;
import net.aohayou.collector.data.formula.tokenizer.AddToken;
import net.aohayou.collector.data.formula.tokenizer.NumberToken;
import net.aohayou.collector.data.formula.tokenizer.Token;
import net.aohayou.collector.data.formula.tokenizer.UntilToken;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.aohayou.collector.data.formula.FormulaConverterTestConstants.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void parser_single_number() throws Exception {
        Token token = new NumberToken(123);
        List<Token> tokens = Collections.singletonList(token);
        Parser parser = new Parser(tokens);
        Node result = parser.parse();
        assertEquals(result, new NumberNode(123));
    }

    @Test
    public void parser_simple_formula() throws Exception {
        List<Token> tokens = Arrays.asList(expectedTokensSimpleFormula());
        Parser parser = new Parser(tokens);
        Node result = parser.parse();
        assertEquals(result, expectedNodesSimpleFormula());
    }

    @Test
    public void parser_empty_formula() throws Exception {
        List<Token> tokens = new ArrayList<>();
        Parser parser = new Parser(tokens);
        Node result = parser.parse();
        assertEquals(result, null);
    }

    @Test (expected = InvalidFormulaException.class)
    public void parser_multiple_until() throws Exception {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(1));
        tokens.add(new UntilToken());
        tokens.add(new NumberToken(2));
        tokens.add(new UntilToken());
        tokens.add(new NumberToken(3));

        Parser parser = new Parser(tokens);
        parser.parse();
    }

    @Test (expected = InvalidFormulaException.class)
    public void parser_unfinished_range_operation() throws Exception {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(1));
        tokens.add(new AddToken());

        Parser parser = new Parser(tokens);
        parser.parse();
    }

    @Test (expected = InvalidFormulaException.class)
    public void parser_too_many_numbers() throws Exception {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(1));
        tokens.add(new NumberToken(2));

        Parser parser = new Parser(tokens);
        parser.parse();
    }

    @Test (expected = InvalidFormulaException.class)
    public void parser_not_enough_numbers() throws Exception {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new AddToken());

        Parser parser = new Parser(tokens);
        parser.parse();
    }
}
