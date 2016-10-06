package net.aohayou.collector.data.formula.parser;

import com.google.common.base.Preconditions;

import net.aohayou.collector.data.formula.InvalidFormulaException;
import net.aohayou.collector.data.formula.tokenizer.NumberToken;
import net.aohayou.collector.data.formula.tokenizer.Token;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private Token currentToken;
    private int current = -1;

    public Parser(List<Token> tokens) {
        this.tokens = Preconditions.checkNotNull(tokens);
    }

    private void nextToken() {
        currentToken = (++current < tokens.size()) ? tokens.get(current) : null;
    }

    private boolean accept(Token.Type tokenType) {
        if (currentToken != null && currentToken.type == tokenType) {
            nextToken();
            return true;
        }
        return false;
    }

    public Node parse() throws InvalidFormulaException {
        nextToken();
        Node result = parseRangeOperations();
        if (current < tokens.size()) {
            throw new InvalidFormulaException("Unexpected token: " + currentToken.type.name());
        }
        return result;
    }

    private Node parseRangeOperations() throws InvalidFormulaException {
        Node left = parseRange();
        while (true) {
            if (accept(Token.Type.ADD)) {
                Node right = parseRange();
                left = new AddOperator(left, right);
            } else if (accept(Token.Type.REMOVE)) {
                Node right = parseRange();
                left = new RemoveOperator(left, right);
            } else {
                return left;
            }
        }
    }

    private Node parseRange() throws InvalidFormulaException {
        Node left = parseNumber();
        if (accept(Token.Type.UNTIL)) {
            Node right = parseNumber();
            return new UntilOperator(left, right);
        } else {
            return left;
        }
    }

    private Node parseNumber() throws InvalidFormulaException {
        if (currentToken != null && currentToken.type == Token.Type.NUMBER) {
            Node node = new NumberNode(((NumberToken) currentToken).number);
            nextToken();
            return node;
        } else {
            String tokenName = currentToken == null ? "null" : currentToken.type.name();
            throw new InvalidFormulaException(
                    "Unexpected token: " + tokenName + ", number expected");
        }
    }
}
