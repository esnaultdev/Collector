package net.aohayou.collector.data.formula.parser;

import android.support.annotation.NonNull;

public final class UntilOperator extends Operator {
    public UntilOperator(@NonNull NumberNode left, @NonNull NumberNode right) {
        super(left, right);
    }

    public @NonNull NumberNode getLeft() {
        return (NumberNode) left;
    }

    public @NonNull NumberNode getRight() {
        return (NumberNode) right;
    }
}
