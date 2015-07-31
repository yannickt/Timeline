package me.tailliez.timeline;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class RangeMetadata<T, R extends Range<V>, V extends Comparable<V>> {

    Function<T,R> rangeGetter = null;
    BiConsumer<T,R> rangeSetter = null;
    BiPredicate<T,T> sameSerie = null;

    public RangeMetadata(Function<T, R> rangeGetter, BiConsumer<T, R> rangeSetter) {
        this.rangeGetter = rangeGetter;
        this.rangeSetter = rangeSetter;
    }

    public RangeMetadata(Function<T, R> rangeGetter, BiConsumer<T, R> rangeSetter, BiPredicate<T, T> sameSerie) {
        this.rangeGetter = rangeGetter;
        this.rangeSetter = rangeSetter;
        this.sameSerie = sameSerie;
    }

    public Predicate<T> isInSameSerieThen(T entity1) {
        return entity2 -> sameSerie.test(entity1,entity2);
    }

    public Function<T, R> getRangeGetter() {
        return rangeGetter;
    }

    public BiConsumer<T, R> getRangeSetter() {
        return rangeSetter;
    }

    public BiPredicate<T, T> getSameSerie() {
        if (sameSerie == null) {
            return (t, t2) -> true;
        }
        return sameSerie;
    }
}
