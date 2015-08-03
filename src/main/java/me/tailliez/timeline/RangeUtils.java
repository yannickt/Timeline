package me.tailliez.timeline;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
// Interface  org.hibernate.usertype.UserCollectionType

public class RangeUtils {

    public static <E, R extends Range<V>, V extends Comparable<V>> Collection<E> addRange(final Collection<E> coll, final E newEntity, final Function<E, RangeMetadata<E, R, V>> metadataSupplier) {
        if (newEntity == null) return coll;
        if (coll == null) throw new NullPointerException("Collection is null");
        RangeMetadata<E, R, V> metadata = metadataSupplier.apply(newEntity);
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
                entityRange.setEnd(newEntityRange.getBegin());
                // Et on insert la nouvelle
                i.add(newEntity);
                newAdded = true;
            }
        }
        // Si la nouvelle plage doit être mise devant toutes les autres
        if (!newAdded) {
            if (minBegin != null) {
                newEntityRange.setEnd(minBegin);
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

    public static <E, R extends Range<V>, V extends Comparable<V>> Collection<E> removeRange(final Collection<E> coll, final E oldEntity, final Function<E, RangeMetadata<E, R, V>> metadataSupplier) {
        if (coll == null) throw new NullPointerException("Collection is null");
        if ( oldEntity == null ) throw new IllegalArgumentException("element to be removed is null");
        if ( !coll.contains(oldEntity)) {
            throw new IllegalArgumentException("element not in collection");
        }
        RangeMetadata<E, R, V> metadata = metadataSupplier.apply(oldEntity);
        if (metadata == null) throw new NullPointerException("Range metadata is null");
        Range<V> oldEntityRange = metadata.getRangeGetter().apply(oldEntity);
        if (oldEntityRange == null) throw new NullPointerException("Range is null");
        if (oldEntityRange.getBegin() == null) throw new IllegalArgumentException("Begin value is null");

        final V endOld = oldEntityRange.getEnd();
        final V previousBeginOld =  oldEntityRange.getBegin();

        // On ne traite que les éléments de la série
        Collection<E> serie = coll.stream().filter(e -> metadata.getSameSerie().test(e, oldEntity)).collect(Collectors.toList());

        // On retire l'élément
        coll.remove(oldEntity);

        // On prolonge les éléments précédents jusqu'a la fin du supprimé
        serie.stream().filter(e-> Objects.equals(metadata.getRangeGetter().apply(e).getEnd(), previousBeginOld)).forEach(e -> metadata.getRangeGetter().apply(e).setEnd(endOld));

        return coll;
    }

    public static <E, R extends Range<V>, V extends Comparable<V>> boolean checkRangeConsecutive(final Collection<E> coll, final Function<E, RangeMetadata<E, R, V>> metadataSupplier) {
        if (coll == null) throw new NullPointerException("Collection is null");

        Map<E, List<E>> dispatchBySeries = new HashMap<>();

        final RangeMetadata<E, R, V> metadata = coll.stream().filter(e->e!=null).map(metadataSupplier).findAny().orElseThrow(()->new NullPointerException("Range metadata is null"));

        for(Iterator<E> i = coll.iterator();i.hasNext();) {
            E entity = i.next();
            Optional<E> key = dispatchBySeries.keySet().stream().filter(e -> metadata.getSameSerie().test(entity, e)).findAny();
            if (!key.isPresent()) {
                key = Optional.of(entity);
                dispatchBySeries.put(entity, new ArrayList<E>());
            }
            dispatchBySeries.get(key.get()).add(entity);
        }

        final AtomicBoolean consecutive = new AtomicBoolean(true);
        dispatchBySeries.values().stream().forEach(serie->
        {
            if ( serie.size()>1 ) {
                List sortedRanges = serie.stream().map(metadata.getRangeGetter()).sorted((o1, o2) -> o1.getBegin().compareTo(o2.getBegin())).collect(Collectors.toList());
                Iterator<R> i = sortedRanges.iterator();
                R current = i.next();
                R next;
                while ( i.hasNext() ) {
                    next = i.next();
                    if ( !current.isConsecutive(current,next) ) {
                        consecutive.set(false);
                        break;
                    }
                    current = next;
                }
            }
        });
        return consecutive.get();
    }

}