package net.aohayou.collector.data.formula;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FormulaConverter {

    public List<Range> parse(@NonNull String formulaString) throws InvalidFormulaException {
        Preconditions.checkArgument(!TextUtils.isEmpty(formulaString));


        return new ArrayList<>();
    }

    private static class Tokenizer {

        private static class Token {
            private enum Type {
                NUMBER_LITERAL,
                OPERATOR
            }

            public final Type type;
            public final String value;

            public Token(@NonNull Type type, @NonNull String value) {
                this.type = type;
                this.value = value;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Token token = (Token) o;

                return value.equals(token.value) && type == token.type;
            }
        }

        public static List<Token> tokenize(@NonNull CharSequence input)
                throws InvalidFormulaException {
            List<Token> tokens = new LinkedList<>();
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

                    tokens.add(new Token(Token.Type.NUMBER_LITERAL, builder.toString()));
                    continue;
                }

                if (isOperator(currentChar)) {
                    tokens.add(new Token(Token.Type.OPERATOR, String.valueOf(currentChar)));
                    current++;
                    continue;
                }

                throw new InvalidFormulaException("Unknown character: " + currentChar);
            }

            return tokens;
        }

        private static boolean isOperator(char character) {
            return character == '+' || character == '-' || character == '*';
        }
    }
}
