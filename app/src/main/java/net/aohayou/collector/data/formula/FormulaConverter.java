package net.aohayou.collector.data.formula;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class FormulaConverter {

    public List<Range> convert(@NonNull String formulaString) throws InvalidFormulaException {
        Preconditions.checkArgument(!TextUtils.isEmpty(formulaString));


        return new ArrayList<>();
    }

    // Visible for testing
    static class Tokenizer {

        static abstract class Token {
            enum Type {
                ADD,
                REMOVE,
                UNTIL,
                NUMBER
            }

            private final Type type;

            private Token(@NonNull Type type) {
                this.type = type;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (!(obj instanceof Token)) return false;
                return ((Token) obj).type == type;
            }
        }

        static final class AddToken extends Token {
            public AddToken() {
                super(Type.ADD);
            }
        }

        static final class RemoveToken extends Token {
            public RemoveToken() {
                super(Type.REMOVE);
            }
        }

        static final class UntilToken extends Token {
            public UntilToken() {
                super(Type.UNTIL);
            }
        }

        static final class NumberToken extends Token {
            public final int number;
            public NumberToken(int number) {
                super(Type.NUMBER);
                this.number = number;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (!(obj instanceof NumberToken)) return false;
                return ((NumberToken) obj).number == number;
            }
        }

        public static List<Token> tokenize(@NonNull CharSequence input)
                throws InvalidFormulaException {
            List<Token> tokens = new ArrayList<>();
            int current = 0;

            while (current < input.length()) {
                char currentChar = input.charAt(current);

                if (currentChar == ' ') {
                    current++;
                    continue;
                }

                if (Character.isDigit(currentChar)) {
                    StringBuilder builder = new StringBuilder();

                    while (Character.isDigit(currentChar)) {
                        builder.append(currentChar);
                        current ++;
                        if (current < input.length()) {
                            currentChar = input.charAt(current);
                        } else {
                            break;
                        }
                    }

                    tokens.add(new NumberToken(Integer.parseInt(builder.toString())));
                    continue;
                }

                if (currentChar == '+') {
                    tokens.add(new AddToken());
                    current++;
                    continue;
                }

                if (currentChar == '-') {
                    tokens.add(new RemoveToken());
                    current++;
                    continue;
                }

                if (currentChar == '*') {
                    tokens.add(new UntilToken());
                    current++;
                    continue;
                }

                throw new InvalidFormulaException("Unknown character: " + currentChar);
            }

            return tokens;
        }
    }

    static class Parser {

        static abstract class Node {}

        static abstract class Operator extends Node {
            public final Node left;
            public final Node right;

            public Operator(@NonNull Node left, @NonNull Node right) {
                this.left = left;
                this.right = right;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (!(obj instanceof Operator)) return false;
                Operator op = (Operator) obj;
                return left.equals(op.left) && right.equals(op.right);
            }
        }

        static final class AddOperator extends Operator {
            public AddOperator(@NonNull Node left, @NonNull Node right) {
                super(left, right);
            }
        }

        static final class RemoveOperator extends Operator {
            public RemoveOperator(@NonNull Node left, @NonNull Node right) {
                super(left, right);
            }
        }

        static final class UntilOperator extends Operator {
            public UntilOperator(@NonNull Node left, @NonNull Node right) {
                super(left, right);
            }
        }

        static final class NumberNode extends Node {
            public final int value;

            public NumberNode(int value) {
                this.value = value;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (!(obj instanceof NumberNode)) return false;
                return ((NumberNode) obj).value == value;
            }
        }

        private final List<Tokenizer.Token> tokens;
        private Tokenizer.Token currentToken;
        private int current = -1;

        public Parser(List<Tokenizer.Token> tokens) {
            this.tokens = Preconditions.checkNotNull(tokens);
        }

        private void nextToken() {
            currentToken = (++current < tokens.size()) ? tokens.get(current) : null;
        }

        private boolean accept(Tokenizer.Token.Type tokenType) {
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
                if (accept(Tokenizer.Token.Type.ADD)) {
                    Node right = parseRange();
                    left = new AddOperator(left, right);
                } else if (accept(Tokenizer.Token.Type.REMOVE)) {
                    Node right = parseRange();
                    left = new RemoveOperator(left, right);
                } else {
                    return left;
                }
            }
        }

        private Node parseRange() throws InvalidFormulaException {
            Node left = parseNumber();
            if (accept(Tokenizer.Token.Type.UNTIL)) {
                Node right = parseNumber();
                return new UntilOperator(left, right);
            } else {
                return left;
            }
        }

        private Node parseNumber() throws InvalidFormulaException {
            if (currentToken != null && currentToken.type == Tokenizer.Token.Type.NUMBER) {
                Node node = new NumberNode(((Tokenizer.NumberToken) currentToken).number);
                nextToken();
                return node;
            } else {
                String tokenName = currentToken == null ? "null" : currentToken.type.name();
                throw new InvalidFormulaException(
                        "Unexpected token: " + tokenName + ", number expected");
            }
        }
    }
}
