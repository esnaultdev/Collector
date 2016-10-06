package net.aohayou.collector.data.formula;

import org.junit.Test;

import java.util.Arrays;
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
}
