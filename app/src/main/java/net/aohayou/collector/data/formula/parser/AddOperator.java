package net.aohayou.collector.data.formula.parser;

import android.support.annotation.NonNull;

public final class AddOperator extends Operator {
    public AddOperator(@NonNull Node left, @NonNull Node right) {
        super(left, right);
    }
}
