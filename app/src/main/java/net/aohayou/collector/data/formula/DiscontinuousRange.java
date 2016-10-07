package net.aohayou.collector.data.formula;

import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class DiscontinuousRange {

    private NavigableMap<Integer, Integer> ranges;
    private int elementCount = 0;

    public DiscontinuousRange() {
        ranges = new TreeMap<>();
    }

    public DiscontinuousRange(@NonNull Range range) {
        this(new Range[]{range});
    }

    public DiscontinuousRange(@NonNull Range[] ranges) {
        this.ranges = new TreeMap<>();
        for (Range range : ranges) {
            this.ranges.put(range.first, range.last);
            elementCount += range.size();
        }
    }

    public DiscontinuousRange add(@NonNull Range other) {
        return new Merger(toRangeArray(), new Range[]{other}).merge();
    }

    public DiscontinuousRange add(@NonNull DiscontinuousRange other) {
        return new Merger(toRangeArray(), other.toRangeArray()).merge();
    }

    public DiscontinuousRange remove(@NonNull Range other) {
        return Remover.remove(toRangeArray(), new Range[] {other});
    }

    public DiscontinuousRange remove(@NonNull DiscontinuousRange other) {
        return Remover.remove(toRangeArray(), other.toRangeArray());
    }

    private static Range[] toRangeArray(@NonNull List<Range> list) {
        Range[] resultArray = new Range[list.size()];
        return list.toArray(resultArray);
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
        return elementCount;
    }

    public Range[] toRangeArray() {
        Range[] result = new Range[ranges.size()];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : ranges.entrySet()) {
            Range range = new Range(entry.getKey(), entry.getValue());
            result[i] = range;
            i++;
        }
        return result;
    }

    /**
     * Utility class performing a merge of two discontinuous ranges.
     */
    private static class Merger {

        private final Range[] left;
        private final Range[] right;

        private LinkedList<Range> result;

        public Merger(@NonNull Range[] left, @NonNull Range[] right) {
            this.left = left;
            this.right = right;

            result = new LinkedList<>();
        }

        public DiscontinuousRange merge() {
            int indexLeft = 0;
            int indexRight = 0;
            Range currentLeft;
            Range currentRight;

            while (indexLeft < left.length && indexRight < right.length) {
                currentLeft = left[indexLeft];
                currentRight = right[indexRight];

                if (currentLeft.last < currentRight.first) {
                    //  [   left   ]
                    //                   [   right   ]
                    addRangeToResult(currentLeft);
                    indexLeft++;
                } else if (currentRight.last < currentLeft.first) {
                    //                   [   left   ]
                    //  [   right   ]
                    addRangeToResult(currentRight);
                    indexRight++;
                } else if (currentLeft.first <= currentRight.first
                        && currentLeft.last >= currentRight.last) {
                    //  [           left            ]
                    //      [  right  ]
                    //
                    // We can't only add left and increment both sides, or we will have two times
                    // the same portion of range in the following case:
                    //  [           left            ]
                    //      [  right  ]     [  nextRight  ]
                    //
                    // We need to split the original left range in two pieces that will be append
                    // later in the result
                    //  [    left     ][  nextLeft  ]
                    //      [  right  ]     [  nextRight  ]

                    addRangeToResult(new Range(currentLeft.first, currentRight.last));
                    left[indexLeft] = new Range(currentRight.last, currentLeft.last);
                    indexRight++;
                } else if (currentRight.first <= currentLeft.first
                        && currentRight.last >= currentLeft.last) {
                    //      [  left  ]
                    //  [        right         ]
                    // We need to split the right range this time
                    addRangeToResult(new Range(currentRight.first, currentLeft.last));
                    right[indexRight] = new Range(currentLeft.last, currentRight.last);
                    indexLeft++;
                } else if (currentLeft.first <= currentRight.first
                        && currentLeft.last < currentRight.last) {
                    //  [       left       ]
                    //            [       right       ]
                    // We need to split the right range again
                    addRangeToResult(new Range(currentLeft.first, currentLeft.last));
                    right[indexRight] = new Range(currentLeft.last, currentRight.last);
                    indexLeft++;
                } else if (currentRight.first <= currentLeft.first
                        && currentRight.last < currentLeft.last) {
                    //            [       left       ]
                    //  [       right       ]
                    // We need to split the left range again
                    addRangeToResult(new Range(currentRight.first, currentRight.last));
                    left[indexLeft] = new Range(currentRight.last, currentLeft.last);
                    indexRight++;
                }
            }

            if (indexLeft == left.length) {
                while (indexRight < right.length) {
                    addRangeToResult(right[indexRight]);
                    indexRight++;
                }
            }

            if (indexRight == right.length) {
                while (indexLeft < left.length) {
                    addRangeToResult(left[indexLeft]);
                    indexLeft++;
                }
            }

            return new DiscontinuousRange(toRangeArray(result));
        }

        /** Add the range to the result, appending it to the last range if possible */
        private void addRangeToResult(@NonNull Range range) {
            Range lastRange = result.peekLast();
            if (lastRange != null && canAppend(lastRange, range)) {
                result.removeLast();
                result.add(new Range(lastRange.first, range.last));
            } else {
                result.add(range);
            }
        }

        private static boolean canAppend(@NonNull Range left, @NonNull Range right) {
            return left.last == right.first || left.last == right.first - 1;
        }
    }

    /**
     * Utility class performing a removal of a discontinuous range from another.
     * This could be a static method of {@link DiscontinuousRange} but is kept in a static class
     * to match the {@link Merger} behaviour.
     */
    private static class Remover {

        public static DiscontinuousRange remove(@NonNull Range[] left, @NonNull Range[] right) {
            List<Range> result = new LinkedList<>();
            int indexLeft = 0;
            int indexRight = 0;
            Range currentLeft;
            Range currentRight;

            while (indexLeft < left.length && indexRight < right.length) {
                currentLeft = left[indexLeft];
                currentRight = right[indexRight];

                if (currentLeft.last < currentRight.first) {
                    //  [   left   ]
                    //                   [   right   ]
                    result.add(currentLeft);
                    indexLeft++;
                } else if (currentRight.last < currentLeft.first) {
                    //                   [   left   ]
                    //  [   right   ]
                    //  Nothing to remove
                    indexRight++;
                } else if (currentLeft.first <= currentRight.first
                        && currentLeft.last >= currentRight.last) {
                    //  [           left            ]
                    //       [  right  ]
                    //
                    // We can't only add left - right and increment both sides, or we will miss
                    // a removal in the following case:
                    //  [           left            ]
                    //       [  right  ]     [  nextRight  ]
                    //
                    // We need to only add the part of left before right, the other part will be
                    // processed in the next iteration of the loop
                    //  [add]           [  nextLeft  ]
                    //       [  right  ]     [  nextRight  ]
                    if (currentLeft.first != currentRight.first) {
                        result.add(new Range(currentLeft.first, currentRight.first - 1));
                    }
                    if (currentLeft.last != currentRight.last) {
                        left[indexLeft] = new Range(currentRight.last + 1, currentLeft.last);
                    } else {
                        indexLeft++;
                    }
                    indexRight++;
                } else if (currentRight.first <= currentLeft.first
                        && currentRight.last >= currentLeft.last) {
                    //      [  left  ]
                    //  [        right         ]
                    indexLeft++;
                } else if (currentLeft.first <= currentRight.first
                        && currentLeft.last < currentRight.last) {
                    //  [       left       ]
                    //            [       right       ]
                    if (currentLeft.first != currentRight.first) {
                        result.add(new Range(currentLeft.first, currentRight.first - 1));
                    }
                    indexLeft++;
                } else if (currentRight.first <= currentLeft.first
                        && currentRight.last < currentLeft.last) {
                    //            [       left       ]
                    //  [       right       ]
                    // We might also miss a removal, we only resize the left range
                    left[indexLeft] = new Range(currentRight.last + 1, currentLeft.last);
                    indexRight++;
                }
            }

            if (indexRight == right.length) {
                while (indexLeft < left.length) {
                    result.add(left[indexLeft]);
                    indexLeft++;
                }
            }

            return new DiscontinuousRange(toRangeArray(result));
        }
    }
}
