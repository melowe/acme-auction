package com.acme.auction;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ItemTest {

    @Test
    public void createValid() {

        final String id = "SOMEID";
        final String description = "Some description";

        final Item item = Item.Builder.create()
                .withId(id)
                .withDescription(description)
                .build();

        assertThat(item.getId()).isEqualTo(id);
        assertThat(item.getDescription()).isEqualTo(description);

    }

    @Test
    public void itemsWithSameIdAreEqual() {

        final String id = "SOMEID";
        final String description = "Some description";

        final Item item = Item.Builder.create()
                .withId(id)
                .withDescription(description)
                .build();

        final Item otherItem = Item.Builder.create()
                .withId(id)
                .withDescription("Other description")
                .build();

        final Item yetAnotherItem = Item.Builder.create()
                .withId("OtherId")
                .withDescription(description)
                .build();

        assertThat(item)
                .isEqualTo(otherItem)
                .isNotEqualTo(yetAnotherItem);
        
        assertThat(item.hashCode()).isEqualTo(otherItem.hashCode());
        assertThat(item).isSameAs(item).isEqualTo(item);
        //coverage: check null is not equal
        assertThat(item).isNotEqualTo(null);
        //covergae: check different type is not equal
        assertThat(item).isNotEqualTo(mock(Object.class));
        
        assertThat(item).isEqualByComparingTo(otherItem);
        assertThat(item).isNotEqualByComparingTo(yetAnotherItem);
        
    }
    


}
