package com.acme.auction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultDataStoreTest {

    private DefaultDataStore defaultDataStore;

    private SortedSet<Bid> bids;

    @Before
    public void setUp() {
        bids = new ConcurrentSkipListSet<>();
        defaultDataStore = new DefaultDataStore(bids);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void saveBid() {
        
        Bid bid = Bid.Builder.create()
                .withAmount(BigDecimal.ONE)
                .withExecutionTime(Instant.EPOCH)
                .withItem(Item.Builder.create()
                        .withId("SOMEID")
                        .withDescription("Some desc").build())
                .withUser(User.Builder.create()
                        .withId("USER1")
                        .withName("Some User").build())
                .build();
  
        defaultDataStore.saveBid(bid);

        assertThat(bids)
                .containsOnly(bid);

    }

    @Test
    public void findBidsForUser() {

        //Given
        User user = mock(User.class);

        User otherUser = mock(User.class);

        Item item = mock(Item.class);

        Bid first = mock(Bid.class);
        when(first.getUser()).thenReturn(user);

        Bid second = mock(Bid.class);
        when(second.getUser()).thenReturn(user);

        Bid third = mock(Bid.class);
        when(third.getUser()).thenReturn(otherUser);

        Bid forth = mock(Bid.class);
        when(forth.getUser()).thenReturn(user);

        bids.add(first);
        bids.add(second);
        bids.add(third);
        bids.add(forth);

        //when/then
        List<Bid> results = defaultDataStore
                .findBids(b -> b.getUser().equals(user));

        assertThat(results)
                .containsExactlyInAnyOrder(first, second, forth);

        List<Bid> resultsForOtherUser = defaultDataStore
                .findBids(b -> b.getUser().equals(otherUser));

        assertThat(resultsForOtherUser)
                .containsExactly(third);

    }

   

    @Test
    public void createDefaultInstance() {
        DefaultDataStore instance = new DefaultDataStore();

        assertThat(instance).isNotNull();

    }

}
