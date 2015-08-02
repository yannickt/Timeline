package me.tailliez.timeline;

public class RangeIE<V extends Comparable<V>> extends Range<V> {


    public RangeIE(V begin, V end) {
        super(begin, end);
    }

    public RangeIE(RangeIE<V> other) {
        super(other);
    }

    public boolean isValueBeforeRange(V value) {
        return isValueBeforeBegin(value);
    }

    public boolean isValueInRange(V value) {
        return isValueAfterOrEqualsBegin(value) && isValueBeforeEnd(value);
    }

    public boolean isValueAfterRange(V value) {
        return isValueAfterOrEqualsEnd(value);
    }

    public static <V extends Comparable<V>> boolean isConsecutive(RangeIE<V> r1, RangeIE<V> r2) {
        if (r1 == null || r2 == null) throw new NullPointerException();
        return (r2.getBegin()).compareTo(r1.getEnd()) == 0
                || (r1.getBegin()).compareTo(r2.getEnd()) == 0;
    }

}
