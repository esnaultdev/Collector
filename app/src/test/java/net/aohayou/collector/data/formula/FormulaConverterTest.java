package net.aohayou.collector.data.formula;

import org.junit.Test;

import java.util.List;

import static net.aohayou.collector.data.formula.FormulaConverter.Tokenizer;
import static net.aohayou.collector.data.formula.FormulaConverter.Tokenizer.Token;

import static org.junit.Assert.*;

public class FormulaConverterTest {

    @Test
    public void tokenizer_single_number() throws Exception {
        List<Token> tokens = Tokenizer.tokenize("123");
        Token[] expected = {
                new Token(Token.Type.NUMBER_LITERAL, "123")
        };
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void tokenizer_simple_expression() throws Exception {
        List<Token> tokens = Tokenizer.tokenize("1+2*4-3");
        Token[] expected = expectedSimpleExpression();
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void tokenizer_whitespace_simple_expression() throws Exception {
        List<Token> tokens = Tokenizer.tokenize(" 1 +2  * 4 -3 ");
        Token[] expected = expectedSimpleExpression();
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test(expected = InvalidFormulaException.class)
    public void tokenizer_unknown_character() throws Exception {
        Tokenizer.tokenize("1+2*a4-3");
    }

    private Token[] expectedSimpleExpression() {
        Token[] expected = {
                new Token(Token.Type.NUMBER_LITERAL, "1"),
                new Token(Token.Type.OPERATOR, "+"),
                new Token(Token.Type.NUMBER_LITERAL, "2"),
                new Token(Token.Type.OPERATOR, "*"),
                new Token(Token.Type.NUMBER_LITERAL, "4"),
                new Token(Token.Type.OPERATOR, "-"),
                new Token(Token.Type.NUMBER_LITERAL, "3"),
        };
        return expected;
    }
}
