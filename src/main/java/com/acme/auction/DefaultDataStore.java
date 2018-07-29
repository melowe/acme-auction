package com.acme.auction;

import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultDataStore implements DataStore {


    private final SortedSet<Bid> bids;

    public DefaultDataStore() {
        this(new ConcurrentSkipListSet<Bid>());
    }

    //For testing only
    protected DefaultDataStore(SortedSet<Bid> bids) {
        this.bids = Objects.requireNonNull(bids);
    }

    @Override
    public void saveBid(Bid bid) {
        bids.add(bid);
    }

    @Override
    public List<Bid> findBids(Predicate<Bid> predicate) {
        return bids.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
