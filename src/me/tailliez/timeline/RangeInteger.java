package me.tailliez.timeline;

public class RangeInteger<T> extends Range<Integer> {

    public RangeInteger(Integer begin, Integer end) {
        super(begin, end);
    }

    //public abstract Range<V> factory(Integer begin, Integer end);

    public Integer getNextValue(Integer value) {
        return Integer.valueOf(value + 1);
    }

    public Integer getPreviousValue(Integer value) {
        return Integer.valueOf(value - 1);
    }

}
