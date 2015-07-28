package me.tailliez.timeline;

import java.util.Collection;
import java.util.Iterator;

public class RangeUtils {

    public static <E,V extends Comparable<V>> Collection<E> addRange(final Collection<E> coll, final E newEntity, final RangeMetadata<E,Range<V>, V> range) {
        Range<V> newEntityRange = range.getRangeGetter().apply(newEntity);
        V minBegin = null;
        boolean newAdded = false;
        for(Iterator<E> i = coll.iterator();i.hasNext();) {
            E entity = i.next();
            if ( !range.sameSerie.test(entity, newEntity) ) continue;
            Range<V> entityRange = range.getRangeGetter().apply(entity);
            if ( minBegin == null || minBegin.compareTo(entityRange.getBegin())==-1) {
                minBegin = entityRange.getBegin();
            }
            if (entityRange.isValueInRange(newEntityRange.getBegin())) {
                newEntityRange.setEnd(entityRange.getEnd());
                entityRange.setEnd(entityRange.getPreviousValue(newEntityRange.getBegin()));
                coll.add(newEntity);
                newAdded = true;
            }
        }
        if ( !newAdded ) {
            newEntityRange.setEnd(newEntityRange.getPreviousValue(minBegin));
            coll.add(newEntity);
        }
        return coll;
    }

}
