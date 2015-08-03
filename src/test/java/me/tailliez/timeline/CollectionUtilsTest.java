package me.tailliez.timeline;

import com.google.common.base.Objects;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiPredicate;

public class CollectionUtilsTest {

    public Collection<EntityOld> makeInitData() {
        Collection<EntityOld> list = new ArrayList<>();
        list.add(new EntityOld(1,1,100,199,1000));
        list.add(new EntityOld(2,1,200,299,1000));
        list.add(new EntityOld(3,1,300,399,1000));
        return list;
    }

    @Test
    public void testMerge() throws Exception {
        BiPredicate<EntityOld,EntityOld> serieEquality = (entity, entity2) -> Objects.equal(entity.getSerieId(),entity2.getSerieId());

        Collection<EntityOld> list = null;
        list = new ArrayList<>();
        list.add(new EntityOld(1,1,100,199,1000));
        list.add(new EntityOld(2,1,200,299,1000));

        //Assertions.assertThat(CollectionUtils.merge(makeInitData(),list,serieEquality, EntityOld::getStart, EntityOld::getEnd)).hasSize(2);

    }

    @Test
    public void testMissingElements() throws Exception {
        Assertions.assertThat(CollectionUtils.missingElements(makeInitData(), makeInitData())).hasSize(0);
        Collection<EntityOld> list = null;
        list = new ArrayList<>();
        list.add(new EntityOld(1,1,100,199,1000));
        list.add(new EntityOld(2, 1, 200, 299, 1000));
        Assertions.assertThat(CollectionUtils.missingElements(makeInitData(),list)).hasSize(1).containsAll(Arrays.asList(new EntityOld(3, 1, 300, 399, 1000)));
        Assertions.assertThat(CollectionUtils.missingElements(list, makeInitData())).hasSize(0);
    }
}