package me.tailliez.timeline.complete;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class SerieDescriptor<E,V extends Value<T>,T extends Comparable<T>,K> {

    Function<E,Range<V>> rangeGetter = null;
    BiConsumer<E,Range> rangeSetter = null;
    Function<K,E> serieId = null;


}
