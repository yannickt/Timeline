package me.tailliez.timeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RangeUtils {

    public static <E, V extends Comparable<V>> Collection<E> addRange(final Collection<E> coll, final E newEntity, final Function<E, RangeMetadata<E, Range<V>, V>> metadataSupplier) {
        if (newEntity == null) return coll;
        if (coll == null) throw new NullPointerException("Collection is null");
        RangeMetadata<E, Range<V>, V> metadata = metadataSupplier.apply(newEntity);
        if (metadata == null) throw new NullPointerException("Range metadata is null");
        Range<V> newEntityRange = metadata.getRangeGetter().apply(newEntity);
        if (newEntityRange == null) throw new NullPointerException("Range is null");
        if (newEntityRange.getBegin() == null) throw new IllegalArgumentException("Begin value is null");
        if (newEntityRange.getEnd() != null) throw new IllegalArgumentException("End value is not null");
        boolean foundMin = false;
        V minBegin = null;
        boolean newAdded = false;
        // Parcour les plages existantes
        List<E> list = (coll instanceof List) ? (List<E>) coll : new ArrayList<>(coll);
        for (ListIterator<E> i = list.listIterator(); i.hasNext(); ) {
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
            if (minBegin != null) {
                newEntityRange.setEnd(newEntityRange.getPreviousValue(minBegin));
                coll.add(newEntity);
            } else if (!foundMin) {
                coll.add(newEntity);
            } else {
                throw new IllegalArgumentException("Can't add a range before");
            }
        }
        if ( ! (coll instanceof List) ) {
            coll.clear();
            coll.addAll(list);
        }
        return coll;
    }

    public static <E, V extends Comparable<V>> Collection<E> removeRange(final Collection<E> coll, final E oldEntity, final Function<E, RangeMetadata<E, Range<V>, V>> metadataSupplier) {
        if (coll == null) throw new NullPointerException("Collection is null");
        if ( oldEntity == null ) throw new IllegalArgumentException("element to be removed is null");
        if ( !coll.contains(oldEntity)) {
            throw new IllegalArgumentException("element not in collection");
        }
        RangeMetadata<E, Range<V>, V> metadata = metadataSupplier.apply(oldEntity);
        if (metadata == null) throw new NullPointerException("Range metadata is null");
        Range<V> oldEntityRange = metadata.getRangeGetter().apply(oldEntity);
        if (oldEntityRange == null) throw new NullPointerException("Range is null");
        if (oldEntityRange.getBegin() == null) throw new IllegalArgumentException("Begin value is null");

        final V endOld = oldEntityRange.getEnd();
        final V previousBeginOld =  oldEntityRange.getPreviousValue(oldEntityRange.getBegin());

        // On ne traite que les éléments de la série
        Collection<E> serie = coll.stream().filter(e -> metadata.getSameSerie().test(e, oldEntity)).collect(Collectors.toList());

        // On retire l'élément
        coll.remove(oldEntity);

        // On prolonge les éléments précédents jusqu'a la fin du supprimé
        serie.stream().filter(e-> Objects.equals(metadata.getRangeGetter().apply(e).getEnd(), previousBeginOld)).forEach(e -> metadata.getRangeGetter().apply(e).setEnd(endOld));

        return coll;
    }

    public static <E, V extends Comparable<V>> Collection<E> checkRange(final Collection<E> coll, final Function<E, RangeMetadata<E, Range<V>, V>> metadataSupplier) {
        if (coll == null) throw new NullPointerException("Collection is null");

        Map<V, List<E>> dispatchBySeries = new HashMap<>();

        //coll.stream().collect(Collectors.groupingBy());

        for(Iterator<E> i = coll.iterator();i.hasNext();) {
            E entity = i.next();

        }




        return coll;
    }
}