package net.aohayou.collector.data.formula.parser;

import android.support.annotation.NonNull;

public final class UntilOperator extends Operator {
    public UntilOperator(@NonNull Node left, @NonNull Node right) {
        super(left, right);
    }
}
