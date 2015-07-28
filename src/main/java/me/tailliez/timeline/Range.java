package me.tailliez.timeline;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class Range<V extends Comparable<V>> {

    private Class<V> type = null;

    protected V begin;
    protected V end;

    public Range(V begin, V end) {
        this.begin = begin;
        this.end = end;
    }

    public Class<V> getType() {
        if (type == null) {
            Type t = getClass().getGenericSuperclass();
            ParameterizedType pt = (ParameterizedType) t;
            type = (Class) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }
        return type;
    }

    public abstract Range<V> factory(Integer begin, Integer end);

    /*public abstract V getNextValue(V value);

    public abstract V getPreviousValue(V Value);*/

    public V getNextValue(V value) {
        if (Integer.class.equals(getType())) {
            Integer v = (Integer) value;
            return (V) Integer.valueOf(v + 1);
        }
        throw new NotImplementedException();
    }

    public V getPreviousValue(V value) {
        if (Integer.class.equals(getType())) {
            Integer v = (Integer) value;
            return (V) Integer.valueOf(v - 1);
        }
        throw new NotImplementedException();
    }

    public boolean isValueInRange(V value) {
        return isValueAfterOrEqualsBegin(value) && isValueBeforeOrEqualsEnd(value);
    }

    public boolean isValueAfterOrEqualsBegin(V value) {
        if (begin == null) return true;
        if (value == null) return false;
        return begin.compareTo(value) <= 0;
    }

    public boolean isValueBeforeOrEqualsEnd(V value) {
        if (end == null) return true;
        if (value == null) return false;
        return value.compareTo(end) <= 0;
    }

    public V getBegin() {
        return begin;
    }

    public void setBegin(V begin) {
        this.begin = begin;
    }

    public V getEnd() {
        return end;
    }

    public void setEnd(V end) {
        this.end = end;
    }
}
