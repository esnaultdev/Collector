package net.aohayou.collector.data.formula.tokenizer;

import android.support.annotation.NonNull;

public abstract class Token {
    public enum Type {
        ADD,
        REMOVE,
        UNTIL,
        NUMBER
    }

    public final Type type;

    protected Token(@NonNull Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Token)) return false;
        return ((Token) obj).type == type;
    }
}
