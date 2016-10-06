package net.aohayou.collector.data.formula;

public class Range {
    public final int first; // number of the first element of the range
    public final int last;  // number of the last element of the range

    public Range(int first, int last) {
        if (first > last) {
            throw new RuntimeException("Invalid range: first must be lower than or equal to last");
        }
        this.first = first;
        this.last = last;
    }

    public boolean contains(int number) {
        return number >= first && number <= last;
    }

    public int size() {
        return last - first + 1;
    }
}