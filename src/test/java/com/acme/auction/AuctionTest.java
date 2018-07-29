package com.acme.auction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AuctionTest {

    private Auction auction;

    private DataStore dataStore;

    public AuctionTest() {
    }

    @Before
    public void setUp() {
        this.dataStore = mock(DataStore.class);
        this.auction = new Auction(dataStore);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(dataStore);
    }

    @Test
    public void executeBid() {

        User user = mock(User.class);
        BigDecimal amount = new BigDecimal("99.99");
        Item item = mock(Item.class);

        List<Bid> results = new ArrayList<>();

        doAnswer((invocation) -> {
            results.add(invocation.getArgument(0));
            return null;
        }).when(dataStore).saveBid(any(Bid.class));

        Bid result = auction.executeBid(user, amount, item);

        assertThat(results).containsExactly(result);

        verify(dataStore).saveBid(any(Bid.class));
    }

    @Test
    public void findBidsForUserNoResults() {
        User user = mock(User.class);
        List<Bid> results = auction.findBidsForUser(user);

        assertThat(results).isEmpty();

        verify(dataStore).findBids(any(Predicate.class));

    }

    @Test
    public void findBidsForUser() {

        final Bid bid = mock(Bid.class);
        final User user = mock(User.class);

        when(dataStore.findBids(any(Predicate.class)))
                .thenReturn(Arrays.asList(bid));

        final List<Bid> results = auction.findBidsForUser(user);

        assertThat(results).containsExactly(bid);

        verify(dataStore).findBids(any(Predicate.class));

    }

    @Test
    public void findWinningBid() {

        Item item = Item.Builder.create()
                .withId("ITEM1")
                .withDescription("ITEM ONE DESC")
                .build();

        Instant baseExecutionTime = Instant.MAX;

        //given
        List<Bid> bids = Arrays.asList(
                Bid.Builder.create()
                        .withAmount(BigDecimal.ONE)
                        .withExecutionTime(baseExecutionTime)
                        .withItem(item)
                        .withUser(
                                User.Builder.create()
                                        .withId("USER1")
                                        .withName("User One")
                                        .build())
                        .build(),
                Bid.Builder.create()
                        .withAmount(BigDecimal.ONE.add(BigDecimal.TEN))
                        .withExecutionTime(baseExecutionTime.minusSeconds(5L))
                        .withItem(item)
                        .withUser(
                                User.Builder.create()
                                        .withId("USER2")
                                        .withName("User Two")
                                        .build())
                        .build(),
                Bid.Builder.create()
                        .withAmount(BigDecimal.ONE.multiply(BigDecimal.TEN))
                        .withExecutionTime(baseExecutionTime.minusSeconds(10L))
                        .withItem(item)
                        .withUser(
                                User.Builder.create()
                                        .withId("USER3")
                                        .withName("User Three")
                                        .build())
                        .build()
        );

        when(dataStore.findBids(any(Predicate.class))).thenReturn(bids);

        Optional<Bid> result = auction.findWinningBid(item);

        assertThat(result).isPresent();
        assertThat(result.get().getAmount())
                .isEqualTo(BigDecimal.ONE.add(BigDecimal.TEN));

        verify(dataStore).findBids(any(Predicate.class));

    }

    @Test
    public void findItemsBidOnByUser() {
        Item item = Item.Builder.create()
                .withId("ITEM1")
                .withDescription("ITEM ONE DESC")
                .build();

        Item otherItem = Item.Builder.create()
                .withId("ITEM2")
                .withDescription("ITEM TWO DESC")
                .build();

        Instant baseExecutionTime = Instant.MAX;

        User user = User.Builder.create()
                .withId("USER1")
                .withName("Some user").build();

        List<Bid> bids = Arrays.asList(
                Bid.Builder.create()
                        .withAmount(BigDecimal.ONE)
                        .withExecutionTime(baseExecutionTime)
                        .withItem(item)
                        .withUser(user)
                        .build(),
                Bid.Builder.create()
                        .withAmount(BigDecimal.ONE.add(BigDecimal.TEN))
                        .withExecutionTime(baseExecutionTime.minusSeconds(5L))
                        .withItem(item)
                        .withUser(
                                User.Builder.create()
                                        .withId("USER2")
                                        .withName("User Two")
                                        .build())
                        .build(),
                Bid.Builder.create()
                        .withAmount(BigDecimal.ONE.multiply(BigDecimal.TEN))
                        .withExecutionTime(baseExecutionTime.minusSeconds(10L))
                        .withItem(otherItem)
                        .withUser(user)
                        .build()
        );

        when(dataStore.findBids(any(Predicate.class))).thenReturn(bids);

        Set<Item> results = auction.findItemsBidOnByUser(user);

        assertThat(results).containsExactlyInAnyOrder(item,otherItem);
        
        verify(dataStore).findBids(any(Predicate.class));
        
    }
    
    

}
