package net.aohayou.collector.data.formula;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class DiscontinuousRange {

    private NavigableMap<Integer, Integer> ranges;

    public DiscontinuousRange() {
        ranges = new TreeMap<>();
    }

    public DiscontinuousRange(@NonNull Range range) {
        ranges = new TreeMap<>();
        ranges.put(range.first, range.last);
    }

    public DiscontinuousRange add(@NonNull Range range) {
        //TODO
        return null;
    }

    public DiscontinuousRange add(@NonNull DiscontinuousRange other) {
        //TODO
        return null;
    }

    public DiscontinuousRange remove(@NonNull Range other) {
        //TODO
        return null;
    }

    public DiscontinuousRange remove(@NonNull DiscontinuousRange other) {
        //TODO
        return null;
    }

    public boolean contains(int number) {
        Map.Entry<Integer, Integer> floorRangeEntry = ranges.floorEntry(number);
        if (floorRangeEntry == null) {
            return false;
        }
        Range floorRange = new Range(floorRangeEntry.getKey(), floorRangeEntry.getValue());
        return floorRange.contains(number);
    }

    public boolean contains(@NonNull Range range) {
        Map.Entry<Integer, Integer> floorRangeEntry = ranges.floorEntry(range.first);
        if (floorRangeEntry == null) {
            return false;
        }
        Range floorRange = new Range(floorRangeEntry.getKey(), floorRangeEntry.getValue());
        return floorRange.contains(range.first) && floorRange.contains(range.last);
    }

    public int size() {
        return 0;
    }
}
