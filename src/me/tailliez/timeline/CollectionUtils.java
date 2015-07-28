package me.tailliez.timeline;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils {

	public static <E,V extends Comparable<V>> Collection<E> merge(Collection<E> collSrc, Collection<E> collDest, BiPredicate<E,E> sameSerie, Function<E,V> getStart, BiConsumer<E,V> setStart, Function<E,V> getEnd, BiConsumer<E,V> setEnd, Function<V,V> previousValue) {
		/*Set<E> collInitCopy = ContainerUtil.newTreeSet(entityComparator);
		collInitCopy.addAll(collInit);
		Set<E> collInitNew = ContainerUtil.newTreeSet(entityComparator);
		collInitNew.addAll(collNew);*/
		//Sets.SetView<E> removedEntities = Sets.difference(collSrc, collDest);
		//Sets.SetView<E> addedEntities = Sets.difference(collDest, collSrc);
		Collection<E> removedEntities = new ArrayList<>(collSrc);
		removedEntities.removeAll(collDest);
		Collection<E> addedEntities = new ArrayList<>(collDest);
		addedEntities.removeAll(collSrc);
		for(Iterator<E>  i = addedEntities.iterator();i.hasNext();) {
			E addedEntity = i.next();
			V startValue = getStart.apply(addedEntity);
			Collection<E> serieEntities = collDest.stream().filter(e -> sameSerie.test(addedEntity, e)).collect(Collectors.toList());
			Collection<E> concurrentEntities = serieEntities.stream()
					.filter(entity -> {
						V startEntityValue = getStart.apply(entity);
						V endEntityValue = getEnd.apply(entity);
						return ( startEntityValue == null && endEntityValue == null )
								|| ( startEntityValue == null && endEntityValue.compareTo(startValue) >= 0)
								|| ( endEntityValue == null && startEntityValue.compareTo(startValue) <=0)
								|| ( startEntityValue.compareTo(startValue) <=0 && startValue.compareTo(endEntityValue) <=0);
					}).collect(Collectors.toList());

/*
			TreeMap<V, List<E>> relevantEntitiesByStart = new TreeMap(serieEntities.stream().collect(Collectors.groupingBy(getStart)));
			TreeMap<V, List<E>> relevantEntitiesByEnd = new TreeMap(serieEntities.stream().collect(Collectors.groupingBy(e -> getEnd.apply(e))));
			List<E> justBeforeEntries = relevantEntitiesByStart.lowerEntry(getStart.apply(addedEntity)).getValue();
			justBeforeEntries.stream().forEach(e->setEnd.accept(e,previousValue.apply(getStart.apply(addedEntity))));
*/

		}

		return collDest;
	}

	public static <E> Collection<E> missingElements(Collection<E> collSrc, Collection<E> collDest) {
		Collection<E> removedEntities = new ArrayList<>(collSrc);
		removedEntities.removeAll(collDest);
		return removedEntities;
	}

	public static <E> Collection<DiffElement<E>> diffOfCollection(Set<E> collInit, Set<E> collNew, Comparator<E> entityComparator) {
		Set<E> collInitCopy = Sets.newTreeSet(entityComparator);
		collInitCopy.addAll(collInit);
		Set<E> collInitNew = Sets.newTreeSet(entityComparator);
		collInitNew.addAll(collNew);
		Sets.SetView<E> removedEntity = Sets.difference(collInitCopy, collInitNew);
		Sets.SetView<E> addedEntity = Sets.difference(collInitNew, collInitCopy);

		Collection<DiffElement<E>> result = new ArrayList<>();
/*
		Collection<E> collOfAdded = new ArrayList<>();
		Collection<E> collInitCopy = new ArrayList<>(collInit);
		Collection<E> collNewCopy = new ArrayList<>(collNew);
		boolean 				isModifed = false;
		for(Iterator<E> i=collNewCopy.iterator();i.hasNext();){
			E aEntityDest = i.next();
			boolean found = false;
			for(Iterator<E> j=collInitCopy.iterator();j.hasNext();){
				E aEntitySrc = j.next();
				if ( entityEquality.test(aEntitySrc, aEntityDest) ) {
					result.add(new DiffElement<E>(aEntitySrc, aEntityDest));
					i.remove();
					j.remove();
					found = true;
				}
			}
			if ( !found ) {
				result.add(new DiffElement<E>(null, aEntityDest));
			}
		}
		result.addAll(collInitCopy.stream().map(e -> new DiffElement(aEntitySrc,null));*/
		return result;
	}

	public static <E> Collection<DiffElement<E>> diffOfCollection(Collection<E> collInit, Collection<E> collNew, BiPredicate<E,E> entityEquality) {
		Collection<DiffElement<E>> result = new ArrayList<>();
		Collection<E> collOfAdded = new ArrayList<>();
		Collection<E> collInitCopy = new ArrayList<>(collInit);
		Collection<E> collNewCopy = new ArrayList<>(collNew);
		boolean 				isModifed = false;
		for(Iterator<E> i=collNewCopy.iterator();i.hasNext();){
			E aEntityDest = i.next();
			boolean found = false;
			for(Iterator<E> j=collInitCopy.iterator();j.hasNext();){
				E aEntitySrc = j.next();
				if ( entityEquality.test(aEntitySrc, aEntityDest) ) {
					result.add(new DiffElement<E>(aEntitySrc, aEntityDest));
					i.remove();
					j.remove();
					found = true;
				}
			}
			if ( !found ) {
				result.add(new DiffElement<E>(null, aEntityDest));
			}
		}
		//result.addAll(collInitCopy.stream().map((E aEntitySrc) -> new DiffElement(aEntitySrc,null)).collect(Collectors.toList()));
		return result;
	}
}

