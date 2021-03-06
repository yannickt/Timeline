package me.tailliez.timeline;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RangeUtilsTest {

    private List<Entity> srcColl;
    private Entity entity_10_20;
    private Entity entity_20_30;
    private Entity entity_30_40;
    private Entity entity_40___;
    private Entity entity_25;
    private Entity entity_45;
    private Entity entity_null_9;
    private Entity entity_20_30_serie_2;
    private Entity entity_30_40_serie_2;

    @Before
    public void setUp() throws Exception {
        srcColl = new ArrayList<>();
        entity_10_20 = new Entity(1, 1, 10, 20, 1);
        entity_20_30 = new Entity(2, 1, 20, 30, 2);
        entity_30_40 = new Entity(3, 1, 30, 40, 3);
        entity_40___ = new Entity(4, 1, 40, null, 4);
        entity_25 = new Entity(null, 1, 25, null, 25);
        entity_45 = new Entity(null, 1, 45, null, 45);
        entity_null_9 = new Entity(null, 1, null, 9, 7);
        entity_20_30_serie_2 = new Entity(22, 2, 20, 30, 22);
        entity_30_40_serie_2 = new Entity(23, 2, 30, 40, 23);
    }

    @Test
    public void testAddNullRangeInEmpty() throws Exception {
        // Ajout vide dans liste vide
        RangeUtils.addRange(srcColl, null, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl).hasSize(0);
    }

    @Test
    public void testAddRangeInEmpty() throws Exception {
        // Ajout dans une liste vide
        RangeUtils.addRange(srcColl, entity_40___, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl).hasSize(1).containsOnly(entity_40___);
    }

    @Test
    public void testAddRangeInInvalid() throws Exception {
        srcColl.add(entity_null_9);
        Assertions.assertThatThrownBy(() -> {
            RangeUtils.addRange(srcColl, entity_25, Entity::getRangeMetadata);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testAddRangeAfter() throws Exception {
        srcColl.add(entity_40___);
        RangeUtils.addRange(srcColl, entity_45, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(2)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(4, 40, 45, 4), Tuple.tuple(null, 45, null, 45));
    }

    @Test
    public void testAddRangeBefore() throws Exception {
        srcColl.add(entity_40___);
        RangeUtils.addRange(srcColl, entity_25, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(2)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(null, 25, 40, 25), Tuple.tuple(4, 40, null, 4));
    }

    @Test
    public void testAddRangeInRange() throws Exception {
        srcColl.add(entity_20_30);
        srcColl.add(entity_30_40);
        srcColl.add(entity_40___);
        RangeUtils.addRange(srcColl, entity_25, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(4)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(2, 20, 25, 2), Tuple.tuple(null, 25, 30, 25), Tuple.tuple(3, 30, 40, 3), Tuple.tuple(4, 40, null, 4));
    }

    @Test
    public void testAddRangeInRangeWithSeries() throws Exception {
        srcColl.add(entity_20_30);
        srcColl.add(entity_30_40);
        srcColl.add(entity_40___);
        srcColl.add(entity_20_30_serie_2);
        srcColl.add(entity_30_40_serie_2);
        RangeUtils.addRange(srcColl, entity_25, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(6)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(2, 20, 25, 2), Tuple.tuple(null, 25, 30, 25), Tuple.tuple(3, 30, 40, 3), Tuple.tuple(4, 40, null, 4),
                        Tuple.tuple(22, 20, 30, 22), Tuple.tuple(23, 30, 40, 23));
    }

    @Test
    public void testRemoveFirstRange() throws Exception {
        srcColl.add(entity_30_40);
        srcColl.add(entity_40___);
        RangeUtils.removeRange(srcColl, entity_30_40, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(1)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(4, 40, null, 4));
    }

    @Test
    public void testRemoveLastRange() throws Exception {
        srcColl.add(entity_30_40);
        srcColl.add(entity_40___);
        RangeUtils.removeRange(srcColl, entity_40___, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(1)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(3, 30, null, 3));
    }

    @Test
    public void testRemoveBetweenRange() throws Exception {
        srcColl.add(entity_20_30);
        srcColl.add(entity_30_40);
        srcColl.add(entity_40___);
        RangeUtils.removeRange(srcColl, entity_30_40, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(2)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(2, 20, 40, 2), Tuple.tuple(4, 40, null, 4));
    }

    @Test
    public void testRemoveRangeInSerie() throws Exception {
        srcColl.add(entity_20_30);
        srcColl.add(entity_30_40);
        srcColl.add(entity_40___);
        srcColl.add(entity_20_30_serie_2);
        srcColl.add(entity_30_40_serie_2);
        RangeUtils.removeRange(srcColl, entity_30_40, Entity::getRangeMetadata);
        Assertions.assertThat(srcColl)
                .hasSize(4)
                .extracting("id", "range.begin", "range.end", "data")
                .containsOnly(Tuple.tuple(2, 20, 40, 2), Tuple.tuple(4, 40, null, 4),
                        Tuple.tuple(22, 20, 30, 22), Tuple.tuple(23, 30, 40, 23));
    }

    @Test
    public void testCheckRangeConsecutiveWithZero() {
        srcColl.add(entity_20_30);
        Assertions.assertThat(RangeUtils.checkRangeConsecutive(srcColl, Entity::getRangeMetadata)).isEqualTo(true);
    }

    @Test
    public void testCheckRangeConsecutiveWithOne() {
        srcColl.add(entity_20_30);
        Assertions.assertThat(RangeUtils.checkRangeConsecutive(srcColl, Entity::getRangeMetadata)).isEqualTo(true);
    }

    @Test
    public void testCheckRangeConsecutiveWithTwoConsecutive() {
        srcColl.add(entity_10_20);
        srcColl.add(entity_20_30);
        Assertions.assertThat(RangeUtils.checkRangeConsecutive(srcColl, Entity::getRangeMetadata)).isEqualTo(true);
    }

    @Test
    public void testCheckRangeConsecutiveWithTwoUnconsecutive() {
        srcColl.add(entity_10_20);
        srcColl.add(entity_30_40);
        Assertions.assertThat(RangeUtils.checkRangeConsecutive(srcColl, Entity::getRangeMetadata)).isEqualTo(false);
    }

    @Test
    public void testCheckRangeConsecutiveWithTwoConsecutiveTwoSerie() {
        srcColl.add(entity_10_20);
        srcColl.add(entity_20_30);
        srcColl.add(entity_20_30_serie_2);
        Assertions.assertThat(RangeUtils.checkRangeConsecutive(srcColl, Entity::getRangeMetadata)).isEqualTo(true);
        srcColl.add(entity_30_40_serie_2);
        Assertions.assertThat(RangeUtils.checkRangeConsecutive(srcColl, Entity::getRangeMetadata)).isEqualTo(true);
    }

    @Test
    public void testCheckRangeConsecutiveWithTwoUnconsecutiveTwoSerie() {
        srcColl.add(entity_10_20);
        srcColl.add(entity_30_40);
        srcColl.add(entity_20_30_serie_2);
        srcColl.add(entity_30_40_serie_2);
        Assertions.assertThat(RangeUtils.checkRangeConsecutive(srcColl, Entity::getRangeMetadata)).isEqualTo(false);
    }

}