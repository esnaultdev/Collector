package net.aohayou.collector.data.formula.parser;

import android.support.annotation.NonNull;

public abstract class Operator extends Node {
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
