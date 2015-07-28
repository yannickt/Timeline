package me.tailliez.timeline;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RangeUtilsTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAddRangeInEmpty() throws Exception {
        Entity2 entity_25 = new Entity2(1,1,25,null,0);
        Entity2 entity_null_9 = new Entity2(1,1,null,9,0);

        List<Entity2> srcColl = new ArrayList<>();

        // Ajout vide dans collection vide
        RangeUtils.addRange(srcColl, null, Entity2::getRangeMetadata);
        Assertions.assertThat(srcColl).hasSize(0);

        // Ajout dans collection vide
        RangeUtils.addRange(srcColl, entity_25, Entity2::getRangeMetadata);
        Assertions.assertThat(srcColl).hasSize(1).containsOnly(entity_25);

        try {
            srcColl.add(entity_null_9);
            RangeUtils.addRange(srcColl, entity_25, Entity2::getRangeMetadata);
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (IllegalArgumentException e){
        }

    }
}