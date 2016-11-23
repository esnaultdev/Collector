package net.aohayou.collector.data.formula;

public class Range {
    public final int first; // number of the first element of the range
    public final int last;  // number of the last element of the range

    public Range(int first, int last) {
        if (first > last) {
            throw new RuntimeException("Invalid range: " + first + "*" + last);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        return first == range.first && last == range.last;

    }
}