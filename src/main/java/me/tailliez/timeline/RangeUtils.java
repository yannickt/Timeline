package me.tailliez.timeline;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public class RangeUtils {

    public static <E, V extends Comparable<V>> List<E> addRange(final List<E> coll, final E newEntity, final Function<E, RangeMetadata<E, Range<V>, V>> metadataSupplier) {
        if (newEntity == null) return coll;
        if (coll == null) throw new NullPointerException("Collection is null");
        RangeMetadata<E, Range<V>, V> metadata = metadataSupplier.apply(newEntity);
        if (metadata == null) throw new NullPointerException("Range metadata is null");
        Range<V> newEntityRange = metadata.getRangeGetter().apply(newEntity);
        if ( newEntityRange == null) throw new NullPointerException("Range is null");
        if ( newEntityRange.getBegin() == null) throw new IllegalArgumentException("Begin value is null");
        if ( newEntityRange.getEnd() != null) throw new IllegalArgumentException("End value is not null");
        boolean foundMin = false;
        V minBegin = null;
        boolean newAdded = false;
        // Parcour les plages existantes
        for (ListIterator<E> i = coll.listIterator(); i.hasNext(); ) {
            E entity = i.next();
            // On passe les plages d'une séries de données différentes
            if (!metadata.getSameSerie().test(entity, newEntity)) continue;
            // On recherche la plage la plus petite pour le cas d'un ajout devant.
            Range<V> entityRange = metadata.getRangeGetter().apply(entity);
            if (minBegin == null || entityRange.getBegin() == null || minBegin.compareTo(entityRange.getBegin()) == -1) {
                minBegin = entityRange.getBegin();
                foundMin = true;
            }
            // Si on a une plage qui contient la valeur de début a insérer
            if (entityRange.isValueInRange(newEntityRange.getBegin())) {
                // On la modifie pour la terminer avec la nouvelle
                newEntityRange.setEnd(entityRange.getEnd());
                entityRange.setEnd(entityRange.getPreviousValue(newEntityRange.getBegin()));
                // Et on insert la nouvelle
                i.add(newEntity);
                newAdded = true;
            }
        }
        // Si la nouvelle plage doit être mis devant toutes les autres
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
