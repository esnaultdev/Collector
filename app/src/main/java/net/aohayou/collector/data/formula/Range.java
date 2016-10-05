package net.aohayou.collector.data.formula;

public class Range {
    public final int first; // number of the first element of the range
    public final int last;  // number of the last element of the range

    public Range(int first, int last) {
        this.first = first;
        this.last = last;
    }

    public boolean contains(int number) {
        return number >= first && number <= last;
    }
}