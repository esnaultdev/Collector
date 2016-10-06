package net.aohayou.collector.data.formula.tokenizer;

public final class NumberToken extends Token {
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
