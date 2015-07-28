package me.tailliez.timeline;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RangeUtilsTest {

    private List<Entity2> srcColl;
    private Entity2 entity_10_19;
    private Entity2 entity_20_29;
    private Entity2 entity_30_39;
    private Entity2 entity_40___;
    private Entity2 entity_25;
    private Entity2 entity_45;
    private Entity2 entity_null_9;
    private Entity2 entity_20_29_serie_2;
    private Entity2 entity_30_39_serie_2;

    @Before
    public void setUp() throws Exception {
        srcColl = new ArrayList<>();
        entity_10_19 = new Entity2(1, 1, 10, 19, 1);
        entity_20_29 = new Entity2(2, 1, 20, 29, 2);
        entity_30_39 = new Entity2(3, 1, 30, 39, 3);
        entity_40___ = new Entity2(4, 1, 40, null, 4);
        entity_25 = new Entity2(null, 1, 25, null, 25);
        entity_45 = new Entity2(null, 1, 45, null, 45);
        entity_null_9 = new Entity2(null, 1, null, 9, 7);
        entity_20_29_serie_2 = new Entity2(22, 2, 20, 29, 22);
        entity_30_39_serie_2 = new Entity2(23, 2, 30, 39, 23);
    }

    @Test
    public void testAddNullRangeInEmpty() throws Exception {
        // Ajout vide dans liste vide
        RangeUtils.addRange(srcColl, null, Entity2::getRangeMetadata);
        Assertions.assertThat(srcColl).hasSize(0);
    }

    @Test
    public void testAddRangeInEmpty() throws Exception {
        // Ajout dans une liste vide
        RangeUtils.addRange(srcColl, entity_40___, Entity2::getRangeMetadata);
        Assertions.assertThat(srcColl).hasSize(1).containsOnly(entity_40___);
    }

    @Test
    public void testAddRangeInInvalid() throws Exception {
        srcColl.add(entity_null_9);
        Assertions.assertThatThrownBy(() -> {RangeUtils.addRange(srcColl, entity_25, Entity2::getRangeMetadata);}).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testAddRangeAfter() throws Exception {
        srcColl.add(entity_40___);
        RangeUtils.addRange(srcColl, entity_45, Entity2::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(2)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(4, 40, 44, 4), Tuple.tuple(null, 45, null, 45));
    }

    @Test
    public void testAddRangeBefore() throws Exception {
        srcColl.add(entity_40___);
        RangeUtils.addRange(srcColl, entity_25, Entity2::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(2)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(null, 25, 39, 25), Tuple.tuple(4, 40, null, 4));
    }

    @Test
    public void testAddRangeInRange() throws Exception {
        srcColl.add(entity_20_29);
        srcColl.add(entity_30_39);
        srcColl.add(entity_40___);
        RangeUtils.addRange(srcColl, entity_25, Entity2::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(4)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(2, 20, 24, 2), Tuple.tuple(null, 25, 29, 25), Tuple.tuple(3, 30, 39, 3), Tuple.tuple(4, 40, null, 4));
    }

    @Test
    public void testAddRangeInRangeWithSeries() throws Exception {
        srcColl.add(entity_20_29);
        srcColl.add(entity_30_39);
        srcColl.add(entity_40___);
        srcColl.add(entity_20_29_serie_2);
        srcColl.add(entity_30_39_serie_2);
        RangeUtils.addRange(srcColl, entity_25, Entity2::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(6)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(2, 20, 24, 2), Tuple.tuple(null, 25, 29, 25), Tuple.tuple(3, 30, 39, 3), Tuple.tuple(4, 40, null, 4),
                        Tuple.tuple(22, 20, 29, 22), Tuple.tuple(23, 30, 39, 23));
    }

}