package me.tailliez.timeline;

import com.google.common.base.Objects;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiPredicate;

public class CollectionUtilsTest {

    public Collection<Entity> makeInitData() {
        Collection<Entity> list = new ArrayList<>();
        list.add(new Entity(1,1,100,199,1000));
        list.add(new Entity(2,1,200,299,1000));
        list.add(new Entity(3,1,300,399,1000));
        return list;
    }

    @Test
    public void testMerge() throws Exception {
        BiPredicate<Entity,Entity> serieEquality = (entity, entity2) -> Objects.equal(entity.getSerieId(),entity2.getSerieId());

        Collection<Entity> list = null;
        list = new ArrayList<>();
        list.add(new Entity(1,1,100,199,1000));
        list.add(new Entity(2,1,200,299,1000));

        //Assertions.assertThat(CollectionUtils.merge(makeInitData(),list,serieEquality, Entity::getStart, Entity::getEnd)).hasSize(2);

    }

    @Test
    public void testMissingElements() throws Exception {
        Assertions.assertThat(CollectionUtils.missingElements(makeInitData(), makeInitData())).hasSize(0);
        Collection<Entity> list = null;
        list = new ArrayList<>();
        list.add(new Entity(1,1,100,199,1000));
        list.add(new Entity(2, 1, 200, 299, 1000));
        Assertions.assertThat(CollectionUtils.missingElements(makeInitData(),list)).hasSize(1).containsAll(Arrays.asList(new Entity(3, 1, 300, 399, 1000)));
        Assertions.assertThat(CollectionUtils.missingElements(list, makeInitData())).hasSize(0);
    }
}