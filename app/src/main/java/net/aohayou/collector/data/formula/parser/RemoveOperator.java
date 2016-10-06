package net.aohayou.collector.data.formula.parser;

import android.support.annotation.NonNull;

public final class RemoveOperator extends Operator {
    public RemoveOperator(@NonNull Node left, @NonNull Node right) {
        super(left, right);
    }
}
