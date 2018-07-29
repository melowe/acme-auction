package com.acme.auction;

import java.math.BigDecimal;
import java.time.Instant;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class BidTest {

    private final Instant executionTime = Instant.MIN;

    private final User user = User.Builder.create()
            .withId("USER1")
            .withName("Some user")
            .build();

    private final Item item = Item.Builder.create()
            .withId("ITEM1")
            .withDescription("Some item")
            .build();

    @Test
    public void createValidInstance() {

        final BigDecimal amount = new BigDecimal("99.67");

        final Bid bid = Bid.Builder.create()
                .withAmount(amount)
                .withItem(item)
                .withExecutionTime(executionTime)
                .withUser(user)
                .build();

        assertThat(bid).isNotNull();
        assertThat(bid.toString()).isNotEmpty();
        assertThat(bid.getAmount()).isSameAs(amount);
        assertThat(bid.getItem()).isSameAs(item);
        assertThat(bid.getExecutionTime()).isEqualTo(executionTime);
        assertThat(bid.getUser()).isSameAs(user);

    }

    @Test(expected = NullPointerException.class)
    public void createWithNoPropertesDefined() {
        Bid.Builder.create().build();
    }

    @Test
    public void bidsWithSamePropertiesAreEqual() {

        final Bid bid = Bid.Builder.create()
                .withItem(item)
                .withUser(user)
                .withExecutionTime(executionTime)
                .withAmount(BigDecimal.ONE)
                .build();

        final Bid otherBid = Bid.Builder.create()
                .withItem(item)
                .withUser(user)
                .withExecutionTime(executionTime)
                .withAmount(BigDecimal.ONE)
                .build();

        final Bid yetAnotherBid = Bid.Builder.create()
                .withItem(item)
                .withUser(user)
                .withExecutionTime(executionTime)
                .withAmount(BigDecimal.TEN)
                .build();

        assertThat(bid)
                .isEqualTo(otherBid)
                .isNotEqualTo(yetAnotherBid);

        assertThat(bid.hashCode()).isEqualTo(otherBid.hashCode());
        assertThat(bid).isSameAs(bid).isEqualTo(bid);
        //coverage: check null is not equal
        assertThat(bid).isNotEqualTo(null);
        //covergae: check different type is not equal
        assertThat(bid).isNotEqualTo(mock(Object.class));
    }

    @Test
    public void bidsWithDifferentAmountsAreNotEqual() {

        final Bid bid = Bid.Builder.create()
                .withItem(item)
                .withUser(user)
                .withExecutionTime(executionTime)
                .withAmount(BigDecimal.ONE)
                .build();

        final Bid otherBid = Bid.Builder.create()
                .withItem(item)
                .withUser(user)
                .withExecutionTime(executionTime)
                .withAmount(BigDecimal.TEN)
                .build();

        assertThat(bid).isNotEqualTo(otherBid);
    }

    @Test
    public void bidsWithDifferentExecutionTimesAreNotEqual() {

        BigDecimal amount = BigDecimal.ONE;

        final Bid bid = Bid.Builder.create()
                .withItem(item)
                .withUser(user)
                .withExecutionTime(executionTime)
                .withAmount(amount)
                .build();

        final Bid otherBid = Bid.Builder.create()
                .withItem(item)
                .withUser(user)
                .withExecutionTime(executionTime.plusSeconds(3L))
                .withAmount(amount)
                .build();

        assertThat(bid).isNotEqualTo(otherBid);
    }

    @Test
    public void bidsByDifferentUsersNotEqual() {

        BigDecimal amount = BigDecimal.ONE;

        final Bid bid = Bid.Builder.create()
                .withItem(item)
                .withUser(user)
                .withExecutionTime(executionTime)
                .withAmount(amount)
                .build();

        final Bid otherBid = Bid.Builder.create()
                .withItem(item)
                .withUser(mock(User.class))
                .withExecutionTime(executionTime)
                .withAmount(amount)
                .build();

        assertThat(bid).isNotEqualTo(otherBid);
    }

    @Test
    public void bidsForDifferentItemNotEqual() {

        BigDecimal amount = BigDecimal.ONE;

        final Bid bid = Bid.Builder.create()
                .withItem(item)
                .withUser(user)
                .withExecutionTime(executionTime)
                .withAmount(amount)
                .build();

        final Bid otherBid = Bid.Builder.create()
                .withItem(mock(Item.class))
                .withUser(user)
                .withExecutionTime(executionTime)
                .withAmount(amount)
                .build();

        assertThat(bid).isNotEqualTo(otherBid);
    }

}
