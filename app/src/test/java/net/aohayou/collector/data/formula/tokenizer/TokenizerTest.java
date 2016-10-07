package net.aohayou.collector.data.formula.tokenizer;

import net.aohayou.collector.data.formula.InvalidFormulaException;

import org.junit.Test;

import java.util.List;

import static net.aohayou.collector.data.formula.FormulaConverterTestConstants.*;
import static org.junit.Assert.*;

public class TokenizerTest {

    @Test
    public void single_number() throws Exception {
        List<Token> tokens = Tokenizer.tokenize("123");
        Token[] expected = {new NumberToken(123)};
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void simple_formula() throws Exception {
        List<Token> tokens = Tokenizer.tokenize(simpleFormula());
        Token[] expected = expectedTokensSimpleFormula();
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void whitespace_simple_formula() throws Exception {
        List<Token> tokens = Tokenizer.tokenize(simpleFormulaWithSpaces());
        Token[] expected = expectedTokensSimpleFormula();
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void empty_formula() throws Exception {
        List<Token> tokens = Tokenizer.tokenize("");
        assertArrayEquals(new Token[]{}, tokens.toArray());
    }

    @Test(expected = InvalidFormulaException.class)
    public void unknown_character() throws Exception {
        Tokenizer.tokenize("1+2*a4-3");
    }
}
