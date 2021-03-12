package utility;

import java.util.Objects;

public class Range implements Comparable<Range> {
    private int start;
    private int end;
    private int size;

    public Range(int start, int end) {
        this.start = start;
        this.end = end;
        this.size = this.end - this.start;
    }

    public int getStart() {return start;}
    public int getEnd() {return end;}
    public int getSize() {return size;}

    public boolean contains(int number) {
        return number >= start && number <= end;
    }

    public boolean overlaps(Range other) {
        return (this.end<=other.end && this.end>=other.start) || (this.start>=other.start && this.start<=other.end);
    }

    @Override
    public int compareTo(Range other) {
        int val = this.start-other.start;
        if (val == 0) return this.size-other.size;
        return val;
    }
}

