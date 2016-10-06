package net.aohayou.collector.data.formula;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class DiscontinuousRange {

    private TreeMap<Integer, Integer> ranges;

    public DiscontinuousRange() {
        ranges = new TreeMap<>();
    }

    public DiscontinuousRange(@NonNull Range range) {
        ranges = new TreeMap<>();
        ranges.put(range.first, range.last);
    }

    public DiscontinuousRange mergeWith(@NonNull DiscontinuousRange other) {
        //TODO
        return null;
    }

    public DiscontinuousRange removeFrom(@NonNull DiscontinuousRange other) {
        //TODO
        return null;
    }

    public boolean contains(int number) {
        if (ranges.size() == 0) {
            return false;
        }
        NavigableMap<Integer, Integer> lowerRanges = ranges.headMap(number, true);
        Map.Entry<Integer, Integer> lastLowerRange = lowerRanges.lastEntry();
        Range range = new Range(lastLowerRange.getKey(), lastLowerRange.getValue());
        return range.contains(number);
    }
}
