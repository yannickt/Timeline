package me.tailliez.timeline;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public class RangeUtils {

    public static <E, V extends Comparable<V>> List<E> addRange(final List<E> coll, final E newEntity, final Function<E, RangeMetadata<E, Range<V>, V>> metadataSupplier) {
        if (newEntity == null) return coll;
        if (coll == null) throw new NullPointerException("null collection");
        RangeMetadata<E, Range<V>, V> metadata = metadataSupplier.apply(newEntity);
        if (metadata == null) throw new NullPointerException("null range matadata");
        Range<V> newEntityRange = metadata.getRangeGetter().apply(newEntity);
        boolean foundMin = false;
        V minBegin = null;
        boolean newAdded = false;
        for (ListIterator<E> i = coll.listIterator(); i.hasNext(); ) {
            E entity = i.next();
            if (!metadata.getSameSerie().test(entity, newEntity)) continue;
            Range<V> entityRange = metadata.getRangeGetter().apply(entity);
            if (minBegin == null || entityRange.getBegin() == null || minBegin.compareTo(entityRange.getBegin()) == -1) {
                minBegin = entityRange.getBegin();
                foundMin = true;
            }
            if (entityRange.isValueInRange(newEntityRange.getBegin())) {
                newEntityRange.setEnd(entityRange.getEnd());
                entityRange.setEnd(entityRange.getPreviousValue(newEntityRange.getBegin()));
                i.add(newEntity);
                newAdded = true;
            }
        }
        if (!newAdded) {
            if ( minBegin != null ) {
                newEntityRange.setEnd(newEntityRange.getPreviousValue(minBegin));
                coll.add(newEntity);
            }
            else if (!foundMin) {
                coll.add(newEntity);
            }
            else {
                throw new IllegalArgumentException("Can't add a range before");
            }
        }
        return coll;
    }

}
