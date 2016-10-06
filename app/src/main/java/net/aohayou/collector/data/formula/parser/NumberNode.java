package net.aohayou.collector.data.formula.parser;

public final class NumberNode extends Node {
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
