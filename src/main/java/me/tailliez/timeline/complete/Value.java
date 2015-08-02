package me.tailliez.timeline.complete;

public class Value<T extends Comparable<T>> {

    private T value = null;
    private boolean unknow = true;
    private boolean isIntinity = false;

    public Value() {
        super();
    }

    public Value(T value) {
        super();
        this.value = value;
    }

    private Value(T value, boolean unknow, boolean isIntinity) {
        this.value = value;
        this.unknow = unknow;
        this.isIntinity = isIntinity;
    }

    public static Value Intinity() {
        return new Value(null, false, true);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isUnknow() {
        return unknow;
    }

    public void setUnknow(boolean unknow) {
        this.unknow = unknow;
    }

    public boolean isIntinity() {
        return isIntinity;
    }

    public void setIsIntinity(boolean isIntinity) {
        this.isIntinity = isIntinity;
    }
}
