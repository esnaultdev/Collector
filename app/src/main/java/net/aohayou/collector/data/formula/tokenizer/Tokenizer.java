package net.aohayou.collector.data.formula.tokenizer;

import android.support.annotation.NonNull;

import net.aohayou.collector.data.formula.InvalidFormulaException;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

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
