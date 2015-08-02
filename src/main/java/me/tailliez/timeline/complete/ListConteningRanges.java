package me.tailliez.timeline.complete;

import java.util.ArrayList;

public class ListConteningRanges<E> extends ArrayList<E> {

    public <E> ListConteningRanges<E> factory(Class<E> clazz, SerieDescriptor... descriptors) {
        return new ListConteningRanges<E>();
    }


}
